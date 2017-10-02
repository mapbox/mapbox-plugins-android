package com.mapbox.mapboxsdk.plugins.offline;

import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

/**
 * Wrapper around contents of a OfflineTilePyramidRegionDefinition
 */
public class OfflineDownload implements Parcelable {

  public static final String ACTION_OFFLINE = "com.mapbox.mapboxsdk.plugins.offline";
  public static final IntentFilter INTENT_FILTER = new IntentFilter(ACTION_OFFLINE);

  public static final String KEY_OBJECT = "com.mapbox.mapboxsdk.plugins.offline.download.object";
  public static final String KEY_STATE = "com.mapbox.mapboxsdk.plugins.offline.state";
  public static final String STATE_FINISHED = "com.mapbox.mapboxsdk.plugins.offline.state.complete";
  public static final String STATE_ERROR = "com.mapbox.mapboxsdk.plugins.offline.state.error";
  public static final String STATE_CANCEL = "com.mapbox.mapboxsdk.plugins.offline.state.cancel";

  public static final String KEY_BUNDLE_OFFLINE_REGION = "com.mapbox.mapboxsdk.plugins.offline.region";
  public static final String KEY_BUNDLE_ERROR = "com.mapbox.mapboxsdk.plugins.offline.error";
  public static final String KEY_BUNDLE_MESSAGE = "com.mapbox.mapboxsdk.plugins.offline.error";

  // unique identifier used by service + notifications
  private int serviceId = -1;
  private long regionId = -1;
  private OfflineTilePyramidRegionDefinition definition;
  private String name;
  private NotificationOptions notificationOptions;

  private OfflineDownload(OfflineTilePyramidRegionDefinition offlineTilePyramidRegionDefinition, String name, NotificationOptions notificationOptions) {
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
  }

  OfflineTilePyramidRegionDefinition getRegionDefinition() {
    return definition;
  }

  public String getName() {
    return name;
  }

  public int getServiceId() {
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

  public NotificationOptions getNotificationOptions() {
    return notificationOptions;
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
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public OfflineDownload createFromParcel(Parcel in) {
      return new OfflineDownload(in);
    }

    public OfflineDownload[] newArray(int size) {
      return new OfflineDownload[size];
    }
  };

  public static class Builder {

    private String name;
    private OfflineTilePyramidRegionDefinition definition;
    private NotificationOptions notificationOptions;

    public Builder setDefinition(OfflineTilePyramidRegionDefinition definition) {
      this.definition = definition;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setNotificationsOptions(NotificationOptions notificationsOptions) {
      this.notificationOptions = notificationsOptions;
      return this;
    }

    public OfflineDownload build() {
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
