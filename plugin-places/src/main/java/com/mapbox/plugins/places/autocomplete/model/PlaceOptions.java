package com.mapbox.plugins.places.autocomplete.model;

import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.plugins.places.autocomplete.PlaceConstants;
import com.mapbox.services.utils.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@AutoValue
public abstract class PlaceOptions implements Parcelable {

  // Geocoding options
  @Nullable
  public abstract Point proximity();

  @Nullable
  public abstract String language();

  public abstract int limit();

  @Nullable
  public abstract String bbox();

  @Nullable
  public abstract String geocodingTypes();

  @Nullable
  public abstract String country();

  @Nullable
  public abstract List<String> injectedPlaces();

  // View options
  public abstract int backgroundColor();

  public abstract int toolbarColor();

  @Nullable
  public abstract String hint();

  public static Builder builder() {
    return new AutoValue_PlaceOptions.Builder()
      .backgroundColor(Color.TRANSPARENT)
      .toolbarColor(Color.WHITE)
      .limit(10);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    private List<String> countries = new ArrayList<>();
    private List<String> injectedPlaces = new ArrayList<>();

    public abstract Builder proximity(@Nullable Point proximity);

    public abstract Builder language(@Nullable String language);

    public Builder geocodingTypes(@NonNull @GeocodingTypeCriteria String... geocodingTypes) {
      geocodingTypes(TextUtils.join(",", geocodingTypes));
      return this;
    }

    abstract Builder geocodingTypes(String geocodingTypes);

    public abstract Builder limit(@IntRange(from = 1, to = 10) int limit);

    public Builder bbox(Point southwest, Point northeast) {
      bbox(southwest.longitude(), southwest.latitude(),
        northeast.longitude(), northeast.latitude());
      return this;
    }

    public Builder bbox(@FloatRange(from = -180, to = 180) double minX,
                        @FloatRange(from = -90, to = 90) double minY,
                        @FloatRange(from = -180, to = 180) double maxX,
                        @FloatRange(from = -90, to = 90) double maxY) {
      bbox(String.format(Locale.US, "%s,%s,%s,%s",
        TextUtils.formatCoordinate(minX),
        TextUtils.formatCoordinate(minY),
        TextUtils.formatCoordinate(maxX),
        TextUtils.formatCoordinate(maxY))
      );
      return this;
    }

    public Builder country(Locale country) {
      countries.add(country.getCountry());
      return this;
    }

    public Builder country(String... country) {
      countries.addAll(Arrays.asList(country));
      return this;
    }

    public abstract Builder country(String country);

    public abstract Builder bbox(@NonNull String bbox);

    public Builder addInjectedFeature(CarmenFeature carmenFeature) {
      carmenFeature.properties().addProperty(PlaceConstants.SAVED_PLACE, true);
      injectedPlaces.add(carmenFeature.toJson());
      return this;
    }

    abstract Builder injectedPlaces(List<String> injectedPlaces);

    public abstract Builder backgroundColor(@ColorInt int backgroundColor);

    public abstract Builder toolbarColor(@ColorInt int toolbarColor);

    public abstract Builder hint(@Nullable String hint);

    public abstract PlaceOptions autoBuild();

    public PlaceOptions build() {

      if (!countries.isEmpty()) {
        country(TextUtils.join(",", countries.toArray()));
      }

      injectedPlaces(injectedPlaces);

      return autoBuild();
    }
  }
}
