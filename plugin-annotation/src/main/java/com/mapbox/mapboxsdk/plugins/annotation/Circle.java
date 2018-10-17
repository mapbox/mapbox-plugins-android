// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.ColorInt;
import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

@UiThread
public class Circle extends Annotation {

  /**
   * Create a circle.
   *
   * @param id            the id of the circle
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Circle(long id, JsonObject jsonObject, Geometry geometry) {
    super(id, jsonObject, geometry);
  }

  /**
   * Set the LatLng of the circle, which represents the location of the circle on the map
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param latLng the location of the circle in a longitude and latitude pair
   */
  public void setLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
  }

  /**
   * Set the Geometry of the circle, which represents the location of the circle on the map
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param geometry the geometry of the circle
   */
  public void setGeometry(Point geometry) {
    this.geometry = geometry;
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
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleRadius(Float value) {
    jsonObject.addProperty("circle-radius", value);
  }

  /**
   * Get the CircleColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getCircleColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("circle-color").getAsString());
  }

  /**
   * Set the CircleColor property
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setCircleColor(@ColorInt int color) {
    jsonObject.addProperty("circle-color", ColorUtils.colorToRgbaString(color));
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
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleBlur(Float value) {
    jsonObject.addProperty("circle-blur", value);
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
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleOpacity(Float value) {
    jsonObject.addProperty("circle-opacity", value);
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
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleStrokeWidth(Float value) {
    jsonObject.addProperty("circle-stroke-width", value);
  }

  /**
   * Get the CircleStrokeColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getCircleStrokeColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("circle-stroke-color").getAsString());
  }

  /**
   * Set the CircleStrokeColor property
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setCircleStrokeColor(@ColorInt int color) {
    jsonObject.addProperty("circle-stroke-color", ColorUtils.colorToRgbaString(color));
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
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleStrokeOpacity(Float value) {
    jsonObject.addProperty("circle-stroke-opacity", value);
  }
}
