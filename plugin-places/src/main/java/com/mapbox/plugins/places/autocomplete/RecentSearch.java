package com.mapbox.plugins.places.autocomplete;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RecentSearch {

  public RecentSearch(@NonNull String placeId, String carmenFeature) {
    this.placeId = placeId;
    this.carmenFeature = carmenFeature;
  }

  @NonNull
  @PrimaryKey
  private String placeId;

  @ColumnInfo(name = "carmen_feature")
  private String carmenFeature;

  @NonNull
  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(@NonNull String placeId) {
    this.placeId = placeId;
  }

  public String getCarmenFeature() {
    return carmenFeature;
  }

  public void setCarmenFeature(String carmenFeature) {
    this.carmenFeature = carmenFeature;
  }
}
