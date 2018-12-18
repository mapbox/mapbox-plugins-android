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
  public boolean isDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this circle should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public CircleOptions setDraggable(boolean draggable) {
    isDraggable = draggable;
    return this;
  }

  @Override
  Circle build(long id, AnnotationManager<?, Circle, ?, ?, ?, ?> annotationManager) {
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
    Circle circle = new Circle(id, annotationManager, jsonObject, geometry);
    circle.setDraggable(isDraggable);
    return circle;
  }

  /**
   * Creates CircleOptions out of a Feature.
   * <p>
   * All supported properties are:<br>
   * "circle-radius" - Float<br>
   * "circle-color" - String<br>
   * "circle-blur" - Float<br>
   * "circle-opacity" - Float<br>
   * "circle-stroke-width" - Float<br>
   * "circle-stroke-color" - String<br>
   * "circle-stroke-opacity" - Float<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the circle should be draggable, false otherwise
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
    if (feature.hasProperty("circle-radius")) {
      options.circleRadius = feature.getProperty("circle-radius").getAsFloat();
    }
    if (feature.hasProperty("circle-color")) {
      options.circleColor = feature.getProperty("circle-color").getAsString();
    }
    if (feature.hasProperty("circle-blur")) {
      options.circleBlur = feature.getProperty("circle-blur").getAsFloat();
    }
    if (feature.hasProperty("circle-opacity")) {
      options.circleOpacity = feature.getProperty("circle-opacity").getAsFloat();
    }
    if (feature.hasProperty("circle-stroke-width")) {
      options.circleStrokeWidth = feature.getProperty("circle-stroke-width").getAsFloat();
    }
    if (feature.hasProperty("circle-stroke-color")) {
      options.circleStrokeColor = feature.getProperty("circle-stroke-color").getAsString();
    }
    if (feature.hasProperty("circle-stroke-opacity")) {
      options.circleStrokeOpacity = feature.getProperty("circle-stroke-opacity").getAsFloat();
    }
    if (feature.hasProperty("is-draggable")) {
      options.isDraggable = feature.getProperty("is-draggable").getAsBoolean();
    }
    return options;
  }
}
