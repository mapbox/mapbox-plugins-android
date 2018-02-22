package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class controls the location layer stale state when the {@link android.location.Location} hasn't
 * been updated in 'x' amount of time. {@link LocationLayerOptions#staleStateDelay()} can be used to
 * control the amount of time before the locations considered stale.
 * {@link LocationLayerOptions#enableStaleState()} is available for disabling this behaviour.
 *
 * @since 0.4.0
 */
class StaleStateRunnable implements Runnable {

  private final OnLocationStaleListener innerOnLocationStaleListeners;
  private final Handler handler;
  private boolean isStale;
  private long delayTime;

  StaleStateRunnable(OnLocationStaleListener innerListener, long delayTime) {
    innerOnLocationStaleListeners = innerListener;
    this.delayTime = delayTime;
    handler = new Handler();
  }

  @Override
  public void run() {
    isStale = true;
    innerOnLocationStaleListeners.onStaleStateChange(true);
  }

  boolean isStale() {
    return isStale;
  }

  void updateLatestLocationTime() {
    if (isStale) {
      isStale = false;
      innerOnLocationStaleListeners.onStaleStateChange(false);
    }

    handler.removeCallbacksAndMessages(this);
    handler.postDelayed(this, delayTime);
  }

  void setDelayTime(long delayTime) {
    this.delayTime = delayTime;
    handler.removeCallbacksAndMessages(this);
    handler.postDelayed(this, delayTime);
  }

  void onStart() {
    if (!isStale) {
      handler.postDelayed(this, delayTime);
    }
  }

  void onStop() {
    handler.removeCallbacksAndMessages(this);
  }
}
