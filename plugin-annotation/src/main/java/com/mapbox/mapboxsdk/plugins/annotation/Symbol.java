// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.ColorInt;
import android.graphics.PointF;
import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.mapboxsdk.maps.Projection;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.constants.GeometryConstants.MAX_MERCATOR_LATITUDE;
import static com.mapbox.mapboxsdk.constants.GeometryConstants.MIN_MERCATOR_LATITUDE;

@UiThread
public class Symbol extends Annotation {

  public static final String Z_INDEX = "z-index";

  /**
   * Create a symbol.
   *
   * @param id            the id of the symbol
   * @param jsonObject the features of the annotation
   * @param geometry the geometry of the annotation
   */
  Symbol(long id, JsonObject jsonObject, Geometry geometry) {
    super(id, jsonObject, geometry);
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
    Point point = (Point) geometry;
    return new LatLng(point.latitude(), point.longitude());
  }

  /**
   * Set the Geometry of the symbol, which represents the location of the symbol on the map
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param geometry the geometry of the symbol
   */
  public void setGeometry(Point geometry) {
    this.geometry = geometry;
  }

  /**
   * Get the Geometry of the symbol, which represents the location of the symbol on the map
   *
   * @return the geometry of the symbol
   */
  public Point getGeometry() {
    return ((Point) geometry);
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
    jsonObject.addProperty(Z_INDEX, index);
  }

  /**
   * Get the z-index of a symbol.
   *
   * @return the z-index value, 0 if not set
   */
  public int getZIndex() {
    return jsonObject.get(Z_INDEX).getAsInt();
  }

  // Property accessors

  /**
   * Get the IconSize property
   *
   * @return property wrapper value around Float
   */
  public Float getIconSize() {
    return jsonObject.get("icon-size").getAsFloat();
  }

  /**
   * Set the IconSize property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconSize(Float value) {
    jsonObject.addProperty("icon-size", value);
  }

  /**
   * Get the IconImage property
   *
   * @return property wrapper value around String
   */
  public String getIconImage() {
    return jsonObject.get("icon-image").getAsString();
  }

  /**
   * Set the IconImage property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setIconImage(String value) {
    jsonObject.addProperty("icon-image", value);
  }

  /**
   * Get the IconRotate property
   *
   * @return property wrapper value around Float
   */
  public Float getIconRotate() {
    return jsonObject.get("icon-rotate").getAsFloat();
  }

  /**
   * Set the IconRotate property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconRotate(Float value) {
    jsonObject.addProperty("icon-rotate", value);
  }

  /**
   * Get the IconOffset property
   *
   * @return PointF value for Float[]
   */
  public PointF getIconOffset() {
    JsonArray jsonArray = jsonObject.getAsJsonArray("icon-offset");
    return new PointF(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat());
  }

