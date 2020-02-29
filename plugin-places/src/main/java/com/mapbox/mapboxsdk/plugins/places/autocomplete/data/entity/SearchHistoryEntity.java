package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity;


import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.SearchHistory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchhistory")
public class SearchHistoryEntity implements SearchHistory {

  public SearchHistoryEntity(@NonNull String placeId, CarmenFeature carmenFeature) {
    this.placeId = placeId;
    this.carmenFeature = carmenFeature;
  }

  @NonNull
  @PrimaryKey
  private String placeId;

  @ColumnInfo(name = "carmen_feature")
  private CarmenFeature carmenFeature;

  @Override
  @NonNull
  public String getPlaceId() {
    return placeId;
  }

  @Override
  public CarmenFeature getCarmenFeature() {
    return carmenFeature;
  }
}
