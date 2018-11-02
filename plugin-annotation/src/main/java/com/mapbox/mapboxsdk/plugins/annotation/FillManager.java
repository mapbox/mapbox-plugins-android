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
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The fill manager allows to add fills to a map.
 */
public class FillManager extends AnnotationManager<Fill, FillOptions, OnFillDragListener, OnFillClickListener, OnFillLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-fill-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-fill-layer";

  private FillLayer layer;

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   */
  @UiThread
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this(mapView, mapboxMap, null);
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @Nullable String belowLayerId) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new FillLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      ), belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap     the map object to add fills to
   * @param geoJsonSource the geojson source to add fills to
   * @param layer         the fill layer to visualise Fills with
   */
  @VisibleForTesting
  public FillManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, @NonNull FillLayer layer, @Nullable String belowLayerId, DraggableAnnotationController<Fill, OnFillDragListener> draggableAnnotationController) {
    super(mapboxMap, geoJsonSource, null, draggableAnnotationController);
    initLayer(layer, belowLayerId);
  }

  /**
   * Initialise the layer on the map.
   *
   * @param layer the layer to be added
   * @param belowLayerId the id of the layer above the circle layer
   */
  private void initLayer(@NonNull FillLayer layer, @Nullable String belowLayerId) {
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
    return Fill.ID_KEY;
  }

  private static PropertyValue<?>[] getLayerDefinition() {
    return new PropertyValue[]{
      fillOpacity(get("fill-opacity")),
      fillColor(get("fill-color")),
      fillOutlineColor(get("fill-outline-color")),
      fillPattern(get("fill-pattern")),
    };
  }

  // Property accessors
  /**
   * Get the FillAntialias property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getFillAntialias() {
    return layer.getFillAntialias().value;
  }

  /**
   * Set the FillAntialias property
   *
   * @param value property wrapper value around Boolean
   */
  public void setFillAntialias( Boolean value) {
    layer.setProperties(fillAntialias(value));
  }

  /**
   * Get the FillTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getFillTranslate() {
    return layer.getFillTranslate().value;
  }

  /**
   * Set the FillTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setFillTranslate( Float[] value) {
    layer.setProperties(fillTranslate(value));
  }

  /**
   * Get the FillTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getFillTranslateAnchor() {
    return layer.getFillTranslateAnchor().value;
  }

  /**
   * Set the FillTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setFillTranslateAnchor(@Property.FILL_TRANSLATE_ANCHOR String value) {
    layer.setProperties(fillTranslateAnchor(value));
  }

}
