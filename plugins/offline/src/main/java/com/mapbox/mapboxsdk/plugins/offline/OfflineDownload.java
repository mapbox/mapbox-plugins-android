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
 * TODO replace with https://github.com/mapbox/mapbox-gl-native/issues/9872
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

  private String regionName;
  private String styleUrl;
  private double latNorth;
  private double lonEast;
  private double latSouth;
  private double lonWest;
  private float minZoom;
  private float maxZoom;

  private OfflineDownload(String regionName, String styleUrl, double latNorth, double lonEast,
                          double latSouth, double lonWest, float minZoom, float maxZoom) {
    this.regionName = regionName;
    this.styleUrl = styleUrl;
    this.latNorth = latNorth;
    this.lonEast = lonEast;
    this.latSouth = latSouth;
    this.lonWest = lonWest;
    this.minZoom = minZoom;
    this.maxZoom = maxZoom;
  }

  private OfflineDownload(Parcel in) {
    regionName = in.readString();
    styleUrl = in.readString();
    latNorth = in.readDouble();
    lonEast = in.readDouble();
    latSouth = in.readDouble();
    lonWest = in.readDouble();
    minZoom = in.readFloat();
    maxZoom = in.readFloat();
  }

  OfflineRegionDefinition toRegionDefinition() {
    LatLngBounds latLngBounds = new LatLngBounds.Builder()
      .include(new LatLng(latNorth, lonEast))
      .include(new LatLng(latSouth, lonWest))
      .build();

    return new OfflineTilePyramidRegionDefinition(styleUrl, latLngBounds, minZoom, maxZoom,
      Mapbox.getApplicationContext().getResources().getDisplayMetrics().density);
  }

  static OfflineDownload fromRegion(OfflineRegion offlineRegion) {
    OfflineTilePyramidRegionDefinition definition = (OfflineTilePyramidRegionDefinition) offlineRegion.getDefinition();
    LatLngBounds latLngBounds = definition.getBounds();
    return new Builder()
      .setRegionName(OfflineUtils.convertRegionName(offlineRegion.getMetadata()))
      .setStyleUrl(definition.getStyleURL())
      .setMaxZoom((float) definition.getMaxZoom())
      .setMinZoom((float) definition.getMinZoom())
      .setLonEast(latLngBounds.getLonEast())
      .setLonWest(latLngBounds.getLonWest())
      .setLatNorth(latLngBounds.getLatNorth())
      .setLatSouth(latLngBounds.getLatSouth())
      .build();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(regionName);
    dest.writeString(styleUrl);
    dest.writeDouble(latNorth);
    dest.writeDouble(lonEast);
    dest.writeDouble(latSouth);
    dest.writeDouble(lonWest);
    dest.writeFloat(minZoom);
    dest.writeFloat(maxZoom);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public OfflineDownload createFromParcel(Parcel in) {
      return new OfflineDownload(in);
    }

    public OfflineDownload[] newArray(int size) {
      return new OfflineDownload[size];
    }
  };

  public String getRegionName() {
    return regionName;
  }

  public String getStyleUrl() {
    return styleUrl;
  }

  public double getLatNorth() {
    return latNorth;
  }

  public double getLonEast() {
    return lonEast;
  }

  public double getLatSouth() {
    return latSouth;
  }

  public double getLonWest() {
    return lonWest;
  }

  public float getMinZoom() {
    return minZoom;
  }

  public float getMaxZoom() {
    return maxZoom;
  }

  // TODO add LatLngBounds as arg
  public static class Builder {

    private String regionName;
    private String styleUrl;
    private double latNorth;
    private double lonEast;
    private double latSouth;
    private double lonWest;
    private float minZoom;
    private float maxZoom;

    public Builder setRegionName(String regionName) {
      this.regionName = regionName;
      return this;
    }

    public Builder setStyleUrl(String styleUrl) {
      this.styleUrl = styleUrl;
      return this;
    }

    public Builder setLatNorth(double latNorth) {
      this.latNorth = latNorth;
      return this;
    }

    public Builder setLonEast(double lonEast) {
      this.lonEast = lonEast;
      return this;
    }

    public Builder setLatSouth(double latSouth) {
      this.latSouth = latSouth;
      return this;
    }

    public Builder setLonWest(double lonWest) {
      this.lonWest = lonWest;
      return this;
    }

    public Builder setMinZoom(float minZoom) {
      this.minZoom = minZoom;
      return this;
    }

    public Builder setMaxZoom(float maxZoom) {
      this.maxZoom = maxZoom;
      return this;
    }

    public OfflineDownload build() {
      // todo add validation and throw exceptions
      return new OfflineDownload(regionName, styleUrl, latNorth, lonEast, latSouth, lonWest, minZoom, maxZoom);
    }

    public static OfflineDownload fromMap(String regionName, MapboxMap mapboxMap) {
      String styleUrl = mapboxMap.getStyleUrl();
      Projection projection = mapboxMap.getProjection();
      LatLngBounds latLngBounds = projection.getVisibleRegion().latLngBounds;
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      float currentZoom = (float) cameraPosition.zoom;
      float maxZoom = (float) mapboxMap.getMaxZoomLevel();
      return new OfflineDownload(regionName, styleUrl, latLngBounds.getLatNorth(), latLngBounds.getLonEast(),
        latLngBounds.getLatSouth(), latLngBounds.getLonWest(), currentZoom, maxZoom);
    }
  }
}
