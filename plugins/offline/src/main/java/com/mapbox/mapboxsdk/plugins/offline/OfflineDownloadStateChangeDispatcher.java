package com.mapbox.mapboxsdk.plugins.offline;

import java.util.ArrayList;
import java.util.List;

class OfflineDownloadStateChangeDispatcher implements OfflineDownloadStateChangeListener {

  private final List<OfflineDownloadStateChangeListener> changeListeners = new ArrayList<>();

  void addListener(OfflineDownloadStateChangeListener offlineDownloadStateChangeListener) {
    changeListeners.add(offlineDownloadStateChangeListener);
  }

  void removeListener(OfflineDownloadStateChangeListener offlineDownloadStateChangeListener) {
    if (changeListeners.contains(offlineDownloadStateChangeListener)) {
      changeListeners.remove(offlineDownloadStateChangeListener);
    }
  }

  @Override
  public void onSuccess(OfflineDownload offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadStateChangeListener offlineDownloadStateChangeListener : changeListeners) {
        offlineDownloadStateChangeListener.onSuccess(offlineDownload);
      }
    }
  }

  @Override
  public void onCancel(OfflineDownload offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadStateChangeListener offlineDownloadStateChangeListener : changeListeners) {
        offlineDownloadStateChangeListener.onCancel(offlineDownload);
      }
    }
  }

  @Override
  public void onError(OfflineDownload offlineDownload, String error, String message) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadStateChangeListener offlineDownloadStateChangeListener : changeListeners) {
        offlineDownloadStateChangeListener.onError(offlineDownload, error, message);
      }
    }
  }
}
