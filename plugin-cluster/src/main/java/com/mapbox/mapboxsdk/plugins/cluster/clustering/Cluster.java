package com.mapbox.mapboxsdk.plugins.cluster.clustering;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Collection;

/**
 * A collection of ClusterItems that are nearby each other.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public interface Cluster<T extends ClusterItem> {
  public LatLng getPosition();

  Collection<T> getItems();

  int getSize();
}