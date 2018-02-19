package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.ValueAnimator;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDelegate;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapView.OnMapChangedListener;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraMoveListener;
import com.mapbox.mapboxsdk.maps.MapboxMap.OnMapClickListener;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.commons.geojson.Point;

import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.ACCURACY_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BEARING_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.COMPASS_UPDATE_RATE_MS;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.MAX_ANIMATION_DURATION_MS;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.NAVIGATION_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.shortestRotation;

/**
 * The Location layer plugin provides location awareness to your mobile application. Enabling this
 * plugin provides a contextual experience to your users by showing an icon representing the users
 * current location. A few different modes are offered to provide the right context to your users at
 * the correct time. {@link LocationLayerMode#TRACKING} simply shows the users location on the map
 * represented as a dot. {@link LocationLayerMode#COMPASS} mode allows you to display an arrow icon
 * (by default) that points in the direction the device is pointing in.
 * {@link LocationLayerMode#NAVIGATION} can be used in conjunction with our Navigation SDK to
 * display a larger icon we call the user puck. Lastly, {@link LocationLayerMode#NONE} can be used
 * to disable the Location Layer but keep the instance around till the activity is destroyed.
 * <p>
 * Using this plugin requires you to request permission beforehand manually or using
 * {@link com.mapbox.services.android.telemetry.permissions.PermissionsManager}. Either
 * {@code ACCESS_COARSE_LOCATION} or {@code ACCESS_FINE_LOCATION} permissions can be requested and
 * this plugin work as expected.
 *
 * @since 0.1.0
 */
