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
 * Builder class from which a fill is created.
 */
public class FillOptions extends Options<Fill> {

  private boolean isDraggable;
  private Geometry geometry;
  private Float fillOpacity;
  private String fillColor;
  private String fillOutlineColor;
  private String fillPattern;

  /**
   * Set fill-opacity to initialise the fill with.
   *
   * @param fillOpacity the fill-opacity value
   * @return this
   */
  public FillOptions withFillOpacity(Float fillOpacity) {
    this.fillOpacity =  fillOpacity;
    return this;
  }

  /**
   * Get the current configured  fill-opacity for the fill
   *
   * @return fillOpacity value
   */
  public Float getFillOpacity() {
    return fillOpacity;
  }

  /**
   * Set fill-color to initialise the fill with.
   *
   * @param fillColor the fill-color value
   * @return this
   */
  public FillOptions withFillColor(String fillColor) {
    this.fillColor =  fillColor;
    return this;
  }

  /**
   * Get the current configured  fill-color for the fill
   *
   * @return fillColor value
   */
  public String getFillColor() {
    return fillColor;
  }

  /**
   * Set fill-outline-color to initialise the fill with.
   *
   * @param fillOutlineColor the fill-outline-color value
   * @return this
   */
  public FillOptions withFillOutlineColor(String fillOutlineColor) {
    this.fillOutlineColor =  fillOutlineColor;
    return this;
  }

  /**
   * Get the current configured  fill-outline-color for the fill
   *
   * @return fillOutlineColor value
   */
  public String getFillOutlineColor() {
    return fillOutlineColor;
  }

  /**
   * Set fill-pattern to initialise the fill with.
   *
   * @param fillPattern the fill-pattern value
   * @return this
   */
  public FillOptions withFillPattern(String fillPattern) {
    this.fillPattern =  fillPattern;
    return this;
  }

  /**
   * Get the current configured  fill-pattern for the fill
   *
   * @return fillPattern value
   */
  public String getFillPattern() {
    return fillPattern;
  }

  /**
   * Set a list of lists of LatLng for the fill, which represents the locations of the fill on the map
   *
   * @param latLngs a list of a lists of the locations of the line in a longitude and latitude pairs
   * @return this
   */
  public FillOptions withLatLngs(List<List<LatLng>> latLngs) {
    List<List<Point>> points = new ArrayList<>();
    for (List<LatLng> innerLatLngs : latLngs) {
      List<Point>innerList = new ArrayList<>();
      for (LatLng latLng : innerLatLngs) {
        innerList.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
      }
      points.add(innerList);
    }
    geometry = Polygon.fromLngLats(points);
    return this;
  }

  /**
   * Set the geometry of the fill, which represents the location of the fill on the map
   *
   * @param geometry the location of the fill
   * @return this
   */
  public FillOptions withGeometry(Polygon geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Returns whether this fill is draggable, meaning it can be dragged across the screen when touched and moved.
   *
   * @return draggable when touched
   */
  public boolean isDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this fill should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public FillOptions setDraggable(boolean draggable) {
    isDraggable = draggable;
    return this;
  }

  @Override
  Fill build(long id, AnnotationManager<?, Fill, ?, ?, ?, ?> annotationManager) {
    if (geometry == null) {
      throw new RuntimeException("geometry field is required");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("fill-opacity", fillOpacity);
    jsonObject.addProperty("fill-color", fillColor);
    jsonObject.addProperty("fill-outline-color", fillOutlineColor);
    jsonObject.addProperty("fill-pattern", fillPattern);
    Fill fill = new Fill(id, annotationManager, jsonObject, geometry);
    fill.setDraggable(isDraggable);
    return fill;
  }
}
