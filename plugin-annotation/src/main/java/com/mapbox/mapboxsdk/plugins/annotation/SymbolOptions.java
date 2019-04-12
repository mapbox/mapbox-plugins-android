// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.*;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;

import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.convertArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toFloatArray;
import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.toStringArray;

/**
 * Builder class from which a symbol is created.
 */
public class SymbolOptions extends Options<Symbol> {

  private boolean isDraggable;
  private Point geometry;
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

  static final String PROPERTY_iconSize = "icon-size";
  static final String PROPERTY_iconImage = "icon-image";
  static final String PROPERTY_iconRotate = "icon-rotate";
  static final String PROPERTY_iconOffset = "icon-offset";
  static final String PROPERTY_iconAnchor = "icon-anchor";
  static final String PROPERTY_textField = "text-field";
  static final String PROPERTY_textFont = "text-font";
  static final String PROPERTY_textSize = "text-size";
  static final String PROPERTY_textMaxWidth = "text-max-width";
  static final String PROPERTY_textLetterSpacing = "text-letter-spacing";
  static final String PROPERTY_textJustify = "text-justify";
  static final String PROPERTY_textAnchor = "text-anchor";
  static final String PROPERTY_textRotate = "text-rotate";
  static final String PROPERTY_textTransform = "text-transform";
  static final String PROPERTY_textOffset = "text-offset";
  static final String PROPERTY_iconOpacity = "icon-opacity";
  static final String PROPERTY_iconColor = "icon-color";
  static final String PROPERTY_iconHaloColor = "icon-halo-color";
  static final String PROPERTY_iconHaloWidth = "icon-halo-width";
  static final String PROPERTY_iconHaloBlur = "icon-halo-blur";
  static final String PROPERTY_textOpacity = "text-opacity";
  static final String PROPERTY_textColor = "text-color";
  static final String PROPERTY_textHaloColor = "text-halo-color";
  static final String PROPERTY_textHaloWidth = "text-halo-width";
  static final String PROPERTY_textHaloBlur = "text-halo-blur";
  static final String PROPERTY_zIndex = "z-index";
  private static final String PROPERTY_isDraggable = "is-draggable";

  /**
   * Set icon-size to initialise the symbol with.
   * <p>
   * Scales the original size of the icon by the provided factor. The new pixel size of the image will be the original pixel size multiplied by {@link PropertyFactory#iconSize}. 1 is the original size; 3 triples the size of the image.
   * </p>
   * @param iconSize the icon-size value
   * @return this
   */
  public SymbolOptions withIconSize(Float iconSize) {
    this.iconSize =  iconSize;
    return this;
  }

  /**
   * Get the current configured  icon-size for the symbol
   * <p>
   * Scales the original size of the icon by the provided factor. The new pixel size of the image will be the original pixel size multiplied by {@link PropertyFactory#iconSize}. 1 is the original size; 3 triples the size of the image.
   * </p>
   * @return iconSize value
   */
  public Float getIconSize() {
    return iconSize;
  }

  /**
   * Set icon-image to initialise the symbol with.
   * <p>
   * Name of image in sprite to use for drawing an image background.
   * </p>
   * @param iconImage the icon-image value
   * @return this
   */
  public SymbolOptions withIconImage(String iconImage) {
    this.iconImage =  iconImage;
    return this;
  }

  /**
   * Get the current configured  icon-image for the symbol
   * <p>
   * Name of image in sprite to use for drawing an image background.
   * </p>
   * @return iconImage value
   */
  public String getIconImage() {
    return iconImage;
  }

  /**
   * Set icon-rotate to initialise the symbol with.
   * <p>
   * Rotates the icon clockwise.
   * </p>
   * @param iconRotate the icon-rotate value
   * @return this
   */
  public SymbolOptions withIconRotate(Float iconRotate) {
    this.iconRotate =  iconRotate;
    return this;
  }

