// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.ColorInt;
import android.graphics.PointF;
import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.mapboxsdk.maps.Projection;

import static com.mapbox.mapboxsdk.constants.GeometryConstants.MAX_MERCATOR_LATITUDE;
import static com.mapbox.mapboxsdk.constants.GeometryConstants.MIN_MERCATOR_LATITUDE;

@UiThread
public class Symbol extends Annotation<Point> {

  private final AnnotationManager<?, Symbol, ?, ?, ?, ?> annotationManager;

  /**
   * Create a symbol.
   *
   * @param id            the id of the symbol
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Symbol(long id, AnnotationManager<?, Symbol, ?, ?, ?, ?> annotationManager, JsonObject jsonObject, Point geometry) {
    super(id, jsonObject, geometry);
    this.annotationManager = annotationManager;
  }

  @Override
  void setUsedDataDrivenProperties() {
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconSize) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconSize);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconImage) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconImage);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconRotate) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconRotate);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconOffset) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconOffset);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconAnchor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconAnchor);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textField) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textField);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textFont) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textFont);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textSize) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textSize);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textMaxWidth) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textMaxWidth);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textLetterSpacing) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textLetterSpacing);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textJustify) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textJustify);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textAnchor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textAnchor);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textRotate) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textRotate);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textTransform) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textTransform);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textOffset) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textOffset);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconOpacity) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconOpacity);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconColor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconColor);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconHaloColor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconHaloColor);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconHaloWidth) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconHaloWidth);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_iconHaloBlur) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_iconHaloBlur);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textOpacity) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textOpacity);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textColor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textColor);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textHaloColor) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textHaloColor);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textHaloWidth) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textHaloWidth);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_textHaloBlur) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_textHaloBlur);
    }
    if (!(jsonObject.get(SymbolOptions.PROPERTY_zIndex) instanceof JsonNull)) {
      annotationManager.enableDataDrivenProperty(SymbolOptions.PROPERTY_zIndex);
    }
  }

  /**
   * Set the LatLng of the symbol, which represents the location of the symbol on the map
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param latLng the location of the symbol in a latitude and longitude pair
   */
  public void setLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
  }

  /**
   * Get the LatLng of the symbol, which represents the location of the symbol on the map
   *
   * @return the location of the symbol
   */
  @NonNull
  public LatLng getLatLng() {
    return new LatLng(geometry.latitude(), geometry.longitude());
  }

  /**
   * Set the z-index of a symbol.
   * <p>
   * If a symbol z-index is higher as another symbol it will be rendered above it.
   * </p>
   * <p>
   * Default value is 0.
   * </p>
   *
   * @param index the z-index value
   */
  public void setZIndex(int index) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_zIndex, index);
  }

  /**
   * Get the z-index of a symbol.
   *
   * @return the z-index value, 0 if not set
   */
  public int getZIndex() {
    return jsonObject.get(SymbolOptions.PROPERTY_zIndex).getAsInt();
  }

  // Property accessors

  /**
   * Get the IconSize property
   * <p>
   * Scales the original size of the icon by the provided factor. The new pixel size of the image will be the original pixel size multiplied by {@link PropertyFactory#iconSize}. 1 is the original size; 3 triples the size of the image.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getIconSize() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconSize).getAsFloat();
  }

  /**
   * Set the IconSize property
   * <p>
   * Scales the original size of the icon by the provided factor. The new pixel size of the image will be the original pixel size multiplied by {@link PropertyFactory#iconSize}. 1 is the original size; 3 triples the size of the image.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconSize(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconSize, value);
  }

  /**
   * Get the IconImage property
   * <p>
   * Name of image in sprite to use for drawing an image background.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getIconImage() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconImage).getAsString();
  }

  /**
   * Set the IconImage property
   * <p>
   * Name of image in sprite to use for drawing an image background.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setIconImage(String value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconImage, value);
  }

  /**
   * Get the IconRotate property
   * <p>
   * Rotates the icon clockwise.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getIconRotate() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconRotate).getAsFloat();
  }

  /**
   * Set the IconRotate property
   * <p>
   * Rotates the icon clockwise.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconRotate(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconRotate, value);
  }

  /**
   * Get the IconOffset property
   * <p>
   * Offset distance of icon from its anchor. Positive values indicate right and down, while negative values indicate left and up. Each component is multiplied by the value of {@link PropertyFactory#iconSize} to obtain the final offset in density-independent pixels. When combined with {@link PropertyFactory#iconRotate} the offset will be as if the rotated direction was up.
   * </p>
   *
   * @return PointF value for Float[]
   */
  public PointF getIconOffset() {
    JsonArray jsonArray = jsonObject.getAsJsonArray(SymbolOptions.PROPERTY_iconOffset);
    return new PointF(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat());
  }

  /**
   * Set the IconOffset property.
   * <p>
   * Offset distance of icon from its anchor. Positive values indicate right and down, while negative values indicate left and up. Each component is multiplied by the value of {@link PropertyFactory#iconSize} to obtain the final offset in density-independent pixels. When combined with {@link PropertyFactory#iconRotate} the offset will be as if the rotated direction was up.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param pointF value for Float[]
   */
  public void setIconOffset(PointF pointF) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(pointF.x);
    jsonArray.add(pointF.y);
    jsonObject.add(SymbolOptions.PROPERTY_iconOffset, jsonArray);
  }

  /**
   * Get the IconAnchor property
   * <p>
   * Part of the icon placed closest to the anchor.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getIconAnchor() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconAnchor).getAsString();
  }

  /**
   * Set the IconAnchor property
   * <p>
   * Part of the icon placed closest to the anchor.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setIconAnchor(@Property.ICON_ANCHOR String value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconAnchor, value);
  }

  /**
   * Get the TextField property
   * <p>
   * Value to use for a text label. If a plain `string` is provided, it will be treated as a `formatted` with default/inherited formatting options.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextField() {
    return jsonObject.get(SymbolOptions.PROPERTY_textField).getAsString();
  }

  /**
   * Set the TextField property
   * <p>
   * Value to use for a text label. If a plain `string` is provided, it will be treated as a `formatted` with default/inherited formatting options.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextField(String value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textField, value);
  }

  /**
   * Get the TextFont property
   * <p>
   * Font stack to use for displaying text.
   * </p>
   *
   * @return property wrapper value around String[]
   */
  public String[] getTextFont() {
    JsonArray jsonArray = jsonObject.getAsJsonArray(SymbolOptions.PROPERTY_textFont);
    String[] value = new String[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      value[i] = jsonArray.get(i).getAsString();
    }
    return value;
  }

  /**
   * Set the TextFont property.
   * <p>
   * Font stack to use for displaying text.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String[]
   */
  public void setTextFont(String[] value) {
    JsonArray jsonArray = new JsonArray();
    for (String element : value) {
      jsonArray.add(element);
    }
    jsonObject.add(SymbolOptions.PROPERTY_textFont, jsonArray);
  }

  /**
   * Get the TextSize property
   * <p>
   * Font size.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextSize() {
    return jsonObject.get(SymbolOptions.PROPERTY_textSize).getAsFloat();
  }

  /**
   * Set the TextSize property
   * <p>
   * Font size.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextSize(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textSize, value);
  }

  /**
   * Get the TextMaxWidth property
   * <p>
   * The maximum line width for text wrapping.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextMaxWidth() {
    return jsonObject.get(SymbolOptions.PROPERTY_textMaxWidth).getAsFloat();
  }

  /**
   * Set the TextMaxWidth property
   * <p>
   * The maximum line width for text wrapping.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextMaxWidth(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textMaxWidth, value);
  }

  /**
   * Get the TextLetterSpacing property
   * <p>
   * Text tracking amount.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextLetterSpacing() {
    return jsonObject.get(SymbolOptions.PROPERTY_textLetterSpacing).getAsFloat();
  }

  /**
   * Set the TextLetterSpacing property
   * <p>
   * Text tracking amount.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextLetterSpacing(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textLetterSpacing, value);
  }

  /**
   * Get the TextJustify property
   * <p>
   * Text justification options.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextJustify() {
    return jsonObject.get(SymbolOptions.PROPERTY_textJustify).getAsString();
  }

  /**
   * Set the TextJustify property
   * <p>
   * Text justification options.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextJustify(@Property.TEXT_JUSTIFY String value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textJustify, value);
  }

  /**
   * Get the TextAnchor property
   * <p>
   * Part of the text placed closest to the anchor.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextAnchor() {
    return jsonObject.get(SymbolOptions.PROPERTY_textAnchor).getAsString();
  }

  /**
   * Set the TextAnchor property
   * <p>
   * Part of the text placed closest to the anchor.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextAnchor(@Property.TEXT_ANCHOR String value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textAnchor, value);
  }

  /**
   * Get the TextRotate property
   * <p>
   * Rotates the text clockwise.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextRotate() {
    return jsonObject.get(SymbolOptions.PROPERTY_textRotate).getAsFloat();
  }

  /**
   * Set the TextRotate property
   * <p>
   * Rotates the text clockwise.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextRotate(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textRotate, value);
  }

  /**
   * Get the TextTransform property
   * <p>
   * Specifies how to capitalize text, similar to the CSS {@link PropertyFactory#textTransform} property.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getTextTransform() {
    return jsonObject.get(SymbolOptions.PROPERTY_textTransform).getAsString();
  }

  /**
   * Set the TextTransform property
   * <p>
   * Specifies how to capitalize text, similar to the CSS {@link PropertyFactory#textTransform} property.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextTransform(@Property.TEXT_TRANSFORM String value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textTransform, value);
  }

  /**
   * Get the TextOffset property
   * <p>
   * Offset distance of text from its anchor. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   *
   * @return PointF value for Float[]
   */
  public PointF getTextOffset() {
    JsonArray jsonArray = jsonObject.getAsJsonArray(SymbolOptions.PROPERTY_textOffset);
    return new PointF(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat());
  }

  /**
   * Set the TextOffset property.
   * <p>
   * Offset distance of text from its anchor. Positive values indicate right and down, while negative values indicate left and up.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param pointF value for Float[]
   */
  public void setTextOffset(PointF pointF) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(pointF.x);
    jsonArray.add(pointF.y);
    jsonObject.add(SymbolOptions.PROPERTY_textOffset, jsonArray);
  }

  /**
   * Get the IconOpacity property
   * <p>
   * The opacity at which the icon will be drawn.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getIconOpacity() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconOpacity).getAsFloat();
  }

  /**
   * Set the IconOpacity property
   * <p>
   * The opacity at which the icon will be drawn.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconOpacity(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconOpacity, value);
  }

  /**
   * Get the IconColor property
   * <p>
   * The color of the icon. This can only be used with sdf icons.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getIconColorAsInt() {
    return ColorUtils.rgbaToColor(jsonObject.get(SymbolOptions.PROPERTY_iconColor).getAsString());
  }

  /**
   * Get the IconColor property
   * <p>
   * The color of the icon. This can only be used with sdf icons.
   * </p>
   *
   * @return color value for String
   */
  public String getIconColor() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconColor).getAsString();
  }

  /**
   * Set the IconColor property
   * <p>
   * The color of the icon. This can only be used with sdf icons.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setIconColor(@ColorInt int color) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconColor, ColorUtils.colorToRgbaString(color));
  }

  /**
   * Set the IconColor property
   * <p>
   * The color of the icon. This can only be used with sdf icons.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setIconColor(@NonNull String color) {
    jsonObject.addProperty("icon-color", color);
  }

  /**
   * Get the IconHaloColor property
   * <p>
   * The color of the icon's halo. Icon halos can only be used with SDF icons.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getIconHaloColorAsInt() {
    return ColorUtils.rgbaToColor(jsonObject.get(SymbolOptions.PROPERTY_iconHaloColor).getAsString());
  }

  /**
   * Get the IconHaloColor property
   * <p>
   * The color of the icon's halo. Icon halos can only be used with SDF icons.
   * </p>
   *
   * @return color value for String
   */
  public String getIconHaloColor() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconHaloColor).getAsString();
  }

  /**
   * Set the IconHaloColor property
   * <p>
   * The color of the icon's halo. Icon halos can only be used with SDF icons.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setIconHaloColor(@ColorInt int color) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconHaloColor, ColorUtils.colorToRgbaString(color));
  }

  /**
   * Set the IconHaloColor property
   * <p>
   * The color of the icon's halo. Icon halos can only be used with SDF icons.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setIconHaloColor(@NonNull String color) {
    jsonObject.addProperty("icon-halo-color", color);
  }

  /**
   * Get the IconHaloWidth property
   * <p>
   * Distance of halo to the icon outline.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getIconHaloWidth() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconHaloWidth).getAsFloat();
  }

  /**
   * Set the IconHaloWidth property
   * <p>
   * Distance of halo to the icon outline.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconHaloWidth(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconHaloWidth, value);
  }

  /**
   * Get the IconHaloBlur property
   * <p>
   * Fade out the halo towards the outside.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getIconHaloBlur() {
    return jsonObject.get(SymbolOptions.PROPERTY_iconHaloBlur).getAsFloat();
  }

  /**
   * Set the IconHaloBlur property
   * <p>
   * Fade out the halo towards the outside.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconHaloBlur(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_iconHaloBlur, value);
  }

  /**
   * Get the TextOpacity property
   * <p>
   * The opacity at which the text will be drawn.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextOpacity() {
    return jsonObject.get(SymbolOptions.PROPERTY_textOpacity).getAsFloat();
  }

  /**
   * Set the TextOpacity property
   * <p>
   * The opacity at which the text will be drawn.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextOpacity(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textOpacity, value);
  }

  /**
   * Get the TextColor property
   * <p>
   * The color with which the text will be drawn.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getTextColorAsInt() {
    return ColorUtils.rgbaToColor(jsonObject.get(SymbolOptions.PROPERTY_textColor).getAsString());
  }

  /**
   * Get the TextColor property
   * <p>
   * The color with which the text will be drawn.
   * </p>
   *
   * @return color value for String
   */
  public String getTextColor() {
    return jsonObject.get(SymbolOptions.PROPERTY_textColor).getAsString();
  }

  /**
   * Set the TextColor property
   * <p>
   * The color with which the text will be drawn.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setTextColor(@ColorInt int color) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textColor, ColorUtils.colorToRgbaString(color));
  }

  /**
   * Set the TextColor property
   * <p>
   * The color with which the text will be drawn.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setTextColor(@NonNull String color) {
    jsonObject.addProperty("text-color", color);
  }

  /**
   * Get the TextHaloColor property
   * <p>
   * The color of the text's halo, which helps it stand out from backgrounds.
   * </p>
   *
   * @return color value for String
   */
  @ColorInt
  public int getTextHaloColorAsInt() {
    return ColorUtils.rgbaToColor(jsonObject.get(SymbolOptions.PROPERTY_textHaloColor).getAsString());
  }

  /**
   * Get the TextHaloColor property
   * <p>
   * The color of the text's halo, which helps it stand out from backgrounds.
   * </p>
   *
   * @return color value for String
   */
  public String getTextHaloColor() {
    return jsonObject.get(SymbolOptions.PROPERTY_textHaloColor).getAsString();
  }

  /**
   * Set the TextHaloColor property
   * <p>
   * The color of the text's halo, which helps it stand out from backgrounds.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setTextHaloColor(@ColorInt int color) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textHaloColor, ColorUtils.colorToRgbaString(color));
  }

  /**
   * Set the TextHaloColor property
   * <p>
   * The color of the text's halo, which helps it stand out from backgrounds.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setTextHaloColor(@NonNull String color) {
    jsonObject.addProperty("text-halo-color", color);
  }

  /**
   * Get the TextHaloWidth property
   * <p>
   * Distance of halo to the font outline. Max text halo width is 1/4 of the font-size.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextHaloWidth() {
    return jsonObject.get(SymbolOptions.PROPERTY_textHaloWidth).getAsFloat();
  }

  /**
   * Set the TextHaloWidth property
   * <p>
   * Distance of halo to the font outline. Max text halo width is 1/4 of the font-size.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextHaloWidth(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textHaloWidth, value);
  }

  /**
   * Get the TextHaloBlur property
   * <p>
   * The halo's fadeout distance towards the outside.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getTextHaloBlur() {
    return jsonObject.get(SymbolOptions.PROPERTY_textHaloBlur).getAsFloat();
  }

  /**
   * Set the TextHaloBlur property
   * <p>
   * The halo's fadeout distance towards the outside.
   * </p>
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextHaloBlur(Float value) {
    jsonObject.addProperty(SymbolOptions.PROPERTY_textHaloBlur, value);
  }

  @Override
  @Nullable
  Geometry getOffsetGeometry(@NonNull Projection projection, @NonNull MoveDistancesObject moveDistancesObject,
                             float touchAreaShiftX, float touchAreaShiftY) {
    PointF pointF = new PointF(
      moveDistancesObject.getCurrentX() - touchAreaShiftX,
      moveDistancesObject.getCurrentY() - touchAreaShiftY
    );

    LatLng latLng = projection.fromScreenLocation(pointF);
    if (latLng.getLatitude() > MAX_MERCATOR_LATITUDE || latLng.getLatitude() < MIN_MERCATOR_LATITUDE) {
      return null;
    }

    return Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
  }

  @Override
  String getName() {
    return "Symbol";
  }
}
