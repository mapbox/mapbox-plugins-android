package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.markerlayer.Marker;
import com.mapbox.mapboxsdk.plugins.markerlayer.MarkerPlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkerLayerActivity extends AppCompatActivity implements OnMapReadyCallback {

  @BindView(R.id.mapView)
  MapView mapView;

  @BindView(R.id.fabStyles)
  FloatingActionButton stylesFab;

  @BindView(R.id.fabTraffic)
  FloatingActionButton trafficFab;

  private MapboxMap mapboxMap;

  private static final LatLng[] LAT_LNGS = new LatLng[] {
    new LatLng(38.897424, -77.036508),
    new LatLng(38.909698, -77.029642),
    new LatLng(38.907227, -77.036530),
    new LatLng(38.905607, -77.031916),
    new LatLng(38.889441, -77.050134),
    new LatLng(38.888000, -77.050000),
    new LatLng(38.895918, -77.038188),
    new LatLng(38.888033, -77.0500023)
  };

  private static final String[] SYMBOL_IDS = new String[] {
    "airport",
    "restaurant",
    "school",
    "stadium",
    "taxi"
  };

  private final Random random = new Random();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_marker_layer);
    ButterKnife.bind(this);

    mapView.setStyleUrl(Utils.getNextStyle());
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    addIcons();
    addRandomMarkers();
  }

  private void addIcons() {
    mapboxMap.addImage(SYMBOL_IDS[0], BitmapFactory.decodeResource(getResources(), R.drawable.airport));
    mapboxMap.addImage(SYMBOL_IDS[1], BitmapFactory.decodeResource(getResources(), R.drawable.restaurant));
    mapboxMap.addImage(SYMBOL_IDS[2], BitmapFactory.decodeResource(getResources(), R.drawable.school));
    mapboxMap.addImage(SYMBOL_IDS[3], BitmapFactory.decodeResource(getResources(), R.drawable.stadium));
    mapboxMap.addImage(SYMBOL_IDS[4], BitmapFactory.decodeResource(getResources(), R.drawable.taxi));
  }

  private void addRandomMarkers() {
    MarkerPlugin markerPlugin = new MarkerPlugin(mapboxMap);
    for (int i = 0; i < LAT_LNGS.length; i++) {
      Marker marker = new Marker();
      marker.setPosition(LAT_LNGS[i]);
      marker.setIconId(SYMBOL_IDS[random.nextInt(5)]);
      marker.setIconOpacity(i != 0 ? 1 : 0.5f);
      marker.setIconRotate(i * 17);
      marker.setIconSize(i != 0 ? 1 : 3f);
      markerPlugin.addMarker(marker);
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
}
