package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.os.Handler;

class RefreshPluginRunnable implements Runnable {

  private final Handler handler = new Handler();
  private RefreshPluginListener listener;
  private double refreshIntervalMillis;

  RefreshPluginRunnable(double refreshIntervalMillis) {
    this.refreshIntervalMillis = refreshIntervalMillis;
  }

  @Override
  public void run() {
    listener.onShouldRefresh();
    handler.postDelayed(this, (long) refreshIntervalMillis);
  }

  void cancel() {
    handler.removeCallbacksAndMessages(null);
  }

  void setListener(RefreshPluginListener listener) {
    this.listener = listener;
  }

  void updateRefreshInterval(double refreshIntervalMillis) {
    this.refreshIntervalMillis = refreshIntervalMillis;
  }

  interface RefreshPluginListener {
    void onShouldRefresh();
  }
}
