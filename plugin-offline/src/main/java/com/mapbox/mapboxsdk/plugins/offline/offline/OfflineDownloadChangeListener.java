package com.mapbox.mapboxsdk.plugins.offline.offline;

import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;

public interface OfflineDownloadChangeListener {

  void onCreate(OfflineDownloadOptions offlineDownload);

  void onSuccess(OfflineDownloadOptions offlineDownload);

  void onCancel(OfflineDownloadOptions offlineDownload);

  void onError(OfflineDownloadOptions offlineDownload, String error, String message);

  void onProgress(OfflineDownloadOptions offlineDownload, int progress);

}
