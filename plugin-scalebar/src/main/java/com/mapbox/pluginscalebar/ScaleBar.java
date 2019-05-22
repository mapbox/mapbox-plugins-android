package com.mapbox.pluginscalebar;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

/**
 * Plugin class that shows a scale bar on MapView and changes the scale corresponding to the MapView's scale.
 */
public class ScaleBar {
  private MapboxMap mapboxMap;
  private Projection projection;
  private boolean enabled = true;
  private ScaleBarWidget scaleBarWidget;
  private CameraMoveListener cameraMoveListener;

  public ScaleBar(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    this.projection = mapboxMap.getProjection();
    this.scaleBarWidget = new ScaleBarWidget(mapView.getContext());
    mapView.addView(scaleBarWidget);
    cameraMoveListener = new CameraMoveListener();
    mapboxMap.addOnCameraMoveListener(cameraMoveListener);
    scaleBarWidget.setMapViewWidth(mapView.getWidth());
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
   * If the scale plugin wasn't enabled, a {@link com.mapbox.mapboxsdk.maps.MapboxMap.OnScaleListener}
   * will be added to the {@link MapView} to listen to scale change events to update the state of this plugin. If the
   * plugin was enabled the {@link com.mapbox.mapboxsdk.maps.MapboxMap.OnScaleListener} will be removed from the map.
   * </p>
   */
  @UiThread
  public void setEnabled(boolean enabled) {
    if (this.enabled == enabled) {
      // already in correct state
      return;
    }
    this.enabled = enabled;
    scaleBarWidget.setVisibility(enabled ? View.VISIBLE : View.GONE);
    if (enabled) {
      mapboxMap.addOnCameraMoveListener(cameraMoveListener);
    } else {
      mapboxMap.removeOnCameraMoveListener(cameraMoveListener);
    }
  }


  /**
   * Set colors for the scale bar.
   *
   * @param textColor      The color for the texts above the scale bar.
   * @param primaryColor   The color for odd number index bars.
   * @param secondaryColor The color for even number index bars.
   */
  public void setColors(@ColorRes int textColor, @ColorRes int primaryColor, @ColorRes int secondaryColor) {
    scaleBarWidget.setColors(textColor, primaryColor, secondaryColor);
  }

  class CameraMoveListener implements MapboxMap.OnCameraMoveListener {
    @Override
    public void onCameraMove() {
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      double metersPerPixel = projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude());
      scaleBarWidget.setDistancePerPixel(projection.getMetersPerPixelAtLatitude(metersPerPixel));
    }
  }

  @VisibleForTesting
  public ScaleBarWidget getScaleBarWidget() {
    return scaleBarWidget;
  }
}
