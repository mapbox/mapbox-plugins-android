package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.autocomplete.PlaceAutocomplete;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutocompleteLauncherActivity extends AppCompatActivity {

  private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_launcher);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.fabOverlay)
  public void onOverlayFabClick(View view) {
    Intent intent = new PlaceAutocomplete.IntentBuilder()
      .limit(10)
      .build(AutocompleteLauncherActivity.this);
    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
  }
}
