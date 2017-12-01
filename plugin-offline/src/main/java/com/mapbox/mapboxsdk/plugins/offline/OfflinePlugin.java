package com.mapbox.mapboxsdk.plugins.offline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.offline.OfflineRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * OfflinePlugin is the main entry point for integrating the offline plugin into your app.
 * <p>
 * To start downloading a region call {@link #startDownload(Context, OfflineDownloadOptions)}
 * </p>
 */
public class OfflinePlugin {

  private static OfflinePlugin INSTANCE;
  private final List<OfflineDownload> offlineDownloads = new ArrayList<>();
  private final OfflineDownloadChangeDispatcher stateChangeDispatcher = new OfflineDownloadChangeDispatcher();

  /**
   * Get a single instance of OfflinePlugin
   *
   * @return the single instance of OfflinePlugin
   */
  public static synchronized OfflinePlugin getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new OfflinePlugin();
    }
    return INSTANCE;
  }

  /**
   * Private no-args constructor for singleton
   */
  private OfflinePlugin() {
  }

  //
  // public API
  //

  /**
   * Get the active offline downloads
   *
   * @return a List of active offline downloads.
   */
  @NonNull
  public List<OfflineDownload> getActiveDownloads() {
    return offlineDownloads;
  }

  /**
   * Start downloading an offline download by providing an options object.
   * <p>
   * You can listen to the actual creation of the download with {@link OfflineDownloadChangeListener}.
   * </p>
   *
   * @param context the context to derive the application context of
   * @param options the offline download builder
   */
  public void startDownload(@NonNull Context context, OfflineDownloadOptions options) {
    Context appContext = context.getApplicationContext();
    Intent intent = new Intent(appContext, OfflineDownloadService.class);
    intent.setAction(Constants.ACTION_START_DOWNLOAD);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, options.build());
    appContext.startService(intent);
  }

  /**
   * Cancel an ongoing download.
   *
   * @param context         the context to derive the application context of
   * @param offlineDownload the offline download
   */
  public void cancelDownload(@NonNull Context context, OfflineDownload offlineDownload) {
    Context appContext = context.getApplicationContext();
    Intent intent = new Intent(appContext, OfflineDownloadService.class);
    intent.setAction(Constants.ACTION_CANCEL_DOWNLOAD);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    appContext.startService(intent);
  }

  /**
   * Get the OfflineDownload for an offline region, returns null if no download is active for region.
   *
   * @param offlineRegion the offline region to get related offline download for
   * @return the active offline download, null if not downloading the region.
   */
  @Nullable
  public OfflineDownload getActiveDownloadForOfflineRegion(OfflineRegion offlineRegion) {
    OfflineDownload offlineDownload = null;
    if (!offlineDownloads.isEmpty()) {
      for (OfflineDownload download : offlineDownloads) {
        if (download.getRegionId() == offlineRegion.getID()) {
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
   */
  void addDownload(OfflineDownload offlineDownload) {
    offlineDownloads.add(offlineDownload);
    stateChangeDispatcher.onCreate(offlineDownload);
  }

  /**
   * Called when the OfflineDownloadService has finished downloading.
   *
   * @param offlineDownload the offline download to stop tracking
   */
  void removeDownload(OfflineDownload offlineDownload, boolean canceled) {
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
   */
  void errorDownload(OfflineDownload offlineDownload, String error, String errorMessage) {
    stateChangeDispatcher.onError(offlineDownload, error, errorMessage);
    offlineDownloads.remove(offlineDownload);
  }

  /**
   * Called when the offline download service has made progress downloading an offline download.
   *
   * @param offlineDownload the offline download for which progress was made
   * @param progress        the amount of progress
   */
  void onProgressChanged(OfflineDownload offlineDownload, int progress) {
    stateChangeDispatcher.onProgress(offlineDownload, progress);
  }
}