  /**
   * Get the current configured  icon-rotate for the symbol
   * <p>
   * Rotates the icon clockwise.
   * </p>
   * @return iconRotate value
   */
  public Float getIconRotate() {
    return iconRotate;
  }

  /**
   * Set icon-offset to initialise the symbol with.
   * <p>
   * Offset distance of icon from its anchor. Positive values indicate right and down, while negative values indicate left and up. Each component is multiplied by the value of {@link PropertyFactory#iconSize} to obtain the final offset in density-independent pixels. When combined with {@link PropertyFactory#iconRotate} the offset will be as if the rotated direction was up.
   * </p>
   * @param iconOffset the icon-offset value
   * @return this
   */
  public SymbolOptions withIconOffset(Float[] iconOffset) {
    this.iconOffset =  iconOffset;
    return this;
  }

  /**
   * Get the current configured  icon-offset for the symbol
   * <p>
   * Offset distance of icon from its anchor. Positive values indicate right and down, while negative values indicate left and up. Each component is multiplied by the value of {@link PropertyFactory#iconSize} to obtain the final offset in density-independent pixels. When combined with {@link PropertyFactory#iconRotate} the offset will be as if the rotated direction was up.
   * </p>
   * @return iconOffset value
   */
  public Float[] getIconOffset() {
    return iconOffset;
  }

  /**
   * Set icon-anchor to initialise the symbol with.
   * <p>
   * Part of the icon placed closest to the anchor.
   * </p>
   * @param iconAnchor the icon-anchor value
   * @return this
   */
  public SymbolOptions withIconAnchor(@Property.ICON_ANCHOR String iconAnchor) {
    this.iconAnchor =  iconAnchor;
    return this;
  }

  /**
   * Get the current configured  icon-anchor for the symbol
   * <p>
   * Part of the icon placed closest to the anchor.
   * </p>
   * @return iconAnchor value
   */
  public String getIconAnchor() {
    return iconAnchor;
  }

  /**
   * Set text-field to initialise the symbol with.
   * <p>
   * Value to use for a text label. If a plain `string` is provided, it will be treated as a `formatted` with default/inherited formatting options.
   * </p>
   * @param textField the text-field value
   * @return this
   */
  public SymbolOptions withTextField(String textField) {
    this.textField =  textField;
    return this;
  }

  /**
   * Get the current configured  text-field for the symbol
   * <p>
   * Value to use for a text label. If a plain `string` is provided, it will be treated as a `formatted` with default/inherited formatting options.
   * </p>
   * @return textField value
   */
  public String getTextField() {
    return textField;
  }

  /**
   * Set text-font to initialise the symbol with.
   * <p>
   * Font stack to use for displaying text.
   * </p>
   * @param textFont the text-font value
   * @return this
   */
  public SymbolOptions withTextFont(String[] textFont) {
    this.textFont =  textFont;
    return this;
  }

  /**
   * Get the current configured  text-font for the symbol
   * <p>
   * Font stack to use for displaying text.
   * </p>
   * @return textFont value
   */
  public String[] getTextFont() {
    return textFont;
  }

  /**
   * Set text-size to initialise the symbol with.
   * <p>
   * Font size.
   * </p>
   * @param textSize the text-size value
   * @return this
   */
  public SymbolOptions withTextSize(Float textSize) {
    this.textSize =  textSize;
    return this;
  }

  /**
   * Get the current configured  text-size for the symbol
   * <p>
   * Font size.
   * </p>
   * @return textSize value
   */
  public Float getTextSize() {
    return textSize;
  }

  /**
   * Set text-max-width to initialise the symbol with.
   * <p>
   * The maximum line width for text wrapping.
   * </p>
   * @param textMaxWidth the text-max-width value
   * @return this
   */
  public SymbolOptions withTextMaxWidth(Float textMaxWidth) {
    this.textMaxWidth =  textMaxWidth;
    return this;
  }

