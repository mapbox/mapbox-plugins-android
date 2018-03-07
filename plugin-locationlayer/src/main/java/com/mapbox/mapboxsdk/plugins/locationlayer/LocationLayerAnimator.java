package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.ValueAnimator;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

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

  private Location previousLocation;
  private float previousCompassBearing = -1;

  void addListener(OnAnimationsValuesChangeListener listener) {
    listeners.add(listener);
  }

  void removeListener(OnAnimationsValuesChangeListener listener) {
    listeners.remove(listener);
  }

  void feedNewLocation(@NonNull Location newLocation) {
    if (previousLocation == null) {
      previousLocation = newLocation;
    }

    LatLng previousLatLng = getPreviousLatLng();
    LatLng targetLatLng = new LatLng(newLocation);

    float previousBearing = getPreviousGpsBearing();
    float targetBearing = newLocation.getBearing();

    // TODO create animator duration / interpolator
//    float speed = location == null ? 0 : location.getSpeed();
//
//    locationChangeAnimator.setDuration(linearAnimation || speed > 0
//      ? getLocationUpdateDuration() : LocationLayerConstants.LOCATION_UPDATE_DELAY_MS);
//    if (linearAnimation || speed > 0) {
//      locationChangeAnimator.setInterpolator(new LinearInterpolator());
//    } else {
//      locationChangeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//    }

    cancelLocationAnimations();
    latLngAnimator = new LatLngAnimator(previousLatLng, targetLatLng, 1000);
    gpsBearingAnimator = new BearingAnimator(previousBearing, targetBearing, 1000);

    latLngAnimator.addUpdateListener(latLngUpdateListener);
    gpsBearingAnimator.addUpdateListener(gpsBearingUpdateListener);

    latLngAnimator.start();
    gpsBearingAnimator.start();

    previousLocation = newLocation;
  }

  void feedNewCompassBearing(float targetCompassBearing) {
    if (previousCompassBearing < 0) {
      previousCompassBearing = targetCompassBearing;
    }

    float previousBearing = getPreviousCompassBearing();

    cancelCompassAnimations();
    compassBearingAnimator = new BearingAnimator(previousBearing, targetCompassBearing, 1000);
    compassBearingAnimator.setInterpolator(new LinearInterpolator());

    compassBearingAnimator.addUpdateListener(compassBearingUpdateListener);
    compassBearingAnimator.start();

    previousCompassBearing = targetCompassBearing;
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

  private LatLng getPreviousLatLng() {
    LatLng previousLatLng;
    if (latLngAnimator != null) {
      previousLatLng = (LatLng) latLngAnimator.getAnimatedValue();
    } else {
      previousLatLng = new LatLng(previousLocation);
    }
    return previousLatLng;
  }

  private float getPreviousGpsBearing() {
    float previousBearing;
    if (gpsBearingAnimator != null) {
      previousBearing = (float) gpsBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = previousLocation.getBearing();
    }
    return previousBearing;
  }

  private float getPreviousCompassBearing() {
    float previousBearing;
    if (compassBearingAnimator != null) {
      previousBearing = (float) compassBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = previousCompassBearing;
    }
    return previousBearing;
  }

  private void cancelLocationAnimations() {
    cancelLatLngAnimations();
    cancelGpsBearingAnimations();
  }

  private void cancelLatLngAnimations() {
    if (latLngAnimator != null) {
      latLngAnimator.cancel();
      latLngAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelGpsBearingAnimations() {
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
