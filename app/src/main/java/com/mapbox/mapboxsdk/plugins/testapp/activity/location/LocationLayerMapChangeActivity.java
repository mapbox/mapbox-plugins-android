package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationLayerMapChangeActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.map_view)
  MapView mapView;
  @BindView(R.id.fabStyles)
  FloatingActionButton stylesFab;

  private LocationLayerPlugin locationPlugin;
  private LocationEngine locationEngine;
  private MapboxMap mapboxMap;

  private boolean customStyle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_layer_map_change);
    ButterKnife.bind(this);

    mapView.setStyleUrl(Utils.getNextStyle());
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.setFastestInterval(1000);
    locationEngine.activate();
    locationPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
    locationPlugin.setRenderMode(RenderMode.COMPASS);
    getLifecycle().addObserver(locationPlugin);
  }

  @OnClick(R.id.fabStyles)
  public void onStyleFabClick() {
    if (mapboxMap != null) {
      mapboxMap.setStyleUrl(Utils.getNextStyle());
    }
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  protected void onStart() {
    super.onStart();
    mapView.onStart();
    if (locationEngine != null) {
      locationEngine.requestLocationUpdates();
    }
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
    if (locationEngine != null) {
      locationEngine.removeLocationUpdates();
    }
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
    if (locationEngine != null) {
      locationEngine.deactivate();
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}