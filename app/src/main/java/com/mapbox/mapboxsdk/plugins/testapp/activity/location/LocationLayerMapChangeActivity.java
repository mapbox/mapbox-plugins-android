package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;

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
    locationEngine = LostLocationEngine.getLocationEngine(this);
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.activate();
    locationPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
    locationPlugin.setRenderMode(RenderMode.COMPASS);
  }

  @OnClick(R.id.fabStyles)
  public void onStyleFabClick() {
    if (mapboxMap != null) {
      mapboxMap.setStyleUrl(Utils.getNextStyle());
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
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
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_location, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (locationPlugin == null) {
      return super.onOptionsItemSelected(item);
    }

    if (item.getItemId() == R.id.action_style_change) {
      customStyle = !customStyle;
      locationPlugin.applyStyle(customStyle ? R.style.CustomLocationLayer : R.style.mapbox_LocationLayer);
      return true;
    }

    return super.onOptionsItemSelected(item);
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