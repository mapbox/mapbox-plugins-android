package com.mapbox.mapboxsdk.plugins.cluster.clustering;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * ClusterItem represents a marker on the map.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public interface ClusterItem {

  /**
   * The position of this marker. This must always return the same value.
   */
  LatLng getPosition();

  /**
   * The title of this marker.
   */
  String getTitle();

  /**
   * The description of this marker.
   */
  String getSnippet();
}