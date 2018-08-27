package com.mapbox.mapboxsdk.plugins.locationlayer.modes;

import android.support.annotation.IntDef;

import com.mapbox.mapboxsdk.plugins.locationlayer.CompassEngine;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Contains the variety of ways the user location can be rendered on the map.
 *
 * @since 0.5.0
 */
public final class RenderMode {

  private RenderMode() {
    // Class should not be initialized
  }

  /**
   * One of these constants should be used with {@link LocationLayerPlugin#setRenderMode(int)}.
   *mode can be switched at anytime by calling the {@code setLocationLayerMode} method passing
   * in the new mode you'd like the location layer to be in.
   *
   * @since 0.1.0
   */
  @IntDef( {COMPASS, GPS, NORMAL})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {
  }

  /**
   * Basic tracking is enabled, bearing ignored.
   *
   * @since 0.1.0
   */
  public static final int NORMAL = 0x00000012;

  /**
   * Tracking the user location with bearing considered
   * from a {@link CompassEngine}.
   *
   * @since 0.1.0
   */
  public static final int COMPASS = 0x00000004;

  /**
   * Tracking the user location with bearing considered from {@link android.location.Location}.
   *
   * @since 0.1.0
   */
  public static final int GPS = 0x00000008;
}
