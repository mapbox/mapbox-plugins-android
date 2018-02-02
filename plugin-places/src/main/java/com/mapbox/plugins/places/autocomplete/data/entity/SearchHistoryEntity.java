package com.mapbox.plugins.places.autocomplete.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.plugins.places.autocomplete.model.SearchHistory;

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

  public void setPlaceId(@NonNull String placeId) {
    this.placeId = placeId;
  }

  @Override
  public CarmenFeature getCarmenFeature() {
    return carmenFeature;
  }

  public void setCarmenFeature(CarmenFeature carmenFeature) {
    this.carmenFeature = carmenFeature;
  }
}
