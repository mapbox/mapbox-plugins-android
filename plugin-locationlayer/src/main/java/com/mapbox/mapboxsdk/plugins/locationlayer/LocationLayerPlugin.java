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

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapView.OnMapChangedListener;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraIdleListener;
import com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraMoveListener;
import com.mapbox.mapboxsdk.maps.MapboxMap.OnMapClickListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.concurrent.CopyOnWriteArrayList;

import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.DEFAULT_TRACKING_ZOOM_ANIMATION_DURATION;

/**
 * The Location layer plugin provides location awareness to your mobile application. Enabling this
 * plugin provides a contextual experience to your users by showing an icon representing the users
 * current location. A few different modes are offered to provide the right context to your users at
 * the correct time. {@link RenderMode#NORMAL} simply shows the users location on the map
 * represented as a dot. {@link RenderMode#COMPASS} mode allows you to display an arrow icon
 * (by default) that points in the direction the device is pointing in.
 * {@link RenderMode#GPS} can be used in conjunction with our Navigation SDK to
 * display a larger icon (customized with {@link LocationLayerOptions#gpsDrawable()}) we call the user puck.
 * <p>
 * This plugin also offers the ability to set a map camera behavior for tracking the user
 * location. These different {@link CameraMode}s will track, stop tracking the location based on the
 * mode set with {@link LocationLayerPlugin#setCameraMode(int)}.
 * <p>
 * Lastly, {@link LocationLayerPlugin#setLocationLayerEnabled(boolean)} can be used
 * to disable the Location Layer but keep the instance around till the activity is destroyed.
 * <p>
 * Using this plugin requires you to request permission beforehand manually or using
 * {@link com.mapbox.android.core.permissions.PermissionsManager}. Either
 * {@code ACCESS_COARSE_LOCATION} or {@code ACCESS_FINE_LOCATION} permissions can be requested and
 * this plugin work as expected.
 * <p>
 * When instantiating the plugin for the first time, the map's max/min zoom levels will be set to
 * {@link LocationLayerOptions#MAX_ZOOM_DEFAULT} and {@link LocationLayerOptions#MIN_ZOOM_DEFAULT} respectively.
 * You can adjust the zoom range with {@link LocationLayerOptions#maxZoom()} and {@link LocationLayerOptions#minZoom()}.
 *
 * @since 0.1.0
 */
public final class LocationLayerPlugin implements LifecycleObserver {

  private final MapboxMap mapboxMap;
  private final MapView mapView;
  private LocationLayerOptions options;
  private LocationEngine locationEngine;
  private CompassManager compassManager;
  private boolean usingInternalLocationEngine;

  private LocationLayer locationLayer;
  private LocationLayerCamera locationLayerCamera;

  private PluginAnimatorCoordinator pluginAnimatorCoordinator;

  /**
   * Holds last location which is being returned in the {@link #getLastKnownLocation()}
   * when there is no {@link #locationEngine} set or when the last location returned by the engine is null.
   */
  private Location lastLocation;
  private CameraPosition lastCameraPosition;

  /**
   * Indicates that the plugin is enabled and should be displaying location if Mapbox components are available and
   * the lifecycle is in a resumed state.
   */
  private boolean isEnabled;

  /**
   * Indicated that plugin's lifecycle {@link #onStart()} method has been called or the plugin is initialized..
   * This allows Mapbox components enter started state and display data, and adds state safety for methods like
   * {@link #setLocationLayerEnabled(boolean)}
   * <p>
   * Initialized in a started state because the plugin can be instantiated after lifecycle's onStart() and
   * the developer might not register the lifecycle observer but call lifecycle methods manually instead.
   */
  private boolean isPluginStarted = true;

  /**
   * Indicates if Mapbox components are ready to be interacted with. This can differ from {@link #isPluginStarted}
   * if the Mapbox style is being reloaded.
   */
  private boolean isLocationLayerStarted;

  private StaleStateManager staleStateManager;
  private final CopyOnWriteArrayList<OnLocationStaleListener> onLocationStaleListeners
    = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<OnLocationLayerClickListener> onLocationLayerClickListeners
    = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<OnLocationLayerLongClickListener> onLocationLayerLongClickListeners
    = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<OnCameraTrackingChangedListener> onCameraTrackingChangedListeners
    = new CopyOnWriteArrayList<>();

