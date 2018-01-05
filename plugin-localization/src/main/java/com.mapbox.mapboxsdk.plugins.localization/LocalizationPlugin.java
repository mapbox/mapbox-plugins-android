package com.mapbox.mapboxsdk.plugins.localization;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

/**
 * The localization plugin enables localization of map labels into the user’s preferred language
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)} and provide
 * a valid instance of a {@link MapboxMap}.
 * </p>
 */
public final class LocalizationPlugin {

  private List<Layer> listOfMapLayers;
  private String TAG = "LocalizationPlugin";

  /**
   * Create a {@link LocalizationPlugin}.
   *
   * @param mapboxMap the MapboxMap to apply the localization plugin to
   * @since 0.1.0
   */
  public LocalizationPlugin(@NonNull final MapboxMap mapboxMap) {
    listOfMapLayers = mapboxMap.getLayers();
    for (Layer layer : listOfMapLayers) {
      if (layer instanceof SymbolLayer) {
        if (((SymbolLayer) layer).getTextField() != null) {
          if (((SymbolLayer) layer).getTextField().getValue() != null &&
            !((SymbolLayer) layer).getTextField().getValue().isEmpty()) {
            if (((SymbolLayer) layer).getTextField().getValue().contains("name")) {
              layer.setProperties(textField(String.format("{name_%s}", getDeviceLanguage())));
            }
            if (((SymbolLayer) layer).getTextField().getValue().contains("abbr") &&
              !getDeviceLanguage().equals("en")) {
              layer.setProperties(textField(String.format("{name_%s}", getDeviceLanguage())));
            }
          }
        }
      }
    }
  }

  /**
   * This method helps getting the right language ISO code, which suppose to be
   * according to ISO-639-1, BUT, on some devices it still returns the deprecated ISO-639.
   * <BR>
   * Languages codes that are translated in this method:
   * <ul>
   * <li>Hebrew:  IW -> he
   * <li>Indonesian: IN -> id
   * <li>Yiddish: JI -> yi
   * <li>Javanese: JW -> jv
   * </ul>
   *
   * @return The right code according to ISO-639-1
   */
  private String getDeviceLanguage() {

    String deviceLanguage = Locale.getDefault().getLanguage();

    if (deviceLanguage.equalsIgnoreCase("IW")) {
      return "he";
    } else if (deviceLanguage.equalsIgnoreCase("IN")) {
      return "id";
    } else if (deviceLanguage.equalsIgnoreCase("JI")) {
      return "yi";
    } else if (deviceLanguage.equalsIgnoreCase("JW")) {
      return "jv";
    } else if (getLanguageTag().contains("zh-Hans") || Locale.getDefault().getDisplayName().contains("简体")) {
      return "zh-Hans";
    } else if (getLanguageTag().contains("zh-Hant") || Locale.getDefault().getDisplayName().contains("繁體")) {
      return "zh";
    } else {
      return deviceLanguage;
    }
  }

  @TargetApi(21)
  private String getLanguageTag() {
    return Locale.getDefault().toLanguageTag();
  }

  /**
   * Sets map text to the specified language
   *
   * @param languageToSetMapTo The language that you'd like to set the map text to.
   * @since 0.1.0
   */
  public void setMapTextLanguage(String languageToSetMapTo) {
    for (Layer layer : listOfMapLayers) {
      layer.setProperties(textField(String.format("{name_%s}", languageToSetMapTo)));
    }
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
   * Sets map text to Chinese.
   * <p>
   * This method uses simplified Chinese characters for custom label layers: #country_label, #state_label,
   * and #marine_label. All other label layers are sourced from OpenStreetMap and may contain one of several dialects
   * and either simplified or traditional Chinese characters in the {name_zh} field.
   */
  public void setMapTextToChinese() {
    setMapTextLanguage("zh");
  }

  /**
   * Sets map text to Simplified Chinese.
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