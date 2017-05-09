package com.mapbox.mapboxsdk.plugins.testapp.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.widgets.MyLocationViewSettings;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.MyLocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.MyLocationLayerOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BasicLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.mapView)
  MapView mapView;

  private MyLocationLayerPlugin locationLayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic_location);
    ButterKnife.bind(this);

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {

    locationLayer = new MyLocationLayerPlugin(mapView, mapboxMap);
    locationLayer.setMyLocationEnabled(true);
    locationLayer.setMyBearingEnabled(true);

    MyLocationLayerOptions locationSettings = locationLayer.getMyLocationLayerOptions();
    locationSettings.setAccuracyAlpha(0.20f);

    mapboxMap.moveCamera(CameraUpdateFactory.zoomBy(12));
    mapboxMap.setMyLocationEnabled(true);
    mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
    mapboxMap.getTrackingSettings().setDismissAllTrackingOnGesture(false);

    MyLocationViewSettings settings = mapboxMap.getMyLocationViewSettings();
    settings.setAccuracyAlpha(0);

  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    // TODO this shouldn't be required
    if (locationLayer != null) {
      locationLayer.onStart();
    }
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    locationLayer.onStop();
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