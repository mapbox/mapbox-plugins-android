package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;

public class BearingAnimator extends ValueAnimator {

  private float targetBearing;

  public BearingAnimator(float previous, float target, long duration) {
    setEvaluator(new FloatEvaluator());
    setDuration(duration);
    setFloatValues(previous, target);
    this.targetBearing = target;
  }

  public float getTargetBearing() {
    return targetBearing;
  }
}
