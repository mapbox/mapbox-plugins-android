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
public class Line extends Annotation {

  /**
   * Create a line.
   *
   * @param id            the id of the line
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Line(long id, JsonObject jsonObject, Geometry geometry) {
    super(id, jsonObject, geometry);
  }

  /**
   * Set a list of LatLng for the line, which represents the locations of the line on the map
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param latLngs a list of the locations of the line in a longitude and latitude pairs
   */
  public void setLatLngs(List<LatLng> latLngs) {
    List<Point>points = new ArrayList<>();
    for (LatLng latLng : latLngs) {
      points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
    }
    geometry = LineString.fromLngLats(points);
  }

  /**
   * Set the Geometry of the line, which represents the location of the line on the map
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param geometry the geometry of the line
   */
  public void setGeometry(LineString geometry) {
    this.geometry = geometry;
  }

  // Property accessors

  /**
   * Get the LineJoin property
   *
   * @return property wrapper value around String
   */
  public String getLineJoin() {
    return jsonObject.get("line-join").getAsString();
  }

  /**
   * Set the LineJoin property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setLineJoin(@Property.LINE_JOIN String value) {
    jsonObject.addProperty("line-join", value);
  }

  /**
   * Get the LineOpacity property
   *
   * @return property wrapper value around Float
   */
  public Float getLineOpacity() {
    return jsonObject.get("line-opacity").getAsFloat();
  }

  /**
   * Set the LineOpacity property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineOpacity(Float value) {
    jsonObject.addProperty("line-opacity", value);
  }

  /**
   * Get the LineColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getLineColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("line-color").getAsString());
  }

  /**
   * Set the LineColor property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setLineColor(@ColorInt int color) {
    jsonObject.addProperty("line-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the LineWidth property
   *
   * @return property wrapper value around Float
   */
  public Float getLineWidth() {
    return jsonObject.get("line-width").getAsFloat();
  }

  /**
   * Set the LineWidth property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineWidth(Float value) {
    jsonObject.addProperty("line-width", value);
  }

  /**
   * Get the LineGapWidth property
   *
   * @return property wrapper value around Float
   */
  public Float getLineGapWidth() {
    return jsonObject.get("line-gap-width").getAsFloat();
  }

  /**
   * Set the LineGapWidth property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineGapWidth(Float value) {
    jsonObject.addProperty("line-gap-width", value);
  }

  /**
   * Get the LineOffset property
   *
   * @return property wrapper value around Float
   */
  public Float getLineOffset() {
    return jsonObject.get("line-offset").getAsFloat();
  }

  /**
   * Set the LineOffset property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineOffset(Float value) {
    jsonObject.addProperty("line-offset", value);
  }

  /**
   * Get the LineBlur property
   *
   * @return property wrapper value around Float
   */
  public Float getLineBlur() {
    return jsonObject.get("line-blur").getAsFloat();
  }

  /**
   * Set the LineBlur property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineBlur(Float value) {
    jsonObject.addProperty("line-blur", value);
  }

  /**
   * Get the LinePattern property
   *
   * @return property wrapper value around String
   */
  public String getLinePattern() {
    return jsonObject.get("line-pattern").getAsString();
  }

  /**
   * Set the LinePattern property
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setLinePattern(String value) {
    jsonObject.addProperty("line-pattern", value);
  }
}
