package com.mapbox.mapboxsdk.plugins.offline.offline;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;

import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.KEY_BUNDLE;

public class OfflineDownloadStateReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String actionName = intent.getStringExtra(OfflineConstants.KEY_STATE);
    OfflineDownloadOptions offlineDownload = intent.getParcelableExtra(KEY_BUNDLE);

    OfflinePlugin offlinePlugin = OfflinePlugin.getInstance(context);

    switch (actionName) {
      case OfflineConstants.STATE_STARTED:
        offlinePlugin.addDownload(offlineDownload);
        break;
      case OfflineConstants.STATE_ERROR:
        String error = intent.getStringExtra(OfflineConstants.KEY_BUNDLE_OFFLINE_REGION);
        String message = intent.getStringExtra(OfflineConstants.KEY_BUNDLE_ERROR);
        offlinePlugin.errorDownload(offlineDownload, error, message);
        break;
      case OfflineConstants.STATE_PROGRESS:
        int progress = intent.getIntExtra(OfflineConstants.KEY_PROGRESS, 0);
        offlinePlugin.onProgressChanged(offlineDownload, progress);
        break;
      default:
        // removes the offline download (cancel or finish)
        offlinePlugin.removeDownload(offlineDownload, actionName.equals(OfflineConstants.STATE_CANCEL));
        break;
    }
  }

  static void dispatchProgressChanged(Context context, OfflineDownloadOptions offlineDownload,
                                      int percentage) {
    Intent intent = new Intent(OfflineConstants.ACTION_OFFLINE);
    intent.putExtra(OfflineConstants.KEY_STATE, OfflineConstants.STATE_PROGRESS);
    intent.putExtra(KEY_BUNDLE, offlineDownload);
    intent.putExtra(OfflineConstants.KEY_PROGRESS, percentage);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchStartBroadcast(Context context, OfflineDownloadOptions offlineDownload) {
    Intent intent = new Intent(OfflineConstants.ACTION_OFFLINE);
    intent.putExtra(OfflineConstants.KEY_STATE, OfflineConstants.STATE_STARTED);
    intent.putExtra(KEY_BUNDLE, offlineDownload);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchSuccessBroadcast(Context context, OfflineDownloadOptions offlineDownload) {
    Intent intent = new Intent(OfflineConstants.ACTION_OFFLINE);
    intent.putExtra(OfflineConstants.KEY_STATE, OfflineConstants.STATE_FINISHED);
    intent.putExtra(KEY_BUNDLE, offlineDownload);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchErrorBroadcast(Context context, OfflineDownloadOptions offlineDownload, String error) {
    dispatchErrorBroadcast(context, offlineDownload, error, error);
  }

  static void dispatchErrorBroadcast(Context context, OfflineDownloadOptions offlineDownload,
                                     String error, String message) {
    Intent intent = new Intent(OfflineConstants.ACTION_OFFLINE);
    intent.putExtra(OfflineConstants.KEY_STATE, OfflineConstants.STATE_ERROR);
    intent.putExtra(KEY_BUNDLE, offlineDownload);
    intent.putExtra(OfflineConstants.KEY_BUNDLE_ERROR, error);
    intent.putExtra(OfflineConstants.KEY_BUNDLE_MESSAGE, message);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchCancelBroadcast(Context context, OfflineDownloadOptions offlineDownload) {
    Intent intent = new Intent(OfflineConstants.ACTION_OFFLINE);
    intent.putExtra(OfflineConstants.KEY_STATE, OfflineConstants.STATE_CANCEL);
    intent.putExtra(KEY_BUNDLE, offlineDownload);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static Intent createCancelIntent(Context context, OfflineDownloadOptions offlineDownload) {
    Intent cancelIntent = new Intent(context, OfflineDownloadService.class);
    cancelIntent.putExtra(KEY_BUNDLE, offlineDownload);
    cancelIntent.setAction(OfflineConstants.ACTION_CANCEL_DOWNLOAD);
    return cancelIntent;
  }

  static PendingIntent createNotificationIntent(Context context, OfflineDownloadOptions offlineDownload) {
    Class returnActivity = offlineDownload.notificationOptions().getReturnActivity();
    Intent notificationIntent = new Intent(context, returnActivity);
    notificationIntent.putExtra(KEY_BUNDLE, offlineDownload);
    return PendingIntent.getActivity(
      context,
      0,
      notificationIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    );
  }
}
