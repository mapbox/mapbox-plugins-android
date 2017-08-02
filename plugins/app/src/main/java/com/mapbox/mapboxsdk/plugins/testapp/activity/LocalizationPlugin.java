package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * The localization plugin enables automatic localization of map labels into the user’s preferred language
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)} and provide
 * a valid instance of {@link MapView} and {@link MapboxMap}.
 * </p>
 * <ul>
 * </ul>
 */
public final class LocalizationPlugin {

  private MapView mapView;
  private MapboxMap mapboxMap;
  private boolean localizationEnabled;
  private String deviceLanguage;


  /**
   * Create a localization plugin.
   *
   * @param mapView   the MapView to apply the localization plugin to
   * @param mapboxMap the MapboxMap to apply localization plugin with
   * @since 0.1.0
   */
  public LocalizationPlugin(@NonNull MapView mapView, @NonNull final MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;

    // TODO: I don't think getMapAsync() is the right method to call on mapView below...
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {

      }
    });
  }

  /**
   * Initialises and adds the localized language by this plugin.
   *
   * @param belowLayer optionally place the buildings layer below a provided layer id
   */
  private void initLayer(String belowLayer) {
    get

  }

  /**
   * Toggles the localization of the map's layers that have text.
   *
   * @param localized true for visible, false for none
   * @since 0.1.0
   */
  public void setLocalization(boolean localiztionEnabled) {
    LocalizationPlugin.this.localizationEnabled = localiztionEnabled;
  }

}