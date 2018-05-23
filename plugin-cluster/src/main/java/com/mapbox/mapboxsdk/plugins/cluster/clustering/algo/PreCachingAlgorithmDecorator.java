package com.mapbox.mapboxsdk.plugins.cluster.clustering.algo;

import android.support.v4.util.LruCache;

import com.mapbox.mapboxsdk.plugins.cluster.clustering.Cluster;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Optimistically fetch clusters for adjacent zoom levels, caching them as necessary.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class PreCachingAlgorithmDecorator<T extends ClusterItem> implements Algorithm<T> {
  private final Algorithm<T> mAlgorithm;

  private final LruCache<Integer, Set<? extends Cluster<T>>> mCache = new LruCache<>(5);
  private final ReadWriteLock mCacheLock = new ReentrantReadWriteLock();

  @Deprecated
  public PreCachingAlgorithmDecorator(Algorithm<T> algorithm) {
    mAlgorithm = algorithm;
  }

  @Deprecated
  public void addItem(T item) {
    mAlgorithm.addItem(item);
    clearCache();
  }

  @Deprecated
  @Override
  public void addItems(Collection<T> items) {
    mAlgorithm.addItems(items);
    clearCache();
  }

  @Deprecated
  @Override
  public void clearItems() {
    mAlgorithm.clearItems();
    clearCache();
  }

  @Deprecated
  public void removeItem(T item) {
    mAlgorithm.removeItem(item);
    clearCache();
  }

  private void clearCache() {
    mCache.evictAll();
  }

  @Deprecated
  @Override
  public Set<? extends Cluster<T>> getClusters(double zoom) {
    int discreteZoom = (int) zoom;
    Set<? extends Cluster<T>> results = getClustersInternal(discreteZoom);
    if (mCache.get(discreteZoom + 1) == null) {
      new Thread(new PrecacheRunnable(discreteZoom + 1)).start();
    }
    if (mCache.get(discreteZoom - 1) == null) {
      new Thread(new PrecacheRunnable(discreteZoom - 1)).start();
    }
    return results;
  }

  @Deprecated
  @Override
  public Collection<T> getItems() {
    return mAlgorithm.getItems();
  }

  @Deprecated
  @Override
  public void setMaxDistanceBetweenClusteredItems(int maxDistance) {
    mAlgorithm.setMaxDistanceBetweenClusteredItems(maxDistance);
    clearCache();
  }

  @Deprecated
  @Override
  public int getMaxDistanceBetweenClusteredItems() {
    return mAlgorithm.getMaxDistanceBetweenClusteredItems();
  }

  private Set<? extends Cluster<T>> getClustersInternal(int discreteZoom) {
    Set<? extends Cluster<T>> results;
    mCacheLock.readLock().lock();
    results = mCache.get(discreteZoom);
    mCacheLock.readLock().unlock();

    if (results == null) {
      mCacheLock.writeLock().lock();
      results = mCache.get(discreteZoom);
      if (results == null) {
        results = mAlgorithm.getClusters(discreteZoom);
        mCache.put(discreteZoom, results);
      }
      mCacheLock.writeLock().unlock();
    }
    return results;
  }

  private class PrecacheRunnable implements Runnable {
    private final int mZoom;

    @Deprecated
    public PrecacheRunnable(int zoom) {
      mZoom = zoom;
    }

    @Deprecated
    @Override
    public void run() {
      try {
        // Wait between 500 - 1000 ms.
        Thread.sleep((long) (Math.random() * 500 + 500));
      } catch (InterruptedException exception) {
        // ignore. keep going.
      }
      getClustersInternal(mZoom);
    }
  }
}
