package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.plugins.places.autocomplete.model.PlaceOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AutocompleteLauncherActivity extends AppCompatActivity {

  private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

  @BindView(R.id.mapView)
  MapView mapView;

  private CarmenFeature home;
  private CarmenFeature work;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_launcher);
    ButterKnife.bind(this);
    addUserLocations();
  }

  private void addUserLocations() {
    home = CarmenFeature.builder().text("Directions to Home")
      .geometry(Point.fromLngLat(1.0, 2.0))
      .placeName("300 Massachusetts Ave NW")
      .id("directions-home")
      .properties(new JsonObject())
      .build();

    work = CarmenFeature.builder().text("Directions to Work")
      .placeName("1509 16th St NW")
      .geometry(Point.fromLngLat(1.0, 2.0))
      .id("directions-work")
      .properties(new JsonObject())
      .build();
  }

  @OnClick(R.id.fabCard)
  public void onOverlayFabClick(View view) {
    Intent intent = new PlaceAutocomplete.IntentBuilder()
      .accessToken(Mapbox.getAccessToken())
      .placeOptions(PlaceOptions.builder()
        .backgroundColor(Color.parseColor("#EEEEEE"))
        .addInjectedFeature(home)
        .addInjectedFeature(work)
        .limit(10)
        .build(PlaceOptions.MODE_CARDS))
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
    Intent intent = new PlaceAutocomplete.IntentBuilder()
      .accessToken(Mapbox.getAccessToken())
      .placeOptions(PlaceOptions.builder()
        .backgroundColor(Color.WHITE)
        .addInjectedFeature(home)
        .addInjectedFeature(work)
        .build())
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
