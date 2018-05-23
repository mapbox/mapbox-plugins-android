package com.mapbox.mapboxsdk.plugins.cluster.geometry;

/**
 * Represents an area in the cartesian plane.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class Bounds {
  public final double minX;
  public final double minY;

  public final double maxX;
  public final double maxY;

  public final double midX;
  public final double midY;

  @Deprecated
  public Bounds(double minX, double maxX, double minY, double maxY) {
    this.minX = minX;
    this.minY = minY;
    this.maxX = maxX;
    this.maxY = maxY;

    midX = (minX + maxX) / 2;
    midY = (minY + maxY) / 2;
  }

  @Deprecated
  public boolean contains(double x, double y) {
    return minX <= x && x <= maxX && minY <= y && y <= maxY;
  }

  @Deprecated
  public boolean contains(Point point) {
    return contains(point.x, point.y);
  }

  @Deprecated
  public boolean intersects(double minX, double maxX, double minY, double maxY) {
    return minX < this.maxX && this.minX < maxX && minY < this.maxY && this.minY < maxY;
  }

  @Deprecated
  public boolean intersects(Bounds bounds) {
    return intersects(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY);
  }

  @Deprecated
  public boolean contains(Bounds bounds) {
    return bounds.minX >= minX && bounds.maxX <= maxX && bounds.minY >= minY && bounds.maxY <= maxY;
  }
}