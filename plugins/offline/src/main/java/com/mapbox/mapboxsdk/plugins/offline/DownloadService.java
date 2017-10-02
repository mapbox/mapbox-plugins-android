package com.mapbox.mapboxsdk.plugins.offline;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LongSparseArray;
import android.widget.Toast;

import com.mapbox.androidsdk.plugins.offline.R;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.net.ConnectivityListener;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DownloadService extends Service implements ConnectivityListener {

  private final IBinder myBinder = new DownloadServiceBinder();

  static final String ACTION_START_DOWNLOAD = "com.mapbox.mapboxsdk.plugins.offline.download.start";
  static final String ACTION_CANCEL_DOWNLOAD = "com.mapbox.mapboxsdk.plugins.offline.download.cancel";
  static final int REQ_CANCEL_DOWNLOAD = 98;

  private DownloadServiceResponder downloadServiceResponder;

  private MapSnapshotter mapSnapshotter;
  private NotificationManagerCompat notificationManager;
  private NotificationCompat.Builder notificationBuilder;
  private int progressDownloadCounter;

  private List<Integer> startCommands = new ArrayList<>();

  // map offline regions to requests, ids are received through onStartCommand
  private final LongSparseArray<OfflineRegion> regionLongSparseArray = new LongSparseArray<>();

  @Override
  public void onCreate() {
    super.onCreate();
    ConnectivityReceiver receiver = ConnectivityReceiver.instance(this);
    receiver.addListener(this);
    notificationManager = NotificationManagerCompat.from(this);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return myBinder;
  }

  /**
   * Set a callback that is invoked when the progress of a download changes.
   *
   * @param downloadServiceResponder the callback
   */
  public void setDownloadServiceResponder(DownloadServiceResponder downloadServiceResponder) {
    this.downloadServiceResponder = downloadServiceResponder;
  }

  @Override
  public int onStartCommand(final Intent intent, int flags, final int startId) {
    Toast.makeText(this, "onStartCommnad with StartId " + startId, Toast.LENGTH_SHORT).show();
    startCommands.add(startId);

    String intentAction = intent.getAction();
    if (ACTION_START_DOWNLOAD.equals(intentAction)) {
      final Bundle bundle = intent.getExtras();
      final OfflineDownload offlineDownload = bundle.getParcelable(OfflineDownload.KEY_OBJECT);
      final OfflineTilePyramidRegionDefinition definition = offlineDownload.getRegionDefinition();
      final String regionName = offlineDownload.getName();
      offlineDownload.setServiceId(startId);

      // Create region, if success start download
      OfflineManager.getInstance(getApplicationContext())
        .createOfflineRegion(
          definition,
          OfflineUtils.convertRegionName(regionName),
          new OfflineManager.CreateOfflineRegionCallback() {
            @Override
            public void onCreate(OfflineRegion offlineRegion) {
              Timber.e("offline region created with %s", offlineRegion.getID());
              offlineDownload.setRegionId(offlineRegion.getID());
              offlineRegion.setDeliverInactiveMessages(false);
              regionLongSparseArray.put(offlineDownload.getServiceId(), offlineRegion);
              launchDownload(offlineDownload, offlineRegion);

              showNotification(
                offlineDownload
              );
            }

            @Override
            public void onError(String error) {
              // TODO handle error
              Timber.e("Error creating offline region: %s", error);
            }
          });
    } else if (ACTION_CANCEL_DOWNLOAD.equals(intentAction)) {
      Toast.makeText(this, "Cancel downloads", Toast.LENGTH_SHORT).show();
      OfflineDownload offlineDownload = intent.getParcelableExtra(OfflineDownload.KEY_OBJECT);
      cancelOngoingDownload(offlineDownload);
      stopService(startId);
    } else {
      stopService(startId);
    }
    return START_STICKY;
  }

  private void showNotification(final OfflineDownload offlineDownload) {
    NotificationOptions notificationOptions = offlineDownload.getNotificationOptions();

    Intent notificationIntent = new Intent(this, notificationOptions.getReturnActivity());
    notificationIntent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);

    Intent cancelIntent = new Intent(this, DownloadService.class);
    cancelIntent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);
    cancelIntent.setAction(ACTION_CANCEL_DOWNLOAD);

    PendingIntent pendingIntent = PendingIntent.getActivity(
      this,
      0,
      notificationIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    );

    notificationBuilder = new NotificationCompat.Builder(this)
      .setContentTitle(notificationOptions.getContentTitle())
      .setContentText(notificationOptions.getContextText())
      .setCategory(NotificationCompat.CATEGORY_PROGRESS)
      .setSmallIcon(notificationOptions.getSmallIconRes())
      .setContentIntent(pendingIntent)
      .addAction(R.drawable.ic_cancel_black_24dp, "Cancel", PendingIntent.getService(this,
        REQ_CANCEL_DOWNLOAD, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT))
      .setTicker(notificationOptions.getTicker());
    startForeground(offlineDownload.getServiceId(), notificationBuilder.build());

    // create map bitmap to show as notification icon
    createMapSnapshot(offlineDownload.getRegionDefinition(), new MapboxMap.SnapshotReadyCallback() {
      @Override
      public void onSnapshotReady(Bitmap snapshot) {
        notificationBuilder.setLargeIcon(snapshot);
        notificationManager.notify(offlineDownload.getServiceId(), notificationBuilder.build());
      }
    });
  }

  private void createMapSnapshot(OfflineTilePyramidRegionDefinition definition,
                                 MapboxMap.SnapshotReadyCallback callback) {
    Resources resources = getResources();
    int height = (int) resources.getDimension(android.R.dimen.notification_large_icon_height);
    int width = (int) resources.getDimension(android.R.dimen.notification_large_icon_width);

    MapSnapshotter.Options options = new MapSnapshotter.Options(width, height);
    options.withStyle(definition.getStyleURL());
    options.withRegion(definition.getBounds());
    mapSnapshotter = new MapSnapshotter(this, options);
    mapSnapshotter.start(callback);
  }

  public void cancelOngoingDownload(OfflineDownload offlineDownload) {
    int serviceId = offlineDownload.getServiceId();
    if (serviceId == -1) {
      throw new RuntimeException();
    }
    OfflineRegion offlineRegion = regionLongSparseArray.get(offlineDownload.getServiceId());
    offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
    offlineRegion.setObserver(null);
    offlineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
      @Override
      public void onDelete() {
        Timber.e("OFFLINE REGION CANCELED AND DELETED");
      }

      @Override
      public void onError(String error) {
        Timber.e("COULD NOT REMOVE OFFLINE REGION WHILE CANCELLING");
      }
    });
    dispatchCancelBroadcast(offlineDownload);
    notificationManager.cancel(serviceId);
    stopService(serviceId);
  }

  private void launchDownload(final OfflineDownload offlineDownload, final OfflineRegion offlineRegion) {
    progressDownloadCounter = 0;

    final long hashCode = offlineRegion.hashCode();
    final int serviceId = offlineDownload.getServiceId();
    // Set an observer
    offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
      @Override
      public void onStatusChanged(OfflineRegionStatus status) {

        // Debug
        Timber.d("%s/%s resources; %s bytes downloaded. for region %s",
          String.valueOf(status.getCompletedResourceCount()),
          String.valueOf(status.getRequiredResourceCount()),
          String.valueOf(status.getCompletedResourceSize()),
          hashCode);

        if (status.isComplete()) {
          Timber.e("Download complete for %s with notification id %s", hashCode, serviceId);
          if (notificationBuilder != null) {
            notificationManager.cancel(serviceId);
          }
          dispatchSuccessBroadcast(offlineDownload);
          offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
          offlineRegion.setObserver(null);
          regionLongSparseArray.remove(offlineDownload.getServiceId());
          stopService(serviceId);
          return;
        }

        progressDownloadCounter++;
        if (progressDownloadCounter % 10 == 0) {
          int percentage = (int) (status.getRequiredResourceCount() >= 0
            ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
            0.0);

          if (downloadServiceResponder != null) {
            downloadServiceResponder.onDownloadProgressChanged(offlineRegion.getID(), percentage);
          }

          if (notificationBuilder == null) {
            // map bitmap icon not ready yet
            return;
          }

          notificationBuilder.setProgress(100, percentage, false);
          notificationManager.notify(serviceId, notificationBuilder.build());
        }
      }

      @Override
      public void onError(OfflineRegionError error) {
        Timber.e("onError: %s, %s", error.getReason(), error.getMessage());
        dispatchErrorBroadcast(offlineDownload, error.getReason(), error.getMessage());
        stopService(offlineDownload.getServiceId());
      }

      @Override
      public void mapboxTileCountLimitExceeded(long limit) {
        Timber.e("Mapbox tile count limit exceeded: %s", limit);
      }
    });

    // Change the region state
    offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
  }

  private void dispatchSuccessBroadcast(OfflineDownload offlineDownload) {
    Intent intent = new Intent(OfflineDownload.ACTION_OFFLINE);
    intent.putExtra(OfflineDownload.KEY_STATE, OfflineDownload.STATE_FINISHED);
    intent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

  private void dispatchErrorBroadcast(OfflineDownload offlineDownload, String error, String message) {
    Intent intent = new Intent(OfflineDownload.ACTION_OFFLINE);
    intent.putExtra(OfflineDownload.KEY_STATE, OfflineDownload.STATE_ERROR);
    intent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);
    intent.putExtra(OfflineDownload.KEY_BUNDLE_ERROR, error);
    intent.putExtra(OfflineDownload.KEY_BUNDLE_MESSAGE, message);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

  private void dispatchCancelBroadcast(OfflineDownload offlineDownload) {
    Intent intent = new Intent(OfflineDownload.ACTION_OFFLINE);
    intent.putExtra(OfflineDownload.KEY_STATE, OfflineDownload.STATE_CANCEL);
    intent.putExtra(OfflineDownload.KEY_OBJECT, offlineDownload);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

  @Override
  public void onNetworkStateChanged(boolean connected) {
    Timber.e("OnNetworkStateChanged : " + connected);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Timber.e("onDestroy");
    if (mapSnapshotter != null) {
      mapSnapshotter.cancel();
    }
    ConnectivityReceiver.instance(this).removeListener(this);
  }

  public class DownloadServiceBinder extends Binder {
    public DownloadService getService() {
      return DownloadService.this;
    }
  }

  public interface DownloadServiceResponder {
    void onDownloadProgressChanged(long offlineRegionId, int percentage);
  }

  private void stopService(int serviceId) {
    printStartCommands(serviceId);
    stopSelf(serviceId);
    startCommands.remove(startCommands.indexOf(serviceId));
  }

  private void printStartCommands(int serviceId) {
    StringBuilder builder = new StringBuilder();
    for (Integer startCommand : startCommands) {
      builder.append(" ").append(startCommand);
    }
    Timber.e("stofSelf for" + serviceId);
    Timber.e("start commands " + builder.toString());
  }

  public static class RegionConstants {
    public static final String ID = "com.mapbox.mapboxsdk.plugins.offline.bundle.id";
  }
}
