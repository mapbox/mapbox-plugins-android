package com.mapbox.mapboxsdk.plugins.cluster.data;

/**
 * An abstraction that represents a Geometry object
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @param <T> the type of Geometry object
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public interface Geometry<T> {
  /**
   * Gets the type of geometry
   *
   * @return type of geometry
   */
  public String getGeometryType();

  /**
   * Gets the stored KML Geometry object
   *
   * @return geometry object
   */
  public T getGeometryObject();

}
