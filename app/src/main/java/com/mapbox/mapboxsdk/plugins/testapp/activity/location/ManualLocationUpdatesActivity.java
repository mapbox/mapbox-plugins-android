package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ManualLocationUpdatesActivity extends AppCompatActivity implements OnMapReadyCallback,
  LocationEngineListener, LifecycleRegistryOwner {

  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.fabToggleManualLocation)
  FloatingActionButton fabToggleManualLocation;

  private LocationLayerPlugin locationLayerPlugin;
  private LifecycleRegistry lifecycleRegistry;
  private LocationEngine locationEngine;
  private MapboxMap mapboxMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_manual_update);
    ButterKnife.bind(this);
    lifecycleRegistry = new LifecycleRegistry(this);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @OnClick(R.id.fabToggleManualLocation)
  public void toggleManualLocationFabClick(View view) {
    toggleManualLocation();
  }

  @OnClick(R.id.fabManualLocationChange)
  public void manualLocationChangeFabClick(View view) {
    if (locationLayerPlugin != null) {
      locationLayerPlugin.forceLocationUpdate(
        Utils.getRandomLocation(new double[] {-77.1825, 38.7825, -76.9790, 39.0157}));
    }
  }

  private void toggleManualLocation() {
    locationLayerPlugin.setLocationEngine(locationLayerPlugin.getLocationEngine() == null ? locationEngine : null);
    fabToggleManualLocation.setImageResource(locationLayerPlugin.getLocationEngine() == null
      ? R.drawable.ic_location : R.drawable.ic_location_disabled);
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    locationEngine = new LostLocationEngine(this);
    locationEngine.addLocationEngineListener(this);
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.activate();
    locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, null);
    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
    getLifecycle().addObserver(locationLayerPlugin);
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
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    Timber.d(String.format(Locale.US, "Location change occurred: %s", location.toString()));
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