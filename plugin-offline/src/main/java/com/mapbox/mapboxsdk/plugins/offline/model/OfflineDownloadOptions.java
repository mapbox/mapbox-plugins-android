package com.mapbox.mapboxsdk.plugins.offline.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownloadService;

/**
 * This model class wraps the offline region definition with notifications options and the offline
 * region metadata. It is a companion object to {@link OfflineRegion} with regionId and
 * {@link OfflineDownloadService} with serviceId.
 *
 * @since 0.1.0
 */
@AutoValue
public abstract class OfflineDownloadOptions implements Parcelable {

  // unique identifier used by service + notifications
  // will be created at service startup as part of onStartCommand
  private int serviceId = -1;
  // unique identifier matching an offline region
  // will be created during the start of a download after creating an offline region
  private long regionId = -1;
  // download progress
  private int progress;

  @NonNull
  public abstract OfflineTilePyramidRegionDefinition definition();

  public abstract NotificationOptions notificationOptions();

  public abstract byte[] metadata();

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public int getProgress() {
    return progress;
  }

  public int getServiceId() {
    return serviceId;
  }

  public void setServiceId(int serviceId) {
    this.serviceId = serviceId;
  }

  public void setRegionId(long regionId) {
    this.regionId = regionId;
  }

  public long getRegionId() {
    return regionId;
  }

  public static Builder builder() {
    return new AutoValue_OfflineDownloadOptions.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder definition(@NonNull OfflineTilePyramidRegionDefinition definition);

    public abstract Builder notificationOptions(NotificationOptions notificationOptions);

    public abstract Builder metadata(byte[] metadata);

    public abstract OfflineDownloadOptions build();
  }
}
