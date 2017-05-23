package com.mapbox.mapboxsdk.plugins.geojson.model;


import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

public class MarkerData {
    private LatLng point;
    private JSONObject properties;

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    public JSONObject getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }
}
