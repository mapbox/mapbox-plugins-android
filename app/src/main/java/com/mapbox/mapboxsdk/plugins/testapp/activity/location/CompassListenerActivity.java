package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.CompassListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CompassListenerActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.map_view)
  MapView mapView;

  private LocationLayerPlugin locationLayerPlugin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compass_listener);
    ButterKnife.bind(this);

    mapView = (MapView) findViewById(R.id.map_view);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    LocationEngine locationEngine = new LostLocationEngine(this);
    locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
    locationLayerPlugin.setLocationLayerEnabled(LocationLayerMode.COMPASS);
    locationLayerPlugin.addCompassListener(new CompassListener() {
      @Override
      public void onCompassChanged(float userHeading) {
        CameraPosition cameraPosition = new CameraPosition.Builder().bearing(userHeading).build();
        mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      }

      @Override
      public void onCompassAccuracyChange(int compassStatus) {
        Timber.v("Compass reading: %d", compassStatus);
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
    if (locationLayerPlugin != null) {
      locationLayerPlugin.onStart();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
    if (locationLayerPlugin != null) {
      locationLayerPlugin.onStop();
    }
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
    locationLayerPlugin.removeCompassListener(null);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}