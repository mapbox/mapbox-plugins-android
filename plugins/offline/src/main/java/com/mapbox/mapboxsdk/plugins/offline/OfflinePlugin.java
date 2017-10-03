package com.mapbox.mapboxsdk.plugins.offline;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class OfflinePlugin {

  public void downloadRegion(@NonNull Context context, OfflineDownload offlineDownload) {
    Context appContext = context.getApplicationContext();
    Intent intent = new Intent(appContext, DownloadService.class);
    intent.setAction(DownloadService.ACTION_START_DOWNLOAD);
    intent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);
    appContext.startService(intent);
  }
}
