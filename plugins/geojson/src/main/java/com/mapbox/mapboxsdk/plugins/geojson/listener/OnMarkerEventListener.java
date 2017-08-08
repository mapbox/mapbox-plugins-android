package com.mapbox.mapboxsdk.plugins.geojson.listener;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.annotations.Marker;


public interface OnMarkerEventListener {
  /**
   * this function will be trigger after click of markers and return JSONObject of properties
   *
   * @param marker     the marker that clicked
   * @param properties the JSONObject of properties values
   */
  void onMarkerClickListener(Marker marker, JsonObject properties);
}