  /**
   * Get the current configured  text-max-width for the symbol
   * <p>
   * The maximum line width for text wrapping.
   * </p>
   * @return textMaxWidth value
   */
  public Float getTextMaxWidth() {
    return textMaxWidth;
  }

  /**
   * Set text-letter-spacing to initialise the symbol with.
   * <p>
   * Text tracking amount.
   * </p>
   * @param textLetterSpacing the text-letter-spacing value
   * @return this
   */
  public SymbolOptions withTextLetterSpacing(Float textLetterSpacing) {
    this.textLetterSpacing =  textLetterSpacing;
    return this;
  }

  /**
   * Get the current configured  text-letter-spacing for the symbol
   * <p>
   * Text tracking amount.
   * </p>
   * @return textLetterSpacing value
   */
  public Float getTextLetterSpacing() {
    return textLetterSpacing;
  }

  /**
   * Set text-justify to initialise the symbol with.
   * <p>
   * Text justification options.
   * </p>
   * @param textJustify the text-justify value
   * @return this
   */
  public SymbolOptions withTextJustify(@Property.TEXT_JUSTIFY String textJustify) {
    this.textJustify =  textJustify;
    return this;
  }

  /**
   * Get the current configured  text-justify for the symbol
   * <p>
   * Text justification options.
   * </p>
   * @return textJustify value
   */
  public String getTextJustify() {
    return textJustify;
  }

  /**
   * Set text-anchor to initialise the symbol with.
   * <p>
   * Part of the text placed closest to the anchor.
   * </p>
   * @param textAnchor the text-anchor value
   * @return this
   */
  public SymbolOptions withTextAnchor(@Property.TEXT_ANCHOR String textAnchor) {
    this.textAnchor =  textAnchor;
    return this;
  }

  /**
   * Get the current configured  text-anchor for the symbol
   * <p>
   * Part of the text placed closest to the anchor.
   * </p>
   * @return textAnchor value
   */
  public String getTextAnchor() {
    return textAnchor;
  }

  /**
   * Set text-rotate to initialise the symbol with.
   * <p>
   * Rotates the text clockwise.
   * </p>
   * @param textRotate the text-rotate value
   * @return this
   */
  public SymbolOptions withTextRotate(Float textRotate) {
    this.textRotate =  textRotate;
    return this;
  }

  /**
   * Get the current configured  text-rotate for the symbol
   * <p>
   * Rotates the text clockwise.
   * </p>
   * @return textRotate value
   */
  public Float getTextRotate() {
    return textRotate;
  }

  /**
   * Set text-transform to initialise the symbol with.
   * <p>
   * Specifies how to capitalize text, similar to the CSS {@link PropertyFactory#textTransform} property.
   * </p>
   * @param textTransform the text-transform value
   * @return this
   */
  public SymbolOptions withTextTransform(@Property.TEXT_TRANSFORM String textTransform) {
    this.textTransform =  textTransform;
    return this;
  }

  /**
   * Get the current configured  text-transform for the symbol
   * <p>
   * Specifies how to capitalize text, similar to the CSS {@link PropertyFactory#textTransform} property.
   * </p>
   * @return textTransform value
   */
  public String getTextTransform() {
    return textTransform;
  }

  /**
   * Set text-offset to initialise the symbol with.
   * <p>
   * Offset distance of text from its anchor. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   * @param textOffset the text-offset value
   * @return this
   */
  public SymbolOptions withTextOffset(Float[] textOffset) {
    this.textOffset =  textOffset;
    return this;
  }

  /**
   * Get the current configured  text-offset for the symbol
   * <p>
   * Offset distance of text from its anchor. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   * @return textOffset value
   */
  public Float[] getTextOffset() {
    return textOffset;
  }

  /**
   * Set icon-opacity to initialise the symbol with.
   * <p>
   * The opacity at which the icon will be drawn.
   * </p>
   * @param iconOpacity the icon-opacity value
   * @return this
   */
  public SymbolOptions withIconOpacity(Float iconOpacity) {
    this.iconOpacity =  iconOpacity;
    return this;
  }

