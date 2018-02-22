package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.OnLocationLayerClickListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationLayerModesActivity extends AppCompatActivity implements OnMapReadyCallback,
  LocationEngineListener, OnLocationLayerClickListener {

  @BindView(R.id.map_view)
  MapView mapView;
  @BindView(R.id.tv_mode)
  TextView modeText;
  @BindView(R.id.tv_tracking)
  TextView trackingText;
  @BindView(R.id.button_location_mode)
  Button locationModeBtn;
  @BindView(R.id.button_location_tracking)
  Button locationTrackingBtn;

  private LocationLayerPlugin locationLayerPlugin;
  private LocationEngine locationEngine;
  private MapboxMap mapboxMap;
  private boolean customStyle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_layer_mode);
    ButterKnife.bind(this);

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @SuppressWarnings( {"MissingPermission"})
  @OnClick(R.id.button_location_mode)
  public void locationMode(View view) {
    if (locationLayerPlugin == null) {
      return;
    }
    showModeListDialog();
  }

  @OnClick(R.id.button_location_tracking)
  public void locationModeCompass(View view) {
    if (locationLayerPlugin == null) {
      return;
    }
    showTrackingListDialog();
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    locationEngine = new LostLocationEngine(this);
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();
    locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
    locationLayerPlugin.addOnLocationClickListener(this);
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
      toggleStyle();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @VisibleForTesting
  public void toggleStyle() {
    customStyle = !customStyle;
    locationLayerPlugin.applyStyle(customStyle ? R.style.CustomLocationLayer
      : R.style.mapbox_LocationLayer);
  }

  @VisibleForTesting
  public LocationLayerPlugin getLocationLayerPlugin() {
    return locationLayerPlugin;
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
    locationEngine.removeLocationEngineListener(this);
  }

  @Override
  public void onLocationLayerClick() {
    Toast.makeText(this, "OnLocationLayerClick", Toast.LENGTH_LONG).show();
  }

  private void showModeListDialog() {
    List<String> modes = new ArrayList<>();
    modes.add("Normal");
    modes.add("Compass");
    modes.add("GPS");
    ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(this,
      android.R.layout.simple_list_item_1, modes);
    ListPopupWindow listPopup = new ListPopupWindow(this);
    listPopup.setAdapter(profileAdapter);
    listPopup.setAnchorView(locationModeBtn);
    listPopup.setOnItemClickListener((parent, itemView, position, id) -> {
      String selectedMode = modes.get(position);
      locationModeBtn.setText(selectedMode);
      if (selectedMode.contentEquals("Normal")) {
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
      } else if (selectedMode.contentEquals("Compass")) {
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);
      } else if (selectedMode.contentEquals("GPS")) {
        locationLayerPlugin.setRenderMode(RenderMode.GPS);
      }
      listPopup.dismiss();
    });
    listPopup.show();
  }

  private void showTrackingListDialog() {
    List<String> trackingTypes = new ArrayList<>();
    trackingTypes.add("None");
    trackingTypes.add("Tracking");
    trackingTypes.add("Tracking Compass");
    trackingTypes.add("Tracking GPS");
    trackingTypes.add("Tracking GPS North");
    ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(this,
      android.R.layout.simple_list_item_1, trackingTypes);
    ListPopupWindow listPopup = new ListPopupWindow(this);
    listPopup.setAdapter(profileAdapter);
    listPopup.setAnchorView(locationTrackingBtn);
    listPopup.setOnItemClickListener((parent, itemView, position, id) -> {
      String selectedTrackingType = trackingTypes.get(position);
      locationTrackingBtn.setText(selectedTrackingType);
      if (selectedTrackingType.contentEquals("None")) {
        locationLayerPlugin.setCameraMode(CameraMode.NONE);
      } else if (selectedTrackingType.contentEquals("Tracking")) {
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
      } else if (selectedTrackingType.contentEquals("Tracking Compass")) {
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING_COMPASS);
      } else if (selectedTrackingType.contentEquals("Tracking GPS")) {
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING_GPS);
      } else if (selectedTrackingType.contentEquals("Tracking GPS North")) {
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
      }
      listPopup.dismiss();
    });
    listPopup.show();
  }
}