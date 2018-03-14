package com.mapbox.mapboxsdk.plugins.offline.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.mapbox.mapboxsdk.plugins.offline.OfflineConstants;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownloadStateReceiver;
import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class NotificationUtils {

  // TODO allow customizing the channel name and color
  @RequiresApi(api = Build.VERSION_CODES.O)
  public static void setupNotificationChannel() {
    NotificationManager manager
      = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationChannel channel = new NotificationChannel(OfflineConstants.NOTIFICATION_CHANNEL,
      "Offline", NotificationManager.IMPORTANCE_DEFAULT);
    channel.setLightColor(Color.GREEN);
    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    manager.createNotificationChannel(channel);
  }

  public static NotificationCompat.Builder buildNotification(final DownloadOptions downloadOptions) {
    return OfflineUtils.toNotificationBuilder(getApplicationContext(),
      OfflineDownloadStateReceiver.createNotificationIntent(getApplicationContext(), downloadOptions),
      downloadOptions.notificationOptions(),
      OfflineDownloadStateReceiver.createCancelIntent(getApplicationContext(), downloadOptions)
    );
  }
}