package com.mapbox.mapboxsdk.plugins.offline;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.mapbox.androidsdk.plugins.offline.R;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.net.ConnectivityListener;
import com.mapbox.mapboxsdk.net.ConnectivityReceiver;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

// TODO check for connection status
// TODO handle multiple requests
// TODO allow binding for receiving status updates
// TODO add ability to cancel ongoing
// TODO replace Icon in notification with a Snapshotter map
public class DownloadService extends Service implements ConnectivityListener {

  private final IBinder myBinder = new DownloadServiceBinder();

  static final String BUNDLE_KEY_NOTIFICATION_RETURN_ACTIVITY = "com.mapbox.mapboxsdk.plugins.offline.bundle.activity";
  static final String ACTION_START_DOWNLOAD = "com.mapbox.mapboxsdk.plugins.offline.download.start";
  //  static final String ACTION_CANCEL_DOWNLOAD = "com.mapbox.mapboxsdk.plugins.offline.download.cancel";
  static final int ONGOING_NOTIFICATION_ID = 99;

  private DownloadServiceResponder downloadServiceResponder;

  private NotificationManagerCompat notificationManager;
  private NotificationCompat.Builder notificationBuilder;
  private int progressDownloadCounter;

  // map offline regions to requests, ids are received through onStartCommand
  private final Map<OfflineRegion, Integer> regionMap = new HashMap<>();

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

