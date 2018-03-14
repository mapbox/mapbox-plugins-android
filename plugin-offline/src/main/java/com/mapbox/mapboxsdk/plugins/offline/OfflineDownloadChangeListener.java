package com.mapbox.mapboxsdk.plugins.offline;

import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;

public interface OfflineDownloadChangeListener {

  void onCreate(DownloadOptions offlineDownload);

  void onSuccess(DownloadOptions offlineDownload);

  void onCancel(DownloadOptions offlineDownload);

  void onError(DownloadOptions offlineDownload, String error, String message);

  void onProgress(DownloadOptions offlineDownload, int progress);

}
