package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.support.annotation.NonNull;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

class LayerCameraOnMoveListener implements MapboxMap.OnMoveListener {

  private final LocationLayerCamera camera;
  private MoveGestureDetector moveGestureDetector;
  private LocationLayerOptions options;
  private boolean interrupt;

  LayerCameraOnMoveListener(LocationLayerCamera camera, MoveGestureDetector moveGestureDetector,
                            LocationLayerOptions options) {
    this.camera = camera;
    this.moveGestureDetector = moveGestureDetector;
    this.options = options;
  }

  void updateOptions(LocationLayerOptions options) {
    this.options = options;
  }

  @Override
  public void onMoveBegin(@NonNull MoveGestureDetector detector) {
    if (detector.getPointersCount() > 1
      && detector.getMoveThreshold() != options.trackingMultiFingerMoveThreshold()
      && camera.isLocationTracking()) {
      detector.setMoveThreshold(options.trackingMultiFingerMoveThreshold());
      interrupt = true;
    }
  }

  @Override
  public void onMove(@NonNull MoveGestureDetector detector) {
    if (interrupt) {
      detector.interrupt();
      return;
    }

    camera.setCameraMode(CameraMode.NONE);
  }

  @Override
  public void onMoveEnd(@NonNull MoveGestureDetector detector) {
    if (!interrupt && camera.isLocationTracking()) {
      moveGestureDetector.setMoveThreshold(options.trackingInitialMoveThreshold());
    }
    interrupt = false;
  }
}
