package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.plugins.places.autocomplete.ui.PlaceSelectionListener;

import butterknife.ButterKnife;

public class AutocompleteFragmentActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_fragment);
    ButterKnife.bind(this);

    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
      getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

    autocompleteFragment.setPlaceOptions(new PlaceOptions()
      .withBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    );

    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(CarmenFeature carmenFeature) {
        Toast.makeText(AutocompleteFragmentActivity.this,
          carmenFeature.text(), Toast.LENGTH_LONG).show();
        finish();
      }

      @Override
      public void onCancel() {
        finish();
      }
    });
  }
}
