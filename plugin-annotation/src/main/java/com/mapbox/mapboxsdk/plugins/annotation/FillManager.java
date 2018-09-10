package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LongSparseArray;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillAntialias;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOutlineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillTranslateAnchor;

/**
 * The fill manager allows to add fills to a map.
 */
public class FillManager {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-fill-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-fill-layer";

  // map integration components
  private MapboxMap mapboxMap;
  private GeoJsonSource geoJsonSource;
  private FillLayer layer;

  // callback listeners
  private List<OnFillClickListener> fillClickListeners = new ArrayList<>();
  private final MapClickResolver mapClickResolver;

  // internal data set
  private final LongSparseArray<Fill> fills = new LongSparseArray<>();
  private final List<Feature> features = new ArrayList<>();
  private long currentId;

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   */
  @UiThread
  public FillManager(@NonNull MapboxMap mapboxMap) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new FillLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      )
    );
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap     the map object to add fills to
   * @param geoJsonSource the geojson source to add fills to
   * @param layer         the fill layer to visualise Fills with
   */
  @VisibleForTesting
  public FillManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, FillLayer layer) {
    this.mapboxMap = mapboxMap;
    this.geoJsonSource = geoJsonSource;
    this.layer = layer;
    mapboxMap.addSource(geoJsonSource);
    mapboxMap.addLayer(layer);
    mapboxMap.addOnMapClickListener(mapClickResolver = new MapClickResolver(mapboxMap));
  }

  /**
   * Cleanup fill manager, used to clear listeners
   */
  @UiThread
  public void onDestroy() {
    mapboxMap.removeOnMapClickListener(mapClickResolver);
    fillClickListeners.clear();
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
    fills.put(currentId, fill);
    currentId++;
    return fill;
  }

  /**
   * Delete a fill from the map.
   *
   * @param fill to be deleted
   */
  @UiThread
  public void deleteFill(@NonNull Fill fill) {
    fills.remove(fill.getId());
    updateSource();
  }

  /**
   * Get a list of current fills.
   *
   * @return list of fills
   */
  @UiThread
  public LongSparseArray<Fill> getFills() {
    return fills;
  }

  /**
   * Trigger an update to the underlying source
   */
  public void updateSource() {
    // todo move feature creation to a background thread?
    features.clear();
    Fill fill;
    for (int i = 0; i < fills.size(); i++) {
      fill = fills.valueAt(i);
      features.add(Feature.fromGeometry(fill.getGeometry(), fill.getFeature()));
    }
    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));
  }

  /**
   * Add a callback to be invoked when a fill has been clicked.
   *
   * @param listener the callback to be invoked when a fill is clicked
   */
  @UiThread
  public void addOnFillClickListener(@NonNull OnFillClickListener listener) {
    fillClickListeners.add(listener);
  }

  /**
   * Remove a previously added callback that was to be invoked when fill has been clicked.
   *
   * @param listener the callback to be removed
   */
  @UiThread
  public void removeOnFillClickListener(@NonNull OnFillClickListener listener) {
    if (fillClickListeners.contains(listener)) {
      fillClickListeners.remove(listener);
    }
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
  public void setFillAntialias(Boolean value) {
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
  public void setFillTranslate(Float[] value) {
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
  public void setFillTranslateAnchor(String value) {
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
      if (fillClickListeners.isEmpty()) {
        return;
      }

      PointF screenLocation = mapboxMap.getProjection().toScreenLocation(point);
      List<Feature> features = mapboxMap.queryRenderedFeatures(screenLocation, ID_GEOJSON_LAYER);
      if (!features.isEmpty()) {
        long fillId = features.get(0).getProperty(Fill.ID_KEY).getAsLong();
        Fill fill = fills.get(fillId);
        if (fill != null) {
          for (OnFillClickListener listener : fillClickListeners) {
            listener.onFillClick(fill);
          }
        }
      }
    }
  }

}
