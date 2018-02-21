package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Contains the variety of Location Layer modes which shape the behavior of the plugin.
 *
 * @since 0.1.0
 */
public final class LocationLayerMode {

  private LocationLayerMode() {
    // Class should not be initialized
  }

  /**
   * One of these constants should be used when
   * {@link LocationLayerPlugin#setLocationLayerMode(int)}'s called. The
   * mode can be switched at anytime by calling the {@code setLocationLayerMode} method passing
   * in the new mode you'd like the location layer to be in.
   *
   * @since 0.1.0
   */
  @IntDef( {COMPASS, NAVIGATION, NORMAL})
  @Retention(RetentionPolicy.SOURCE)
  @interface Mode {
  }

  /**
   * Tracking the bearing of the user based on sensor data.
   *
   * @since 0.1.0
   */
  public static final int COMPASS = 0x00000004;

  /**
   * Tracking the user location in navigation mode.
   *
   * @since 0.1.0
   */
  public static final int NAVIGATION = 0x00000008;

  /**
   * Basic tracking is enabled.
   *
   * @since 0.1.0
   */
  public static final int NORMAL = 0x00000012;
}
