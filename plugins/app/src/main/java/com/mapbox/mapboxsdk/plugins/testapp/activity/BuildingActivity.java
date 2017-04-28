package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.mapbox.androidsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.style.light.Light;
import com.mapbox.mapboxsdk.style.light.Position;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Activity showcasing building plugin integration
 */
public class BuildingActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.mapView)
  MapView mapView;

  @BindView(R.id.fabBuilding)
  View fab;

  @BindView(R.id.seekbarLightRadialCoordinate)
  SeekBar seekbarRadialCoordinate;

  @BindView(R.id.seekbarLightAzimuthalAngle)
  SeekBar seekbarAzimuthalAngle;

  @BindView(R.id.seekbarLightPolarAngle)
  SeekBar seekbarPolarAngle;

  private MapboxMap mapboxMap;
  private BuildingPlugin buildingPlugin;
  private boolean isEnabled;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_building);
    ButterKnife.bind(this);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    buildingPlugin = new BuildingPlugin(mapView, mapboxMap);
    buildingPlugin.setMinZoomLevel(15);
    fab.setVisibility(View.VISIBLE);
    initLightSeekbar();
  }

  private void initLightSeekbar() {
    seekbarRadialCoordinate.setMax(24); // unknown?
    seekbarAzimuthalAngle.setMax(180); // unknown?
    seekbarPolarAngle.setMax(180); // polar angle ranges from 0 to 180 degrees

    PositionChangeListener positionChangeListener = new PositionChangeListener();
    seekbarRadialCoordinate.setOnSeekBarChangeListener(positionChangeListener);
    seekbarAzimuthalAngle.setOnSeekBarChangeListener(positionChangeListener);
    seekbarPolarAngle.setOnSeekBarChangeListener(positionChangeListener);
  }

  @OnClick(R.id.fabBuilding)
  public void onBuildingFabClicked() {
    if (mapboxMap != null) {
      isEnabled = !isEnabled;
      buildingPlugin.setVisibility(isEnabled);
      Timber.e("Building plugin is enabled :%s", isEnabled);
    }
  }

  private class PositionChangeListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      invalidateLightPosition();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
      // Only listening to positionChange for onProgress.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      // Only listening to positionChange for onProgress.
    }
  }

  private void invalidateLightPosition() {
    Light light = mapboxMap.getLight();
    float radialCoordinate = (float) seekbarRadialCoordinate.getProgress() / 20;
    float azimuthalAngle = seekbarAzimuthalAngle.getProgress();
    float polarAngle = seekbarPolarAngle.getProgress();
    light.setPosition(new Position(radialCoordinate, azimuthalAngle, polarAngle));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_building_min_zoom:
        buildingPlugin.setMinZoomLevel(14);
        return true;
      case R.id.menu_building_opacity:
        buildingPlugin.setOpacity(1.0f);
        return true;
      case R.id.menu_building_color:
        buildingPlugin.setColor(Color.RED);
        return true;
      case R.id.menu_building_style:
        mapboxMap.setStyle(Style.DARK);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_building, menu);
    return super.onCreateOptionsMenu(menu);
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