  /**
   * Construct a LocationLayerPlugin
   * <p>
   * <strong>Note</strong>: This constructor will initialize and use an internal {@link LocationEngine}.
   * </p>
   *
   * @param mapView   the MapView to apply the LocationLayerPlugin to
   * @param mapboxMap the MapboxMap to apply the LocationLayerPlugin with
   * @since 0.5.3
   */
  public LocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    this.mapView = mapView;
    options = LocationLayerOptions.createFromAttributes(mapView.getContext(), R.style.mapbox_LocationLayer);
    initializeLocationEngine();
    initialize();
  }

  /**
   * Construct a LocationLayerPlugin
   *
   * @param mapView        the MapView to apply the LocationLayerPlugin to
   * @param mapboxMap      the MapboxMap to apply the LocationLayerPlugin with
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
   * Construct a LocationLayerPlugin
   *
   * @param mapView        the MapView to apply the LocationLayerPlugin to
   * @param mapboxMap      the MapboxMap to apply the LocationLayerPlugin with
   * @param locationEngine the {@link LocationEngine} this plugin should use to update
   * @param styleRes       customize the user location icons inside your apps {@code style.xml}
   * @since 0.1.0
   */
  public LocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap,
                             @Nullable LocationEngine locationEngine, @StyleRes int styleRes) {
    this(mapView, mapboxMap, locationEngine,
      LocationLayerOptions.createFromAttributes(mapView.getContext(), styleRes));
  }

  /**
   * Construct a LocationLayerPlugin
   *
   * @param mapView        the MapView to apply the LocationLayerPlugin to
   * @param mapboxMap      the MapboxMap to apply the LocationLayerPlugin with
   * @param locationEngine the {@link LocationEngine} this plugin should use to update
   * @param options        to customize the user location icons inside your apps
   * @since 0.3.0
   */
  public LocationLayerPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap,
                             @Nullable LocationEngine locationEngine,
                             @NonNull LocationLayerOptions options) {
    this.locationEngine = locationEngine;
    this.mapboxMap = mapboxMap;
    this.mapView = mapView;
    this.options = options;
    initialize();
  }

  /**
   * This method will show or hide the location icon and enable or disable the camera
   * tracking the location.
   *
   * @param isEnabled true to show layers and enable camera, false otherwise
   * @since 0.5.0
   */
  public void setLocationLayerEnabled(boolean isEnabled) {
    if (isEnabled) {
      enableLocationLayerPlugin();
    } else {
      disableLocationLayerPlugin();
    }
  }

  /**
   * Returns whether the plugin is enabled, meaning that location can be displayed and camera modes can be used.
   *
   * @return true if the plugin is enabled, false otherwise
   * @since 0.6.0
   */
  public boolean isLocationLayerEnabled() {
    return isEnabled;
  }

  /**
   * Sets the camera mode, which determines how the map camera will track the rendered location.
   * <p>
   * <ul>
   * <li>{@link CameraMode#NONE}: No camera tracking</li>
   * <li>{@link CameraMode#NONE_COMPASS}: Camera does not track location, but does track compass bearing</li>
   * <li>{@link CameraMode#NONE_GPS}: Camera does not track location, but does track GPS bearing</li>
   * <li>{@link CameraMode#TRACKING}: Camera tracks the user location</li>
   * <li>{@link CameraMode#TRACKING_COMPASS}: Camera tracks the user location, with bearing provided by a compass</li>
   * <li>{@link CameraMode#TRACKING_GPS}: Camera tracks the user location, with normalized bearing</li>
   * <li>{@link CameraMode#TRACKING_GPS_NORTH}: Camera tracks the user location, with bearing always set to north</li>
   * </ul>
   *
   * @param cameraMode one of the modes found in {@link CameraMode}
   * @since 0.5.0
   */
  public void setCameraMode(@CameraMode.Mode int cameraMode) {
    boolean isGpsNorth = cameraMode == CameraMode.TRACKING_GPS_NORTH;
    pluginAnimatorCoordinator.resetAllCameraAnimations(mapboxMap.getCameraPosition(), isGpsNorth);
    locationLayerCamera.setCameraMode(cameraMode);
  }

  /**
   * Provides the current camera mode being used to track
   * the location or compass updates.
   *
   * @return the current camera mode
   * @since 0.5.0
   */
  @CameraMode.Mode
  public int getCameraMode() {
    return locationLayerCamera.getCameraMode();
  }

  /**
   * Sets the render mode, which determines how the location updates will be rendered on the map.
   * <p>
   * <ul>
   * <li>{@link RenderMode#NORMAL}: Shows user location, bearing ignored</li>
   * <li>{@link RenderMode#COMPASS}: Shows user location with bearing considered from compass</li>
   * <li>{@link RenderMode#GPS}: Shows user location with bearing considered from location</li>
   * </ul>
   *
   * @param renderMode one of the modes found in {@link RenderMode}
   * @since 0.5.0
   */
  public void setRenderMode(@RenderMode.Mode int renderMode) {
    locationLayer.setRenderMode(renderMode);
    updateLayerOffsets(true);
  }

  /**
   * Provides the current render mode being used to show
   * the location and/or compass updates on the map.
   *
   * @return the current render mode
   * @since 0.5.0
   */
  @RenderMode.Mode
  public int getRenderMode() {
    return locationLayer.getRenderMode();
  }

  /**
   * Returns the current location options being used.
   *
   * @return the current {@link LocationLayerOptions}
   * @since 0.4.0
   */
  public LocationLayerOptions getLocationLayerOptions() {
    return options;
  }

  /**
   * Apply a new LocationLayer style with a style resource.
   *
   * @param styleRes a XML style overriding some or all the options
   * @since 0.4.0
   */
  public void applyStyle(@StyleRes int styleRes) {
    applyStyle(LocationLayerOptions.createFromAttributes(mapView.getContext(), styleRes));
  }

  /**
   * Apply a new LocationLayer style with location layer options.
   *
   * @param options to update the current style
   * @since 0.4.0
   */
  public void applyStyle(LocationLayerOptions options) {
    this.options = options;
    locationLayer.applyStyle(options);
    if (!options.enableStaleState()) {
      staleStateManager.onStop();
    }
    staleStateManager.setDelayTime(options.staleStateTimeout());
    updateMapWithOptions(options);
  }

  /**
   * Zooms to the desired zoom level.
   * This API can only be used in pair with camera modes other than {@link CameraMode#NONE}.
   * If you are not using any of {@link CameraMode} modes,
   * use one of {@link MapboxMap#moveCamera(CameraUpdate)},
   * {@link MapboxMap#easeCamera(CameraUpdate)} or {@link MapboxMap#animateCamera(CameraUpdate)} instead.
   *
   * @param zoomLevel         The desired zoom level.
   * @param animationDuration The zoom animation duration.
   * @param callback          The callback with finish/cancel information
   */
  public void zoomWhileTracking(double zoomLevel, long animationDuration,
                                @Nullable MapboxMap.CancelableCallback callback) {
    if (!isLocationLayerStarted) {
      return;
    } else if (getCameraMode() == CameraMode.NONE) {
      Timber.e("%s%s",
        "LocationLayerPlugin#zoomWhileTracking method can only be used",
        " when a camera mode other than CameraMode#NONE is engaged.");
      return;
    }
    pluginAnimatorCoordinator.feedNewZoomLevel(zoomLevel, mapboxMap.getCameraPosition(), animationDuration, callback);
  }

  /**
   * Zooms to the desired zoom level.
   * This API can only be used in pair with camera modes other than {@link CameraMode#NONE}.
   * If you are not using any of {@link CameraMode} modes,
   * use one of {@link MapboxMap#moveCamera(CameraUpdate)},
   * {@link MapboxMap#easeCamera(CameraUpdate)} or {@link MapboxMap#animateCamera(CameraUpdate)} instead.
   *
   * @param zoomLevel         The desired zoom level.
   * @param animationDuration The zoom animation duration.
   */
  public void zoomWhileTracking(double zoomLevel, long animationDuration) {
    zoomWhileTracking(zoomLevel, animationDuration, null);
  }

  /**
   * Zooms to the desired zoom level.
   * This API can only be used in pair with camera modes other than {@link CameraMode#NONE}.
   * If you are not using any of {@link CameraMode} modes,
   * use one of {@link MapboxMap#moveCamera(CameraUpdate)},
   * {@link MapboxMap#easeCamera(CameraUpdate)} or {@link MapboxMap#animateCamera(CameraUpdate)} instead.
   *
   * @param zoomLevel The desired zoom level.
   */
  public void zoomWhileTracking(double zoomLevel) {
    zoomWhileTracking(zoomLevel, DEFAULT_TRACKING_ZOOM_ANIMATION_DURATION, null);
  }

  public void cancelZoomWhileTrackingAnimation() {
    pluginAnimatorCoordinator.cancelZoomAnimation();
  }

  // TODO: 12/07/2018 docs
  public void tiltTo(double tilt) {

  }

  /**
   * Use to either force a location update or to manually control when the user location gets
   * updated.
   *
   * @param location where the location icon is placed on the map
   * @since 0.1.0
   */
  public void forceLocationUpdate(@Nullable Location location) {
    updateLocation(location);
  }

  /**
   * Set the location engine to update the current user location.
   * <p>
   * If {@code null} is passed in, all updates will occur through the
   * {@link LocationLayerPlugin#forceLocationUpdate(Location)} method.
   *
   * @param locationEngine a {@link LocationEngine} this plugin should use to handle updates
   * @since 0.1.0
   */
  public void setLocationEngine(@Nullable LocationEngine locationEngine) {
    if (this.locationEngine != null) {
      // If internal location engines being used, extra steps need to be taken to deconstruct the
      // instance.
      if (usingInternalLocationEngine) {
        this.locationEngine.removeLocationUpdates();
        this.locationEngine.deactivate();
        usingInternalLocationEngine = false;
      }
      this.locationEngine.removeLocationEngineListener(locationEngineListener);
      this.locationEngine = null;
    }

    if (locationEngine != null) {
      this.locationEngine = locationEngine;
      if (isEnabled) {
        this.locationEngine.addLocationEngineListener(locationEngineListener);
      }
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
   * Get the last know location of the location layer plugin.
   *
   * @return the last known location
   * @since 0.1.0
   */
  @Nullable
  @RequiresPermission(anyOf = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
  public Location getLastKnownLocation() {
    Location location = locationEngine != null ? locationEngine.getLastLocation() : null;
    if (location == null) {
      location = lastLocation;
    }
    return location;
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
   * @param listener The location layer click listener that is invoked when the
   *                 location layer is clicked
   * @since 0.3.0
   */
  public void addOnLocationClickListener(@NonNull OnLocationLayerClickListener listener) {
    onLocationLayerClickListeners.add(listener);
  }

  /**
   * Removes the passed listener from the current list of location click listeners.
   *
   * @param listener to be removed
   * @since 0.3.0
   */
  public void removeOnLocationClickListener(@NonNull OnLocationLayerClickListener listener) {
    onLocationLayerClickListeners.remove(listener);
  }

  /**
   * Adds a listener that gets invoked when the user long clicks the location layer.
   *
   * @param listener The location layer click listener that is invoked when the
   *                 location layer is clicked
   * @since 0.5.0
   */
  public void addOnLocationLongClickListener(@NonNull OnLocationLayerLongClickListener listener) {
    onLocationLayerLongClickListeners.add(listener);
  }

  /**
   * Removes the passed listener from the current list of location long click listeners.
   *
   * @param listener to be removed
   * @since 0.5.0
   */
  public void removeOnLocationLongClickListener(@NonNull OnLocationLayerLongClickListener listener) {
    onLocationLayerLongClickListeners.remove(listener);
  }

  /**
   * Adds a listener that gets invoked when camera tracking state changes.
   *
   * @param listener Listener that gets invoked when camera tracking state changes.
   * @since 0.5.0
   */
  public void addOnCameraTrackingChangedListener(@NonNull OnCameraTrackingChangedListener listener) {
    onCameraTrackingChangedListeners.add(listener);
  }

  /**
   * Removes a listener that gets invoked when camera tracking state changes.
   *
   * @param listener Listener that gets invoked when camera tracking state changes.
   * @since 0.5.0
   */
  public void removeOnCameraTrackingChangedListener(@NonNull OnCameraTrackingChangedListener listener) {
    onCameraTrackingChangedListeners.remove(listener);
  }

  /**
   * Adds the passed listener that gets invoked when user updates have stopped long enough for the last update
   * to be considered stale.
   * <p>
   * This timeout is set by {@link LocationLayerOptions#staleStateTimeout()}.
   *
   * @param listener invoked when last update is considered stale
   * @since 0.5.0
   */
  public void addOnLocationStaleListener(@NonNull OnLocationStaleListener listener) {
    onLocationStaleListeners.add(listener);
  }

  /**
   * Removes the passed listener from the current list of stale listeners.
   *
   * @param listener to be removed from the list
   * @since 0.5.0
   */
  public void removeOnLocationStaleListener(@NonNull OnLocationStaleListener listener) {
    onLocationStaleListeners.remove(listener);
  }

  /**
   * Required to place inside your activities {@code onStart} method. You'll also most likely want
   * to check that this Location Layer plugin instance inside your activity is null or not.
   *
   * @since 0.1.0
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  public void onStart() {
    isPluginStarted = true;
    onLocationLayerStart();
  }

  /**
   * Required to place inside your activities {@code onStop} method.
   *
   * @since 0.1.0
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  public void onStop() {
    onLocationLayerStop();
    isPluginStarted = false;
  }

  void onLocationLayerStart() {
    if (isLocationLayerStarted || !isPluginStarted) {
      return;
    }

    isLocationLayerStarted = true;
    if (isEnabled) {
      if (locationEngine != null) {
        locationEngine.addLocationEngineListener(locationEngineListener);
      }
      setLastLocation();
      setLastCompassHeading();
      locationLayer.show();
      setCameraMode(locationLayerCamera.getCameraMode());
    }
    if (mapboxMap != null) {
      mapboxMap.addOnCameraMoveListener(onCameraMoveListener);
      mapboxMap.addOnCameraIdleListener(onCameraIdleListener);
    }
    if (options.enableStaleState()) {
      staleStateManager.onStart();
    }
    compassManager.onStart();
  }

  void onLocationLayerStop() {
    if (!isLocationLayerStarted || !isPluginStarted) {
      return;
    }

    isLocationLayerStarted = false;
    locationLayer.hide();
    staleStateManager.onStop();
    compassManager.onStop();
    pluginAnimatorCoordinator.cancelAllAnimations();
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(locationEngineListener);
    }
    if (mapboxMap != null) {
      mapboxMap.removeOnCameraMoveListener(onCameraMoveListener);
      mapboxMap.removeOnCameraIdleListener(onCameraIdleListener);
    }
  }

  private void initialize() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    mapView.addOnMapChangedListener(onMapChangedListener);

    mapboxMap.addOnMapClickListener(onMapClickListener);
    mapboxMap.addOnMapLongClickListener(onMapLongClickListener);

    locationLayer = new LocationLayer(mapView, mapboxMap, options);
    locationLayerCamera = new LocationLayerCamera(
      mapView.getContext(), mapboxMap, cameraTrackingChangedListener, options, onCameraMoveInvalidateListener);
    pluginAnimatorCoordinator = new PluginAnimatorCoordinator();
    pluginAnimatorCoordinator.addLayerListener(locationLayer);
    pluginAnimatorCoordinator.addCameraListener(locationLayerCamera);

    compassManager = new CompassManager(mapView.getContext());
    compassManager.addCompassListener(compassListener);
    staleStateManager = new StaleStateManager(onLocationStaleListener, options.staleStateTimeout());

    updateMapWithOptions(options);

    enableLocationLayerPlugin();
    setRenderMode(RenderMode.NORMAL);
    setCameraMode(CameraMode.NONE);
  }

  private void initializeLocationEngine() {
    usingInternalLocationEngine = true;
    locationEngine = new LocationEngineProvider(mapView.getContext()).obtainBestLocationEngineAvailable();
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.setFastestInterval(1000);
    locationEngine.activate();
  }

  private void enableLocationLayerPlugin() {
    isEnabled = true;
    onLocationLayerStart();
  }

  private void disableLocationLayerPlugin() {
    isEnabled = false;
    onLocationLayerStop();
  }

  private void updateMapWithOptions(final LocationLayerOptions options) {
    mapboxMap.setPadding(
      options.padding()[0], options.padding()[1], options.padding()[2], options.padding()[3]
    );

    mapboxMap.setMaxZoomPreference(options.maxZoom());
    mapboxMap.setMinZoomPreference(options.minZoom());
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
    } else if (!isLocationLayerStarted) {
      lastLocation = location;
      return;
    }

    staleStateManager.updateLatestLocationTime();
    CameraPosition currentCameraPosition = mapboxMap.getCameraPosition();
    boolean isGpsNorth = getCameraMode() == CameraMode.TRACKING_GPS_NORTH;
    pluginAnimatorCoordinator.feedNewLocation(location, currentCameraPosition, isGpsNorth);
    updateAccuracyRadius(location, false);
    lastLocation = location;
  }

  private void updateCompassHeading(float heading) {
    pluginAnimatorCoordinator.feedNewCompassBearing(heading, mapboxMap.getCameraPosition());
  }

  /**
   * If the locationEngine contains a last location value, we use it for the initial location layer
   * position.
   */
  @SuppressLint("MissingPermission")
  private void setLastLocation() {
    if (locationEngine != null) {
      updateLocation(getLastKnownLocation());
    }
  }

  private void setLastCompassHeading() {
    updateCompassHeading(compassManager.getLastHeading());
  }

  @SuppressLint("MissingPermission")
  private void updateLayerOffsets(boolean forceUpdate) {
    CameraPosition position = mapboxMap.getCameraPosition();
    if (lastCameraPosition == null || forceUpdate) {
      lastCameraPosition = position;
      locationLayer.updateForegroundBearing((float) position.bearing);
      locationLayer.updateForegroundOffset(position.tilt);
      updateAccuracyRadius(getLastKnownLocation(), true);
      return;
    }

    if (position.bearing != lastCameraPosition.bearing) {
      locationLayer.updateForegroundBearing((float) position.bearing);
    }
    if (position.tilt != lastCameraPosition.tilt) {
      locationLayer.updateForegroundOffset(position.tilt);
    }
    if (position.zoom != lastCameraPosition.zoom) {
      updateAccuracyRadius(getLastKnownLocation(), true);
    }
    lastCameraPosition = position;
  }

  private void updateAccuracyRadius(Location location, boolean noAnimation) {
    pluginAnimatorCoordinator.feedNewAccuracyRadius(Utils.calculateZoomLevelRadius(mapboxMap, location), noAnimation);
  }

  private OnCameraMoveListener onCameraMoveListener = new OnCameraMoveListener() {
    @Override
    public void onCameraMove() {
      updateLayerOffsets(false);
    }
  };

  private OnCameraIdleListener onCameraIdleListener = new OnCameraIdleListener() {
    @Override
    public void onCameraIdle() {
      updateLayerOffsets(false);
    }
  };

  private OnMapClickListener onMapClickListener = new OnMapClickListener() {
    @Override
    public void onMapClick(@NonNull LatLng point) {
      if (!onLocationLayerClickListeners.isEmpty() && locationLayer.onMapClick(point)) {
        for (OnLocationLayerClickListener listener : onLocationLayerClickListeners) {
          listener.onLocationLayerClick();
        }
      }
    }
  };

  private MapboxMap.OnMapLongClickListener onMapLongClickListener = new MapboxMap.OnMapLongClickListener() {
    @Override
    public void onMapLongClick(@NonNull LatLng point) {
      if (!onLocationLayerLongClickListeners.isEmpty() && locationLayer.onMapClick(point)) {
        for (OnLocationLayerLongClickListener listener : onLocationLayerLongClickListeners) {
          listener.onLocationLayerLongClick();
        }
      }
    }
  };

  private OnLocationStaleListener onLocationStaleListener = new OnLocationStaleListener() {
    @Override
    public void onStaleStateChange(boolean isStale) {
      locationLayer.setLocationsStale(isStale);

      for (OnLocationStaleListener listener : onLocationStaleListeners) {
        listener.onStaleStateChange(isStale);
      }
    }
  };

  private OnMapChangedListener onMapChangedListener = new OnMapChangedListener() {
    @SuppressLint("MissingPermission")
    @Override
    public void onMapChanged(int change) {
      if (change == MapView.WILL_START_LOADING_MAP) {
        onLocationLayerStop();
      } else if (change == MapView.DID_FINISH_LOADING_STYLE) {
        locationLayer.initializeComponents(options);
        locationLayerCamera.initializeOptions(options);
        onLocationLayerStart();
      }
    }
  };

  private OnCameraMoveInvalidateListener onCameraMoveInvalidateListener = new OnCameraMoveInvalidateListener() {
    @Override
    public void onInvalidateCameraMove() {
      onCameraMoveListener.onCameraMove();
    }
  };

  private CompassListener compassListener = new CompassListener() {
    @Override
    public void onCompassChanged(float userHeading) {
      updateCompassHeading(userHeading);
    }

    @Override
    public void onCompassAccuracyChange(int compassStatus) {
      // Currently don't handle this inside SDK
    }
  };

  private LocationEngineListener locationEngineListener = new LocationEngineListener() {
    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
      if (usingInternalLocationEngine) {
        locationEngine.requestLocationUpdates();
      }
    }

    @Override
    public void onLocationChanged(Location location) {
      updateLocation(location);
    }
  };

  private OnCameraTrackingChangedListener cameraTrackingChangedListener = new OnCameraTrackingChangedListener() {
    @Override
    public void onCameraTrackingDismissed() {
      for (OnCameraTrackingChangedListener listener : onCameraTrackingChangedListeners) {
        listener.onCameraTrackingDismissed();
      }
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {
      pluginAnimatorCoordinator.cancelZoomAnimation();
      for (OnCameraTrackingChangedListener listener : onCameraTrackingChangedListeners) {
        listener.onCameraTrackingChanged(currentMode);
      }
    }
  };
}
