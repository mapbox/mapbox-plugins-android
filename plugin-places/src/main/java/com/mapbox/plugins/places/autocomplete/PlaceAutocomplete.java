package com.mapbox.plugins.places.autocomplete;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntRange;

import com.mapbox.geojson.Point;

public class PlaceAutocomplete {

  public static final int THEME_FULLSCREEN = 2;
  public static final int THEME_OVERLAY = 1;

  private PlaceAutocomplete() {

  }


  public static class IntentBuilder {

    final Intent intent;

    public IntentBuilder(@IntRange(from = 1, to = 2) int mode) {
      intent = new Intent();
      intent.putExtra("mode", mode);
    }

    public IntentBuilder boundingBoxBias(Point southwest, Point northeast) {
//      intent.putExtra()
      return this;
    }

    public IntentBuilder country() {

      return this;
    }

    public IntentBuilder countries() {

      return this;
    }

    public IntentBuilder proximity() {

      return this;
    }

    public IntentBuilder types() {

      return this;
    }

    public IntentBuilder language() {

      return this;
    }

    public IntentBuilder limit() {

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
