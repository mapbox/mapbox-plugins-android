package com.mapbox.plugins.places.autocomplete;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.places.R;

public class PlacesCompleteActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_complete);

    Intent intent = getIntent();
    int mode = intent.getIntExtra("mode", PlaceAutocomplete.THEME_FULLSCREEN);
    if (mode == PlaceAutocomplete.THEME_OVERLAY) {
    } else {
      getWindow().getDecorView().setBackgroundColor(Color.parseColor("#263D57"));
    }













  }
}
