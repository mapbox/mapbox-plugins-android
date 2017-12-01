package com.mapbox.mapboxsdk.plugins.offline;

import java.util.ArrayList;
import java.util.List;

class OfflineDownloadChangeDispatcher implements OfflineDownloadChangeListener {

  private final List<OfflineDownloadChangeListener> changeListeners = new ArrayList<>();

  void addListener(OfflineDownloadChangeListener offlineDownloadChangeListener) {
    changeListeners.add(offlineDownloadChangeListener);
  }

  void removeListener(OfflineDownloadChangeListener offlineDownloadChangeListener) {
    if (changeListeners.contains(offlineDownloadChangeListener)) {
      changeListeners.remove(offlineDownloadChangeListener);
    }
  }

  @Override
  public void onCreate(OfflineDownload offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onCreate(offlineDownload);
      }
    }
  }

  @Override
  public void onSuccess(OfflineDownload offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onSuccess(offlineDownload);
      }
    }
  }

  @Override
  public void onCancel(OfflineDownload offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onCancel(offlineDownload);
      }
    }
  }

  @Override
  public void onError(OfflineDownload offlineDownload, String error, String message) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onError(offlineDownload, error, message);
      }
    }
  }

  @Override
  public void onProgress(OfflineDownload offlineDownload, int progress) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onProgress(offlineDownload, progress);
      }
    }
  }
}
