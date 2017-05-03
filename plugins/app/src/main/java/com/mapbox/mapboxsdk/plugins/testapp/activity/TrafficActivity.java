package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Activity showcasing TrafficPlugin plugin integration
 */
public class TrafficActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.mapView)
  MapView mapView;

  @BindView(R.id.fabStyles)
  FloatingActionButton stylesFab;

  @BindView(R.id.fabTraffic)
  FloatingActionButton trafficFab;

  private MapboxMap mapboxMap;
  private TrafficPlugin trafficPlugin;
  private StyleCycle styleCycle = new StyleCycle();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_traffic);
    ButterKnife.bind(this);

    mapView.setStyleUrl(styleCycle.getStyle());
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    this.trafficPlugin = new TrafficPlugin(mapView, mapboxMap);
    this.trafficPlugin.toggle(); // Enable the traffic view by default
  }

  @OnClick(R.id.fabTraffic)
  public void onTrafficFabClick() {
    if (mapboxMap != null) {
      trafficPlugin.toggle();
      Timber.e("Traffic plugin is enabled :%s", trafficPlugin.isEnabled());
    }
  }

  @OnClick(R.id.fabStyles)
  public void onStyleFabClick() {
    if (mapboxMap != null) {
      mapboxMap.setStyleUrl(styleCycle.getNextStyle());
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

  private static class StyleCycle {
    private static final String[] STYLES = new String[] {
      Style.MAPBOX_STREETS,
      Style.OUTDOORS,
      Style.LIGHT,
      Style.DARK,
      Style.SATELLITE_STREETS
    };

    private int index;

    private String getNextStyle() {
      index++;
      if (index == STYLES.length) {
        index = 0;
      }
      return getStyle();
    }

    private String getStyle() {
      return STYLES[index];
    }
  }
}