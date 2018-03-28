package com.mapbox.mapboxsdk.plugins.offline.ui;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

public interface RegionSelectedCallback {

  void onSelected(OfflineTilePyramidRegionDefinition definition, String regionName);

  void onCancel();
}
