package com.mapbox.mapboxsdk.plugins.offline;

public interface OfflineDownloadChangeListener {

  void onCreate(OfflineDownload offlineDownload);

  void onSuccess(OfflineDownload offlineDownload);

  void onCancel(OfflineDownload offlineDownload);

  void onError(OfflineDownload offlineDownload, String error, String message);

  void onProgress(OfflineDownload offlineDownload, int progress);

}
