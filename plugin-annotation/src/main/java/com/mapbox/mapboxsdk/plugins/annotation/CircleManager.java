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
    this(mapboxMap, style, new GeoJsonSource(ID_GEOJSON_SOURCE), new CircleLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE),
    belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap     the map object to add circles to
   * @param style a valid a fully loaded style object
   * @param geoJsonSource the geojson source to add circles to
   * @param layer         the circle layer to visualise Circles with
   */
  @VisibleForTesting
  public CircleManager(@NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull GeoJsonSource geoJsonSource, @NonNull CircleLayer layer, @Nullable String belowLayerId, DraggableAnnotationController<Circle, OnCircleDragListener> draggableAnnotationController) {
    super(mapboxMap, style, layer, geoJsonSource, null, draggableAnnotationController, belowLayerId);
    initializeDataDrivenPropertyMap();
  }

  /**
   * Create a list of circles on the map.
   * <p>
   * Circles are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link CircleOptions#fromFeature(Feature)}.
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
   * You can inspect a full list of supported feature properties in {@link CircleOptions#fromFeature(Feature)}.
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

  private void initializeDataDrivenPropertyMap() {
    propertyUsageMap.put("circle-radius", false);
    propertyUsageMap.put("circle-color", false);
    propertyUsageMap.put("circle-blur", false);
    propertyUsageMap.put("circle-opacity", false);
    propertyUsageMap.put("circle-stroke-width", false);
    propertyUsageMap.put("circle-stroke-color", false);
    propertyUsageMap.put("circle-stroke-opacity", false);
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
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getCircleTranslate() {
    return layer.getCircleTranslate().value;
  }

  /**
   * Set the CircleTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setCircleTranslate( Float[] value) {
    layer.setProperties(circleTranslate(value));
  }

  /**
   * Get the CircleTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getCircleTranslateAnchor() {
    return layer.getCircleTranslateAnchor().value;
  }

  /**
   * Set the CircleTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setCircleTranslateAnchor(@Property.CIRCLE_TRANSLATE_ANCHOR String value) {
    layer.setProperties(circleTranslateAnchor(value));
  }

  /**
   * Get the CirclePitchScale property
   *
   * @return property wrapper value around String
   */
  public String getCirclePitchScale() {
    return layer.getCirclePitchScale().value;
  }

  /**
   * Set the CirclePitchScale property
   *
   * @param value property wrapper value around String
   */
  public void setCirclePitchScale(@Property.CIRCLE_PITCH_SCALE String value) {
    layer.setProperties(circlePitchScale(value));
  }

  /**
   * Get the CirclePitchAlignment property
   *
   * @return property wrapper value around String
   */
  public String getCirclePitchAlignment() {
    return layer.getCirclePitchAlignment().value;
  }

  /**
   * Set the CirclePitchAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setCirclePitchAlignment(@Property.CIRCLE_PITCH_ALIGNMENT String value) {
    layer.setProperties(circlePitchAlignment(value));
  }

  /**
   * Set filter on the managed circles.
   *
   * @param expression expression
   */
  public void setFilter(@NonNull Expression expression) {
    layer.setFilter(expression);
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
