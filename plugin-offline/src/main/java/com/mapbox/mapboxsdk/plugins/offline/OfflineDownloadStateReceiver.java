package com.mapbox.mapboxsdk.plugins.offline;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OfflineDownloadStateReceiver extends BroadcastReceiver {

  private OfflinePlugin offlinePlugin;

  public OfflineDownloadStateReceiver() {
    offlinePlugin = OfflinePlugin.getInstance();
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    String actionName = intent.getStringExtra(Constants.KEY_STATE);
    OfflineDownload offlineDownload = intent.getParcelableExtra(OfflineDownload.KEY_BUNDLE);

    switch (actionName) {
      case Constants.STATE_STARTED:
        offlinePlugin.addDownload(offlineDownload);
        break;
      case Constants.STATE_ERROR:
        String error = intent.getStringExtra(Constants.KEY_BUNDLE_OFFLINE_REGION);
        String message = intent.getStringExtra(Constants.KEY_BUNDLE_ERROR);
        offlinePlugin.errorDownload(offlineDownload, error, message);
        break;
      case Constants.STATE_PROGRESS:
        int progress = intent.getIntExtra(Constants.KEY_PROGRESS, 0);
        offlinePlugin.onProgressChanged(offlineDownload, progress);
        break;
      default:
        // removes the offline download (cancel or finish)
        offlinePlugin.removeDownload(offlineDownload, actionName.equals(Constants.STATE_CANCEL));
        break;
    }
  }

  static void dispatchProgressChanged(Context context, OfflineDownload offlineDownload, int percentage) {
    Intent intent = new Intent(Constants.ACTION_OFFLINE);
    intent.putExtra(Constants.KEY_STATE, Constants.STATE_PROGRESS);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    intent.putExtra(Constants.KEY_PROGRESS, percentage);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchStartBroadcast(Context context, OfflineDownload offlineDownload) {
    Intent intent = new Intent(Constants.ACTION_OFFLINE);
    intent.putExtra(Constants.KEY_STATE, Constants.STATE_STARTED);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchSuccessBroadcast(Context context, OfflineDownload offlineDownload) {
    Intent intent = new Intent(Constants.ACTION_OFFLINE);
    intent.putExtra(Constants.KEY_STATE, Constants.STATE_FINISHED);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchErrorBroadcast(Context context, OfflineDownload offlineDownload, String error) {
    dispatchErrorBroadcast(context, offlineDownload, error, error);
  }

  static void dispatchErrorBroadcast(Context context, OfflineDownload offlineDownload, String error, String message) {
    Intent intent = new Intent(Constants.ACTION_OFFLINE);
    intent.putExtra(Constants.KEY_STATE, Constants.STATE_ERROR);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    intent.putExtra(Constants.KEY_BUNDLE_ERROR, error);
    intent.putExtra(Constants.KEY_BUNDLE_MESSAGE, message);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static void dispatchCancelBroadcast(Context context, OfflineDownload offlineDownload) {
    Intent intent = new Intent(Constants.ACTION_OFFLINE);
    intent.putExtra(Constants.KEY_STATE, Constants.STATE_CANCEL);
    intent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    context.getApplicationContext().sendBroadcast(intent);
  }

  static Intent createCancelIntent(Context context, OfflineDownload offlineDownload) {
    Intent cancelIntent = new Intent(context, OfflineDownloadService.class);
    cancelIntent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    cancelIntent.setAction(Constants.ACTION_CANCEL_DOWNLOAD);
    return cancelIntent;
  }

  static PendingIntent createNotificationIntent(Context context, OfflineDownload offlineDownload) {
    Class returnActivity = offlineDownload.getNotificationOptions().getReturnActivity();
    Intent notificationIntent = new Intent(context, returnActivity);
    notificationIntent.putExtra(OfflineDownload.KEY_BUNDLE, offlineDownload);
    return PendingIntent.getActivity(
      context,
      0,
      notificationIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    );
  }
}
