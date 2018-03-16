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
import com.mapbox.mapboxsdk.plugins.offline.OfflinePlugin;
import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadThread.OfflineCallback;
import com.mapbox.mapboxsdk.plugins.offline.utils.NotificationUtils;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter.SnapshotReadyCallback;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.plugins.offline.OfflineConstants.KEY_BUNDLE;

/**
 * Internal usage only, use this service indirectly by using methods found in {@link OfflinePlugin}.
 * When an offline download is initiated for the first time using this plugin, this service is
 * created, captures the {@code StartCommand} and collects the {@link DownloadOptions} instance
 * which holds all the download metadata needed to perform the download.
 * <p>
 * If another offline download is initiated through the {@link OfflinePlugin} while another download
 * is currently in process this service will create a new thread and begin the download process
 * completely isolated from the other downloads. Once all downloads have been completed, all threads
 * are destroyed (if they haven't been destroyed already) and this service is stopped.
 *
 * @since 0.1.0
 */
public class OfflineDownloadService extends Service implements OfflineCallback, SnapshotReadyCallback {

  /**
   * Map used to track download threads and their corresponding {@link DownloadOptions} UUID value.
   */
  private final LongSparseArray<OfflineDownloadThread> downloadThreads = new LongSparseArray<>();

  private NotificationCompat.Builder notificationBuilder;
  private NotificationManagerCompat notificationManager;
  private MapSnapshotter mapSnapshotter;

  @Override
  public void onCreate() {
    super.onCreate();
    Timber.v("Service onCreate method called.");
    // Setup notification manager and channel
    notificationManager = NotificationManagerCompat.from(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationUtils.setupNotificationChannel();
    }
  }

  /**
   * Called each time a new download is initiated. First it acquires the {@link DownloadOptions}
   * from the intent and if found, the processing of downloading the offline region carries on to
   * the {@link #onResolveCommand(String, DownloadOptions)}. If the {@link DownloadOptions} fails to
   * be found inside the intent, the service is stopped (only if no other downloads are currently
   * running) and throws a nullpoint exception.
   * <p>
   * {@inheritDoc}
   *
   * @since 0.1.0
   */
  @Override
  public int onStartCommand(final Intent intent, int flags, final int startId) {
    Timber.v("onStartCommand called.");
    DownloadOptions downloadOptions = intent.getParcelableExtra(KEY_BUNDLE);
    if (downloadOptions != null) {
      onResolveCommand(intent.getAction(), downloadOptions);
    } else {
      stopService();
      throw new NullPointerException("A DownloadOptions instance must be passed into the service to"
        + " begin downloading.");
    }
    return START_STICKY;
  }

  /**
   * Several actions can be taken place inside this service including starting and canceling a
   * specific region download. This method first generates a new thread which the action will
   * actually take place. From there, it is determined what action to take by using the
   * {@code intentAction} parameter. This action is finally passed in through the
   * {@link OfflineDownloadThread#threadAction(int, DownloadOptions)}.
   *
   * @param intentAction    string holding the task that should be performed on the specific
   *                        {@link DownloadOptions} regional download.
   * @param downloadOptions the download model which defines the region and other metadata needed to
   *                        download the correct region.
   * @since 0.1.0
   */
  private void onResolveCommand(String intentAction, DownloadOptions downloadOptions) {
    Timber.v("onResolveCommand called");
    createDownloadThread(downloadOptions);

    OfflineDownloadThread thread = downloadThreads.get(downloadOptions.uuid());
    if (OfflineConstants.ACTION_START_DOWNLOAD.equals(intentAction)) {
      Timber.v("Starting a new download");
      thread.threadAction(OfflineDownloadThread.START_DOWNLOAD, downloadOptions);
    } else if (OfflineConstants.ACTION_CANCEL_DOWNLOAD.equals(intentAction)) {
      Timber.v("Canceling the current download");
      thread.threadAction(OfflineDownloadThread.CANCEL_DOWNLOAD, downloadOptions);
      stopService();
    }
  }

  /**
   * Simple method which creates a new {@link OfflineDownloadThread}, starts the thread and prepares
   * the handler. Once this is done, we take the {@link DownloadOptions} instance UUID and match it
   * with its corresponding thread.
   *
   * @param downloadOptions used to acquire the specific {@link DownloadOptions} UUID for keeping
   *                        track on which thread is downloading which defined
   *                        {@link DownloadOptions}.
   * @since 0.1.0
   */
  private void createDownloadThread(DownloadOptions downloadOptions) {
    // Setup thread
    OfflineDownloadThread thread
      = new OfflineDownloadThread(new Handler(), OfflineManager.getInstance(this), this);
    thread.start();
    thread.prepareHandler();
    downloadThreads.put(downloadOptions.uuid(), thread);
  }

  /**
   * When a particular download has been completed, this methods called which handles removing the
   * notification, setting the download state, and lastly, destroying the thread.
   *
   * @param offlineRegion   the region which has finished being downloaded
   * @param downloadOptions the corresponding options used to define the offline region
   * @since 0.1.0
   */
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

  /**
   * Stop this service only if all threads created for downloading have been killed.
   *
   * @since 0.1.0
   */
  private void stopService() {
    // If no other downloads are running stop service
    if (downloadThreads.size() == 0) {
      stopSelf();
    }
  }

  /**
   * Updates the notification progress bars.
   *
   * @param progress        the current download progress which comes from the thread
   * @param downloadOptions used to know which notification to update
   * @since 0.1.0
   */
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

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mapSnapshotter != null) {
      mapSnapshotter.cancel();
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // Don't provide binding
    return null;
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
