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
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The circle manager allows to add circles to a map.
 */
public class CircleManager extends AnnotationManager<CircleLayer, Circle, CircleOptions, OnCircleDragListener, OnCircleClickListener, OnCircleLongClickListener> {

  private static final String PROPERTY_CIRCLE_TRANSLATE = "circle-translate";
  private static final String PROPERTY_CIRCLE_TRANSLATE_ANCHOR = "circle-translate-anchor";
  private static final String PROPERTY_CIRCLE_PITCH_SCALE = "circle-pitch-scale";
  private static final String PROPERTY_CIRCLE_PITCH_ALIGNMENT = "circle-pitch-alignment";

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap the map object to add circles to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null, null);
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap the map object to add circles to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId) {
    this(mapView, mapboxMap, style, belowLayerId, null);
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap the map object to add circles to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   * @param geoJsonOptions options for the internal source
   */
  @UiThread
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions) {
    this(mapView, mapboxMap, style, new CircleElementProvider(), belowLayerId, geoJsonOptions, new DraggableAnnotationController<Circle, OnCircleDragListener>(mapView, mapboxMap));
  }

  @VisibleForTesting
  CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<CircleLayer> coreElementProvider, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions, DraggableAnnotationController<Circle, OnCircleDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId, geoJsonOptions);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_RADIUS, false);
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_COLOR, false);
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_BLUR, false);
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_OPACITY, false);
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_STROKE_WIDTH, false);
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_STROKE_COLOR, false);
    dataDrivenPropertyUsageMap.put(CircleOptions.PROPERTY_CIRCLE_STROKE_OPACITY, false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case CircleOptions.PROPERTY_CIRCLE_RADIUS:
        layer.setProperties(circleRadius(get(CircleOptions.PROPERTY_CIRCLE_RADIUS)));
        break;
      case CircleOptions.PROPERTY_CIRCLE_COLOR:
        layer.setProperties(circleColor(get(CircleOptions.PROPERTY_CIRCLE_COLOR)));
        break;
      case CircleOptions.PROPERTY_CIRCLE_BLUR:
        layer.setProperties(circleBlur(get(CircleOptions.PROPERTY_CIRCLE_BLUR)));
        break;
      case CircleOptions.PROPERTY_CIRCLE_OPACITY:
        layer.setProperties(circleOpacity(get(CircleOptions.PROPERTY_CIRCLE_OPACITY)));
        break;
      case CircleOptions.PROPERTY_CIRCLE_STROKE_WIDTH:
        layer.setProperties(circleStrokeWidth(get(CircleOptions.PROPERTY_CIRCLE_STROKE_WIDTH)));
        break;
      case CircleOptions.PROPERTY_CIRCLE_STROKE_COLOR:
        layer.setProperties(circleStrokeColor(get(CircleOptions.PROPERTY_CIRCLE_STROKE_COLOR)));
        break;
      case CircleOptions.PROPERTY_CIRCLE_STROKE_OPACITY:
        layer.setProperties(circleStrokeOpacity(get(CircleOptions.PROPERTY_CIRCLE_STROKE_OPACITY)));
        break;
    }
  }

  /**
   * Create a list of circles on the map.
   * <p>
   * Circles are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * CircleOptions.PROPERTY_CIRCLE_RADIUS - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_COLOR - String<br>
   * CircleOptions.PROPERTY_CIRCLE_BLUR - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_OPACITY - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_STROKE_WIDTH - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_STROKE_COLOR - String<br>
   * CircleOptions.PROPERTY_CIRCLE_STROKE_OPACITY - Float<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the circle should be draggable, false otherwise
   *
   * @param json the GeoJSON defining the list of circles to build
   * @return the list of built circles
   */
  @UiThread
  public List<Circle> create(@NonNull String json) {
    return create(FeatureCollection.fromJson(json));
  }

  /**
   * Create a list of circles on the map.
   * <p>
   * Circles are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * CircleOptions.PROPERTY_CIRCLE_RADIUS - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_COLOR - String<br>
   * CircleOptions.PROPERTY_CIRCLE_BLUR - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_OPACITY - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_STROKE_WIDTH - Float<br>
   * CircleOptions.PROPERTY_CIRCLE_STROKE_COLOR - String<br>
   * CircleOptions.PROPERTY_CIRCLE_STROKE_OPACITY - Float<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the circle should be draggable, false otherwise
   *
   * @param featureCollection the featureCollection defining the list of circles to build
   * @return the list of built circles
   */
  @UiThread
  public List<Circle> create(@NonNull FeatureCollection featureCollection) {
    List<Feature> features = featureCollection.features();
    List<CircleOptions> options = new ArrayList<>();
    if (features != null) {
      for (Feature feature : features) {
        CircleOptions option = CircleOptions.fromFeature(feature);
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
    return Circle.ID_KEY;
  }

  // Property accessors
  /**
   * Get the CircleTranslate property
   * <p>
   * The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getCircleTranslate() {
    return layer.getCircleTranslate().value;
  }

  /**
   * Set the CircleTranslate property
   * <p>
   * The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setCircleTranslate( Float[] value) {
    PropertyValue propertyValue = circleTranslate(value);
    constantPropertyUsageMap.put(PROPERTY_CIRCLE_TRANSLATE, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the CircleTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#circleTranslate}.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getCircleTranslateAnchor() {
    return layer.getCircleTranslateAnchor().value;
  }

  /**
   * Set the CircleTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#circleTranslate}.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setCircleTranslateAnchor(@Property.CIRCLE_TRANSLATE_ANCHOR String value) {
    PropertyValue propertyValue = circleTranslateAnchor(value);
    constantPropertyUsageMap.put(PROPERTY_CIRCLE_TRANSLATE_ANCHOR, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the CirclePitchScale property
   * <p>
   * Controls the scaling behavior of the circle when the map is pitched.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getCirclePitchScale() {
    return layer.getCirclePitchScale().value;
  }

  /**
   * Set the CirclePitchScale property
   * <p>
   * Controls the scaling behavior of the circle when the map is pitched.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setCirclePitchScale(@Property.CIRCLE_PITCH_SCALE String value) {
    PropertyValue propertyValue = circlePitchScale(value);
    constantPropertyUsageMap.put(PROPERTY_CIRCLE_PITCH_SCALE, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the CirclePitchAlignment property
   * <p>
   * Orientation of circle when map is pitched.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getCirclePitchAlignment() {
    return layer.getCirclePitchAlignment().value;
  }

  /**
   * Set the CirclePitchAlignment property
   * <p>
   * Orientation of circle when map is pitched.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setCirclePitchAlignment(@Property.CIRCLE_PITCH_ALIGNMENT String value) {
    PropertyValue propertyValue = circlePitchAlignment(value);
    constantPropertyUsageMap.put(PROPERTY_CIRCLE_PITCH_ALIGNMENT, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Set filter on the managed circles.
   *
   * @param expression expression
   */
   @Override
  public void setFilter(@NonNull Expression expression) {
    layerFilter = expression;
    layer.setFilter(layerFilter);
  }

  /**
   * Get filter of the managed circles.
   *
   * @return expression
   */
  @Nullable
  public Expression getFilter() {
    return layer.getFilter();
  }
}