  /**
   * Set the IconOffset property.
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   * @param pointF value for Float[]
   */
  public void setIconOffset(PointF pointF) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(pointF.x);
    jsonArray.add(pointF.y);
    jsonObject.add("icon-offset", jsonArray);
  }

  /**
   * Get the IconAnchor property
   *
   * @return property wrapper value around String
   */
  public String getIconAnchor() {
    return jsonObject.get("icon-anchor").getAsString();
  }

  /**
   * Set the IconAnchor property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setIconAnchor(@Property.ICON_ANCHOR String value) {
    jsonObject.addProperty("icon-anchor", value);
  }

  /**
   * Get the TextField property
   *
   * @return property wrapper value around String
   */
  public String getTextField() {
    return jsonObject.get("text-field").getAsString();
  }

  /**
   * Set the TextField property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextField(String value) {
    jsonObject.addProperty("text-field", value);
  }

  /**
   * Get the TextFont property
   *
   * @return property wrapper value around String[]
   */
  public String[] getTextFont() {
    JsonArray jsonArray = jsonObject.getAsJsonArray("text-font");
    String[] value = new String[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      value[i] = jsonArray.get(i).getAsString();
    }
    return value;
  }

  /**
   * Set the TextFont property.
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   * @param value constant property value for String[]
   */
  public void setTextFont(String[] value) {
    JsonArray jsonArray = new JsonArray();
    for (String element : value) {
      jsonArray.add(element);
    }
    jsonObject.add("text-font", jsonArray);
  }

  /**
   * Get the TextSize property
   *
   * @return property wrapper value around Float
   */
  public Float getTextSize() {
    return jsonObject.get("text-size").getAsFloat();
  }

  /**
   * Set the TextSize property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextSize(Float value) {
    jsonObject.addProperty("text-size", value);
  }

  /**
   * Get the TextMaxWidth property
   *
   * @return property wrapper value around Float
   */
  public Float getTextMaxWidth() {
    return jsonObject.get("text-max-width").getAsFloat();
  }

  /**
   * Set the TextMaxWidth property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextMaxWidth(Float value) {
    jsonObject.addProperty("text-max-width", value);
  }

  /**
   * Get the TextLetterSpacing property
   *
   * @return property wrapper value around Float
   */
  public Float getTextLetterSpacing() {
    return jsonObject.get("text-letter-spacing").getAsFloat();
  }

  /**
   * Set the TextLetterSpacing property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextLetterSpacing(Float value) {
    jsonObject.addProperty("text-letter-spacing", value);
  }

  /**
   * Get the TextJustify property
   *
   * @return property wrapper value around String
   */
  public String getTextJustify() {
    return jsonObject.get("text-justify").getAsString();
  }

  /**
   * Set the TextJustify property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextJustify(@Property.TEXT_JUSTIFY String value) {
    jsonObject.addProperty("text-justify", value);
  }

  /**
   * Get the TextAnchor property
   *
   * @return property wrapper value around String
   */
  public String getTextAnchor() {
    return jsonObject.get("text-anchor").getAsString();
  }

  /**
   * Set the TextAnchor property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextAnchor(@Property.TEXT_ANCHOR String value) {
    jsonObject.addProperty("text-anchor", value);
  }

  /**
   * Get the TextRotate property
   *
   * @return property wrapper value around Float
   */
  public Float getTextRotate() {
    return jsonObject.get("text-rotate").getAsFloat();
  }

  /**
   * Set the TextRotate property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextRotate(Float value) {
    jsonObject.addProperty("text-rotate", value);
  }

  /**
   * Get the TextTransform property
   *
   * @return property wrapper value around String
   */
  public String getTextTransform() {
    return jsonObject.get("text-transform").getAsString();
  }

  /**
   * Set the TextTransform property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for String
   */
  public void setTextTransform(@Property.TEXT_TRANSFORM String value) {
    jsonObject.addProperty("text-transform", value);
  }

  /**
   * Get the TextOffset property
   *
   * @return PointF value for Float[]
   */
  public PointF getTextOffset() {
    JsonArray jsonArray = jsonObject.getAsJsonArray("text-offset");
    return new PointF(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat());
  }

  /**
   * Set the TextOffset property.
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   * @param pointF value for Float[]
   */
  public void setTextOffset(PointF pointF) {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(pointF.x);
    jsonArray.add(pointF.y);
    jsonObject.add("text-offset", jsonArray);
  }

  /**
   * Get the IconOpacity property
   *
   * @return property wrapper value around Float
   */
  public Float getIconOpacity() {
    return jsonObject.get("icon-opacity").getAsFloat();
  }

  /**
   * Set the IconOpacity property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconOpacity(Float value) {
    jsonObject.addProperty("icon-opacity", value);
  }

  /**
   * Get the IconColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getIconColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("icon-color").getAsString());
  }

  /**
   * Set the IconColor property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setIconColor(@ColorInt int color) {
    jsonObject.addProperty("icon-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the IconHaloColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getIconHaloColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("icon-halo-color").getAsString());
  }

  /**
   * Set the IconHaloColor property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setIconHaloColor(@ColorInt int color) {
    jsonObject.addProperty("icon-halo-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the IconHaloWidth property
   *
   * @return property wrapper value around Float
   */
  public Float getIconHaloWidth() {
    return jsonObject.get("icon-halo-width").getAsFloat();
  }

  /**
   * Set the IconHaloWidth property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconHaloWidth(Float value) {
    jsonObject.addProperty("icon-halo-width", value);
  }

  /**
   * Get the IconHaloBlur property
   *
   * @return property wrapper value around Float
   */
  public Float getIconHaloBlur() {
    return jsonObject.get("icon-halo-blur").getAsFloat();
  }

  /**
   * Set the IconHaloBlur property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setIconHaloBlur(Float value) {
    jsonObject.addProperty("icon-halo-blur", value);
  }

  /**
   * Get the TextOpacity property
   *
   * @return property wrapper value around Float
   */
  public Float getTextOpacity() {
    return jsonObject.get("text-opacity").getAsFloat();
  }

  /**
   * Set the TextOpacity property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextOpacity(Float value) {
    jsonObject.addProperty("text-opacity", value);
  }

  /**
   * Get the TextColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getTextColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("text-color").getAsString());
  }

  /**
   * Set the TextColor property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setTextColor(@ColorInt int color) {
    jsonObject.addProperty("text-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the TextHaloColor property
   *
   * @return color value for String
   */
  @ColorInt
  public int getTextHaloColor() {
    return ColorUtils.rgbaToColor(jsonObject.get("text-halo-color").getAsString());
  }

  /**
   * Set the TextHaloColor property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param color value for String
   */
  public void setTextHaloColor(@ColorInt int color) {
    jsonObject.addProperty("text-halo-color", ColorUtils.colorToRgbaString(color));
  }

  /**
   * Get the TextHaloWidth property
   *
   * @return property wrapper value around Float
   */
  public Float getTextHaloWidth() {
    return jsonObject.get("text-halo-width").getAsFloat();
  }

  /**
   * Set the TextHaloWidth property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextHaloWidth(Float value) {
    jsonObject.addProperty("text-halo-width", value);
  }

  /**
   * Get the TextHaloBlur property
   *
   * @return property wrapper value around Float
   */
  public Float getTextHaloBlur() {
    return jsonObject.get("text-halo-blur").getAsFloat();
  }

  /**
   * Set the TextHaloBlur property
   * <p>
   * To update the symbol on the map use {@link SymbolManager#update(Annotation)}.
   * <p>
   *
   * @param value constant property value for Float
   */
  public void setTextHaloBlur(Float value) {
    jsonObject.addProperty("text-halo-blur", value);
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
}
