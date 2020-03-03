package com.mapbox.mapboxsdk.plugins.localization;


import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale.Languages;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.expressions.Expression.raw;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

/**
 * Useful class for quickly adjusting the maps language and the maps camera starting position.
 * You can either use {@link #matchMapLanguageWithDeviceDefault(boolean acceptFallback)} to match the map language with
 * the one being currently used on the device. Using {@link #setMapLanguage(Locale, boolean acceptFallback)} and it's
 * variants, you can also change the maps language at anytime to any of the supported languages.
 * <p>
 * The plugin uses a fallback logic in case there are missing resources
 * - if there is no available localization for a label, the plugin will use local name, if it's Latin script based,
 * otherwise English. Traditional Chinese falls back to Simplified Chinese before executing before mentioned logic.
 * </p>
 * The plugin only support Mapbox sources:
 * <ul>
 * <li>- mapbox.mapbox-streets-v6</li>
 * <li>- mapbox.mapbox-streets-v7</li>
 * <li>- mapbox.mapbox-streets-v8</li>
 * </ul>
 *
 * @since 0.1.0
 */
@UiThread
public final class LocalizationPlugin {

  private static final List<String> SUPPORTED_SOURCES = new ArrayList<>();

  static {
    SUPPORTED_SOURCES.add("mapbox.mapbox-streets-v6");
    SUPPORTED_SOURCES.add("mapbox.mapbox-streets-v7");
    SUPPORTED_SOURCES.add("mapbox.mapbox-streets-v8");
  }

  // expression syntax
  private static final String EXPRESSION_REGEX = "\\b(name|name_.{2,7})\\b";

  private static final String EXPRESSION_V8_REGEX_BASE = "\\[\"get\", \"name_en\"], \\[\"get\", \"name\"]";
  private static final String EXPRESSION_V8_TEMPLATE_BASE = "[\"get\", \"name_en\"], [\"get\", \"name\"]";
  private static final String EXPRESSION_V8_REGEX_LOCALIZED =
    "\\[\"match\", \"(name|name_.{2,7})\", "
      + "\"name_zh-Hant\", \\[\"coalesce\", "
      + "\\[\"get\", \"name_zh-Hant\"], "
      + "\\[\"get\", \"name_zh-Hans\"], "
      + "\\[\"match\", \\[\"get\", \"name_script\"], \"Latin\", \\[\"get\", \"name\"], \\[\"get\", \"name_en\"]], "
      + "\\[\"get\", \"name\"]], "
      + "\\[\"coalesce\", "
      + "\\[\"get\", \"(name|name_.{2,7})\"], "
      + "\\[\"match\", \\[\"get\", \"name_script\"], \"Latin\", \\[\"get\", \"name\"], \\[\"get\", \"name_en\"]], "
      + "\\[\"get\", \"name\"]]"
      + "]";

  private static final String EXPRESSION_V8_TEMPLATE_LOCALIZED =
    "[\"match\", \"%s\", "
      + "\"name_zh-Hant\", [\"coalesce\", "
      + "[\"get\", \"name_zh-Hant\"], "
      + "[\"get\", \"name_zh-Hans\"], "
      + "[\"match\", [\"get\", \"name_script\"], \"Latin\", [\"get\", \"name\"], [\"get\", \"name_en\"]], "
      + "[\"get\", \"name\"]], "
      + "[\"coalesce\", "
      + "[\"get\", \"%s\"], "
      + "[\"match\", [\"get\", \"name_script\"], \"Latin\", [\"get\", \"name\"], [\"get\", \"name_en\"]], "
      + "[\"get\", \"name\"]]"
      + "]";

  // faulty step expression workaround
  private static final String STEP_REGEX = "\\[\"zoom\"], ";
  private static final String STEP_TEMPLATE = "[\"zoom\"], \"\", ";

  // configuration
  private final MapboxMap mapboxMap;
  private MapLocale mapLocale;
  @NonNull
  private Style style;

