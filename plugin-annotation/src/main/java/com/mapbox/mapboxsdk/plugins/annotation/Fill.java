// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

@UiThread
public class Fill extends Annotation {

  private final FillManager fillManager;

  /**
   * Create a fill.
   *
   * @param manager the fill manager created and managing the fill
   * @param id            the id of the fill
   */
  Fill(FillManager manager, long id) {
    super(id);
    this.fillManager = manager;
  }

  /**
   * Called to update the underlying data source.
   */
  @Override
  public void update() {
    fillManager.updateSource();
  }

  /**
   * Set a list of lists of LatLng for the fill, which represents the locations of the fill on the map
   *
   * @param latLngs a list of a lists of the locations of the line in a longitude and latitude pairs
   */
  public void setLatLngs(List<List<LatLng>> latLngs) {
    setLatLngs(latLngs, true);
  }

  /**
   * Set a list of lists of LatLng for the fill, which represents the locations of the fill on the map
   *
   * @param latLngs a list of a lists of the locations of the line in a longitude and latitude pairs
   * @param updateSource flag to indicate the source instantly
   */
  public void setLatLngs(List<List<LatLng>> latLngs, boolean updateSource) {
    List<List<Point>> points = new ArrayList<>();
    for (List<LatLng> innerLatLngs : latLngs) {
      List<Point>innerList = new ArrayList<>();
      for (LatLng latLng : innerLatLngs) {
        innerList.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
      }
      points.add(innerList);
    }
    geometry = Polygon.fromLngLats(points);
    if (updateSource) {
      fillManager.updateSource();
    }
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
   *
   * @param value constant property value for Float
   */
  public void setFillOpacity(Float value) {
    jsonObject.addProperty("fill-opacity", value);
    fillManager.updateSource();
  }

  /**
   * Get the FillColor property
   *
   * @return property wrapper value around String
   */
  public String getFillColor() {
    return jsonObject.get("fill-color").getAsString();
  }

  /**
   * Set the FillColor property
   *
   * @param value constant property value for String
   */
  public void setFillColor(String value) {
    jsonObject.addProperty("fill-color", value);
    fillManager.updateSource();
  }

  /**
   * Get the FillOutlineColor property
   *
   * @return property wrapper value around String
   */
  public String getFillOutlineColor() {
    return jsonObject.get("fill-outline-color").getAsString();
  }

  /**
   * Set the FillOutlineColor property
   *
   * @param value constant property value for String
   */
  public void setFillOutlineColor(String value) {
    jsonObject.addProperty("fill-outline-color", value);
    fillManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setFillPattern(String value) {
    jsonObject.addProperty("fill-pattern", value);
    fillManager.updateSource();
  }

}
