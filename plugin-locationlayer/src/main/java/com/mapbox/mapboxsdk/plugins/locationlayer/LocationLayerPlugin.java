package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDelegate;

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

import java.util.concurrent.CopyOnWriteArrayList;

import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * The Location layer plugin provides location awareness to your mobile application. Enabling this
 * plugin provides a contextual experience to your users by showing an icon representing the users
 * current location. A few different modes are offered to provide the right context to your users at
 * the correct time. {@link LocationLayerMode#NORMAL} simply shows the users location on the map
 * represented as a dot. {@link LocationLayerMode#COMPASS} mode allows you to display an arrow icon
 * (by default) that points in the direction the device is pointing in.
 * {@link LocationLayerMode#NAVIGATION} can be used in conjunction with our Navigation SDK to
 * display a larger icon we call the user puck.
 * <p>
 * Lastly, {@link LocationLayerPlugin#setLocationLayerEnabled(boolean)} can be used
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

  private final MapboxMap mapboxMap;
  private final MapView mapView;
  private LocationLayerOptions options;
  private LocationLayer locationLayer;
  private LocationEngine locationEngine;
  private CompassManager compassManager;

  // TODO: 21/02/2018 references to animator, LL and camera

  private boolean isEnabled;
  private StaleStateRunnable staleStateRunnable;
  private final CopyOnWriteArrayList<OnLocationStaleListener> onLocationStaleListeners
    = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<OnLocationLayerClickListener> onLocationLayerClickListeners
    = new CopyOnWriteArrayList<>();

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
      LocationLayerOptions.createFromAttributes(mapView.getContext(), R.style.LocationLayer));
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
    initialize();
  }

  private void initialize() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    mapView.addOnMapChangedListener(this);
    mapboxMap.addOnMapClickListener(this);

    locationLayer = new LocationLayer(mapView, mapboxMap, options, staleStateRunnable);
    compassManager = new CompassManager(mapView.getContext());
    compassManager.addCompassListener(this);
    staleStateRunnable = new StaleStateRunnable(this, options.staleStateDelay());

    enableLocationLayerPlugin();
  }

  @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
  public void setLocationLayerEnabled(boolean isEnabled) {
    if (isEnabled) {
      enableLocationLayerPlugin();
    } else {
      disableLocationLayerPlugin();
    }
  }

  private void enableLocationLayerPlugin() {
    isEnabled = true;

    if (locationEngine != null) {
      locationEngine.addLocationEngineListener(this);
    }
    setLastLocation();
    locationLayer.setLayersVisibility(true);
  }

  private void disableLocationLayerPlugin() {
    isEnabled = false;

    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
    }
    locationLayer.setLayersVisibility(false);
  }

  /**
   * After creating an instance of this plugin, you can use this API to enable the location mode of
   * your choice. These modes can be found in the {@link LocationLayerMode} class and the parameter
   * only accepts one of those modes. Note that before enabling the My Location layer, you will need
   * to ensure that you have the requested the required user location permissions.
   * <p>
   * <ul>
   * <li>{@link LocationLayerMode#NORMAL}: Display the user location on the map as a small dot</li>
   * <li>{@link LocationLayerMode#COMPASS}: Display the user location and current heading/bearing</li>
   * <li>{@link LocationLayerMode#NAVIGATION}: Display the user location on the map using a navigation icon</li>
   * </ul>
   *
   * @param locationLayerMode one of the modes found in {@link LocationLayerMode}
   * @since 0.1.0
   */
  // TODO: 21/02/2018 set render mode and camera mode

  // TODO: 21/02/2018 return current render mode and camera mode

  /**
   * Returns the current location mode being used with this plugin.
   *
   * @return on of the {@link LocationLayerMode} values
   * @since 0.1.0
   */
  public LocationLayerOptions getLocationLayerOptions() {
    return options;
  }

  @Override
  public void onMapChanged(int change) {
    if (change == MapView.WILL_START_LOADING_MAP) {
      // TODO: 21/02/2018 stop animations, notify anyone interested
    } else if (change == MapView.DID_FINISH_LOADING_STYLE) {
      mapStyleFinishedLoading();
    }
  }

  @Override
  public void onStaleStateChange(boolean isStale) {
    Timber.v("onStaleStateChange: %b", isStale);
    locationLayer.locationsStale(isStale);

    for (OnLocationStaleListener listener : onLocationStaleListeners) {
      listener.onStaleStateChange(isStale);
    }
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
      staleStateRunnable.onStop();
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
   * Required to place inside your activities {@code onStart} method. You'll also most likely want
   * to check that this Location Layer plugin instance inside your activity is null or not.
   *
   * @since 0.1.0
   */
  @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  public void onStart() {
    if (isEnabled) {
      if (locationEngine != null) {
        locationEngine.addLocationEngineListener(this);
      }
      setLastLocation();
      // TODO: 21/02/2018 reset modes
    }
    if (mapboxMap != null) {
      mapboxMap.addOnCameraMoveListener(this);
    }
    if (options.enableStaleState()) {
      staleStateRunnable.onStart();
    }
    compassManager.onStart();
  }

  /**
   * Required to place inside your activities {@code onStop} method.
   *
   * @since 0.1.0
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  public void onStop() {
    staleStateRunnable.onStop();
    compassManager.onStop();
    // TODO: 21/02/2018 stop animations
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
    }
    if (mapboxMap != null) {
      mapboxMap.removeOnCameraMoveListener(this);
    }
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
  }

  /**
   * Remove a compass listener.
   *
   * @param compassListener the {@link CompassListener} which you'd like to remove from the listener
   *                        list.
   */
  public void removeCompassListener(@NonNull CompassListener compassListener) {
    compassManager.removeCompassListener(compassListener);
  }

  /**
   * Adds a listener that gets invoked when the user clicks the location layer.
   *
   * @param locationClickListener The location layer click listener that is invoked when the
   *                              location layer is clicked
   * @since 0.3.0
   */
  public void addOnLocationClickListener(@NonNull OnLocationLayerClickListener locationClickListener) {
    onLocationLayerClickListeners.add(locationClickListener);
  }

  public void removeOnLocationClickListener(@NonNull OnLocationLayerClickListener locationClickListener) {
    onLocationLayerClickListeners.remove(locationClickListener);
  }

  public void addOnLocationStaleListener(@NonNull OnLocationStaleListener listener) {
    onLocationStaleListeners.add(listener);
  }

  public void removeOnLocationStaleListener(@NonNull OnLocationStaleListener listener) {
    onLocationStaleListeners.remove(listener);
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    if (!onLocationLayerClickListeners.isEmpty() && locationLayer.onMapClick(point)) {
      for (OnLocationLayerClickListener listener : onLocationLayerClickListeners) {
        listener.onLocationLayerClick();
      }
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
    updateCompassHeading(userHeading);
  }

  @Override
  public void onCompassAccuracyChange(int compassStatus) {
    // Currently don't handle this inside SDK
  }

  /**
   * If the locationEngine contains a last location value, we use it for the initial location layer
   * position.
   */
  @SuppressWarnings( {"MissingPermission"})
  private void setLastLocation() {
    if (locationEngine != null) {
      updateLocation(locationEngine.getLastLocation());
    }
  }

  private void setLastCompassHeading() {
    updateCompassHeading(compassManager.getLastHeading());
  }

  /**
   * Get the last know location of the location layer plugin.
   *
   * @return the last known location
   */
  @SuppressLint("MissingPermission")
  @Nullable
  public Location getLastKnownLocation() {
    return locationEngine != null ? locationEngine.getLastLocation() : null;
  }

  /**
   * If the location layer was being displayed before the style change, it will need to be displayed
   * in the new style.
   */
  @SuppressWarnings( {"MissingPermission"})
  private void mapStyleFinishedLoading() {
    // recreate runtime style components
    locationLayer = new LocationLayer(mapView, mapboxMap, options, staleStateRunnable);
    // TODO: 21/02/2018 reset state
    setLastLocation();

  }

  @Override
  public void onCameraMove() {
    CameraPosition position = mapboxMap.getCameraPosition();
    locationLayer.updateAccuracyRadius(getLastKnownLocation());
    locationLayer.updateForegroundOffset(position.tilt);
    locationLayer.updateForegroundBearing((float) position.bearing);
  }

  /**
   * Updates the user location icon.
   *
   * @param location the latest user location
   * @since 0.1.0
   */
  private void updateLocation(final Location location) {
    if (location == null) {
      return;
    }

    staleStateRunnable.updateLatestLocationTime();

    // Convert the new location to a Point object.
    Point newPoint = Point.fromCoordinates(new double[] {location.getLongitude(),
      location.getLatitude()});

    locationLayer.setLocationPoint(newPoint);

    // TODO: 21/02/2018 notify animator class about new location
  }

  private void updateCompassHeading(float heading) {
    // TODO: 21/02/2018 notify animator class about new heading
  }
}
