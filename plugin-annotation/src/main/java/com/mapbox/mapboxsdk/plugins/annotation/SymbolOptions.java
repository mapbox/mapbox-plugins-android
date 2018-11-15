// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.*;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.style.layers.Property;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.convertArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toFloatArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toStringArray;

/**
 * Builder class from which a symbol is created.
 */
public class SymbolOptions extends Options<Symbol> {

  private boolean isDraggable;
  private Geometry geometry;
  private Float iconSize;
  private String iconImage;
  private Float iconRotate;
  private Float[] iconOffset;
  private String iconAnchor;
  private String textField;
  private String[] textFont;
  private Float textSize;
  private Float textMaxWidth;
  private Float textLetterSpacing;
  private String textJustify;
  private String textAnchor;
  private Float textRotate;
  private String textTransform;
  private Float[] textOffset;
  private Float iconOpacity;
  private String iconColor;
  private String iconHaloColor;
  private Float iconHaloWidth;
  private Float iconHaloBlur;
  private Float textOpacity;
  private String textColor;
  private String textHaloColor;
  private Float textHaloWidth;
  private Float textHaloBlur;
  private int zIndex;

  /**
   * Set icon-size to initialise the symbol with.
   *
   * @param iconSize the icon-size value
   * @return this
   */
  public SymbolOptions withIconSize(Float iconSize) {
    this.iconSize =  iconSize;
    return this;
  }

  /**
   * Get the current configured  icon-size for the symbol
   *
   * @return iconSize value
   */
  public Float getIconSize() {
    return iconSize;
  }

  /**
   * Set icon-image to initialise the symbol with.
   *
   * @param iconImage the icon-image value
   * @return this
   */
  public SymbolOptions withIconImage(String iconImage) {
    this.iconImage =  iconImage;
    return this;
  }

  /**
   * Get the current configured  icon-image for the symbol
   *
   * @return iconImage value
   */
  public String getIconImage() {
    return iconImage;
  }

  /**
   * Set icon-rotate to initialise the symbol with.
   *
   * @param iconRotate the icon-rotate value
   * @return this
   */
  public SymbolOptions withIconRotate(Float iconRotate) {
    this.iconRotate =  iconRotate;
    return this;
  }

  /**
   * Get the current configured  icon-rotate for the symbol
   *
   * @return iconRotate value
   */
  public Float getIconRotate() {
    return iconRotate;
  }

  /**
   * Set icon-offset to initialise the symbol with.
   *
   * @param iconOffset the icon-offset value
   * @return this
   */
  public SymbolOptions withIconOffset(Float[] iconOffset) {
    this.iconOffset =  iconOffset;
    return this;
  }

  /**
   * Get the current configured  icon-offset for the symbol
   *
   * @return iconOffset value
   */
  public Float[] getIconOffset() {
    return iconOffset;
  }

  /**
   * Set icon-anchor to initialise the symbol with.
   *
   * @param iconAnchor the icon-anchor value
   * @return this
   */
  public SymbolOptions withIconAnchor(@Property.ICON_ANCHOR String iconAnchor) {
    this.iconAnchor =  iconAnchor;
    return this;
  }

  /**
   * Get the current configured  icon-anchor for the symbol
   *
   * @return iconAnchor value
   */
  public String getIconAnchor() {
    return iconAnchor;
  }

  /**
   * Set text-field to initialise the symbol with.
   *
   * @param textField the text-field value
   * @return this
   */
  public SymbolOptions withTextField(String textField) {
    this.textField =  textField;
    return this;
  }

  /**
   * Get the current configured  text-field for the symbol
   *
   * @return textField value
   */
  public String getTextField() {
    return textField;
  }

  /**
   * Set text-font to initialise the symbol with.
   *
   * @param textFont the text-font value
   * @return this
   */
  public SymbolOptions withTextFont(String[] textFont) {
    this.textFont =  textFont;
    return this;
  }

  /**
   * Get the current configured  text-font for the symbol
   *
   * @return textFont value
   */
  public String[] getTextFont() {
    return textFont;
  }

  /**
   * Set text-size to initialise the symbol with.
   *
   * @param textSize the text-size value
   * @return this
   */
  public SymbolOptions withTextSize(Float textSize) {
    this.textSize =  textSize;
    return this;
  }

