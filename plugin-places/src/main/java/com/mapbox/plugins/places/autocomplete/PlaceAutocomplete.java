package com.mapbox.plugins.places.autocomplete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mapbox.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;

import java.util.List;

public class PlaceAutocomplete {

  public static final int MODE_FULLSCREEN = 1;
  public static final int MODE_CARDS = 2;

  private PlaceAutocomplete() {
  }

  public static class IntentBuilder {

    final int mode;
    final Intent intent;

    public IntentBuilder(@IntRange(from = 1, to = 2) int mode) {
      intent = new Intent();
      this.mode = mode;
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

    public IntentBuilder injectPlaces(List<CarmenFeature> carmenFeatures) {
      Bundle bundle = new Bundle();
      // TODO https://github.com/mapbox/mapbox-java/pull/646 needs to be merged first
//      bundle.putSerializable("injectPlaces", carmenFeatures);
      intent.putExtras(bundle);
      return this;
    }

    public Intent build(Activity activity) {
      if (mode == MODE_FULLSCREEN) {
        intent.setClass(activity, PlaceCompleteFullActivity.class);
      } else {
        intent.setClass(activity, PlaceCompleteCardActivity.class);
      }
      return intent;
    }
  }
}
