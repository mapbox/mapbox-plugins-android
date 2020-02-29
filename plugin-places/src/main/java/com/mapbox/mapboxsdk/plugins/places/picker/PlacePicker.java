package com.mapbox.mapboxsdk.plugins.places.picker;

import android.app.Activity;
import android.content.Intent;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.ui.PlacePickerActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public final class PlacePicker {

  private PlacePicker() {
    // No instances
  }

  /**
   * Returns the {@link CarmenFeature} selected by the user.
   *
   * @param data the result Intent that was provided in
   *             {@link Activity#onActivityResult(int, int, Intent)}
   * @return the users selected {@link CarmenFeature}
   */
  @Nullable
  public static CarmenFeature getPlace(Intent data) {
    String json = data.getStringExtra(PlaceConstants.RETURNING_CARMEN_FEATURE);
    if (json == null) {
      return null;
    }
    return CarmenFeature.fromJson(json);
  }

  public static CameraPosition getLastCameraPosition(Intent data) {
    return data.getParcelableExtra(PlaceConstants.MAP_CAMERA_POSITION);
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

    public IntentBuilder placeOptions(PlacePickerOptions placeOptions) {
      intent.putExtra(PlaceConstants.PLACE_OPTIONS, placeOptions);
      return this;
    }

    public Intent build(Activity activity) {
      intent.setClass(activity, PlacePickerActivity.class);
      return intent;
    }
  }
}
