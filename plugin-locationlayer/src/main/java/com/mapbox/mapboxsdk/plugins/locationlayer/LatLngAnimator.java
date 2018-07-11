package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.TypeEvaluator;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

abstract class LatLngAnimator<L> extends LocationLayerAnimator<LatLng, L> {

  LatLngAnimator(LatLng previous, LatLng target, List<L> updateListeners) {
    super(previous, target, updateListeners);
  }

  @Override
  TypeEvaluator provideEvaluator() {
    return new LatLngEvaluator();
  }
}
