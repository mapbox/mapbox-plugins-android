package com.mapbox.mapboxsdk.plugins.locationlayer;

/**
 * Contains all the constants being used for the My Location layer.
 *
 * @since 0.1.0
 */
class LocationLayerConstants {

  // Controls the compass update rate in milliseconds
  static final int COMPASS_UPDATE_RATE_MS = 500;

  // Sets the animation time when moving the user location icon from the previous location to the updated. If
  // LinearAnimator's enabled, this values ignored.
  static final int LOCATION_UPDATE_DELAY_MS = 500;

  // Sources
  static final String LOCATION_SOURCE = "mapbox-location-source";
  static final String LOCATION_ACCURACY_SOURCE = "mapbox-location-accuracy-source";

  // Layers
  static final String LOCATION_LAYER = "mapbox-location-layer";
  static final String LOCATION_BACKGROUND_LAYER = "mapbox-location-stroke-layer";
  static final String LOCATION_ACCURACY_LAYER = "mapbox-location-accuracy-layer";
  static final String LOCATION_BEARING_LAYER = "mapbox-location-bearing-layer";
  static final String LOCATION_NAVIGATION_LAYER = "mapbox-location-navigation-layer";

  // Icons
  static final String USER_LOCATION_ICON = "mapbox-location-icon";
  static final String USER_LOCATION_BEARING_ICON = "mapbox-location-bearing-icon";
  static final String USER_LOCATION_BACKGROUND_ICON = "mapbox-location-stroke-icon";
  static final String USER_LOCATION_PUCK_ICON = "mapbox-location-puck-icon";
}
