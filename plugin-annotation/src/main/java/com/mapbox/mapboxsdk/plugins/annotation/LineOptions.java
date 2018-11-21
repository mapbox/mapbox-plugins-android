// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.*;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.style.layers.Property;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.convertArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toFloatArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toStringArray;

/**
 * Builder class from which a line is created.
 */
public class LineOptions extends Options<Line> {

  private boolean isDraggable;
  private LineString geometry;
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

  /**
   * Returns whether this line is draggable, meaning it can be dragged across the screen when touched and moved.
   *
   * @return draggable when touched
   */
  public boolean isDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this line should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public LineOptions setDraggable(boolean draggable) {
    isDraggable = draggable;
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
    Line line = new Line(id, annotationManager, jsonObject, geometry);
    line.setDraggable(isDraggable);
    return line;
  }

  /**
   * Creates LineOptions out of a Feature.
   * <p>
   * All supported properties are:<br>
   * "line-join" - String<br>
   * "line-opacity" - Float<br>
   * "line-color" - String<br>
   * "line-width" - Float<br>
   * "line-gap-width" - Float<br>
   * "line-offset" - Float<br>
   * "line-blur" - Float<br>
   * "line-pattern" - String<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the line should be draggable, false otherwise
   *
   * @param feature feature to be converted
   */
  @Nullable
  static LineOptions fromFeature(@NonNull Feature feature) {
    if (feature.geometry() == null) {
      throw new RuntimeException("geometry field is required");
    }
    if (!(feature.geometry() instanceof LineString)) {
      return null;
    }

    LineOptions options = new LineOptions();
    options.geometry = (LineString) feature.geometry();
    if (feature.hasProperty("line-join")) {
      options.lineJoin = feature.getProperty("line-join").getAsString();
    }
    if (feature.hasProperty("line-opacity")) {
      options.lineOpacity = feature.getProperty("line-opacity").getAsFloat();
    }
    if (feature.hasProperty("line-color")) {
      options.lineColor = feature.getProperty("line-color").getAsString();
    }
    if (feature.hasProperty("line-width")) {
      options.lineWidth = feature.getProperty("line-width").getAsFloat();
    }
    if (feature.hasProperty("line-gap-width")) {
      options.lineGapWidth = feature.getProperty("line-gap-width").getAsFloat();
    }
    if (feature.hasProperty("line-offset")) {
      options.lineOffset = feature.getProperty("line-offset").getAsFloat();
    }
    if (feature.hasProperty("line-blur")) {
      options.lineBlur = feature.getProperty("line-blur").getAsFloat();
    }
    if (feature.hasProperty("line-pattern")) {
      options.linePattern = feature.getProperty("line-pattern").getAsString();
    }
    if (feature.hasProperty("is-draggable")) {
      options.isDraggable = feature.getProperty("is-draggable").getAsBoolean();
    }
    return options;
  }
}
