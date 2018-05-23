package com.mapbox.mapboxsdk.plugins.cluster.clustering.algo;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.Cluster;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A cluster whose center is determined upon creation.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class StaticCluster<T extends ClusterItem> implements Cluster<T> {
  private final LatLng mCenter;
  private final List<T> mItems = new ArrayList<T>();

  @Deprecated
  public StaticCluster(LatLng center) {
    mCenter = center;
  }

  @Deprecated
  public boolean add(T t) {
    return mItems.add(t);
  }

  @Deprecated
  @Override
  public LatLng getPosition() {
    return mCenter;
  }

  @Deprecated
  public boolean remove(T t) {
    return mItems.remove(t);
  }

  @Deprecated
  @Override
  public Collection<T> getItems() {
    return mItems;
  }

  @Deprecated
  @Override
  public int getSize() {
    return mItems.size();
  }

  @Deprecated
  @Override
  public String toString() {
    return "StaticCluster{"
      + "mCenter=" + mCenter
      + ", mItems.size=" + mItems.size()
      + '}';
  }

  @Deprecated
  @Override
  public int hashCode() {
    return mCenter.hashCode() + mItems.hashCode();
  }

  @Deprecated
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof StaticCluster<?>)) {
      return false;
    }

    return ((StaticCluster<?>) other).mCenter.equals(mCenter)
      && ((StaticCluster<?>) other).mItems.equals(mItems);
  }
}