// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LongSparseArray;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The line manager allows to add lines to a map.
 */
public class LineManager extends AnnotationManager<Line, LineOptions, OnLineDragListener, OnLineClickListener, OnLineLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-line-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-line-layer";

  private LineLayer layer;

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap the map object to add lines to
   */
  @UiThread
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this(mapView, mapboxMap, null);
  }

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap the map object to add lines to
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @Nullable String belowLayerId) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new LineLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      ), belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap     the map object to add lines to
   * @param geoJsonSource the geojson source to add lines to
   * @param layer         the line layer to visualise Lines with
   */
  @VisibleForTesting
  public LineManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, @NonNull LineLayer layer, @Nullable String belowLayerId, DraggableAnnotationController<Line, OnLineDragListener> draggableAnnotationController) {
    super(mapboxMap, geoJsonSource, null, draggableAnnotationController);
    initLayer(layer, belowLayerId);
  }

  /**
   * Initialise the layer on the map.
   *
   * @param layer the layer to be added
   * @param belowLayerId the id of the layer above the circle layer
   */
  private void initLayer(@NonNull LineLayer layer, @Nullable String belowLayerId) {
    this.layer = layer;
    if (belowLayerId == null) {
      mapboxMap.addLayer(layer);
    } else {
      mapboxMap.addLayerBelow(layer, belowLayerId);
    }
  }

  /**
   * Get the layer id of the annotation layer.
   *
   * @return the layer id
   */
  @Override
  String getAnnotationLayerId() {
    return ID_GEOJSON_LAYER;
  }

  /**
   * Get the key of the id of the annotation.
   *
   * @return the key of the id of the annotation
   */
  @Override
  String getAnnotationIdKey() {
    return Line.ID_KEY;
  }

  private static PropertyValue<?>[] getLayerDefinition() {
    return new PropertyValue[]{
      lineJoin(get("line-join")),
      lineOpacity(get("line-opacity")),
      lineColor(get("line-color")),
      lineWidth(get("line-width")),
      lineGapWidth(get("line-gap-width")),
      lineOffset(get("line-offset")),
      lineBlur(get("line-blur")),
      linePattern(get("line-pattern")),
    };
  }

  // Property accessors
  /**
   * Get the LineCap property
   *
   * @return property wrapper value around String
   */
  public String getLineCap() {
    return layer.getLineCap().value;
  }

  /**
   * Set the LineCap property
   *
   * @param value property wrapper value around String
   */
  public void setLineCap(@Property.LINE_CAP String value) {
    layer.setProperties(lineCap(value));
  }

  /**
   * Get the LineMiterLimit property
   *
   * @return property wrapper value around Float
   */
  public Float getLineMiterLimit() {
    return layer.getLineMiterLimit().value;
  }

  /**
   * Set the LineMiterLimit property
   *
   * @param value property wrapper value around Float
   */
  public void setLineMiterLimit( Float value) {
    layer.setProperties(lineMiterLimit(value));
  }

  /**
   * Get the LineRoundLimit property
   *
   * @return property wrapper value around Float
   */
  public Float getLineRoundLimit() {
    return layer.getLineRoundLimit().value;
  }

  /**
   * Set the LineRoundLimit property
   *
   * @param value property wrapper value around Float
   */
  public void setLineRoundLimit( Float value) {
    layer.setProperties(lineRoundLimit(value));
  }

  /**
   * Get the LineTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getLineTranslate() {
    return layer.getLineTranslate().value;
  }

  /**
   * Set the LineTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setLineTranslate( Float[] value) {
    layer.setProperties(lineTranslate(value));
  }

  /**
   * Get the LineTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getLineTranslateAnchor() {
    return layer.getLineTranslateAnchor().value;
  }

  /**
   * Set the LineTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setLineTranslateAnchor(@Property.LINE_TRANSLATE_ANCHOR String value) {
    layer.setProperties(lineTranslateAnchor(value));
  }

  /**
   * Get the LineDasharray property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getLineDasharray() {
    return layer.getLineDasharray().value;
  }

  /**
   * Set the LineDasharray property
   *
   * @param value property wrapper value around Float[]
   */
  public void setLineDasharray( Float[] value) {
    layer.setProperties(lineDasharray(value));
  }

}
