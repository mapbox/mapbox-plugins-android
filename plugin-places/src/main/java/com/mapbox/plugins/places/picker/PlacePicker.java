package com.mapbox.plugins.places.picker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.plugins.places.common.PlaceConstants;
import com.mapbox.plugins.places.picker.ui.PlacePickerActivity;

public class PlacePicker {

  private PlacePicker() {
    // No instances
  }

  public static CarmenFeature getPlace() {

    return null;
  }

  public static CameraPosition getLastCameraPosition() {

    return null;
  }

  public static class IntentBuilder {

    private Intent intent;

    /**
     * Creates a new builder that creates an intent to launch the place picker activity.
     *
     * @since 0.1.0
     */
    public IntentBuilder() {
      intent = new Intent();
    }

    public IntentBuilder accessToken(@NonNull String accessToken) {
      intent.putExtra(PlaceConstants.ACCESS_TOKEN, accessToken);
      return this;
    }

//    public IntentBuilder placeOptions(PlaceOptions placeOptions) {
//      intent.putExtra(PlaceConstants.PLACE_OPTIONS, placeOptions);
//      return this;
//    }

    public Intent build(Activity activity) {
      intent.setClass(activity, PlacePickerActivity.class);
      return intent;
    }
  }
}
