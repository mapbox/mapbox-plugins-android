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

  /**
   * Create a {@link LocalizationPlugin}.
   *
   * @param mapboxMap the MapboxMap to apply the localization plugin to
   * @since 0.1.0
   */
  public LocalizationPlugin(@NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    setMapTextLanguage(getDeviceLanguage());
  }

  /**
   * Retrieve the language that the device is currently set to.
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
   * Sets map text to the specified language
   *
   * @param languageToSetMapTo The language that to set the map text to.
   * @since 0.1.0
   */
  public void setMapTextLanguage(String languageToSetMapTo) {
    for (Source source : mapboxMap.getSources()) {
      if (sourceIsFromMapbox(source)) {
        for (Layer layer : mapboxMap.getLayers()) {
          if (layerHasAdjustableTextField(layer)) {
            if (((SymbolLayer) layer).getTextField().getValue().contains("name")) {
              layer.setProperties(textField(String.format("{name_%s}", languageToSetMapTo)));
            }
            if (((SymbolLayer) layer).getTextField().getValue().contains("abbr")
              && !getDeviceLanguage().equals("en")) {
              layer.setProperties(textField(String.format("{name_%s}", languageToSetMapTo)));
            }
          }
        }
      }
    }
  }

  /**
   * Checks whether the map's source is a source provided by Mapbox, rather than a custom source.
   *
   * @param singleSource an individual source object from the map
   * @return true if the source is from a Mapbox vector source, false if it's a
   * custom Studio data set or from elsewhere.
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