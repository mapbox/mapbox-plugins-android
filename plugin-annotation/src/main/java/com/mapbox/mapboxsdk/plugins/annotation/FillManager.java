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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The fill manager allows to add fills to a map.
 */
public class FillManager extends AnnotationManager<FillLayer, Fill, FillOptions, OnFillDragListener, OnFillClickListener, OnFillLongClickListener> {

  private static final String PROPERTY_FILL_ANTIALIAS = "fill-antialias";
  private static final String PROPERTY_FILL_TRANSLATE = "fill-translate";
  private static final String PROPERTY_FILL_TRANSLATE_ANCHOR = "fill-translate-anchor";

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null, null);
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
    this(mapView, mapboxMap, style, belowLayerId, null);
  }

  /**
   * Create a fill manager, used to manage fills.
   *
   * @param mapboxMap the map object to add fills to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   * @param geoJsonOptions options for the internal source
   */
  @UiThread
  public FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions) {
    this(mapView, mapboxMap, style, new FillElementProvider(), belowLayerId, geoJsonOptions, new DraggableAnnotationController<Fill, OnFillDragListener>(mapView, mapboxMap));
  }

  @VisibleForTesting
  FillManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<FillLayer> coreElementProvider, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions, DraggableAnnotationController<Fill, OnFillDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId, geoJsonOptions);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put(FillOptions.PROPERTY_FILL_OPACITY, false);
    dataDrivenPropertyUsageMap.put(FillOptions.PROPERTY_FILL_COLOR, false);
    dataDrivenPropertyUsageMap.put(FillOptions.PROPERTY_FILL_OUTLINE_COLOR, false);
    dataDrivenPropertyUsageMap.put(FillOptions.PROPERTY_FILL_PATTERN, false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case FillOptions.PROPERTY_FILL_OPACITY:
        layer.setProperties(fillOpacity(get(FillOptions.PROPERTY_FILL_OPACITY)));
        break;
      case FillOptions.PROPERTY_FILL_COLOR:
        layer.setProperties(fillColor(get(FillOptions.PROPERTY_FILL_COLOR)));
        break;
      case FillOptions.PROPERTY_FILL_OUTLINE_COLOR:
        layer.setProperties(fillOutlineColor(get(FillOptions.PROPERTY_FILL_OUTLINE_COLOR)));
        break;
      case FillOptions.PROPERTY_FILL_PATTERN:
        layer.setProperties(fillPattern(get(FillOptions.PROPERTY_FILL_PATTERN)));
        break;
    }
  }

  /**
   * Create a list of fills on the map.
   * <p>
   * Fills are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * FillOptions.PROPERTY_FILL_OPACITY - Float<br>
   * FillOptions.PROPERTY_FILL_COLOR - String<br>
   * FillOptions.PROPERTY_FILL_OUTLINE_COLOR - String<br>
   * FillOptions.PROPERTY_FILL_PATTERN - String<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the fill should be draggable, false otherwise
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
   * All supported properties are:<br>
   * FillOptions.PROPERTY_FILL_OPACITY - Float<br>
   * FillOptions.PROPERTY_FILL_COLOR - String<br>
   * FillOptions.PROPERTY_FILL_OUTLINE_COLOR - String<br>
   * FillOptions.PROPERTY_FILL_PATTERN - String<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the fill should be draggable, false otherwise
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
   * <p>
   * Whether or not the fill should be antialiased.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getFillAntialias() {
    return layer.getFillAntialias().value;
  }

  /**
   * Set the FillAntialias property
   * <p>
   * Whether or not the fill should be antialiased.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setFillAntialias( Boolean value) {
    PropertyValue propertyValue = fillAntialias(value);
    constantPropertyUsageMap.put(PROPERTY_FILL_ANTIALIAS, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the FillTranslate property
   * <p>
   * The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getFillTranslate() {
    return layer.getFillTranslate().value;
  }

  /**
   * Set the FillTranslate property
   * <p>
   * The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setFillTranslate( Float[] value) {
    PropertyValue propertyValue = fillTranslate(value);
    constantPropertyUsageMap.put(PROPERTY_FILL_TRANSLATE, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the FillTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#fillTranslate}.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getFillTranslateAnchor() {
    return layer.getFillTranslateAnchor().value;
  }

  /**
   * Set the FillTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#fillTranslate}.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setFillTranslateAnchor(@Property.FILL_TRANSLATE_ANCHOR String value) {
    PropertyValue propertyValue = fillTranslateAnchor(value);
    constantPropertyUsageMap.put(PROPERTY_FILL_TRANSLATE_ANCHOR, propertyValue);
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
