package com.mapbox.mapboxsdk.plugins.geojson.listener;

@Deprecated
public interface OnLoadingGeoJsonListener {
  /**
   * just before process for loading GeoJson file, this function will be trigger
   */
  @Deprecated
  void onPreLoading();

  /**
   * when GeoJson file loaded with event will be trigger
   */
  @Deprecated
  void onLoaded();

  /**
   * @param e the Exception occur during load and parse GeoJson file
   */
  @Deprecated
  void onLoadFailed(Exception e);
}
