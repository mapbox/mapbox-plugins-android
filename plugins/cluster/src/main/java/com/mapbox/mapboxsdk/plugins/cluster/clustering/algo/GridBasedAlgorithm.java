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
 */
public class GridBasedAlgorithm<T extends ClusterItem> implements Algorithm<T> {
  private static final int GRID_SIZE = 100;

  private final Set<T> mItems = Collections.synchronizedSet(new HashSet<T>());

  @Override
  public void addItem(T item) {
    mItems.add(item);
  }

  @Override
  public void addItems(Collection<T> items) {
    mItems.addAll(items);
  }

  @Override
  public void clearItems() {
    mItems.clear();
  }

  @Override
  public void removeItem(T item) {
    mItems.remove(item);
  }

  @Override
  public Set<? extends Cluster<T>> getClusters(double zoom) {
    long numCells = (long) Math.ceil(256 * Math.pow(2, zoom) / GRID_SIZE);
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

  @Override
  public Collection<T> getItems() {
    return mItems;
  }

  private static long getCoord(long numCells, double x, double y) {
    return (long) (numCells * Math.floor(x) + Math.floor(y));
  }
}