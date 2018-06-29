package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.Place;

@Entity(tableName = "places")
public class PlaceEntity implements Place {

  @NonNull
  @PrimaryKey
  private String placeId;

  @ColumnInfo(name = "carmen_feature")
  private CarmenFeature carmenFeature;

  @ColumnInfo(name = "favorite_item")
  private boolean favoriteItem;

  public PlaceEntity(@NonNull String placeId, CarmenFeature carmenFeature, boolean favoriteItem) {
    this.placeId = placeId;
    this.carmenFeature = carmenFeature;
    this.favoriteItem = favoriteItem;
  }

  @Override
  @NonNull
  public String getPlaceId() {
    return placeId;
  }

  @Override
  public CarmenFeature getCarmenFeature() {
    return carmenFeature;
  }

  @Override
  public boolean getFavoriteItem() {
    return favoriteItem;
  }
}
