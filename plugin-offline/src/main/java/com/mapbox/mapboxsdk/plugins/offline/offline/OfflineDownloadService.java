package com.mapbox.mapboxsdk.plugins.offline.offline;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.util.LongSparseArray;

import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.OfflineConstants;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownloadStateReceiver;
import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadThread.OfflineCallback;
import com.mapbox.mapboxsdk.plugins.offline.utils.NotificationUtils;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter.SnapshotReadyCallback;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.plugins.offline.OfflineConstants.KEY_BUNDLE;

public class OfflineDownloadService extends Service implements OfflineCallback, SnapshotReadyCallback {

  private NotificationCompat.Builder notificationBuilder;
  private NotificationManagerCompat notificationManager;
  private MapSnapshotter mapSnapshotter;
  private final LongSparseArray<OfflineDownloadThread> downloadThreads = new LongSparseArray<>();

  @Override
  public void onCreate() {
    super.onCreate();
    Timber.v("Service onCreate method called");

    // Setup notification manager and channel
    notificationManager = NotificationManagerCompat.from(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationUtils.setupNotificationChannel();
    }
  }

  @Override
  public int onStartCommand(final Intent intent, int flags, final int startId) {
    Timber.v("onStartCommand called");
    DownloadOptions downloadOptions = intent.getParcelableExtra(KEY_BUNDLE);
    if (downloadOptions != null) {
      onResolveCommand(intent.getAction(), startId, downloadOptions);
    } else {
      stopService();
      throw new NullPointerException("A DownloadOptions instance must be passed into the service to"
        + " begin downloading.");
    }
    return START_STICKY;
  }

  private void onResolveCommand(String intentAction, int startId, DownloadOptions downloadOptions) {
    Timber.v("onResolveCommand called");
    createDownloadThread(downloadOptions);

    OfflineDownloadThread thread = downloadThreads.get(downloadOptions.uuid());
    if (OfflineConstants.ACTION_START_DOWNLOAD.equals(intentAction)) {
      Timber.v("Starting a new download");
      thread.threadAction(OfflineDownloadThread.START_DOWNLOAD);
    } else if (OfflineConstants.ACTION_CANCEL_DOWNLOAD.equals(intentAction)) {
      Timber.v("Canceling the current download");
      thread.threadAction(OfflineDownloadThread.CANCEL_DOWNLOAD);
      stopService();
    }
  }

  private void createDownloadThread(DownloadOptions downloadOptions) {
    // Setup thread
    OfflineDownloadThread thread
      = new OfflineDownloadThread(new Handler(), OfflineManager.getInstance(this), this);
    thread.start();
    thread.prepareHandler();
    downloadThreads.put(downloadOptions.uuid(), thread);
  }

  private void finishDownload(OfflineRegion offlineRegion, DownloadOptions downloadOptions) {
    if (notificationBuilder != null) {
      notificationManager.cancel(downloadOptions.uuid().intValue());
    }
    OfflineDownloadStateReceiver.dispatchSuccessBroadcast(this, downloadOptions);
    offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
    offlineRegion.setObserver(null);
    destroyThread(downloadThreads.get(downloadOptions.uuid()));
    stopService();
  }

  private void stopService() {
    // If no other downloads are running stop service
    if (downloadThreads.size() == 0) {
      stopSelf();
    }
  }

  private void progressDownload(int progress, DownloadOptions downloadOptions) {
    OfflineDownloadStateReceiver.dispatchProgressChanged(this, downloadOptions, progress);
    if (notificationBuilder != null) {
      notificationBuilder.setProgress(100, progress, false);
      notificationManager.notify(downloadOptions.uuid().intValue(), notificationBuilder.build());
    }
  }

  private void createMapSnapshot(OfflineTilePyramidRegionDefinition definition,
                                 SnapshotReadyCallback callback) {
    Resources resources = getResources();
    int height = (int) resources.getDimension(android.R.dimen.notification_large_icon_height);
    int width = (int) resources.getDimension(android.R.dimen.notification_large_icon_width);

    MapSnapshotter.Options options = new MapSnapshotter.Options(width, height);
    options.withStyle(definition.getStyleURL());
    options.withRegion(definition.getBounds());
    mapSnapshotter = new MapSnapshotter(this, options);
    mapSnapshotter.start(callback);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // Don't provide binding
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mapSnapshotter != null) {
      mapSnapshotter.cancel();
    }
  }

  private void destroyThread(OfflineDownloadThread thread) {
    if (thread == null) {
      return;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      thread.quitSafely();
    } else {
      thread.quit();
    }
  }

  //
  // Thread callbacks
  //

  @Override
  public void onDownloadStarted(DownloadOptions downloadOptions) {
    OfflineDownloadStateReceiver.dispatchStartBroadcast(getApplicationContext(), downloadOptions);
    notificationBuilder = NotificationUtils.buildNotification(downloadOptions);
    startForeground(downloadOptions.uuid().intValue(), notificationBuilder.build());
    notificationManager.notify(downloadOptions.uuid().intValue(), notificationBuilder.build());

    // create map bitmap to show as notification icon
    createMapSnapshot(downloadOptions.definition(), this);
  }

  @Override
  public void onDownloadStatusChange(OfflineRegion offlineRegion, DownloadOptions downloadOptions,
                                     OfflineRegionStatus status, int progress) {
    if (status.isComplete()) {
      finishDownload(offlineRegion, downloadOptions);
    } else {
      progressDownload(progress, downloadOptions);
    }
  }

  @Override
  public void onDownloadCancel(DownloadOptions downloadOptions) {
    OfflineDownloadStateReceiver.dispatchCancelBroadcast(getApplicationContext(), downloadOptions);
    destroyThread(downloadThreads.get(downloadOptions.uuid()));
    notificationManager.cancel(downloadOptions.uuid().intValue());
  }

  @Override
  public void onError(String message, DownloadOptions downloadOptions) {
    OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), downloadOptions, message);
    destroyThread(downloadThreads.get(downloadOptions.uuid()));
    stopService();
  }

  @Override
  public void onSnapshotReady(MapSnapshot snapshot) {
    // TODO fix
//    notificationBuilder.setLargeIcon(snapshot.getBitmap());
//    notificationManager.notify(, notificationBuilder.build());
  }
}
