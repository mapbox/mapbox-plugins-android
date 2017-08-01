package com.mapbox.mapboxsdk.plugins.geojson.model;


import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

public class PolyData {
    private List<LatLng> points;
    private String type;

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
