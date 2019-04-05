package com.mapbox.mapboxsdk.plugins.offline.ui;

import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;

public interface RegionSelectedCallback {

  void onSelected(OfflineRegionDefinition definition, String regionName);

  void onCancel();
}
