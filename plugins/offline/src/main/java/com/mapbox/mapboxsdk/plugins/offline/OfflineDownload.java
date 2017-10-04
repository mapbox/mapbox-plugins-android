package com.mapbox.mapboxsdk.plugins.offline;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import java.util.Arrays;

/**
 * OfflineDownload model wraps the offline region definition with notifications options and the offline region metadata,
 * is a companion object to OfflineRegion with regionId and DownloadService with serviceId.
 */
public class OfflineDownload implements Parcelable {

  public static final String KEY_BUNDLE = "com.mapbox.mapboxsdk.plugins.offline.download.object";

  private final OfflineTilePyramidRegionDefinition definition;
  private final NotificationOptions notificationOptions;
  private final byte[] metadata;

  // unique identifier used by service + notifications
  // will be created at service startup as part of onStartCommand
  private int serviceId = -1;
  // unique identifier matching an offline region
  // will be created during the start of a download after creating an offline region
  private long regionId = -1;
  // download progress
  private int progress;

  OfflineDownload(OfflineTilePyramidRegionDefinition offlineTilePyramidRegionDefinition, byte[] metadata,
                          NotificationOptions notificationOptions) {
    this.definition = offlineTilePyramidRegionDefinition;
    this.metadata = metadata;
    this.notificationOptions = notificationOptions;
  }

  private OfflineDownload(Parcel in) {
    definition = in.readParcelable(OfflineTilePyramidRegionDefinition.class.getClassLoader());
    serviceId = in.readInt();
    regionId = in.readLong();
    notificationOptions = in.readParcelable(NotificationOptions.class.getClassLoader());
    progress = in.readInt();
    metadata = new byte[in.readInt()];
    in.readByteArray(metadata);
  }

  OfflineTilePyramidRegionDefinition getRegionDefinition() {
    return definition;
  }

  byte[] getMetadata() {
    return metadata;
  }

  int getServiceId() {
    return serviceId;
  }

  void setServiceId(int serviceId) {
    this.serviceId = serviceId;
  }

  void setRegionId(long regionId) {
    this.regionId = regionId;
  }

  public long getRegionId() {
    return regionId;
  }

  NotificationOptions getNotificationOptions() {
    return notificationOptions;
  }

  int getProgress() {
    return progress;
  }

  void setProgress(int progress) {
    this.progress = progress;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(definition, flags);
    dest.writeInt(serviceId);
    dest.writeLong(regionId);
    dest.writeParcelable(notificationOptions, flags);
    dest.writeInt(progress);
    dest.writeInt(metadata.length);
    dest.writeByteArray(metadata);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public OfflineDownload createFromParcel(Parcel in) {
      return new OfflineDownload(in);
    }

    public OfflineDownload[] newArray(int size) {
      return new OfflineDownload[size];
    }
  };

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OfflineDownload that = (OfflineDownload) o;
    return serviceId == that.serviceId && regionId == that.regionId && Arrays.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    int result = serviceId;
    result = 31 * result + (int) (regionId ^ (regionId >>> 32));
    result = 31 * result + (metadata != null ? Arrays.hashCode(metadata) : 0);
    result = 31 * result + (notificationOptions != null ? notificationOptions.hashCode() : 0);
    return result;
  }
}
