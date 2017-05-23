package com.mapbox.mapboxsdk.plugins.geojson.listener;

import com.mapbox.mapboxsdk.annotations.Marker;

import org.json.JSONObject;

public interface OnMarkerEventListener {
    /**
     * @param marker     the marker that clicked
     * @param properties the JSONObject of properties values
     */
    void onMarkerClickListener(Marker marker, JSONObject properties);
}
