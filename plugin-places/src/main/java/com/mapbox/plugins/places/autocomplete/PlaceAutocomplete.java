package com.mapbox.plugins.places.autocomplete;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mapbox.geocoding.v5.GeocodingCriteria;
import com.mapbox.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.geojson.Point;

import java.util.Locale;

public class PlaceAutocomplete {

  private PlaceAutocomplete() {

  }

  public static class IntentBuilder {

    final Intent intent;

    public IntentBuilder() {
      intent = new Intent();
    }

    public IntentBuilder boundingBoxBias(Point southwest, Point northeast) {
//      intent.putExtra()
      return this;
    }

    // TODO introduce theme object that can be passed in
    public IntentBuilder backgroundColor(@ColorInt int backgroundColor) {
      intent.putExtra("backgroundColor", backgroundColor);
      return this;
    }

    public IntentBuilder countries() {

      return this;
    }

    public IntentBuilder proximity(@NonNull Point proximity) {
      intent.putExtra("proximity", proximity.toJson());
      return this;
    }

    public IntentBuilder types(@NonNull @GeocodingTypeCriteria String... geocodingTypes) {
      String types = TextUtils.join(",", geocodingTypes);
      intent.putExtra("types", types);
      return this;
    }

    public IntentBuilder language(String languages) {
      intent.putExtra("language", languages);
      return this;
    }

    public IntentBuilder limit(@IntRange(from = 1, to = 10) int limit) {
      intent.putExtra("limit", limit);
      return this;
    }

    public IntentBuilder injectPlaces() {

      return this;
    }

    public Intent build(Activity activity) {
      intent.setClass(activity, PlacesCompleteActivity.class);
      return intent;
    }
  }
}
