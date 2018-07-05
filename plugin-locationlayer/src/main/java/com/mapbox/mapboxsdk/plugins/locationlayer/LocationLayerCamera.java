package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

import static com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode.TRACKING_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode.TRACKING_COMPASS_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode.TRACKING_GPS_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode.TRACKING_LOCATION;

final class LocationLayerCamera implements PluginAnimator.OnCameraAnimationsValuesChangeListener,
  RefreshPluginRunnable.RefreshPluginListener {

  @CameraMode.Mode
  private int cameraMode;
  private float bearing;
  private LatLng target;

  private final OnCameraTrackingChangedListener internalCameraTrackingChangedListener;
  private final OnCameraMoveInvalidateListener onCameraMoveInvalidateListener;
  private final MoveGestureDetector moveGestureDetector;
  private final MapboxMap mapboxMap;
  private final LayerCameraOnMoveListener onMoveListener;
  private final RefreshPluginRunnable refreshPluginRunnable;
  private LocationLayerOptions options;
  private boolean adjustFocalPoint;

  LocationLayerCamera(Context context,
                      OnCameraTrackingChangedListener internalCameraTrackingChangedListener,
                      OnCameraMoveInvalidateListener onCameraMoveInvalidateListener,
                      MapboxMap mapboxMap,
                      LocationLayerOptions options) {
    this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener;
    this.onCameraMoveInvalidateListener = onCameraMoveInvalidateListener;
    this.mapboxMap = mapboxMap;
    this.options = options;
    mapboxMap.setGesturesManager(new PluginsGesturesManager(context), true, true);
    moveGestureDetector = mapboxMap.getGesturesManager().getMoveGestureDetector();
    onMoveListener = new LayerCameraOnMoveListener(this, moveGestureDetector, options);
    mapboxMap.addOnMoveListener(onMoveListener);
    mapboxMap.addOnRotateListener(new LayerCameraOnRotateListener(this));
    refreshPluginRunnable = new RefreshPluginRunnable(options.refreshIntervalInMillis());
    refreshPluginRunnable.setListener(this);
  }

  @Override
  public void onNewLatLngValue(LatLng latLng) {
    updateTargetLatLng(latLng);
  }

  @Override
  public void onNewGpsBearingValue(float gpsBearing) {
    boolean trackingNorth = isTrackingNorth();
    updateGpsBearing(gpsBearing, trackingNorth);
  }

  @Override
  public void onNewCompassBearingValue(float compassBearing) {
    updateCompassBearing(compassBearing);
  }

  @Override
  public void onNewZoomValue(float zoom) {
    setZoom(zoom);
  }

  @Override
  public void onNewTiltValue(float tilt) {
    setTilt(tilt);
  }

  @Override
  public void onShouldRefresh() {
    refreshCamera();
  }

  void updateOptions(LocationLayerOptions options) {
    this.options = options;
    double refreshIntervalMillis = options.refreshIntervalInMillis();
    refreshPluginRunnable.updateRefreshInterval(refreshIntervalMillis);
    onMoveListener.updateOptions(options);
  }

  void setCameraMode(@CameraMode.Mode int cameraMode) {
    refreshWithCurrentCameraPosition();
    final boolean wasTracking = isLocationTracking();
    this.cameraMode = cameraMode;
    mapboxMap.cancelTransitions();
    updateRefreshRunnableWith(cameraMode);
    adjustGesturesThresholds();
    notifyCameraTrackingChangeListener(wasTracking);
  }

  int getCameraMode() {
    return cameraMode;
  }

  boolean isLocationTracking() {
    return TRACKING_LOCATION.contains(cameraMode);
  }

  void onStart() {
    if (cameraMode != CameraMode.NONE) {
      refreshPluginRunnable.run();
    }
  }

  void onStop() {
    refreshPluginRunnable.cancel();
  }

  private void setZoom(float zoom) {
    mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
    onCameraMoveInvalidateListener.onInvalidateCameraMove();
  }

  private void setTilt(float tilt) {
    mapboxMap.moveCamera(CameraUpdateFactory.tiltTo(tilt));
    onCameraMoveInvalidateListener.onInvalidateCameraMove();
  }

  private void updateTargetLatLng(LatLng latLng) {
    target = latLng;
  }

  private boolean isTrackingNorth() {
    return cameraMode == CameraMode.TRACKING_GPS_NORTH
      && mapboxMap.getCameraPosition().bearing != 0;
  }

  private void updateGpsBearing(float gpsBearing, boolean trackingNorth) {
    if (TRACKING_GPS_BEARING.contains(cameraMode) || trackingNorth) {
      bearing = gpsBearing;
    }
  }

  private void updateCompassBearing(float compassBearing) {
    if (TRACKING_COMPASS_BEARING.contains(cameraMode)) {
      bearing = compassBearing;
    }
  }

  private void refreshCamera() {
    CameraUpdate cameraUpdate = buildCameraUpdate();
    mapboxMap.moveCamera(cameraUpdate);
    onCameraMoveInvalidateListener.onInvalidateCameraMove();

    if (isLocationTracking()) {
      adjustFocalPoint(mapboxMap.getCameraPosition().target);
    }
  }

  private CameraUpdate buildCameraUpdate() {
    CameraPosition cameraPosition = mapboxMap.getCameraPosition();
    CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
    addBearing(cameraPosition, cameraPositionBuilder);
    addTarget(cameraPosition, cameraPositionBuilder);
    return CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build());
  }

  private void addBearing(CameraPosition cameraPosition, CameraPosition.Builder cameraPositionBuilder) {
    boolean isTrackingBearing = TRACKING_BEARING.contains(cameraMode);
    boolean isNewBearing = bearing != cameraPosition.bearing;
    if (isTrackingBearing && isNewBearing) {
      cameraPositionBuilder.bearing(bearing);
    }
  }

  private void addTarget(CameraPosition cameraPosition, CameraPosition.Builder cameraPositionBuilder) {
    boolean isValidTarget = target != null;
    if (isValidTarget && isLocationTracking() && !target.equals(cameraPosition.target)) {
      cameraPositionBuilder.target(target);
    }
  }

  private void refreshWithCurrentCameraPosition() {
    target = mapboxMap.getCameraPosition().target;
    bearing = (float) mapboxMap.getCameraPosition().bearing;
  }

  private void updateRefreshRunnableWith(@CameraMode.Mode int cameraMode) {
    if (cameraMode == CameraMode.NONE) {
      refreshPluginRunnable.cancel();
    } else {
      refreshPluginRunnable.run();
    }
  }

  private void adjustGesturesThresholds() {
    if (isLocationTracking()) {
      adjustFocalPoint = true;
      moveGestureDetector.setMoveThreshold(options.trackingInitialMoveThreshold());
    } else {
      moveGestureDetector.setMoveThreshold(0f);
    }
  }

  private void adjustFocalPoint(LatLng latLng) {
    if (adjustFocalPoint) {
      PointF focalPoint = mapboxMap.getProjection().toScreenLocation(latLng);
      mapboxMap.getUiSettings().setFocalPoint(focalPoint);
      adjustFocalPoint = false;
    }
  }

  private void notifyCameraTrackingChangeListener(boolean wasTracking) {
    internalCameraTrackingChangedListener.onCameraTrackingChanged(cameraMode);
    if (wasTracking && !isLocationTracking()) {
      mapboxMap.getUiSettings().setFocalPoint(null);
      internalCameraTrackingChangedListener.onCameraTrackingDismissed();
    }
  }

  private class PluginsGesturesManager extends AndroidGesturesManager {

    PluginsGesturesManager(Context context) {
      super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
      if (motionEvent != null) {
        int action = motionEvent.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
          adjustGesturesThresholds();
        }
      }
      return super.onTouchEvent(motionEvent);
    }
  }
}