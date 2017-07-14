package com.mapbox.mapboxsdk.plugins.locationlayer;

/**
 * Callbacks related to the compass
 *
 * @since 0.1.0
 */
interface CompassListener {

  /**
   * Callback's invoked when a new compass update occurs and is used to update the UI chevron/arrow.
   *
   * @param magneticHeading the new compass heading
   */
  void onCompassChanged(float magneticHeading);
}