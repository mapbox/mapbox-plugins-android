// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.*;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.convertArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toFloatArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toStringArray;

/**
 * Builder class from which a circle is created.
 */
public class CircleOptions extends Options<Circle> {

  private boolean isDraggable;
  private Point geometry;
  private Float circleRadius;
  private String circleColor;
  private Float circleBlur;
  private Float circleOpacity;
  private Float circleStrokeWidth;
  private String circleStrokeColor;
  private Float circleStrokeOpacity;

  static final String PROPERTY_circleRadius = "circle-radius";
  static final String PROPERTY_circleColor = "circle-color";
  static final String PROPERTY_circleBlur = "circle-blur";
  static final String PROPERTY_circleOpacity = "circle-opacity";
  static final String PROPERTY_circleStrokeWidth = "circle-stroke-width";
  static final String PROPERTY_circleStrokeColor = "circle-stroke-color";
  static final String PROPERTY_circleStrokeOpacity = "circle-stroke-opacity";
  private static final String PROPERTY_isDraggable = "is-draggable";

  /**
   * Set circle-radius to initialise the circle with.
   * <p>
   * Circle radius.
   * </p>
   * @param circleRadius the circle-radius value
   * @return this
   */
  public CircleOptions withCircleRadius(Float circleRadius) {
    this.circleRadius =  circleRadius;
    return this;
  }

  /**
   * Get the current configured  circle-radius for the circle
   * <p>
   * Circle radius.
   * </p>
   * @return circleRadius value
   */
  public Float getCircleRadius() {
    return circleRadius;
  }

  /**
   * Set circle-color to initialise the circle with.
   * <p>
   * The fill color of the circle.
   * </p>
   * @param circleColor the circle-color value
   * @return this
   */
  public CircleOptions withCircleColor(String circleColor) {
    this.circleColor =  circleColor;
    return this;
  }

  /**
   * Get the current configured  circle-color for the circle
   * <p>
   * The fill color of the circle.
   * </p>
   * @return circleColor value
   */
  public String getCircleColor() {
    return circleColor;
  }

  /**
   * Set circle-blur to initialise the circle with.
   * <p>
   * Amount to blur the circle. 1 blurs the circle such that only the centerpoint is full opacity.
   * </p>
   * @param circleBlur the circle-blur value
   * @return this
   */
  public CircleOptions withCircleBlur(Float circleBlur) {
    this.circleBlur =  circleBlur;
    return this;
  }

  /**
   * Get the current configured  circle-blur for the circle
   * <p>
   * Amount to blur the circle. 1 blurs the circle such that only the centerpoint is full opacity.
   * </p>
   * @return circleBlur value
   */
  public Float getCircleBlur() {
    return circleBlur;
  }

  /**
   * Set circle-opacity to initialise the circle with.
   * <p>
   * The opacity at which the circle will be drawn.
   * </p>
   * @param circleOpacity the circle-opacity value
   * @return this
   */
  public CircleOptions withCircleOpacity(Float circleOpacity) {
    this.circleOpacity =  circleOpacity;
    return this;
  }

  /**
   * Get the current configured  circle-opacity for the circle
   * <p>
   * The opacity at which the circle will be drawn.
   * </p>
   * @return circleOpacity value
   */
  public Float getCircleOpacity() {
    return circleOpacity;
  }

  /**
   * Set circle-stroke-width to initialise the circle with.
   * <p>
   * The width of the circle's stroke. Strokes are placed outside of the {@link PropertyFactory#circleRadius}.
   * </p>
   * @param circleStrokeWidth the circle-stroke-width value
   * @return this
   */
  public CircleOptions withCircleStrokeWidth(Float circleStrokeWidth) {
    this.circleStrokeWidth =  circleStrokeWidth;
    return this;
  }

  /**
   * Get the current configured  circle-stroke-width for the circle
   * <p>
   * The width of the circle's stroke. Strokes are placed outside of the {@link PropertyFactory#circleRadius}.
   * </p>
   * @return circleStrokeWidth value
   */
  public Float getCircleStrokeWidth() {
    return circleStrokeWidth;
  }

