package com.mapbox.mapboxsdk.plugins.mylocationlayer;

/**
 * Contains all the constants being used for the My Location layer.
 *
 * @since 0.1.0
 */
class MyLocationLayerConstants {

  // Controls the compass update rate in milliseconds
  static final int COMPASS_UPDATE_RATE_MS = 500;

  // Sources
  static final String LOCATION_SOURCE = "mapbox-location-source";
  static final String LOCATION_ACCURACY_SOURCE = "mapbox-location-accuracy-source";

  // Layers
  static final String LOCATION_LAYER = "mapbox-location-layer";
  static final String LOCATION_BACKGROUND_LAYER = "mapbox-location-stroke-layer";
  static final String LOCATION_ACCURACY_LAYER = "mapbox-location-accuracy-layer";
  static final String LOCATION_TEXT_ANNOTATION_LAYER = "mapbox-user-location-text-annotation-layer";

  // Icons
  static final String USER_LOCATION_ICON = "mapbox-user-location-icon";
  static final String USER_LOCATION_BEARING_ICON = "mapbox-user-location-bearing-icon";
  static final String USER_LOCATION_BACKGROUND_ICON = "mapbox-user-location-stroke-icon";
}
