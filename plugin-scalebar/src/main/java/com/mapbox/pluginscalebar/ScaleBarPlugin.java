package com.mapbox.pluginscalebar;

import android.view.View;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;

/**
 * Plugin class that shows a scale bar on MapView and changes the scale corresponding to the MapView's scale.
 */
public class ScaleBarPlugin {
  private static final String TAG = "Mbgl-ScaleBarPlugin";

  private final MapView mapView;
  private final MapboxMap mapboxMap;
  private final Projection projection;
  private boolean enabled = true;
  private ScaleBarWidget scaleBarWidget;

  @VisibleForTesting
  final MapboxMap.OnCameraMoveListener cameraMoveListener = new MapboxMap.OnCameraMoveListener() {
    @Override
    public void onCameraMove() {
      invalidateScaleBar();
    }
  };

  @VisibleForTesting
  final MapboxMap.OnCameraIdleListener cameraIdleListener = new MapboxMap.OnCameraIdleListener() {
    @Override
    public void onCameraIdle() {
      invalidateScaleBar();
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
    mapView.addView(scaleBarWidget);

    scaleBarWidget.setVisibility(enabled ? View.VISIBLE : View.GONE);
    if (enabled) {
      addCameraListeners();
      invalidateScaleBar();
    }
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
   * If the scale plugin wasn enabled, a {@link com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraMoveListener}
   * will be added to the {@link MapView} to listen to scale change events to update the state of this plugin. If the
   * plugin was disabled the {@link com.mapbox.mapboxsdk.maps.MapboxMap.OnCameraMoveListener}
   * will be removed from the map.
   * </p>
   */
  @UiThread
  public void setEnabled(boolean enabled) {
    if (scaleBarWidget == null) {
      Logger.w(TAG, "Create a widget before changing ScalebBarPlugin's state. Ignoring.");
      return;
    }
    if (this.enabled == enabled) {
      // already in correct state
      return;
    }
    this.enabled = enabled;
    scaleBarWidget.setVisibility(enabled ? View.VISIBLE : View.GONE);
    if (enabled) {
      addCameraListeners();
      invalidateScaleBar();
    } else {
      removeCameraListeners();
    }
  }

  private void invalidateScaleBar() {
    CameraPosition cameraPosition = mapboxMap.getCameraPosition();
    scaleBarWidget.setDistancePerPixel((projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude()))
      / mapView.getPixelRatio());
  }

  private void addCameraListeners() {
    mapboxMap.addOnCameraMoveListener(cameraMoveListener);
    mapboxMap.addOnCameraIdleListener(cameraIdleListener);
  }

  private void removeCameraListeners() {
    mapboxMap.removeOnCameraMoveListener(cameraMoveListener);
    mapboxMap.removeOnCameraIdleListener(cameraIdleListener);
  }
}
