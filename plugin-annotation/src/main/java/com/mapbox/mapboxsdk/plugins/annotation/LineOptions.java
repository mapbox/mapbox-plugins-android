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
 * Builder class from which a line is created.
 */
public class LineOptions extends Options<Line> {

  private Geometry geometry;
  private String lineJoin;
  private Float lineOpacity;
  private String lineColor;
  private Float lineWidth;
  private Float lineGapWidth;
  private Float lineOffset;
  private Float lineBlur;
  private String linePattern;

  /**
   * Set line-join to initialise the line with.
   *
   * @param lineJoin the line-join value
   * @return this
   */
  public LineOptions withLineJoin(@Property.LINE_JOIN String lineJoin) {
    this.lineJoin =  lineJoin;
    return this;
  }

  /**
   * Get the current configured  line-join for the line
   *
   * @return lineJoin value
   */
  public String getLineJoin() {
    return lineJoin;
  }

  /**
   * Set line-opacity to initialise the line with.
   *
   * @param lineOpacity the line-opacity value
   * @return this
   */
  public LineOptions withLineOpacity(Float lineOpacity) {
    this.lineOpacity =  lineOpacity;
    return this;
  }

  /**
   * Get the current configured  line-opacity for the line
   *
   * @return lineOpacity value
   */
  public Float getLineOpacity() {
    return lineOpacity;
  }

  /**
   * Set line-color to initialise the line with.
   *
   * @param lineColor the line-color value
   * @return this
   */
  public LineOptions withLineColor(String lineColor) {
    this.lineColor =  lineColor;
    return this;
  }

  /**
   * Get the current configured  line-color for the line
   *
   * @return lineColor value
   */
  public String getLineColor() {
    return lineColor;
  }

  /**
   * Set line-width to initialise the line with.
   *
   * @param lineWidth the line-width value
   * @return this
   */
  public LineOptions withLineWidth(Float lineWidth) {
    this.lineWidth =  lineWidth;
    return this;
  }

  /**
   * Get the current configured  line-width for the line
   *
   * @return lineWidth value
   */
  public Float getLineWidth() {
    return lineWidth;
  }

  /**
   * Set line-gap-width to initialise the line with.
   *
   * @param lineGapWidth the line-gap-width value
   * @return this
   */
  public LineOptions withLineGapWidth(Float lineGapWidth) {
    this.lineGapWidth =  lineGapWidth;
    return this;
  }

  /**
   * Get the current configured  line-gap-width for the line
   *
   * @return lineGapWidth value
   */
  public Float getLineGapWidth() {
    return lineGapWidth;
  }

  /**
   * Set line-offset to initialise the line with.
   *
   * @param lineOffset the line-offset value
   * @return this
   */
  public LineOptions withLineOffset(Float lineOffset) {
    this.lineOffset =  lineOffset;
    return this;
  }

  /**
   * Get the current configured  line-offset for the line
   *
   * @return lineOffset value
   */
  public Float getLineOffset() {
    return lineOffset;
  }

  /**
   * Set line-blur to initialise the line with.
   *
   * @param lineBlur the line-blur value
   * @return this
   */
  public LineOptions withLineBlur(Float lineBlur) {
    this.lineBlur =  lineBlur;
    return this;
  }

  /**
   * Get the current configured  line-blur for the line
   *
   * @return lineBlur value
   */
  public Float getLineBlur() {
    return lineBlur;
  }

  /**
   * Set line-pattern to initialise the line with.
   *
   * @param linePattern the line-pattern value
   * @return this
   */
  public LineOptions withLinePattern(String linePattern) {
    this.linePattern =  linePattern;
    return this;
  }

  /**
   * Get the current configured  line-pattern for the line
   *
   * @return linePattern value
   */
  public String getLinePattern() {
    return linePattern;
  }

  /**
   * Set a list of LatLng for the line, which represents the locations of the line on the map
   *
   * @param latLngs a list of the locations of the line in a longitude and latitude pairs
   * @return this
   */
  public LineOptions withLatLngs(List<LatLng> latLngs) {
    List<Point>points = new ArrayList<>();
    for (LatLng latLng : latLngs) {
      points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
    }
    geometry = LineString.fromLngLats(points);
    return this;
  }

  /**
   * Set the geometry of the line, which represents the location of the line on the map
   *
   * @param geometry the location of the line
   * @return this
   */
  public LineOptions withGeometry(LineString geometry) {
    this.geometry = geometry;
    return this;
  }

  @Override
  Line build(long id, AnnotationManager<?, Line, ?, ?, ?, ?> annotationManager) {
    if (geometry == null) {
      throw new RuntimeException("geometry field is required");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("line-join", lineJoin);
    jsonObject.addProperty("line-opacity", lineOpacity);
    jsonObject.addProperty("line-color", lineColor);
    jsonObject.addProperty("line-width", lineWidth);
    jsonObject.addProperty("line-gap-width", lineGapWidth);
    jsonObject.addProperty("line-offset", lineOffset);
    jsonObject.addProperty("line-blur", lineBlur);
    jsonObject.addProperty("line-pattern", linePattern);
    return new Line(id, annotationManager, jsonObject, geometry);
  }
}
