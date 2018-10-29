// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.ColorInt;
import android.graphics.PointF;
import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

@UiThread
public class Fill extends Annotation {

  /**
   * Create a fill.
   *
   * @param id            the id of the fill
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Fill(long id, JsonObject jsonObject, Geometry geometry) {
    super(id, jsonObject, geometry);
  }

  /**
   * Set a list of lists of LatLng for the fill, which represents the locations of the fill on the map
   * <p>
   * To update the fill on the map use {@link FillManager#update(Annotation)}.
   * <p>
   *
   * @param latLngs a list of a lists of the locations of the line in a longitude and latitude pairs
   */
  public void setLatLngs(List<List<LatLng>> latLngs) {
    List<List<Point>> points = new ArrayList<>();
    for (List<LatLng> innerLatLngs : latLngs) {
      List<Point>innerList = new ArrayList<>();
      for (LatLng latLng : innerLatLngs) {
        innerList.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
      }
      points.add(innerList);
    }
    geometry = Polygon.fromLngLats(points);
  }

  /**
   * Set the Geometry of the fill, which represents the location of the fill on the map
   * <p>
   * To update the fill on the map use {@link FillManager#update(Annotation)}.
   * <p>
   *
   * @param geometry the geometry of the fill
   */
  public void setGeometry(Polygon geometry) {
    this.geometry = geometry;
  }

  // Property accessors

  /**
   * Get the FillOpacity property
   *
   * @return property wrapper value around Float
   */
  public Float getFillOpacity() {
    return jsonObject.get("fill-opacity").getAsFloat();
  }

  /**
   * Set the FillOpacity property
   * <p>
   * To update the fill on the map use {@link FillManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setFillOpacity(Float value) {
    jsonObject.addProperty("fill-opacity", value);
  }

  /**
   * Get the FillColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getFillColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("fill-color").getAsString());
  }

  /**
   * Set the FillColor property
   * <p>
   * To update the fill on the map use {@link FillManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setFillColor(@ColorInt int color) {
    jsonObject.addProperty("fill-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the FillOutlineColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getFillOutlineColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("fill-outline-color").getAsString());
  }

  /**
   * Set the FillOutlineColor property
   * <p>
   * To update the fill on the map use {@link FillManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setFillOutlineColor(@ColorInt int color) {
    jsonObject.addProperty("fill-outline-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the FillPattern property
   *
   * @return property wrapper value around String
   */
  public String getFillPattern() {
    return jsonObject.get("fill-pattern").getAsString();
  }

  /**
   * Set the FillPattern property
   * <p>
   * To update the fill on the map use {@link FillManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setFillPattern(String value) {
    jsonObject.addProperty("fill-pattern", value);
  }
}
