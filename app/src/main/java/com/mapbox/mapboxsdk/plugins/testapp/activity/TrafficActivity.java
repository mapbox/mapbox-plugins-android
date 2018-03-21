package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Activity showcasing TrafficPlugin plugin integration
 */
public class TrafficActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.map_view)
  MapView mapView;

  @BindView(R.id.fabStyles)
  FloatingActionButton stylesFab;

  @BindView(R.id.fabTraffic)
  FloatingActionButton trafficFab;

  private MapboxMap mapboxMap;
  private TrafficPlugin trafficPlugin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_traffic);
    ButterKnife.bind(this);

    mapView.setStyleUrl(Utils.getNextStyle());
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    this.trafficPlugin = new TrafficPlugin(mapView, mapboxMap);
    this.trafficPlugin.setVisibility(true); // Enable the traffic view by default
  }

  @OnClick(R.id.fabTraffic)
  public void onTrafficFabClick() {
    if (mapboxMap != null) {
      trafficPlugin.setVisibility(!trafficPlugin.isVisible());
      Timber.d("Traffic plugin is enabled : %s", trafficPlugin.isVisible());
    }
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

  public TrafficPlugin getTrafficPlugin() {
    return trafficPlugin;
  }
}