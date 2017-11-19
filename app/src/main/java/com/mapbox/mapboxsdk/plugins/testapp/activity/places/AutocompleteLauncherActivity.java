package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.autocomplete.PlaceAutocomplete;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutocompleteLauncherActivity extends AppCompatActivity {

  private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

  @BindView(R.id.mapView)
  MapView mapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_launcher);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.fabCard)
  public void onOverlayFabClick(View view) {
    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_CARDS)
      .limit(10)
      .backgroundColor(Color.parseColor("#EEEEEE"))
      .build(AutocompleteLauncherActivity.this);
    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
  }

  @OnClick(R.id.fabFullScreen)
  public void onFullScreenFabClick(View view) {
    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
      .limit(2)
      .backgroundColor(Color.parseColor("#757575"))
      .build(AutocompleteLauncherActivity.this);
    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
