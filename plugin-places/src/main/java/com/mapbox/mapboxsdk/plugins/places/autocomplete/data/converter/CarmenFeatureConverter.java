package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.converter;


import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public final class CarmenFeatureConverter {

  private CarmenFeatureConverter() {
    // class shouldn't be initialized
  }

  @TypeConverter
  public static CarmenFeature toCarmenFeature(String serializedCarmenFeature) {
    return serializedCarmenFeature == null ? null : CarmenFeature.fromJson(serializedCarmenFeature);
  }

  @TypeConverter
  public static String fromCarmenFeature(@NonNull CarmenFeature carmenFeature) {
    return carmenFeature.toJson();
  }
}
