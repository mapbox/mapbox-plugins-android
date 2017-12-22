package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
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
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ManualLocationUpdatesActivity extends AppCompatActivity implements OnMapReadyCallback,
  LocationEngineListener {

  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.fabToggleManualLocation)
  FloatingActionButton fabToggleManualLocation;

  private LocationLayerPlugin locationLayerPlugin;
  private LocationEngine locationEngine;
  private MapboxMap mapboxMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_manual_update);
    ButterKnife.bind(this);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @OnClick(R.id.fabToggleManualLocation)
  public void toggleManualLocationFabClick(View view) {
    if (locationLayerPlugin != null) {
      locationLayerPlugin.setLocationEngine(locationLayerPlugin.getLocationEngine() == null ? locationEngine : null);
      fabToggleManualLocation.setImageResource(locationLayerPlugin.getLocationEngine() == null
        ? R.drawable.ic_location : R.drawable.ic_location_disabled);
    }
  }

  @OnClick(R.id.fabManualLocationChange)
  public void manualLocationChangeFabClick(View view) {
    if (locationLayerPlugin != null) {
      locationLayerPlugin.forceLocationUpdate(
        Utils.getRandomLocation(new double[] {-77.1825, 38.7825, -76.9790, 39.0157}));
    }
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

  @VisibleForTesting
  public LocationLayerPlugin getLocationLayerPlugin() {
    return locationLayerPlugin;
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    Timber.d("Location change occurred: %s", location.toString());
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