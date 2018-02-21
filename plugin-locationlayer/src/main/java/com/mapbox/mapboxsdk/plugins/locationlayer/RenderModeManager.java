package com.mapbox.mapboxsdk.plugins.locationlayer;

import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.ACCURACY_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BEARING_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.FOREGROUND_LAYER;

class RenderModeManager {

  private LocationLayer locationLayer;

  RenderModeManager(LocationLayer locationLayer) {
    this.locationLayer = locationLayer;
  }

  void updateMode(@RenderMode.Mode int renderMode) {
    locationLayer.hide();

    switch (renderMode) {
      case RenderMode.NORMAL:
        locationLayer.setLayerVisibility(FOREGROUND_LAYER, true);
        locationLayer.setLayerVisibility(BACKGROUND_LAYER, true);
        locationLayer.setLayerVisibility(ACCURACY_LAYER, true);
        break;
      case RenderMode.COMPASS:
        locationLayer.setLayerVisibility(FOREGROUND_LAYER, true);
        locationLayer.setLayerVisibility(BACKGROUND_LAYER, true);
        locationLayer.setLayerVisibility(ACCURACY_LAYER, true);
        locationLayer.setLayerVisibility(BEARING_LAYER, true);
        break;
      case RenderMode.GPS:
        locationLayer.setLayerVisibility(FOREGROUND_LAYER, true);
        locationLayer.setLayerVisibility(BACKGROUND_LAYER, true);
        break;
      default:
        break;
    }
  }
}


