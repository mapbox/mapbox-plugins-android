package com.mapbox.mapboxsdk.plugins.locationlayer.modes;

import android.location.Location;
import android.support.annotation.IntDef;

import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Contains the variety of camera modes which determine how the camera will track
 * the user location.
 *
 * @since 0.5.0
 */
public final class CameraMode {
  public static final int FOLLOW_NONE        = 0b0000_0000_0000;
  public static final int FOLLOW_GPS         = 0b0000_0000_0001;
  public static final int ORIENT_NONE        = 0b0000_0000_0000;
  public static final int ORIENT_COMPASS     = 0b0001_0000_0000;
  public static final int ORIENT_GPS         = 0b0010_0000_0000;
  public static final int ORIENT_NORTH       = 0b0100_0000_0000;

  private CameraMode() {
    // Class should not be initialized
  }

  /**
   * Determine the camera tracking behavior in the {@link LocationLayerPlugin}.
   *
   * @since 0.5.0
   */
  @IntDef({NONE, NONE_COMPASS, NONE_GPS, TRACKING, TRACKING_COMPASS, TRACKING_GPS, TRACKING_GPS_NORTH})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {
  }

  /**
   * No camera tracking.
   *
   * @since 0.5.0
   */
  public static final int NONE = FOLLOW_NONE | ORIENT_NONE;

  /**
   * Camera does not track location, but does track compass bearing.
   *
   * @since 0.5.0
   */
  public static final int NONE_COMPASS = FOLLOW_NONE | ORIENT_COMPASS;

  /**
   * Camera does not track location, but does track GPS {@link Location} bearing.
   *
   * @since 0.5.0
   */
  public static final int NONE_GPS = FOLLOW_NONE | ORIENT_GPS;

  /**
   * Camera tracks the user location.
   *
   * @since 0.5.0
   */
  public static final int TRACKING = FOLLOW_GPS | ORIENT_NONE;

  /**
   * Camera tracks the user location, with bearing
   * provided by a compass.
   *
   * @since 0.5.0
   */
  public static final int TRACKING_COMPASS = FOLLOW_GPS | ORIENT_COMPASS;

  /**
   * Camera tracks the user location, with bearing
   * provided by a normalized {@link Location#getBearing()}.
   *
   * @since 0.5.0
   */
  public static final int TRACKING_GPS = FOLLOW_GPS | ORIENT_GPS;

  /**
   * Camera tracks the user location, with bearing
   * always set to north (0).
   *
   * @since 0.5.0
   */
  public static final int TRACKING_GPS_NORTH = FOLLOW_GPS | ORIENT_NORTH;
}