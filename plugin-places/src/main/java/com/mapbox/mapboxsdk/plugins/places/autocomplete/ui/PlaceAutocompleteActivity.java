package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.places.R;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;

public class PlaceAutocompleteActivity extends AppCompatActivity implements PlaceSelectionListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mapbox_activity_autocomplete);

    // Add autocomplete fragment if this is first creation
    if (savedInstanceState == null) {
      String accessToken = getIntent().getStringExtra(PlaceConstants.ACCESS_TOKEN);

      PlaceAutocompleteFragment fragment;

      PlaceOptions placeOptions = getIntent().getParcelableExtra(PlaceConstants.PLACE_OPTIONS);
      if (placeOptions != null) {
        fragment = PlaceAutocompleteFragment.newInstance(accessToken, placeOptions);
      } else {
        fragment = PlaceAutocompleteFragment.newInstance(accessToken);
      }
      getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment, PlaceAutocompleteFragment.TAG).commit();

      fragment.setOnPlaceSelectedListener(this);
    }
  }

  @Override
  public void onPlaceSelected(CarmenFeature carmenFeature) {
    String json = carmenFeature.toJson();
    Intent returningIntent = new Intent();
    returningIntent.putExtra(PlaceConstants.RETURNING_CARMEN_FEATURE, json);
    setResult(AppCompatActivity.RESULT_OK, returningIntent);
    finish();
  }

  @Override
  public void onCancel() {
    setResult(AppCompatActivity.RESULT_CANCELED);
    finish();
  }
}