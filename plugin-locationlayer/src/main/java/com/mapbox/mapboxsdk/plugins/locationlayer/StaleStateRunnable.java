package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class controls the location layer stale state when the {@link android.location.Location} hasn't
 * been updated in 'x' amount of time. {@link LocationLayerOptions#staleStateDelay()} can be used to
 * control the amount of time before the locations considered stale.
 * {@link LocationLayerOptions#enableStaleState()} is avaliable for disabling this behaviour.
 *
 * @since 0.4.0
 */
class StaleStateRunnable implements Runnable {

  private final List<OnLocationStaleListener> onLocationStaleListeners;
  private final Handler handler;
  private boolean isStale;
  private long delayTime;

  StaleStateRunnable(long delayTime) {
    this.delayTime = delayTime;
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
    isStale = true;
  }

  boolean isStale() {
    return isStale;
  }

  void updateLatestLocationTime() {
    for (OnLocationStaleListener listener : onLocationStaleListeners) {
      listener.isLocationStale(false);
    }
    isStale = false;
    handler.removeCallbacks(this);
    handler.postDelayed(this, delayTime);
  }

  /**
   * Reset the stale state when {@link LocationLayerOptions#enableStaleState()} is set to false.
   *
   * @since 0.4.0
   */
  void reset() {
    for (OnLocationStaleListener listener : onLocationStaleListeners) {
      listener.isLocationStale(false);
    }
    isStale = false;
    handler.removeCallbacks(this);
  }

  void setDelayTime(long delayTime) {
    this.delayTime = delayTime;
  }

  void onStop() {
    removeAllListeners();
    handler.removeCallbacks(this);
  }
}
