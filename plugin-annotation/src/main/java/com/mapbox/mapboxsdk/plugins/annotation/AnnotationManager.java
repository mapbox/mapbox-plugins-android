package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;
import androidx.collection.LongSparseArray;

/**
 * Generic AnnotationManager, can be used to create annotation specific managers.
 *
 * @param <T> type of annotation
 * @param <S> type of options for building the annotation, depends on generic T
 * @param <D> type of annotation drag listener, depends on generic T
 * @param <U> type of annotation click listener, depends on generic T
 * @param <V> type of annotation long click listener, depends on generic T
 */
public abstract class AnnotationManager<
  L extends Layer,
  T extends Annotation,
  S extends Options<T>,
  D extends OnAnnotationDragListener<T>,
  U extends OnAnnotationClickListener<T>,
  V extends OnAnnotationLongClickListener<T>> {

  private static final String TAG = "AnnotationManager";

  protected final MapboxMap mapboxMap;
  protected final LongSparseArray<T> annotations = new LongSparseArray<>();
  final Map<String, Boolean> dataDrivenPropertyUsageMap = new HashMap<>();
  final Map<String, PropertyValue> constantPropertyUsageMap = new HashMap<>();
  Expression layerFilter;

  private final List<D> dragListeners = new ArrayList<>();
  private final List<U> clickListeners = new ArrayList<>();
  private final List<V> longClickListeners = new ArrayList<>();
  private long currentId;

  protected L layer;
  private GeoJsonSource geoJsonSource;
  private final MapClickResolver mapClickResolver;
  private Style style;
  private String belowLayerId;
  private CoreElementProvider<L> coreElementProvider;
  private DraggableAnnotationController draggableAnnotationController;

  @UiThread
  protected AnnotationManager(MapView mapView, final MapboxMap mapboxMap, Style style,
                              CoreElementProvider<L> coreElementProvider,
                              DraggableAnnotationController draggableAnnotationController,
                              String belowLayerId, final GeoJsonOptions geoJsonOptions) {
    this.mapboxMap = mapboxMap;
    this.style = style;
    this.belowLayerId = belowLayerId;
    this.coreElementProvider = coreElementProvider;
    this.draggableAnnotationController = draggableAnnotationController;

    if (!style.isFullyLoaded()) {
      throw new RuntimeException("The style has to be non-null and fully loaded.");
    }

    mapboxMap.addOnMapClickListener(mapClickResolver = new MapClickResolver());
    mapboxMap.addOnMapLongClickListener(mapClickResolver);
    draggableAnnotationController.addAnnotationManager(this);

    initializeSourcesAndLayers(geoJsonOptions);

    mapView.addOnDidFinishLoadingStyleListener(new MapView.OnDidFinishLoadingStyleListener() {
      @Override
      public void onDidFinishLoadingStyle() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style loadedStyle) {
            AnnotationManager.this.style = loadedStyle;
            initializeSourcesAndLayers(geoJsonOptions);
          }
        });
      }
    });
  }

  /**
   * Returns a layer ID that annotations created by this manager are laid out on.
   *
   * This reference can be used together with {@link Style#addLayerAbove(Layer, String)}
   * or {@link Style#addLayerBelow(Layer, String)} to improve other layers positioning in relation to this manager.
   * @return underlying layer's ID
   */
  public String getLayerId() {
    return layer.getId();
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

  /**
   * Create an annotation on the map
   *
   * @param options the annotation options defining the annotation to build
   * @return the build annotation
   */
  @UiThread
  public T create(S options) {
    T t = options.build(currentId, this);
    annotations.put(t.getId(), t);
    currentId++;
    internalUpdateSource();
    return t;
  }

  /**
   * Create a list of annotations on the map.
   *
   * @param optionsList the list of annotation options defining the list of annotations to build
   * @return the list of build annotations
   */
  @UiThread
  public List<T> create(List<S> optionsList) {
    List<T> annotationList = new ArrayList<>();
    for (S options : optionsList) {
      T annotation = options.build(currentId, this);
      annotationList.add(annotation);
      annotations.put(annotation.getId(), annotation);
      currentId++;
    }
    internalUpdateSource();
    return annotationList;
  }

  /**
   * Delete an annotation from the map.
   *
   * @param annotation annotation to be deleted
   */
  @UiThread
  public void delete(T annotation) {
    annotations.remove(annotation.getId());
    draggableAnnotationController.onAnnotationUpdated(annotation);
    internalUpdateSource();
  }

  /**
   * Deletes annotations from the map.
   *
   * @param annotationList the list of annotations to be deleted
   */
  @UiThread
  public void delete(List<T> annotationList) {
    for (T annotation : annotationList) {
      annotations.remove(annotation.getId());
      draggableAnnotationController.onAnnotationUpdated(annotation);
    }
    internalUpdateSource();
  }

  /**
   * Deletes all annotations from the map.
   */
  @UiThread
  public void deleteAll() {
    annotations.clear();
    updateSource();
  }

  /**
   * Update an annotation on the map.
   *
   * @param annotation annotation to be updated
   */
  @UiThread
  public void update(T annotation) {
    if (annotations.containsValue(annotation)) {
      annotations.put(annotation.getId(), annotation);
      draggableAnnotationController.onAnnotationUpdated(annotation);
      internalUpdateSource();
    } else {
      Logger.e(TAG, "Can't update annotation: "
        + annotation.toString()
        + ", the annotation isn't active annotation.");
    }
  }

  /**
   * Update annotations on the map.
   *
   * @param annotationList list of annotation to be updated
   */
  @UiThread
  public void update(List<T> annotationList) {
    for (T annotation : annotationList) {
      annotations.put(annotation.getId(), annotation);
      draggableAnnotationController.onAnnotationUpdated(annotation);
    }
    internalUpdateSource();
  }

  /**
   * Trigger an update to the underlying source. Invalidates active drags.
   */
  public void updateSource() {
    draggableAnnotationController.onSourceUpdated();
    internalUpdateSource();
  }

  void internalUpdateSource() {
    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

    List<Feature> features = new ArrayList<>();
    T t;
    for (int i = 0; i < annotations.size(); i++) {
      t = annotations.valueAt(i);
      features.add(Feature.fromGeometry(t.getGeometry(), t.getFeature()));
      t.setUsedDataDrivenProperties();
    }

    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));
  }

  void enableDataDrivenProperty(@NonNull String property) {
    if (dataDrivenPropertyUsageMap.get(property).equals(false)) {
      dataDrivenPropertyUsageMap.put(property, true);
      setDataDrivenPropertyIsUsed(property);
    }
  }

  protected abstract void setDataDrivenPropertyIsUsed(@NonNull String property);

  /**
   * Add a callback to be invoked when an annotation is dragged.
   *
   * @param d the callback to be invoked when an annotation is dragged
   */
  @UiThread
  public void addDragListener(@NonNull D d) {
    dragListeners.add(d);
  }

  /**
   * Remove a previously added callback that was to be invoked when an annotation has been dragged.
   *
   * @param d the callback to be removed
   */
  @UiThread
  public void removeDragListener(@NonNull D d) {
    dragListeners.remove(d);
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
    clickListeners.remove(u);
  }

  /**
   * Add a callback to be invoked when a symbol has been long clicked.
   *
   * @param v the callback to be invoked when a symbol is clicked
   */
  @UiThread
  public void addLongClickListener(@NonNull V v) {
    longClickListeners.add(v);
  }

  /**
   * Remove a previously added callback that was to be invoked when symbol has been long clicked.
   *
   * @param v the callback to be removed
   */
  @UiThread
  public void removeLongClickListener(@NonNull V v) {
    longClickListeners.remove(v);
  }

  @VisibleForTesting
  List<U> getClickListeners() {
    return clickListeners;
  }

  @VisibleForTesting
  List<V> getLongClickListeners() {
    return longClickListeners;
  }

  List<D> getDragListeners() {
    return dragListeners;
  }

  /**
   * Cleanup annotation manager, used to clear listeners
   */
  @UiThread
  public void onDestroy() {
    mapboxMap.removeOnMapClickListener(mapClickResolver);
    mapboxMap.removeOnMapLongClickListener(mapClickResolver);
    draggableAnnotationController.removeAnnotationManager(this);
    dragListeners.clear();
    clickListeners.clear();
    longClickListeners.clear();
  }

  abstract String getAnnotationIdKey();

  abstract void initializeDataDrivenPropertyMap();

  abstract void setFilter(@NonNull Expression expression);

  private void initializeSourcesAndLayers(GeoJsonOptions geoJsonOptions) {
    geoJsonSource = coreElementProvider.getSource(geoJsonOptions);
    layer = coreElementProvider.getLayer();

    style.addSource(geoJsonSource);
    if (belowLayerId == null) {
      style.addLayer(layer);
    } else {
      style.addLayerBelow(layer, belowLayerId);
    }

    initializeDataDrivenPropertyMap();
    layer.setProperties(constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    if (layerFilter != null) {
      setFilter(layerFilter);
    }

    updateSource();
  }

  /**
   * Inner class for transforming map click events into annotation clicks
   */
  private class MapClickResolver implements MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener {

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
      if (clickListeners.isEmpty()) {
        return false;
      }

      T annotation = queryMapForFeatures(point);
      if (annotation != null) {
        for (U clickListener : clickListeners) {
          if (clickListener.onAnnotationClick(annotation)) {
            return true;
          }
        }
      }
      return false;
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {
      if (longClickListeners.isEmpty()) {
        return false;
      }

      T annotation = queryMapForFeatures(point);
      if (annotation != null) {
        for (V clickListener : longClickListeners) {
          if (clickListener.onAnnotationLongClick(annotation)) {
            return true;
          }
        }
      }
      return false;
    }
  }

  @Nullable
  private T queryMapForFeatures(@NonNull LatLng point) {
    return queryMapForFeatures(mapboxMap.getProjection().toScreenLocation(point));
  }

  @Nullable
  T queryMapForFeatures(@NonNull PointF point) {
    List<Feature> features = mapboxMap.queryRenderedFeatures(point, coreElementProvider.getLayerId());
    if (!features.isEmpty()) {
      long id = features.get(0).getProperty(getAnnotationIdKey()).getAsLong();
      return annotations.get(id);
    }
    return null;
  }
}