  /**
   * Get the current configured  icon-opacity for the symbol
   * <p>
   * The opacity at which the icon will be drawn.
   * </p>
   * @return iconOpacity value
   */
  public Float getIconOpacity() {
    return iconOpacity;
  }

  /**
   * Set icon-color to initialise the symbol with.
   * <p>
   * The color of the icon. This can only be used with sdf icons.
   * </p>
   * @param iconColor the icon-color value
   * @return this
   */
  public SymbolOptions withIconColor(String iconColor) {
    this.iconColor =  iconColor;
    return this;
  }

  /**
   * Get the current configured  icon-color for the symbol
   * <p>
   * The color of the icon. This can only be used with sdf icons.
   * </p>
   * @return iconColor value
   */
  public String getIconColor() {
    return iconColor;
  }

  /**
   * Set icon-halo-color to initialise the symbol with.
   * <p>
   * The color of the icon's halo. Icon halos can only be used with SDF icons.
   * </p>
   * @param iconHaloColor the icon-halo-color value
   * @return this
   */
  public SymbolOptions withIconHaloColor(String iconHaloColor) {
    this.iconHaloColor =  iconHaloColor;
    return this;
  }

  /**
   * Get the current configured  icon-halo-color for the symbol
   * <p>
   * The color of the icon's halo. Icon halos can only be used with SDF icons.
   * </p>
   * @return iconHaloColor value
   */
  public String getIconHaloColor() {
    return iconHaloColor;
  }

  /**
   * Set icon-halo-width to initialise the symbol with.
   * <p>
   * Distance of halo to the icon outline.
   * </p>
   * @param iconHaloWidth the icon-halo-width value
   * @return this
   */
  public SymbolOptions withIconHaloWidth(Float iconHaloWidth) {
    this.iconHaloWidth =  iconHaloWidth;
    return this;
  }

  /**
   * Get the current configured  icon-halo-width for the symbol
   * <p>
   * Distance of halo to the icon outline.
   * </p>
   * @return iconHaloWidth value
   */
  public Float getIconHaloWidth() {
    return iconHaloWidth;
  }

  /**
   * Set icon-halo-blur to initialise the symbol with.
   * <p>
   * Fade out the halo towards the outside.
   * </p>
   * @param iconHaloBlur the icon-halo-blur value
   * @return this
   */
  public SymbolOptions withIconHaloBlur(Float iconHaloBlur) {
    this.iconHaloBlur =  iconHaloBlur;
    return this;
  }

  /**
   * Get the current configured  icon-halo-blur for the symbol
   * <p>
   * Fade out the halo towards the outside.
   * </p>
   * @return iconHaloBlur value
   */
  public Float getIconHaloBlur() {
    return iconHaloBlur;
  }

  /**
   * Set text-opacity to initialise the symbol with.
   * <p>
   * The opacity at which the text will be drawn.
   * </p>
   * @param textOpacity the text-opacity value
   * @return this
   */
  public SymbolOptions withTextOpacity(Float textOpacity) {
    this.textOpacity =  textOpacity;
    return this;
  }

  /**
   * Get the current configured  text-opacity for the symbol
   * <p>
   * The opacity at which the text will be drawn.
   * </p>
   * @return textOpacity value
   */
  public Float getTextOpacity() {
    return textOpacity;
  }

  /**
   * Set text-color to initialise the symbol with.
   * <p>
   * The color with which the text will be drawn.
   * </p>
   * @param textColor the text-color value
   * @return this
   */
  public SymbolOptions withTextColor(String textColor) {
    this.textColor =  textColor;
    return this;
  }

  /**
   * Get the current configured  text-color for the symbol
   * <p>
   * The color with which the text will be drawn.
   * </p>
   * @return textColor value
   */
  public String getTextColor() {
    return textColor;
  }