  /**
   * Public constructor for passing in the required objects.
   *
   * @param mapView   the MapView object in which the map is displayed
   * @param mapboxMap the Mapbox map object which your current map view is using for control
   * @param style     the Style object that represents a fully loaded style
   */
  public LocalizationPlugin(@NonNull MapView mapView, @NonNull final MapboxMap mapboxMap, @NonNull Style style) {
    this.mapboxMap = mapboxMap;
    this.style = style;
    if (!style.isFullyLoaded()) {
      throw new RuntimeException("The style has to be non-null and fully loaded.");
    }

    mapView.addOnDidFinishLoadingStyleListener(new MapView.OnDidFinishLoadingStyleListener() {
      @Override
      public void onDidFinishLoadingStyle() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {
            LocalizationPlugin.this.style = style;
            if (mapLocale != null) {
              setMapLanguage(mapLocale);
            }
          }
        });
      }
    });
  }

  private MapLocale getChineseMapLocale(MapLocale mapLocale, boolean isStreetsV7) {
    if (isStreetsV7) {
      // streets v7 supports name_zh(MapLocale.CHINA) and name_zh-Hans(MapLocale.CHINESE_HANS)
      // https://docs.mapbox.com/vector-tiles/reference/mapbox-streets-v7/#name-fields
      if (mapLocale.equals(MapLocale.CHINESE_HANS)) {
        return mapLocale;
      } else {
        return MapLocale.CHINA;
      }
    } else {
      // streets V6 only supports name_zh(MapLocale.CHINA)
      // https://docs.mapbox.com/vector-tiles/reference/mapbox-streets-v6/#name-fields
      return MapLocale.CHINA;
    }
  }

  /*
   * Map languages
   */

  /**
   * Initializing this class and then calling this method oftentimes will be the only thing you'll
   * need to quickly adjust the map language to the devices specified language.
   *
   * @since 0.1.0
   */
  public void matchMapLanguageWithDeviceDefault() {
    setMapLanguage(Locale.getDefault(), false);
  }

  /**
   * Initializing this class and then calling this method oftentimes will be the only thing you'll
   * need to quickly adjust the map language to the devices specified language.
   *
   * @param acceptFallback whether the locale should fallback to the first declared that matches the language,
   *                       the fallback locale can be added with {@link MapLocale#addMapLocale(Locale, MapLocale)}
   * @since 0.1.0
   */
  public void matchMapLanguageWithDeviceDefault(boolean acceptFallback) {
    setMapLanguage(Locale.getDefault(), acceptFallback);
  }

  /**
   * Set the map language directly by using one of the supported map languages found in
   * {@link Languages}.
   *
   * @param language one of the support languages Mapbox uses
   * @since 0.1.0
   */
  public void setMapLanguage(@Languages String language) {
    setMapLanguage(new MapLocale(language));
  }

  /**
   * If you'd like to set the map language to a specific locale, you can pass it in as a parameter
   * and MapLocale will try matching the information with one of the MapLocales found in its map.
   * If one isn't found, a null point exception will be thrown. To prevent this, ensure that the
   * locale you are trying to use, has a complementary {@link MapLocale} for it.
   *
   * @param locale a {@link Locale} which has a complementary {@link MapLocale} for it
   * @since 0.1.0
   */
  public void setMapLanguage(@NonNull Locale locale) {
    setMapLanguage(locale, false);
  }

  /**
   * If you'd like to set the map language to a specific locale, you can pass it in as a parameter
   * and MapLocale will try matching the information with one of the MapLocales found in its map.
   * If one isn't found, a null point exception will be thrown. To prevent this, ensure that the
   * locale you are trying to use, has a complementary {@link MapLocale} for it.
   *
   * @param locale         a {@link Locale} which has a complementary {@link MapLocale} for it
   * @param acceptFallback whether the locale should fallback to the first declared that matches the language,
   *                       the fallback locale can be added with {@link MapLocale#addMapLocale(Locale, MapLocale)}
   * @since 0.1.0
   */
  public void setMapLanguage(@NonNull Locale locale, boolean acceptFallback) {
    MapLocale mapLocale = MapLocale.getMapLocale(locale, acceptFallback);
    if (mapLocale != null) {
      Timber.d("Locale: %s, set MapLocale: %s", locale.toString(), mapLocale.getMapLanguage());
      setMapLanguage(mapLocale);
    } else {
      Timber.d("Couldn't match Locale %s %s to a MapLocale", locale.toString(), locale.getDisplayName());
    }
  }

  /**
   * You can pass in a {@link MapLocale} directly into this method which uses the language defined
   * in it to represent the language found on the map.
   *
   * @param mapLocale the {@link MapLocale} object which contains the desired map language
   * @since 0.1.0
   */
  public void setMapLanguage(@NonNull MapLocale mapLocale) {
    this.mapLocale = mapLocale;
    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

    List<Layer> layers = style.getLayers();
    for (Source source : style.getSources()) {
      if (sourceIsFromMapbox(source)) {
        boolean isStreetsV8 = sourceIsStreetsV8(source);
        for (Layer layer : layers) {
          if (layer instanceof SymbolLayer) {
            PropertyValue<?> textFieldProperty = ((SymbolLayer) layer).getTextField();
            if (textFieldProperty.isExpression()) {
              if (isStreetsV8) {
                convertExpressionV8(mapLocale, layer, textFieldProperty);
              } else {
                boolean isStreetsV7 = sourceIsStreetsV7(source);
                convertExpression(mapLocale, layer, textFieldProperty, isStreetsV7);
              }
            }
          }
        }
      } else {
        String url = null;
        if (source instanceof VectorSource) {
          url = ((VectorSource) source).getUrl();
        }
        if (url == null) {
          url = "not found";
        }
        Timber.d("The %s (%s) source is not based on Mapbox Vector Tiles. Supported sources:\n %s",
          source.getId(), url, SUPPORTED_SOURCES);
      }
    }
  }

  private void convertExpression(@NonNull MapLocale mapLocale, Layer layer,
                                 PropertyValue<?> textFieldProperty, boolean isStreetsV7) {
    Expression textFieldExpression = textFieldProperty.getExpression();
    if (textFieldExpression != null) {
      MapLocale newMapLocale = mapLocale;
      String mapLanguage = mapLocale.getMapLanguage();
      if (mapLanguage.startsWith("name_zh")) {
        // need to re-get mapLocale, since the default is for street-v8
        newMapLocale = getChineseMapLocale(mapLocale, isStreetsV7);
        Timber.d("reset mapLocale to: %s", newMapLocale.getMapLanguage());
      }

      String text = textFieldExpression.toString().replaceAll(EXPRESSION_REGEX, newMapLocale.getMapLanguage());
      if (text.startsWith("[\"step") && textFieldExpression.toArray().length % 2 == 0) {
        // got an invalid step expression from core, we need to add an additional name_x into step
        text = text.replaceAll(STEP_REGEX, STEP_TEMPLATE);
      }
      layer.setProperties(textField(raw(text)));
    }
  }

  private void convertExpressionV8(@NonNull MapLocale mapLocale, Layer layer, PropertyValue<?> textFieldProperty) {
    Expression textFieldExpression = textFieldProperty.getExpression();
    if (textFieldExpression != null) {
      String stringExpression =
        textFieldExpression.toString().replaceAll(EXPRESSION_V8_REGEX_LOCALIZED, EXPRESSION_V8_TEMPLATE_BASE);

      String mapLanguage = mapLocale.getMapLanguage();
      if (!mapLanguage.equals(MapLocale.ENGLISH)) {
        if (mapLanguage.equals("name_zh")) {
          mapLanguage = MapLocale.SIMPLIFIED_CHINESE;
        }
        stringExpression = stringExpression.replaceAll(EXPRESSION_V8_REGEX_BASE,
          String.format(Locale.US,
            EXPRESSION_V8_TEMPLATE_LOCALIZED,
            mapLanguage,
            mapLanguage));
      }
      layer.setProperties(textField(raw(stringExpression)));
    }
  }

  /*
   * Camera bounding box
   */

  /**
   * Adjust the map's camera position so that the entire countries boarders are within the viewport.
   * Specifically, this method gets the devices currently set locale and adjust the map camera to
   * view that country if a {@link MapLocale} matches.
   *
   * @param padding camera padding
   * @since 0.1.0
   */
  public void setCameraToLocaleCountry(int padding) {
    setCameraToLocaleCountry(Locale.getDefault(), padding);
  }

  /**
   * If you'd like to manually set the camera position to a specific map region or country, pass in
   * the locale (which must have a paired }{@link MapLocale}) to work properly
   *
   * @param locale  a {@link Locale} which has a complementary {@link MapLocale} for it
   * @param padding camera padding
   * @since 0.1.0
   */
  public void setCameraToLocaleCountry(Locale locale, int padding) {
    MapLocale mapLocale = MapLocale.getMapLocale(locale, false);
    if (mapLocale != null) {
      setCameraToLocaleCountry(mapLocale, padding);
    } else {
      Timber.d("Couldn't match Locale %s to a MapLocale", locale.getDisplayName());
    }
  }

  /**
   * You can pass in a {@link MapLocale} directly into this method which uses the country bounds
   * defined in it to represent the language found on the map.
   *
   * @param mapLocale the {@link MapLocale} object which contains the desired map bounds
   * @param padding   camera padding
   * @since 0.1.0
   */
  public void setCameraToLocaleCountry(MapLocale mapLocale, int padding) {
    LatLngBounds bounds = mapLocale.getCountryBounds();
    if (bounds == null) {
      throw new NullPointerException("Expected a LatLngBounds object but received null instead. Mak"
        + "e sure your MapLocale instance also has a country bounding box defined.");
    }
    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
  }

  /*
   * Supporting methods
   */

  /**
   * Checks whether the map's source is a source provided by Mapbox, rather than a custom source.
   *
   * @param singleSource an individual source object from the map
   * @return true if the source is from the Mapbox Streets vector source, false if it's not.
   */
  private boolean sourceIsFromMapbox(Source singleSource) {
    if (singleSource instanceof VectorSource) {
      String url = ((VectorSource) singleSource).getUrl();
      if (url != null) {
        for (String supportedSource : SUPPORTED_SOURCES) {
          if (url.contains(supportedSource)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean sourceIsStreetsV7(Source source) {
    if (source instanceof VectorSource) {
      String url = ((VectorSource) source).getUrl();
      return url != null && url.contains("mapbox.mapbox-streets-v7");
    }
    return false;
  }

  private boolean sourceIsStreetsV8(Source source) {
    if (source instanceof VectorSource) {
      String url = ((VectorSource) source).getUrl();
      return url != null && url.contains("mapbox.mapbox-streets-v8");
    }
    return false;
  }
}
