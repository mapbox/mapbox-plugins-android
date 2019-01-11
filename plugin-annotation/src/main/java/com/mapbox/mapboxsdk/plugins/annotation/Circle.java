// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

@UiThread
public class Circle extends Annotation {

  private final CircleManager circleManager;

  /**
   * Create a circle.
   *
   * @param manager the circle manager created and managing the circle
   * @param id            the id of the circle
   */
  Circle(CircleManager manager, long id) {
    super(id);
    this.circleManager = manager;
  }

  /**
   * Called to update the underlying data source.
   */
  @Override
  public void update() {
    circleManager.updateSource();
  }

  /**
   * Set the LatLng of the circle, which represents the location of the circle on the map
   *
   * @param latLng the location of the circle in a longitude and latitude pair
   */
  public void setLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
    circleManager.updateSource();
  }

  // Property accessors
  /**
   * Get the CircleRadius property
   *
   * @return property wrapper value around Float
   */
  public Float getCircleRadius() {
    return jsonObject.get("circle-radius").getAsFloat();
  }

  /**
   * Set the CircleRadius property
   *
   * @param value constant property value for Float
   */
  public void setCircleRadius(Float value) {
    jsonObject.addProperty("circle-radius", value);
    circleManager.updateSource();
  }

  /**
   * Get the CircleColor property
   *
   * @return property wrapper value around String
   */
  public String getCircleColor() {
    return jsonObject.get("circle-color").getAsString();
  }

  /**
   * Set the CircleColor property
   *
   * @param value constant property value for String
   */
  public void setCircleColor(String value) {
    jsonObject.addProperty("circle-color", value);
    circleManager.updateSource();
  }

  /**
   * Get the CircleBlur property
   *
   * @return property wrapper value around Float
   */
  public Float getCircleBlur() {
    return jsonObject.get("circle-blur").getAsFloat();
  }

  /**
   * Set the CircleBlur property
   *
   * @param value constant property value for Float
   */
  public void setCircleBlur(Float value) {
    jsonObject.addProperty("circle-blur", value);
    circleManager.updateSource();
  }

  /**
   * Get the CircleOpacity property
   *
   * @return property wrapper value around Float
   */
  public Float getCircleOpacity() {
    return jsonObject.get("circle-opacity").getAsFloat();
  }

  /**
   * Set the CircleOpacity property
   *
   * @param value constant property value for Float
   */
  public void setCircleOpacity(Float value) {
    jsonObject.addProperty("circle-opacity", value);
    circleManager.updateSource();
  }

  /**
   * Get the CircleStrokeWidth property
   *
   * @return property wrapper value around Float
   */
  public Float getCircleStrokeWidth() {
    return jsonObject.get("circle-stroke-width").getAsFloat();
  }

  /**
   * Set the CircleStrokeWidth property
   *
   * @param value constant property value for Float
   */
  public void setCircleStrokeWidth(Float value) {
    jsonObject.addProperty("circle-stroke-width", value);
    circleManager.updateSource();
  }

  /**
   * Get the CircleStrokeColor property
   *
   * @return property wrapper value around String
   */
  public String getCircleStrokeColor() {
    return jsonObject.get("circle-stroke-color").getAsString();
  }

  /**
   * Set the CircleStrokeColor property
   *
   * @param value constant property value for String
   */
  public void setCircleStrokeColor(String value) {
    jsonObject.addProperty("circle-stroke-color", value);
    circleManager.updateSource();
  }

  /**
   * Get the CircleStrokeOpacity property
   *
   * @return property wrapper value around Float
   */
  public Float getCircleStrokeOpacity() {
    return jsonObject.get("circle-stroke-opacity").getAsFloat();
  }

  /**
   * Set the CircleStrokeOpacity property
   *
   * @param value constant property value for Float
   */
  public void setCircleStrokeOpacity(Float value) {
    jsonObject.addProperty("circle-stroke-opacity", value);
    circleManager.updateSource();
  }

}
