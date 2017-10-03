package com.mapbox.mapboxsdk.plugins.offline;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

/**
 * Wrapper around contents of a OfflineTilePyramidRegionDefinition
 */
public class OfflineDownload implements Parcelable {

  static final String ACTION_OFFLINE = "com.mapbox.mapboxsdk.plugins.offline";
  public static final String KEY_OBJECT = "com.mapbox.mapboxsdk.plugins.offline.download.object";
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

  private final OfflineTilePyramidRegionDefinition definition;
  private final String name;
  private final NotificationOptions notificationOptions;

  // unique identifier used by service + notifications
  // will be created at service startup as part of onStartCommand
  private int serviceId = -1;
  // unique identifier matching an offline region
  // will be created during the start of a download after creating an offline region
  private long regionId = -1;
  // download progress
  private int progress;

  private OfflineDownload(OfflineTilePyramidRegionDefinition offlineTilePyramidRegionDefinition, String name,
                          NotificationOptions notificationOptions) {
    this.definition = offlineTilePyramidRegionDefinition;
    this.name = name;
    this.notificationOptions = notificationOptions;
  }

  private OfflineDownload(Parcel in) {
    definition = in.readParcelable(OfflineTilePyramidRegionDefinition.class.getClassLoader());
    name = in.readString();
    serviceId = in.readInt();
    regionId = in.readLong();
    notificationOptions = in.readParcelable(NotificationOptions.class.getClassLoader());
    progress = in.readByte();
  }

  OfflineTilePyramidRegionDefinition getRegionDefinition() {
    return definition;
  }

  String getName() {
    return name;
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
    dest.writeString(name);
    dest.writeInt(serviceId);
    dest.writeLong(regionId);
    dest.writeParcelable(notificationOptions, flags);
    dest.writeInt(progress);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public OfflineDownload createFromParcel(Parcel in) {
      return new OfflineDownload(in);
    }

    public OfflineDownload[] newArray(int size) {
      return new OfflineDownload[size];
    }
  };

  public static class Options {

    private String name;
    private OfflineTilePyramidRegionDefinition definition;
    private NotificationOptions notificationOptions;

    public Options withDefinition(OfflineTilePyramidRegionDefinition definition) {
      this.definition = definition;
      return this;
    }

    public Options withName(String name) {
      this.name = name;
      return this;
    }

    public Options withNotificationOptions(NotificationOptions notificationsOptions) {
      this.notificationOptions = notificationsOptions;
      return this;
    }

    // restrict visibility, only libs allowed to invoke building the OfflineDownload
    OfflineDownload build() {
      //TODO add validation
      return new OfflineDownload(definition, name, notificationOptions);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OfflineDownload that = (OfflineDownload) o;

    if (serviceId != that.serviceId) {
      return false;
    }
    if (regionId != that.regionId) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = serviceId;
    result = 31 * result + (int) (regionId ^ (regionId >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (notificationOptions != null ? notificationOptions.hashCode() : 0);
    return result;
  }
}
