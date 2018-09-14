package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.util.LongSparseArray;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public abstract class AnnotationManager<T extends Annotation,U> {

  protected final MapboxMap mapboxMap;
  protected final LongSparseArray<T> annotations = new LongSparseArray<>();
  protected final List<Feature> features = new ArrayList<>();
  protected final List<U> clickListeners = new ArrayList<>();
  protected long currentId;

  private final GeoJsonSource geoJsonSource;

  @UiThread
  protected AnnotationManager(MapboxMap mapboxMap, GeoJsonSource geoJsonSource) {
    this.mapboxMap = mapboxMap;
    this.geoJsonSource = geoJsonSource;
    mapboxMap.addSource(geoJsonSource);
  }

  /**
   * Get a list of current annotations.
   *
   * @return long sparse array of annotations
   */
  @UiThread
  public LongSparseArray<T> getAnnotations() {
    return annotations;
  }

  @UiThread
  void add(@NonNull T t) {
    annotations.put(currentId, t);
    currentId++;
  }

  /**
   * Delete an annotation from the map.
   *
   * @param t annotation to be deleted
   */
  @UiThread
  public void delete(T t){
    annotations.remove(t.getId());
    updateSource();
  }

  /**
   * Trigger an update to the underlying source
   */
  public void updateSource() {
    // todo move feature creation to a background thread?
    features.clear();
    T t;
    for (int i = 0; i < annotations.size(); i++) {
      t = annotations.valueAt(i);
      features.add(Feature.fromGeometry(t.getGeometry(), t.getFeature()));
    }
    //Collections.sort(features, symbolComparator);
    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));
  }

  /**
   * Add a callback to be invoked when a symbol has been clicked.
   *
   * @param u the callback to be invoked when a symbol is clicked
   */
  @UiThread
  public void addClickListener(@NonNull U u) {
    clickListeners.add(u);
  }

  /**
   * Remove a previously added callback that was to be invoked when symbol has been clicked.
   *
   * @param u the callback to be removed
   */
  @UiThread
  public void removeClickListener(@NonNull U u) {
    if (clickListeners.contains(u)) {
      clickListeners.remove(u);
    }
  }

  /**
   * Cleanup annotation manager, used to clear listeners
   */
  @UiThread
  public void onDestroy() {
    clickListeners.clear();
  }
}
