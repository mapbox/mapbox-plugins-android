package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LongSparseArray;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconHaloBlur;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconHaloColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconHaloWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconKeepUpright;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOptional;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconPadding;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconPitchAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotationAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTextFit;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTextFitPadding;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTranslateAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.symbolAvoidEdges;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.symbolPlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.symbolSpacing;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textFont;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textHaloBlur;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textHaloColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textHaloWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textJustify;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textKeepUpright;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textLetterSpacing;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textLineHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textMaxAngle;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textMaxWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOptional;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textPadding;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textPitchAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textRotationAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textTransform;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textTranslateAnchor;

/**
 * The symbol manager allows to add symbols to a map.
 */
public class SymbolManager {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-symbol-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-symbol-layer";

  // map integration components
  private MapboxMap mapboxMap;
  private GeoJsonSource geoJsonSource;
  private SymbolLayer layer;

  // callback listeners
  private List<OnSymbolClickListener> symbolClickListeners = new ArrayList<>();
  private final MapClickResolver mapClickResolver;

  // internal data set
  private final LongSparseArray<Symbol> symbols = new LongSparseArray<>();
  private final List<Feature> features = new ArrayList<>();
  private long currentId;

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   */
  @UiThread
  public SymbolManager(@NonNull MapboxMap mapboxMap) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new SymbolLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      )
    );
  }

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap     the map object to add symbols to
   * @param geoJsonSource the geojson source to add symbols to
   * @param layer         the symbol layer to visualise Symbols with
   */
  @VisibleForTesting
  public SymbolManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, SymbolLayer layer) {
    this.mapboxMap = mapboxMap;
    this.geoJsonSource = geoJsonSource;
    this.layer = layer;
    mapboxMap.addSource(geoJsonSource);
    mapboxMap.addLayer(layer);
    mapboxMap.addOnMapClickListener(mapClickResolver = new MapClickResolver(mapboxMap));
  }

  /**
   * Cleanup symbol manager, used to clear listeners
   */
  @UiThread
  public void onDestroy() {
    mapboxMap.removeOnMapClickListener(mapClickResolver);
    symbolClickListeners.clear();
  }

  /**
   * Create a symbol on the map from a LatLng coordinate.
   *
   * @param latLng place to layout the symbol on the map
   * @return the newly created symbol
   */
  @UiThread
  public Symbol createSymbol(@NonNull LatLng latLng) {
    Symbol symbol = new Symbol(this, currentId);
    symbol.setLatLng(latLng);
    symbols.put(currentId, symbol);
    currentId++;
    return symbol;
  }

  /**
   * Delete a symbol from the map.
   *
   * @param symbol to be deleted
   */
  @UiThread
  public void deleteSymbol(@NonNull Symbol symbol) {
    symbols.remove(symbol.getId());
    updateSource();
  }

  /**
   * Get a list of current symbols.
   *
   * @return list of symbols
   */
  @UiThread
  public LongSparseArray<Symbol> getSymbols() {
    return symbols;
  }

  /**
   * Trigger an update to the underlying source
   */
  public void updateSource() {
    // todo move feature creation to a background thread?
    features.clear();
    Symbol symbol;
    for (int i = 0; i < symbols.size(); i++) {
      symbol = symbols.valueAt(i);
      features.add(Feature.fromGeometry(symbol.getGeometry(), symbol.getFeature()));
    }
    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));
  }

  /**
   * Add a callback to be invoked when a symbol has been clicked.
   *
   * @param listener the callback to be invoked when a symbol is clicked
   */
  @UiThread
  public void addOnSymbolClickListener(@NonNull OnSymbolClickListener listener) {
    symbolClickListeners.add(listener);
  }

  /**
   * Remove a previously added callback that was to be invoked when symbol has been clicked.
   *
   * @param listener the callback to be removed
   */
  @UiThread
  public void removeOnSymbolClickListener(@NonNull OnSymbolClickListener listener) {
    if (symbolClickListeners.contains(listener)) {
      symbolClickListeners.remove(listener);
    }
  }

  private static PropertyValue<?>[] getLayerDefinition() {
    return new PropertyValue[]{
     iconSize(get("icon-size")),
     iconImage(get("icon-image")),
     iconRotate(get("icon-rotate")),
     iconOffset(get("icon-offset")),
     iconAnchor(get("icon-anchor")),
     textField(get("text-field")),
     textFont(get("text-font")),
     textSize(get("text-size")),
     textMaxWidth(get("text-max-width")),
     textLetterSpacing(get("text-letter-spacing")),
     textJustify(get("text-justify")),
     textAnchor(get("text-anchor")),
     textRotate(get("text-rotate")),
     textTransform(get("text-transform")),
     textOffset(get("text-offset")),
     iconOpacity(get("icon-opacity")),
     iconColor(get("icon-color")),
     iconHaloColor(get("icon-halo-color")),
     iconHaloWidth(get("icon-halo-width")),
     iconHaloBlur(get("icon-halo-blur")),
     textOpacity(get("text-opacity")),
     textColor(get("text-color")),
     textHaloColor(get("text-halo-color")),
     textHaloWidth(get("text-halo-width")),
     textHaloBlur(get("text-halo-blur")),
    };
  }

  // Property accessors
  /**
   * Get the SymbolPlacement property
   *
   * @return property wrapper value around String
   */
  public String getSymbolPlacement() {
    return layer.getSymbolPlacement().value;
  }

  /**
   * Set the SymbolPlacement property
   *
   * @param value property wrapper value around String
   */
  public void setSymbolPlacement(String value) {
    layer.setProperties(symbolPlacement(value));
  }

  /**
   * Get the SymbolSpacing property
   *
   * @return property wrapper value around Float
   */
  public Float getSymbolSpacing() {
    return layer.getSymbolSpacing().value;
  }

  /**
   * Set the SymbolSpacing property
   *
   * @param value property wrapper value around Float
   */
  public void setSymbolSpacing(Float value) {
    layer.setProperties(symbolSpacing(value));
  }

  /**
   * Get the SymbolAvoidEdges property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getSymbolAvoidEdges() {
    return layer.getSymbolAvoidEdges().value;
  }

  /**
   * Set the SymbolAvoidEdges property
   *
   * @param value property wrapper value around Boolean
   */
  public void setSymbolAvoidEdges(Boolean value) {
    layer.setProperties(symbolAvoidEdges(value));
  }

  /**
   * Get the IconAllowOverlap property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconAllowOverlap() {
    return layer.getIconAllowOverlap().value;
  }

  /**
   * Set the IconAllowOverlap property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconAllowOverlap(Boolean value) {
    layer.setProperties(iconAllowOverlap(value));
  }

  /**
   * Get the IconIgnorePlacement property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconIgnorePlacement() {
    return layer.getIconIgnorePlacement().value;
  }

  /**
   * Set the IconIgnorePlacement property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconIgnorePlacement(Boolean value) {
    layer.setProperties(iconIgnorePlacement(value));
  }

  /**
   * Get the IconOptional property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconOptional() {
    return layer.getIconOptional().value;
  }

  /**
   * Set the IconOptional property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconOptional(Boolean value) {
    layer.setProperties(iconOptional(value));
  }

  /**
   * Get the IconRotationAlignment property
   *
   * @return property wrapper value around String
   */
  public String getIconRotationAlignment() {
    return layer.getIconRotationAlignment().value;
  }

  /**
   * Set the IconRotationAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setIconRotationAlignment(String value) {
    layer.setProperties(iconRotationAlignment(value));
  }

  /**
   * Get the IconTextFit property
   *
   * @return property wrapper value around String
   */
  public String getIconTextFit() {
    return layer.getIconTextFit().value;
  }

  /**
   * Set the IconTextFit property
   *
   * @param value property wrapper value around String
   */
  public void setIconTextFit(String value) {
    layer.setProperties(iconTextFit(value));
  }

  /**
   * Get the IconTextFitPadding property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconTextFitPadding() {
    return layer.getIconTextFitPadding().value;
  }

  /**
   * Set the IconTextFitPadding property
   *
   * @param value property wrapper value around Float[]
   */
  public void setIconTextFitPadding(Float[] value) {
    layer.setProperties(iconTextFitPadding(value));
  }

  /**
   * Get the IconPadding property
   *
   * @return property wrapper value around Float
   */
  public Float getIconPadding() {
    return layer.getIconPadding().value;
  }

  /**
   * Set the IconPadding property
   *
   * @param value property wrapper value around Float
   */
  public void setIconPadding(Float value) {
    layer.setProperties(iconPadding(value));
  }

  /**
   * Get the IconKeepUpright property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconKeepUpright() {
    return layer.getIconKeepUpright().value;
  }

  /**
   * Set the IconKeepUpright property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconKeepUpright(Boolean value) {
    layer.setProperties(iconKeepUpright(value));
  }

  /**
   * Get the IconPitchAlignment property
   *
   * @return property wrapper value around String
   */
  public String getIconPitchAlignment() {
    return layer.getIconPitchAlignment().value;
  }

  /**
   * Set the IconPitchAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setIconPitchAlignment(String value) {
    layer.setProperties(iconPitchAlignment(value));
  }

  /**
   * Get the TextPitchAlignment property
   *
   * @return property wrapper value around String
   */
  public String getTextPitchAlignment() {
    return layer.getTextPitchAlignment().value;
  }

  /**
   * Set the TextPitchAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setTextPitchAlignment(String value) {
    layer.setProperties(textPitchAlignment(value));
  }

  /**
   * Get the TextRotationAlignment property
   *
   * @return property wrapper value around String
   */
  public String getTextRotationAlignment() {
    return layer.getTextRotationAlignment().value;
  }

  /**
   * Set the TextRotationAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setTextRotationAlignment(String value) {
    layer.setProperties(textRotationAlignment(value));
  }

  /**
   * Get the TextLineHeight property
   *
   * @return property wrapper value around Float
   */
  public Float getTextLineHeight() {
    return layer.getTextLineHeight().value;
  }

  /**
   * Set the TextLineHeight property
   *
   * @param value property wrapper value around Float
   */
  public void setTextLineHeight(Float value) {
    layer.setProperties(textLineHeight(value));
  }

  /**
   * Get the TextMaxAngle property
   *
   * @return property wrapper value around Float
   */
  public Float getTextMaxAngle() {
    return layer.getTextMaxAngle().value;
  }

  /**
   * Set the TextMaxAngle property
   *
   * @param value property wrapper value around Float
   */
  public void setTextMaxAngle(Float value) {
    layer.setProperties(textMaxAngle(value));
  }

  /**
   * Get the TextPadding property
   *
   * @return property wrapper value around Float
   */
  public Float getTextPadding() {
    return layer.getTextPadding().value;
  }

  /**
   * Set the TextPadding property
   *
   * @param value property wrapper value around Float
   */
  public void setTextPadding(Float value) {
    layer.setProperties(textPadding(value));
  }

  /**
   * Get the TextKeepUpright property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextKeepUpright() {
    return layer.getTextKeepUpright().value;
  }

  /**
   * Set the TextKeepUpright property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextKeepUpright(Boolean value) {
    layer.setProperties(textKeepUpright(value));
  }

  /**
   * Get the TextAllowOverlap property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextAllowOverlap() {
    return layer.getTextAllowOverlap().value;
  }

  /**
   * Set the TextAllowOverlap property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextAllowOverlap(Boolean value) {
    layer.setProperties(textAllowOverlap(value));
  }

  /**
   * Get the TextIgnorePlacement property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextIgnorePlacement() {
    return layer.getTextIgnorePlacement().value;
  }

  /**
   * Set the TextIgnorePlacement property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextIgnorePlacement(Boolean value) {
    layer.setProperties(textIgnorePlacement(value));
  }

  /**
   * Get the TextOptional property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextOptional() {
    return layer.getTextOptional().value;
  }

  /**
   * Set the TextOptional property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextOptional(Boolean value) {
    layer.setProperties(textOptional(value));
  }

  /**
   * Get the IconTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconTranslate() {
    return layer.getIconTranslate().value;
  }

  /**
   * Set the IconTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setIconTranslate(Float[] value) {
    layer.setProperties(iconTranslate(value));
  }

  /**
   * Get the IconTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getIconTranslateAnchor() {
    return layer.getIconTranslateAnchor().value;
  }

  /**
   * Set the IconTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setIconTranslateAnchor(String value) {
    layer.setProperties(iconTranslateAnchor(value));
  }

  /**
   * Get the TextTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getTextTranslate() {
    return layer.getTextTranslate().value;
  }

  /**
   * Set the TextTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setTextTranslate(Float[] value) {
    layer.setProperties(textTranslate(value));
  }

  /**
   * Get the TextTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getTextTranslateAnchor() {
    return layer.getTextTranslateAnchor().value;
  }

  /**
   * Set the TextTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setTextTranslateAnchor(String value) {
    layer.setProperties(textTranslateAnchor(value));
  }

  /**
   * Inner class for transforming map click events into symbol clicks
   */
  private class MapClickResolver implements MapboxMap.OnMapClickListener {

    private MapboxMap mapboxMap;

    private MapClickResolver(MapboxMap mapboxMap) {
      this.mapboxMap = mapboxMap;
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
      if (symbolClickListeners.isEmpty()) {
        return;
      }

      PointF screenLocation = mapboxMap.getProjection().toScreenLocation(point);
      List<Feature> features = mapboxMap.queryRenderedFeatures(screenLocation, ID_GEOJSON_LAYER);
      if (!features.isEmpty()) {
        long symbolId = features.get(0).getProperty(Symbol.ID_KEY).getAsLong();
        Symbol symbol = symbols.get(symbolId);
        if (symbol != null) {
          for (OnSymbolClickListener listener : symbolClickListeners) {
            listener.onSymbolClick(symbol);
          }
        }
      }
    }
  }

}
