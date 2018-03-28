package com.mapbox.mapboxsdk.plugins.offline;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.util.LongSparseArray;

import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;

import static com.mapbox.mapboxsdk.plugins.offline.OfflineConstants.KEY_BUNDLE;

public class OfflineDownloadService extends Service {

  private MapSnapshotter mapSnapshotter;
  private NotificationManagerCompat notificationManager;
  private NotificationCompat.Builder notificationBuilder;

  // map offline regions to requests, ids are received with onStartCommand, these match serviceId
  // in OfflineDownloadOptions
  private final LongSparseArray<OfflineRegion> regionLongSparseArray = new LongSparseArray<>();

  @Override
  public void onCreate() {
    super.onCreate();
    notificationManager = NotificationManagerCompat.from(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      setupNotificationChannel();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void setupNotificationChannel() {
    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationChannel channel = new NotificationChannel(OfflineConstants.NOTIFICATION_CHANNEL,
      "Offline", NotificationManager.IMPORTANCE_DEFAULT);
    channel.setLightColor(Color.GREEN);
    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    manager.createNotificationChannel(channel);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    // don't provide binding
    return null;
  }

  @Override
  public int onStartCommand(final Intent intent, int flags, final int startId) {
    final OfflineDownloadOptions offlineDownload = intent.getParcelableExtra(KEY_BUNDLE);
    if (offlineDownload != null) {
      onResolveCommand(intent.getAction(), offlineDownload, startId);
    } else {
      stopSelf(startId);
    }
    return START_STICKY;
  }

  private void onResolveCommand(String intentAction, OfflineDownloadOptions offlineDownload, int startId) {
    if (OfflineConstants.ACTION_START_DOWNLOAD.equals(intentAction)) {
      offlineDownload.setServiceId(startId);
      createDownload(offlineDownload);
    } else if (OfflineConstants.ACTION_CANCEL_DOWNLOAD.equals(intentAction)) {
      cancelDownload(offlineDownload);
      stopSelf(startId);
    }
  }

  private void createDownload(final OfflineDownloadOptions offlineDownload) {
    final OfflineTilePyramidRegionDefinition definition = offlineDownload.definition();
    final byte[] metadata = offlineDownload.metadata();
    OfflineManager.getInstance(getApplicationContext())
      .createOfflineRegion(
        definition,
        metadata,
        new OfflineManager.CreateOfflineRegionCallback() {
          @Override
          public void onCreate(OfflineRegion offlineRegion) {
            offlineDownload.setRegionId(offlineRegion.getID());
            OfflineDownloadStateReceiver.dispatchStartBroadcast(getApplicationContext(), offlineDownload);
            regionLongSparseArray.put(offlineDownload.getServiceId(), offlineRegion);

            launchDownload(offlineDownload, offlineRegion);
            showNotification(offlineDownload);
          }

          @Override
          public void onError(String error) {
            OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), offlineDownload, error);
          }
        });
  }


  private void showNotification(final OfflineDownloadOptions offlineDownload) {
    notificationBuilder = OfflineUtils.toNotificationBuilder(this,
      OfflineDownloadStateReceiver.createNotificationIntent(getApplicationContext(), offlineDownload),
      offlineDownload.notificationOptions(),
      OfflineDownloadStateReceiver.createCancelIntent(getApplicationContext(), offlineDownload)
    );
    startForeground(offlineDownload.getServiceId(), notificationBuilder.build());

    // create map bitmap to show as notification icon
    createMapSnapshot(offlineDownload.definition(), new MapSnapshotter.SnapshotReadyCallback() {
      @Override
      public void onSnapshotReady(MapSnapshot snapshot) {
        notificationBuilder.setLargeIcon(snapshot.getBitmap());
        notificationManager.notify(offlineDownload.getServiceId(), notificationBuilder.build());
      }
    });
  }

  private void createMapSnapshot(OfflineTilePyramidRegionDefinition definition,
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
    int serviceId = offlineDownload.getServiceId();
    OfflineRegion offlineRegion = regionLongSparseArray.get(serviceId);
    offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
    offlineRegion.setObserver(null);
    offlineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
      @Override
      public void onDelete() {
      }

      @Override
      public void onError(String error) {
        OfflineDownloadStateReceiver.dispatchErrorBroadcast(getApplicationContext(), offlineDownload, error);
      }
    });
    OfflineDownloadStateReceiver.dispatchCancelBroadcast(getApplicationContext(), offlineDownload);
    notificationManager.cancel(serviceId);
    stopSelf(serviceId);
  }

  private void launchDownload(final OfflineDownloadOptions offlineDownload, final OfflineRegion offlineRegion) {
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
        stopSelf(offlineDownload.getServiceId());
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

  private void finishDownload(OfflineDownloadOptions offlineDownload, OfflineRegion offlineRegion) {
    if (notificationBuilder != null) {
      notificationManager.cancel(offlineDownload.getServiceId());
    }
    OfflineDownloadStateReceiver.dispatchSuccessBroadcast(this, offlineDownload);
    offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
    offlineRegion.setObserver(null);
    regionLongSparseArray.remove(offlineDownload.getServiceId());
    stopSelf(offlineDownload.getServiceId());
  }

  private void progressDownload(OfflineDownloadOptions offlineDownload, OfflineRegionStatus status) {
    int percentage = (int) (status.getRequiredResourceCount() >= 0
      ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
      0.0);

    offlineDownload.setProgress(percentage);
    if (percentage % 5 == 0) {
      OfflineDownloadStateReceiver.dispatchProgressChanged(this, offlineDownload, percentage);
      if (notificationBuilder != null) {
        notificationBuilder.setProgress(100, percentage, false);
        notificationManager.notify(offlineDownload.getServiceId(), notificationBuilder.build());
      }
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mapSnapshotter != null) {
      mapSnapshotter.cancel();
    }
  }
}
