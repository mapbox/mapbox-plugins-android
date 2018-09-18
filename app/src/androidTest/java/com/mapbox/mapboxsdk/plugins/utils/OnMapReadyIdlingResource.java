package com.mapbox.mapboxsdk.plugins.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.test.espresso.IdlingResource;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.testapp.R;

public class OnMapReadyIdlingResource implements IdlingResource, OnMapReadyCallback {

  private MapboxMap mapboxMap;
  private MapView mapView;
  private IdlingResource.ResourceCallback resourceCallback;

  public OnMapReadyIdlingResource(Activity activity) {
    new Handler(Looper.getMainLooper()).post(() -> {
      try {
        mapView = activity.findViewById(R.id.mapView);
        if (mapView == null) {
          mapView = activity.findViewById(R.id.map_view);
        }
        mapView.getMapAsync(this);
      } catch (Exception err) {
        throw new RuntimeException(err);
      }
    });
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public boolean isIdleNow() {
    return mapboxMap != null;
  }

  @Override
  public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
    this.resourceCallback = resourceCallback;
  }

  public MapView getMapView() {
    return mapView;
  }

  public MapboxMap getMapboxMap() {
    return mapboxMap;
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    if (resourceCallback != null) {
      resourceCallback.onTransitionToIdle();
    }
  }
}