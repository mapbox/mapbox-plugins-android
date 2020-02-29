package com.mapbox.mapboxsdk.plugins.offline.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.mapbox.mapboxsdk.plugins.offline.R;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class NotificationUtils {

  private NotificationUtils() {
    // No instances
  }

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

  public static NotificationCompat.Builder toNotificationBuilder(Context context,
                                                                 OfflineDownloadOptions offlineDownload,
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
      .addAction(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? 0 : R.drawable.ic_cancel,
        options.cancelText(),
        PendingIntent.getService(context, offlineDownload.uuid().intValue(), cancelIntent,
          PendingIntent.FLAG_CANCEL_CURRENT));
  }
}