package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPlugin;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPluginBuilder;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnMarkerEventListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Activity showcasing GeoJson plugin integration
 */
public class GeoJsonActivity extends AppCompatActivity implements OnMapReadyCallback, FileChooserDialog.FileCallback,
  OnLoadingGeoJsonListener, OnMarkerEventListener {
  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.fabURL)
  FloatingActionButton urlFab;
  @BindView(R.id.fabAssets)
  FloatingActionButton assetsFab;

  @BindView(R.id.fabPath)
  FloatingActionButton pathFab;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  private MapboxMap mapboxMap;
  private GeoJsonPlugin geoJsonPlugin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geojson);
    ButterKnife.bind(this);
    progressBar.getIndeterminateDrawable().setColorFilter(Color.RED,
      android.graphics.PorterDuff.Mode.SRC_IN);
    mapView.setStyleUrl(Style.MAPBOX_STREETS);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    geoJsonPlugin = new GeoJsonPluginBuilder()
      .withContext(this)
      .withMap(mapboxMap)
      .withOnLoadingURL(this)
      .withOnLoadingFileAssets(this)
      .withOnLoadingFilePath(this)
      .withMarkerClickListener(this)
      .withRandomFillColor()
      .build();
    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.6546, 51.6680), 7));

  }

  @OnClick(R.id.fabURL)
  public void onURLFabClick() {
    if (mapboxMap != null && geoJsonPlugin != null) {
      mapboxMap.clear();
      geoJsonPlugin.setUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson");

    }
  }

  @OnClick(R.id.fabAssets)
  public void onAssetsFabClick() {
    if (mapboxMap != null && geoJsonPlugin != null) {
      mapboxMap.clear();
      geoJsonPlugin.setAssetsName("IRN.geo.json");
    }
  }

  @OnClick(R.id.fabPath)
  public void onPathFabClick() {
    if (Build.VERSION.SDK_INT >= 23) {
      if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {
        Timber.v("Permission is granted");
        showFileChooserDialog();
      } else {

        Timber.v("Permission is revoked");
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
      }
    } else { //permission is automatically granted on sdk<23 upon installation
      Timber.v("Permission is granted");
      showFileChooserDialog();
    }

  }

  private void showFileChooserDialog() {
    new FileChooserDialog.Builder(this)
      .extensionsFilter(".geojson", ".json", ".js", ".txt")
      .goUpLabel("Up")
      .show();
  }

  /**
   * draw GeoJson file from path. please locate some GeoJson file in your device for test it.
   *
   * @param file selected file from external storage
   */
  private void drawFromPath(File file) {
    String path = file.getAbsolutePath();
    if (mapboxMap != null && geoJsonPlugin != null) {
      mapboxMap.clear();
      geoJsonPlugin.setFilePath(path);
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

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Timber.v("Permission: " + permissions[0] + "was " + grantResults[0]);
      showFileChooserDialog();
    }
  }

  @Override
  public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
    drawFromPath(file);
  }

  @Override
  public void onFileChooserDismissed(@NonNull FileChooserDialog dialog) {

  }

  @Override
  public void onPreLoading() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void onLoaded() {
    Toast.makeText(GeoJsonActivity.this, "GeoJson data loaded", Toast.LENGTH_LONG).show();
    progressBar.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onLoadFailed(Exception e) {
    progressBar.setVisibility(View.INVISIBLE);
    Toast.makeText(GeoJsonActivity.this, "Error occur during load GeoJson data. see logcat", Toast.LENGTH_LONG).show();
    e.printStackTrace();
  }

  @Override
  public void onMarkerClickListener(Marker marker, JsonObject properties) {
    Toast.makeText(GeoJsonActivity.this, properties.get("title").getAsString(), Toast.LENGTH_SHORT).show();
  }
}