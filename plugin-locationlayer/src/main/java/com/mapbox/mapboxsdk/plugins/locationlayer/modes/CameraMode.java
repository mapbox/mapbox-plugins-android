package com.mapbox.mapboxsdk.plugins.locationlayer.modes;

import android.location.Location;
import android.support.annotation.IntDef;

import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains the variety of camera modes which determine how the camera will track
 * the user location.
 *
 * @since 0.5.0
 */
public final class CameraMode {

  private CameraMode() {
    // Class should not be initialized
  }

  /**
   * Determine the camera tracking behavior in the {@link LocationLayerPlugin}.
   *
   * @since 0.5.0
   */
  @IntDef( {NONE, NONE_COMPASS, NONE_GPS, TRACKING, TRACKING_COMPASS, TRACKING_GPS, TRACKING_GPS_NORTH})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {
  }

  /**
   * No camera tracking.
   *
   * @since 0.5.0
   */
  public static final int NONE = 0x00000008;

  /**
   * Camera does not track location, but does track compass bearing.
   *
   * @since 0.5.0
   */
  public static final int NONE_COMPASS = 0x00000010;

  /**
   * Camera does not track location, but does track GPS {@link Location} bearing.
   *
   * @since 0.5.0
   */
  public static final int NONE_GPS = 0x00000016;

  /**
   * Camera tracks the user location.
   *
   * @since 0.5.0
   */
  public static final int TRACKING = 0x00000018;

  /**
   * Camera tracks the user location, with bearing
   * provided by a compass.
   *
   * @since 0.5.0
   */
  public static final int TRACKING_COMPASS = 0x00000020;

  /**
   * Camera tracks the user location, with bearing
   * provided by a normalized {@link Location#getBearing()}.
   *
   * @since 0.5.0
   */
  public static final int TRACKING_GPS = 0x00000022;

  /**
   * Camera tracks the user location, with bearing
   * always set to north (0).
   *
   * @since 0.5.0
   */
  public static final int TRACKING_GPS_NORTH = 0x00000024;

  /**
   * Set of camera modes that are tracking user location.
   *
   * @since 0.6.0
   */
  public static final Set<Integer> TRACKING_LOCATION = new HashSet<Integer>() {
    {
      add(CameraMode.TRACKING_COMPASS);
      add(CameraMode.TRACKING_GPS);
      add(CameraMode.TRACKING);
      add(CameraMode.TRACKING_GPS_NORTH);
    }
  };

  /**
   * Set of camera modes that are tracking user bearing.
   *
   * @since 0.6.0
   */
  public static final Set<Integer> TRACKING_BEARING = new HashSet<Integer>() {
    {
      add(CameraMode.NONE_COMPASS);
      add(CameraMode.TRACKING_COMPASS);
      add(CameraMode.NONE_GPS);
      add(CameraMode.TRACKING_GPS);
      add(CameraMode.TRACKING_GPS_NORTH);
    }
  };

  /**
   * Set of camera modes that are tracking compass bearing.
   *
   * @since 0.6.0
   */
  public static final Set<Integer> TRACKING_COMPASS_BEARING = new HashSet<Integer>() {
    {
      add(CameraMode.TRACKING_COMPASS);
      add(CameraMode.NONE_COMPASS);
    }
  };

  /**
   * Set of camera modes that are tracking gps bearing.
   *
   * @since 0.6.0
   */
  public static final Set<Integer> TRACKING_GPS_BEARING = new HashSet<Integer>() {
    {
      add(CameraMode.TRACKING_GPS);
      add(CameraMode.NONE_GPS);
    }
  };
}
