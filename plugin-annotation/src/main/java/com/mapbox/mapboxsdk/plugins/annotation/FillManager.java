// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.Symbol.Z_INDEX;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The fill manager allows to add fills to a map.
 */
public class FillManager extends AnnotationManager<FillLayer, Fill, FillOptions, OnFillDragListener, OnFillClickListener, OnFillLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-fill-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-fill-layer";

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null);
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId) {
    this(mapView, mapboxMap, style,
      new CoreElementProvider<FillLayer>() {
        @Override
        public FillLayer getLayer() {
          return new FillLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE);
        }

        @Override
        public GeoJsonSource getSource() {
          return new GeoJsonSource(ID_GEOJSON_SOURCE);
        }
      },
     belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap     the map object to add fills to
   * @param style a valid a fully loaded style object
   */
  @VisibleForTesting
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<FillLayer> coreElementProvider, @Nullable String belowLayerId, DraggableAnnotationController<Fill, OnFillDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put("fill-opacity", false);
    dataDrivenPropertyUsageMap.put("fill-color", false);
    dataDrivenPropertyUsageMap.put("fill-outline-color", false);
    dataDrivenPropertyUsageMap.put("fill-pattern", false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case "fill-opacity":
        layer.setProperties(fillOpacity(get("fill-opacity")));
        break;
      case "fill-color":
        layer.setProperties(fillColor(get("fill-color")));
        break;
      case "fill-outline-color":
        layer.setProperties(fillOutlineColor(get("fill-outline-color")));
        break;
      case "fill-pattern":
        layer.setProperties(fillPattern(get("fill-pattern")));
        break;
    }
  }

  /**
   * Create a list of fills on the map.
   * <p>
   * Fills are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link FillOptions#fromFeature(Feature)}.
   *
   * @param json the GeoJSON defining the list of fills to build
   * @return the list of built fills
   */
  @UiThread
  public List<Fill> create(@NonNull String json) {
    return create(FeatureCollection.fromJson(json));
  }

  /**
   * Create a list of fills on the map.
   * <p>
   * Fills are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link FillOptions#fromFeature(Feature)}.
   *
   * @param featureCollection the featureCollection defining the list of fills to build
   * @return the list of built fills
   */
  @UiThread
  public List<Fill> create(@NonNull FeatureCollection featureCollection) {
    List<Feature> features = featureCollection.features();
    List<FillOptions> options = new ArrayList<>();
    if (features != null) {
      for (Feature feature : features) {
        FillOptions option = FillOptions.fromFeature(feature);
        if (option != null) {
          options.add(option);
        }
      }
    }
    return create(options);
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
    PropertyValue propertyValue = fillAntialias(value);
    constantPropertyUsageMap.put("fill-antialias", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = fillTranslate(value);
    constantPropertyUsageMap.put("fill-translate", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = fillTranslateAnchor(value);
    constantPropertyUsageMap.put("fill-translate-anchor", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Set filter on the managed fills.
   *
   * @param expression expression
   */
   @Override
  public void setFilter(@NonNull Expression expression) {
    layerFilter = expression;
    layer.setFilter(layerFilter);
  }

  /**
   * Get filter of the managed fills.
   *
   * @return expression
   */
  @Nullable
  public Expression getFilter() {
    return layer.getFilter();
  }
}
