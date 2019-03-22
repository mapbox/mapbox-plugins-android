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
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.Symbol.Z_INDEX;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The circle manager allows to add circles to a map.
 */
public class CircleManager extends AnnotationManager<CircleLayer, Circle, CircleOptions, OnCircleDragListener, OnCircleClickListener, OnCircleLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-circle-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-circle-layer";

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap the map object to add circles to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null);
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
    this(mapView, mapboxMap, style,
      new CoreElementProvider<CircleLayer>() {
        @Override
        public CircleLayer getLayer() {
          return new CircleLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE);
        }

        @Override
        public GeoJsonSource getSource() {
          return new GeoJsonSource(ID_GEOJSON_SOURCE);
        }
      },
     belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap     the map object to add circles to
   * @param style a valid a fully loaded style object
   */
  @VisibleForTesting
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<CircleLayer> coreElementProvider, @Nullable String belowLayerId, DraggableAnnotationController<Circle, OnCircleDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put("circle-radius", false);
    dataDrivenPropertyUsageMap.put("circle-color", false);
    dataDrivenPropertyUsageMap.put("circle-blur", false);
    dataDrivenPropertyUsageMap.put("circle-opacity", false);
    dataDrivenPropertyUsageMap.put("circle-stroke-width", false);
    dataDrivenPropertyUsageMap.put("circle-stroke-color", false);
    dataDrivenPropertyUsageMap.put("circle-stroke-opacity", false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case "circle-radius":
        layer.setProperties(circleRadius(get("circle-radius")));
        break;
      case "circle-color":
        layer.setProperties(circleColor(get("circle-color")));
        break;
      case "circle-blur":
        layer.setProperties(circleBlur(get("circle-blur")));
        break;
      case "circle-opacity":
        layer.setProperties(circleOpacity(get("circle-opacity")));
        break;
      case "circle-stroke-width":
        layer.setProperties(circleStrokeWidth(get("circle-stroke-width")));
        break;
      case "circle-stroke-color":
        layer.setProperties(circleStrokeColor(get("circle-stroke-color")));
        break;
      case "circle-stroke-opacity":
        layer.setProperties(circleStrokeOpacity(get("circle-stroke-opacity")));
        break;
    }
  }

  /**
   * Create a list of circles on the map.
   * <p>
   * Circles are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * "circle-radius" - Float<br>
   * "circle-color" - String<br>
   * "circle-blur" - Float<br>
   * "circle-opacity" - Float<br>
   * "circle-stroke-width" - Float<br>
   * "circle-stroke-color" - String<br>
   * "circle-stroke-opacity" - Float<br>
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
   * "circle-radius" - Float<br>
   * "circle-color" - String<br>
   * "circle-blur" - Float<br>
   * "circle-opacity" - Float<br>
   * "circle-stroke-width" - Float<br>
   * "circle-stroke-color" - String<br>
   * "circle-stroke-opacity" - Float<br>
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
    constantPropertyUsageMap.put("circle-translate", propertyValue);
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
    constantPropertyUsageMap.put("circle-translate-anchor", propertyValue);
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
    constantPropertyUsageMap.put("circle-pitch-scale", propertyValue);
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
    constantPropertyUsageMap.put("circle-pitch-alignment", propertyValue);
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
