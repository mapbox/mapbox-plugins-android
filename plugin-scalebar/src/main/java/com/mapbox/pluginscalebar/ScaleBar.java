package com.mapbox.pluginscalebar;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

/**
 * Plugin class that shows a scale bar on MapView and changes the scale corresponding to the MapView's scale.
 */
public class ScaleBar implements MapboxMap.OnScaleListener {
  private MapboxMap mapboxMap;
  private Projection projection;
  private boolean enabled;
  private ScaleBarWidget scaleBarWidget;

  public ScaleBar(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    this.projection = mapboxMap.getProjection();
    this.scaleBarWidget = new ScaleBarWidget(mapView.getContext());
    mapView.addView(scaleBarWidget);
    mapboxMap.addOnScaleListener(this);
    scaleBarWidget.setWidth(mapView.getWidth());
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
      mapboxMap.addOnScaleListener(this);
    } else {
      mapboxMap.removeOnScaleListener(this);
    }
  }

  @Override
  public void onScaleBegin(@NonNull StandardScaleGestureDetector detector) {

  }

  /**
   * Set colors for the scale bar.
   *
   * @param textColor      The color for the texts above scale bar.
   * @param primaryColor   The color for odd number index bars.
   * @param secondaryColor The color for even number index bars.
   */
  public void setColors(@ColorInt int textColor, @ColorInt int primaryColor, @ColorInt int secondaryColor) {
    scaleBarWidget.setColors(textColor, primaryColor, secondaryColor);
  }

  @Override
  public void onScale(@NonNull StandardScaleGestureDetector detector) {
    CameraPosition cameraPosition = mapboxMap.getCameraPosition();
    double metersPerPixel = projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude());
    scaleBarWidget.setDistancePerPixel(projection.getMetersPerPixelAtLatitude(metersPerPixel));
  }

  @Override
  public void onScaleEnd(@NonNull StandardScaleGestureDetector detector) {

  }

}
