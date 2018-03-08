package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.location.Location;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.BearingAnimator;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.LatLngAnimator;

import java.util.ArrayList;
import java.util.List;

final class LocationLayerAnimator {

  private final List<OnLayerAnimationsValuesChangeListener> layerListeners = new ArrayList<>();
  private final List<OnCameraAnimationsValuesChangeListener> cameraListeners = new ArrayList<>();

  private LatLngAnimator layerLatLngAnimator;
  private BearingAnimator layerGpsBearingAnimator;
  private BearingAnimator layerCompassBearingAnimator;

  private LatLngAnimator cameraLatLngAnimator;
  private BearingAnimator cameraGpsBearingAnimator;
  private BearingAnimator cameraCompassBearingAnimator;

  private CameraPosition currentCameraPosition;
  private Location previousLocation;
  private float previousCompassBearing = -1;

  void addLayerListener(OnLayerAnimationsValuesChangeListener listener) {
    layerListeners.add(listener);
  }

  void removeLayerListener(OnLayerAnimationsValuesChangeListener listener) {
    layerListeners.remove(listener);
  }

  void addCameraListener(OnCameraAnimationsValuesChangeListener listener) {
    cameraListeners.add(listener);
  }

  void removeCameraListener(OnCameraAnimationsValuesChangeListener listener) {
    cameraListeners.remove(listener);
  }

  void feedNewLocation(@NonNull Location newLocation) {
    if (previousLocation == null) {
      previousLocation = newLocation;
    }

    LatLng previousLayerLatLng = getPreviousLayerLatLng();
    float previousLayerBearing = getPreviousLayerGpsBearing();
    LatLng previousCameraLatLng = getPreviousCameraLatLng();
    float previousCameraBearing = getPreviousCameraGpsBearing();

    LatLng targetLatLng = new LatLng(newLocation);
    float targetBearing = newLocation.getBearing();

    updateLayerAnimators(previousLayerLatLng, targetLatLng, previousLayerBearing, targetBearing);
    updateCameraAnimators(previousCameraLatLng, previousCameraBearing, targetLatLng, targetBearing);
    playLocationAnimators();

    previousLocation = newLocation;
  }

  void feedNewCompassBearing(float targetCompassBearing) {
    if (previousCompassBearing < 0) {
      previousCompassBearing = targetCompassBearing;
    }

    float previousLayerBearing = getPreviousLayerCompassBearing();
    float previousCameraBearing = getPreviousCameraCompassBearing();

    updateCompassAnimators(targetCompassBearing, previousLayerBearing, previousCameraBearing);
    playCompassAnimators();

    previousCompassBearing = targetCompassBearing;
  }

  void updateCameraPosition(CameraPosition cameraPosition) {
    currentCameraPosition = cameraPosition;
  }

