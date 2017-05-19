package com.mapbox.mapboxsdk.plugins.locationlayer;

/**
 * Callback occurs when a new compass update occurs and is used to update the UI chevron/arrow for example
 *
 * @since 0.1.0
 */
interface CompassListener {
  void onCompassChanged(float magneticHeading);
}
