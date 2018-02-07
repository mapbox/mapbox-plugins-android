package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class StaleStateRunnable implements Runnable {

  private static StaleStateRunnable instance;

  private List<OnLocationStaleListener> onLocationStaleListeners;
  private Handler handler;
  private long delayTime;

  static StaleStateRunnable getInstance() {
    if (instance == null) {
      instance = new StaleStateRunnable();
    }
    return instance;
  }

  private StaleStateRunnable() {
    onLocationStaleListeners = new ArrayList<>();
    handler = new Handler();
  }

  void addOnLocationStaleListener(@NonNull OnLocationStaleListener onLocationStaleListener) {
    onLocationStaleListeners.add(onLocationStaleListener);
  }

  void removeOnLocationStaleListener(@NonNull OnLocationStaleListener onLocationStaleListener) {
    onLocationStaleListeners.remove(onLocationStaleListener);
  }

  void removeAllListeners() {
    onLocationStaleListeners.clear();
  }

  @Override
  public void run() {
    for (OnLocationStaleListener listener : onLocationStaleListeners) {
      listener.isLocationStale(true);
    }
  }

  void updateLatestLocationTime() {
    for (OnLocationStaleListener listener : onLocationStaleListeners) {
      listener.isLocationStale(false);
    }
    handler.removeCallbacks(this);
    handler.postDelayed(this, delayTime);
  }

  void setDelayTime(long delayTime) {
    this.delayTime = delayTime;
  }

  void onStop() {
    removeAllListeners();
    handler.removeCallbacks(this);
  }
}
