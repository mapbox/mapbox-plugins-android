package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.picker.PlacePicker;
import com.mapbox.plugins.places.picker.model.PlacePickerOptions;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickerLauncherActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 5678;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picker_launcher);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.launch_location_picker)
  public void onClick(FloatingActionButton view) {
    startActivityForResult(
      new PlacePicker.IntentBuilder()
        .accessToken(Mapbox.getAccessToken())
        .placeOptions(PlacePickerOptions.builder()
          .statingCameraPosition(new CameraPosition.Builder()
            .target(new LatLng(40.7544, -73.9862)).zoom(16).build())
          .build())
        .build(this), REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      CarmenFeature carmenFeature = PlacePicker.getPlace(data);
      String toastMsg = String.format("Place: %s", carmenFeature.placeName());
      Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }
  }
}
