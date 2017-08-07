package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import java.util.List;
import java.util.Locale;

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

  private boolean localizationEnabled;
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

    listOfMapLayers = mapboxMap.getLayers();
    String deviceLanguage = Locale.getDefault().getLanguage();
    for (Layer layer : listOfMapLayers) {
      // TODO: Fix if() statement? What should if statement be looking for?
      if (layer.getId().contains("")) {
        layer.setProperties(PropertyFactory.textField(String.format("{name_%s}", deviceLanguage)));
        Log.d("LocalizationPlugin", "LocalizationPlugin: for textField = " + String.format("{name_%s}", deviceLanguage));
      }
    }
  }

  /**
   * Resets map text to English language (default)
   * <p>
   * Full list of language codes supported by Android system: https://stackoverflow.com/a/30028371/6358488
   *
   * @param languageToSetMapTo The language that you'd like to set the map text to.
   * @since 0.1.0
   */
  public void setMapTextLanguage(String languageToSetMapTo) {
    for (Layer layer : listOfMapLayers) {
      layer.setProperties(PropertyFactory.textField(String.format("{name_%s}", languageToSetMapTo)));
      Log.d(TAG, "setMapTextLanguage: run");
    }
  }

  /**
   * Sets map text to English language
   */
  public void setMapTextToEnglish() {
    setMapTextLanguage("en");
  }

  /**
   * Sets map text to Spanish language
   */
  public void setMapTextToSpanish() {
    setMapTextLanguage("es");
  }

  /**
   * Sets map text to French language
   */
  public void setMapTextToFrench() {
    setMapTextLanguage("fr");
  }

  /**
   * Sets map text to German language
   */
  public void setMapTextToGerman() {
    setMapTextLanguage("de");
  }

  /**
   * Sets map text to Russian language
   */
  public void setMapTextToRussian() {
    setMapTextLanguage("ru");
  }

  /**
   * Sets map text to Traditional Chinese language
   */
  public void setMapTextToTraditionalChinese() {
    setMapTextLanguage("zh");
  }

  /**
   * Sets map text to Simplified Chinese language
   */
  public void setMapTextToSimplifiedChinese() {
    setMapTextLanguage("zh-Hans");
  }

  /**
   * Sets map text to Portuguese language
   */
  public void setMapTextToPortuguese() {
    setMapTextLanguage("pt");
  }

  /**
   * Sets map text to Arabic language
   */
  public void setMapTextToArabic() {
    setMapTextLanguage("ar");
  }
}