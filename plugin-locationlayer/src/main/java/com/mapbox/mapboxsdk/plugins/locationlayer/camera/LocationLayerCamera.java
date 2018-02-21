package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.Utils;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

public class LocationLayerCamera {

  private MapboxMap mapboxMap;
  private CameraModeManager cameraModeManager;
  private MapAnimator mapAnimator;

  public LocationLayerCamera(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    cameraModeManager = new CameraModeManager(this);
  }

  public void setCameraMode(@CameraMode.Mode int cameraMode) {
    cancelRunningAnimator();
    cameraModeManager.setCameraMode(cameraMode);
  }

  public void updateFromCompassBearing(float bearing) {
    cameraModeManager.updateFromBearing(bearing);
  }

  public void updateFromLocation(Location location) {
    cameraModeManager.updateFromLocation(location);
  }

  void buildBearingAnimation(float bearing) {
    MapAnimator.Builder mapAnimation = MapAnimator.builder(mapboxMap);
    addBearingAnimator(mapAnimation, bearing);
    mapAnimator = mapAnimation.build();
    mapAnimator.playTogether();
  }

  void buildBearingGPSAnimation(Location location) {
    if (location.hasBearing()) {
      buildBearingAnimation(location.getBearing());
    }
  }

  void buildTrackingAnimation(Location location) {
    LatLng target = new LatLng(location);

    LatLngAnimator latLngAnimator = new LatLngAnimator(target, 1000);
    latLngAnimator.setInterpolator(new LinearInterpolator());

    MapAnimator.builder(mapboxMap)
      .addLatLngAnimator(latLngAnimator)
      .build()
      .playTogether();
  }

  void buildTrackingGPSAnimation(Location location) {
    MapAnimator.Builder mapAnimation = MapAnimator.builder(mapboxMap);
    addLatLngAnimator(location, mapAnimation);

    if (!location.hasBearing()) {
      mapAnimation.build().playTogether();
      return;
    }

    addBearingAnimator(mapAnimation, location.getBearing());
    mapAnimation.build().playTogether();
  }

  void buildTrackingGPSNorthAnimation(Location location) {
    MapAnimator.Builder mapAnimation = MapAnimator.builder(mapboxMap);
    addLatLngAnimator(location, mapAnimation);

    double bearing = mapboxMap.getCameraPosition().bearing;
    if (bearing == 0) {
      mapAnimation.build().playTogether();
      return;
    }

    float targetBearing = Utils.shortestRotation(0, (float) bearing);
    addBearingAnimator(mapAnimation, targetBearing);
    mapAnimation.build().playTogether();
  }

  private void cancelRunningAnimator() {
    if (mapAnimator != null && mapAnimator.isRunning()) {
      mapAnimator.cancel();
    }
  }

  private void addLatLngAnimator(Location location, MapAnimator.Builder mapAnimation) {
    LatLng target = new LatLng(location);
    LatLngAnimator latLngAnimator = new LatLngAnimator(target, 1000);
    latLngAnimator.setInterpolator(new LinearInterpolator());
    mapAnimation.addLatLngAnimator(latLngAnimator);
  }

  private void addBearingAnimator(MapAnimator.Builder mapAnimation, float targetBearing) {
    BearingAnimator bearingAnimator = new BearingAnimator(targetBearing, 1000);
    bearingAnimator.setInterpolator(new LinearInterpolator());
    mapAnimation.addBearingAnimator(bearingAnimator);
  }
}
