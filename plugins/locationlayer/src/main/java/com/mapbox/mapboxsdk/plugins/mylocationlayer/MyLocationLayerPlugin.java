package com.mapbox.mapboxsdk.plugins.mylocationlayer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

/**
 * The My Location layer plugin provides location awareness to your mobile application. Enabling this plugin provides a
 * contextual experience to your users by showing an icon representing the users current location. A few different modes
 * are offered to provide the right context to your users at the correct time. {@link MyLocationLayerMode#TRACKING}
 * simply shows the users location on the map represented as a dot. {@link MyLocationLayerMode#COMPASS} mode allows you
 * to display an arrow icon (by default) that points in the direction the device is pointing in.
 * {@link MyLocationLayerMode#NAVIGATION} can be used in conjunction with our Navigation SDK to display a larger icon we
 * call the user puck. Lastly, {@link MyLocationLayerMode#NONE} can be used to disable the Location Layer but keep the
 * instance around till the activity is destroyed.
 * <p>
 * Using this plugin requires you to request permission beforehand manually or using
 * {@link com.mapbox.services.android.telemetry.permissions.PermissionsManager}. Either {@code ACCESS_COARSE_LOCATION}
 * or {@code ACCESS_FINE_LOCATION} permissions can be requested and this plugin work as expected.
 *
 * @since 0.1.0
 */
@SuppressWarnings( {"MissingPermission"})
public class MyLocationLayerPlugin implements LocationEngineListener, CompassListener {

  private static final int ACCURACY_CIRCLE_STEPS = 48;

  private MyLocationLayerOptions options;
  private CompassManager compassListener;
  private LocationEngine locationSource;
  private MapboxMap mapboxMap;
  private MapView mapView;

  // Enabled booleans
  @MyLocationLayerMode.Mode
  private int myLocationLayerMode;

  // Previous compass and location values
  private float previousMagneticHeading;
  private Location previousLocation;

  // Animators
  private ValueAnimator locationChangeAnimator;
  private ValueAnimator bearingChangeAnimator;

  private long locationUpdateTimestamp;
  private boolean linearAnimation;

  /**
   * Construct a {@code MyLocationLayerPlugin}
   *
   * @param mapView   the MapView to apply the My Location layer plugin to
   * @param mapboxMap the MapboxMap to apply the My Location layer plugin with
   * @since 0.1.0
   */
  public MyLocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapView = mapView;
    this.mapboxMap = mapboxMap;

