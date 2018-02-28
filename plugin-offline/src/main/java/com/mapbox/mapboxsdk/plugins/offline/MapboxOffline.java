package com.mapbox.mapboxsdk.plugins.offline;

import android.app.Activity;
import android.content.Intent;

import com.mapbox.mapboxsdk.plugins.offline.ui.OfflineActivity;

public class MapboxOffline {

  private MapboxOffline() {
    // No Instances
  }

  public static class IntentBuilder {

    Intent intent;

    public IntentBuilder() {
      intent = new Intent();
    }

    public Intent build(Activity activity) {
      intent.setClass(activity, OfflineActivity.class);
      return intent;
    }
  }
}
