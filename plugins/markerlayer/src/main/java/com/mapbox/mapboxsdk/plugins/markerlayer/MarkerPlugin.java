package com.mapbox.mapboxsdk.plugins.markerlayer;

import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MarkerPlugin {

  private final MarkerSource markerSource;
  private final MarkerLayer markerLayer;
  private long currentId;

  /**
   * Create a marker layer plugin.
   *
   * @param mapboxMap the MapboxMap to apply the marker layer plugin with
   */
  public MarkerPlugin(@NonNull MapboxMap mapboxMap) {
    this.markerSource = new MarkerSource(mapboxMap);
    this.markerLayer = new MarkerLayer(mapboxMap);
  }

  public void addMarker(Marker marker) {
    markerSource.addMarker(marker, String.valueOf(currentId));
    currentId++;
  }
}
