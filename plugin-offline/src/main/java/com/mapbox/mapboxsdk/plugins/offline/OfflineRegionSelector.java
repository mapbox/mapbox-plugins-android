package com.mapbox.mapboxsdk.plugins.offline;

import android.app.Activity;
import android.content.Intent;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.model.DownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;
import com.mapbox.mapboxsdk.plugins.offline.ui.OfflineActivity;

import static com.mapbox.mapboxsdk.plugins.offline.OfflineConstants.RETURNING_DEFINITION;
import static com.mapbox.mapboxsdk.plugins.offline.OfflineConstants.RETURNING_REGION_NAME;

public class OfflineRegionSelector {

  private OfflineRegionSelector() {
    // No Instances
  }

  public static DownloadOptions getDownloadOptions(final Intent data, byte[] metadata) {
    return DownloadOptions.builder()
      .definition(getRegionDefinition(data))
      .regionName(getRegionName(data))
      .metadata(metadata)
      .build();
  }

  public static DownloadOptions getDownloadOptions(final Intent data, NotificationOptions notificationOptions) {
    return DownloadOptions.builder()
      .definition(getRegionDefinition(data))
      .regionName(getRegionName(data))
      .notificationOptions(notificationOptions)
      .build();
  }

  public static DownloadOptions getDownloadOptions(final Intent data,
                                                   NotificationOptions notificationOptions, byte[] metadata) {
    return DownloadOptions.builder()
      .definition(getRegionDefinition(data))
      .regionName(getRegionName(data))
      .notificationOptions(notificationOptions)
      .metadata(metadata)
      .build();
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
