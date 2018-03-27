package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.graphics.PointF;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

final class LocationLayerCamera implements LocationLayerAnimator.OnCameraAnimationsValuesChangeListener {

  @CameraMode.Mode
  private int cameraMode;

  private final MapboxMap mapboxMap;
  private final OnCameraTrackingChangedListener internalCameraTrackingChangedListener;
  private LocationLayerOptions options;
  private boolean adjustFocalPoint;

  private final MoveGestureDetector moveGestureDetector;

  LocationLayerCamera(
    MapboxMap mapboxMap,
    OnCameraTrackingChangedListener internalCameraTrackingChangedListener,
    LocationLayerOptions options) {
    this.mapboxMap = mapboxMap;
    this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener;
    initializeOptions(options);

    moveGestureDetector = mapboxMap.getGesturesManager().getMoveGestureDetector();
    mapboxMap.addOnMoveListener(onMoveListener);
    mapboxMap.addOnRotateListener(onRotateListener);
  }

  void initializeOptions(LocationLayerOptions options) {
    this.options = options;
  }

  void setCameraMode(@CameraMode.Mode int cameraMode) {
    boolean wasTracking = isLocationTracking();
    this.cameraMode = cameraMode;
    mapboxMap.cancelTransitions();
    adjustGesturesThresholds();
    notifyCameraTrackingChangeListener(wasTracking);
  }

  int getCameraMode() {
    return cameraMode;
  }

  private void setBearing(float bearing) {
    mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(bearing));
  }

  private void setLatLng(LatLng latLng) {
    mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
  }

  @Override
  public void onNewLatLngValue(LatLng latLng) {
    if (cameraMode == CameraMode.TRACKING
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH) {
      setLatLng(latLng);
    }

    if (adjustFocalPoint) {
      PointF focalPoint = mapboxMap.getProjection().toScreenLocation(latLng);
      mapboxMap.getUiSettings().setFocalPoint(focalPoint);
      adjustFocalPoint = false;
    }
  }

  @Override
  public void onNewGpsBearingValue(float gpsBearing) {
    boolean trackingNorth = cameraMode == CameraMode.TRACKING_GPS_NORTH
      && mapboxMap.getCameraPosition().bearing != 0;

    if (cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.NONE_GPS
      || trackingNorth) {
      setBearing(gpsBearing);
    }
  }

  @Override
  public void onNewCompassBearingValue(float compassBearing) {
    if (cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.NONE_COMPASS) {
      setBearing(compassBearing);
    }
  }

  private void adjustGesturesThresholds() {
    if (isLocationTracking()) {
      adjustFocalPoint = true;
      moveGestureDetector.setMoveThreshold(options.trackingInitialMoveThreshold());
    }
  }

  private boolean isLocationTracking() {
    return cameraMode == CameraMode.TRACKING
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH;
  }

  private boolean isBearingTracking() {
    return cameraMode == CameraMode.NONE_COMPASS
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.NONE_GPS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH;
  }

  private void notifyCameraTrackingChangeListener(boolean wasTracking) {
    internalCameraTrackingChangedListener.onCameraTrackingChanged(cameraMode);
    if (wasTracking && !isLocationTracking()) {
      mapboxMap.getUiSettings().setFocalPoint(null);
      moveGestureDetector.setMoveThreshold(moveGestureDetector.getDefaultMoveThreshold());
      internalCameraTrackingChangedListener.onCameraTrackingDismissed();
    }
  }

  private MapboxMap.OnMoveListener onMoveListener = new MapboxMap.OnMoveListener() {
    private boolean interrupt;

    @Override
    public void onMoveBegin(MoveGestureDetector detector) {
      if (detector.getPointersCount() > 1
        && detector.getMoveThreshold() != options.trackingMultiFingerMoveThreshold()
        && isLocationTracking()) {
        detector.setMoveThreshold(options.trackingMultiFingerMoveThreshold());
        interrupt = true;
      }
    }

    @Override
    public void onMove(MoveGestureDetector detector) {
      if (interrupt) {
        detector.interrupt();
        return;
      }

      if (isLocationTracking()) {
        setCameraMode(CameraMode.NONE);
      }
    }

    @Override
    public void onMoveEnd(MoveGestureDetector detector) {
      if (!interrupt && isLocationTracking()) {
        moveGestureDetector.setMoveThreshold(options.trackingInitialMoveThreshold());
      }
      interrupt = false;
    }
  };

  private MapboxMap.OnRotateListener onRotateListener = new MapboxMap.OnRotateListener() {
    @Override
    public void onRotateBegin(RotateGestureDetector detector) {
      if (isBearingTracking()) {
        setCameraMode(CameraMode.NONE);
      }
    }

    @Override
    public void onRotate(RotateGestureDetector detector) {
      // no implementation
    }

    @Override
    public void onRotateEnd(RotateGestureDetector detector) {
      // no implementation
    }
  };
}