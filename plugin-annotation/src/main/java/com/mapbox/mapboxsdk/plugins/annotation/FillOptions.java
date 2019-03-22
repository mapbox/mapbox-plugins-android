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
 * Builder class from which a fill is created.
 */
public class FillOptions extends Options<Fill> {

  private boolean isDraggable;
  private Polygon geometry;
  private Float fillOpacity;
  private String fillColor;
  private String fillOutlineColor;
  private String fillPattern;

  /**
   * Set fill-opacity to initialise the fill with.
   * <p>
   * The opacity of the entire fill layer. In contrast to the {@link PropertyFactory#fillColor}, this value will also affect the 1px stroke around the fill, if the stroke is used.
   * </p>
   * @param fillOpacity the fill-opacity value
   * @return this
   */
  public FillOptions withFillOpacity(Float fillOpacity) {
    this.fillOpacity =  fillOpacity;
    return this;
  }

  /**
   * Get the current configured  fill-opacity for the fill
   * <p>
   * The opacity of the entire fill layer. In contrast to the {@link PropertyFactory#fillColor}, this value will also affect the 1px stroke around the fill, if the stroke is used.
   * </p>
   * @return fillOpacity value
   */
  public Float getFillOpacity() {
    return fillOpacity;
  }

  /**
   * Set fill-color to initialise the fill with.
   * <p>
   * The color of the filled part of this layer. This color can be specified as `rgba` with an alpha component and the color's opacity will not affect the opacity of the 1px stroke, if it is used.
   * </p>
   * @param fillColor the fill-color value
   * @return this
   */
  public FillOptions withFillColor(String fillColor) {
    this.fillColor =  fillColor;
    return this;
  }

  /**
   * Get the current configured  fill-color for the fill
   * <p>
   * The color of the filled part of this layer. This color can be specified as `rgba` with an alpha component and the color's opacity will not affect the opacity of the 1px stroke, if it is used.
   * </p>
   * @return fillColor value
   */
  public String getFillColor() {
    return fillColor;
  }

  /**
   * Set fill-outline-color to initialise the fill with.
   * <p>
   * The outline color of the fill. Matches the value of {@link PropertyFactory#fillColor} if unspecified.
   * </p>
   * @param fillOutlineColor the fill-outline-color value
   * @return this
   */
  public FillOptions withFillOutlineColor(String fillOutlineColor) {
    this.fillOutlineColor =  fillOutlineColor;
    return this;
  }

  /**
   * Get the current configured  fill-outline-color for the fill
   * <p>
   * The outline color of the fill. Matches the value of {@link PropertyFactory#fillColor} if unspecified.
   * </p>
   * @return fillOutlineColor value
   */
  public String getFillOutlineColor() {
    return fillOutlineColor;
  }

  /**
   * Set fill-pattern to initialise the fill with.
   * <p>
   * Name of image in sprite to use for drawing image fills. For seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
   * </p>
   * @param fillPattern the fill-pattern value
   * @return this
   */
  public FillOptions withFillPattern(String fillPattern) {
    this.fillPattern =  fillPattern;
    return this;
  }

  /**
   * Get the current configured  fill-pattern for the fill
   * <p>
   * Name of image in sprite to use for drawing image fills. For seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
   * </p>
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

  /**
   * Creates FillOptions out of a Feature.
   *
   * @param feature feature to be converted
   */
  @Nullable
  static FillOptions fromFeature(@NonNull Feature feature) {
    if (feature.geometry() == null) {
      throw new RuntimeException("geometry field is required");
    }
    if (!(feature.geometry() instanceof Polygon)) {      return null;
    }

    FillOptions options = new FillOptions();
    options.geometry = (Polygon) feature.geometry();
    if (feature.hasProperty("fill-opacity")) {
      options.fillOpacity = feature.getProperty("fill-opacity").getAsFloat();
    }
    if (feature.hasProperty("fill-color")) {
      options.fillColor = feature.getProperty("fill-color").getAsString();
    }
    if (feature.hasProperty("fill-outline-color")) {
      options.fillOutlineColor = feature.getProperty("fill-outline-color").getAsString();
    }
    if (feature.hasProperty("fill-pattern")) {
      options.fillPattern = feature.getProperty("fill-pattern").getAsString();
    }
    if (feature.hasProperty("is-draggable")) {
      options.isDraggable = feature.getProperty("is-draggable").getAsBoolean();
    }
    return options;
  }
}
