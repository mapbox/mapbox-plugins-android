package com.mapbox.mapboxsdk.plugins.offline.utils;

import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;

public class MathUtils {

  public static int progressDownload(OfflineRegionStatus status) {
    return (int) (status.getRequiredResourceCount() >= 0
      ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) : 0.0);

  }
}
