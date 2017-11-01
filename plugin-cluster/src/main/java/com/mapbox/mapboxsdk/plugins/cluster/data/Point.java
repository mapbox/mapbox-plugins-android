package com.mapbox.mapboxsdk.plugins.cluster.data;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * An abstraction that shares common properties
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 */
public class Point implements Geometry {

  private static final String GEOMETRY_TYPE = "Point";

  private final LatLng mCoordinates;

  /**
   * Creates a new Point object
   *
   * @param coordinates coordinates of Point to store
   */
  public Point(LatLng coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cannot be null");
    }
    mCoordinates = coordinates;
  }

  /**
   * Gets the type of geometry
   *
   * @return type of geometry
   */
  public String getGeometryType() {
    return GEOMETRY_TYPE;
  }

  /**
   * Gets the coordinates of the Point
   *
   * @return coordinates of the Point
   */
  public LatLng getGeometryObject() {
    return mCoordinates;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
    sb.append("\n coordinates=").append(mCoordinates);
    sb.append("\n}\n");
    return sb.toString();
  }

}