public final class LocationLayerPlugin implements LocationEngineListener, CompassListener,
  OnMapChangedListener, LifecycleObserver, OnCameraMoveListener, OnMapClickListener,
  OnLocationStaleListener {

  private LocationLayer locationLayer;
  private CompassManager compassManager;
  private LocationEngine locationEngine;
  private final MapboxMap mapboxMap;
  private final MapView mapView;

  // Enabled booleans
  @LocationLayerMode.Mode
  private int locationLayerMode;

  // Previous compass and location values
  private float previousMagneticHeading;
  private Point previousPoint;
  private Location location;

  // Animators
  private ValueAnimator locationChangeAnimator;
  private ValueAnimator bearingChangeAnimator;

  private long locationUpdateTimestamp;
  private boolean linearAnimation;

  private OnLocationLayerClickListener onLocationLayerClickListener;
  private StaleStateRunnable staleStateRunnable;
  private LocationLayerOptions options;

  /**
   * Construct a {@code LocationLayerPlugin}
   *
   * @param mapView        the MapView to apply the My Location layer plugin to
   * @param mapboxMap      the MapboxMap to apply the My Location layer plugin with
   * @param locationEngine the {@link LocationEngine} this plugin should use to update
   * @since 0.1.0
   */
  public LocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap,
                             @Nullable LocationEngine locationEngine) {
    this(mapView, mapboxMap, locationEngine,
      LocationLayerOptions.createFromAttributes(mapView.getContext(),
        R.style.mapbox_LocationLayer));
  }

  /**
   * Construct a {@code LocationLayerPlugin}
   *
   * @param mapView        the MapView to apply the My Location layer plugin to
   * @param mapboxMap      the MapboxMap to apply the My Location layer plugin with
   * @param locationEngine the {@link LocationEngine} this plugin should use to update
   * @param styleRes       customize the user location icons inside your apps {@code style.xml}
   * @since 0.1.0
   */
  public LocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap,
                             @Nullable LocationEngine locationEngine, @StyleRes int styleRes) {
    this(mapView, mapboxMap, locationEngine,
      LocationLayerOptions.createFromAttributes(mapView.getContext(), styleRes));

  }

  public LocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap,
                             @Nullable LocationEngine locationEngine,
                             LocationLayerOptions options) {
    this.locationEngine = locationEngine;
    this.mapboxMap = mapboxMap;
    this.mapView = mapView;
    this.options = options;
    mapView.addOnMapChangedListener(this);
    initialize();
  }

  private void initialize() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    staleStateRunnable = new StaleStateRunnable(options.staleStateDelay());
    locationLayerMode = LocationLayerMode.NONE;
    locationLayer = new LocationLayer(mapView, mapboxMap, options, staleStateRunnable);
    compassManager = new CompassManager(mapView.getContext(), this);
  }

  /**
   * After creating an instance of this plugin, you can use this API to enable the location mode of
   * your choice. These modes can be found in the {@link LocationLayerMode} class and the parameter
   * only accepts one of those modes. Note that before enabling the My Location layer, you will need
   * to ensure that you have the requested the required user location permissions.
   * <p>
   * <ul>
   * <li>{@link LocationLayerMode#TRACKING}: Display the user location on the map as a small dot</li>
   * <li>{@link LocationLayerMode#COMPASS}: Display the user location and current heading/bearing</li>
   * <li>{@link LocationLayerMode#NAVIGATION}: Display the user location on the map using a navigation icon</li>
   * <li>{@link LocationLayerMode#NONE}: Disable user location showing on the map</li>
   * </ul>
   *
   * @param locationLayerMode one of the modes found in {@link LocationLayerMode}
   * @since 0.1.0
   */
  @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
  public void setLocationLayerEnabled(@LocationLayerMode.Mode int locationLayerMode) {
    this.locationLayerMode = locationLayerMode;
    if (locationLayerMode != LocationLayerMode.NONE) {
      locationLayer.setLayersVisibility(true);

      // Set an initial location if one is available and the locationEngines not null
      if (locationEngine != null) {
        setLastLocation();
        locationEngine.addLocationEngineListener(this);
      }

      toggleCameraListener();

      if (locationLayerMode == LocationLayerMode.COMPASS) {
        setLinearAnimation(false);
        setNavigationEnabled(false);
        setMyBearingEnabled(true);
      } else if (locationLayerMode == LocationLayerMode.NAVIGATION) {
        setMyBearingEnabled(false);
        setNavigationEnabled(true);
      } else if (locationLayerMode == LocationLayerMode.TRACKING) {
        setLinearAnimation(false);
        setMyBearingEnabled(false);
        setNavigationEnabled(false);
      }
    } else {
      disableLocationLayerPlugin();
    }
  }

  /**
   * Returns the current location mode being used with this plugin.
   *
   * @return on of the {@link LocationLayerMode} values
   * @since 0.1.0
   */
  public int getLocationLayerMode() {
    return locationLayerMode;
  }

  public LocationLayerOptions getLocationLayerOptions() {
    return options;
  }

  @Override
  public void onMapChanged(int change) {
    if (change == MapView.WILL_START_LOADING_MAP) {
      stopAllAnimations();
    } else if (change == MapView.DID_FINISH_LOADING_STYLE) {
      mapStyleFinishedLoading();
    }
  }

  @Override
  public void isLocationStale(boolean stale) {
    Timber.v("isLocationStale: %b", stale);
    locationLayer.locationsStale(stale);
  }

  /**
   * Apply a new Location Layer style after the {@link LocationLayerPlugin} has been constructed.
   *
   * @param styleRes a XML style overriding some or all the options
   * @since 0.1.0
   */
  public void applyStyle(@StyleRes int styleRes) {
    applyStyle(LocationLayerOptions.createFromAttributes(mapView.getContext(), styleRes));
  }

  public void applyStyle(LocationLayerOptions options) {
    locationLayer.applyStyle(options);
    if (!options.enableStaleState()) {
      staleStateRunnable.reset();
    }
    staleStateRunnable.setDelayTime(options.staleStateDelay());
  }

  /**
   * Use to either force a location update or to manually control when the user location gets
   * updated.
   *
   * @param location where you'd like the location icon to be placed on the map
   * @since 0.1.0
   */
  public void forceLocationUpdate(@Nullable Location location) {
    updateLocation(location);
  }

  /**
   * The {@link LocationEngine} the plugin will use to update it's position. If {@code null} is
   * passed in, all updates will occur through the
   * {@link LocationLayerPlugin#forceLocationUpdate(Location)} method.
   *
   * @param locationEngine a {@link LocationEngine} this plugin should use to handle updates
   * @since 0.1.0
   */
  @SuppressWarnings( {"MissingPermission"})
  public void setLocationEngine(@Nullable LocationEngine locationEngine) {
    if (locationEngine != null) {
      this.locationEngine = locationEngine;
      setLocationLayerEnabled(locationLayerMode);
    } else if (this.locationEngine != null) {
      this.locationEngine.removeLocationEngineListener(this);
      this.locationEngine = null;
    }
  }

  /**
   * Returns the current {@link LocationEngine} being used for updating the user location layer.
   *
   * @return the {@link LocationEngine} being used to update the user location layer
   * @since 0.1.0
   */
  @Nullable
  public LocationEngine getLocationEngine() {
    return locationEngine;
  }

  /**
   * Required to place inside your activities {@code onStop} method.
   *
   * @since 0.1.0
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  public void onStop() {
    staleStateRunnable.onStop();
    stopAllAnimations();
    if (compassManager != null && compassManager.isSensorAvailable()) {
      compassManager.onStop();
    }
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
    }
    if (mapboxMap != null) {
      mapboxMap.removeOnCameraMoveListener(this);
    }
  }

  /**
   * Required to place inside your activities {@code onStart} method. You'll also most likely want
   * to check that this Location Layer plugin instance inside your activity is null or not.
   *
   * @since 0.1.0
   */
  @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  public void onStart() {
    if (locationLayerMode != LocationLayerMode.NONE) {
      setLocationLayerEnabled(locationLayerMode);
    }

    if (!compassManager.getCompassListeners().isEmpty()
      || (locationLayerMode == LocationLayerMode.COMPASS && compassManager.isSensorAvailable())) {
      compassManager.onStart();
    }
    if (mapboxMap != null) {
      mapboxMap.addOnCameraMoveListener(this);
    }
    staleStateRunnable.addOnLocationStaleListener(this);
  }

  /**
   * Check whether the location update animator is using a linear or an accelerate/decelerate
   * interpolator. When the navigation mode is being used, the animator automatically become linear.
   *
   * @return boolean true if the location update animator is set to linear, otherwise false
   * @since 0.1.0
   */
  public boolean isLinearAnimation() {
    return linearAnimation;
  }

  /**
   * Set whether the location update animator is using linear (true) or an accelerate/decelerate
   * (false) interpolator. When the navigation mode is being used, the animator automatically become
   * linear.
   *
   * @param linearAnimation boolean true if you'd like to set the location update animator to
   *                        linear, otherwise false
   * @since 0.1.0
   */
  public void setLinearAnimation(boolean linearAnimation) {
    this.linearAnimation = linearAnimation;
  }

  /**
   * Add a compass listener to get heading updates every second. Once the first listener gets added,
   * the sensor gets initiated and starts returning values.
   *
   * @param compassListener a {@link CompassListener} for listening into compass heading and
   *                        accuracy changes
   * @since 0.2.0
   */
  public void addCompassListener(@NonNull CompassListener compassListener) {
    compassManager.addCompassListener(compassListener);
    compassManager.onStart();
  }

  /**
   * Remove either a single instance of compass listener or all the listeners using null.
   *
   * @param compassListener the {@link CompassListener} which you'd like to remove from the listener
   *                        list. You can optionally pass in null to remove all listeners
   */
  public void removeCompassListener(@Nullable CompassListener compassListener) {
    compassManager.removeCompassListener(compassListener);
    if (compassManager.getCompassListeners().isEmpty()) {
      compassManager.onStop();
    }
  }

  /**
   * Adds a listener that gets invoked when the user clicks the location layer.
   *
   * @param locationClickListener The location layer click listener that is invoked when the
   *                              location layer is clicked
   * @since 0.3.0
   */
  public void setOnLocationClickListener(
    @Nullable OnLocationLayerClickListener locationClickListener) {
    this.onLocationLayerClickListener = locationClickListener;
    if (onLocationLayerClickListener != null) {
      mapboxMap.addOnMapClickListener(this);
    }
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    if (onLocationLayerClickListener != null && locationLayer.onMapClick(point)) {
      onLocationLayerClickListener.onLocationLayerClick();
    }
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  public void onConnected() {
    if (locationEngine != null) {
      locationEngine.requestLocationUpdates();
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    updateLocation(location);
  }

  @Override
  public void onCompassChanged(float userHeading) {
    bearingChangeAnimate(userHeading);
  }

  @Override
  public void onCompassAccuracyChange(int compassStatus) {
    // Currently don't handle this inside SDK
  }

  private void toggleCameraListener() {
    if (locationLayerMode == LocationLayerMode.NAVIGATION) {
      mapboxMap.removeOnCameraMoveListener(this);
      return;
    }
    mapboxMap.addOnCameraMoveListener(this);
  }

  private void updateLocation(Location location) {
    this.location = location;
    if (location == null) {
      locationUpdateTimestamp = SystemClock.elapsedRealtime();
      return;
    }
    if (locationLayerMode == LocationLayerMode.NAVIGATION && location.hasBearing()) {
      bearingChangeAnimate(location.getBearing());
    } else if (locationLayerMode != LocationLayerMode.NAVIGATION) {
      locationLayer.updateAccuracyRadius(location);
    }
    setLocation(location);
  }

  /**
   * disable the location layer plugin if the locationLayerMode is set to none.
   */
  private void disableLocationLayerPlugin() {
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
    }
    locationLayer.setLayersVisibility(false);
  }

  /**
   * If the locationEngine contains a last location value, we use it for the initial location layer
   * position.
   */
  @SuppressWarnings( {"MissingPermission"})
  private void setLastLocation() {
    Location lastLocation = locationEngine.getLastLocation();
    if (lastLocation != null) {
      setLocation(lastLocation);
      if (locationLayerMode != LocationLayerMode.NAVIGATION) {
        locationLayer.updateAccuracyRadius(lastLocation);
      }
    }
  }

  /**
   * Get the last know location of the location layer plugin.
   *
   * @return the last known location
   */
  @Nullable
  public Location getLastKnownLocation() {
    return location;
  }

  /**
   * Convenience method for stopping all animations
   */
  private void stopAllAnimations() {
    if (locationChangeAnimator != null) {
      locationChangeAnimator.removeAllListeners();
      locationChangeAnimator.cancel();
      locationChangeAnimator = null;
    }
    if (bearingChangeAnimator != null) {
      bearingChangeAnimator.removeAllListeners();
      bearingChangeAnimator.cancel();
      bearingChangeAnimator = null;
    }
  }

  /**
   * If the location layer was being displayed before the style change, it will need to be displayed
   * in the new style.
   */
  @SuppressWarnings( {"MissingPermission"})
  private void mapStyleFinishedLoading() {
    // recreate runtime style components
    locationLayer = new LocationLayer(mapView, mapboxMap, options, staleStateRunnable);
    // reset state
    setLocationLayerEnabled(locationLayerMode);
    setBearing(previousMagneticHeading);
    if (previousPoint != null) {
      locationLayer.setLocationPoint(previousPoint);
    }
  }

  /**
   * Enable or disable the My Location bearing by passing in a boolean here. Once enabled, The users
   * location and bearing's indicated on the map by default as a small blue dot with a chevron
   * pointing in the direction of the devices compass bearing.
   *
   * @param bearingEnabled boolean true if you'd like to enable the user location bearing,
   *                       otherwise, false will disable
   * @since 0.1.0
   */
  private void setMyBearingEnabled(boolean bearingEnabled) {
    locationLayer.setLayerVisibility(BEARING_LAYER, bearingEnabled);
    if (bearingEnabled) {
      compassManager.onStart();
    } else {
      if (compassManager != null && compassManager.getCompassListeners().isEmpty()) {
        compassManager.onStop();
      }
    }
  }

  /**
   * Enable or disable the My Location navigation by passing in a boolean here. Once enabled, The
   * users location indicated on the map will show (by default) as a large navigation puck with a
   * cheveron/arrow showing the users GPS location bearing.
   *
   * @param navigationEnabled boolean true if you'd like to enable the user location navigation,
   *                          disable otherwise, false will
   * @since 0.1.0
   */
  private void setNavigationEnabled(boolean navigationEnabled) {
    setNavigationLayerVisibility(navigationEnabled);
    setLinearAnimation(navigationEnabled);
    locationLayer.setLayerVisibility(ACCURACY_LAYER, !navigationEnabled);
  }

  private void setNavigationLayerVisibility(boolean visible) {
    locationLayer.setLayerVisibility(NAVIGATION_LAYER, visible);
  }

  @Override
  public void onCameraMove() {
    CameraPosition position = mapboxMap.getCameraPosition();
    locationLayer.updateAccuracyRadius(location);
    locationLayer.updateForegroundOffset(position.tilt);
    locationLayer.updateForegroundBearing((float) position.bearing);
  }

  /**
   * Updates the user location icon.
   *
   * @param location the latest user location
   * @since 0.1.0
   */
  private void setLocation(final Location location) {
    this.location = location;
    staleStateRunnable.updateLatestLocationTime();

    // Convert the new location to a Point object.
    Point newPoint = Point.fromCoordinates(new double[] {location.getLongitude(),
      location.getLatitude()});

    // If the source doesn't have geometry, a Point gets added.
    if (previousPoint == null) {
      locationLayer.setLocationPoint(newPoint);
      previousPoint = newPoint;
      return;
    }

    // Do nothing if the location source's current Point is identical to the new location Point.
    if (previousPoint.getCoordinates().equals(newPoint.getCoordinates())) {
      return;
    }
    locationChangeAnimate(previousPoint, newPoint);
  }

  /*
   * Animators
   */

  /**
   * Handles the animation from currentSourcePoint to the new user location point.
   */
  private void locationChangeAnimate(@NonNull Point currentSourcePoint, @NonNull Point newPoint) {
    if (locationChangeAnimator != null) {
      locationChangeAnimator.end();
    }

    locationChangeAnimator = ValueAnimator.ofObject(new Utils.PointEvaluator(), currentSourcePoint,
      newPoint);

    float speed = location == null ? 0 : location.getSpeed();

    locationChangeAnimator.setDuration(linearAnimation || speed > 0
      ? getLocationUpdateDuration() : LocationLayerConstants.LOCATION_UPDATE_DELAY_MS);
    if (linearAnimation || speed > 0) {
      locationChangeAnimator.setInterpolator(new LinearInterpolator());
    } else {
      locationChangeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    locationChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        previousPoint = (Point) animation.getAnimatedValue();
        locationLayer.setLocationPoint(previousPoint);
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

    // Always rotate the bearing shortest distance
    magneticHeading = shortestRotation(magneticHeading, previousMagneticHeading);

    // No visible change occurred
    if (Math.abs(magneticHeading - previousMagneticHeading) < 1) {
      return;
    }

    bearingChangeAnimator = ValueAnimator.ofFloat(previousMagneticHeading, magneticHeading);
    bearingChangeAnimator.setDuration(COMPASS_UPDATE_RATE_MS);
    bearingChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setBearing((float) valueAnimator.getAnimatedValue());
      }
    });
    bearingChangeAnimator.start();
    previousMagneticHeading = magneticHeading;
  }

  private void setBearing(float bearing) {
    locationLayer.setLayerBearing(
      locationLayerMode == LocationLayerMode.NAVIGATION
        ? NAVIGATION_LAYER : BEARING_LAYER, bearing
    );
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
    long duration = locationUpdateTimestamp - previousUpdateTimeStamp;
    return duration < MAX_ANIMATION_DURATION_MS ? duration : MAX_ANIMATION_DURATION_MS;
  }
}
