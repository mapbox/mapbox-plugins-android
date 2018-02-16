package com.mapbox.mapboxsdk.plugins.cluster.clustering.algo;

import com.mapbox.mapboxsdk.plugins.cluster.clustering.Cluster;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;

import java.util.Collection;
import java.util.Set;

/**
 * Logic for computing clusters
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 */
public interface Algorithm<T extends ClusterItem> {
  void addItem(T item);

  void addItems(Collection<T> items);

  void clearItems();

  void removeItem(T item);

  Set<? extends Cluster<T>> getClusters(double zoom);

  Collection<T> getItems();

  /**
   * Sets the biggest distance allowed between two markers before they get clustered.
   * By default this value is 100.
   *
   * @param maxDistance the max distance between markers before they get clustered
   */
  void setMaxDistanceBetweenClusteredItems(int maxDistance);

  /**
   * @return the max distance between markers before they get clustered
   */
  int getMaxDistanceBetweenClusteredItems();
}
