package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.location.Location;

import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

class CameraModeManager {

  private LocationLayerCamera camera;
  private int cameraMode;

  CameraModeManager(LocationLayerCamera camera) {
    this.camera = camera;
  }

  void setCameraMode(@CameraMode.Mode int cameraMode) {
    this.cameraMode = cameraMode;
  }

  void updateFromBearing(float bearing) {
    switch (cameraMode) {
      case CameraMode.NONE_COMPASS:
      case CameraMode.TRACKING_COMPASS:
        camera.buildBearingAnimation(bearing);
        break;
      default:
        break;
    }
  }

  void updateFromLocation(Location location) {
    switch (cameraMode) {
      case CameraMode.NONE_GPS:
        camera.buildBearingGPSAnimation(location);
        break;
      case CameraMode.TRACKING:
        camera.buildTrackingAnimation(location);
        break;
      case CameraMode.TRACKING_GPS:
        camera.buildTrackingGPSAnimation(location);
        break;
      case CameraMode.TRACKING_GPS_NORTH:
        camera.buildTrackingGPSNorthAnimation(location);
        break;
      default:
        break;
    }
  }
}
