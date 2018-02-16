package com.mapbox.plugins.places.picker.model;

import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.api.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.plugins.places.common.model.BasePlaceOptions;

@AutoValue
public abstract class PlacePickerOptions implements BasePlaceOptions, Parcelable {

  @Override
  @Nullable
  public abstract String language();

  @Override
  @Nullable
  public abstract String geocodingTypes();

  @Nullable
  public abstract LatLngBounds startingBounds();

  @Nullable
  @Override
  public abstract Integer toolbarColor();

  @Nullable
  public abstract CameraPosition statingCameraPosition();

  public static Builder builder() {
    return new AutoValue_PlacePickerOptions.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder toolbarColor(@ColorInt Integer toolbarColor);

    public abstract Builder language(String language);

    public Builder geocodingTypes(@NonNull @GeocodingTypeCriteria String... geocodingTypes) {
      geocodingTypes(TextUtils.join(",", geocodingTypes));
      return this;
    }

    abstract Builder geocodingTypes(String geocodingTypes);

    public abstract Builder startingBounds(@NonNull LatLngBounds bounds);

    public abstract Builder statingCameraPosition(@NonNull CameraPosition cameraPosition);

    public abstract PlacePickerOptions build();
  }
}
