package com.mapbox.mapboxsdk.plugins.offline.offline;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.utils.NotificationUtils;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.KEY_BUNDLE;
import static com.mapbox.mapboxsdk.plugins.offline.utils.NotificationUtils.setupNotificationChannel;

/**
 * Internal usage only, use this service indirectly by using methods found in
 * {@link OfflinePlugin}. When an offline download is initiated
 * for the first time using this plugin, this service is created, captures the {@code StartCommand}
 * and collects the {@link OfflineDownloadOptions} instance which holds all the download metadata
 * needed to perform the download.
 * <p>
 * If another offline download is initiated through the
 * {@link OfflinePlugin} while another download is currently in
 * process, this service will add it to the {@link OfflineManager} queue for downloading,
 * downstream, this will execute the region downloads asynchronously (although writing to the same
 * file). Once all downloads have been completed, this service is stopped and destroyed.
 *
 * @since 0.1.0
 */
public class OfflineDownloadService extends Service {

  private MapSnapshotter mapSnapshotter;
  NotificationManagerCompat notificationManager;
  NotificationCompat.Builder notificationBuilder;
  OfflineDownloadStateReceiver broadcastReceiver;

  // map offline regions to requests, ids are received with onStartCommand, these match serviceId
  // in OfflineDownloadOptions
  final LongSparseArray<OfflineRegion> regionLongSparseArray = new LongSparseArray<>();

  @Override
  public void onCreate() {
    super.onCreate();
    Timber.v("Service onCreate method called.");
    // Setup notification manager and channel
    notificationManager = NotificationManagerCompat.from(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      setupNotificationChannel();
    }

    // Register the broadcast receiver needed for updating APIs in the OfflinePlugin class.
    broadcastReceiver = new OfflineDownloadStateReceiver();
    IntentFilter filter = new IntentFilter(OfflineConstants.ACTION_OFFLINE);
    getApplicationContext().registerReceiver(broadcastReceiver, filter);
  }

  /**
   * Called each time a new download is initiated. First it acquires the
   * {@link OfflineDownloadOptions} from the intent and if found, the process of downloading the
   * offline region carries on to the {@link #onResolveCommand(String, OfflineDownloadOptions)}.
   * If the {@link OfflineDownloadOptions} fails to be found inside the intent, the service is
   * stopped (only if no other downloads are currently running) and throws a
   * {@link NullPointerException}.
   * <p>
   * {@inheritDoc}
   *
   * @since 0.1.0
   */
  @Override
  public int onStartCommand(final Intent intent, int flags, final int startId) {
    Timber.v("onStartCommand called.");
    if (intent != null) {
      final OfflineDownloadOptions offlineDownload = intent.getParcelableExtra(KEY_BUNDLE);
      if (offlineDownload != null) {
        onResolveCommand(intent.getAction(), offlineDownload);
      } else {
        stopSelf(startId);
        throw new NullPointerException("A DownloadOptions instance must be passed into the service to"
            + " begin downloading.");
      }
    }
    return START_STICKY;
  }

  /**
   * Several actions can take place inside this service including starting and canceling a specific
   * region download. First, it is determined what action to take by using the {@code intentAction}
   * parameter. This action is finally passed in to the correct map offline methods.
   *
   * @param intentAction    string holding the task that should be performed on the specific
   *                        {@link OfflineDownloadOptions} regional download.
   * @param offlineDownload the download model which defines the region and other metadata needed to
   *                        download the correct region.
   * @since 0.1.0
   */
  private void onResolveCommand(String intentAction, OfflineDownloadOptions offlineDownload) {
    if (OfflineConstants.ACTION_START_DOWNLOAD.equals(intentAction)) {
      createDownload(offlineDownload);
    } else if (OfflineConstants.ACTION_CANCEL_DOWNLOAD.equals(intentAction)) {
      cancelDownload(offlineDownload);
    }
  }

