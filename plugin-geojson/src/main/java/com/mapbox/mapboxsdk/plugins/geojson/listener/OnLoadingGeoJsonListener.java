package com.mapbox.mapboxsdk.plugins.geojson.listener;

public interface OnLoadingGeoJsonListener {
  /**
   * just before process for loading GeoJson file, this function will be trigger
   */
  void onPreLoading();

  /**
   * when GeoJson file loaded with event will be trigger
   */
  void onLoaded();

  /**
   * @param e the Exception occur during load and parse GeoJson file
   */
  void onLoadFailed(Exception e);
}
