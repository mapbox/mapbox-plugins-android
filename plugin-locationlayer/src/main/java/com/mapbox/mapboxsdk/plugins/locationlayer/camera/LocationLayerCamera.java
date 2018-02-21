package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.Utils;

public class LocationLayerCamera {

  private MapboxMap mapboxMap;
  private int cameraMode = CameraMode.NONE;
  private float bearing = -1;

  public LocationLayerCamera(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
  }

  public void setCameraMode(@CameraMode.Mode int cameraMode) {
    this.cameraMode = cameraMode;
  }

  public void updateFromBearing(float bearing) {
    if (cameraMode == CameraMode.NONE) {
      return;
    }
    buildBearingAnimation(bearing);
  }

  public void updateFromLocation(Location location) {
    switch (cameraMode) {
      case CameraMode.NONE:
        return;
      case CameraMode.NONE_GPS:
        break;
      case CameraMode.TRACKING:
        buildTrackingAnimation(location);
        break;
      case CameraMode.TRACKING_GPS:
        buildTrackingGPSAnimation(location);
        break;
      case CameraMode.TRACKING_GPS_NORTH:
        buildTrackingGPSNorthAnimation(location);
        break;
      default:
        break;
    }
  }

  private void buildBearingAnimation(float bearing) {
    MapAnimator.Builder mapAnimation = MapAnimator.builder(mapboxMap);
    createBearingAnimator(mapAnimation, bearing);
    mapAnimation.build().playTogether();
  }

  private void buildTrackingAnimation(Location location) {
    LatLng target = new LatLng(location);

    LatLngAnimator latLngAnimator = new LatLngAnimator(target, 1000);
    latLngAnimator.setInterpolator(new LinearInterpolator());

    MapAnimator.builder(mapboxMap)
      .addLatLngAnimator(latLngAnimator)
      .build()
      .playTogether();
  }

  private void buildTrackingGPSAnimation(Location location) {
    MapAnimator.Builder mapAnimation = MapAnimator.builder(mapboxMap);
    addLatLngAnimator(location, mapAnimation);

    if (!location.hasBearing()) {
      mapAnimation.build().playTogether();
      return;
    }

    float targetBearing = calculateBearing(location);
    createBearingAnimator(mapAnimation, targetBearing);
    mapAnimation.build().playTogether();
  }

  private void buildTrackingGPSNorthAnimation(Location location) {
    MapAnimator.Builder mapAnimation = MapAnimator.builder(mapboxMap);
    addLatLngAnimator(location, mapAnimation);

    double bearing = mapboxMap.getCameraPosition().bearing;
    if (bearing == 0) {
      mapAnimation.build().playTogether();
      return;
    }

    float targetBearing = Utils.shortestRotation(0, (float) bearing);
    createBearingAnimator(mapAnimation, targetBearing);
    mapAnimation.build().playTogether();
  }

  private void addLatLngAnimator(Location location, MapAnimator.Builder mapAnimation) {
    LatLng target = new LatLng(location);
    LatLngAnimator latLngAnimator = new LatLngAnimator(target, 1000);
    latLngAnimator.setInterpolator(new LinearInterpolator());
    mapAnimation.addLatLngAnimator(latLngAnimator);
  }

  private void createBearingAnimator(MapAnimator.Builder mapAnimation, float targetBearing) {
    BearingAnimator bearingAnimator = new BearingAnimator(targetBearing, 1000);
    bearingAnimator.setInterpolator(new LinearInterpolator());
    mapAnimation.addBearingAnimator(bearingAnimator);
  }

  private float calculateBearing(Location location) {
    if (location.hasBearing() && bearing > 0) {
      float bearing = Utils.shortestRotation(location.getBearing(), this.bearing);
      this.bearing = bearing;
      return bearing;
    } else {
      this.bearing = location.getBearing();
      return this.bearing;
    }
  }
}
