package com.mapbox.mapboxsdk.plugins.locationlayer;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

public class LocationLayerCamera implements LocationLayerAnimator.OnAnimationsValuesChangeListener {

  @CameraMode.Mode
  private int cameraMode;

  private MapboxMap mapboxMap;

  public LocationLayerCamera(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
  }

  public void setCameraMode(@CameraMode.Mode int cameraMode) {
    this.cameraMode = cameraMode;
    mapboxMap.cancelTransitions();
  }

  public int getCameraMode() {
    return cameraMode;
  }

  private void setBearing(float bearing) {
    mapboxMap.setBearing(bearing);
  }

  private void setLatLng(LatLng latLng) {
    mapboxMap.setLatLng(latLng);
  }

  @Override
  public void onNewLatLngValue(LatLng latLng) {
    if (cameraMode == CameraMode.TRACKING
      || cameraMode == CameraMode.TRACKING_COMPASS
      || cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH) {
      setLatLng(latLng);
    }
  }

  @Override
  public void onNewGpsBearingValue(float gpsBearing) {
    if (cameraMode == CameraMode.TRACKING_GPS
      || cameraMode == CameraMode.NONE_GPS
      || cameraMode == CameraMode.TRACKING_GPS_NORTH) {
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
}