  private void createDownload(final OfflineDownloadOptions offlineDownload) {
    final OfflineRegionDefinition definition = offlineDownload.definition();
    final byte[] metadata = offlineDownload.metadata();
    OfflineManager.getInstance(getApplicationContext())
      .createOfflineRegion(
        definition,
        metadata,
        new OfflineManager.CreateOfflineRegionCallback() {
          @Override
          public void onCreate(OfflineRegion offlineRegion) {
            OfflineDownloadOptions options
              = offlineDownload.toBuilder().uuid(offlineRegion.getID()).build();
            OfflineDownloadStateReceiver.dispatchStartBroadcast(getApplicationContext(), options);
            regionLongSparseArray.put(options.uuid(), offlineRegion);

            launchDownload(options, offlineRegion);
            showNotification(options);
          }

          @Override
          public void onError(String error) {
            OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), offlineDownload, error);
          }
        });
  }


  void showNotification(final OfflineDownloadOptions offlineDownload) {
    notificationBuilder = NotificationUtils.toNotificationBuilder(this,
      offlineDownload, OfflineDownloadStateReceiver.createNotificationIntent(
        getApplicationContext(), offlineDownload), offlineDownload.notificationOptions(),
      OfflineDownloadStateReceiver.createCancelIntent(getApplicationContext(), offlineDownload)
    );
    if (regionLongSparseArray.isEmpty()) {
      startForeground(offlineDownload.uuid().intValue(), notificationBuilder.build());
    } else {
      notificationManager.notify(offlineDownload.uuid().intValue(), notificationBuilder.build());
    }
    if (offlineDownload.notificationOptions().requestMapSnapshot()) {
      // create map bitmap to show as notification icon
      createMapSnapshot(offlineDownload.definition(), new MapSnapshotter.SnapshotReadyCallback() {
        @Override
        public void onSnapshotReady(MapSnapshot snapshot) {
          final int regionId = offlineDownload.uuid().intValue();
          if (regionLongSparseArray.get(regionId) != null) {
            notificationBuilder.setLargeIcon(snapshot.getBitmap());
            notificationManager.notify(regionId, notificationBuilder.build());
          }
        }
      });
    }
  }

  private void createMapSnapshot(OfflineRegionDefinition definition,
                                 MapSnapshotter.SnapshotReadyCallback callback) {
    Resources resources = getResources();
    int height = (int) resources.getDimension(android.R.dimen.notification_large_icon_height);
    int width = (int) resources.getDimension(android.R.dimen.notification_large_icon_width);

    MapSnapshotter.Options options = new MapSnapshotter.Options(width, height);
    options.withStyle(definition.getStyleURL());
    options.withRegion(definition.getBounds());
    mapSnapshotter = new MapSnapshotter(this, options);
    mapSnapshotter.start(callback);
  }

  private void cancelDownload(final OfflineDownloadOptions offlineDownload) {
    int serviceId = offlineDownload.uuid().intValue();
    OfflineRegion offlineRegion = regionLongSparseArray.get(serviceId);
    if (offlineRegion != null) {
      offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
      offlineRegion.setObserver(null);
      offlineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
        @Override
        public void onDelete() {
          // no-op
        }

        @Override
        public void onError(String error) {
          OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), offlineDownload, error);
        }
      });
    }
    OfflineDownloadStateReceiver.dispatchCancelBroadcast(getApplicationContext(), offlineDownload);
    removeOfflineRegion(serviceId);
  }

  private synchronized void removeOfflineRegion(int regionId) {
    if (notificationBuilder != null) {
      notificationManager.cancel(regionId);
    }
    regionLongSparseArray.remove(regionId);
    if (regionLongSparseArray.size() == 0) {
      stopForeground(true);
    }
    stopSelf(regionId);
  }

  void launchDownload(final OfflineDownloadOptions offlineDownload, final OfflineRegion offlineRegion) {
    offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
      @Override
      public void onStatusChanged(OfflineRegionStatus status) {
        if (status.isComplete()) {
          finishDownload(offlineDownload, offlineRegion);
          return;
        }
        progressDownload(offlineDownload, status);
      }

      @Override
      public void onError(OfflineRegionError error) {
        OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), offlineDownload,
          error.getReason(), error.getMessage());
        stopSelf(offlineDownload.uuid().intValue());
      }

      @Override
      public void mapboxTileCountLimitExceeded(long limit) {
        OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), offlineDownload,
          "Mapbox tile count limit exceeded:" + limit);
      }
    });

    // Change the region state
    offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
  }

  /**
   * When a particular download has been completed, this method's called which handles removing the
   * notification and setting the download state.
   *
   * @param offlineRegion   the region which has finished being downloaded
   * @param offlineDownload the corresponding options used to define the offline region
   * @since 0.1.0
   */
  void finishDownload(OfflineDownloadOptions offlineDownload, OfflineRegion offlineRegion) {
    OfflineDownloadStateReceiver.dispatchSuccessBroadcast(this, offlineDownload);
    offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
    offlineRegion.setObserver(null);
    removeOfflineRegion(offlineDownload.uuid().intValue());
  }

  void progressDownload(OfflineDownloadOptions offlineDownload, OfflineRegionStatus status) {
    int percentage = (int) (status.getRequiredResourceCount() >= 0
      ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
      0.0);

    offlineDownload = offlineDownload.toBuilder().progress(percentage).build();

    if (percentage % 2 == 0 && regionLongSparseArray.get(offlineDownload.uuid().intValue()) != null) {
      OfflineDownloadStateReceiver.dispatchProgressChanged(this, offlineDownload, percentage);
      if (notificationBuilder != null) {
        notificationBuilder.setProgress(100, percentage, false);
        notificationManager.notify(offlineDownload.uuid().intValue(), notificationBuilder.build());
      }
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mapSnapshotter != null) {
      mapSnapshotter.cancel();
    }
    if (broadcastReceiver != null) {
      getApplicationContext().unregisterReceiver(broadcastReceiver);
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // don't provide binding
    return null;
  }
}
