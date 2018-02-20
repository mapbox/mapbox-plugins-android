package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerTracking;
import com.mapbox.mapboxsdk.plugins.locationlayer.Utils;

public class LocationLayerCamera {

  private MapboxMap mapboxMap;
  private int trackingMode = LocationLayerTracking.NONE;
  private float bearing = -1;

  public LocationLayerCamera(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
  }

  public void setTrackingMode(@LocationLayerTracking.Mode int trackingMode) {
    this.trackingMode = trackingMode;
  }

  public void moveToLocation(Location location) {
    if (trackingMode == LocationLayerTracking.NONE) {
      return;
    }
    buildCameraUpdateFromLocation(location);
  }

  public void updateBearing(float bearing) {
    if (trackingMode == LocationLayerTracking.TRACKING_COMPASS) {
      mapboxMap.setBearing(bearing);
    }
  }

  private void buildCameraUpdateFromLocation(Location location) {
    switch (trackingMode) {
      case LocationLayerTracking.TRACKING:
      case LocationLayerTracking.TRACKING_COMPASS:
        buildTrackingAnimation(location);
        break;
      case LocationLayerTracking.TRACKING_GPS:
        buildTrackingGPSAnimation(location);
        break;
      case LocationLayerTracking.TRACKING_GPS_NORTH:
        buildTrackingGPSNorthAnimation(location);
        break;
      default:
        break;
    }
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
