package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;

public class ZoomAnimator extends ValueAnimator {

  private float targetZoom;

  public ZoomAnimator(double targetZoom, long duration) {
    setEvaluator(new FloatEvaluator());
    setDuration(duration);
    this.targetZoom = (float) targetZoom;
  }

  public float getTargetZoom() {
    return targetZoom;
  }
}
