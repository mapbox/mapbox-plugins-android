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
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerTracking;
import com.mapbox.mapboxsdk.plugins.locationlayer.OnLocationLayerClickListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationLayerModesActivity extends AppCompatActivity implements OnMapReadyCallback,
  LocationEngineListener, OnLocationLayerClickListener {

  public static final String NONE_TRACKING = "None tracking";
  public static final String NONE_TRACKING_SHOW_COMPASS_BEARING = "None tracking, show compass bearing";
  public static final String NONE_TRACKING_SHOW_GPS_BEARING = "None tracking, show GPS bearing";
  public static final String TRACKING = "Tracking";
  public static final String TRACKING_WITH_COMPASS_BEARING = "Tracking with compass bearing";
  public static final String TRACKING_WITH_GPS_BEARING = "Tracking with GPS bearing";
  public static final String TRACKING_WITH_BEARING_FACING_NORTH = "Tracking with bearing facing north";
  @BindView(R.id.map_view)
  MapView mapView;
  @BindView(R.id.tv_tracking)
  TextView trackingText;
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
    locationLayerPlugin.setOnLocationClickListener(this);
    locationLayerPlugin.setLocationLayerEnabled(true);
    locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.NONE);
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
    locationLayerPlugin.applyStyle(customStyle ? R.style.CustomLocationLayer : R.style.LocationLayer);
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

  private void showTrackingListDialog() {
    List<String> trackingTypes = new ArrayList<>();
    trackingTypes.add(NONE_TRACKING);
    trackingTypes.add(NONE_TRACKING_SHOW_COMPASS_BEARING);
    trackingTypes.add(NONE_TRACKING_SHOW_GPS_BEARING);
    trackingTypes.add(TRACKING);
    trackingTypes.add(TRACKING_WITH_COMPASS_BEARING);
    trackingTypes.add(TRACKING_WITH_GPS_BEARING);
    trackingTypes.add(TRACKING_WITH_BEARING_FACING_NORTH);
    ArrayAdapter<String> profileAdapter = new ArrayAdapter<>(this,
      android.R.layout.simple_list_item_1, trackingTypes);
    ListPopupWindow listPopup = new ListPopupWindow(this);
    listPopup.setAdapter(profileAdapter);
    listPopup.setAnchorView(locationTrackingBtn);
    listPopup.setOnItemClickListener((parent, itemView, position, id) -> {
      String selectedTrackingType = trackingTypes.get(position);
      locationTrackingBtn.setText(selectedTrackingType);
      if (selectedTrackingType.contentEquals(NONE_TRACKING)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.NONE);
      } else if (selectedTrackingType.contentEquals(NONE_TRACKING_SHOW_COMPASS_BEARING)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.NONE_COMPASS);
      } else if (selectedTrackingType.contentEquals(NONE_TRACKING_SHOW_GPS_BEARING)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.NONE_GPS);
      } else if (selectedTrackingType.contentEquals(TRACKING)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.TRACKING);
      } else if (selectedTrackingType.contentEquals(TRACKING_WITH_COMPASS_BEARING)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.TRACKING_COMPASS);
      } else if (selectedTrackingType.contentEquals(TRACKING_WITH_GPS_BEARING)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.TRACKING_GPS);
      } else if (selectedTrackingType.contentEquals(TRACKING_WITH_BEARING_FACING_NORTH)) {
        locationLayerPlugin.setLocationLayerTracking(LocationLayerTracking.TRACKING_GPS_NORTH);
      }
      listPopup.dismiss();
    });
    listPopup.show();
  }
}