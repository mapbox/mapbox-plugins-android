package com.mapbox.mapboxsdk.plugins.markerlayer;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import static com.mapbox.mapboxsdk.style.functions.Function.property;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

class MarkerLayer {

  private static final String LAYER_ID = "com.mapbox.mapboxsdk.plugins.markerlayer";
  private SymbolLayer symbolLayer;

  public MarkerLayer(MapboxMap mapboxMap) {
    symbolLayer = createSymbolLayer();
    mapboxMap.addLayer(symbolLayer);
  }

  SymbolLayer createSymbolLayer() {
    SymbolLayer symbolLayer = new SymbolLayer(LAYER_ID, MarkerSource.SOURCE_ID);
    symbolLayer.setProperties(
      iconSize(
        property(
          "icon-size",
          Stops.<Float>identity())
      ),
      iconOpacity(
        property(
          "icon-opacity",
          Stops.<Float>identity())
      ),
      iconRotate(
        property(
          "icon-rotate",
          Stops.<Float>identity())
      ),
      iconImage("{icon-id}"),
      iconAllowOverlap(true)
    );
    return symbolLayer;
  }
}
