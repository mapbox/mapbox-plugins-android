package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.OnLocationLayerClickListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationLayerModesActivity extends AppCompatActivity implements OnMapReadyCallback,
  LifecycleRegistryOwner, LocationEngineListener, OnLocationLayerClickListener {

  @BindView(R.id.mapView)
  MapView mapView;

  private LocationLayerPlugin locationLayerPlugin;
  private LifecycleRegistry lifecycleRegistry;
  private LocationEngine locationEngine;
  private MapboxMap mapboxMap;
  private boolean customStyle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_layer_mode);
    ButterKnife.bind(this);
    lifecycleRegistry = new LifecycleRegistry(this);

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @SuppressWarnings( {"MissingPermission"})
  @OnClick(R.id.button_location_mode_none)
  public void locationModeNone(View view) {
    if (locationLayerPlugin == null) {
      return;
    }
    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.NONE);
  }

  @SuppressWarnings( {"MissingPermission"})
  @OnClick(R.id.button_location_mode_compass)
  public void locationModeCompass(View view) {
    if (locationLayerPlugin == null) {
      return;
    }
    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.COMPASS);
  }

  @SuppressWarnings( {"MissingPermission"})
  @OnClick(R.id.button_location_mode_tracking)
  public void locationModeTracking(View view) {
    if (locationLayerPlugin == null) {
      return;
    }
    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
  }

  @SuppressWarnings( {"MissingPermission"})
  @OnClick(R.id.button_location_mode_navigation)
  public void locationModeNavigation(View view) {
    if (locationLayerPlugin == null) {
      return;
    }
    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.NAVIGATION);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    locationEngine = new LostLocationEngine(this);
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();
    locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
    locationLayerPlugin.setOnLocationClickListener(this);
    getLifecycle().addObserver(locationLayerPlugin);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_location, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (locationLayerPlugin == null) {
      return super.onOptionsItemSelected(item);
    }

    if (item.getItemId() == R.id.action_style_change) {
      customStyle = !customStyle;
      locationLayerPlugin.applyStyle(customStyle ? R.style.CustomLocationLayer : R.style.LocationLayer);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public LocationLayerPlugin getLocationLayerPlugin() {
    return locationLayerPlugin;
  }

  @Override
  public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  protected void onStart() {
    super.onStart();
    mapView.onStart();
    if (locationEngine != null) {
      locationEngine.requestLocationUpdates();
      locationEngine.addLocationEngineListener(this);
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
    mapView.onStop();
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
      locationEngine.removeLocationUpdates();
    }
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

  @Override
  @SuppressWarnings( {"MissingPermission"})
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
      new LatLng(location.getLatitude(), location.getLongitude()), 16));
  }

  @Override
  public void onLocationLayerClick() {
    Toast.makeText(this, "OnLocationLayerClick", Toast.LENGTH_LONG).show();
  }
}