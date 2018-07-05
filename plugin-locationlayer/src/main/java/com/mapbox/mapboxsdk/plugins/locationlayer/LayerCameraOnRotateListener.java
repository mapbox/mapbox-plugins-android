package com.mapbox.mapboxsdk.plugins.locationlayer;

import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

import static com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode.TRACKING_BEARING;

class LayerCameraOnRotateListener implements MapboxMap.OnRotateListener {

  private final LocationLayerCamera camera;

  LayerCameraOnRotateListener(LocationLayerCamera camera) {
    this.camera = camera;
  }

  @Override
  public void onRotateBegin(RotateGestureDetector detector) {
    if (isBearingTracking()) {
      camera.setCameraMode(CameraMode.NONE);
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

  private boolean isBearingTracking() {
    int cameraMode = camera.getCameraMode();
    return TRACKING_BEARING.contains(cameraMode);
  }
}
