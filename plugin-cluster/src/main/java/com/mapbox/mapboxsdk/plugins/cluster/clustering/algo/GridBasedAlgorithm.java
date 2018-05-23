package com.mapbox.mapboxsdk.plugins.cluster.clustering.algo;

import android.support.v4.util.LongSparseArray;

import com.mapbox.mapboxsdk.plugins.cluster.clustering.Cluster;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;
import com.mapbox.mapboxsdk.plugins.cluster.geometry.Point;
import com.mapbox.mapboxsdk.plugins.cluster.projection.SphericalMercatorProjection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Groups markers into a grid.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class GridBasedAlgorithm<T extends ClusterItem> implements Algorithm<T> {
  private static final int GRID_SIZE = 100;

  private int mGridSize = GRID_SIZE;

  private final Set<T> mItems = Collections.synchronizedSet(new HashSet<T>());

  @Deprecated
  @Override
  public void addItem(T item) {
    mItems.add(item);
  }

  @Deprecated
  @Override
  public void addItems(Collection<T> items) {
    mItems.addAll(items);
  }

  @Deprecated
  @Override
  public void clearItems() {
    mItems.clear();
  }

  @Deprecated
  @Override
  public void removeItem(T item) {
    mItems.remove(item);
  }

  @Deprecated
  @Override
  public Set<? extends Cluster<T>> getClusters(double zoom) {
    long numCells = (long) Math.ceil(256 * Math.pow(2, zoom) / mGridSize);
    SphericalMercatorProjection proj = new SphericalMercatorProjection(numCells);

    HashSet<Cluster<T>> clusters = new HashSet<>();
    LongSparseArray<StaticCluster<T>> sparseArray = new LongSparseArray<StaticCluster<T>>();

    synchronized (mItems) {
      for (T item : mItems) {
        Point p = proj.toPoint(item.getPosition());

        long coord = getCoord(numCells, p.x, p.y);

        StaticCluster<T> cluster = sparseArray.get(coord);
        if (cluster == null) {
          cluster = new StaticCluster<T>(proj.toLatLng(new Point(Math.floor(p.x) + .5, Math.floor(p.y) + .5)));
          sparseArray.put(coord, cluster);
          clusters.add(cluster);
        }
        cluster.add(item);
      }
    }

    return clusters;
  }

  @Deprecated
  @Override
  public Collection<T> getItems() {
    return mItems;
  }

  @Deprecated
  @Override
  public void setMaxDistanceBetweenClusteredItems(int maxDistance) {
    mGridSize = maxDistance;
  }

  @Deprecated
  @Override
  public int getMaxDistanceBetweenClusteredItems() {
    return mGridSize;
  }

  private static long getCoord(long numCells, double x, double y) {
    return (long) (numCells * Math.floor(x) + Math.floor(y));
  }
}
