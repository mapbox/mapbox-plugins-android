package com.mapbox.mapboxsdk.plugins.offline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OfflineStateReceiver extends BroadcastReceiver {

  private OfflinePlugin offlinePlugin;

  public OfflineStateReceiver() {
    offlinePlugin = OfflinePlugin.getInstance();
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    String actionName = intent.getStringExtra(OfflineDownload.KEY_STATE);
    OfflineDownload offlineDownload = intent.getParcelableExtra(OfflineDownload.KEY_OBJECT);
    if (actionName.equals(OfflineDownload.STATE_STARTED)) {
      Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();
      offlinePlugin.addDownload(offlineDownload);
    } else {
      offlinePlugin.removeDownload(offlineDownload);
    }

    if (actionName.equals(OfflineDownload.STATE_FINISHED)) {
      Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show();
    } else if (actionName.equals(OfflineDownload.STATE_CANCEL)) {
      Toast.makeText(context, "Download cancel", Toast.LENGTH_SHORT).show();
    } else if (actionName.equals(OfflineDownload.STATE_ERROR)) {
      String error = intent.getStringExtra(OfflineDownload.KEY_BUNDLE_OFFLINE_REGION);
      String message = intent.getStringExtra(OfflineDownload.KEY_BUNDLE_ERROR);
      Toast.makeText(context, "Offline Download error: " + error + " " + message, Toast.LENGTH_SHORT).show();
    }
  }
}
