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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The symbol manager allows to add symbols to a map.
 */
public class SymbolManager extends AnnotationManager<SymbolLayer, Symbol, SymbolOptions, OnSymbolDragListener, OnSymbolClickListener, OnSymbolLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-symbol-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-symbol-layer";

  private static final String PROPERTY_symbolPlacement = "symbol-placement";
  private static final String PROPERTY_symbolSpacing = "symbol-spacing";
  private static final String PROPERTY_symbolAvoidEdges = "symbol-avoid-edges";
  private static final String PROPERTY_iconAllowOverlap = "icon-allow-overlap";
  private static final String PROPERTY_iconIgnorePlacement = "icon-ignore-placement";
  private static final String PROPERTY_iconOptional = "icon-optional";
  private static final String PROPERTY_iconRotationAlignment = "icon-rotation-alignment";
  private static final String PROPERTY_iconTextFit = "icon-text-fit";
  private static final String PROPERTY_iconTextFitPadding = "icon-text-fit-padding";
  private static final String PROPERTY_iconPadding = "icon-padding";
  private static final String PROPERTY_iconKeepUpright = "icon-keep-upright";
  private static final String PROPERTY_iconPitchAlignment = "icon-pitch-alignment";
  private static final String PROPERTY_textPitchAlignment = "text-pitch-alignment";
  private static final String PROPERTY_textRotationAlignment = "text-rotation-alignment";
  private static final String PROPERTY_textLineHeight = "text-line-height";
  private static final String PROPERTY_textMaxAngle = "text-max-angle";
  private static final String PROPERTY_textPadding = "text-padding";
  private static final String PROPERTY_textKeepUpright = "text-keep-upright";
  private static final String PROPERTY_textAllowOverlap = "text-allow-overlap";
  private static final String PROPERTY_textIgnorePlacement = "text-ignore-placement";
  private static final String PROPERTY_textOptional = "text-optional";
  private static final String PROPERTY_iconTranslate = "icon-translate";
  private static final String PROPERTY_iconTranslateAnchor = "icon-translate-anchor";
  private static final String PROPERTY_textTranslate = "text-translate";
  private static final String PROPERTY_textTranslateAnchor = "text-translate-anchor";

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null, null);
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
    this(mapView, mapboxMap, style, belowLayerId, null);
  }

  /**
   * Create a symbol manager, used to manage symbols.
   *
   * @param mapboxMap the map object to add symbols to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   * @param geoJsonOptions options for the internal source
   */
  @UiThread
  public SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions) {
    this(mapView, mapboxMap, style,
      new CoreElementProvider<SymbolLayer>() {
        @Override
        public SymbolLayer getLayer() {
          return new SymbolLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE);
        }

        @Override
        public GeoJsonSource getSource(@Nullable GeoJsonOptions geoJsonOptions) {
          if (geoJsonOptions != null) {
            return new GeoJsonSource(ID_GEOJSON_SOURCE, geoJsonOptions);
          } else {
            return new GeoJsonSource(ID_GEOJSON_SOURCE);
          }
        }
      },
     belowLayerId, geoJsonOptions, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  @VisibleForTesting
  SymbolManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<SymbolLayer> coreElementProvider, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions, DraggableAnnotationController<Symbol, OnSymbolDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, new SymbolComparator(), draggableAnnotationController, belowLayerId, geoJsonOptions);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconSize, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconImage, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconRotate, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconOffset, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconAnchor, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textField, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textFont, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textSize, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textMaxWidth, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textLetterSpacing, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textJustify, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textAnchor, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textRotate, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textTransform, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textOffset, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconOpacity, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconColor, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconHaloColor, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconHaloWidth, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_iconHaloBlur, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textOpacity, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textColor, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textHaloColor, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textHaloWidth, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_textHaloBlur, false);
    dataDrivenPropertyUsageMap.put(SymbolOptions.PROPERTY_zIndex, false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case SymbolOptions.PROPERTY_iconSize:
        layer.setProperties(iconSize(get(SymbolOptions.PROPERTY_iconSize)));
        break;
      case SymbolOptions.PROPERTY_iconImage:
        layer.setProperties(iconImage(get(SymbolOptions.PROPERTY_iconImage)));
        break;
      case SymbolOptions.PROPERTY_iconRotate:
        layer.setProperties(iconRotate(get(SymbolOptions.PROPERTY_iconRotate)));
        break;
      case SymbolOptions.PROPERTY_iconOffset:
        layer.setProperties(iconOffset(get(SymbolOptions.PROPERTY_iconOffset)));
        break;
      case SymbolOptions.PROPERTY_iconAnchor:
        layer.setProperties(iconAnchor(get(SymbolOptions.PROPERTY_iconAnchor)));
        break;
      case SymbolOptions.PROPERTY_textField:
        layer.setProperties(textField(get(SymbolOptions.PROPERTY_textField)));
        break;
      case SymbolOptions.PROPERTY_textFont:
        layer.setProperties(textFont(get(SymbolOptions.PROPERTY_textFont)));
        break;
      case SymbolOptions.PROPERTY_textSize:
        layer.setProperties(textSize(get(SymbolOptions.PROPERTY_textSize)));
        break;
      case SymbolOptions.PROPERTY_textMaxWidth:
        layer.setProperties(textMaxWidth(get(SymbolOptions.PROPERTY_textMaxWidth)));
        break;
      case SymbolOptions.PROPERTY_textLetterSpacing:
        layer.setProperties(textLetterSpacing(get(SymbolOptions.PROPERTY_textLetterSpacing)));
        break;
      case SymbolOptions.PROPERTY_textJustify:
        layer.setProperties(textJustify(get(SymbolOptions.PROPERTY_textJustify)));
        break;
      case SymbolOptions.PROPERTY_textAnchor:
        layer.setProperties(textAnchor(get(SymbolOptions.PROPERTY_textAnchor)));
        break;
      case SymbolOptions.PROPERTY_textRotate:
        layer.setProperties(textRotate(get(SymbolOptions.PROPERTY_textRotate)));
        break;
      case SymbolOptions.PROPERTY_textTransform:
        layer.setProperties(textTransform(get(SymbolOptions.PROPERTY_textTransform)));
        break;
      case SymbolOptions.PROPERTY_textOffset:
        layer.setProperties(textOffset(get(SymbolOptions.PROPERTY_textOffset)));
        break;
      case SymbolOptions.PROPERTY_iconOpacity:
        layer.setProperties(iconOpacity(get(SymbolOptions.PROPERTY_iconOpacity)));
        break;
      case SymbolOptions.PROPERTY_iconColor:
        layer.setProperties(iconColor(get(SymbolOptions.PROPERTY_iconColor)));
        break;
      case SymbolOptions.PROPERTY_iconHaloColor:
        layer.setProperties(iconHaloColor(get(SymbolOptions.PROPERTY_iconHaloColor)));
        break;
      case SymbolOptions.PROPERTY_iconHaloWidth:
        layer.setProperties(iconHaloWidth(get(SymbolOptions.PROPERTY_iconHaloWidth)));
        break;
      case SymbolOptions.PROPERTY_iconHaloBlur:
        layer.setProperties(iconHaloBlur(get(SymbolOptions.PROPERTY_iconHaloBlur)));
        break;
      case SymbolOptions.PROPERTY_textOpacity:
        layer.setProperties(textOpacity(get(SymbolOptions.PROPERTY_textOpacity)));
        break;
      case SymbolOptions.PROPERTY_textColor:
        layer.setProperties(textColor(get(SymbolOptions.PROPERTY_textColor)));
        break;
      case SymbolOptions.PROPERTY_textHaloColor:
        layer.setProperties(textHaloColor(get(SymbolOptions.PROPERTY_textHaloColor)));
        break;
      case SymbolOptions.PROPERTY_textHaloWidth:
        layer.setProperties(textHaloWidth(get(SymbolOptions.PROPERTY_textHaloWidth)));
        break;
      case SymbolOptions.PROPERTY_textHaloBlur:
        layer.setProperties(textHaloBlur(get(SymbolOptions.PROPERTY_textHaloBlur)));
        break;
      case SymbolOptions.PROPERTY_zIndex:
        layer.setProperties(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE));
        break;
    }
  }

  /**
   * Create a list of symbols on the map.
   * <p>
   * Symbols are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * SymbolOptions.PROPERTY_iconSize - Float<br>
   * SymbolOptions.PROPERTY_iconImage - String<br>
   * SymbolOptions.PROPERTY_iconRotate - Float<br>
   * SymbolOptions.PROPERTY_iconOffset - Float[]<br>
   * SymbolOptions.PROPERTY_iconAnchor - String<br>
   * SymbolOptions.PROPERTY_textField - String<br>
   * SymbolOptions.PROPERTY_textFont - String[]<br>
   * SymbolOptions.PROPERTY_textSize - Float<br>
   * SymbolOptions.PROPERTY_textMaxWidth - Float<br>
   * SymbolOptions.PROPERTY_textLetterSpacing - Float<br>
   * SymbolOptions.PROPERTY_textJustify - String<br>
   * SymbolOptions.PROPERTY_textAnchor - String<br>
   * SymbolOptions.PROPERTY_textRotate - Float<br>
   * SymbolOptions.PROPERTY_textTransform - String<br>
   * SymbolOptions.PROPERTY_textOffset - Float[]<br>
   * SymbolOptions.PROPERTY_iconOpacity - Float<br>
   * SymbolOptions.PROPERTY_iconColor - String<br>
   * SymbolOptions.PROPERTY_iconHaloColor - String<br>
   * SymbolOptions.PROPERTY_iconHaloWidth - Float<br>
   * SymbolOptions.PROPERTY_iconHaloBlur - Float<br>
   * SymbolOptions.PROPERTY_textOpacity - Float<br>
   * SymbolOptions.PROPERTY_textColor - String<br>
   * SymbolOptions.PROPERTY_textHaloColor - String<br>
   * SymbolOptions.PROPERTY_textHaloWidth - Float<br>
   * SymbolOptions.PROPERTY_textHaloBlur - Float<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "z-index" - Integer, z-index of the feature within the manager<br>
   * "is-draggable" - Boolean, true if the symbol should be draggable, false otherwise
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
   * All supported properties are:<br>
   * SymbolOptions.PROPERTY_iconSize - Float<br>
   * SymbolOptions.PROPERTY_iconImage - String<br>
   * SymbolOptions.PROPERTY_iconRotate - Float<br>
   * SymbolOptions.PROPERTY_iconOffset - Float[]<br>
   * SymbolOptions.PROPERTY_iconAnchor - String<br>
   * SymbolOptions.PROPERTY_textField - String<br>
   * SymbolOptions.PROPERTY_textFont - String[]<br>
   * SymbolOptions.PROPERTY_textSize - Float<br>
   * SymbolOptions.PROPERTY_textMaxWidth - Float<br>
   * SymbolOptions.PROPERTY_textLetterSpacing - Float<br>
   * SymbolOptions.PROPERTY_textJustify - String<br>
   * SymbolOptions.PROPERTY_textAnchor - String<br>
   * SymbolOptions.PROPERTY_textRotate - Float<br>
   * SymbolOptions.PROPERTY_textTransform - String<br>
   * SymbolOptions.PROPERTY_textOffset - Float[]<br>
   * SymbolOptions.PROPERTY_iconOpacity - Float<br>
   * SymbolOptions.PROPERTY_iconColor - String<br>
   * SymbolOptions.PROPERTY_iconHaloColor - String<br>
   * SymbolOptions.PROPERTY_iconHaloWidth - Float<br>
   * SymbolOptions.PROPERTY_iconHaloBlur - Float<br>
   * SymbolOptions.PROPERTY_textOpacity - Float<br>
   * SymbolOptions.PROPERTY_textColor - String<br>
   * SymbolOptions.PROPERTY_textHaloColor - String<br>
   * SymbolOptions.PROPERTY_textHaloWidth - Float<br>
   * SymbolOptions.PROPERTY_textHaloBlur - Float<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "z-index" - Integer, z-index of the feature within the manager<br>
   * "is-draggable" - Boolean, true if the symbol should be draggable, false otherwise
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
   * <p>
   * Label placement relative to its geometry.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getSymbolPlacement() {
    return layer.getSymbolPlacement().value;
  }

  /**
   * Set the SymbolPlacement property
   * <p>
   * Label placement relative to its geometry.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setSymbolPlacement(@Property.SYMBOL_PLACEMENT String value) {
    PropertyValue propertyValue = symbolPlacement(value);
    constantPropertyUsageMap.put(PROPERTY_symbolPlacement, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the SymbolSpacing property
   * <p>
   * Distance between two symbol anchors.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getSymbolSpacing() {
    return layer.getSymbolSpacing().value;
  }

  /**
   * Set the SymbolSpacing property
   * <p>
   * Distance between two symbol anchors.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setSymbolSpacing( Float value) {
    PropertyValue propertyValue = symbolSpacing(value);
    constantPropertyUsageMap.put(PROPERTY_symbolSpacing, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the SymbolAvoidEdges property
   * <p>
   * If true, the symbols will not cross tile edges to avoid mutual collisions. Recommended in layers that don't have enough padding in the vector tile to prevent collisions, or if it is a point symbol layer placed after a line symbol layer.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getSymbolAvoidEdges() {
    return layer.getSymbolAvoidEdges().value;
  }

  /**
   * Set the SymbolAvoidEdges property
   * <p>
   * If true, the symbols will not cross tile edges to avoid mutual collisions. Recommended in layers that don't have enough padding in the vector tile to prevent collisions, or if it is a point symbol layer placed after a line symbol layer.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setSymbolAvoidEdges( Boolean value) {
    PropertyValue propertyValue = symbolAvoidEdges(value);
    constantPropertyUsageMap.put(PROPERTY_symbolAvoidEdges, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconAllowOverlap property
   * <p>
   * If true, the icon will be visible even if it collides with other previously drawn symbols.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconAllowOverlap() {
    return layer.getIconAllowOverlap().value;
  }

  /**
   * Set the IconAllowOverlap property
   * <p>
   * If true, the icon will be visible even if it collides with other previously drawn symbols.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconAllowOverlap( Boolean value) {
    PropertyValue propertyValue = iconAllowOverlap(value);
    constantPropertyUsageMap.put(PROPERTY_iconAllowOverlap, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconIgnorePlacement property
   * <p>
   * If true, other symbols can be visible even if they collide with the icon.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconIgnorePlacement() {
    return layer.getIconIgnorePlacement().value;
  }

  /**
   * Set the IconIgnorePlacement property
   * <p>
   * If true, other symbols can be visible even if they collide with the icon.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconIgnorePlacement( Boolean value) {
    PropertyValue propertyValue = iconIgnorePlacement(value);
    constantPropertyUsageMap.put(PROPERTY_iconIgnorePlacement, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconOptional property
   * <p>
   * If true, text will display without their corresponding icons when the icon collides with other symbols and the text does not.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconOptional() {
    return layer.getIconOptional().value;
  }

  /**
   * Set the IconOptional property
   * <p>
   * If true, text will display without their corresponding icons when the icon collides with other symbols and the text does not.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconOptional( Boolean value) {
    PropertyValue propertyValue = iconOptional(value);
    constantPropertyUsageMap.put(PROPERTY_iconOptional, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconRotationAlignment property
   * <p>
   * In combination with {@link Property.SYMBOL_PLACEMENT}, determines the rotation behavior of icons.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getIconRotationAlignment() {
    return layer.getIconRotationAlignment().value;
  }

  /**
   * Set the IconRotationAlignment property
   * <p>
   * In combination with {@link Property.SYMBOL_PLACEMENT}, determines the rotation behavior of icons.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setIconRotationAlignment(@Property.ICON_ROTATION_ALIGNMENT String value) {
    PropertyValue propertyValue = iconRotationAlignment(value);
    constantPropertyUsageMap.put(PROPERTY_iconRotationAlignment, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconTextFit property
   * <p>
   * Scales the icon to fit around the associated text.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getIconTextFit() {
    return layer.getIconTextFit().value;
  }

  /**
   * Set the IconTextFit property
   * <p>
   * Scales the icon to fit around the associated text.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setIconTextFit(@Property.ICON_TEXT_FIT String value) {
    PropertyValue propertyValue = iconTextFit(value);
    constantPropertyUsageMap.put(PROPERTY_iconTextFit, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconTextFitPadding property
   * <p>
   * Size of the additional area added to dimensions determined by {@link Property.ICON_TEXT_FIT}, in clockwise order: top, right, bottom, left.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconTextFitPadding() {
    return layer.getIconTextFitPadding().value;
  }

  /**
   * Set the IconTextFitPadding property
   * <p>
   * Size of the additional area added to dimensions determined by {@link Property.ICON_TEXT_FIT}, in clockwise order: top, right, bottom, left.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setIconTextFitPadding( Float[] value) {
    PropertyValue propertyValue = iconTextFitPadding(value);
    constantPropertyUsageMap.put(PROPERTY_iconTextFitPadding, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconPadding property
   * <p>
   * Size of the additional area around the icon bounding box used for detecting symbol collisions.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getIconPadding() {
    return layer.getIconPadding().value;
  }

  /**
   * Set the IconPadding property
   * <p>
   * Size of the additional area around the icon bounding box used for detecting symbol collisions.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setIconPadding( Float value) {
    PropertyValue propertyValue = iconPadding(value);
    constantPropertyUsageMap.put(PROPERTY_iconPadding, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconKeepUpright property
   * <p>
   * If true, the icon may be flipped to prevent it from being rendered upside-down.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getIconKeepUpright() {
    return layer.getIconKeepUpright().value;
  }

  /**
   * Set the IconKeepUpright property
   * <p>
   * If true, the icon may be flipped to prevent it from being rendered upside-down.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setIconKeepUpright( Boolean value) {
    PropertyValue propertyValue = iconKeepUpright(value);
    constantPropertyUsageMap.put(PROPERTY_iconKeepUpright, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconPitchAlignment property
   * <p>
   * Orientation of icon when map is pitched.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getIconPitchAlignment() {
    return layer.getIconPitchAlignment().value;
  }

  /**
   * Set the IconPitchAlignment property
   * <p>
   * Orientation of icon when map is pitched.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setIconPitchAlignment(@Property.ICON_PITCH_ALIGNMENT String value) {
    PropertyValue propertyValue = iconPitchAlignment(value);
    constantPropertyUsageMap.put(PROPERTY_iconPitchAlignment, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextPitchAlignment property
   * <p>
   * Orientation of text when map is pitched.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextPitchAlignment() {
    return layer.getTextPitchAlignment().value;
  }

  /**
   * Set the TextPitchAlignment property
   * <p>
   * Orientation of text when map is pitched.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setTextPitchAlignment(@Property.TEXT_PITCH_ALIGNMENT String value) {
    PropertyValue propertyValue = textPitchAlignment(value);
    constantPropertyUsageMap.put(PROPERTY_textPitchAlignment, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextRotationAlignment property
   * <p>
   * In combination with {@link Property.SYMBOL_PLACEMENT}, determines the rotation behavior of the individual glyphs forming the text.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextRotationAlignment() {
    return layer.getTextRotationAlignment().value;
  }

  /**
   * Set the TextRotationAlignment property
   * <p>
   * In combination with {@link Property.SYMBOL_PLACEMENT}, determines the rotation behavior of the individual glyphs forming the text.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setTextRotationAlignment(@Property.TEXT_ROTATION_ALIGNMENT String value) {
    PropertyValue propertyValue = textRotationAlignment(value);
    constantPropertyUsageMap.put(PROPERTY_textRotationAlignment, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextLineHeight property
   * <p>
   * Text leading value for multi-line text.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextLineHeight() {
    return layer.getTextLineHeight().value;
  }

  /**
   * Set the TextLineHeight property
   * <p>
   * Text leading value for multi-line text.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setTextLineHeight( Float value) {
    PropertyValue propertyValue = textLineHeight(value);
    constantPropertyUsageMap.put(PROPERTY_textLineHeight, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextMaxAngle property
   * <p>
   * Maximum angle change between adjacent characters.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextMaxAngle() {
    return layer.getTextMaxAngle().value;
  }

  /**
   * Set the TextMaxAngle property
   * <p>
   * Maximum angle change between adjacent characters.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setTextMaxAngle( Float value) {
    PropertyValue propertyValue = textMaxAngle(value);
    constantPropertyUsageMap.put(PROPERTY_textMaxAngle, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextPadding property
   * <p>
   * Size of the additional area around the text bounding box used for detecting symbol collisions.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextPadding() {
    return layer.getTextPadding().value;
  }

  /**
   * Set the TextPadding property
   * <p>
   * Size of the additional area around the text bounding box used for detecting symbol collisions.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setTextPadding( Float value) {
    PropertyValue propertyValue = textPadding(value);
    constantPropertyUsageMap.put(PROPERTY_textPadding, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextKeepUpright property
   * <p>
   * If true, the text may be flipped vertically to prevent it from being rendered upside-down.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextKeepUpright() {
    return layer.getTextKeepUpright().value;
  }

  /**
   * Set the TextKeepUpright property
   * <p>
   * If true, the text may be flipped vertically to prevent it from being rendered upside-down.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextKeepUpright( Boolean value) {
    PropertyValue propertyValue = textKeepUpright(value);
    constantPropertyUsageMap.put(PROPERTY_textKeepUpright, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextAllowOverlap property
   * <p>
   * If true, the text will be visible even if it collides with other previously drawn symbols.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextAllowOverlap() {
    return layer.getTextAllowOverlap().value;
  }

  /**
   * Set the TextAllowOverlap property
   * <p>
   * If true, the text will be visible even if it collides with other previously drawn symbols.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextAllowOverlap( Boolean value) {
    PropertyValue propertyValue = textAllowOverlap(value);
    constantPropertyUsageMap.put(PROPERTY_textAllowOverlap, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextIgnorePlacement property
   * <p>
   * If true, other symbols can be visible even if they collide with the text.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextIgnorePlacement() {
    return layer.getTextIgnorePlacement().value;
  }

  /**
   * Set the TextIgnorePlacement property
   * <p>
   * If true, other symbols can be visible even if they collide with the text.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextIgnorePlacement( Boolean value) {
    PropertyValue propertyValue = textIgnorePlacement(value);
    constantPropertyUsageMap.put(PROPERTY_textIgnorePlacement, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextOptional property
   * <p>
   * If true, icons will display without their corresponding text when the text collides with other symbols and the icon does not.
   * </p>
   *
   * @return property wrapper value around Boolean
   */
  public Boolean getTextOptional() {
    return layer.getTextOptional().value;
  }

  /**
   * Set the TextOptional property
   * <p>
   * If true, icons will display without their corresponding text when the text collides with other symbols and the icon does not.
   * </p>
   *
   * @param value property wrapper value around Boolean
   */
  public void setTextOptional( Boolean value) {
    PropertyValue propertyValue = textOptional(value);
    constantPropertyUsageMap.put(PROPERTY_textOptional, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconTranslate property
   * <p>
   * Distance that the icon's anchor is moved from its original placement. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconTranslate() {
    return layer.getIconTranslate().value;
  }

  /**
   * Set the IconTranslate property
   * <p>
   * Distance that the icon's anchor is moved from its original placement. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setIconTranslate( Float[] value) {
    PropertyValue propertyValue = iconTranslate(value);
    constantPropertyUsageMap.put(PROPERTY_iconTranslate, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the IconTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#iconTranslate}.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getIconTranslateAnchor() {
    return layer.getIconTranslateAnchor().value;
  }

  /**
   * Set the IconTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#iconTranslate}.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setIconTranslateAnchor(@Property.ICON_TRANSLATE_ANCHOR String value) {
    PropertyValue propertyValue = iconTranslateAnchor(value);
    constantPropertyUsageMap.put(PROPERTY_iconTranslateAnchor, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextTranslate property
   * <p>
   * Distance that the text's anchor is moved from its original placement. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getTextTranslate() {
    return layer.getTextTranslate().value;
  }

  /**
   * Set the TextTranslate property
   * <p>
   * Distance that the text's anchor is moved from its original placement. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setTextTranslate( Float[] value) {
    PropertyValue propertyValue = textTranslate(value);
    constantPropertyUsageMap.put(PROPERTY_textTranslate, propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the TextTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#textTranslate}.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextTranslateAnchor() {
    return layer.getTextTranslateAnchor().value;
  }

  /**
   * Set the TextTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#textTranslate}.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setTextTranslateAnchor(@Property.TEXT_TRANSLATE_ANCHOR String value) {
    PropertyValue propertyValue = textTranslateAnchor(value);
    constantPropertyUsageMap.put(PROPERTY_textTranslateAnchor, propertyValue);
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
