package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.ValueAnimator;
import android.location.Location;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.BearingAnimator;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.LatLngAnimator;

import java.util.ArrayList;
import java.util.List;

final class LocationLayerAnimator {

  private final List<OnAnimationsValuesChangeListener> listeners = new ArrayList<>();
  private LatLngAnimator latLngAnimator;
  private BearingAnimator gpsBearingAnimator;
  private BearingAnimator compassBearingAnimator;

  void addListener(OnAnimationsValuesChangeListener listener) {
    listeners.add(listener);
  }

  void removeListener(OnAnimationsValuesChangeListener listener) {
    listeners.remove(listener);
  }

  void feedNewLocation(@NonNull Location previousLocation, @NonNull Location newLocation) {
    LatLng previousLatLng;
    if (latLngAnimator != null) {
      previousLatLng = (LatLng) latLngAnimator.getAnimatedValue();
    } else {
      previousLatLng = new LatLng(previousLocation);
    }
    LatLng newLatLng = new LatLng(newLocation);

    float previousBearing;
    if (gpsBearingAnimator != null) {
      previousBearing = (float) gpsBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = previousLocation.getBearing();
    }

    cancelLocationAnimations();
    latLngAnimator = new LatLngAnimator(previousLatLng, newLatLng, 1000);
    gpsBearingAnimator = new BearingAnimator(previousBearing, newLocation.getBearing(), 1000);
    // FIXME: 22/02/2018 evaluate duration of animation better

    latLngAnimator.addUpdateListener(latLngUpdateListener);
    gpsBearingAnimator.addUpdateListener(gpsBearingUpdateListener);

    latLngAnimator.start();
    gpsBearingAnimator.start();
  }

  void feedNewCompassBearing(float previousCompassBearing, float targetCompassBearing) {
    cancelCompassAnimations();
    compassBearingAnimator = new BearingAnimator(previousCompassBearing, targetCompassBearing, 1000);
    // FIXME: 22/02/2018 evaluate duration of animation better

    compassBearingAnimator.addUpdateListener(compassBearingUpdateListener);
    compassBearingAnimator.start();
  }

  private final ValueAnimator.AnimatorUpdateListener latLngUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnAnimationsValuesChangeListener listener : listeners) {
          listener.onNewLatLngValue((LatLng) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener compassBearingUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnAnimationsValuesChangeListener listener : listeners) {
          listener.onNewCompassBearingValue((Float) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener gpsBearingUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnAnimationsValuesChangeListener listener : listeners) {
          listener.onNewGpsBearingValue((Float) valueAnimator.getAnimatedValue());
        }
      }
    };

  interface OnAnimationsValuesChangeListener {
    void onNewLatLngValue(LatLng latLng);

    void onNewGpsBearingValue(float gpsBearing);

    void onNewCompassBearingValue(float compassBearing);
  }

  void cancelAllAnimations() {
    cancelLocationAnimations();
    cancelCompassAnimations();
  }

  private void cancelLocationAnimations() {
    if (latLngAnimator != null) {
      latLngAnimator.cancel();
      latLngAnimator.removeAllUpdateListeners();
    }

    if (gpsBearingAnimator != null) {
      gpsBearingAnimator.cancel();
      gpsBearingAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelCompassAnimations() {
    if (compassBearingAnimator != null) {
      compassBearingAnimator.cancel();
      compassBearingAnimator.removeAllUpdateListeners();
    }
  }
}
