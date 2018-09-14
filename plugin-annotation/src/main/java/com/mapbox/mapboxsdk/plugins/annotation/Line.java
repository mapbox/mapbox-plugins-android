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
public class Line extends Annotation {

  private final LineManager lineManager;

  /**
   * Create a line.
   *
   * @param manager the line manager created and managing the line
   * @param id            the id of the line
   */
  Line(LineManager manager, long id) {
    super(id);
    this.lineManager = manager;
  }

  /**
   * Called to update the underlying data source.
   */
  public void update() {
    lineManager.updateSource();
  }

  /**
   * Set a list of LatLng for the line, which represents the locations of the line on the map
   *
   * @param latLngs a list of the locations of the line in a longitude and latitude pairs
   */
  public void setLatLngs(List<LatLng> latLngs) {
    List<Point>points = new ArrayList<>();
    for (LatLng latLng : latLngs) {
      points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
    }
    geometry = LineString.fromLngLats(points);
    lineManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setLineJoin(@Property.LINE_JOIN String value) {
    jsonObject.addProperty("line-join", value);
    lineManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setLineOpacity(Float value) {
    jsonObject.addProperty("line-opacity", value);
    lineManager.updateSource();
  }

  /**
   * Get the LineColor property
   *
   * @return property wrapper value around String
   */
  public String getLineColor() {
    return jsonObject.get("line-color").getAsString();
  }

  /**
   * Set the LineColor property
   *
   * @param value constant property value for String
   */
  public void setLineColor(String value) {
    jsonObject.addProperty("line-color", value);
    lineManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setLineWidth(Float value) {
    jsonObject.addProperty("line-width", value);
    lineManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setLineGapWidth(Float value) {
    jsonObject.addProperty("line-gap-width", value);
    lineManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setLineOffset(Float value) {
    jsonObject.addProperty("line-offset", value);
    lineManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setLineBlur(Float value) {
    jsonObject.addProperty("line-blur", value);
    lineManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setLinePattern(String value) {
    jsonObject.addProperty("line-pattern", value);
    lineManager.updateSource();
  }

}