  /**
   * Get the current configured  text-size for the symbol
   *
   * @return textSize value
   */
  public Float getTextSize() {
    return textSize;
  }

  /**
   * Set text-max-width to initialise the symbol with.
   *
   * @param textMaxWidth the text-max-width value
   * @return this
   */
  public SymbolOptions withTextMaxWidth(Float textMaxWidth) {
    this.textMaxWidth =  textMaxWidth;
    return this;
  }

  /**
   * Get the current configured  text-max-width for the symbol
   *
   * @return textMaxWidth value
   */
  public Float getTextMaxWidth() {
    return textMaxWidth;
  }

  /**
   * Set text-letter-spacing to initialise the symbol with.
   *
   * @param textLetterSpacing the text-letter-spacing value
   * @return this
   */
  public SymbolOptions withTextLetterSpacing(Float textLetterSpacing) {
    this.textLetterSpacing =  textLetterSpacing;
    return this;
  }

  /**
   * Get the current configured  text-letter-spacing for the symbol
   *
   * @return textLetterSpacing value
   */
  public Float getTextLetterSpacing() {
    return textLetterSpacing;
  }

  /**
   * Set text-justify to initialise the symbol with.
   *
   * @param textJustify the text-justify value
   * @return this
   */
  public SymbolOptions withTextJustify(@Property.TEXT_JUSTIFY String textJustify) {
    this.textJustify =  textJustify;
    return this;
  }

  /**
   * Get the current configured  text-justify for the symbol
   *
   * @return textJustify value
   */
  public String getTextJustify() {
    return textJustify;
  }

  /**
   * Set text-anchor to initialise the symbol with.
   *
   * @param textAnchor the text-anchor value
   * @return this
   */
  public SymbolOptions withTextAnchor(@Property.TEXT_ANCHOR String textAnchor) {
    this.textAnchor =  textAnchor;
    return this;
  }

  /**
   * Get the current configured  text-anchor for the symbol
   *
   * @return textAnchor value
   */
  public String getTextAnchor() {
    return textAnchor;
  }

  /**
   * Set text-rotate to initialise the symbol with.
   *
   * @param textRotate the text-rotate value
   * @return this
   */
  public SymbolOptions withTextRotate(Float textRotate) {
    this.textRotate =  textRotate;
    return this;
  }

  /**
   * Get the current configured  text-rotate for the symbol
   *
   * @return textRotate value
   */
  public Float getTextRotate() {
    return textRotate;
  }

  /**
   * Set text-transform to initialise the symbol with.
   *
   * @param textTransform the text-transform value
   * @return this
   */
  public SymbolOptions withTextTransform(@Property.TEXT_TRANSFORM String textTransform) {
    this.textTransform =  textTransform;
    return this;
  }

  /**
   * Get the current configured  text-transform for the symbol
   *
   * @return textTransform value
   */
  public String getTextTransform() {
    return textTransform;
  }

  /**
   * Set text-offset to initialise the symbol with.
   *
   * @param textOffset the text-offset value
   * @return this
   */
  public SymbolOptions withTextOffset(Float[] textOffset) {
    this.textOffset =  textOffset;
    return this;
  }

  /**
   * Get the current configured  text-offset for the symbol
   *
   * @return textOffset value
   */
  public Float[] getTextOffset() {
    return textOffset;
  }

  /**
   * Set icon-opacity to initialise the symbol with.
   *
   * @param iconOpacity the icon-opacity value
   * @return this
   */
  public SymbolOptions withIconOpacity(Float iconOpacity) {
    this.iconOpacity =  iconOpacity;
    return this;
  }

  /**
   * Get the current configured  icon-opacity for the symbol
   *
   * @return iconOpacity value
   */
  public Float getIconOpacity() {
    return iconOpacity;
  }

  /**
   * Set icon-color to initialise the symbol with.
   *
   * @param iconColor the icon-color value
   * @return this
   */
  public SymbolOptions withIconColor(String iconColor) {
    this.iconColor =  iconColor;
    return this;
  }

