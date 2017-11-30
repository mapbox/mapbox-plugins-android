package com.mapbox.plugins.places.autocomplete.data.converter;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.mapbox.geocoding.v5.models.CarmenFeature;

public class CarmenFeatureConverter {

  @TypeConverter
  public static CarmenFeature toCarmenFeature(String serializedCarmenFeature) {
    return serializedCarmenFeature == null ? null : CarmenFeature.fromJson(serializedCarmenFeature);
  }

  @TypeConverter
  public static String fromCarmenFeature(@NonNull CarmenFeature carmenFeature) {
    return carmenFeature.toJson();
  }
}
