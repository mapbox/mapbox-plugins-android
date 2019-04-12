// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.ColorInt;
import android.graphics.PointF;
import android.support.annotation.UiThread;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.mapboxsdk.maps.Projection;

import static com.mapbox.mapboxsdk.constants.GeometryConstants.MAX_MERCATOR_LATITUDE;
import static com.mapbox.mapboxsdk.constants.GeometryConstants.MIN_MERCATOR_LATITUDE;

@UiThread
public class Circle extends Annotation<Point> {

  private final AnnotationManager<?, Circle, ?, ?, ?, ?> annotationManager;

  /**
   * Create a circle.
   *
   * @param id            the id of the circle
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Circle(long id, AnnotationManager<?, Circle, ?, ?, ?, ?> annotationManager, JsonObject jsonObject, Point geometry) {
    super(id, jsonObject, geometry);
    this.annotationManager = annotationManager;
  }

  @Override
  void setUsedDataDrivenProperties() {
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleRadius) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleRadius);
    }
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleColor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleColor);
    }
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleBlur) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleBlur);
    }
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleOpacity) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleOpacity);
    }
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleStrokeWidth) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleStrokeWidth);
    }
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleStrokeColor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleStrokeColor);
    }
    if (!(jsonObject.get(CircleOptions.PROPERTY_circleStrokeOpacity) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(CircleOptions.PROPERTY_circleStrokeOpacity);
    }
  }

  /**
   * Set the LatLng of the circle, which represents the location of the circle on the map
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param latLng the location of the circle in a latitude and longitude pair
   */
  public void setLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
  }

  /**
   * Get the LatLng of the circle, which represents the location of the circle on the map
   *
   * @return the location of the circle
   */
  @NonNull
  public LatLng getLatLng() {
    return new LatLng(geometry.latitude(), geometry.longitude());
  }

  // Property accessors

  /**
   * Get the CircleRadius property
   * <p>
   * Circle radius.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getCircleRadius() {
    return jsonObject.get(CircleOptions.PROPERTY_circleRadius).getAsFloat();
  }

  /**
   * Set the CircleRadius property
   * <p>
   * Circle radius.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleRadius(Float value) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleRadius, value);
  }

  /**
   * Get the CircleColor property
   * <p>
   * The fill color of the circle.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getCircleColorAsInt() {
    return ColorUtils.rgbaToColor(jsonObject.get(CircleOptions.PROPERTY_circleColor).getAsString());
  }

  /**
   * Get the CircleColor property
   * <p>
   * The fill color of the circle.
   * </p>
   *
   * @return color value for String
   */
  public String getCircleColor() {
    return jsonObject.get(CircleOptions.PROPERTY_circleColor).getAsString();
  }

  /**
   * Set the CircleColor property
   * <p>
   * The fill color of the circle.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setCircleColor(@ColorInt int color) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleColor, ColorUtils.colorToRgbaString(color));
  }

  /**
   * Set the CircleColor property
   * <p>
   * The fill color of the circle.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setCircleColor(@NonNull String color) {
    jsonObject.addProperty("circle-color", color);
  }

  /**
   * Get the CircleBlur property
   * <p>
   * Amount to blur the circle. 1 blurs the circle such that only the centerpoint is full opacity.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getCircleBlur() {
    return jsonObject.get(CircleOptions.PROPERTY_circleBlur).getAsFloat();
  }

  /**
   * Set the CircleBlur property
   * <p>
   * Amount to blur the circle. 1 blurs the circle such that only the centerpoint is full opacity.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleBlur(Float value) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleBlur, value);
  }

  /**
   * Get the CircleOpacity property
   * <p>
   * The opacity at which the circle will be drawn.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getCircleOpacity() {
    return jsonObject.get(CircleOptions.PROPERTY_circleOpacity).getAsFloat();
  }

  /**
   * Set the CircleOpacity property
   * <p>
   * The opacity at which the circle will be drawn.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleOpacity(Float value) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleOpacity, value);
  }

  /**
   * Get the CircleStrokeWidth property
   * <p>
   * The width of the circle's stroke. Strokes are placed outside of the {@link PropertyFactory#circleRadius}.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getCircleStrokeWidth() {
    return jsonObject.get(CircleOptions.PROPERTY_circleStrokeWidth).getAsFloat();
  }

  /**
   * Set the CircleStrokeWidth property
   * <p>
   * The width of the circle's stroke. Strokes are placed outside of the {@link PropertyFactory#circleRadius}.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleStrokeWidth(Float value) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleStrokeWidth, value);
  }

  /**
   * Get the CircleStrokeColor property
   * <p>
   * The stroke color of the circle.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getCircleStrokeColorAsInt() {
    return ColorUtils.rgbaToColor(jsonObject.get(CircleOptions.PROPERTY_circleStrokeColor).getAsString());
  }

  /**
   * Get the CircleStrokeColor property
   * <p>
   * The stroke color of the circle.
   * </p>
   *
   * @return color value for String
   */
  public String getCircleStrokeColor() {
    return jsonObject.get(CircleOptions.PROPERTY_circleStrokeColor).getAsString();
  }

  /**
   * Set the CircleStrokeColor property
   * <p>
   * The stroke color of the circle.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setCircleStrokeColor(@ColorInt int color) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleStrokeColor, ColorUtils.colorToRgbaString(color));
  }

  /**
   * Set the CircleStrokeColor property
   * <p>
   * The stroke color of the circle.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setCircleStrokeColor(@NonNull String color) {
    jsonObject.addProperty("circle-stroke-color", color);
  }

  /**
   * Get the CircleStrokeOpacity property
   * <p>
   * The opacity of the circle's stroke.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getCircleStrokeOpacity() {
    return jsonObject.get(CircleOptions.PROPERTY_circleStrokeOpacity).getAsFloat();
  }

  /**
   * Set the CircleStrokeOpacity property
   * <p>
   * The opacity of the circle's stroke.
   * </p>
   * <p>
   * To update the circle on the map use {@link CircleManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setCircleStrokeOpacity(Float value) {
    jsonObject.addProperty(CircleOptions.PROPERTY_circleStrokeOpacity, value);
  }

  @Override
  @Nullable
  Geometry getOffsetGeometry(@NonNull Projection projection, @NonNull MoveDistancesObject moveDistancesObject,
                             float touchAreaShiftX, float touchAreaShiftY) {
    PointF pointF = new PointF(
      moveDistancesObject.getCurrentX() - touchAreaShiftX,
      moveDistancesObject.getCurrentY() - touchAreaShiftY
    );

    LatLng latLng = projection.fromScreenLocation(pointF);
    if (latLng.getLatitude() > MAX_MERCATOR_LATITUDE || latLng.getLatitude() < MIN_MERCATOR_LATITUDE) {
      return null;
    }

    return Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
  }

  @Override
  String getName() {
    return "Circle";
  }
}
