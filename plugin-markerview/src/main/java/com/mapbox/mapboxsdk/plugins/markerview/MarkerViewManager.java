package com.mapbox.mapboxsdk.plugins.markerview;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for synchronising views at a LatLng on top of a Map.
 */
public class MarkerViewManager implements MapView.OnMapChangedListener {

  private final MapView mapView;
  private final MapboxMap mapboxMap;
  private final List<MarkerView> markers = new ArrayList<>();
  private boolean initialised;

  /**
   * Create a MarkerViewManager.
   *
   * @param mapView   the MapView used to synchronise views on
   * @param mapboxMap the MapboxMap to synchronise views with
   */
  public MarkerViewManager(MapView mapView, MapboxMap mapboxMap) {
    this.mapView = mapView;
    this.mapboxMap = mapboxMap;
  }

  /**
   * Destroys the MarkerViewManager.
   * <p>
   * Should be called before MapView#onDestroy
   * </p>
   */
  @UiThread
  public void onDestroy() {
    markers.clear();
    mapView.removeOnMapChangedListener(this);
    initialised = false;
  }

  /**
   * Add a MarkerView to the map using MarkerView and LatLng.
   *
   * @param markerView the markerView to synchronise on the map
   */
  @UiThread
  public void addMarker(@NonNull MarkerView markerView) {
    if (mapView.isDestroyed() || markers.contains(markerView)) {
      return;
    }

    if (!initialised) {
      initialised = true;
      mapView.addOnMapChangedListener(this);
    }
    markerView.setProjection(mapboxMap.getProjection());
    mapView.addView(markerView.getView());
    markers.add(markerView);
  }

  /**
   * Remove an existing markerView from the map.
   *
   * @param markerView the markerView to be removed from the map
   */
  @UiThread
  public void removeMarker(@NonNull MarkerView markerView) {
    if (mapView.isDestroyed() || !markers.contains(markerView)) {
      return;
    }

    mapView.removeView(markerView.getView());
    markers.remove(markerView);
  }

  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_RENDERING_FRAME_FULLY_RENDERED) {
      update();
    }
  }

  private void update() {
    for (MarkerView marker : markers) {
      marker.update();
    }
  }
}
