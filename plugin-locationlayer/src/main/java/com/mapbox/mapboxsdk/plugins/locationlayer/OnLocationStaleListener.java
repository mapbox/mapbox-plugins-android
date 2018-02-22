package com.mapbox.mapboxsdk.plugins.locationlayer;

public interface OnLocationStaleListener {
  void onStaleStateChange(boolean isStale);
}
