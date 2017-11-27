package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.autocomplete.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AutocompleteLauncherActivity extends AppCompatActivity {

  private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

  @BindView(R.id.mapView)
  MapView mapView;

  private List<CarmenFeature> carmenFeatures;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_launcher);
    ButterKnife.bind(this);
    carmenFeatures = new ArrayList<>();
    addUserLocations();
  }

  private void addUserLocations() {
    carmenFeatures.add(CarmenFeature.builder().text("Directions to Home")
      .address("300 Massachusetts Ave NW")
      .properties(new JsonObject())
      .build());

    carmenFeatures.add(CarmenFeature.builder().text("Directions to Work")
      .address("1509 16th St NW")
      .properties(new JsonObject())
      .build());
  }

  @OnClick(R.id.fabCard)
  public void onOverlayFabClick(View view) {
    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_CARDS)
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .limit(10)
      .backgroundColor(Color.parseColor("#EEEEEE"))
      .injectPlaces(carmenFeatures)
      .build(AutocompleteLauncherActivity.this);
    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
  }

  @OnLongClick(R.id.fabCard)
  public boolean onOverlayFabLongClick(View view) {
    PlaceAutocomplete.clearRecentHistory(this);
    Toast.makeText(this, "database cleared", Toast.LENGTH_LONG).show();
    return true;
  }

  @OnClick(R.id.fabFullScreen)
  public void onFullScreenFabClick(View view) {
    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
      .limit(2)
      .backgroundColor(Color.WHITE)
      .build(AutocompleteLauncherActivity.this);
    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
      CarmenFeature feature = PlaceAutocomplete.getPlace(data);
      Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
    }
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