    myLocationLayerMode = MyLocationLayerMode.NONE;
    options = new MyLocationLayerOptions(this, mapView, mapboxMap);
    compassListener = new CompassManager(mapView.getContext(), this);
  }

  /**
   * Get the current {@link MyLocationLayerOptions} object which can be used to customize the look of the location icon.
   *
   * @return the {@link MyLocationLayerOptions} object this My Location layer plugin instance's using
   * @since 0.1.0
   */
  public MyLocationLayerOptions getMyLocationLayerOptions() {
    return options;
  }

  /**
   * After creating an instance of this plugin, you can use this API to enable the location mode of your choice. These
   * modes can be found in the {@link MyLocationLayerMode} class and the parameter only accepts one of those modes. Note
   * that before enabling the My Location layer, you will need to ensure that you have the requested the required user
   * location permissions.
   * <p>
   * <ul>
   * <li>{@link MyLocationLayerMode#TRACKING}: Display the user location on the map as a small dot</li>
   * <li>{@link MyLocationLayerMode#COMPASS}: Display the user location and current heading/bearing</li>
   * <li>{@link MyLocationLayerMode#NAVIGATION}: Display the user location on the map using a navigation icon</li>
   * <li>{@link MyLocationLayerMode#NONE}: Disable user location showing on the map</li>
   * </ul>
   *
   * @param myLocationLayerMode one of the modes found in {@link MyLocationLayerMode}
   * @since 0.1.0
   */
  public void setMyLocationEnabled(@MyLocationLayerMode.Mode int myLocationLayerMode) {
    if (locationSource == null) {
      locationSource = LocationSource.getLocationEngine(mapView.getContext());
    }

    if (myLocationLayerMode != MyLocationLayerMode.NONE) {
      enableLocationUpdates();

      options.initialize();

      // Set an initial location if one available
      Location lastLocation = locationSource.getLastLocation();

      if (lastLocation != null) {
        setLocation(lastLocation);
        setAccuracy(lastLocation);
      }

      if (myLocationLayerMode == MyLocationLayerMode.COMPASS) {
        setNavigationEnabled(false);
        setMyBearingEnabled(true);
      } else if (myLocationLayerMode == MyLocationLayerMode.NAVIGATION) {
        setMyBearingEnabled(false);
        setNavigationEnabled(true);
      } else if (myLocationLayerMode == MyLocationLayerMode.TRACKING) {
        setMyBearingEnabled(false);
        setNavigationEnabled(false);
      }
    } else {
      // Check that the mode isn't already none
      if (this.myLocationLayerMode != MyLocationLayerMode.NONE) {
        disableLocationUpdates();

        options.removeLayerAndSources();
      }
    }
    this.myLocationLayerMode = myLocationLayerMode;
  }

  /**
   * Returns the current location mode being used with this plugin.
   *
   * @return on of the {@link MyLocationLayerMode} values
   * @since 0.1.0
   */
  public int getMyLocationMode() {
    return myLocationLayerMode;
  }

  /**
   * Required to place inside your activities {@code onStop} method.
   *
   * @since 0.1.0
   */
  public void onStop() {
    if (locationChangeAnimator != null) {
      locationChangeAnimator.end();
      locationChangeAnimator = null;
    }

    if (bearingChangeAnimator != null) {
      bearingChangeAnimator.end();
      bearingChangeAnimator = null;
    }

    if (myLocationLayerMode == MyLocationLayerMode.COMPASS && compassListener.isSensorAvailable()) {
      compassListener.onStop();
    }

    if (mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_LAYER) != null) {
      disableLocationUpdates();
    }
  }

  /**
   * Required to place inside your activities {@code onStart} method. You'll also most likely want to check that this
   * Location Layer plugin instance inside your activity is null or not.
   *
   * @since 0.1.0
   */
  public void onStart() {
    if (myLocationLayerMode != MyLocationLayerMode.NONE) {
      setMyLocationEnabled(myLocationLayerMode);
    }

    if (myLocationLayerMode == MyLocationLayerMode.COMPASS && compassListener.isSensorAvailable()) {
      compassListener.onStart();
    }
  }

  /**
   * Check whether the location update animator is using a linear or an accelerate/decelerate interpolator. When the
   * navigation mode is being used, the animator automatically become linear.
   *
   * @return boolean true if the location update animator is set to linear, otherwise false
   * @since 0.1.0
   */
  public boolean isLinearAnimation() {
    return linearAnimation;
  }

  /**
   * Set whether the location update animator is using linear (true) or an accelerate/decelerate (false) interpolator.
   * When the navigation mode is being used, the animator automatically become linear.
   *
   * @param linearAnimation boolean true if you'd like to set the location update animator to linear, otherwise false
   * @since 0.1.0
   */
  public void setLinearAnimation(boolean linearAnimation) {
    this.linearAnimation = linearAnimation;
  }

  @Override
  public void onConnected() {
    locationSource.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    if (location == null) {
      locationUpdateTimestamp = SystemClock.elapsedRealtime();
      return;
    }
    if (myLocationLayerMode == MyLocationLayerMode.NAVIGATION) {
      bearingChangeAnimate(location.getBearing());
    } else {
      setAccuracy(location);
    }
    setLocation(location);
  }

  @Override
  public void onCompassChanged(float magneticHeading) {
    bearingChangeAnimate(magneticHeading);
  }

  /**
   * Used internally in this class to disable Location Updates.
   *
   * @since 0.1.0
   */
  private void disableLocationUpdates() {
    previousLocation = null;
    if (locationSource != null) {
      locationSource.removeLocationEngineListener(this);
      locationSource.removeLocationUpdates();
      locationSource.deactivate();
    }
  }

  /**
   * Used internally in this class to enable Location Updates.
   *
   * @since 0.1.0
   */
  private void enableLocationUpdates() {
    if (locationSource != null) {
      locationSource.addLocationEngineListener(this);
      locationSource.setPriority(LocationEnginePriority.HIGH_ACCURACY);
      locationSource.activate();
    }
  }

  /**
   * Enable or disable the My Location bearing by passing in a boolean here. Once enabled, The users location and
   * bearing's indicated on the map by default as a small blue dot with a chevron pointing in the direction of the
   * devices compass bearing.
   *
   * @param bearingEnabled boolean true if you'd like to enable the user location bearing, otherwise, false will disable
   * @since 0.1.0
   */
  private void setMyBearingEnabled(boolean bearingEnabled) {
    Layer layer = mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_LAYER);
    if (layer != null) {
      layer.setProperties(
        PropertyFactory.iconImage(bearingEnabled ? MyLocationLayerConstants.USER_LOCATION_BEARING_ICON
          : MyLocationLayerConstants.USER_LOCATION_ICON)
      );
    }
    if (bearingEnabled) {
      compassListener.onStart();
    } else {
      compassListener.onStop();
    }
  }

  /**
   * Enable or disable the My Location navigation by passing in a boolean here. Once enabled, The users location
   * indicated on the map will show (by default) as a large navigation puck with a cheveron/arrow showing the users GPS
   * location bearing.
   *
   * @param navigationEnabled boolean true if you'd like to enable the user location navigation, otherwise, false will
   *                          disable
   * @since 0.1.0
   */
  private void setNavigationEnabled(boolean navigationEnabled) {
    Layer layer = mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_LAYER);
    if (layer != null) {
      layer.setProperties(
        PropertyFactory.iconImage(navigationEnabled ? MyLocationLayerConstants.USER_LOCATION_PUCK_ICON
          : MyLocationLayerConstants.USER_LOCATION_ICON)
      );
    }

    setLinearAnimation(true);

    Layer textAnnotation = mapboxMap.getLayer(MyLocationLayerConstants.NAVIGATION_ANNOTATION_LAYER);
    if (textAnnotation != null) {
      if (!navigationEnabled) {
        mapboxMap.removeLayer(MyLocationLayerConstants.NAVIGATION_ANNOTATION_LAYER);
      } else {
        options.setLocationTextAnnotation(options.getLocationTextAnnotation());
      }
    }

    Layer accuracyLayer = mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_ACCURACY_LAYER);
    if (accuracyLayer != null) {
      accuracyLayer.setProperties(
        PropertyFactory.visibility(navigationEnabled ? Property.NONE : Property.VISIBLE)
      );
    }
  }

  /**
   * Using the {@link Location#getAccuracy()} provided when a new location comes in, the source is updated to reflect
   * the new value
   *
   * @param location the latest user location
   * @since 0.1.0
   */
  private void setAccuracy(Location location) {
    // TODO replace fill-layer with circle-layer once circle-pitch-alignment is supported in Runtime Styling
    // https://github.com/mapbox/mapbox-gl-js/issues/4120
    Position userPosition = Position.fromLngLat(location.getLongitude(), location.getLatitude());
    Polygon accuracyCircle = TurfTransformation.circle(
      userPosition, location.getAccuracy(), ACCURACY_CIRCLE_STEPS, TurfConstants.UNIT_METERS);

    // Create new GeoJson object using the new accuracy circle created.
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(
      new Feature[] {Feature.fromGeometry(accuracyCircle)}
    );

    // Update the location accuracy source with new GeoJson object
    GeoJsonSource locationGeoJsonSource = mapboxMap.getSourceAs(MyLocationLayerConstants.LOCATION_ACCURACY_SOURCE);
    if (locationGeoJsonSource != null) {
      locationGeoJsonSource.setGeoJson(featureCollection);
    }
  }

  /**
   * Updates the user location icon.
   *
   * @param location the latest user location
   * @since 0.1.0
   */
  private void setLocation(final Location location) {
    GeoJsonSource locationGeoJsonSource = mapboxMap.getSourceAs(MyLocationLayerConstants.LOCATION_SOURCE);

    if (previousLocation == null) {
      // Create new GeoJson object with the new user location
      FeatureCollection locationSourceFeature = FeatureCollection.fromFeatures(
        new Feature[] {Feature.fromGeometry(Point.fromCoordinates(
          new double[] {location.getLongitude(), location.getLatitude()})
        )}
      );
      if (locationGeoJsonSource != null) {
        locationGeoJsonSource.setGeoJson(locationSourceFeature);
      }
      previousLocation = location;
      return;
    }

    locationChangeAnimate(locationGeoJsonSource, location);
    previousLocation = location;
  }

  /**
   * Handles the animation from previous user location to the current.
   *
   * @param locationGeoJsonSource the location source being animated
   * @param location              the latest user location
   * @since 0.1.0
   */
  private void locationChangeAnimate(final GeoJsonSource locationGeoJsonSource, Location location) {
    if (locationChangeAnimator != null) {
      locationChangeAnimator.end();
    }

    locationChangeAnimator = ValueAnimator.ofObject(new PointEvaluator(),
      Point.fromCoordinates(new double[] {previousLocation.getLongitude(), previousLocation.getLatitude()}),
      Point.fromCoordinates(new double[] {location.getLongitude(), location.getLatitude()})
    );

    locationChangeAnimator.setDuration(linearAnimation ? getLocationUpdateDuration()
      : MyLocationLayerConstants.LOCATION_UPDATE_DELAY_MS);
    if (linearAnimation) {
      locationChangeAnimator.setInterpolator(new LinearInterpolator());
    } else {
      locationChangeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    locationChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        if (locationGeoJsonSource != null) {
          locationGeoJsonSource.setGeoJson(FeatureCollection.fromFeatures(
            new Feature[] {Feature.fromGeometry((Point) animation.getAnimatedValue())}
          ));
        }
      }
    });
    locationChangeAnimator.start();
  }

  /**
   * Internal method being used to calculate the time duration for the location change animator.
   *
   * @return millisecond time value as a long value
   * @since 0.1.0
   */
  private long getLocationUpdateDuration() {
    // calculate updateLatLng time + add some extra offset to improve animation
    long previousUpdateTimeStamp = locationUpdateTimestamp;
    locationUpdateTimestamp = SystemClock.elapsedRealtime();
    return (long) ((locationUpdateTimestamp - previousUpdateTimeStamp) * 1.2f);
  }

  /**
   * Handles the animation from the previous user bearing to the current.
   *
   * @param magneticHeading the raw compass heading
   * @since 0.1.0
   */
  private void bearingChangeAnimate(float magneticHeading) {
    if (bearingChangeAnimator != null) {
      previousMagneticHeading = (Float) bearingChangeAnimator.getAnimatedValue();
      bearingChangeAnimator.end();
      bearingChangeAnimator = null;
    }

    // No visible change occurred
    if (Math.abs(magneticHeading - previousMagneticHeading) < 1) {
      return;
    }
    // Make sure the icon is always rotated the shortest distance.
    float diff = previousMagneticHeading - magneticHeading;
    if (diff > 180.0f) {
      magneticHeading += 360.0f;
    } else if (diff < -180.0f) {
      magneticHeading -= 360.f;
    }
    bearingChangeAnimator = ValueAnimator.ofFloat(previousMagneticHeading, magneticHeading);
    bearingChangeAnimator.setDuration(MyLocationLayerConstants.COMPASS_UPDATE_RATE_MS);
    bearingChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Layer locationLayer = mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_LAYER);
        if (locationLayer != null) {
          locationLayer.setProperties(
            PropertyFactory.iconRotate((float) valueAnimator.getAnimatedValue())
          );
        }
      }
    });
    bearingChangeAnimator.start();
    previousMagneticHeading = magneticHeading;
  }

  /**
   * Used for animating the user location icon
   *
   * @since 0.1.0
   */
  private static class PointEvaluator implements TypeEvaluator<Point> {
    // Method is used to interpolate the user icon animation.
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
      return Point.fromCoordinates(new double[] {
        startValue.getCoordinates().getLongitude()
          + ((endValue.getCoordinates().getLongitude() - startValue.getCoordinates().getLongitude()) * fraction),
        startValue.getCoordinates().getLatitude()
          + ((endValue.getCoordinates().getLatitude() - startValue.getCoordinates().getLatitude()) * fraction)
      });
    }
  }
}
