// This file is generated.

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
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The symbol manager allows to add symbols to a map.
 */
public class SymbolManager extends AnnotationManager<Symbol, SymbolOptions, OnSymbolDragListener, OnSymbolClickListener, OnSymbolLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-symbol-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-symbol-layer";

  private SymbolLayer layer;

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   */
  @UiThread
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this(mapView, mapboxMap, null);
  }

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @Nullable String belowLayerId) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new SymbolLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      ), belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap     the map object to add symbols to
   * @param geoJsonSource the geojson source to add symbols to
   * @param layer         the symbol layer to visualise Symbols with
   */
  @VisibleForTesting
  public SymbolManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, @NonNull SymbolLayer layer, @Nullable String belowLayerId, DraggableAnnotationController<Symbol, OnSymbolDragListener> draggableAnnotationController) {
    super(mapboxMap, geoJsonSource, new SymbolComparator(), draggableAnnotationController);
    initLayer(layer, belowLayerId);
  }

  /**
   * Initialise the layer on the map.
   *
   * @param layer the layer to be added
   * @param belowLayerId the id of the layer above the circle layer
   */
  private void initLayer(@NonNull SymbolLayer layer, @Nullable String belowLayerId) {
    this.layer = layer;
    if (belowLayerId == null) {
      mapboxMap.addLayer(layer);
    } else {
      mapboxMap.addLayerBelow(layer, belowLayerId);
    }
  }

  /**
   * Get the layer id of the annotation layer.
   *
   * @return the layer id
   */
  @Override
  String getAnnotationLayerId() {
    return ID_GEOJSON_LAYER;
  }

  /**
   * Get the key of the id of the annotation.
   *
   * @return the key of the id of the annotation
   */
  @Override
  String getAnnotationIdKey() {
    return Symbol.ID_KEY;
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
      symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE)
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
  public void setSymbolPlacement(@Property.SYMBOL_PLACEMENT String value) {
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
  public void setSymbolSpacing( Float value) {
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
  public void setSymbolAvoidEdges( Boolean value) {
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
  public void setIconAllowOverlap( Boolean value) {
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
  public void setIconIgnorePlacement( Boolean value) {
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
  public void setIconOptional( Boolean value) {
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
  public void setIconRotationAlignment(@Property.ICON_ROTATION_ALIGNMENT String value) {
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
  public void setIconTextFit(@Property.ICON_TEXT_FIT String value) {
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
  public void setIconTextFitPadding( Float[] value) {
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
  public void setIconPadding( Float value) {
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
  public void setIconKeepUpright( Boolean value) {
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
  public void setIconPitchAlignment(@Property.ICON_PITCH_ALIGNMENT String value) {
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
  public void setTextPitchAlignment(@Property.TEXT_PITCH_ALIGNMENT String value) {
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
  public void setTextRotationAlignment(@Property.TEXT_ROTATION_ALIGNMENT String value) {
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
  public void setTextLineHeight( Float value) {
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
  public void setTextMaxAngle( Float value) {
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
  public void setTextPadding( Float value) {
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
  public void setTextKeepUpright( Boolean value) {
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
  public void setTextAllowOverlap( Boolean value) {
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
  public void setTextIgnorePlacement( Boolean value) {
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
  public void setTextOptional( Boolean value) {
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
  public void setIconTranslate( Float[] value) {
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
  public void setIconTranslateAnchor(@Property.ICON_TRANSLATE_ANCHOR String value) {
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
  public void setTextTranslate( Float[] value) {
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
  public void setTextTranslateAnchor(@Property.TEXT_TRANSLATE_ANCHOR String value) {
    layer.setProperties(textTranslateAnchor(value));
  }

}
