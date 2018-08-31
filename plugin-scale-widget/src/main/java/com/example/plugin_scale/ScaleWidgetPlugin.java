package com.example.plugin_scale;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;

import timber.log.Timber;

/**
 * The scale plugin allows to show a map scale on a MapView from the Mapbox Android SDK v5.0.2
 */
public class ScaleWidgetPlugin implements MapView.OnMapChangedListener {

  private ScaleWidget scaleWidget;
  private MapboxMap mapboxMap;
  private Projection projection;
  private MapView mapView;
  private boolean enabled;
  private int screenWidth;

  public ScaleWidgetPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, int screenWidth) {
    this.mapView = mapView;
    this.mapboxMap = mapboxMap;
    this.projection = mapboxMap.getProjection();
    this.screenWidth = screenWidth;
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
   * If the scale plugin wasn't enabled, a {@link com.mapbox.mapboxsdk.maps.MapView.OnMapChangedListener}
   * will be added to the {@link MapView} to listen to map change events to update the state of this plugin. If the
   * plugin was enabled the {@link com.mapbox.mapboxsdk.maps.MapView.OnMapChangedListener} will be removed from the map.
   * </p>
   */
  @UiThread
  public void setEnabled(boolean enabled) {
    if(this.enabled == enabled){
      // already in correct state
      return;
    }
    this.enabled = enabled;

    if (this.enabled && scaleWidget == null) {
      this.scaleWidget = new ScaleWidget(mapView.getContext());
      this.mapView.addView(scaleWidget);
    } else {
      scaleWidget.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    if (enabled) {
      Timber.e(">>>>>> ENABLED >>>");
      mapView.addOnMapChangedListener(this);
      // trigger initial update
      onMapChanged(MapView.REGION_DID_CHANGE);
    } else {
      Timber.e(">>>>>> DISABLED >>>");
      mapView.removeOnMapChangedListener(this);
    }
  }

  @Override
  public void onMapChanged(int change) {
    if (change == MapView.REGION_DID_CHANGE || change == MapView.REGION_DID_CHANGE_ANIMATED) {
      Timber.e(">>>>>> OnMapChange %s", change);

      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      double metersPerPixel = projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude());
      scaleWidget.setMetersPerPixel(metersPerPixel, screenWidth);
    }
  }
}