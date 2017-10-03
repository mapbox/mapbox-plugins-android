package com.mapbox.mapboxsdk.plugins.offline;

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
    String actionName = intent.getStringExtra(OfflineDownload.KEY_STATE);
    OfflineDownload offlineDownload = intent.getParcelableExtra(OfflineDownload.KEY_OBJECT);

    switch (actionName) {
      case OfflineDownload.STATE_STARTED:
        offlinePlugin.addDownload(offlineDownload);
        break;
      case OfflineDownload.STATE_ERROR:
        String error = intent.getStringExtra(OfflineDownload.KEY_BUNDLE_OFFLINE_REGION);
        String message = intent.getStringExtra(OfflineDownload.KEY_BUNDLE_ERROR);
        offlinePlugin.errorDownload(offlineDownload, error, message);
        break;
      case OfflineDownload.STATE_PROGRESS:
        int progress = intent.getIntExtra(OfflineDownload.KEY_PROGRESS, 0);
        offlinePlugin.onProgressChanged(offlineDownload, progress);
        break;
      default:
        // removes the offline download (cancel or finish)
        offlinePlugin.removeDownload(offlineDownload, actionName.equals(OfflineDownload.STATE_CANCEL));
        break;
    }
  }
}
