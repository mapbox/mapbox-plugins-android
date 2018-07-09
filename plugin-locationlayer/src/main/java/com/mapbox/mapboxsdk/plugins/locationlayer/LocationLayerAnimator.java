package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.AccuracyAnimator;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.BearingAnimator;
import com.mapbox.mapboxsdk.plugins.locationlayer.camera.LatLngAnimator;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.ACCURACY_RADIUS_ANIMATION_DURATION;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.COMPASS_UPDATE_RATE_MS;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.MAX_ANIMATION_DURATION_MS;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.TRANSITION_ANIMATION_DURATION_MS;

final class LocationLayerAnimator {

  private final List<OnLayerAnimationsValuesChangeListener> layerListeners = new ArrayList<>();
  private final List<OnCameraAnimationsValuesChangeListener> cameraListeners = new ArrayList<>();

  private LatLngAnimator layerLatLngAnimator;
  private BearingAnimator layerGpsBearingAnimator;
  private BearingAnimator layerCompassBearingAnimator;
  private AccuracyAnimator accuracyRadiusAnimator;

  private LatLngAnimator cameraLatLngAnimator;
  private BearingAnimator cameraGpsBearingAnimator;
  private BearingAnimator cameraCompassBearingAnimator;

  private Location previousLocation;
  private float previousAccuracyRadius = -1;
  private float previousCompassBearing = -1;
  private long locationUpdateTimestamp = -1;

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
    float targetLayerBearing = newLocation.getBearing();
    float targetCameraBearing = newLocation.getBearing();
    targetCameraBearing = checkGpsNorth(isGpsNorth, targetCameraBearing);

    updateLayerAnimators(previousLayerLatLng, targetLatLng, previousLayerBearing, targetLayerBearing);
    updateCameraAnimators(previousCameraLatLng, previousCameraBearing, targetLatLng, targetCameraBearing);

    playAllLocationAnimators(getAnimationDuration());

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

