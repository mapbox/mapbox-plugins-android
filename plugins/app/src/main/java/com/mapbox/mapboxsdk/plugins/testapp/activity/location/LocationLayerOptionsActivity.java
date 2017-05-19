package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.LocationLayerOptions;
import com.mapbox.mapboxsdk.plugins.mylocationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationLayerOptionsActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.button_location_option_tint)
  Button locationOptionTintButton;
  @BindView(R.id.button_location_option_accuracy_tint)
  Button locationOptionAccuracyTint;
  @BindView(R.id.button_location_option_background_tint)
  Button locationOptionBackgroundTint;
  @BindView(R.id.button_location_option_compass)
  Button locationOptionCompassButton;

  private LocationLayerOptions options;
  private LocationLayerPlugin locationPlugin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_location_options);
    ButterKnife.bind(this);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @OnClick(R.id.button_location_option_tint)
  public void locationOptionTint(View view) {
    if (locationPlugin == null) {
      return;
    }
    options.setForegroundTintColor(
      options.getForegroundTintColor() == Color.GREEN ? ContextCompat.getColor(this, R.color.mapbox_plugin_location_layer_blue)
        : Color.GREEN
    );
  }

  @OnClick(R.id.button_location_option_accuracy_tint)
  public void locationOptionAccuracyTint(View view) {
    if (locationPlugin == null) {
      return;
    }
    options.setAccuracyTintColor(
      options.getAccuracyTintColor() == Color.BLUE ? ContextCompat.getColor(this, R.color.mapbox_plugin_location_layer_blue)
        : Color.BLUE
    );
    options.setAccuracyAlpha(options.getAccuracyAlpha() == 1f ? 0.15f : 1f);
  }

  @OnClick(R.id.button_location_option_background_tint)
  public void locationOptionBackgroundTint(View view) {
    if (locationPlugin == null) {
      return;
    }
    options.setBackgroundTintColor(options.getBackgroundTintColor() == Color.CYAN ? Color.WHITE : Color.CYAN);
  }

  @OnClick(R.id.button_location_option_compass)
  public void locationOptionCompass(View view) {
    if (locationPlugin == null) {
      return;
    }
    locationPlugin.setMyLocationEnabled(
      locationPlugin.getMyLocationMode() == LocationLayerMode.COMPASS ? LocationLayerMode.TRACKING
        : LocationLayerMode.COMPASS
    );
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    locationPlugin = new LocationLayerPlugin(mapView, mapboxMap);
    locationPlugin.setMyLocationEnabled(LocationLayerMode.TRACKING);
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