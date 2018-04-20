package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;

import butterknife.OnClick;

public class LocalizationActivity extends AppCompatActivity implements OnMapReadyCallback {

  private MapView mapView;

  private LocalizationPlugin localizationPlugin;
  private MapboxMap mapboxMap;
  private boolean mapIsLocalized;

  private static final MapLocale[] LOCALES = new MapLocale[] {
    MapLocale.CANADA,
    MapLocale.GERMANY,
    MapLocale.CHINA,
    MapLocale.US,
    MapLocale.CANADA_FRENCH,
    MapLocale.ITALY,
    MapLocale.JAPAN,
    MapLocale.KOREA,
    MapLocale.FRANCE
  };

  private static int index;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_localization);

    mapIsLocalized = true;
    Toast.makeText(this, R.string.change_language_instruction, Toast.LENGTH_LONG).show();
    mapView = findViewById(R.id.map_view);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    localizationPlugin = new LocalizationPlugin(mapView, mapboxMap);
    localizationPlugin.matchMapLanguageWithDeviceDefault();
  }

  @OnClick(R.id.localize_fab)
  public void localizeToggleFab() {
    if (mapIsLocalized) {
      localizationPlugin.setMapLanguage(new MapLocale(MapLocale.FRENCH));
      Toast.makeText(this, R.string.map_not_localized, Toast.LENGTH_SHORT).show();
      mapIsLocalized = false;
    } else {
      localizationPlugin.matchMapLanguageWithDeviceDefault();
      Toast.makeText(this, R.string.map_localized, Toast.LENGTH_SHORT).show();
      mapIsLocalized = true;
    }
  }

  @OnClick(R.id.camera_localization)
  public void localizeCameraFab() {
    MapLocale locale = getNextMapLocale();
    localizationPlugin.setMapLanguage(locale);
    localizationPlugin.setCameraToLocaleCountry(locale);
  }

  @OnClick(R.id.change_map_style)
  public void changeMapStyleFab() {
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_languages, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.english:
        localizationPlugin.setMapLanguage(MapLocale.ENGLISH);
        return true;
      case R.id.spanish:
        localizationPlugin.setMapLanguage(MapLocale.SPANISH);
        return true;
      case R.id.french:
        localizationPlugin.setMapLanguage(MapLocale.FRENCH);
        return true;
      case R.id.german:
        localizationPlugin.setMapLanguage(MapLocale.GERMAN);
        return true;
      case R.id.russian:
        localizationPlugin.setMapLanguage(MapLocale.RUSSIAN);
        return true;
      case R.id.chinese:
        localizationPlugin.setMapLanguage(MapLocale.CHINESE);
        return true;
      case R.id.simplified_chinese:
        localizationPlugin.setMapLanguage(MapLocale.SIMPLIFIED_CHINESE);
        return true;
      case R.id.portuguese:
        localizationPlugin.setMapLanguage(MapLocale.PORTUGUESE);
        return true;
      case R.id.arabic:
        localizationPlugin.setMapLanguage(MapLocale.ARABIC);
        return true;
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public static MapLocale getNextMapLocale() {
    index++;
    if (index == LOCALES.length) {
      index = 0;
    }
    return LOCALES[index];
  }

  @VisibleForTesting
  public LocalizationPlugin getLocalizationPlugin() {
    return localizationPlugin;
  }
}

