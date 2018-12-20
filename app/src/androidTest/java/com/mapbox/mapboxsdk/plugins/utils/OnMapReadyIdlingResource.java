package com.mapbox.mapboxsdk.plugins.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.testapp.R;

public class OnMapReadyIdlingResource implements IdlingResource, OnMapReadyCallback {

  private MapboxMap mapboxMap;
  private MapView mapView;
  private IdlingResource.ResourceCallback resourceCallback;

  public OnMapReadyIdlingResource(Activity activity) {
    new Handler(Looper.getMainLooper()).post(() -> {
      mapView = activity.findViewById(R.id.mapView);
      if (mapView == null) {
        // some activities have different MapView id
        mapView = activity.findViewById(R.id.map_view);
      }

      if (mapView != null) {
        mapView.getMapAsync(OnMapReadyIdlingResource.this);
      }
    });
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public boolean isIdleNow() {
    return mapboxMap != null && mapboxMap.getStyle() != null && mapboxMap.getStyle().isFullyLoaded();
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
  public void onMapReady(@NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
      if (resourceCallback != null) {
        resourceCallback.onTransitionToIdle();
      }
    });
  }
}