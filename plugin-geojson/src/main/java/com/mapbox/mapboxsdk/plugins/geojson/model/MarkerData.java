package com.mapbox.mapboxsdk.plugins.geojson.model;


import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class MarkerData {
  private LatLng point;
  private JsonObject properties;

  public LatLng getPoint() {
    return point;
  }

  public void setPoint(LatLng point) {
    this.point = point;
  }

  public JsonObject getProperties() {
    return properties;
  }

  public void setProperties(JsonObject properties) {
    this.properties = properties;
  }
}
