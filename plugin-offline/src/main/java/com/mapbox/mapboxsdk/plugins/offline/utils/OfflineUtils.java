package com.mapbox.mapboxsdk.plugins.offline.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.OfflineConstants;
import com.mapbox.mapboxsdk.plugins.offline.R;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;

import org.json.JSONObject;

public class OfflineUtils {

  private static final String JSON_CHARSET = "UTF-8";
  private static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

  public static String convertRegionName(@NonNull byte[] metadata) {
    try {
      String json = new String(metadata, JSON_CHARSET);
      JSONObject jsonObject = new JSONObject(json);
      return jsonObject.getString(JSON_FIELD_REGION_NAME);
    } catch (Exception exception) {
      return null;
    }
  }

  public static byte[] convertRegionName(String regionName) {
    byte[] metadata = null;
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(JSON_FIELD_REGION_NAME, regionName);
      String json = jsonObject.toString();
      metadata = json.getBytes(JSON_CHARSET);
    } catch (Exception exception) {
      Log.e("OfflineUtils", "Failed to encode metadata: " + exception.getMessage());
    }
    return metadata;
  }

  public static CameraPosition getCameraPosition(OfflineTilePyramidRegionDefinition definition) {
    return new CameraPosition.Builder()
      .target(definition.getBounds().getCenter())
      .zoom(definition.getMinZoom())
      .build();
  }

  public static Builder toNotificationBuilder(Context context, PendingIntent contentIntent,
                                              NotificationOptions options, Intent cancelIntent) {
    return new Builder(context, OfflineConstants.NOTIFICATION_CHANNEL)
      .setContentTitle(options.contentTitle())
      .setContentText(options.contentText())
      .setCategory(NotificationCompat.CATEGORY_PROGRESS)
      .setSmallIcon(options.smallIconRes())
      .setOnlyAlertOnce(true)
      .setContentIntent(contentIntent)
      .addAction(R.drawable.ic_cancel,
        context.getString(R.string.mapbox_offline_notification_action_cancel),
        PendingIntent.getService(context, OfflineConstants.REQ_CANCEL_DOWNLOAD, cancelIntent,
          PendingIntent.FLAG_CANCEL_CURRENT));
  }
}
