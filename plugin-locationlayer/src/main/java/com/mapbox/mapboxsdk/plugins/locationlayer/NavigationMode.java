package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.location.Location;
import android.os.SystemClock;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

class NavigationMode {

  private final MapboxMap mapboxMap;
  private final MapView mapView;

  private ImageView imageView;
  private boolean tracking;

  private ValueAnimator locationAnimator;
  private ValueAnimator bearingAnimator;
  private long locationUpdateTime;

  NavigationMode(MapboxMap mapboxMap, MapView mapView) {
    this.mapboxMap = mapboxMap;
    this.mapView = mapView;
  }

  void startTracking() {
    if(tracking){
      return;
    }

    tracking = true;
    showLocationPuck();
    enableFocalPoint();
    enableTrackCameraChanges();
  }

  void stopTracking() {
    if(!tracking){
      return;
    }

    tracking = false;
    hideLocationPuck();
    disableTrackCameraChanges();
    disableTrackCameraChanges();
    resetLocationAnimator();
    resetBearingAnimator();
  }

  //
  // View code
  //

  private void showLocationPuck() {
    // add image to map
    if (imageView == null) {
      imageView = new ImageView(mapView.getContext());
      imageView.setImageResource(R.drawable.mapbox_user_puck_icon);

      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
      layoutParams.setMargins(0, mapView.getHeight() / 4, 0, 0);
      imageView.setLayoutParams(layoutParams);
      mapView.addView(imageView);
    } else {
      imageView.setVisibility(View.VISIBLE);
    }
  }

  private void hideLocationPuck() {
    imageView.setVisibility(View.GONE);
  }

  //
  // Focal point
  //

  private void enableFocalPoint() {
    float centerWidth = mapboxMap.getWidth() / 2;
    float centerHeight = mapboxMap.getHeight() / 2;
    float offset = mapView.getHeight() / 4;
    mapboxMap.getUiSettings().setFocalPoint(new PointF(centerWidth, centerHeight + offset));
  }

  private void disableFocalPoint() {
    mapboxMap.getUiSettings().setFocalPoint(null);
  }

  //
  // Camera changes
  //

  private void enableTrackCameraChanges() {
    mapboxMap.setOnCameraChangeListener(new CameraInvalidationListener(imageView));
  }

  private void disableTrackCameraChanges() {
    mapboxMap.setOnCameraChangeListener(null);
  }

  //
  // Location state
  //

  void onLocationChanged(Location newLocation) {
    if (tracking) {
      createLatLngAnimator(getLatLng(), new LatLng(newLocation), calculateAnimationDuration()).start();
      createBearingAnimator(getBearing(), newLocation.getBearing());
    }
  }

  private float getBearing() {
    float previousHeading;
    if (bearingAnimator != null) {
      previousHeading = (Float) bearingAnimator.getAnimatedValue();
      bearingAnimator.end();
    } else {
      previousHeading = (float) mapboxMap.getCameraPosition().bearing;
    }
    return previousHeading;
  }

  private LatLng getLatLng() {
    LatLng current;
    if (locationAnimator != null) {
      current = (LatLng) locationAnimator.getAnimatedValue();
      locationAnimator.end();
    } else {
      current = mapboxMap.getCameraPosition().target;
    }
    return current;
  }


  //
  // Animator code
  //

  private Animator createLatLngAnimator(LatLng currentPosition, LatLng target, long duration) {
    locationAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), currentPosition, target);
    locationAnimator.setDuration(duration);
    locationAnimator.addUpdateListener(locationUpdateListener);
    locationAnimator.addListener(locationEndListener);
    return locationAnimator;
  }

  private Animator createBearingAnimator(float currentBearing, float targetBearing) {
    bearingAnimator = ValueAnimator.ofFloat(currentBearing, targetBearing);
    bearingAnimator.setDuration(450);
    bearingAnimator.setInterpolator(new FastOutSlowInInterpolator());
    bearingAnimator.addUpdateListener(bearingUpdateListener);
    bearingAnimator.addListener(bearingEndListener);
    return bearingAnimator;
  }

  private long calculateAnimationDuration() {
    long locationUpdateTime = SystemClock.elapsedRealtime();
    long duration = (long) ((locationUpdateTime - this.locationUpdateTime) * 1.1);
    this.locationUpdateTime = locationUpdateTime;
    return duration;
  }

  private void resetLocationAnimator() {
    if (locationAnimator != null) {
      locationAnimator.cancel();
      locationAnimator.removeUpdateListener(locationUpdateListener);
      locationAnimator.removeListener(locationEndListener);
      locationAnimator.removeAllListeners();
      locationAnimator = null;
    }
  }

  private void resetBearingAnimator() {
    if (bearingAnimator != null) {
      bearingAnimator.cancel();
      bearingAnimator.removeUpdateListener(locationUpdateListener);
      bearingAnimator.removeListener(locationEndListener);
      bearingAnimator.removeAllListeners();
      bearingAnimator = null;
    }
  }

  private ValueAnimator.AnimatorUpdateListener locationUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      mapboxMap.setLatLng((LatLng) animation.getAnimatedValue());
    }
  };

  private ValueAnimator.AnimatorListener locationEndListener = new AnimatorListenerAdapter() {
    @Override
    public void onAnimationEnd(Animator animation) {
      super.onAnimationEnd(animation);
      resetLocationAnimator();
    }
  };

  private ValueAnimator.AnimatorUpdateListener bearingUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      mapboxMap.setBearing((Float) animation.getAnimatedValue());
    }
  };

  private ValueAnimator.AnimatorListener bearingEndListener = new AnimatorListenerAdapter() {
    @Override
    public void onAnimationEnd(Animator animation) {
      super.onAnimationEnd(animation);
      resetBearingAnimator();
    }
  };

  /**
   * Class that allows to evaluate two LatLng objects for a ValueAnimator
   */
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

  /**
   * Class that listens to tilt changes and transforms the ImageView
   */
  private static class CameraInvalidationListener implements MapboxMap.OnCameraChangeListener {

    private ImageView navigationView;
    private float currentTilt;
    private Camera camera = new Camera();
    private Matrix matrix = new Matrix();

    CameraInvalidationListener(ImageView navigationView) {
      this.navigationView = navigationView;
      this.navigationView.setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
      if (cameraPosition.tilt != currentTilt) {
        currentTilt = (float) cameraPosition.tilt;
        camera.save();
        camera.rotate(currentTilt, 0, 0);
        camera.getMatrix(matrix);
        camera.restore();
        navigationView.setImageMatrix(matrix);
      }
    }
  }

}
