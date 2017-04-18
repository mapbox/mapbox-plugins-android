package com.mapbox.mapboxsdk.plugins.scale;

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
public class ScalePlugin implements MapView.OnMapChangedListener {

  private ScaleWidget scaleWidget;
  private MapboxMap mapboxMap;
  private Projection projection;
  private MapView mapView;
  private boolean enabled;

  public ScalePlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapView = mapView;
    this.mapboxMap = mapboxMap;
    this.projection = mapboxMap.getProjection();
    this.scaleWidget = new ScaleWidget(mapView.getContext());
    this.mapView.addView(scaleWidget);
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
    if(this.enabled==enabled){
      // already in correct state
      return;
    }
    this.enabled = enabled;
    scaleWidget.setVisibility(enabled ? View.VISIBLE : View.GONE);
    if (enabled) {
      mapView.addOnMapChangedListener(this);
      // trigger initial update
      onMapChanged(MapView.REGION_DID_CHANGE);
    } else {
      mapView.removeOnMapChangedListener(this);
    }
  }

  @Override
  public void onMapChanged(int change) {
    Timber.e("OnMapChange %s", change);
    if (change == MapView.REGION_DID_CHANGE || change == MapView.REGION_DID_CHANGE_ANIMATED) {
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      double metersPerPixel = projection.getMetersPerPixelAtLatitude(cameraPosition.target.getLatitude());
      scaleWidget.setMetersPerPixel(projection.getMetersPerPixelAtLatitude(metersPerPixel));
    }
  }
}
