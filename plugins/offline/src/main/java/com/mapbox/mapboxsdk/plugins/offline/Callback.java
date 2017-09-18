package com.mapbox.mapboxsdk.plugins.offline;

public interface Callback<T> {
  void onResult(T t);

  void onError(String message);
}
