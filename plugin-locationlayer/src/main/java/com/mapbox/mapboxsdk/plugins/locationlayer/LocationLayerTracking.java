package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.location.Location;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Contains the variety of Location Layer modes which shape the behavior of the plugin.
 *
 * @since 0.1.0
 */
public final class LocationLayerTracking {

  private LocationLayerTracking() {
    // Class should not be initialized
  }

  /**
   * Determine the camera tracking behavior in the {@link LocationLayerPlugin}.
   *
   * @since 0.4.0
   */
  @IntDef( {NONE, TRACKING, TRACKING_COMPASS, TRACKING_GPS, TRACKING_GPS_NORTH})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {
  }

  /**
   * No camera tracking.
   *
   * @since 0.4.0
   */
  public static final int NONE = 0x00000000;

  /**
   * Tracks the user location.
   *
   * @since 0.4.0
   */
  public static final int TRACKING = 0x0000006;

  /**
   * Tracks the user location, with camera bearing
   * based on the bearing provided by the compass.
   *
   * @since 0.4.0
   */
  public static final int TRACKING_COMPASS = 0x00000010;

  /**
   * Tracks the user location, with camera bearing
   * based on the bearing provided by a normalized {@link Location#getBearing()}.
   *
   * @since 0.4.0
   */
  public static final int TRACKING_GPS = 0x00000014;


  /**
   * Tracks the user location, with camera bearing
   * always set to north.
   *
   * @since 0.4.0
   */
  public static final int TRACKING_GPS_NORTH = 0x00000018;
}
