package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.IntDef;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Abstract class for all of the plugin animators.
 *
 * @param <K> Data type that will be animated.
 * @param <L> Listener of animation updates.
 */
abstract class LocationLayerAnimator<K, L> extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
  @Retention(RetentionPolicy.SOURCE)
  @IntDef( {
    ANIMATOR_LAYER_LATLNG,
    ANIMATOR_CAMERA_LATLNG
  })
  @interface Type {
  }

  static final int ANIMATOR_LAYER_LATLNG = 0;
  static final int ANIMATOR_CAMERA_LATLNG = 1;

  private final int animatorType = provideAnimatorType();
  final List<L> updateListeners;
  private final K target;

  LocationLayerAnimator(K previous, K target, List<L> updateListeners) {
    setObjectValues(previous, target);
    setEvaluator(provideEvaluator());
    this.updateListeners = updateListeners;
    this.target = target;
    addUpdateListener(this);
  }

  K getTarget() {
    return target;
  }

  @Type
  int getAnimatorType() {
    return animatorType;
  }

  @Type
  abstract int provideAnimatorType();

  abstract TypeEvaluator provideEvaluator();

  interface OnLayerAnimationsValuesChangeListener {
    void onNewLatLngValue(LatLng latLng);

    void onNewGpsBearingValue(float gpsBearing);

    void onNewCompassBearingValue(float compassBearing);

    void onNewAccuracyRadiusValue(float accuracyRadiusValue);
  }

  interface OnCameraAnimationsValuesChangeListener {
    void onNewLatLngValue(LatLng latLng);

    void onNewGpsBearingValue(float gpsBearing);

    void onNewCompassBearingValue(float compassBearing);
  }
}
