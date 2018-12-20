// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.Symbol.Z_INDEX;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The symbol manager allows to add symbols to a map.
 */
public class SymbolManager extends AnnotationManager<SymbolLayer, Symbol, SymbolOptions, OnSymbolDragListener, OnSymbolClickListener, OnSymbolLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-symbol-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-symbol-layer";

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null);
  }

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId) {
    this(mapView, mapboxMap, style,
      new CoreElementProvider<SymbolLayer>() {
        @Override
        public SymbolLayer getLayer() {
          return new SymbolLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE);
        }

        @Override
        public GeoJsonSource getSource() {
          return new GeoJsonSource(ID_GEOJSON_SOURCE);
        }
      },
     belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap     the map object to add symbols to
   * @param style a valid a fully loaded style object
   */
  @VisibleForTesting
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<SymbolLayer> coreElementProvider, @Nullable String belowLayerId, DraggableAnnotationController<Symbol, OnSymbolDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, new SymbolComparator(), draggableAnnotationController, belowLayerId);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put("icon-size", false);
    dataDrivenPropertyUsageMap.put("icon-image", false);
    dataDrivenPropertyUsageMap.put("icon-rotate", false);
    dataDrivenPropertyUsageMap.put("icon-offset", false);
    dataDrivenPropertyUsageMap.put("icon-anchor", false);
    dataDrivenPropertyUsageMap.put("text-field", false);
    dataDrivenPropertyUsageMap.put("text-font", false);
    dataDrivenPropertyUsageMap.put("text-size", false);
    dataDrivenPropertyUsageMap.put("text-max-width", false);
    dataDrivenPropertyUsageMap.put("text-letter-spacing", false);
    dataDrivenPropertyUsageMap.put("text-justify", false);
    dataDrivenPropertyUsageMap.put("text-anchor", false);
    dataDrivenPropertyUsageMap.put("text-rotate", false);
    dataDrivenPropertyUsageMap.put("text-transform", false);
    dataDrivenPropertyUsageMap.put("text-offset", false);
    dataDrivenPropertyUsageMap.put("icon-opacity", false);
    dataDrivenPropertyUsageMap.put("icon-color", false);
    dataDrivenPropertyUsageMap.put("icon-halo-color", false);
    dataDrivenPropertyUsageMap.put("icon-halo-width", false);
    dataDrivenPropertyUsageMap.put("icon-halo-blur", false);
    dataDrivenPropertyUsageMap.put("text-opacity", false);
    dataDrivenPropertyUsageMap.put("text-color", false);
    dataDrivenPropertyUsageMap.put("text-halo-color", false);
    dataDrivenPropertyUsageMap.put("text-halo-width", false);
    dataDrivenPropertyUsageMap.put("text-halo-blur", false);
    dataDrivenPropertyUsageMap.put(Z_INDEX, false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case "icon-size":
        layer.setProperties(iconSize(get("icon-size")));
        break;
      case "icon-image":
        layer.setProperties(iconImage(get("icon-image")));
        break;
      case "icon-rotate":
        layer.setProperties(iconRotate(get("icon-rotate")));
        break;
      case "icon-offset":
        layer.setProperties(iconOffset(get("icon-offset")));
        break;
      case "icon-anchor":
        layer.setProperties(iconAnchor(get("icon-anchor")));
        break;
      case "text-field":
        layer.setProperties(textField(get("text-field")));
        break;
      case "text-font":
        layer.setProperties(textFont(get("text-font")));
        break;
      case "text-size":
        layer.setProperties(textSize(get("text-size")));
        break;
      case "text-max-width":
        layer.setProperties(textMaxWidth(get("text-max-width")));
        break;
      case "text-letter-spacing":
        layer.setProperties(textLetterSpacing(get("text-letter-spacing")));
        break;
      case "text-justify":
        layer.setProperties(textJustify(get("text-justify")));
        break;
      case "text-anchor":
        layer.setProperties(textAnchor(get("text-anchor")));
        break;
      case "text-rotate":
        layer.setProperties(textRotate(get("text-rotate")));
        break;
      case "text-transform":
        layer.setProperties(textTransform(get("text-transform")));
        break;
      case "text-offset":
        layer.setProperties(textOffset(get("text-offset")));
        break;
      case "icon-opacity":
        layer.setProperties(iconOpacity(get("icon-opacity")));
        break;
      case "icon-color":
        layer.setProperties(iconColor(get("icon-color")));
        break;
      case "icon-halo-color":
        layer.setProperties(iconHaloColor(get("icon-halo-color")));
        break;
      case "icon-halo-width":
        layer.setProperties(iconHaloWidth(get("icon-halo-width")));
        break;
      case "icon-halo-blur":
        layer.setProperties(iconHaloBlur(get("icon-halo-blur")));
        break;
      case "text-opacity":
        layer.setProperties(textOpacity(get("text-opacity")));
        break;
      case "text-color":
        layer.setProperties(textColor(get("text-color")));
        break;
      case "text-halo-color":
        layer.setProperties(textHaloColor(get("text-halo-color")));
        break;
      case "text-halo-width":
        layer.setProperties(textHaloWidth(get("text-halo-width")));
        break;
      case "text-halo-blur":
        layer.setProperties(textHaloBlur(get("text-halo-blur")));
        break;
      case Z_INDEX:
        layer.setProperties(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE));
        break;
    }
  }

  /**
   * Create a list of symbols on the map.
   * <p>
   * Symbols are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link SymbolOptions#fromFeature(Feature)}.
   *
   * @param json the GeoJSON defining the list of symbols to build
   * @return the list of built symbols
   */
  @UiThread
  public List<Symbol> create(@NonNull String json) {
    return create(FeatureCollection.fromJson(json));
  }

  /**
   * Create a list of symbols on the map.
   * <p>
   * Symbols are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link SymbolOptions#fromFeature(Feature)}.
   *
   * @param featureCollection the featureCollection defining the list of symbols to build
   * @return the list of built symbols
   */
  @UiThread
  public List<Symbol> create(@NonNull FeatureCollection featureCollection) {
    List<Feature> features = featureCollection.features();
    List<SymbolOptions> options = new ArrayList<>();
    if (features != null) {
      for (Feature feature : features) {
        SymbolOptions option = SymbolOptions.fromFeature(feature);
        if (option != null) {
          options.add(option);
        }
      }
    }
    return create(options);
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
    PropertyValue propertyValue = symbolPlacement(value);
    constantPropertyUsageMap.put("symbol-placement", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = symbolSpacing(value);
    constantPropertyUsageMap.put("symbol-spacing", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = symbolAvoidEdges(value);
    constantPropertyUsageMap.put("symbol-avoid-edges", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconAllowOverlap(value);
    constantPropertyUsageMap.put("icon-allow-overlap", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconIgnorePlacement(value);
    constantPropertyUsageMap.put("icon-ignore-placement", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconOptional(value);
    constantPropertyUsageMap.put("icon-optional", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconRotationAlignment(value);
    constantPropertyUsageMap.put("icon-rotation-alignment", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconTextFit(value);
    constantPropertyUsageMap.put("icon-text-fit", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconTextFitPadding(value);
    constantPropertyUsageMap.put("icon-text-fit-padding", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconPadding(value);
    constantPropertyUsageMap.put("icon-padding", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconKeepUpright(value);
    constantPropertyUsageMap.put("icon-keep-upright", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconPitchAlignment(value);
    constantPropertyUsageMap.put("icon-pitch-alignment", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textPitchAlignment(value);
    constantPropertyUsageMap.put("text-pitch-alignment", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textRotationAlignment(value);
    constantPropertyUsageMap.put("text-rotation-alignment", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textLineHeight(value);
    constantPropertyUsageMap.put("text-line-height", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textMaxAngle(value);
    constantPropertyUsageMap.put("text-max-angle", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textPadding(value);
    constantPropertyUsageMap.put("text-padding", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textKeepUpright(value);
    constantPropertyUsageMap.put("text-keep-upright", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textAllowOverlap(value);
    constantPropertyUsageMap.put("text-allow-overlap", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textIgnorePlacement(value);
    constantPropertyUsageMap.put("text-ignore-placement", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textOptional(value);
    constantPropertyUsageMap.put("text-optional", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconTranslate(value);
    constantPropertyUsageMap.put("icon-translate", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = iconTranslateAnchor(value);
    constantPropertyUsageMap.put("icon-translate-anchor", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textTranslate(value);
    constantPropertyUsageMap.put("text-translate", propertyValue);
    layer.setProperties(propertyValue);
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
    PropertyValue propertyValue = textTranslateAnchor(value);
    constantPropertyUsageMap.put("text-translate-anchor", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Set filter on the managed symbols.
   *
   * @param expression expression
   */
   @Override
  public void setFilter(@NonNull Expression expression) {
    layerFilter = expression;
    layer.setFilter(layerFilter);
  }

  /**
   * Get filter of the managed symbols.
   *
   * @return expression
   */
  @Nullable
  public Expression getFilter() {
    return layer.getFilter();
  }
}