  /**
   * Set text-halo-color to initialise the symbol with.
   * <p>
   * The color of the text's halo, which helps it stand out from backgrounds.
   * </p>
   * @param textHaloColor the text-halo-color value
   * @return this
   */
  public SymbolOptions withTextHaloColor(String textHaloColor) {
    this.textHaloColor =  textHaloColor;
    return this;
  }

  /**
   * Get the current configured  text-halo-color for the symbol
   * <p>
   * The color of the text's halo, which helps it stand out from backgrounds.
   * </p>
   * @return textHaloColor value
   */
  public String getTextHaloColor() {
    return textHaloColor;
  }

  /**
   * Set text-halo-width to initialise the symbol with.
   * <p>
   * Distance of halo to the font outline. Max text halo width is 1/4 of the font-size.
   * </p>
   * @param textHaloWidth the text-halo-width value
   * @return this
   */
  public SymbolOptions withTextHaloWidth(Float textHaloWidth) {
    this.textHaloWidth =  textHaloWidth;
    return this;
  }

  /**
   * Get the current configured  text-halo-width for the symbol
   * <p>
   * Distance of halo to the font outline. Max text halo width is 1/4 of the font-size.
   * </p>
   * @return textHaloWidth value
   */
  public Float getTextHaloWidth() {
    return textHaloWidth;
  }

  /**
   * Set text-halo-blur to initialise the symbol with.
   * <p>
   * The halo's fadeout distance towards the outside.
   * </p>
   * @param textHaloBlur the text-halo-blur value
   * @return this
   */
  public SymbolOptions withTextHaloBlur(Float textHaloBlur) {
    this.textHaloBlur =  textHaloBlur;
    return this;
  }

