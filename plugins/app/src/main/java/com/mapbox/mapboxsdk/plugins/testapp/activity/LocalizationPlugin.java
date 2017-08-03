package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Layer;

import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

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
  private String TAG = "LocalizationPlugin";
  private List<Layer> listOfMapLayers;

  /**
   * Create a localization plugin.
   *
   * @param mapView   the MapView to apply the localization plugin to
   * @param mapboxMap the MapboxMap to apply localization plugin with
   * @since 0.1.0
   */
  public LocalizationPlugin(@NonNull MapView mapView, @NonNull final MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    this.localizationEnabled = true;

    // TODO: Is getMapAsync() the right method to call on mapView below?
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        if (localizationEnabled) {
          listOfMapLayers = mapboxMap.getLayers();
          String deviceLanguage = Locale.getDefault().getLanguage();
          for (Layer layer : listOfMapLayers) {
            if (layer.getId().contains("")) {
              layer.setProperties(textField(String.format("{name_%s}", deviceLanguage)));
              Log.d("LocalizationPlugin", "LocalizationPlugin: for textField = " + String.format("{name_%s}", deviceLanguage));
            }
          }
        }
      }
    });
  }

  /**
   * Toggles the localization of the map's layers that have text.
   *
   * @param localizationEnabled true for text being localized, false for none
   * @since 0.1.0
   */
  public void setLocalization(boolean localizationEnabled) {
    Log.d(TAG, "setLocalization: About to run this");
    LocalizationPlugin.this.localizationEnabled = localizationEnabled;
  }

  /**
   * Resets map text to English language (default)
   * <p>
   * Full list of language codes supported by Android system: https://stackoverflow.com/a/30028371/6358488
   *
   * @param languageToSetMapTo The language that you'd like to set the map text to.
   * @since 0.1.0
   */
  public void setMapLanguageTo(String languageToSetMapTo) {
    for (Layer layer : listOfMapLayers) {
      layer.setProperties(textField(String.format("{name_%s}", languageToSetMapTo)));
      Log.d(TAG, "setMapLanguageTo: run");
    }
  }
}