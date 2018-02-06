package com.mapbox.mapboxsdk.plugins.localization;

import android.os.Build;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

/**
 * The localization plugin enables localization of map labels into the user’s preferred language.
 * Preferred language is determined by the language that the device is set to in the device's settings.
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)} and provide
 * a valid instance of a {@link MapboxMap}.
 * </p>
 */
public final class LocalizationPlugin {

  private MapboxMap mapboxMap;
  private String selectedBackupLanguage;

  /**
   * Create a {@link LocalizationPlugin}.
   *
   * @param mapboxMap      the MapboxMap to apply the localization plugin to
   * @param backupLanguage the default language for the map to fall back to if the device language is not
   *                       supported by the Mapbox Streets style source.
   *                       See https://www.mapbox.com/vector-tiles/mapbox-streets-v7/#overview for a list of
   *                       supported languages.
   * @since 0.1.0
   */
  public LocalizationPlugin(@NonNull MapboxMap mapboxMap, @NonNull String backupLanguage) {
    this.mapboxMap = mapboxMap;
    this.selectedBackupLanguage = backupLanguage;
    setMapTextLanguage(getDeviceLanguage());
  }

  /**
   * Retrieve the code for the language that the device is currently set to.
   *
   * @return The correct code according to ISO-639-1
   */
  public String getDeviceLanguage() {
    if (Build.VERSION.SDK_INT >= 21) {
      String languageTag = Locale.getDefault().toLanguageTag();
      if (languageTag.contains("Hans")) {
        return "zh-Hans";
      } else if (languageTag.contains("Hant")) {
        return "zh";
      } else {
        return Locale.getDefault().toLanguageTag().substring(0, 2);
      }
    } else {
      if (Locale.getDefault().getLanguage().equalsIgnoreCase("IW")) {
        return "he";
      } else if (Locale.getDefault().getLanguage().equalsIgnoreCase("IN")) {
        return "id";
      } else if (Locale.getDefault().getLanguage().equalsIgnoreCase("JI")) {
        return "yi";
      } else if (Locale.getDefault().getLanguage().equalsIgnoreCase("JW")) {
        return "jv";
      } else if (Locale.getDefault().getLanguage().contains("zh-Hans")
        || Locale.getDefault().getDisplayName().contains("简体")) {
        return "zh-Hans";
      } else if (Locale.getDefault().getLanguage().contains("zh-Hant")
        || Locale.getDefault().getDisplayName().contains("繁體")) {
        return "zh";
      } else {
        return Locale.getDefault().getLanguage();
      }
    }
  }

  /**
   * Checks whether a certain language is supported by Mapbox Streets style source.
   *
   * @param languageTag the language tag to check against Mapbox-supported languages
   * @return whether a certain language (tag) matches one of the tags associated with a
   * OSM-supported language
   */
  private boolean mapboxSourceSupportsLanguage(String languageTag) {
    return languageTag.equals("ar")
      || languageTag.equals("en")
      || languageTag.equals("es")
      || languageTag.equals("fr")
      || languageTag.equals("de")
      || languageTag.equals("pt")
      || languageTag.equals("ru")
      || languageTag.equals("zh")
      || languageTag.equals("zh-Hans");
  }

  /**
   * Retrieves and adjusts the individual map layers that need to be adjusted
   *
   * @param languageToSetMapTo the language that the map is being adjusted to
   */
  private void adjustMapTextLayers(String languageToSetMapTo) {
    for (Source source : mapboxMap.getSources()) {
      if (sourceIsFromMapbox(source)) {
        for (Layer layer : mapboxMap.getLayers()) {
          if (layerHasAdjustableTextField(layer)) {
            if (((SymbolLayer) layer).getTextField().getValue().contains("{name")
              || !getDeviceLanguage().equals("en") && ((SymbolLayer) layer).getTextField().getValue().contains("{abbr}")
              ) {
              layer.setProperties(textField(String.format("{name_%s}", languageToSetMapTo)));
            }
          }
        }
      }
    }
  }

  /**
   * Sets map text to a specified language. If the language is not supported by OpenStreetMap, then the
   * default language (specified during the instantiation of the plugin) is checked for OSM support. If
   * it is supported by OSM, then the map will be changed to the default language. English is the last-resort
   * language used in case the default language isn't supported by OSM neither.
   *
   * @param language The language that to set the map text to.
   * @since 0.1.0
   */
  public void setMapTextLanguage(String language) {
    if (mapboxSourceSupportsLanguage(language)) {
      adjustMapTextLayers(language);
    } else if (mapboxSourceSupportsLanguage(selectedBackupLanguage)) {
      adjustMapTextLayers(selectedBackupLanguage);
    } else {
      adjustMapTextLayers("en");
    }
  }

  /**
   * Checks whether the map's source is a source provided by Mapbox, rather than a custom source.
   *
   * @param singleSource an individual source object from the map
   * @return true if the source is from the Mapbox Streets vector source, false if it's not.
   */
  private boolean sourceIsFromMapbox(Source singleSource) {
    return singleSource instanceof VectorSource
      && ((VectorSource) singleSource).getUrl().substring(0, 9).equals("mapbox://")
      && (((VectorSource) singleSource).getUrl().contains("mapbox.mapbox-streets-v7")
      || ((VectorSource) singleSource).getUrl().contains("mapbox.mapbox-streets-v6"));
  }

  /**
   * Checks whether a single map layer has a textField that could potentially be localized to the device's language.
   *
   * @param singleLayer an individual layer from the map
   * @return true if the layer has a textField eligible for translation, false if not.
   */
  private boolean layerHasAdjustableTextField(Layer singleLayer) {
    return singleLayer instanceof SymbolLayer && (((SymbolLayer) singleLayer).getTextField() != null
      && (((SymbolLayer) singleLayer).getTextField().getValue() != null
      && !(((SymbolLayer) singleLayer).getTextField().getValue().isEmpty())));
  }

  /**
   * Sets map text to English.
   */
  public void setMapTextToEnglish() {
    setMapTextLanguage("en");
  }

  /**
   * Sets map text to Spanish.
   */
  public void setMapTextToSpanish() {
    setMapTextLanguage("es");
  }

  /**
   * Sets map text to French.
   */
  public void setMapTextToFrench() {
    setMapTextLanguage("fr");
  }

  /**
   * Sets map text to German.
   */
  public void setMapTextToGerman() {
    setMapTextLanguage("de");
  }

  /**
   * Sets map text to Russian.
   */
  public void setMapTextToRussian() {
    setMapTextLanguage("ru");
  }

  /**
   * Sets map text to Traditional Chinese characters.
   * <p>
   * This method uses simplified Chinese characters for custom label layers: #country_label, #state_label,
   * and #marine_label. All other label layers are sourced from OpenStreetMap and may contain one of several dialects
   * and either simplified or traditional Chinese characters in the {name_zh} field.
   */
  public void setMapTextToChinese() {
    setMapTextLanguage("zh");
  }

  /**
   * Sets map text to Simplified Chinese characters.
   * <p>
   * Using this method is similar to setMapTextToChinese(), except any Traditional Chinese
   * characters are automatically transformed to Simplified Chinese.
   */
  public void setMapTextToSimplifiedChinese() {
    setMapTextLanguage("zh-Hans");
  }

  /**
   * Sets map text to Portuguese.
   */
  public void setMapTextToPortuguese() {
    setMapTextLanguage("pt");
  }

  /**
   * Sets map text to Arabic.
   */
  public void setMapTextToArabic() {
    setMapTextLanguage("ar");
  }
}