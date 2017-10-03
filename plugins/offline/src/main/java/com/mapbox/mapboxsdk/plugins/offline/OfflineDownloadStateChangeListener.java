package com.mapbox.mapboxsdk.plugins.offline;

public interface OfflineDownloadStateChangeListener {

  void onSuccess(OfflineDownload offlineDownload);

  void onCancel(OfflineDownload offlineDownload);

  void onError(OfflineDownload offlineDownload, String error, String message);

}
