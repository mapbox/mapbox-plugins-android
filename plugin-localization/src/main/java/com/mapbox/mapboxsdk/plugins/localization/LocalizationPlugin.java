package com.mapbox.mapboxsdk.plugins.localization;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale.Languages;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

/**
 * Useful class for quickly adjusting the maps language and the maps camera starting position.
 * You can either use {@link #matchMapLanguageWithDeviceDefault()} to match the map language with
 * the one being currently used on the device. Using {@link #setMapLanguage(Locale)} and it's
 * variants, you can also change the maps langauge at anytime to any of the supported languages.
 *
 * @since 0.1.0
 */
public final class LocalizationPlugin implements MapView.OnMapChangedListener {

  private final MapboxMap mapboxMap;
  private MapLocale mapLocale;

  /**
   * Public constructor for passing in the required {@link MapboxMap} object.
   *
   * @param mapboxMap the Mapbox map object which your current map view is using for control
   * @since 0.1.0
   */
  public LocalizationPlugin(@NonNull MapView mapview, @NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapview.addOnMapChangedListener(this);
  }

  /**
   * Handles resetting the map language when the map style changes.
   */
  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_LOADING_STYLE && mapLocale != null) {
      setMapLanguage(mapLocale);
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
    setMapLanguage(Locale.getDefault());
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
   * @throws NullPointerException thrown when the locale passed into the method doesn't have a
   *                              matching {@link MapLocale}
   * @since 0.1.0
   */
  public void setMapLanguage(@NonNull Locale locale) {
    setMapLanguage(checkMapLocalNonNull(locale));
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
    List<Layer> layers = mapboxMap.getLayers();
    for (Source source : mapboxMap.getSources()) {
      if (sourceIsFromMapbox(source)) {
        for (Layer layer : layers) {
          if (layerHasAdjustableTextField(layer)) {
            transformTextFieldValues(layer);
          } else if (layerHasAdjustableExpressionTextField(layer)) {
            transformExpressionTextFieldValue(layer);
          }
        }
      }
    }
  }

  private void transformTextFieldValues(Layer layer) {
    String textField = ((SymbolLayer) layer).getTextField().getValue();
    if (textField != null
      && (textField.contains("{name") || textField.contains("{abbr}"))) {
      textField = textField.replaceAll("[{]((name).*?)[}]",
        String.format("{%s}", mapLocale.getMapLanguage()));
      layer.setProperties(textField(textField));
    }
  }

  // TODO Finish transforming expression
  private void transformExpressionTextFieldValue(Layer layer) {
    SymbolLayer textLayer = (SymbolLayer) layer;
    int position;
    for (position = 0; position < textLayer.getTextField().getExpression().toArray().length; position++) {
      if (textLayer.getTextField().getExpression().toArray()[position] instanceof String
        && textLayer.getTextField().getExpression().toArray()[position].toString().contains("{name")) {
        break;
      }
    }
    System.out.println(textLayer.getId());
//    Object[] expressionArray = textLayer.getTextField().getExpression().toArray();

    String s = textLayer.getTextField().getExpression().toString();
    System.out.println(s);
    s = s.replaceAll("[{]((name).*?)[}]", String.format("[\"get\", \"{%s}\"]", mapLocale.getMapLanguage()));
//    s = s.replace(", ,", ",[\"literal\", \"\"],");


    s = s.replace("{ref}", "[\"get\", \"{ref}\"]");
    s = s.replace("{abbr}", "[\"get\", \"{abbr}\"]");
    s = s.replace("{code}", "[\"get\", \"{code}\"]");
    System.out.println(s);

    Gson gson = new Gson();
    JsonArray jsonArray = gson.fromJson(s, JsonArray.class);


//    expressionArray[position] = mapLocale.getMapLanguage();
    System.out.println(jsonArray.toString());


    textLayer.setProperties(textField(Expression.Converter.convert(jsonArray)));
  }


  //
  // Camera bounding box
  //

  /**
   * Adjust the map's camera position so that the entire countries boarders are within the viewport.
   * Specifically, this method gets the devices currently set locale and adjust the map camera to
   * view that country if a {@link MapLocale]} matches.
   *
   * @since 0.1.0
   */
  public void setCameraToLocaleCountry() {
    setCameraToLocaleCountry(Locale.getDefault());
  }

  /**
   * If you'd like to manually set the camera position to a specific map region or country, pass in
   * the locale (which must have a paired }{@link MapLocale}) to work properly
   *
   * @param locale a {@link Locale} which has a complementary {@link MapLocale} for it
   * @throws NullPointerException thrown when the locale passed into the method doesn't have a
   *                              matching {@link MapLocale}
   * @since 0.1.0
   */
  public void setCameraToLocaleCountry(Locale locale) {
    setCameraToLocaleCountry(checkMapLocalNonNull(locale));
  }

  /**
   * You can pass in a {@link MapLocale} directly into this method which uses the country bounds
   * defined in it to represent the language found on the map.
   *
   * @param mapLocale he {@link MapLocale} object which contains the desired map bounds
   * @throws NullPointerException thrown when it was expecting a {@link LatLngBounds} but instead
   *                              it was null
   * @since 0.1.0
   */
  public void setCameraToLocaleCountry(MapLocale mapLocale) {
    LatLngBounds bounds = mapLocale.getCountryBounds();
    if (bounds == null) {
      throw new NullPointerException("Expected a LatLngBounds object but received null instead. Mak"
        + "e sure your MapLocale instance also has a country bounding box defined.");
    }
    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
  }

  //
  // Supporting methods
  //

  private MapLocale checkMapLocalNonNull(Locale locale) {
    MapLocale mapLocale = MapLocale.getMapLocale(locale);
    if (mapLocale == null) {
      throw new NullPointerException("Locale " + locale.toString() + " has no matching MapLocale ob"
        + "ject. You need to create an instance of MapLocale and add it to the MapLocale Cache usin"
        + "g the addMapLocale method.");
    }
    return mapLocale;
  }

  /**
   * Checks whether the map's source is a source provided by Mapbox, rather than a custom source.
   *
   * @param singleSource an individual source object from the map
   * @return true if the source is from the Mapbox Streets vector source, false if it's not.
   */
  private boolean sourceIsFromMapbox(Source singleSource) {
    String url = ((VectorSource) singleSource).getUrl();

    return url != null
      && (url.substring(0, 9).equals("mapbox://")
      && url.contains("mapbox.mapbox-streets-v8")
      || url.contains("mapbox.mapbox-streets-v7")
      || url.contains("mapbox.mapbox-streets-v6"));
  }

  /**
   * Checks whether a single map layer has a textField that could potentially be localized to the
   * device's language.
   *
   * @param layer an individual layer from the map style
   * @return true if the layer has a textField eligible for translation, false if not.
   */
  private boolean layerHasAdjustableTextField(Layer layer) {
    // Check that the layers a symbol layer
    SymbolLayer textLayer = isSymbolLayer(layer);
    if (textLayer == null) {
      return false;
    }

    return textLayer.getTextField() != null && textLayer.getTextField().getValue() != null
      && !TextUtils.isEmpty(textLayer.getTextField().getValue());
  }

  private boolean layerHasAdjustableExpressionTextField(Layer layer) {
    // Check that the layers a symbol layer
    SymbolLayer textLayer = isSymbolLayer(layer);
    if (textLayer == null) {
      return false;
    }

    return textLayer.getTextField() != null && textLayer.getTextField().isExpression();
  }

  /**
   * Checks that the layer is an instance of a symbol layer and if so, returns the layer as an
   * instance of {@link SymbolLayer}.
   *
   * @param layer an individual layer from the map style
   * @return true if the layer is an instance of a symbol layer
   */
  @Nullable
  private SymbolLayer isSymbolLayer(Layer layer) {
    if (layer instanceof SymbolLayer) {
      return (SymbolLayer) layer;
    } else {
      return null;
    }
  }
}