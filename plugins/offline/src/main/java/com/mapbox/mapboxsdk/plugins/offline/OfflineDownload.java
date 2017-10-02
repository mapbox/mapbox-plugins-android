package com.mapbox.mapboxsdk.plugins.offline;

import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

/**
 * Wrapper around contents of a OfflineTilePyramidRegionDefinition
 */
public class OfflineDownload implements Parcelable {

  public static final String ACTION_OFFLINE = "com.mapbox.mapboxsdk.plugins.offline";
  public static final IntentFilter INTENT_FILTER = new IntentFilter(ACTION_OFFLINE);

  public static final String KEY_STATE = "com.mapbox.mapboxsdk.plugins.offline.state";
  public static final String STATE_FINISHED = "com.mapbox.mapboxsdk.plugins.offline.state.complete";
  public static final String STATE_ERROR = "com.mapbox.mapboxsdk.plugins.offline.state.error";
  public static final String STATE_CANCEL = "com.mapbox.mapboxsdk.plugins.offline.state.cancel";

  public static final String KEY_BUNDLE_OFFLINE_REGION = "com.mapbox.mapboxsdk.plugins.offline.region";
  public static final String KEY_BUNDLE_ERROR = "com.mapbox.mapboxsdk.plugins.offline.error";
  public static final String KEY_BUNDLE_MESSAGE = "com.mapbox.mapboxsdk.plugins.offline.error";

  private OfflineTilePyramidRegionDefinition definition;
  private String name;

  private OfflineDownload(OfflineTilePyramidRegionDefinition offlineTilePyramidRegionDefinition, String name) {
    this.definition = offlineTilePyramidRegionDefinition;
    this.name = name;
  }

  private OfflineDownload(Parcel in) {
    definition = in.readParcelable(OfflineTilePyramidRegionDefinition.class.getClassLoader());
    name = in.readString();
  }

  OfflineTilePyramidRegionDefinition getRegionDefinition() {
    return definition;
  }

  public String getName() {
    return name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(definition, flags);
    dest.writeString(name);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public OfflineDownload createFromParcel(Parcel in) {
      return new OfflineDownload(in);
    }

    public OfflineDownload[] newArray(int size) {
      return new OfflineDownload[size];
    }
  };

  // TODO add LatLngBounds as arg
  public static class Builder {

    private OfflineTilePyramidRegionDefinition definition;
    private String name;

    public Builder setDefinition(OfflineTilePyramidRegionDefinition definition) {
      this.definition = definition;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public OfflineDownload build() {
      return new OfflineDownload(definition, name);
    }
  }
}
