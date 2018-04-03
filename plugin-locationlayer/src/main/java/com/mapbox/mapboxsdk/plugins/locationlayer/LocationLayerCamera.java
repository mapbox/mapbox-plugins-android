package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

import java.util.List;
import java.util.Set;

final class LocationLayerCamera implements LocationLayerAnimator.OnCameraAnimationsValuesChangeListener {

  @CameraMode.Mode
  private int cameraMode;

  private final MapboxMap mapboxMap;
  private final OnCameraTrackingChangedListener internalCameraTrackingChangedListener;
  private LocationLayerOptions options;
  private boolean adjustFocalPoint;

  private final MoveGestureDetector moveGestureDetector;
  private float bearing;
  private LatLng latLng;

  LocationLayerCamera(
    Context context,
    MapboxMap mapboxMap,
    OnCameraTrackingChangedListener internalCameraTrackingChangedListener,
    LocationLayerOptions options) {
    this.mapboxMap = mapboxMap;
    mapboxMap.setGesturesManager(
      new PluginsGesturesManager(context), true, true);
    moveGestureDetector = mapboxMap.getGesturesManager().getMoveGestureDetector();
    mapboxMap.addOnMoveListener(onMoveListener);
    mapboxMap.addOnRotateListener(onRotateListener);

    this.internalCameraTrackingChangedListener = internalCameraTrackingChangedListener;
    initializeOptions(options);
  }

  void initializeOptions(LocationLayerOptions options) {
    this.options = options;
  }

  void setCameraMode(@CameraMode.Mode int cameraMode) {
    final boolean wasTracking = isLocationTracking();
    this.cameraMode = cameraMode;
    mapboxMap.cancelTransitions();
    adjustGesturesThresholds();
    notifyCameraTrackingChangeListener(wasTracking);
  }

  int getCameraMode() {
    return cameraMode;
  }

  private void setCameraLatLngBearing() {
    mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
      new CameraPosition.Builder().bearing(bearing).target(latLng).build())
    );
  }

  @Override
  public void onNewLatLngValue(LatLng latLng) {
    if (cameraMode == CameraMode.TRACKING
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH) {
      this.latLng = latLng;
      setCameraLatLngBearing();
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
      this.bearing = gpsBearing;
      setCameraLatLngBearing();
    }
  }

  @Override
  public void onNewCompassBearingValue(float compassBearing) {
    if (cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.NONE_COMPASS) {
      this.bearing = compassBearing;
      setCameraLatLngBearing();
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

  private class PluginsGesturesManager extends AndroidGesturesManager {

    public PluginsGesturesManager(Context context) {
      super(context);
    }

    public PluginsGesturesManager(Context context, boolean applyDefaultThresholds) {
      super(context, applyDefaultThresholds);
    }

    public PluginsGesturesManager(Context context, Set<Integer>[] exclusiveGestures) {
      super(context, exclusiveGestures);
    }

    public PluginsGesturesManager(Context context, List<Set<Integer>> exclusiveGestures,
                                  boolean applyDefaultThresholds) {
      super(context, exclusiveGestures, applyDefaultThresholds);
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