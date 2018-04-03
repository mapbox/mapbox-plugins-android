package com.mapbox.mapboxsdk.plugins.offline.offline;

public class OfflineConstants {

  private OfflineConstants() {
    // No Instances
  }

  static final String ACTION_START_DOWNLOAD = "com.mapbox.mapboxsdk.plugins.offline.download.start";
  static final String ACTION_CANCEL_DOWNLOAD = "com.mapbox.mapboxsdk.plugins.offline.download.cancel";
  static final String ACTION_OFFLINE = "com.mapbox.mapboxsdk.plugins.offline";
  static final String KEY_STATE = "com.mapbox.mapboxsdk.plugins.offline.state";
  static final String STATE_STARTED = "com.mapbox.mapboxsdk.plugins.offline.state.started";
  static final String STATE_FINISHED = "com.mapbox.mapboxsdk.plugins.offline.state.complete";
  static final String STATE_ERROR = "com.mapbox.mapboxsdk.plugins.offline.state.error";
  static final String STATE_CANCEL = "com.mapbox.mapboxsdk.plugins.offline.state.cancel";
  static final String STATE_PROGRESS = "com.mapbox.mapboxsdk.plugins.offline.state.progress";
  static final String KEY_BUNDLE_OFFLINE_REGION = "com.mapbox.mapboxsdk.plugins.offline.region";
  static final String KEY_BUNDLE_ERROR = "com.mapbox.mapboxsdk.plugins.offline.error";
  static final String KEY_BUNDLE_MESSAGE = "com.mapbox.mapboxsdk.plugins.offline.error";
  static final String KEY_PROGRESS = "com.mapbox.mapboxsdk.plugins.offline.progress";
  public static final String NOTIFICATION_CHANNEL = "offline";

  public static final String RETURNING_DEFINITION = "com.mapbox.mapboxsdk.plugins.offline.returning.definition";
  public static final String RETURNING_REGION_NAME = "com.mapbox.mapboxsdk.plugins.offline.returing.region.name";

  public static final String KEY_BUNDLE = "com.mapbox.mapboxsdk.plugins.offline.download.object";
}
