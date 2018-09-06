package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
  private GeoJsonSource geoJsonSource;
  private SymbolLayer symbolLayer;

  // callback listeners
  private OnSymbolClickListener symbolClickListener;

  // internal data set
  private final LongSparseArray<Symbol> symbols = new LongSparseArray<>();
  private final List<Feature> features = new ArrayList<>();
  private long currentMarkerId;

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
   * @param symbolLayer   the sybol layer to visualise symbols with
   */
  @VisibleForTesting
  public SymbolManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, SymbolLayer symbolLayer) {
    this.geoJsonSource = geoJsonSource;
    this.symbolLayer = symbolLayer;
    mapboxMap.addSource(geoJsonSource);
    mapboxMap.addLayer(symbolLayer);
    mapboxMap.addOnMapClickListener(new MapClickResolver(mapboxMap));
  }

  /**
   * Create a symbol on the map from a LatLng coordinate.
   *
   * @param latLng place to layout the symbol on the map
   * @return the newly created symbol
   */
  @UiThread
  public Symbol createSymbol(@NonNull LatLng latLng) {
    Symbol symbol = new Symbol(this, currentMarkerId);
    symbol.setLatLng(latLng);
    symbols.put(currentMarkerId, symbol);
    currentMarkerId++;
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
   * Set a callback to be invoked when a symbol has been clicked.
   * <p>
   * To unset, use a null argument.
   * </p>
   *
   * @param symbolClickListener the callback to be invoked when a symbol is clicked, or null to unset
   */
  public void setOnSymbolClickListener(@Nullable OnSymbolClickListener symbolClickListener) {
    this.symbolClickListener = symbolClickListener;
  }

  private static PropertyValue<?>[] getLayerDefinition() {
    return new PropertyValue[] {
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
    return symbolLayer.getSymbolPlacement().value;
  }

  /**
   * Set the SymbolPlacement property
   *
   * @param value property wrapper value around String
   */
  public void setSymbolPlacement(String value) {
    symbolLayer.setProperties(symbolPlacement(value));
  }

  /**
   * Get the SymbolSpacing property
   *
   * @return property wrapper value around Float
   */
  public Float getSymbolSpacing() {
    return symbolLayer.getSymbolSpacing().value;
  }

  /**
   * Set the SymbolSpacing property
   *
   * @param value property wrapper value around Float
   */
  public void setSymbolSpacing(Float value) {
    symbolLayer.setProperties(symbolSpacing(value));
  }

  /**
   * Get the SymbolAvoidEdges property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getSymbolAvoidEdges() {
    return symbolLayer.getSymbolAvoidEdges().value;
  }

  /**
   * Set the SymbolAvoidEdges property
   *
   * @param value property wrapper value around Boolean
   */
  public void setSymbolAvoidEdges(Boolean value) {
    symbolLayer.setProperties(symbolAvoidEdges(value));
  }

  /**
   * Get the IconAllowOverlap property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconAllowOverlap() {
    return symbolLayer.getIconAllowOverlap().value;
  }

  /**
   * Set the IconAllowOverlap property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconAllowOverlap(Boolean value) {
    symbolLayer.setProperties(iconAllowOverlap(value));
  }

  /**
   * Get the IconIgnorePlacement property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconIgnorePlacement() {
    return symbolLayer.getIconIgnorePlacement().value;
  }

  /**
   * Set the IconIgnorePlacement property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconIgnorePlacement(Boolean value) {
    symbolLayer.setProperties(iconIgnorePlacement(value));
  }

  /**
   * Get the IconOptional property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconOptional() {
    return symbolLayer.getIconOptional().value;
  }

  /**
   * Set the IconOptional property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconOptional(Boolean value) {
    symbolLayer.setProperties(iconOptional(value));
  }

  /**
   * Get the IconRotationAlignment property
   *
   * @return property wrapper value around String
   */
  public String getIconRotationAlignment() {
    return symbolLayer.getIconRotationAlignment().value;
  }

  /**
   * Set the IconRotationAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setIconRotationAlignment(String value) {
    symbolLayer.setProperties(iconRotationAlignment(value));
  }

  /**
   * Get the IconTextFit property
   *
   * @return property wrapper value around String
   */
  public String getIconTextFit() {
    return symbolLayer.getIconTextFit().value;
  }

  /**
   * Set the IconTextFit property
   *
   * @param value property wrapper value around String
   */
  public void setIconTextFit(String value) {
    symbolLayer.setProperties(iconTextFit(value));
  }

  /**
   * Get the IconTextFitPadding property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconTextFitPadding() {
    return symbolLayer.getIconTextFitPadding().value;
  }

  /**
   * Set the IconTextFitPadding property
   *
   * @param value property wrapper value around Float[]
   */
  public void setIconTextFitPadding(Float[] value) {
    symbolLayer.setProperties(iconTextFitPadding(value));
  }

  /**
   * Get the IconPadding property
   *
   * @return property wrapper value around Float
   */
  public Float getIconPadding() {
    return symbolLayer.getIconPadding().value;
  }

  /**
   * Set the IconPadding property
   *
   * @param value property wrapper value around Float
   */
  public void setIconPadding(Float value) {
    symbolLayer.setProperties(iconPadding(value));
  }

  /**
   * Get the IconKeepUpright property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconKeepUpright() {
    return symbolLayer.getIconKeepUpright().value;
  }

  /**
   * Set the IconKeepUpright property
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconKeepUpright(Boolean value) {
    symbolLayer.setProperties(iconKeepUpright(value));
  }

  /**
   * Get the IconPitchAlignment property
   *
   * @return property wrapper value around String
   */
  public String getIconPitchAlignment() {
    return symbolLayer.getIconPitchAlignment().value;
  }

  /**
   * Set the IconPitchAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setIconPitchAlignment(String value) {
    symbolLayer.setProperties(iconPitchAlignment(value));
  }

  /**
   * Get the TextPitchAlignment property
   *
   * @return property wrapper value around String
   */
  public String getTextPitchAlignment() {
    return symbolLayer.getTextPitchAlignment().value;
  }

  /**
   * Set the TextPitchAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setTextPitchAlignment(String value) {
    symbolLayer.setProperties(textPitchAlignment(value));
  }

  /**
   * Get the TextRotationAlignment property
   *
   * @return property wrapper value around String
   */
  public String getTextRotationAlignment() {
    return symbolLayer.getTextRotationAlignment().value;
  }

  /**
   * Set the TextRotationAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setTextRotationAlignment(String value) {
    symbolLayer.setProperties(textRotationAlignment(value));
  }

  /**
   * Get the TextLineHeight property
   *
   * @return property wrapper value around Float
   */
  public Float getTextLineHeight() {
    return symbolLayer.getTextLineHeight().value;
  }

  /**
   * Set the TextLineHeight property
   *
   * @param value property wrapper value around Float
   */
  public void setTextLineHeight(Float value) {
    symbolLayer.setProperties(textLineHeight(value));
  }

  /**
   * Get the TextMaxAngle property
   *
   * @return property wrapper value around Float
   */
  public Float getTextMaxAngle() {
    return symbolLayer.getTextMaxAngle().value;
  }

  /**
   * Set the TextMaxAngle property
   *
   * @param value property wrapper value around Float
   */
  public void setTextMaxAngle(Float value) {
    symbolLayer.setProperties(textMaxAngle(value));
  }

  /**
   * Get the TextPadding property
   *
   * @return property wrapper value around Float
   */
  public Float getTextPadding() {
    return symbolLayer.getTextPadding().value;
  }

  /**
   * Set the TextPadding property
   *
   * @param value property wrapper value around Float
   */
  public void setTextPadding(Float value) {
    symbolLayer.setProperties(textPadding(value));
  }

  /**
   * Get the TextKeepUpright property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextKeepUpright() {
    return symbolLayer.getTextKeepUpright().value;
  }

  /**
   * Set the TextKeepUpright property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextKeepUpright(Boolean value) {
    symbolLayer.setProperties(textKeepUpright(value));
  }

  /**
   * Get the TextAllowOverlap property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextAllowOverlap() {
    return symbolLayer.getTextAllowOverlap().value;
  }

  /**
   * Set the TextAllowOverlap property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextAllowOverlap(Boolean value) {
    symbolLayer.setProperties(textAllowOverlap(value));
  }

  /**
   * Get the TextIgnorePlacement property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextIgnorePlacement() {
    return symbolLayer.getTextIgnorePlacement().value;
  }

  /**
   * Set the TextIgnorePlacement property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextIgnorePlacement(Boolean value) {
    symbolLayer.setProperties(textIgnorePlacement(value));
  }

  /**
   * Get the TextOptional property
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextOptional() {
    return symbolLayer.getTextOptional().value;
  }

  /**
   * Set the TextOptional property
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextOptional(Boolean value) {
    symbolLayer.setProperties(textOptional(value));
  }

  /**
   * Get the IconTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconTranslate() {
    return symbolLayer.getIconTranslate().value;
  }

  /**
   * Set the IconTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setIconTranslate(Float[] value) {
    symbolLayer.setProperties(iconTranslate(value));
  }

  /**
   * Get the IconTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getIconTranslateAnchor() {
    return symbolLayer.getIconTranslateAnchor().value;
  }

  /**
   * Set the IconTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setIconTranslateAnchor(String value) {
    symbolLayer.setProperties(iconTranslateAnchor(value));
  }

  /**
   * Get the TextTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getTextTranslate() {
    return symbolLayer.getTextTranslate().value;
  }

  /**
   * Set the TextTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setTextTranslate(Float[] value) {
    symbolLayer.setProperties(textTranslate(value));
  }

  /**
   * Get the TextTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getTextTranslateAnchor() {
    return symbolLayer.getTextTranslateAnchor().value;
  }

  /**
   * Set the TextTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setTextTranslateAnchor(String value) {
    symbolLayer.setProperties(textTranslateAnchor(value));
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
      if (symbolClickListener == null) {
        return;
      }

      PointF screenLocation = mapboxMap.getProjection().toScreenLocation(point);
      List<Feature> features = mapboxMap.queryRenderedFeatures(screenLocation, ID_GEOJSON_LAYER);
      if (!features.isEmpty()) {
        long symbolId = features.get(0).getProperty(Symbol.ID_KEY).getAsLong();
        Symbol symbol = symbols.get(symbolId);
        if (symbol != null) {
          symbolClickListener.onSymbolClick(symbols.get(symbolId));
        }
      }
    }
  }

}
