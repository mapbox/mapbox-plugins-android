package com.mapbox.mapboxsdk.plugins.offline;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


public class OfflinePlugin {

  //  private WeakReference<Context> context;
  //  private static OfflinePlugin instance;

  //  public static synchronized OfflinePlugin getInstance(@NonNull Context context) {
  //    if (instance == null) {
  //      instance = new OfflinePlugin(context);
  //    }
  //    return instance;
  //  }

  public void downloadRegion(@NonNull Context context, OfflineDownload offlineDownload, String returnActivity) {
    //    Context appContext = context.get();
    Context appContext = context.getApplicationContext();
    Intent intent = new Intent(appContext, DownloadService.class);
    intent.setAction(DownloadService.ACTION_START_DOWNLOAD);
    intent.putExtra(DownloadService.BUNDLE_KEY_NOTIFICATION_RETURN_ACTIVITY, returnActivity);
    // todo replace below with parceable OfflineDownload
    intent.putExtra(DownloadService.RegionConstants.REGION_NAME, offlineDownload.getRegionName());
    intent.putExtra(DownloadService.RegionConstants.LAT_NORTH_BOUNDS, offlineDownload.getLatNorth());
    intent.putExtra(DownloadService.RegionConstants.LON_EAST_BOUNDS, offlineDownload.getLonEast());
    intent.putExtra(DownloadService.RegionConstants.LAT_SOUTH_BOUNDS, offlineDownload.getLatSouth());
    intent.putExtra(DownloadService.RegionConstants.LON_WEST_BOUNDS, offlineDownload.getLonWest());
    intent.putExtra(DownloadService.RegionConstants.MIN_ZOOM, offlineDownload.getMinZoom());
    intent.putExtra(DownloadService.RegionConstants.MAX_ZOOM, offlineDownload.getMaxZoom());
    intent.putExtra(DownloadService.RegionConstants.STYLE, offlineDownload.getStyleUrl());
    appContext.startService(intent);
  }
}
