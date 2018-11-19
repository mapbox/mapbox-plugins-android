// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

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
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new SymbolLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE),
    belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
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
    super(mapboxMap, layer, geoJsonSource, new SymbolComparator(), draggableAnnotationController, belowLayerId);
    initializeDataDrivenPropertyMap();
  }

  private void initializeDataDrivenPropertyMap() {
    propertyUsageMap.put("icon-size", false);
    propertyUsageMap.put("icon-image", false);
    propertyUsageMap.put("icon-rotate", false);
    propertyUsageMap.put("icon-offset", false);
    propertyUsageMap.put("icon-anchor", false);
    propertyUsageMap.put("text-field", false);
    propertyUsageMap.put("text-font", false);
    propertyUsageMap.put("text-size", false);
    propertyUsageMap.put("text-max-width", false);
    propertyUsageMap.put("text-letter-spacing", false);
    propertyUsageMap.put("text-justify", false);
    propertyUsageMap.put("text-anchor", false);
    propertyUsageMap.put("text-rotate", false);
    propertyUsageMap.put("text-transform", false);
    propertyUsageMap.put("text-offset", false);
    propertyUsageMap.put("icon-opacity", false);
    propertyUsageMap.put("icon-color", false);
    propertyUsageMap.put("icon-halo-color", false);
    propertyUsageMap.put("icon-halo-width", false);
    propertyUsageMap.put("icon-halo-blur", false);
    propertyUsageMap.put("text-opacity", false);
    propertyUsageMap.put("text-color", false);
    propertyUsageMap.put("text-halo-color", false);
    propertyUsageMap.put("text-halo-width", false);
    propertyUsageMap.put("text-halo-blur", false);
    propertyUsageMap.put(Z_INDEX, false);
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
   * Get the SymbolZOrder expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getSymbolZOrderExpression() {
    return layer.getSymbolZOrder().getExpression();
  }

  /**
   * Set the SymbolZOrder expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setSymbolZOrderExpression(@NonNull Expression expression) {
    layer.setProperties(symbolZOrder(expression));
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
   * Get the IconSize expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getIconSizeExpression() {
    return layer.getIconSize().getExpression();
  }

  /**
   * Set the IconSize expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setIconSizeExpression(@NonNull Expression expression) {
    layer.setProperties(iconSize(expression));
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
   * Get the IconImage expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getIconImageExpression() {
    return layer.getIconImage().getExpression();
  }

  /**
   * Set the IconImage expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setIconImageExpression(@NonNull Expression expression) {
    layer.setProperties(iconImage(expression));
  }

  /**
   * Get the IconRotate expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getIconRotateExpression() {
    return layer.getIconRotate().getExpression();
  }

  /**
   * Set the IconRotate expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setIconRotateExpression(@NonNull Expression expression) {
    layer.setProperties(iconRotate(expression));
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
   * Get the IconOffset expression
   *
   * @return property wrapper value around Float[]
   */
  @Nullable
  public Expression getIconOffsetExpression() {
    return layer.getIconOffset().getExpression();
  }

  /**
   * Set the IconOffset expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float[]
   */
  public void setIconOffsetExpression(@NonNull Expression expression) {
    layer.setProperties(iconOffset(expression));
  }

  /**
   * Get the IconAnchor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getIconAnchorExpression() {
    return layer.getIconAnchor().getExpression();
  }

  /**
   * Set the IconAnchor expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setIconAnchorExpression(@NonNull Expression expression) {
    layer.setProperties(iconAnchor(expression));
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
   * Get the TextField expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getTextFieldExpression() {
    return layer.getTextField().getExpression();
  }

  /**
   * Set the TextField expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setTextFieldExpression(@NonNull Expression expression) {
    layer.setProperties(textField(expression));
  }

  /**
   * Get the TextFont expression
   *
   * @return property wrapper value around String[]
   */
  @Nullable
  public Expression getTextFontExpression() {
    return layer.getTextFont().getExpression();
  }

  /**
   * Set the TextFont expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String[]
   */
  public void setTextFontExpression(@NonNull Expression expression) {
    layer.setProperties(textFont(expression));
  }

  /**
   * Get the TextSize expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextSizeExpression() {
    return layer.getTextSize().getExpression();
  }

  /**
   * Set the TextSize expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextSizeExpression(@NonNull Expression expression) {
    layer.setProperties(textSize(expression));
  }

  /**
   * Get the TextMaxWidth expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextMaxWidthExpression() {
    return layer.getTextMaxWidth().getExpression();
  }

  /**
   * Set the TextMaxWidth expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextMaxWidthExpression(@NonNull Expression expression) {
    layer.setProperties(textMaxWidth(expression));
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
   * Get the TextLetterSpacing expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextLetterSpacingExpression() {
    return layer.getTextLetterSpacing().getExpression();
  }

  /**
   * Set the TextLetterSpacing expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextLetterSpacingExpression(@NonNull Expression expression) {
    layer.setProperties(textLetterSpacing(expression));
  }

  /**
   * Get the TextJustify expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getTextJustifyExpression() {
    return layer.getTextJustify().getExpression();
  }

  /**
   * Set the TextJustify expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setTextJustifyExpression(@NonNull Expression expression) {
    layer.setProperties(textJustify(expression));
  }

  /**
   * Get the TextAnchor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getTextAnchorExpression() {
    return layer.getTextAnchor().getExpression();
  }

  /**
   * Set the TextAnchor expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setTextAnchorExpression(@NonNull Expression expression) {
    layer.setProperties(textAnchor(expression));
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
   * Get the TextRotate expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextRotateExpression() {
    return layer.getTextRotate().getExpression();
  }

  /**
   * Set the TextRotate expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextRotateExpression(@NonNull Expression expression) {
    layer.setProperties(textRotate(expression));
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
   * Get the TextTransform expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getTextTransformExpression() {
    return layer.getTextTransform().getExpression();
  }

  /**
   * Set the TextTransform expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setTextTransformExpression(@NonNull Expression expression) {
    layer.setProperties(textTransform(expression));
  }

  /**
   * Get the TextOffset expression
   *
   * @return property wrapper value around Float[]
   */
  @Nullable
  public Expression getTextOffsetExpression() {
    return layer.getTextOffset().getExpression();
  }

  /**
   * Set the TextOffset expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float[]
   */
  public void setTextOffsetExpression(@NonNull Expression expression) {
    layer.setProperties(textOffset(expression));
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
   * Get the IconOpacity expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getIconOpacityExpression() {
    return layer.getIconOpacity().getExpression();
  }

  /**
   * Set the IconOpacity expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setIconOpacityExpression(@NonNull Expression expression) {
    layer.setProperties(iconOpacity(expression));
  }

  /**
   * Get the IconColor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getIconColorExpression() {
    return layer.getIconColor().getExpression();
  }

  /**
   * Set the IconColor expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setIconColorExpression(@NonNull Expression expression) {
    layer.setProperties(iconColor(expression));
  }

  /**
   * Get the IconHaloColor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getIconHaloColorExpression() {
    return layer.getIconHaloColor().getExpression();
  }

  /**
   * Set the IconHaloColor expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setIconHaloColorExpression(@NonNull Expression expression) {
    layer.setProperties(iconHaloColor(expression));
  }

  /**
   * Get the IconHaloWidth expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getIconHaloWidthExpression() {
    return layer.getIconHaloWidth().getExpression();
  }

  /**
   * Set the IconHaloWidth expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setIconHaloWidthExpression(@NonNull Expression expression) {
    layer.setProperties(iconHaloWidth(expression));
  }

  /**
   * Get the IconHaloBlur expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getIconHaloBlurExpression() {
    return layer.getIconHaloBlur().getExpression();
  }

  /**
   * Set the IconHaloBlur expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setIconHaloBlurExpression(@NonNull Expression expression) {
    layer.setProperties(iconHaloBlur(expression));
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
   * Get the TextOpacity expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextOpacityExpression() {
    return layer.getTextOpacity().getExpression();
  }

  /**
   * Set the TextOpacity expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextOpacityExpression(@NonNull Expression expression) {
    layer.setProperties(textOpacity(expression));
  }

  /**
   * Get the TextColor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getTextColorExpression() {
    return layer.getTextColor().getExpression();
  }

  /**
   * Set the TextColor expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setTextColorExpression(@NonNull Expression expression) {
    layer.setProperties(textColor(expression));
  }

  /**
   * Get the TextHaloColor expression
   *
   * @return property wrapper value around String
   */
  @Nullable
  public Expression getTextHaloColorExpression() {
    return layer.getTextHaloColor().getExpression();
  }

  /**
   * Set the TextHaloColor expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for String
   */
  public void setTextHaloColorExpression(@NonNull Expression expression) {
    layer.setProperties(textHaloColor(expression));
  }

  /**
   * Get the TextHaloWidth expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextHaloWidthExpression() {
    return layer.getTextHaloWidth().getExpression();
  }

  /**
   * Set the TextHaloWidth expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextHaloWidthExpression(@NonNull Expression expression) {
    layer.setProperties(textHaloWidth(expression));
  }

  /**
   * Get the TextHaloBlur expression
   *
   * @return property wrapper value around Float
   */
  @Nullable
  public Expression getTextHaloBlurExpression() {
    return layer.getTextHaloBlur().getExpression();
  }

  /**
   * Set the TextHaloBlur expression.
   * <p>
   * this expression is applied to all symbols used by this manager and overrides
   * behaviour defined on a symbol itself.
   * </p>
   *
   * @param expression value for Float
   */
  public void setTextHaloBlurExpression(@NonNull Expression expression) {
    layer.setProperties(textHaloBlur(expression));
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
