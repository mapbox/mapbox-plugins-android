package com.mapbox.mapboxsdk.plugins.geojson.listener;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.annotations.Marker;

@Deprecated
public interface OnMarkerEventListener {
  /**
   * this function will be trigger after click of markers and return JSONObject of properties
   *
   * @param marker     the marker that clicked
   * @param properties the JSONObject of properties values
   * @deprecated this plugin has been deprecated and will no longer be supported. Similar
   * functionality is built into Runtime Styling found inside the Map SDK.
   */
  @Deprecated
  void onMarkerClickListener(Marker marker, JsonObject properties);
}
