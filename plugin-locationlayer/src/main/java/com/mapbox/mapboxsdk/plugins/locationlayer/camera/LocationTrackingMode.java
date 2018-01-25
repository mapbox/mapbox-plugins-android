package com.mapbox.mapboxsdk.plugins.locationlayer.camera;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class LocationTrackingMode {

  @IntDef( {TRACKING_NONE, TRACKING_FOLLOW})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {
  }

  /**
   * Tracking the location of the user is disabled.
   *
   * @since 0.4.0
   */
  public static final int TRACKING_NONE = 0x00000000;

  /**
   * Tracking the location of the user. The map camera will reposition itself to where the user's
   * location's at.
   *
   * @since 0.4.0
   */
  public static final int TRACKING_FOLLOW = 0x00000004;

  private LocationTrackingMode() {
    // No instances
  }
}
