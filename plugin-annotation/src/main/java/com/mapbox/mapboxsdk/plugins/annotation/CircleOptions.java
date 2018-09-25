// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import com.google.gson.*;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.style.layers.Property;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.convertArray;

/**
 * Builder class from which a circle is created.
 */
public class CircleOptions {

  private Geometry geometry;
  private Float circleRadius;
  private String circleColor;
  private Float circleBlur;
  private Float circleOpacity;
  private Float circleStrokeWidth;
  private String circleStrokeColor;
  private Float circleStrokeOpacity;

  /**
   * Set circle-radius to initialise the circle with.
   *
   * @param circleRadius the circle-radius value
   * @return this
   */
  public CircleOptions withCircleRadius(Float circleRadius) {
    this.circleRadius =  circleRadius;
    return this;
  }

  /**
   * Get the current configured  circle-radius for the circle
   *
   * @return circleRadius value
   */
  public Float getCircleRadius() {
    return circleRadius;
  }

  /**
   * Set circle-color to initialise the circle with.
   *
   * @param circleColor the circle-color value
   * @return this
   */
  public CircleOptions withCircleColor(String circleColor) {
    this.circleColor =  circleColor;
    return this;
  }

  /**
   * Get the current configured  circle-color for the circle
   *
   * @return circleColor value
   */
  public String getCircleColor() {
    return circleColor;
  }

  /**
   * Set circle-blur to initialise the circle with.
   *
   * @param circleBlur the circle-blur value
   * @return this
   */
  public CircleOptions withCircleBlur(Float circleBlur) {
    this.circleBlur =  circleBlur;
    return this;
  }

  /**
   * Get the current configured  circle-blur for the circle
   *
   * @return circleBlur value
   */
  public Float getCircleBlur() {
    return circleBlur;
  }

  /**
   * Set circle-opacity to initialise the circle with.
   *
   * @param circleOpacity the circle-opacity value
   * @return this
   */
  public CircleOptions withCircleOpacity(Float circleOpacity) {
    this.circleOpacity =  circleOpacity;
    return this;
  }

  /**
   * Get the current configured  circle-opacity for the circle
   *
   * @return circleOpacity value
   */
  public Float getCircleOpacity() {
    return circleOpacity;
  }

  /**
   * Set circle-stroke-width to initialise the circle with.
   *
   * @param circleStrokeWidth the circle-stroke-width value
   * @return this
   */
  public CircleOptions withCircleStrokeWidth(Float circleStrokeWidth) {
    this.circleStrokeWidth =  circleStrokeWidth;
    return this;
  }

  /**
   * Get the current configured  circle-stroke-width for the circle
   *
   * @return circleStrokeWidth value
   */
  public Float getCircleStrokeWidth() {
    return circleStrokeWidth;
  }

  /**
   * Set circle-stroke-color to initialise the circle with.
   *
   * @param circleStrokeColor the circle-stroke-color value
   * @return this
   */
  public CircleOptions withCircleStrokeColor(String circleStrokeColor) {
    this.circleStrokeColor =  circleStrokeColor;
    return this;
  }

  /**
   * Get the current configured  circle-stroke-color for the circle
   *
   * @return circleStrokeColor value
   */
  public String getCircleStrokeColor() {
    return circleStrokeColor;
  }

  /**
   * Set circle-stroke-opacity to initialise the circle with.
   *
   * @param circleStrokeOpacity the circle-stroke-opacity value
   * @return this
   */
  public CircleOptions withCircleStrokeOpacity(Float circleStrokeOpacity) {
    this.circleStrokeOpacity =  circleStrokeOpacity;
    return this;
  }

  /**
   * Get the current configured  circle-stroke-opacity for the circle
   *
   * @return circleStrokeOpacity value
   */
  public Float getCircleStrokeOpacity() {
    return circleStrokeOpacity;
  }



  /**
   * Set the LatLng of the circle, which represents the location of the circle on the map
   *
   * @param latLng the location of the circle in a longitude and latitude pair
   * @return this
   */
  public CircleOptions withLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
    return this;
  }

  Circle build(CircleManager circleManager, long id) {
    if (geometry == null) {
      throw new RuntimeException("geometry field is required");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("circle-radius", circleRadius);
    jsonObject.addProperty("circle-color", circleColor);
    jsonObject.addProperty("circle-blur", circleBlur);
    jsonObject.addProperty("circle-opacity", circleOpacity);
    jsonObject.addProperty("circle-stroke-width", circleStrokeWidth);
    jsonObject.addProperty("circle-stroke-color", circleStrokeColor);
    jsonObject.addProperty("circle-stroke-opacity", circleStrokeOpacity);
    return new Circle(circleManager, id, jsonObject, geometry);
  }
}
