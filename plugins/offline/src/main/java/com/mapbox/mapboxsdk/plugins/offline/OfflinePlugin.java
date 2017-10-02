package com.mapbox.mapboxsdk.plugins.offline;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class OfflinePlugin {

  // TODO replace with options/builder pattern
  public void downloadRegion(@NonNull Context context, OfflineDownload offlineDownload, NotificationOptions notificationOptions) {
    Context appContext = context.getApplicationContext();
    Intent intent = new Intent(appContext, DownloadService.class);
    intent.setAction(DownloadService.ACTION_START_DOWNLOAD);
    intent.putExtra(DownloadService.RegionConstants.REGION_DEFINTION, offlineDownload.getRegionDefinition());
    intent.putExtra(DownloadService.RegionConstants.NAME, offlineDownload.getName());
    intent.putExtra(DownloadService.NotificationConstants.OPTIONS, notificationOptions);
    appContext.startService(intent);
  }
}