  /**
   * Set circle-stroke-color to initialise the circle with.
   * <p>
   * The stroke color of the circle.
   * </p>
   * @param circleStrokeColor the circle-stroke-color value
   * @return this
   */
  public CircleOptions withCircleStrokeColor(String circleStrokeColor) {
    this.circleStrokeColor =  circleStrokeColor;
    return this;
  }

  /**
   * Get the current configured  circle-stroke-color for the circle
   * <p>
   * The stroke color of the circle.
   * </p>
   * @return circleStrokeColor value
   */
  public String getCircleStrokeColor() {
    return circleStrokeColor;
  }

  /**
   * Set circle-stroke-opacity to initialise the circle with.
   * <p>
   * The opacity of the circle's stroke.
   * </p>
   * @param circleStrokeOpacity the circle-stroke-opacity value
   * @return this
   */
  public CircleOptions withCircleStrokeOpacity(Float circleStrokeOpacity) {
    this.circleStrokeOpacity =  circleStrokeOpacity;
    return this;
  }

  /**
   * Get the current configured  circle-stroke-opacity for the circle
   * <p>
   * The opacity of the circle's stroke.
   * </p>
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

  /**
   * Set the geometry of the circle, which represents the location of the circle on the map
   *
   * @param geometry the location of the circle
   * @return this
   */
  public CircleOptions withGeometry(Point geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Returns whether this circle is draggable, meaning it can be dragged across the screen when touched and moved.
   *
   * @return draggable when touched
   */
  public boolean getDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this circle should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public CircleOptions withDraggable(boolean draggable) {
    isDraggable = draggable;
    return this;
  }

  @Override
  Circle build(long id, AnnotationManager<?, Circle, ?, ?, ?, ?> annotationManager) {
    if (geometry == null) {
      throw new RuntimeException("geometry field is required");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(PROPERTY_circleRadius, circleRadius);
    jsonObject.addProperty(PROPERTY_circleColor, circleColor);
    jsonObject.addProperty(PROPERTY_circleBlur, circleBlur);
    jsonObject.addProperty(PROPERTY_circleOpacity, circleOpacity);
    jsonObject.addProperty(PROPERTY_circleStrokeWidth, circleStrokeWidth);
    jsonObject.addProperty(PROPERTY_circleStrokeColor, circleStrokeColor);
    jsonObject.addProperty(PROPERTY_circleStrokeOpacity, circleStrokeOpacity);
    Circle circle = new Circle(id, annotationManager, jsonObject, geometry);
    circle.setDraggable(isDraggable);
    return circle;
  }

  /**
   * Creates CircleOptions out of a Feature.
   *
   * @param feature feature to be converted
   */
  @Nullable
  static CircleOptions fromFeature(@NonNull Feature feature) {
    if (feature.geometry() == null) {
      throw new RuntimeException("geometry field is required");
    }
    if (!(feature.geometry() instanceof Point)) {
      return null;
    }

    CircleOptions options = new CircleOptions();
    options.geometry = (Point) feature.geometry();
    if (feature.hasProperty(PROPERTY_circleRadius)) {
      options.circleRadius = feature.getProperty(PROPERTY_circleRadius).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_circleColor)) {
      options.circleColor = feature.getProperty(PROPERTY_circleColor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_circleBlur)) {
      options.circleBlur = feature.getProperty(PROPERTY_circleBlur).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_circleOpacity)) {
      options.circleOpacity = feature.getProperty(PROPERTY_circleOpacity).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_circleStrokeWidth)) {
      options.circleStrokeWidth = feature.getProperty(PROPERTY_circleStrokeWidth).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_circleStrokeColor)) {
      options.circleStrokeColor = feature.getProperty(PROPERTY_circleStrokeColor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_circleStrokeOpacity)) {
      options.circleStrokeOpacity = feature.getProperty(PROPERTY_circleStrokeOpacity).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_isDraggable)) {
      options.isDraggable = feature.getProperty(PROPERTY_isDraggable).getAsBoolean();
    }
    return options;
  }
}
