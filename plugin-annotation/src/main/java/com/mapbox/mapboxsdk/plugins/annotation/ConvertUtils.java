package com.mapbox.mapboxsdk.plugins.annotation;

import com.google.gson.JsonArray;

class ConvertUtils {
  static JsonArray convertArray(Float[] value) {
    JsonArray jsonArray = new JsonArray();
    if (value != null) {
      for (Float element : value) {
        jsonArray.add(element);
      }
    }
    return jsonArray;
  }

  static JsonArray convertArray(String[] value) {
    JsonArray jsonArray = new JsonArray();
    if (value != null) {
      for (String element : value) {
        jsonArray.add(element);
      }
    }
    return jsonArray;
  }
}
