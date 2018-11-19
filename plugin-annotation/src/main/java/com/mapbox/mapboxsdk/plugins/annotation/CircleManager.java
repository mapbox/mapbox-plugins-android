// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

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
   */
  @UiThread
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this(mapView, mapboxMap, null);
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap the map object to add circles to
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public CircleManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @Nullable String belowLayerId) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new CircleLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE),
    belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap     the map object to add circles to
   * @param geoJsonSource the geojson source to add circles to
   * @param layer         the circle layer to visualise Circles with
   */
  @VisibleForTesting
  public CircleManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, @NonNull CircleLayer layer, @Nullable String belowLayerId, DraggableAnnotationController<Circle, OnCircleDragListener> draggableAnnotationController) {
    super(mapboxMap, layer, geoJsonSource, null, draggableAnnotationController, belowLayerId);
    initializeDataDrivenPropertyMap();
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
   * Get the CircleRadius expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getCircleRadiusExpression() {
    return layer.getCircleRadius().getExpression();
  }

  /**
   * Set the CircleRadius expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setCircleRadiusExpression(@NonNull Expression expression) {
    layer.setProperties(circleRadius(expression));
  }

  /**
   * Get the CircleColor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getCircleColorExpression() {
    return layer.getCircleColor().getExpression();
  }

  /**
   * Set the CircleColor expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setCircleColorExpression(@NonNull Expression expression) {
    layer.setProperties(circleColor(expression));
  }

  /**
   * Get the CircleBlur expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getCircleBlurExpression() {
    return layer.getCircleBlur().getExpression();
  }

  /**
   * Set the CircleBlur expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setCircleBlurExpression(@NonNull Expression expression) {
    layer.setProperties(circleBlur(expression));
  }

  /**
   * Get the CircleOpacity expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getCircleOpacityExpression() {
    return layer.getCircleOpacity().getExpression();
  }

  /**
   * Set the CircleOpacity expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setCircleOpacityExpression(@NonNull Expression expression) {
    layer.setProperties(circleOpacity(expression));
  }

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
   * Get the CircleStrokeWidth expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getCircleStrokeWidthExpression() {
    return layer.getCircleStrokeWidth().getExpression();
  }

  /**
   * Set the CircleStrokeWidth expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setCircleStrokeWidthExpression(@NonNull Expression expression) {
    layer.setProperties(circleStrokeWidth(expression));
  }

  /**
   * Get the CircleStrokeColor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getCircleStrokeColorExpression() {
    return layer.getCircleStrokeColor().getExpression();
  }

  /**
   * Set the CircleStrokeColor expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setCircleStrokeColorExpression(@NonNull Expression expression) {
    layer.setProperties(circleStrokeColor(expression));
  }

  /**
   * Get the CircleStrokeOpacity expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getCircleStrokeOpacityExpression() {
    return layer.getCircleStrokeOpacity().getExpression();
  }

  /**
   * Set the CircleStrokeOpacity expression.
   * <p>
   * this expression is applied to all circles used by this manager and overrides
   * behaviour defined on a circle itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setCircleStrokeOpacityExpression(@NonNull Expression expression) {
    layer.setProperties(circleStrokeOpacity(expression));
  }

}
