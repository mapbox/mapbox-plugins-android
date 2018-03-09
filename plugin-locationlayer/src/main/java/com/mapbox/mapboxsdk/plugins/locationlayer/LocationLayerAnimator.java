package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.BearingAnimator;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.LatLngAnimator;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.COMPASS_UPDATE_RATE_MS;

final class LocationLayerAnimator {

  private final List<OnLayerAnimationsValuesChangeListener> layerListeners = new ArrayList<>();
  private final List<OnCameraAnimationsValuesChangeListener> cameraListeners = new ArrayList<>();

  private LatLngAnimator layerLatLngAnimator;
  private BearingAnimator layerGpsBearingAnimator;
  private BearingAnimator layerCompassBearingAnimator;

  private LatLngAnimator cameraLatLngAnimator;
  private BearingAnimator cameraGpsBearingAnimator;
  private BearingAnimator cameraCompassBearingAnimator;

  private Location previousLocation;
  private float previousCompassBearing = -1;
  private long locationUpdateTimestamp;

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

  void feedNewLocation(@NonNull Location newLocation, @NonNull CameraPosition currentCameraPosition,
                       boolean isGpsNorth) {
    if (previousLocation == null) {
      previousLocation = newLocation;
      locationUpdateTimestamp = SystemClock.elapsedRealtime();
    }

    LatLng previousLayerLatLng = getPreviousLayerLatLng();
    float previousLayerBearing = getPreviousLayerGpsBearing();
    LatLng previousCameraLatLng = currentCameraPosition.target;
    float previousCameraBearing = (float) currentCameraPosition.bearing;

    LatLng targetLatLng = new LatLng(newLocation);
    float targetBearing = newLocation.getBearing();

    updateLayerAnimators(previousLayerLatLng, targetLatLng, previousLayerBearing, targetBearing);
    updateCameraAnimators(previousCameraLatLng, previousCameraBearing, targetLatLng, targetBearing);

    //    long animationDuration = getAnimationDuration();
    playLocationAnimators(1500);

    previousLocation = newLocation;
  }

  void feedNewCompassBearing(float targetCompassBearing, @NonNull CameraPosition currentCameraPosition) {
    if (previousCompassBearing < 0) {
      previousCompassBearing = targetCompassBearing;
    }

    float previousLayerBearing = getPreviousLayerCompassBearing();
    float previousCameraBearing = (float) currentCameraPosition.bearing;

    updateCompassAnimators(targetCompassBearing, previousLayerBearing, previousCameraBearing);
    playCompassAnimators(COMPASS_UPDATE_RATE_MS);

    previousCompassBearing = targetCompassBearing;
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

  void cancelAllCameraAnimations() {
    cancelCameraCompassAnimations();
    cancelCameraLocationAnimations();
  }

  void cancelAllAnimations() {
    cancelLayerLocationAnimations();
    cancelLayerCompassAnimations();
    cancelCameraLocationAnimations();
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

  private void updateLayerAnimators(LatLng previousLatLng, LatLng targetLatLng,
                                    float previousBearing, float targetBearing) {
    cancelLayerLocationAnimations();
    layerLatLngAnimator = new LatLngAnimator(previousLatLng, targetLatLng, 1000);
    float normalizedLayerBearing = Utils.shortestRotation(targetBearing, previousBearing);
    layerGpsBearingAnimator = new BearingAnimator(previousBearing, normalizedLayerBearing, 1000);

    layerLatLngAnimator.addUpdateListener(layerLatLngUpdateListener);
    layerGpsBearingAnimator.addUpdateListener(layerGpsBearingUpdateListener);
  }

  private void updateCameraAnimators(LatLng previousCameraLatLng, float previousCameraBearing,
                                     LatLng targetLatLng, float targetBearing) {
    cancelCameraLocationAnimations();
    cameraLatLngAnimator = new LatLngAnimator(previousCameraLatLng, targetLatLng, 1000);
    cameraLatLngAnimator.addUpdateListener(cameraLatLngUpdateListener);

    float normalizedCameraBearing = Utils.shortestRotation(targetBearing, previousCameraBearing);
    cameraGpsBearingAnimator = new BearingAnimator(previousCameraBearing, normalizedCameraBearing, 1000);
    cameraGpsBearingAnimator.addUpdateListener(cameraGpsBearingUpdateListener);
  }

  private long getAnimationDuration() {
    float previousUpdateTimeStamp = locationUpdateTimestamp;
    locationUpdateTimestamp = SystemClock.elapsedRealtime();

    int animationDuration;
    if (previousUpdateTimeStamp == 0) {
      animationDuration = 0;
    } else {
      animationDuration = (int) ((locationUpdateTimestamp - previousUpdateTimeStamp) * 1.1f)
        /*make animation slightly longer*/;
    }
    return animationDuration;
  }

  private void playLocationAnimators(long duration) {
    List<Animator> locationAnimators = new ArrayList<>();
    locationAnimators.add(layerLatLngAnimator);
    locationAnimators.add(layerGpsBearingAnimator);
    locationAnimators.add(cameraLatLngAnimator);
    locationAnimators.add(cameraGpsBearingAnimator);
    AnimatorSet locationAnimatorSet = new AnimatorSet();
    locationAnimatorSet.playTogether(locationAnimators);
    locationAnimatorSet.setDuration(duration);
    locationAnimatorSet.start();
  }

  private void updateCompassAnimators(float targetCompassBearing, float previousLayerBearing,
                                      float previousCameraBearing) {
    cancelLayerCompassAnimations();
    float normalizedLayerBearing = Utils.shortestRotation(targetCompassBearing, previousLayerBearing);
    layerCompassBearingAnimator = new BearingAnimator(previousLayerBearing, normalizedLayerBearing, 1000);
    layerCompassBearingAnimator.addUpdateListener(layerCompassBearingUpdateListener);

    cancelCameraCompassAnimations();
    float normalizedCameraBearing = Utils.shortestRotation(targetCompassBearing, previousCameraBearing);
    cameraCompassBearingAnimator = new BearingAnimator(previousCameraBearing, normalizedCameraBearing, 1000);
    cameraCompassBearingAnimator.addUpdateListener(cameraCompassBearingUpdateListener);
  }

  private void playCompassAnimators(long duration) {
    List<Animator> compassAnimators = new ArrayList<>();
    compassAnimators.add(layerCompassBearingAnimator);
    compassAnimators.add(cameraCompassBearingAnimator);
    AnimatorSet compassAnimatorSet = new AnimatorSet();
    compassAnimatorSet.playTogether(compassAnimators);
    compassAnimatorSet.setDuration(duration);
    compassAnimatorSet.start();
  }

  private void cancelLayerLocationAnimations() {
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

  private void cancelCameraLocationAnimations() {
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
