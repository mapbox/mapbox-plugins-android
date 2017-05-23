package com.mapbox.mapboxsdk.plugins.geojson.listener;

public interface OnLoadingGeoJsonListener {
    void onPreLoading();

    void onLoaded();

    /**
     * @param e the Exception occur during load & parse GeoJson file
     */
    void onLoadFailed(Exception e);
}
