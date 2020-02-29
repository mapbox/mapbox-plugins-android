package com.mapbox.mapboxsdk.plugins.offline.offline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.KEY_BUNDLE;

/**
 * OfflinePlugin is the main entry point for integrating the offline plugin into your app.
 * <p>
 * To start downloading a region call {@link #startDownload(OfflineDownloadOptions)}
 * </p>
 *
 * @since 0.1.0
 */
public class OfflinePlugin {

  // Suppress warning about context being possibly leaked, we immediately get the application
  // context which removes this risk.
  @SuppressLint("StaticFieldLeak")
  private static OfflinePlugin instance;

  private final OfflineDownloadChangeDispatcher stateChangeDispatcher = new OfflineDownloadChangeDispatcher();
  private final List<OfflineDownloadOptions> offlineDownloads = new ArrayList<>();
  private final Context context;

  /**
   * Get a single instance of OfflinePlugin
   *
   * @return the single instance of OfflinePlugin
   * @since 0.1.0
   */
  public static synchronized OfflinePlugin getInstance(@NonNull Context context) {
    if (instance == null) {
      instance = new OfflinePlugin(context.getApplicationContext());
    }
    return instance;
  }

  /**
   * Private no-args constructor for singleton
   */
  private OfflinePlugin(Context context) {
    this.context = context;
  }

  //
  // public API
  //

  /**
   * Get the active offline downloads
   *
   * @return a List of active offline downloads.
   * @since 0.1.0
   */
  @NonNull
  public List<OfflineDownloadOptions> getActiveDownloads() {
    return offlineDownloads;
  }

  /**
   * Start downloading an offline download by providing an options object.
   * <p>
   * You can listen to the actual creation of the download with {@link OfflineDownloadChangeListener}.
   * </p>
   *
   * @param options the offline download builder
   * @since 0.1.0
   */
  public void startDownload(OfflineDownloadOptions options) {
    Intent intent = new Intent(context, OfflineDownloadService.class);
    intent.setAction(OfflineConstants.ACTION_START_DOWNLOAD);
    intent.putExtra(KEY_BUNDLE, options);
    context.startService(intent);
  }

  /**
   * Cancel an ongoing download.
   *
   * @param offlineDownload the offline download
   * @since 0.1.0
   */
  public void cancelDownload(OfflineDownloadOptions offlineDownload) {
    Intent intent = new Intent(context, OfflineDownloadService.class);
    intent.setAction(OfflineConstants.ACTION_CANCEL_DOWNLOAD);
    intent.putExtra(KEY_BUNDLE, offlineDownload);
    context.startService(intent);
  }

  /**
   * Get the OfflineDownloadOptions for an offline region, returns null if no download is active for region.
   *
   * @param offlineRegion the offline region to get related offline download for
   * @return the active offline download, null if not downloading the region.
   * @since 0.1.0
   */
  @Nullable
  public OfflineDownloadOptions getActiveDownloadForOfflineRegion(OfflineRegion offlineRegion) {
    OfflineDownloadOptions offlineDownload = null;
    if (!offlineDownloads.isEmpty()) {
      for (OfflineDownloadOptions download : offlineDownloads) {
        if (download.uuid() == offlineRegion.getID()) {
          offlineDownload = download;
        }
      }
    }
    return offlineDownload;
  }

  /**
   * Add a callback that is invoked when the offline download state changes.
   * <p>
   * In normal cases this method will be invoked as part of {@link Activity#onStart()}
   * </p>
   *
   * @param listener the callback that will be invoked
   * @since 0.1.0
   */
  public void addOfflineDownloadStateChangeListener(OfflineDownloadChangeListener listener) {
    stateChangeDispatcher.addListener(listener);
  }

  /**
   * remove a callback that is invoked when the offline download state changes.
   * <p>
   * In normal cases this method will be invoked as part of {@link Activity#onStop()}
   * </p>
   *
   * @param listener the callback that will be removed
   * @since 0.1.0
   */
  public void removeOfflineDownloadStateChangeListener(OfflineDownloadChangeListener listener) {
    stateChangeDispatcher.removeListener(listener);
  }

  //
  // internal API
  //

  /**
   * Called when the OfflineDownloadService has created an offline region for an offlineDownload and
   * has assigned a region and service id.
   *
   * @param offlineDownload the offline download to track
   * @since 0.1.0
   */
  void addDownload(OfflineDownloadOptions offlineDownload) {
    offlineDownloads.add(offlineDownload);
    stateChangeDispatcher.onCreate(offlineDownload);
  }

  /**
   * Called when the OfflineDownloadService has finished downloading.
   *
   * @param offlineDownload the offline download to stop tracking
   * @since 0.1.0
   */
  void removeDownload(OfflineDownloadOptions offlineDownload, boolean canceled) {
    if (canceled) {
      stateChangeDispatcher.onCancel(offlineDownload);
    } else {
      stateChangeDispatcher.onSuccess(offlineDownload);
    }
    offlineDownloads.remove(offlineDownload);
  }

  /**
   * Called when the OfflineDownloadService produced an error while downloading
   *
   * @param offlineDownload the offline download that produced an error
   * @param error           short description of the error
   * @param errorMessage    full description of the error
   * @since 0.1.0
   */
  void errorDownload(OfflineDownloadOptions offlineDownload, String error, String errorMessage) {
    stateChangeDispatcher.onError(offlineDownload, error, errorMessage);
    offlineDownloads.remove(offlineDownload);
  }

  /**
   * Called when the offline download service has made progress downloading an offline download.
   *
   * @param offlineDownload the offline download for which progress was made
   * @param progress        the amount of progress
   * @since 0.1.0
   */
  void onProgressChanged(OfflineDownloadOptions offlineDownload, int progress) {
    stateChangeDispatcher.onProgress(offlineDownload, progress);
  }
}