package com.mapbox.mapboxsdk.plugins.markerlayer;

import com.mapbox.mapboxsdk.geometry.LatLng;

// todo add MarkerOptions class
public class Marker {

  private LatLng position;
  private String iconId;
  private float iconOpacity;
  private float iconRotate;
  private float iconSize;

  public void setPosition(LatLng position) {
    this.position = position;
  }

  public void setIconId(String iconId) {
    this.iconId = iconId;
  }

  public void setIconOpacity(float iconOpacity) {
    this.iconOpacity = iconOpacity;
  }

  public void setIconRotate(float iconRotate) {
    this.iconRotate = iconRotate;
  }

  public LatLng getPosition() {
    return position;
  }

  public String getIconId() {
    return iconId;
  }

  public float getIconOpacity() {
    return iconOpacity;
  }

  public float getIconRotate() {
    return iconRotate;
  }

  public float getIconSize() {
    return iconSize;
  }

  public void setIconSize(float iconSize) {
    this.iconSize = iconSize;
  }
}
