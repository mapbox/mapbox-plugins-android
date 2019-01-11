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
//import static com.mapbox.mapboxsdk.annotations.symbol.Symbol.Z_INDEX;

/**
 * The fill manager allows to add fills to a map.
 */
public class FillManager extends AnnotationManager<Fill, OnFillClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-fill-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-fill-layer";

  private FillLayer layer;
  private final MapClickResolver mapClickResolver;

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   */
  @UiThread
  public FillManager(@NonNull MapboxMap mapboxMap) {
    this(mapboxMap, null);
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public FillManager(@NonNull MapboxMap mapboxMap, @Nullable String belowLayerId) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new FillLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      ), belowLayerId);
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap     the map object to add fills to
   * @param geoJsonSource the geojson source to add fills to
   * @param layer         the fill layer to visualise Fills with
   */
  @VisibleForTesting
  public FillManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, @NonNull FillLayer layer, @Nullable String belowLayerId) {
    super(mapboxMap, geoJsonSource);
    initLayer(layer, belowLayerId);
    mapboxMap.addOnMapClickListener(mapClickResolver = new MapClickResolver(mapboxMap));
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
   * Cleanup fill manager, used to clear listeners
   */
  @UiThread
  @Override
  public void onDestroy() {
    super.onDestroy();
    mapboxMap.removeOnMapClickListener(mapClickResolver);
  }

  /**
   * Create a fill on the map from a LatLng coordinate.
   *
   * @param latLngs places to layout the fill on the map
   * @return the newly created fill
   */
  @UiThread
  public Fill createFill(@NonNull List<List<LatLng>> latLngs) {
    Fill fill = new Fill(this, currentId);
    fill.setLatLngs(latLngs);
    add(fill);
    return fill;
  }

  private static PropertyValue<?>[] getLayerDefinition() {
    return new PropertyValue[]{
      fillOpacity(get("fill-opacity")),
      fillColor(get("fill-color")),
      fillOutlineColor(get("fill-outline-color")),
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

  /**
   * Inner class for transforming map click events into fill clicks
   */
  private class MapClickResolver implements MapboxMap.OnMapClickListener {

    private MapboxMap mapboxMap;

    private MapClickResolver(MapboxMap mapboxMap) {
      this.mapboxMap = mapboxMap;
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
      if (clickListeners.isEmpty()) {
        return;
      }

      PointF screenLocation = mapboxMap.getProjection().toScreenLocation(point);
      List<Feature> features = mapboxMap.queryRenderedFeatures(screenLocation, ID_GEOJSON_LAYER);
      if (!features.isEmpty()) {
        long fillId = features.get(0).getProperty(Fill.ID_KEY).getAsLong();
        Fill fill = annotations.get(fillId);
        if (fill != null) {
          for (OnFillClickListener listener : clickListeners) {
            listener.onFillClick(fill);
          }
        }
      }
    }
  }
}
