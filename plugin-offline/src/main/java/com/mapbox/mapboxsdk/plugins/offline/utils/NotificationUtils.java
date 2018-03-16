package com.mapbox.mapboxsdk.plugins.offline.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.mapbox.mapboxsdk.plugins.offline.OfflineConstants;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownloadStateReceiver;
import com.mapbox.mapboxsdk.plugins.offline.R;
import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;

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
    return toNotificationBuilder(getApplicationContext(), downloadOptions,
      OfflineDownloadStateReceiver.createNotificationIntent(getApplicationContext(), downloadOptions),
      downloadOptions.notificationOptions(),
      OfflineDownloadStateReceiver.createCancelIntent(getApplicationContext(), downloadOptions)
    );
  }

  public static NotificationCompat.Builder toNotificationBuilder(Context context,
                                                                 DownloadOptions downloadOptions,
                                                                 PendingIntent contentIntent,
                                                                 NotificationOptions options,
                                                                 Intent cancelIntent) {
    return new NotificationCompat.Builder(context, OfflineConstants.NOTIFICATION_CHANNEL)
      .setContentTitle(options.contentTitle())
      .setContentText(options.contentText())
      .setCategory(NotificationCompat.CATEGORY_PROGRESS)
      .setSmallIcon(options.smallIconRes())
      .setOnlyAlertOnce(true)
      .setContentIntent(contentIntent)
      .addAction(R.drawable.ic_cancel,
        context.getString(R.string.mapbox_offline_notification_action_cancel),
        PendingIntent.getService(context, downloadOptions.uuid().intValue(), cancelIntent,
          PendingIntent.FLAG_CANCEL_CURRENT));
  }
}