  public void setDownloadServiceResponder(DownloadServiceResponder downloadServiceResponder) {
    this.downloadServiceResponder = downloadServiceResponder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, final int startId) {
    String intentAction = intent.getAction();
    if (ACTION_START_DOWNLOAD.equals(intentAction)) {
      Bundle bundle = intent.getExtras();

      // TODO replace below with Parceable OfflineRegion (OfflineDownload for now).
      final String regionName = bundle.getString(RegionConstants.REGION_NAME);
      String styleUrl = bundle.getString(RegionConstants.STYLE);
      float pixelRatio = getResources().getDisplayMetrics().density;
      float minZoom = bundle.getFloat(RegionConstants.MIN_ZOOM);
      float maxZoom = bundle.getFloat(RegionConstants.MAX_ZOOM);
      double latitudeNorth = bundle.getDouble(RegionConstants.LAT_NORTH_BOUNDS);
      double latitudeSouth = bundle.getDouble(RegionConstants.LAT_SOUTH_BOUNDS);
      double longitudeEast = bundle.getDouble(RegionConstants.LON_EAST_BOUNDS);
      double longitudeWest = bundle.getDouble(RegionConstants.LON_WEST_BOUNDS);
      LatLngBounds bounds = new LatLngBounds.Builder()
        .include(new LatLng(latitudeNorth, longitudeEast))
        .include(new LatLng(latitudeSouth, longitudeWest))
        .build();

      OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
        styleUrl, bounds, minZoom, maxZoom, pixelRatio);

      Intent notificationIntent = new Intent(this, resolveActivityForIntent(intent));

      // Intent cancelIntent = new Intent(this, DownloadService.class);
      // cancelIntent.setAction(ACTION_CANCEL_DOWNLOAD);

      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
      notificationBuilder = new NotificationCompat.Builder(this)
        .setContentTitle("Offline Download")
        .setContentText("Downloading..")
        .setCategory(NotificationCompat.CATEGORY_PROGRESS)
        .setSmallIcon(R.drawable.mapbox_logo_icon)
        .setContentIntent(pendingIntent)
        // .addAction(0, "Cancel", PendingIntent.getService(this,
        // REQUEST_CODE_CANCEL_DOWNLOAD, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT))
        .setTicker("Downloading map for offline use");
      startForeground(ONGOING_NOTIFICATION_ID, notificationBuilder.build());

      OfflineManager offlineManager = OfflineManager.getInstance(getApplicationContext());
      byte[] metadata = OfflineUtils.convertRegionName(regionName);
      offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
        @Override
        public void onCreate(OfflineRegion offlineRegion) {
          offlineRegion.setDeliverInactiveMessages(false);
          regionMap.put(offlineRegion, startId);
          launchDownload(offlineRegion, regionName);
        }

        @Override
        public void onError(String error) {

        }
      });
    }

    //    } else if (ACTION_CANCEL_DOWNLOAD.equals(intentAction)) {
    //      for (OfflineRegion offlineRegion : regionMap.keySet()) {
    //        offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
    //        notificationManager.cancel(ONGOING_NOTIFICATION_ID);
    //        stopSelf(regionMap.get(offlineRegion));
    //      }
    //    }
    return START_STICKY;
  }


  private void launchDownload(final OfflineRegion offlineRegion, final String regionName) {
    progressDownloadCounter = 0;

    // Set an observer
    offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
      @Override
      public void onStatusChanged(OfflineRegionStatus status) {
        // Debug
        Timber.d("%s/%s resources; %s bytes downloaded.",
          String.valueOf(status.getCompletedResourceCount()),
          String.valueOf(status.getRequiredResourceCount()),
          String.valueOf(status.getCompletedResourceSize()));

        if (status.isComplete()) {
          Timber.e("Download complete");
          notificationBuilder.setContentText(regionName + "has completed downloading").setProgress(0, 0, false);
          notificationManager.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build());
          dispatchSuccessBroadcast(OfflineDownload.fromRegion(offlineRegion));
          offlineRegion.setObserver(null);
          stopSelf(regionMap.get(offlineRegion));
          return;
        }
        progressDownloadCounter++;
        if (progressDownloadCounter % 10 == 0) {
          int percentage = (int) (status.getRequiredResourceCount() >= 0
            ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
            0.0);

          if (downloadServiceResponder != null) {
            downloadServiceResponder.onDownloadProgressChanged(percentage);
          }

          notificationBuilder.setProgress(100, percentage, false);
          notificationManager.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build());
        }
      }

      @Override
      public void onError(OfflineRegionError error) {
        Timber.e("onError: %s, %s", error.getReason(), error.getMessage());
        dispatchErrorBroadcast(OfflineDownload.fromRegion(offlineRegion), error.getReason(), error.getMessage());
        stopSelf(regionMap.get(offlineRegion));
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
    intent.putExtra(OfflineDownload.KEY_BUNDLE_OFFLINE_REGION, offlineDownload);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

  private void dispatchErrorBroadcast(OfflineDownload offlineDownload, String error, String message) {
    Intent intent = new Intent(OfflineDownload.STATE_FINISHED);
    intent.putExtra(OfflineDownload.KEY_STATE, OfflineDownload.STATE_ERROR);
    intent.putExtra(OfflineDownload.KEY_BUNDLE_OFFLINE_REGION, offlineDownload);
    intent.putExtra(OfflineDownload.KEY_BUNDLE_ERROR, error);
    intent.putExtra(OfflineDownload.KEY_BUNDLE_MESSAGE, message);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

  private void dispatchCancelBroadcast(OfflineDownload offlineDownload) {
    Intent intent = new Intent(OfflineDownload.STATE_FINISHED);
    intent.putExtra(OfflineDownload.KEY_STATE, OfflineDownload.STATE_CANCEL);
    intent.putExtra(OfflineDownload.KEY_BUNDLE_OFFLINE_REGION, offlineDownload);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

  private Class resolveActivityForIntent(Intent intent) {
    try {
      return Class.forName(intent.getExtras().getString(BUNDLE_KEY_NOTIFICATION_RETURN_ACTIVITY));
    } catch (ClassNotFoundException exception) {
      throw new RuntimeException("Could not resolve class for Activity.");
    }
  }

  @Override
  public void onNetworkStateChanged(boolean connected) {
    Timber.e("OnNetworkStateChanged : " + connected);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Timber.e("onDestroy");
    ConnectivityReceiver.instance(this).removeListener(this);
  }

  public class DownloadServiceBinder extends Binder {
    public DownloadService getService() {
      return DownloadService.this;
    }
  }

  public interface DownloadServiceResponder {
    void onDownloadProgressChanged(int percentage);
  }

  public static class RegionConstants {
    static final String REGION_NAME = "com.mapbox.mapboxsdk.plugins.offline.bundle.name";
    static final String STYLE = "com.mapbox.mapboxsdk.plugins.offline.bundle.style";
    static final String MIN_ZOOM = "com.mapbox.mapboxsdk.plugins.offline.bundle.minzoom";
    static final String MAX_ZOOM = "com.mapbox.mapboxsdk.plugins.offline.bundle.maxzoom";
    static final String LAT_NORTH_BOUNDS = "com.mapbox.mapboxsdk.plugins.offline.bundle.north";
    static final String LON_EAST_BOUNDS = "com.mapbox.mapboxsdk.plugins.offline.bundle.east";
    static final String LAT_SOUTH_BOUNDS = "com.mapbox.mapboxsdk.plugins.offline.bundle.south";
    static final String LON_WEST_BOUNDS = "com.mapbox.mapboxsdk.plugins.offline.bundle.west";
  }
}
