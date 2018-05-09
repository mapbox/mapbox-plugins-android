package com.mapbox.mapboxsdk.plugins.geojson.model;


import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;

@Deprecated
public class MarkerData {
  private LatLng point;
  private JsonObject properties;

  @Deprecated
  public LatLng getPoint() {
    return point;
  }

  @Deprecated
  public void setPoint(LatLng point) {
    this.point = point;
  }

  @Deprecated
  public JsonObject getProperties() {
    return properties;
  }

  @Deprecated
  public void setProperties(JsonObject properties) {
    this.properties = properties;
  }
}
