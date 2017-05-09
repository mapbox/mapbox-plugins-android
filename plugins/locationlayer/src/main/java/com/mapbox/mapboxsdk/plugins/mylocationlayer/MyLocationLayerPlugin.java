package com.mapbox.mapboxsdk.plugins.mylocationlayer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
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
 * contextual experience to your users by showing an icon representing the users current location. Enabling bearing
 * tracking allows you to display an arrow icon (by default) that points in the direction the device is pointing in.
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
  private boolean locationEnabled;
  private boolean bearingEnabled;

  // Previous compass and location values
  private float previousMagneticHeading;
  private Location previousLocation;

  // Animators
  private ValueAnimator locationChangeAnimator;
  private ValueAnimator bearingChangeAnimator;

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
   * Enable or disable the My Location layer by passing in a boolean here. If locationEnabled, the users current
   * position will be displayed on the map. Note that before enabling the My Location layer, you will need to ensure
   * that you have the requested the required user location permissions.
   * <p>
   * The location is indicated on the map by default as a small blue dot if the device is stationary, or as a chevron
   * if the you enabled {@link MyLocationLayerPlugin#bearingEnabled}.
   *
   * @param enable boolean true if you'd like to enable the user location, otherwise, false will disable
   * @since 0.1.0
   */
  public void setMyLocationEnabled(boolean enable) {
    this.locationEnabled = enable;
    if (locationSource == null) {
      locationSource = LocationSource.getLocationEngine(mapView.getContext());
    }

    if (enable) {
      locationSource.addLocationEngineListener(this);
      locationSource.setPriority(LocationEnginePriority.HIGH_ACCURACY);
      locationSource.activate();

      // Set an initial location if one available
      Location lastLocation = locationSource.getLastLocation();

      if (lastLocation != null) {
        setLocation(lastLocation);
        setAccuracy(lastLocation);
      }
    } else {
      // Location locationEnabled has been set to false
      previousLocation = null;
      locationSource.removeLocationEngineListener(this);
      locationSource.removeLocationUpdates();
      locationSource.deactivate();
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
  public void setMyBearingEnabled(boolean bearingEnabled) {
    this.bearingEnabled = bearingEnabled;
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
   * Returns true if the My Location layer is currently enabled.
   *
   * @return true if the location layer's enabled, false otherwise
   * @since 0.1.0
   */
  public boolean isMyLocationEnabled() {
    return locationEnabled;
  }

  /**
   * Returns true if the My Location bearing layer is currently enabled.
   *
   * @return true if the location bearing layer's enabled, false otherwise
   * @since 0.1.0
   */
  public boolean isMyBearingEnabled() {
    return bearingEnabled;
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

    if (bearingEnabled && compassListener.isSensorAvailable()) {
      compassListener.onStop();
    }

    if (locationSource != null) {
      setMyLocationEnabled(false);
    }
  }

  /**
   * Required to place inside your activities {@code onStart} method.
   *
   * @since 0.1.0
   */
  public void onStart() {
    if (isMyLocationEnabled()) {
      setMyLocationEnabled(true);
    }
    if (bearingEnabled && compassListener.isSensorAvailable()) {
      compassListener.onStart();
    }
  }

  @Override
  public void onConnected() {
    locationSource.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    if (location == null) {
      return;
    }
    setAccuracy(location);
    setLocation(location);
  }

  @Override
  public void onCompassChanged(float magneticHeading) {
    bearingChangeAnimate(magneticHeading);
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
    locationChangeAnimator.setDuration(500);
    locationChangeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
