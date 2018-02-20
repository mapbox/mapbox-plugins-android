package com.mapbox.mapboxsdk.plugins.localization;

import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale.Languages;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

public final class LocalizationPlugin {

  private final MapboxMap mapboxMap;

  public LocalizationPlugin(@NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
  }

  /*
   * Map languages
   */

  public void setMapLanguage() {
    setMapLanguage(Locale.getDefault());
  }

  public void setMapLangauge(@Languages String language) {
    setMapLanguage(new MapLocale(language));
  }

  public void setMapLanguage(@NonNull Locale locale) {
    setMapLanguage(checkMapLocalNonNull(locale));
  }

  public void setMapLanguage(@NonNull MapLocale mapLocale) {
    List<Layer> layers = mapboxMap.getLayers();
    for (Source source : mapboxMap.getSources()) {
      if (sourceIsFromMapbox(source)) {
        for (Layer layer : layers) {
          if (layerHasAdjustableTextField(layer)
            && ((SymbolLayer) layer).getTextField().getValue().contains("{name")
            || ((SymbolLayer) layer).getTextField().getValue().contains("{abbr}")) {
            layer.setProperties(textField(String.format("{%s}", mapLocale.getMapLanguage())));
            layers.remove(layer);
          }
        }
      }
    }
  }

  /*
   * Camera bounding box
   */

  public void setCameraToLocaleCountry() {
    setCameraToLocaleCountry(Locale.getDefault());
  }

  public void setCameraToLocaleCountry(Locale locale) {
    setCameraToLocaleCountry(checkMapLocalNonNull(locale));
  }

  public void setCameraToLocaleCountry(MapLocale mapLocale) {
    LatLngBounds bounds = mapLocale.getCountryBounds();
    if (bounds == null) {
      throw new NullPointerException("Expected a LatLngBounds object but received null instead. Mak"
        + "e sure your MapLocale instance also has a country bounding box defined.");
    }
    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
  }

  /*
   * Supporting methods
   */

  private MapLocale checkMapLocalNonNull(Locale locale) {
    MapLocale mapLocale = MapLocale.getMapLocale(locale);
    if (mapLocale == null) {
      throw new NullPointerException("Locale " + locale.toString() + "has no matching MapLocale obj"
        + "ect. You need to create an instance of MapLocale and add it to the MapLocale Cache using"
        + " the addMapLocale method.");
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
    return singleSource instanceof VectorSource
      && ((VectorSource) singleSource).getUrl().substring(0, 9).equals("mapbox://")
      && (((VectorSource) singleSource).getUrl().contains("mapbox.mapbox-streets-v7")
      || ((VectorSource) singleSource).getUrl().contains("mapbox.mapbox-streets-v6"));
  }

  /**
   * Checks whether a single map layer has a textField that could potentially be localized to the
   * device's language.
   *
   * @param singleLayer an individual layer from the map
   * @return true if the layer has a textField eligible for translation, false if not.
   */
  private boolean layerHasAdjustableTextField(Layer singleLayer) {
    return singleLayer instanceof SymbolLayer && (((SymbolLayer) singleLayer).getTextField() != null
      && (((SymbolLayer) singleLayer).getTextField().getValue() != null
      && !(((SymbolLayer) singleLayer).getTextField().getValue().isEmpty())));
  }
}