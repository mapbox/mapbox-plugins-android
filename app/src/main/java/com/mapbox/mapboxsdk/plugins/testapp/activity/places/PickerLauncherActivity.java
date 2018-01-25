package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.picker.PlacePicker;

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
    startActivityForResult(new PlacePicker.IntentBuilder().accessToken(Mapbox.getAccessToken()).build(this), REQUEST_CODE);
  }
}