  private final ValueAnimator.AnimatorUpdateListener layerLatLngUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnLayerAnimationsValuesChangeListener listener : layerListeners) {
          listener.onNewLatLngValue((LatLng) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener layerCompassBearingUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnLayerAnimationsValuesChangeListener listener : layerListeners) {
          listener.onNewCompassBearingValue((Float) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener layerGpsBearingUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnLayerAnimationsValuesChangeListener listener : layerListeners) {
          listener.onNewGpsBearingValue((Float) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener cameraLatLngUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnCameraAnimationsValuesChangeListener listener : cameraListeners) {
          listener.onNewLatLngValue((LatLng) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener cameraCompassBearingUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnCameraAnimationsValuesChangeListener listener : cameraListeners) {
          listener.onNewCompassBearingValue((Float) valueAnimator.getAnimatedValue());
        }
      }
    };

  private final ValueAnimator.AnimatorUpdateListener cameraGpsBearingUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnCameraAnimationsValuesChangeListener listener : cameraListeners) {
          listener.onNewGpsBearingValue((Float) valueAnimator.getAnimatedValue());
        }
      }
    };

  interface OnLayerAnimationsValuesChangeListener {
    void onNewLatLngValue(LatLng latLng);

    void onNewGpsBearingValue(float gpsBearing);

    void onNewCompassBearingValue(float compassBearing);
  }

  interface OnCameraAnimationsValuesChangeListener {
    void onNewLatLngValue(LatLng latLng);

    void onNewGpsBearingValue(float gpsBearing);

    void onNewCompassBearingValue(float compassBearing);
  }

  void cancelAllAnimations() {
    cancelLocationLayerAnimations();
    cancelLayerCompassAnimations();
    cancelLocationCameraAnimations();
    cancelCameraCompassAnimations();
  }

  private LatLng getPreviousLayerLatLng() {
    LatLng previousLatLng;
    if (layerLatLngAnimator != null) {
      previousLatLng = (LatLng) layerLatLngAnimator.getAnimatedValue();
    } else {
      previousLatLng = new LatLng(previousLocation);
    }
    return previousLatLng;
  }

  private float getPreviousLayerGpsBearing() {
    float previousBearing;
    if (layerGpsBearingAnimator != null) {
      previousBearing = (float) layerGpsBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = previousLocation.getBearing();
    }
    return previousBearing;
  }

  private float getPreviousLayerCompassBearing() {
    float previousBearing;
    if (layerCompassBearingAnimator != null) {
      previousBearing = (float) layerCompassBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = previousCompassBearing;
    }
    return previousBearing;
  }

  private LatLng getPreviousCameraLatLng() {
    LatLng previousLatLng;
    if (cameraLatLngAnimator != null) {
      previousLatLng = (LatLng) cameraLatLngAnimator.getAnimatedValue();
    } else {
      previousLatLng = currentCameraPosition.target;
    }
    return previousLatLng;
  }

  private float getPreviousCameraGpsBearing() {
    float previousBearing;
    if (cameraGpsBearingAnimator != null) {
      previousBearing = (float) cameraGpsBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = (float) currentCameraPosition.bearing;
    }
    return previousBearing;
  }

  private void updateLayerAnimators(LatLng previousLatLng, LatLng targetLatLng,
                                    float previousBearing, float targetBearing) {
    cancelLocationLayerAnimations();
    layerLatLngAnimator = new LatLngAnimator(previousLatLng, targetLatLng, 1000);
    layerGpsBearingAnimator = new BearingAnimator(previousBearing, targetBearing, 1000);

    layerLatLngAnimator.addUpdateListener(layerLatLngUpdateListener);
    layerGpsBearingAnimator.addUpdateListener(layerGpsBearingUpdateListener);
  }

  private void updateCameraAnimators(LatLng previousCameraLatLng, float previousCameraBearing,
                                     LatLng targetLatLng, float targetBearing) {
    cancelLocationCameraAnimations();
    cameraLatLngAnimator = new LatLngAnimator(previousCameraLatLng, targetLatLng, 1000);
    cameraGpsBearingAnimator = new BearingAnimator(previousCameraBearing, targetBearing, 1000);

    cameraLatLngAnimator.addUpdateListener(cameraLatLngUpdateListener);
    cameraGpsBearingAnimator.addUpdateListener(cameraGpsBearingUpdateListener);
  }

  private void playLocationAnimators() {
    List<Animator> locationAnimators = new ArrayList<>();
    locationAnimators.add(layerLatLngAnimator);
    locationAnimators.add(layerGpsBearingAnimator);
    locationAnimators.add(cameraLatLngAnimator);
    locationAnimators.add(cameraGpsBearingAnimator);
    AnimatorSet locationAnimatorSet = new AnimatorSet();
    locationAnimatorSet.playTogether(locationAnimators);
  }

  private void updateCompassAnimators(float targetCompassBearing, float previousLayerBearing,
                                      float previousCameraBearing) {
    cancelLayerCompassAnimations();
    layerCompassBearingAnimator = new BearingAnimator(previousLayerBearing, targetCompassBearing, 1000);
    layerCompassBearingAnimator.addUpdateListener(layerCompassBearingUpdateListener);

    cancelCameraCompassAnimations();
    cameraCompassBearingAnimator = new BearingAnimator(previousCameraBearing, targetCompassBearing, 1000);
    cameraCompassBearingAnimator.addUpdateListener(cameraCompassBearingUpdateListener);
  }

  private void playCompassAnimators() {
    List<Animator> compassAnimators = new ArrayList<>();
    compassAnimators.add(layerCompassBearingAnimator);
    compassAnimators.add(cameraCompassBearingAnimator);
    AnimatorSet compassAnimatorSet = new AnimatorSet();
    compassAnimatorSet.playTogether(compassAnimators);
  }

  private float getPreviousCameraCompassBearing() {
    float previousBearing;
    if (cameraCompassBearingAnimator != null) {
      previousBearing = (float) cameraCompassBearingAnimator.getAnimatedValue();
    } else {
      previousBearing = (float) currentCameraPosition.bearing;
    }
    return previousBearing;
  }

  private void cancelLocationLayerAnimations() {
    cancelLayerLatLngAnimations();
    cancelLayerGpsBearingAnimations();
  }

  private void cancelLayerLatLngAnimations() {
    if (layerLatLngAnimator != null) {
      layerLatLngAnimator.cancel();
      layerLatLngAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelLayerGpsBearingAnimations() {
    if (layerGpsBearingAnimator != null) {
      layerGpsBearingAnimator.cancel();
      layerGpsBearingAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelLayerCompassAnimations() {
    if (layerCompassBearingAnimator != null) {
      layerCompassBearingAnimator.cancel();
      layerCompassBearingAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelLocationCameraAnimations() {
    cancelCameraLatLngAnimations();
    cancelCameraGpsBearingAnimations();
  }

  private void cancelCameraLatLngAnimations() {
    if (cameraLatLngAnimator != null) {
      cameraLatLngAnimator.cancel();
      cameraLatLngAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelCameraGpsBearingAnimations() {
    if (cameraGpsBearingAnimator != null) {
      cameraGpsBearingAnimator.cancel();
      cameraGpsBearingAnimator.removeAllUpdateListeners();
    }
  }

  private void cancelCameraCompassAnimations() {
    if (cameraCompassBearingAnimator != null) {
      cameraCompassBearingAnimator.cancel();
      cameraCompassBearingAnimator.removeAllUpdateListeners();
    }
  }
}
