package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

class StaleStateRunnable implements Runnable {

  private static StaleStateRunnable instance;

  private static final int DEFAULT_DELAY_TIME_TILL_STALE = 10000;

  private List<OnLocationStaleListener> onLocationStaleListeners;
  private Handler handler;
  private long delayTime = DEFAULT_DELAY_TIME_TILL_STALE;

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

  void removeOnLocationStaleListener(@Nullable OnLocationStaleListener onLocationStaleListener) {
    if (onLocationStaleListener == null) {
      onLocationStaleListeners.clear();
      return;
    }
    onLocationStaleListeners.remove(onLocationStaleListener);
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
    handler.removeCallbacks(this);
  }
}
