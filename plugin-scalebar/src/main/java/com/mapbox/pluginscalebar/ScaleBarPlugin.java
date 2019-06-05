package com.mapbox.pluginscalebar;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

/**
 * Plugin class that shows a scale bar on MapView and changes the scale corresponding to the MapView's scale.
 */
public class ScaleBarPlugin {
  private final MapView mapView;
  private final MapboxMap mapboxMap;
  private final Projection projection;
  private boolean enabled = true;
  private ScaleBarWidget scaleBarWidget;

  private final MapboxMap.OnCameraMoveListener cameraMoveListener = new MapboxMap.OnCameraMoveListener() {
    @Override
    public void onCameraMove() {
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      scaleBarWidget.setDistancePerPixel(projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude()));
    }
  };

  public ScaleBarPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapView = mapView;
    this.mapboxMap = mapboxMap;
    this.projection = mapboxMap.getProjection();
  }

  /**
   * Create a scale bar widget on mapView.
   *
   * @param option The scale bar widget options that used to build scale bar widget.
   * @return The created ScaleBarWidget instance.
   */
  public ScaleBarWidget create(@NonNull ScaleBarOptions option) {
    if (scaleBarWidget != null) {
      mapView.removeView(scaleBarWidget);
    }
    scaleBarWidget = option.build();
    scaleBarWidget.setMapViewWidth(mapView.getWidth());
    mapboxMap.addOnCameraMoveListener(cameraMoveListener);
    mapView.addView(scaleBarWidget);
    CameraPosition cameraPosition = mapboxMap.getCameraPosition();
    scaleBarWidget.setDistancePerPixel(projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude()));
    return scaleBarWidget;
  }

  /**
   * Returns true if the scale plugin is currently enabled and visible.
   *
   * @return true if enabled, false otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Toggles the scale plugin state.
   * <p>
   * If the scale plugin wasn't enabled, a {@link com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraMoveListener}
   * will be added to the {@link MapView} to listen to scale change events to update the state of this plugin. If the
   * plugin was enabled the {@link com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraMoveListener}
   * will be removed from the map.
   * </p>
   */
  @UiThread
  public void setEnabled(boolean enabled) {
    if (this.enabled == enabled) {
      // already in correct state
      return;
    }
    this.enabled = enabled;
    if (scaleBarWidget != null) {
      scaleBarWidget.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }
    if (enabled) {
      mapboxMap.addOnCameraMoveListener(cameraMoveListener);
    } else {
      mapboxMap.removeOnCameraMoveListener(cameraMoveListener);
    }
  }

}
