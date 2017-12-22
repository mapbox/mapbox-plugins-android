package com.mapbox.mapboxsdk.plugins.offline;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

public class OfflineDownloadOptions {

  private byte[] metadata;
  private OfflineTilePyramidRegionDefinition definition;
  private NotificationOptions notificationOptions;

  public OfflineDownloadOptions withDefinition(OfflineTilePyramidRegionDefinition definition) {
    this.definition = definition;
    return this;
  }

  public OfflineDownloadOptions withMetadata(byte[] metadata) {
    this.metadata = metadata;
    return this;
  }

  public OfflineDownloadOptions withNotificationOptions(NotificationOptions notificationsOptions) {
    this.notificationOptions = notificationsOptions;
    return this;
  }

  // restrict visibility, only libs allowed to invoke building the OfflineDownload
  OfflineDownload build() {
    //TODO add validation
    return new OfflineDownload(definition, metadata, notificationOptions);
  }
}
