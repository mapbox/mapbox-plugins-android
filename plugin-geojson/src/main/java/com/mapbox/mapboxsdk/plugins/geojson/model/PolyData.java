package com.mapbox.mapboxsdk.plugins.geojson.model;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

@Deprecated
public class PolyData {
  private List<LatLng> points;
  private String type;

  @Deprecated
  public List<LatLng> getPoints() {
    return points;
  }

  @Deprecated
  public void setPoints(List<LatLng> points) {
    this.points = points;
  }

  @Deprecated
  public String getType() {
    return type;
  }

  @Deprecated
  public void setType(String type) {
    this.type = type;
  }
}
