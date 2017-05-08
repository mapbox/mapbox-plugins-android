package com.mapbox.mapboxsdk.plugins.mylocationlayer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
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

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotationAlignment;

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
public class MyLocationLayerPlugin implements LocationEngineListener, MapView.OnMapChangedListener {

  private static final int ACCURACY_CIRCLE_STEPS = 48;

  private Context context;
  private Location previousLocation;
  private MapboxMap mapboxMap;
  private LocationEngine locationSource;
  private ValueAnimator locationChangeAnimator;
  private boolean enabled;

  /**
   * Construct a {@code MyLocationLayerPlugin}
   *
   * @param mapView   the MapView to apply the My Location layer plugin to
   * @param mapboxMap the MapboxMap to apply the My Location layer plugin with
   * @since 0.1.0
   */
  public MyLocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.context = mapView.getContext();
    this.mapboxMap = mapboxMap;

    mapView.addOnMapChangedListener(this);
    initSourceAndLayers();
  }

  // TODO link to bearing tracking once added in javadoc

  /**
   * Enable or disable the My Location layer by passing in a boolean here. If enabled, the users current position will
   * be displayed on the map. Note that before enabling the My Location layer, you will need to ensure that you have
   * the requested the required user location permissions.
   * <p>
   * The location is indicated on the map by default as a small blue dot if the device is stationary, or as a chevron
   * if the you enabled bearing tracking.
   *
   * @param enable boolean true if you'd like to enable the user location, otherwise, false will disable
   * @since 0.1.0
   */
  public void setMyLocationEnabled(boolean enable) {
    this.enabled = enable;
    if (locationSource == null) {
      locationSource = LocationSource.getLocationEngine(context);
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
      // Location enabled has been set to false
      previousLocation = null;
      locationSource.removeLocationEngineListener(this);
      locationSource.removeLocationUpdates();
      locationSource.deactivate();
    }
  }

  /**
   * Returns true if the My Location layer plugin is currently enabled.
   *
   * @return true if enabled, false otherwise
   * @since 0.1.0
   */
  public boolean isEnabled() {
    return enabled;
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

    if (locationSource != null) {
      setMyLocationEnabled(false);
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
    GeoJsonSource locationGeoJsonSource = mapboxMap.getSourceAs(LocationViewSources.LOCATION_ACCURACY_SOURCE);
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
    GeoJsonSource locationGeoJsonSource = mapboxMap.getSourceAs(LocationViewSources.LOCATION_SOURCE);

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

  /*
   * Overriding to handle when the map style changes.
   */
  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_LOADING_STYLE && isEnabled()) {
      initSourceAndLayers();
    }
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

  private void initSourceAndLayers() {
    mapboxMap.addImage(LocationViewIcons.USER_LOCATION_ICON, getBitmapFromDrawable(context, R.drawable.mapbox_user_icon));
    mapboxMap.addImage(LocationViewIcons.USER_LOCATION_STROKE_ICON, getBitmapFromDrawable(context, R.drawable.mapbox_user_stroke_icon));

    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});
    GeoJsonSource locationSource = new GeoJsonSource(LocationViewSources.LOCATION_SOURCE, emptyFeature);
    mapboxMap.addSource(locationSource);

    GeoJsonSource locationAccuracySource = new GeoJsonSource(LocationViewSources.LOCATION_ACCURACY_SOURCE, emptyFeature);
    mapboxMap.addSource(locationAccuracySource);

    SymbolLayer locationLayer = new SymbolLayer(LocationViewLayers.LOCATION_LAYER, LocationViewSources.LOCATION_SOURCE).withProperties(
//      iconImage(myBearingTrackingMode == MyBearingTracking.NONE ? LocationViewIcons.USER_LOCATION_ICON : USER_LOCATION_BEARING_ICON),
      iconImage(LocationViewIcons.USER_LOCATION_ICON),
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
    mapboxMap.addLayer(locationLayer);

    SymbolLayer locationStrokeLayer = new SymbolLayer(LocationViewLayers.LOCATION_STROKE_LAYER, LocationViewSources.LOCATION_SOURCE).withProperties(
//      iconImage(myBearingTrackingMode == MyBearingTracking.NONE ? LocationViewIcons.USER_LOCATION_ICON : USER_LOCATION_BEARING_ICON),
      iconImage(LocationViewIcons.USER_LOCATION_STROKE_ICON),
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
    mapboxMap.addLayerBelow(locationStrokeLayer, LocationViewLayers.LOCATION_LAYER);

    FillLayer locationAccuracyLayer = new FillLayer(LocationViewLayers.LOCATION_ACCURACY_LAYER, LocationViewSources.LOCATION_ACCURACY_SOURCE).withProperties(
      PropertyFactory.fillColor(Color.parseColor("#4882C6")),
      PropertyFactory.fillOpacity(0.15f)
    );
    mapboxMap.addLayerBelow(locationAccuracyLayer, LocationViewLayers.LOCATION_STROKE_LAYER);
  }


  private static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);

    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);

      return bitmap;
    } else {
      throw new IllegalArgumentException("unsupported drawable type");
    }
  }

  private static class LocationViewSources {
    private static final String LOCATION_SOURCE = "location-source";
    private static final String LOCATION_ACCURACY_SOURCE = "location-accuracy-source";
  }

  private static class LocationViewLayers {
    private static final String LOCATION_LAYER = "location-layer";
    private static final String LOCATION_STROKE_LAYER = "location-stroke-layer";
    private static final String LOCATION_ACCURACY_LAYER = "location-accuracy-layer";
  }

  private static class LocationViewIcons {
    private static final String USER_LOCATION_ICON = "user-location-icon";
    private static final String USER_LOCATION_STROKE_ICON = "user-location-stroke-icon";
  }
}
