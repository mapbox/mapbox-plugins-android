package com.mapbox.mapboxsdk.plugins.offline.offline;

import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;

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
  public void onCreate(OfflineDownloadOptions offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onCreate(offlineDownload);
      }
    }
  }

  @Override
  public void onSuccess(OfflineDownloadOptions offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onSuccess(offlineDownload);
      }
    }
  }

  @Override
  public void onCancel(OfflineDownloadOptions offlineDownload) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onCancel(offlineDownload);
      }
    }
  }

  @Override
  public void onError(OfflineDownloadOptions offlineDownload, String error, String message) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onError(offlineDownload, error, message);
      }
    }
  }

  @Override
  public void onProgress(OfflineDownloadOptions offlineDownload, int progress) {
    if (!changeListeners.isEmpty()) {
      for (OfflineDownloadChangeListener changeListener : changeListeners) {
        changeListener.onProgress(offlineDownload, progress);
      }
    }
  }
}
