package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.MyLocationLayerMode;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.MyLocationLayerOptions;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.MyLocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyLocationModesActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.button_location_mode_none)
  Button locationModeNoneButton;
  @BindView(R.id.button_location_mode_compass)
  Button locationModeCompassButton;
  @BindView(R.id.button_location_mode_tracking)
  Button locationModeTrackingButton;
  @BindView(R.id.button_location_mode_navigation)
  Button locationModeNavigationButton;

  private MyLocationLayerOptions options;
  private MyLocationLayerPlugin locationPlugin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_location_mode);
    ButterKnife.bind(this);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @OnClick(R.id.button_location_mode_none)
  public void locationModeNone(View view) {
    if (locationPlugin == null) {
      return;
    }
    locationPlugin.setMyLocationEnabled(MyLocationLayerMode.NONE);
  }

  @OnClick(R.id.button_location_mode_compass)
  public void locationModeCompass(View view) {
    if (locationPlugin == null) {
      return;
    }
    locationPlugin.setMyLocationEnabled(MyLocationLayerMode.COMPASS);
  }

  @OnClick(R.id.button_location_mode_tracking)
  public void locationModeTracking(View view) {
    if (locationPlugin == null) {
      return;
    }
    locationPlugin.setMyLocationEnabled(MyLocationLayerMode.TRACKING);
    options.setLocationTextAnnotation("1509 16th St NW");
  }

  @OnClick(R.id.button_location_mode_navigation)
  public void locationModeNavigation(View view) {
    if (locationPlugin == null) {
      return;
    }
    locationPlugin.setMyLocationEnabled(MyLocationLayerMode.NAVIGATION);
    options.setNavigationTextAnnotation("16th St NW");
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    locationPlugin = new MyLocationLayerPlugin(mapView, mapboxMap);
    options = locationPlugin.getMyLocationLayerOptions();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (locationPlugin != null) {
      locationPlugin.onStart();
    }
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    locationPlugin.onStop();
    mapView.onStop();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}