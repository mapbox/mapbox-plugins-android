package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class LatLngAnimator extends ValueAnimator {

  private LatLng target;

  public LatLngAnimator(@NonNull LatLng previous, @NonNull LatLng target) {
    setObjectValues(previous, target);
    setEvaluator(new LatLngEvaluator());
    this.target = target;
  }

  public LatLng getTarget() {
    return target;
  }

  private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

    private final LatLng latLng = new LatLng();

    @Override
    public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
      latLng.setLatitude(startValue.getLatitude()
        + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
      latLng.setLongitude(startValue.getLongitude()
        + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
      return latLng;
    }
  }
}
