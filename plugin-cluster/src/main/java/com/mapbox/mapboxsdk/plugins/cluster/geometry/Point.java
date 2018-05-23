package com.mapbox.mapboxsdk.plugins.cluster.geometry;

/**
 * Represents a 2D double coordinate point.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class Point {
  public final double x;
  public final double y;

  @Deprecated
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Deprecated
  @Override
  public String toString() {
    return "Point{"
      + "x=" + x
      + ", y=" + y
      + '}';
  }
}
