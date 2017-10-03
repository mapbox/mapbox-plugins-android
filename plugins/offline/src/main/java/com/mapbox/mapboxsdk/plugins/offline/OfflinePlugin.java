package com.mapbox.mapboxsdk.plugins.offline;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.offline.OfflineRegion;

import java.util.ArrayList;
import java.util.List;

public class OfflinePlugin {

  private static OfflinePlugin INSTANCE;

  private List<OfflineDownload> offlineDownloads = new ArrayList<>();

  public static synchronized OfflinePlugin getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new OfflinePlugin();
    }
    return INSTANCE;
  }

  private OfflinePlugin() {

  }

  /**
   * Download a offline region based on an offline download.
   *
   * @param context the context to derive the application context of
   * @param offlineDownload the offline download model
   */
  public void downloadRegion(@NonNull Context context, OfflineDownload offlineDownload) {
    Context appContext = context.getApplicationContext();
    Intent intent = new Intent(appContext, DownloadService.class);
    intent.setAction(DownloadService.ACTION_START_DOWNLOAD);
    intent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);
    appContext.startService(intent);
  }

  /**
   * Called when the DownloadService has created an offline region for an offlineDownload and
   * has assigned a region and service id.
   *
   * @param offlineDownload the offline download to track
   */
  void addDownload(OfflineDownload offlineDownload) {
    if (offlineDownload.getRegionId() == -1 || offlineDownload.getServiceId() == -1) {
      throw new RuntimeException();
    }
    offlineDownloads.add(offlineDownload);
  }

  /**
   * Called when the DownloadService has finished downloading.
   *
   * @param offlineDownload the offline download to stop tracking
   */
  void removeDownload(OfflineDownload offlineDownload) {
    offlineDownloads.remove(offlineDownload);
  }

  /**
   * Get the OfflineDownload for an offline region, returns null if no download is active for region.
   *
   * @param offlineRegion the offline region to get related offline download for
   * @return the active offline download, null if not downloading the region.
   */
  @Nullable
  public OfflineDownload getDownloadForRegion(OfflineRegion offlineRegion) {
    OfflineDownload offlineDownload = null;
    if (!offlineDownloads.isEmpty()) {
      for (OfflineDownload download : offlineDownloads) {
        if (download.getRegionId() == -1) {
          throw new RuntimeException("All downloads in OfflinePlugin should be initialized (region id + service id)");
        }
        if (download.getRegionId() == offlineRegion.getID()) {
          offlineDownload = download;
        }
      }
    }
    return offlineDownload;
  }
}