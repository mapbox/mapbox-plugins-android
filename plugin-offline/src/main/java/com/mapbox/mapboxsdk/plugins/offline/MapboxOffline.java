package com.mapbox.mapboxsdk.plugins.offline;

import android.app.Activity;
import android.content.Intent;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.ui.OfflineActivity;

import static com.mapbox.mapboxsdk.plugins.offline.Constants.RETURNING_DEFINITION;
import static com.mapbox.mapboxsdk.plugins.offline.Constants.RETURNING_REGION_NAME;

public class MapboxOffline {

  private MapboxOffline() {
    // No Instances
  }

  public static OfflineTilePyramidRegionDefinition getRegionDefinition(Intent data) {
    return data.getParcelableExtra(RETURNING_DEFINITION);
  }

  public static String getRegionName(Intent data) {
    return data.getStringExtra(RETURNING_REGION_NAME);
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
