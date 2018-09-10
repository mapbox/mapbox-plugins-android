package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.UiThread;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

@UiThread
public class Circle {

  public static final String ID_KEY = "id-circle";

  private final CircleManager circleManager;
  private final JsonObject jsonObject = new JsonObject();
  private Geometry geometry;

  /**
   * Create a circle.
   *
   * @param manager the circle manager created and managing the circle
   * @param id            the id of the circle
   */
  Circle(CircleManager manager, long id) {
    this.circleManager = manager;
    this.jsonObject.addProperty(ID_KEY, id);
  }

  /**
   * Get the circle geometry.
   *
   * @return the circle geometry
   */
  Geometry getGeometry() {
    if (geometry == null) {
      throw new IllegalStateException();
    }
    return geometry;
  }

  /**
   * Get the circle feature properties.
   *
   * @return the circle feature properties
   */
  JsonObject getFeature() {
    return jsonObject;
  }

  /**
   * Get the circle id.
   *
   * @return the circle id
   */
  public long getId() {
    return jsonObject.get(ID_KEY).getAsLong();
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
   * @return property wrapper value around Float
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
   * @return property wrapper value around String
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
   * @return property wrapper value around Float
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
   * @return property wrapper value around Float
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
   * @return property wrapper value around Float
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
   * @return property wrapper value around String
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
   * @return property wrapper value around Float
   */
  public void setCircleStrokeOpacity(Float value) {
    jsonObject.addProperty("circle-stroke-opacity", value);
    circleManager.updateSource();
  }

}
