// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.UiThread;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

@UiThread
public class Symbol {

  public static final String ID_KEY = "id-symbol";
  //public static final String Z_INDEX = "z-index";
  private final SymbolManager symbolManager;
  private final JsonObject jsonObject = new JsonObject();
  private Geometry geometry;

  /**
   * Create a symbol.
   *
   * @param manager the symbol manager created and managing the symbol
   * @param id            the id of the symbol
   */
  Symbol(SymbolManager manager, long id) {
    this.symbolManager = manager;
    this.jsonObject.addProperty(ID_KEY, id);
    //this.jsonObject.addProperty(Z_INDEX, 0);
  }

  /**
   * Get the symbol geometry.
   *
   * @return the symbol geometry
   */
  Geometry getGeometry() {
    if (geometry == null) {
      throw new IllegalStateException();
    }
    return geometry;
  }

  /**
   * Get the symbol feature properties.
   *
   * @return the symbol feature properties
   */
  JsonObject getFeature() {
    return jsonObject;
  }

  /**
   * Get the symbol id.
   *
   * @return the symbol id
   */
  public long getId() {
    return jsonObject.get(ID_KEY).getAsLong();
  }

  /**
   * Set the LatLng of the symbol, which represents the location of the symbol on the map
   *
   * @param latLng the location of the symbol in a longitude and latitude pair
   */
  public void setLatLng(LatLng latLng) {
    geometry = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
    symbolManager.updateSource();
  }

  ///**
  // * Set the z-index of a symbol.
  // * <p>
  // * If a symbol z-index is higher as another symbol it will be rendered above it.
  // * </p>
  // * <p>
  // * Default value is 0.
  // * </p>
  // *
  // * @param index the z-index value
  // */
  //public void setZIndex(int index) {
  //  jsonObject.addProperty(Z_INDEX, index);
  //  symbolManager.updateSource();
  //}

  ///**
  // * Get the z-index of a symbol.
  // *
  // * @return the z-index value, 0 if not set
  // */
  //public int getZIndex() {
  //  return jsonObject.get(Z_INDEX).getAsInt();
  //}

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
   *
   * @param value constant property value for Float
   */
  public void setIconSize(Float value) {
    jsonObject.addProperty("icon-size", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setIconImage(String value) {
    jsonObject.addProperty("icon-image", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setIconRotate(Float value) {
    jsonObject.addProperty("icon-rotate", value);
    symbolManager.updateSource();
  }

  /**
   * Get the IconOffset property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getIconOffset() {
    JsonArray jsonArray = jsonObject.getAsJsonArray("icon-offset");
    Float[] value = new Float[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      value[i] = jsonArray.get(i).getAsFloat();
    }
    return value;
  }

  /**
   * Set the IconOffset property
   *
   * @param value constant property value for Float[]
   */
  public void setIconOffset(Float[] value) {
    JsonArray jsonArray = new JsonArray();
    for (Float element : value) {
      jsonArray.add(element);
    }
    jsonObject.add("icon-offset", jsonArray);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setIconAnchor(@Property.ICON_ANCHOR String value) {
    jsonObject.addProperty("icon-anchor", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setTextField(String value) {
    jsonObject.addProperty("text-field", value);
    symbolManager.updateSource();
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
   * Set the TextFont property
   *
   * @param value constant property value for String[]
   */
  public void setTextFont(String[] value) {
    JsonArray jsonArray = new JsonArray();
    for (String element : value) {
      jsonArray.add(element);
    }
    jsonObject.add("text-font", jsonArray);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextSize(Float value) {
    jsonObject.addProperty("text-size", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextMaxWidth(Float value) {
    jsonObject.addProperty("text-max-width", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextLetterSpacing(Float value) {
    jsonObject.addProperty("text-letter-spacing", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setTextJustify(@Property.TEXT_JUSTIFY String value) {
    jsonObject.addProperty("text-justify", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setTextAnchor(@Property.TEXT_ANCHOR String value) {
    jsonObject.addProperty("text-anchor", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextRotate(Float value) {
    jsonObject.addProperty("text-rotate", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for String
   */
  public void setTextTransform(@Property.TEXT_TRANSFORM String value) {
    jsonObject.addProperty("text-transform", value);
    symbolManager.updateSource();
  }

  /**
   * Get the TextOffset property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getTextOffset() {
    JsonArray jsonArray = jsonObject.getAsJsonArray("text-offset");
    Float[] value = new Float[jsonArray.size()];
    for (int i = 0; i < jsonArray.size(); i++) {
      value[i] = jsonArray.get(i).getAsFloat();
    }
    return value;
  }

  /**
   * Set the TextOffset property
   *
   * @param value constant property value for Float[]
   */
  public void setTextOffset(Float[] value) {
    JsonArray jsonArray = new JsonArray();
    for (Float element : value) {
      jsonArray.add(element);
    }
    jsonObject.add("text-offset", jsonArray);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setIconOpacity(Float value) {
    jsonObject.addProperty("icon-opacity", value);
    symbolManager.updateSource();
  }

  /**
   * Get the IconColor property
   *
   * @return property wrapper value around String
   */
  public String getIconColor() {
    return jsonObject.get("icon-color").getAsString();
  }

  /**
   * Set the IconColor property
   *
   * @param value constant property value for String
   */
  public void setIconColor(String value) {
    jsonObject.addProperty("icon-color", value);
    symbolManager.updateSource();
  }

  /**
   * Get the IconHaloColor property
   *
   * @return property wrapper value around String
   */
  public String getIconHaloColor() {
    return jsonObject.get("icon-halo-color").getAsString();
  }

  /**
   * Set the IconHaloColor property
   *
   * @param value constant property value for String
   */
  public void setIconHaloColor(String value) {
    jsonObject.addProperty("icon-halo-color", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setIconHaloWidth(Float value) {
    jsonObject.addProperty("icon-halo-width", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setIconHaloBlur(Float value) {
    jsonObject.addProperty("icon-halo-blur", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextOpacity(Float value) {
    jsonObject.addProperty("text-opacity", value);
    symbolManager.updateSource();
  }

  /**
   * Get the TextColor property
   *
   * @return property wrapper value around String
   */
  public String getTextColor() {
    return jsonObject.get("text-color").getAsString();
  }

  /**
   * Set the TextColor property
   *
   * @param value constant property value for String
   */
  public void setTextColor(String value) {
    jsonObject.addProperty("text-color", value);
    symbolManager.updateSource();
  }

  /**
   * Get the TextHaloColor property
   *
   * @return property wrapper value around String
   */
  public String getTextHaloColor() {
    return jsonObject.get("text-halo-color").getAsString();
  }

  /**
   * Set the TextHaloColor property
   *
   * @param value constant property value for String
   */
  public void setTextHaloColor(String value) {
    jsonObject.addProperty("text-halo-color", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextHaloWidth(Float value) {
    jsonObject.addProperty("text-halo-width", value);
    symbolManager.updateSource();
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
   *
   * @param value constant property value for Float
   */
  public void setTextHaloBlur(Float value) {
    jsonObject.addProperty("text-halo-blur", value);
    symbolManager.updateSource();
  }

}