  /**
   * Get the current configured  icon-color for the symbol
   *
   * @return iconColor value
   */
  public String getIconColor() {
    return iconColor;
  }

  /**
   * Set icon-halo-color to initialise the symbol with.
   *
   * @param iconHaloColor the icon-halo-color value
   * @return this
   */
  public SymbolOptions withIconHaloColor(String iconHaloColor) {
    this.iconHaloColor =  iconHaloColor;
    return this;
  }

  /**
   * Get the current configured  icon-halo-color for the symbol
   *
   * @return iconHaloColor value
   */
  public String getIconHaloColor() {
    return iconHaloColor;
  }

  /**
   * Set icon-halo-width to initialise the symbol with.
   *
   * @param iconHaloWidth the icon-halo-width value
   * @return this
   */
  public SymbolOptions withIconHaloWidth(Float iconHaloWidth) {
    this.iconHaloWidth =  iconHaloWidth;
    return this;
  }

  /**
   * Get the current configured  icon-halo-width for the symbol
   *
   * @return iconHaloWidth value
   */
  public Float getIconHaloWidth() {
    return iconHaloWidth;
  }

  /**
   * Set icon-halo-blur to initialise the symbol with.
   *
   * @param iconHaloBlur the icon-halo-blur value
   * @return this
   */
  public SymbolOptions withIconHaloBlur(Float iconHaloBlur) {
    this.iconHaloBlur =  iconHaloBlur;
    return this;
  }

  /**
   * Get the current configured  icon-halo-blur for the symbol
   *
   * @return iconHaloBlur value
   */
  public Float getIconHaloBlur() {
    return iconHaloBlur;
  }

  /**
   * Set text-opacity to initialise the symbol with.
   *
   * @param textOpacity the text-opacity value
   * @return this
   */
  public SymbolOptions withTextOpacity(Float textOpacity) {
    this.textOpacity =  textOpacity;
    return this;
  }

  /**
   * Get the current configured  text-opacity for the symbol
   *
   * @return textOpacity value
   */
  public Float getTextOpacity() {
    return textOpacity;
  }

  /**
   * Set text-color to initialise the symbol with.
   *
   * @param textColor the text-color value
   * @return this
   */
  public SymbolOptions withTextColor(String textColor) {
    this.textColor =  textColor;
    return this;
  }

  /**
   * Get the current configured  text-color for the symbol
   *
   * @return textColor value
   */
  public String getTextColor() {
    return textColor;
  }

  /**
   * Set text-halo-color to initialise the symbol with.
   *
   * @param textHaloColor the text-halo-color value
   * @return this
   */
  public SymbolOptions withTextHaloColor(String textHaloColor) {
    this.textHaloColor =  textHaloColor;
    return this;
  }

  /**
   * Get the current configured  text-halo-color for the symbol
   *
   * @return textHaloColor value
   */
  public String getTextHaloColor() {
    return textHaloColor;
  }

  /**
   * Set text-halo-width to initialise the symbol with.
   *
   * @param textHaloWidth the text-halo-width value
   * @return this
   */
  public SymbolOptions withTextHaloWidth(Float textHaloWidth) {
    this.textHaloWidth =  textHaloWidth;
    return this;
  }

  /**
   * Get the current configured  text-halo-width for the symbol
   *
   * @return textHaloWidth value
   */
  public Float getTextHaloWidth() {
    return textHaloWidth;
  }

  /**
   * Set text-halo-blur to initialise the symbol with.
   *
   * @param textHaloBlur the text-halo-blur value
   * @return this
   */
  public SymbolOptions withTextHaloBlur(Float textHaloBlur) {
    this.textHaloBlur =  textHaloBlur;
    return this;
  }

  /**
   * Get the current configured  text-halo-blur for the symbol
   *
   * @return textHaloBlur value
   */
  public Float getTextHaloBlur() {
    return textHaloBlur;
  }

