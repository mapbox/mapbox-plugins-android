package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.RectF;
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

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerOptions;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.OnLocationLayerClickListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mapbox.mapboxsdk.style.expressions.Expression.has;

public class LocationLayerModesActivity extends AppCompatActivity implements OnMapReadyCallback,
  LocationEngineListener, OnLocationLayerClickListener, OnCameraTrackingChangedListener {

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
  private boolean initialLocationUpdate = true;

  private static final String SAVED_STATE_CAMERA = "saved_state_camera";
  private static final String SAVED_STATE_RENDER = "saved_state_render";

  @CameraMode.Mode
  private int cameraMode = CameraMode.NONE;

  @RenderMode.Mode
  private int renderMode = RenderMode.NORMAL;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_layer_mode);
    ButterKnife.bind(this);

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    if (savedInstanceState != null) {
      cameraMode = savedInstanceState.getInt(SAVED_STATE_CAMERA);
      renderMode = savedInstanceState.getInt(SAVED_STATE_RENDER);
    }
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

    locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
    locationEngine.setFastestInterval(1000);
    locationEngine.setSmallestDisplacement(0f);
    locationEngine.setInterval(0);
    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();

    int[] padding;
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      padding = new int[] {0, 750, 0, 0};
    } else {
      padding = new int[] {0, 250, 0, 0};
    }
    LocationLayerOptions options = LocationLayerOptions.builder(this)
      .padding(padding)
      .build();
    locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine, options);
    locationLayerPlugin.addOnLocationClickListener(this);
    locationLayerPlugin.addOnCameraTrackingChangedListener(this);
    locationLayerPlugin.setCameraMode(cameraMode);
    setRendererMode(renderMode);

    getLifecycle().addObserver(locationLayerPlugin);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_location, menu);
    return true;
  }

  @SuppressLint("MissingPermission")
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (locationLayerPlugin == null) {
      return super.onOptionsItemSelected(item);
    }

    if (item.getItemId() == R.id.action_style_change) {
      toggleStyle();
      return true;
    } else if (item.getItemId() == R.id.action_plugin_disable) {
      locationLayerPlugin.setLocationLayerEnabled(false);
      return true;
    } else if (item.getItemId() == R.id.action_plugin_enabled) {
      locationLayerPlugin.setLocationLayerEnabled(true);
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
    outState.putInt(SAVED_STATE_CAMERA, cameraMode);
    outState.putInt(SAVED_STATE_RENDER, renderMode);
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
    if (initialLocationUpdate) {
      mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
        new LatLng(location.getLatitude(), location.getLongitude()), 16));
      initialLocationUpdate = false;
    }

    PointF pointF = mapboxMap.getProjection().toScreenLocation(
      new LatLng(location.getLatitude(), location.getLongitude()));

    // Add padding to the point
    RectF rectF = new RectF(pointF.x + 50, pointF.y + 50, pointF.x + 50, pointF.y + 50);

    List<Feature> features = mapboxMap.queryRenderedFeatures(rectF, has("name"));
    if (!features.isEmpty()) {
      locationLayerPlugin.setPlaceText(features.get(0).getStringProperty("name"));
    }
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
        setRendererMode(RenderMode.NORMAL);
      } else if (selectedMode.contentEquals("Compass")) {
        setRendererMode(RenderMode.COMPASS);
      } else if (selectedMode.contentEquals("GPS")) {
        setRendererMode(RenderMode.GPS);
      }
      listPopup.dismiss();
    });
    listPopup.show();
  }

  private void setRendererMode(@RenderMode.Mode int mode) {
    renderMode = mode;
    locationLayerPlugin.setRenderMode(mode);
    if (mode == RenderMode.NORMAL) {
      locationModeBtn.setText("Normal");
    } else if (mode == RenderMode.COMPASS) {
      locationModeBtn.setText("Compass");
    } else if (mode == RenderMode.GPS) {
      locationModeBtn.setText("Gps");
    }
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

  @Override
  public void onCameraTrackingDismissed() {
    locationTrackingBtn.setText("None");
  }

  @Override
  public void onCameraTrackingChanged(int currentMode) {
    this.cameraMode = currentMode;

    if (cameraMode == CameraMode.NONE) {
      locationTrackingBtn.setText("None");
    } else if (cameraMode == CameraMode.TRACKING) {
      locationTrackingBtn.setText("Tracking");
    } else if (cameraMode == CameraMode.TRACKING_COMPASS) {
      locationTrackingBtn.setText("Tracking Compass");
    } else if (cameraMode == CameraMode.TRACKING_GPS) {
      locationTrackingBtn.setText("Tracking GPS");
    } else if (cameraMode == CameraMode.TRACKING_GPS_NORTH) {
      locationTrackingBtn.setText("Tracking GPS North");
    }
  }
}