package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
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

    PlaceAutocompleteFragment autocompleteFragment;

    if (savedInstanceState == null) {

      PlaceOptions placeOptions = PlaceOptions.builder()
        .toolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .hint("Begin searching...")
        .build();

      autocompleteFragment = PlaceAutocompleteFragment.newInstance(
        Mapbox.getAccessToken(), placeOptions);

      final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.add(R.id.fragment_container, autocompleteFragment, PlaceAutocompleteFragment.TAG);
      transaction.commit();
    } else {
      autocompleteFragment = (PlaceAutocompleteFragment)
        getSupportFragmentManager().findFragmentByTag(PlaceAutocompleteFragment.TAG);
    }

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