  /**
   * Set the LatLng of the symbol, which represents the location of the symbol on the map
   *
   * @param latLng the location of the symbol in a longitude and latitude pair
   * @return this
   */
  public SymbolOptions withLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
    return this;
  }

  /**
   * Set the geometry of the symbol, which represents the location of the symbol on the map
   *
   * @param geometry the location of the symbol
   * @return this
   */
  public SymbolOptions withGeometry(Point geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Set the zIndex of the symbol, which represents the place of the symbol on the map inside a layer.
   * <p>
   * A higher value brings the symbol to the front.
   * </p>
   *
   * @param zIndex the z index
   * @return this
   */
  public SymbolOptions withZIndex(int zIndex) {
    this.zIndex = zIndex;
    return this;
  }

  /**
   * Get the zIndex of the symbol, which represents the place of the symbol on the map inside a layer.
   *
   * @return the z index
   */
  public int getZIndex() {
    return zIndex;
  }

  /**
   * Returns whether this symbol is draggable, meaning it can be dragged across the screen when touched and moved.
   *
   * @return draggable when touched
   */
  public boolean isDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this symbol should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public SymbolOptions setDraggable(boolean draggable) {
    isDraggable = draggable;
    return this;
  }

  @Override
  Symbol build(long id, AnnotationManager<?, Symbol, ?, ?, ?, ?> annotationManager) {
    if (geometry == null) {
      throw new RuntimeException("geometry field is required");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("icon-size", iconSize);
    jsonObject.addProperty("icon-image", iconImage);
    jsonObject.addProperty("icon-rotate", iconRotate);
    jsonObject.add("icon-offset", convertArray(iconOffset));
    jsonObject.addProperty("icon-anchor", iconAnchor);
    jsonObject.addProperty("text-field", textField);
    jsonObject.add("text-font", convertArray(textFont));
    jsonObject.addProperty("text-size", textSize);
    jsonObject.addProperty("text-max-width", textMaxWidth);
    jsonObject.addProperty("text-letter-spacing", textLetterSpacing);
    jsonObject.addProperty("text-justify", textJustify);
    jsonObject.addProperty("text-anchor", textAnchor);
    jsonObject.addProperty("text-rotate", textRotate);
    jsonObject.addProperty("text-transform", textTransform);
    jsonObject.add("text-offset", convertArray(textOffset));
    jsonObject.addProperty("icon-opacity", iconOpacity);
    jsonObject.addProperty("icon-color", iconColor);
    jsonObject.addProperty("icon-halo-color", iconHaloColor);
    jsonObject.addProperty("icon-halo-width", iconHaloWidth);
    jsonObject.addProperty("icon-halo-blur", iconHaloBlur);
    jsonObject.addProperty("text-opacity", textOpacity);
    jsonObject.addProperty("text-color", textColor);
    jsonObject.addProperty("text-halo-color", textHaloColor);
    jsonObject.addProperty("text-halo-width", textHaloWidth);
    jsonObject.addProperty("text-halo-blur", textHaloBlur);
    jsonObject.addProperty(Symbol.Z_INDEX, zIndex);
    Symbol symbol = new Symbol(id, annotationManager, jsonObject, geometry);
    symbol.setDraggable(isDraggable);
    return symbol;
  }

  /**
   * Creates SymbolOptions out of a Feature.
   * <p>
   * All supported properties are:<br>
   * "icon-size" - Float<br>
   * "icon-image" - String<br>
   * "icon-rotate" - Float<br>
   * "icon-offset" - Float[]<br>
   * "icon-anchor" - String<br>
   * "text-field" - String<br>
   * "text-font" - String[]<br>
   * "text-size" - Float<br>
   * "text-max-width" - Float<br>
   * "text-letter-spacing" - Float<br>
   * "text-justify" - String<br>
   * "text-anchor" - String<br>
   * "text-rotate" - Float<br>
   * "text-transform" - String<br>
   * "text-offset" - Float[]<br>
   * "icon-opacity" - Float<br>
   * "icon-color" - String<br>
   * "icon-halo-color" - String<br>
   * "icon-halo-width" - Float<br>
   * "icon-halo-blur" - Float<br>
   * "text-opacity" - Float<br>
   * "text-color" - String<br>
   * "text-halo-color" - String<br>
   * "text-halo-width" - Float<br>
   * "text-halo-blur" - Float<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "z-index" - Integer, z-index of the feature within the manager<br>
   * "is-draggable" - Boolean, true if the symbol should be draggable, false otherwise
   *
   * @param feature feature to be converted
   */
  @Nullable
  static SymbolOptions fromFeature(@NonNull Feature feature) {
    if (feature.geometry() == null) {
      throw new RuntimeException("geometry field is required");
    }
    if (!(feature.geometry() instanceof Point)) {
      return null;
    }

    SymbolOptions options = new SymbolOptions();
    options.geometry = feature.geometry();
    if (feature.hasProperty("icon-size")) {
      options.iconSize = feature.getProperty("icon-size").getAsFloat();
    }
    if (feature.hasProperty("icon-image")) {
      options.iconImage = feature.getProperty("icon-image").getAsString();
    }
    if (feature.hasProperty("icon-rotate")) {
      options.iconRotate = feature.getProperty("icon-rotate").getAsFloat();
    }
    if (feature.hasProperty("icon-offset")) {
      options.iconOffset = toFloatArray(feature.getProperty("icon-offset").getAsJsonArray());
    }
    if (feature.hasProperty("icon-anchor")) {
      options.iconAnchor = feature.getProperty("icon-anchor").getAsString();
    }
    if (feature.hasProperty("text-field")) {
      options.textField = feature.getProperty("text-field").getAsString();
    }
    if (feature.hasProperty("text-font")) {
      options.textFont = toStringArray(feature.getProperty("text-font").getAsJsonArray());
    }
    if (feature.hasProperty("text-size")) {
      options.textSize = feature.getProperty("text-size").getAsFloat();
    }
    if (feature.hasProperty("text-max-width")) {
      options.textMaxWidth = feature.getProperty("text-max-width").getAsFloat();
    }
    if (feature.hasProperty("text-letter-spacing")) {
      options.textLetterSpacing = feature.getProperty("text-letter-spacing").getAsFloat();
    }
    if (feature.hasProperty("text-justify")) {
      options.textJustify = feature.getProperty("text-justify").getAsString();
    }
    if (feature.hasProperty("text-anchor")) {
      options.textAnchor = feature.getProperty("text-anchor").getAsString();
    }
    if (feature.hasProperty("text-rotate")) {
      options.textRotate = feature.getProperty("text-rotate").getAsFloat();
    }
    if (feature.hasProperty("text-transform")) {
      options.textTransform = feature.getProperty("text-transform").getAsString();
    }
    if (feature.hasProperty("text-offset")) {
      options.textOffset = toFloatArray(feature.getProperty("text-offset").getAsJsonArray());
    }
    if (feature.hasProperty("icon-opacity")) {
      options.iconOpacity = feature.getProperty("icon-opacity").getAsFloat();
    }
    if (feature.hasProperty("icon-color")) {
      options.iconColor = feature.getProperty("icon-color").getAsString();
    }
    if (feature.hasProperty("icon-halo-color")) {
      options.iconHaloColor = feature.getProperty("icon-halo-color").getAsString();
    }
    if (feature.hasProperty("icon-halo-width")) {
      options.iconHaloWidth = feature.getProperty("icon-halo-width").getAsFloat();
    }
    if (feature.hasProperty("icon-halo-blur")) {
      options.iconHaloBlur = feature.getProperty("icon-halo-blur").getAsFloat();
    }
    if (feature.hasProperty("text-opacity")) {
      options.textOpacity = feature.getProperty("text-opacity").getAsFloat();
    }
    if (feature.hasProperty("text-color")) {
      options.textColor = feature.getProperty("text-color").getAsString();
    }
    if (feature.hasProperty("text-halo-color")) {
      options.textHaloColor = feature.getProperty("text-halo-color").getAsString();
    }
    if (feature.hasProperty("text-halo-width")) {
      options.textHaloWidth = feature.getProperty("text-halo-width").getAsFloat();
    }
    if (feature.hasProperty("text-halo-blur")) {
      options.textHaloBlur = feature.getProperty("text-halo-blur").getAsFloat();
    }
    if (feature.hasProperty("z-index")) {
      options.zIndex = feature.getProperty("z-index").getAsInt();
    }
    if (feature.hasProperty("is-draggable")) {
      options.isDraggable = feature.getProperty("is-draggable").getAsBoolean();
    }
    return options;
  }
}
