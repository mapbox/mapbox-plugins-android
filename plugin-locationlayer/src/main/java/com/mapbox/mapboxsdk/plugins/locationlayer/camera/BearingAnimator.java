package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;

public class BearingAnimator extends ValueAnimator {

  private float targetBearing;

  public BearingAnimator(double targetBearing, long duration) {
    setEvaluator(new FloatEvaluator());
    setDuration(duration);
    this.targetBearing = (float) targetBearing;
  }

  public float getTargetBearing() {
    return targetBearing;
  }
}
