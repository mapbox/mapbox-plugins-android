package com.mapbox.mapboxsdk.plugins.locationlayer;

/**
 * Contains all the constants being used for the Location layer.
 *
 * @since 0.1.0
 */
final class LocationLayerConstants {

  // Controls the compass update rate in milliseconds
  static final int COMPASS_UPDATE_RATE_MS = 500;

  // Sets the animation time when moving the user location icon from the previous location to the
  // updated. If LinearAnimator's enabled, this values ignored.
  static final int LOCATION_UPDATE_DELAY_MS = 500;

  // Sets the max allowed time for the location icon animation from one latlng to another.
  static final long MIN_ANIMATION_DURATION_MS = 1500;

  // Sources
  static final String LOCATION_SOURCE = "mapbox-location-source";

  // Layers
  static final String SHADOW_LAYER = "mapbox-location-shadow";
  static final String FOREGROUND_LAYER = "mapbox-location-layer";
  static final String BACKGROUND_LAYER = "mapbox-location-stroke-layer";
  static final String ACCURACY_LAYER = "mapbox-location-accuracy-layer";
  static final String BEARING_LAYER = "mapbox-location-bearing-layer";

  // Icons
  static final String FOREGROUND_ICON = "mapbox-location-icon";
  static final String BACKGROUND_ICON = "mapbox-location-stroke-icon";
  static final String FOREGROUND_STALE_ICON = "mapbox-location-stale-icon";
  static final String BACKGROUND_STALE_ICON = "mapbox-location-background-stale-icon";
  static final String SHADOW_ICON = "mapbox-location-shadow-icon";
  static final String BEARING_ICON = "mapbox-location-bearing-icon";

  private LocationLayerConstants() {
    // Class should not be initialized
  }
}
