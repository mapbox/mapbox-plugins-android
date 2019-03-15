// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.ColorInt;
import android.graphics.PointF;
import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.mapboxsdk.maps.Projection;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.constants.GeometryConstants.MAX_MERCATOR_LATITUDE;
import static com.mapbox.mapboxsdk.constants.GeometryConstants.MIN_MERCATOR_LATITUDE;

@UiThread
public class Line extends Annotation<LineString> {

  private final AnnotationManager<?, Line, ?, ?, ?, ?> annotationManager;

  /**
   * Create a line.
   *
   * @param id            the id of the line
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Line(long id, AnnotationManager<?, Line, ?, ?, ?, ?> annotationManager, JsonObject jsonObject, LineString geometry) {
    super(id, jsonObject, geometry);
    this.annotationManager = annotationManager;
  }

  @Override
  void setUsedDataDrivenProperties() {
    if (!(jsonObject.get("line-join") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-join");
    }
    if (!(jsonObject.get("line-opacity") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-opacity");
    }
    if (!(jsonObject.get("line-color") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-color");
    }
    if (!(jsonObject.get("line-width") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-width");
    }
    if (!(jsonObject.get("line-gap-width") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-gap-width");
    }
    if (!(jsonObject.get("line-offset") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-offset");
    }
    if (!(jsonObject.get("line-blur") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-blur");
    }
    if (!(jsonObject.get("line-pattern") instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty("line-pattern");
    }
  }

  /**
   * Set a list of LatLng for the line, which represents the locations of the line on the map
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param latLngs a list of the locations of the line in a longitude and latitude pairs
   */
  public void setLatLngs(List<LatLng> latLngs) {
    List<Point>points = new ArrayList<>();
    for (LatLng latLng : latLngs) {
      points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
    }
    geometry = LineString.fromLngLats(points);
  }

  /**
   * Get a list of LatLng for the line, which represents the locations of the line on the map
   *
   * @return a list of the locations of the line in a latitude and longitude pairs
   */
  @NonNull
  public List<LatLng> getLatLngs() {
    LineString lineString = (LineString) geometry;
    List<LatLng> latLngs = new ArrayList<>();
    for (Point point: lineString.coordinates()) {
      latLngs.add(new LatLng(point.latitude(), point.longitude()));
    }
    return latLngs;
  }

  // Property accessors

  /**
   * Get the LineJoin property
   * <p>
   * The display of lines when joining.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getLineJoin() {
    return jsonObject.get("line-join").getAsString();
  }

  /**
   * Set the LineJoin property
   * <p>
   * The display of lines when joining.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setLineJoin(@Property.LINE_JOIN String value) {
    jsonObject.addProperty("line-join", value);
  }

  /**
   * Get the LineOpacity property
   * <p>
   * The opacity at which the line will be drawn.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineOpacity() {
    return jsonObject.get("line-opacity").getAsFloat();
  }

  /**
   * Set the LineOpacity property
   * <p>
   * The opacity at which the line will be drawn.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineOpacity(Float value) {
    jsonObject.addProperty("line-opacity", value);
  }

  /**
   * Get the LineColor property
   * <p>
   * The color with which the line will be drawn.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getLineColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("line-color").getAsString());
  }

  /**
   * Set the LineColor property
   * <p>
   * The color with which the line will be drawn.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setLineColor(@ColorInt int color) {
    jsonObject.addProperty("line-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the LineWidth property
   * <p>
   * Stroke thickness.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineWidth() {
    return jsonObject.get("line-width").getAsFloat();
  }

  /**
   * Set the LineWidth property
   * <p>
   * Stroke thickness.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineWidth(Float value) {
    jsonObject.addProperty("line-width", value);
  }

  /**
   * Get the LineGapWidth property
   * <p>
   * Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineGapWidth() {
    return jsonObject.get("line-gap-width").getAsFloat();
  }

  /**
   * Set the LineGapWidth property
   * <p>
   * Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineGapWidth(Float value) {
    jsonObject.addProperty("line-gap-width", value);
  }

  /**
   * Get the LineOffset property
   * <p>
   * The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineOffset() {
    return jsonObject.get("line-offset").getAsFloat();
  }

  /**
   * Set the LineOffset property
   * <p>
   * The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineOffset(Float value) {
    jsonObject.addProperty("line-offset", value);
  }

  /**
   * Get the LineBlur property
   * <p>
   * Blur applied to the line, in density-independent pixels.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineBlur() {
    return jsonObject.get("line-blur").getAsFloat();
  }

  /**
   * Set the LineBlur property
   * <p>
   * Blur applied to the line, in density-independent pixels.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setLineBlur(Float value) {
    jsonObject.addProperty("line-blur", value);
  }

  /**
   * Get the LinePattern property
   * <p>
   * Name of image in sprite to use for drawing image lines. For seamless patterns, image width must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getLinePattern() {
    return jsonObject.get("line-pattern").getAsString();
  }

  /**
   * Set the LinePattern property
   * <p>
   * Name of image in sprite to use for drawing image lines. For seamless patterns, image width must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
   * </p>
   * <p>
   * To update the line on the map use {@link LineManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setLinePattern(String value) {
    jsonObject.addProperty("line-pattern", value);
  }

  @Override
  @Nullable
  Geometry getOffsetGeometry(@NonNull Projection projection, @NonNull MoveDistancesObject moveDistancesObject,
                             float touchAreaShiftX, float touchAreaShiftY) {
    List<Point> originalPoints = geometry.coordinates();
    List<Point> resultingPoints = new ArrayList<>(originalPoints.size());
    for (Point jsonPoint : originalPoints) {
      PointF pointF = projection.toScreenLocation(new LatLng(jsonPoint.latitude(), jsonPoint.longitude()));
      pointF.x -= moveDistancesObject.getDistanceXSinceLast();
      pointF.y -= moveDistancesObject.getDistanceYSinceLast();

      LatLng latLng = projection.fromScreenLocation(pointF);
      if (latLng.getLatitude() > MAX_MERCATOR_LATITUDE || latLng.getLatitude() < MIN_MERCATOR_LATITUDE) {
        return null;
      }
      resultingPoints.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
    }

    return LineString.fromLngLats(resultingPoints);
  }

  @Override
  String getName() {
     return "Line";
  }
}
