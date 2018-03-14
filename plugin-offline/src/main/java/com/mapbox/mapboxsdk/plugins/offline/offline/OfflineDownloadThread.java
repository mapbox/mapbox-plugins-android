package com.mapbox.mapboxsdk.plugins.offline.offline;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineManager.CreateOfflineRegionCallback;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegion.OfflineRegionObserver;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.utils.MathUtils;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.offline.OfflineRegion.STATE_INACTIVE;

public class OfflineDownloadThread extends HandlerThread implements Handler.Callback,
  CreateOfflineRegionCallback, OfflineRegion.OfflineRegionDeleteCallback, OfflineRegionObserver {

  private static final String THREAD_NAME = "mapbox:offline:download:thread";
  static final int START_DOWNLOAD = 2039;
  static final int CANCEL_DOWNLOAD = 3928;

  private final OfflineManager offlineManager;
  private final OfflineCallback callback;
  private final Handler responseHandler;
  private OfflineRegion offlineRegion;
  private Handler workerHandler;

  OfflineDownloadThread(Handler responseHandler, OfflineManager offlineManager, OfflineCallback callback) {
    super(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND);
    this.responseHandler = responseHandler;
    this.offlineManager = offlineManager;
    this.callback = callback;
  }

  void prepareHandler() {
    // Setup the worker thread
    workerHandler = new Handler(getLooper(), this);
  }

  void threadAction(int message, DownloadOptions downloadOptions) {
    workerHandler.obtainMessage(message, downloadOptions).sendToTarget();
  }

  @Override
  public boolean handleMessage(Message msg) {
    DownloadOptions downloadOptions = (DownloadOptions) msg.obj;
    if (msg.what == START_DOWNLOAD) {
      downloadRegion(downloadOptions);
    } else if (msg.what == CANCEL_DOWNLOAD) {
      cancelDownload();
    }
    return true;
  }

  //
  // Offline operations
  //

  private void downloadRegion(DownloadOptions downloadOptions) {
    offlineManager.createOfflineRegion(
      downloadOptions.definition(),
      downloadOptions.metadata(),
      this);
  }

  private void cancelDownload() {
    offlineRegion.setDownloadState(STATE_INACTIVE);
    offlineRegion.setObserver(null);
    offlineRegion.delete(this);
    responseHandler.post(new Runnable() {
      @Override
      public void run() {
        callback.onDownloadCancel();
      }
    });
  }

  @Override
  public void onCreate(OfflineRegion offlineRegion) {
    Timber.v("Download region created");
    this.offlineRegion = offlineRegion;
    offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    offlineRegion.setObserver(this);
    responseHandler.post(new Runnable() {
      @Override
      public void run() {
        callback.onDownloadStarted();
      }
    });
  }

  @Override
  public void onStatusChanged(final OfflineRegionStatus status) {
    // Prevent calling back to the main thread to frequently
    final int progress = MathUtils.progressDownload(status);
    if (progress % 2 == 0) {
      responseHandler.post(new Runnable() {
        @Override
        public void run() {
          callback.onDownloadStatusChange(offlineRegion, status, progress);
        }
      });
    }
  }

  @Override
  public void onDelete() {
    // no op
  }

  //
  // Error Handling
  //

  @Override
  public void onError(final String error) {
    responseHandler.post(new Runnable() {
      @Override
      public void run() {
        callback.onError(error);
      }
    });
  }

  @Override
  public void onError(final OfflineRegionError error) {
    responseHandler.post(new Runnable() {
      @Override
      public void run() {
        callback.onError(error.getReason() + "\n" + error.getMessage());
      }
    });
  }

  @Override
  public void mapboxTileCountLimitExceeded(final long limit) {
    responseHandler.post(new Runnable() {
      @Override
      public void run() {
        callback.onError("Mapbox tile count limit exceeded: " + limit);
      }
    });
  }

  //
  // Interface for returning information back to service
  //

  interface OfflineCallback {

    void onDownloadStarted();

    void onDownloadStatusChange(OfflineRegion offlineRegion, OfflineRegionStatus status, int progress);

    void onDownloadCancel();

    void onError(String message);
  }
}
