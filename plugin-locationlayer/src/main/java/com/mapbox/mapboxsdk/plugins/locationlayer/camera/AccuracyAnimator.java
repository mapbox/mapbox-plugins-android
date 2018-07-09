package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;

public class AccuracyAnimator extends ValueAnimator {

  public AccuracyAnimator(float previous, float target) {
    setEvaluator(new FloatEvaluator());
    setFloatValues(previous, target);
  }
}