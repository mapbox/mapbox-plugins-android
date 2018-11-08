package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.Nullable;

import com.google.gson.JsonArray;

class ConvertUtils {

  @Nullable
  static JsonArray convertArray(Float[] value) {
    if (value != null) {
      JsonArray jsonArray = new JsonArray();
      for (Float element : value) {
        jsonArray.add(element);
      }
      return jsonArray;
    } else {
      return null;
    }
  }

  @Nullable
  static JsonArray convertArray(String[] value) {
    if (value != null) {
      JsonArray jsonArray = new JsonArray();
      for (String element : value) {
        jsonArray.add(element);
      }
      return jsonArray;
    } else {
      return null;
    }
  }
}