  void feedNewAccuracyRadius(float targetAccuracyRadius, boolean noAnimation) {
    if (previousAccuracyRadius < 0) {
      previousAccuracyRadius = targetAccuracyRadius;
    }

    float previousAccuracyRadius = getPreviousAccuracyRadius();
    updateAccuracyAnimators(targetAccuracyRadius, previousAccuracyRadius);
    accuracyRadiusAnimator.setDuration(noAnimation ? 0 : ACCURACY_RADIUS_ANIMATION_DURATION);
    accuracyRadiusAnimator.start();

    this.previousAccuracyRadius = targetAccuracyRadius;
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

  private final ValueAnimator.AnimatorUpdateListener accuracyRadiusUpdateListener =
    new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        for (OnLayerAnimationsValuesChangeListener listener : layerListeners) {
          listener.onNewAccuracyRadiusValue((Float) valueAnimator.getAnimatedValue());
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

    void onNewAccuracyRadiusValue(float accuracyRadiusValue);
  }

  interface OnCameraAnimationsValuesChangeListener {
    void onNewLatLngValue(LatLng latLng);

    void onNewGpsBearingValue(float gpsBearing);

    void onNewCompassBearingValue(float compassBearing);
  }

  void resetAllCameraAnimations(CameraPosition currentCameraPosition, boolean isGpsNorth) {
    resetCameraCompassAnimation(currentCameraPosition);
    resetCameraLocationAnimations(currentCameraPosition, isGpsNorth);
    playCameraAnimators(TRANSITION_ANIMATION_DURATION_MS);
  }

  void cancelAllAnimations() {
    cancelLayerLocationAnimations();
    cancelLayerCompassAnimations();
    cancelAccuracyRadiusAnimations();
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

  private float getPreviousAccuracyRadius() {
    float previousRadius;
    if (accuracyRadiusAnimator != null) {
      previousRadius = (float) accuracyRadiusAnimator.getAnimatedValue();
    } else {
      previousRadius = previousAccuracyRadius;
    }
    return previousRadius;
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
    layerLatLngAnimator = new LatLngAnimator(previousLatLng, targetLatLng);
    float normalizedLayerBearing = Utils.shortestRotation(targetBearing, previousBearing);
    layerGpsBearingAnimator = new BearingAnimator(previousBearing, normalizedLayerBearing);

    layerLatLngAnimator.addUpdateListener(layerLatLngUpdateListener);
    layerGpsBearingAnimator.addUpdateListener(layerGpsBearingUpdateListener);
  }

  private void updateCameraAnimators(LatLng previousCameraLatLng, float previousCameraBearing,
                                     LatLng targetLatLng, float targetBearing) {
    cancelCameraLocationAnimations();
    cameraLatLngAnimator = new LatLngAnimator(previousCameraLatLng, targetLatLng);
    cameraLatLngAnimator.addUpdateListener(cameraLatLngUpdateListener);

    float normalizedCameraBearing = Utils.shortestRotation(targetBearing, previousCameraBearing);
    cameraGpsBearingAnimator = new BearingAnimator(previousCameraBearing, normalizedCameraBearing);
    cameraGpsBearingAnimator.addUpdateListener(cameraGpsBearingUpdateListener);
  }

  private long getAnimationDuration() {
    long previousUpdateTimeStamp = locationUpdateTimestamp;
    locationUpdateTimestamp = SystemClock.elapsedRealtime();

    long animationDuration;
    if (previousUpdateTimeStamp == 0) {
      animationDuration = 0;
    } else {
      animationDuration = (long) ((locationUpdateTimestamp - previousUpdateTimeStamp) * 1.1f)
      /*make animation slightly longer*/;
    }

    animationDuration = Math.min(animationDuration, MAX_ANIMATION_DURATION_MS);

    return animationDuration;
  }

  private float checkGpsNorth(boolean isGpsNorth, float targetCameraBearing) {
    if (isGpsNorth) {
      targetCameraBearing = 0;
    }
    return targetCameraBearing;
  }

  private void playAllLocationAnimators(long duration) {
    List<Animator> locationAnimators = new ArrayList<>();
    locationAnimators.add(layerLatLngAnimator);
    locationAnimators.add(layerGpsBearingAnimator);
    locationAnimators.add(cameraLatLngAnimator);
    locationAnimators.add(cameraGpsBearingAnimator);
    AnimatorSet locationAnimatorSet = new AnimatorSet();
    locationAnimatorSet.playTogether(locationAnimators);
    locationAnimatorSet.setInterpolator(new LinearInterpolator());
    locationAnimatorSet.setDuration(duration);
    locationAnimatorSet.start();
  }

  private void playCameraAnimators(long duration) {
    List<Animator> locationAnimators = new ArrayList<>();
    locationAnimators.add(cameraLatLngAnimator);
    locationAnimators.add(cameraGpsBearingAnimator);
    AnimatorSet locationAnimatorSet = new AnimatorSet();
    locationAnimatorSet.playTogether(locationAnimators);
    locationAnimatorSet.setInterpolator(new LinearInterpolator());
    locationAnimatorSet.setDuration(duration);
    locationAnimatorSet.start();
  }

  private void updateCompassAnimators(float targetCompassBearing, float previousLayerBearing,
                                      float previousCameraBearing) {
    cancelLayerCompassAnimations();
    float normalizedLayerBearing = Utils.shortestRotation(targetCompassBearing, previousLayerBearing);
    layerCompassBearingAnimator = new BearingAnimator(previousLayerBearing, normalizedLayerBearing);
    layerCompassBearingAnimator.addUpdateListener(layerCompassBearingUpdateListener);

    cancelCameraCompassAnimations();
    float normalizedCameraBearing = Utils.shortestRotation(targetCompassBearing, previousCameraBearing);
    cameraCompassBearingAnimator = new BearingAnimator(previousCameraBearing, normalizedCameraBearing);
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

  private void updateAccuracyAnimators(float targetAccuracyRadius, float previousAccuracyRadius) {
    cancelAccuracyRadiusAnimations();
    accuracyRadiusAnimator = new AccuracyAnimator(previousAccuracyRadius, targetAccuracyRadius);
    accuracyRadiusAnimator.addUpdateListener(accuracyRadiusUpdateListener);
  }

  private void playAccuracyRadiusAnimation(long duration) {
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

  private void cancelAccuracyRadiusAnimations() {
    if (accuracyRadiusAnimator != null) {
      accuracyRadiusAnimator.cancel();
      accuracyRadiusAnimator.removeAllUpdateListeners();
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

  private void resetCameraLocationAnimations(CameraPosition currentCameraPosition, boolean isGpsNorth) {
    resetCameraLatLngAnimation(currentCameraPosition);
    resetCameraGpsBearingAnimation(currentCameraPosition, isGpsNorth);
  }

  private void cancelCameraLatLngAnimations() {
    if (cameraLatLngAnimator != null) {
      cameraLatLngAnimator.cancel();
      cameraLatLngAnimator.removeAllUpdateListeners();
    }
  }

  private void resetCameraLatLngAnimation(CameraPosition currentCameraPosition) {
    if (cameraLatLngAnimator == null) {
      return;
    }
    cancelCameraLatLngAnimations();
    LatLng currentTarget = cameraLatLngAnimator.getTarget();
    LatLng previousCameraTarget = currentCameraPosition.target;
    cameraLatLngAnimator = new LatLngAnimator(previousCameraTarget, currentTarget);
    cameraLatLngAnimator.addUpdateListener(cameraLatLngUpdateListener);
  }

  private void cancelCameraGpsBearingAnimations() {
    if (cameraGpsBearingAnimator != null) {
      cameraGpsBearingAnimator.cancel();
      cameraGpsBearingAnimator.removeAllUpdateListeners();
    }
  }

  private void resetCameraGpsBearingAnimation(CameraPosition currentCameraPosition, boolean isGpsNorth) {
    if (cameraGpsBearingAnimator == null) {
      return;
    }
    cancelCameraGpsBearingAnimations();
    float currentTargetBearing = cameraGpsBearingAnimator.getTargetBearing();
    currentTargetBearing = checkGpsNorth(isGpsNorth, currentTargetBearing);
    float previousCameraBearing = (float) currentCameraPosition.bearing;
    float normalizedCameraBearing = Utils.shortestRotation(currentTargetBearing, previousCameraBearing);
    cameraGpsBearingAnimator = new BearingAnimator(previousCameraBearing, normalizedCameraBearing);
    cameraGpsBearingAnimator.addUpdateListener(cameraGpsBearingUpdateListener);
  }

  private void cancelCameraCompassAnimations() {
    if (cameraCompassBearingAnimator != null) {
      cameraCompassBearingAnimator.cancel();
      cameraCompassBearingAnimator.removeAllUpdateListeners();
    }
  }

  private void resetCameraCompassAnimation(CameraPosition currentCameraPosition) {
    if (cameraCompassBearingAnimator == null || cameraLatLngAnimator == null) {
      return;
    }
    cancelCameraCompassAnimations();
    float currentTargetBearing = cameraCompassBearingAnimator.getTargetBearing();
    float previousCameraBearing = (float) currentCameraPosition.bearing;
    float normalizedCameraBearing = Utils.shortestRotation(currentTargetBearing, previousCameraBearing);
    cameraCompassBearingAnimator = new BearingAnimator(previousCameraBearing, normalizedCameraBearing);
    cameraCompassBearingAnimator.addUpdateListener(cameraCompassBearingUpdateListener);
  }
}