  /**
   * Get the current configured  text-halo-blur for the symbol
   * <p>
   * The halo's fadeout distance towards the outside.
   * </p>
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
  public boolean getDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this symbol should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public SymbolOptions withDraggable(boolean draggable) {
    isDraggable = draggable;
    return this;
  }

  @Override
  Symbol build(long id, AnnotationManager<?, Symbol, ?, ?, ?, ?> annotationManager) {
    if (geometry == null) {
      throw new RuntimeException("geometry field is required");
    }
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(PROPERTY_iconSize, iconSize);
    jsonObject.addProperty(PROPERTY_iconImage, iconImage);
    jsonObject.addProperty(PROPERTY_iconRotate, iconRotate);
    jsonObject.add(PROPERTY_iconOffset, convertArray(iconOffset));
    jsonObject.addProperty(PROPERTY_iconAnchor, iconAnchor);
    jsonObject.addProperty(PROPERTY_textField, textField);
    jsonObject.add(PROPERTY_textFont, convertArray(textFont));
    jsonObject.addProperty(PROPERTY_textSize, textSize);
    jsonObject.addProperty(PROPERTY_textMaxWidth, textMaxWidth);
    jsonObject.addProperty(PROPERTY_textLetterSpacing, textLetterSpacing);
    jsonObject.addProperty(PROPERTY_textJustify, textJustify);
    jsonObject.addProperty(PROPERTY_textAnchor, textAnchor);
    jsonObject.addProperty(PROPERTY_textRotate, textRotate);
    jsonObject.addProperty(PROPERTY_textTransform, textTransform);
    jsonObject.add(PROPERTY_textOffset, convertArray(textOffset));
    jsonObject.addProperty(PROPERTY_iconOpacity, iconOpacity);
    jsonObject.addProperty(PROPERTY_iconColor, iconColor);
    jsonObject.addProperty(PROPERTY_iconHaloColor, iconHaloColor);
    jsonObject.addProperty(PROPERTY_iconHaloWidth, iconHaloWidth);
    jsonObject.addProperty(PROPERTY_iconHaloBlur, iconHaloBlur);
    jsonObject.addProperty(PROPERTY_textOpacity, textOpacity);
    jsonObject.addProperty(PROPERTY_textColor, textColor);
    jsonObject.addProperty(PROPERTY_textHaloColor, textHaloColor);
    jsonObject.addProperty(PROPERTY_textHaloWidth, textHaloWidth);
    jsonObject.addProperty(PROPERTY_textHaloBlur, textHaloBlur);
    jsonObject.addProperty(PROPERTY_zIndex, zIndex);
    Symbol symbol = new Symbol(id, annotationManager, jsonObject, geometry);
    symbol.setDraggable(isDraggable);
    return symbol;
  }

  /**
   * Creates SymbolOptions out of a Feature.
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
    options.geometry = (Point) feature.geometry();
    if (feature.hasProperty(PROPERTY_iconSize)) {
      options.iconSize = feature.getProperty(PROPERTY_iconSize).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_iconImage)) {
      options.iconImage = feature.getProperty(PROPERTY_iconImage).getAsString();
    }
    if (feature.hasProperty(PROPERTY_iconRotate)) {
      options.iconRotate = feature.getProperty(PROPERTY_iconRotate).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_iconOffset)) {
      options.iconOffset = toFloatArray(feature.getProperty(PROPERTY_iconOffset).getAsJsonArray());
    }
    if (feature.hasProperty(PROPERTY_iconAnchor)) {
      options.iconAnchor = feature.getProperty(PROPERTY_iconAnchor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textField)) {
      options.textField = feature.getProperty(PROPERTY_textField).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textFont)) {
      options.textFont = toStringArray(feature.getProperty(PROPERTY_textFont).getAsJsonArray());
    }
    if (feature.hasProperty(PROPERTY_textSize)) {
      options.textSize = feature.getProperty(PROPERTY_textSize).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textMaxWidth)) {
      options.textMaxWidth = feature.getProperty(PROPERTY_textMaxWidth).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textLetterSpacing)) {
      options.textLetterSpacing = feature.getProperty(PROPERTY_textLetterSpacing).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textJustify)) {
      options.textJustify = feature.getProperty(PROPERTY_textJustify).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textAnchor)) {
      options.textAnchor = feature.getProperty(PROPERTY_textAnchor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textRotate)) {
      options.textRotate = feature.getProperty(PROPERTY_textRotate).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textTransform)) {
      options.textTransform = feature.getProperty(PROPERTY_textTransform).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textOffset)) {
      options.textOffset = toFloatArray(feature.getProperty(PROPERTY_textOffset).getAsJsonArray());
    }
    if (feature.hasProperty(PROPERTY_iconOpacity)) {
      options.iconOpacity = feature.getProperty(PROPERTY_iconOpacity).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_iconColor)) {
      options.iconColor = feature.getProperty(PROPERTY_iconColor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_iconHaloColor)) {
      options.iconHaloColor = feature.getProperty(PROPERTY_iconHaloColor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_iconHaloWidth)) {
      options.iconHaloWidth = feature.getProperty(PROPERTY_iconHaloWidth).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_iconHaloBlur)) {
      options.iconHaloBlur = feature.getProperty(PROPERTY_iconHaloBlur).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textOpacity)) {
      options.textOpacity = feature.getProperty(PROPERTY_textOpacity).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textColor)) {
      options.textColor = feature.getProperty(PROPERTY_textColor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textHaloColor)) {
      options.textHaloColor = feature.getProperty(PROPERTY_textHaloColor).getAsString();
    }
    if (feature.hasProperty(PROPERTY_textHaloWidth)) {
      options.textHaloWidth = feature.getProperty(PROPERTY_textHaloWidth).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_textHaloBlur)) {
      options.textHaloBlur = feature.getProperty(PROPERTY_textHaloBlur).getAsFloat();
    }
    if (feature.hasProperty(PROPERTY_zIndex)) {
      options.zIndex = feature.getProperty(PROPERTY_zIndex).getAsInt();
    }
    if (feature.hasProperty(PROPERTY_isDraggable)) {
      options.isDraggable = feature.getProperty(PROPERTY_isDraggable).getAsBoolean();
    }
    return options;
  }
}
