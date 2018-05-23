package com.mapbox.mapboxsdk.plugins.cluster.clustering;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.cluster.MarkerManager;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.algo.Algorithm;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.algo.PreCachingAlgorithmDecorator;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.view.ClusterRenderer;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.view.DefaultClusterRenderer;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import timber.log.Timber;

/**
 * Groups many items on a map based on zoom level.
 * <p/>
 * ClusterManagerPlugin should be added to the map as an: <ul> <li>{@link MapboxMap.OnCameraIdleListener}</li>
 * <li>{@link MapboxMap.OnMarkerClickListener}</li> </ul>
 * <p>
 * This is a port of https://github.com/googlemaps/android-maps-utils to use mapbox as the backend.
 * for clustering functionality at the core level,
 * please follow the work on https://github.com/mapbox/mapbox-gl-native/issues/5814
 * </p>
 *
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class ClusterManagerPlugin<T extends ClusterItem> implements
  MapboxMap.OnCameraIdleListener,
  MapboxMap.OnMarkerClickListener,
  MapboxMap.OnInfoWindowClickListener {

  private final MarkerManager mMarkerManager;
  private final MarkerManager.Collection mMarkers;
  private final MarkerManager.Collection mClusterMarkers;

  private Algorithm<T> mAlgorithm;
  private final ReadWriteLock mAlgorithmLock = new ReentrantReadWriteLock();
  private ClusterRenderer<T> mRenderer;

  private MapboxMap mMap;
  private CameraPosition mPreviousCameraPosition;
  private ClusterTask mClusterTask;
  private final ReadWriteLock mClusterTaskLock = new ReentrantReadWriteLock();

  private OnClusterItemClickListener<T> mOnClusterItemClickListener;
  private OnClusterInfoWindowClickListener<T> mOnClusterInfoWindowClickListener;
  private OnClusterItemInfoWindowClickListener<T> mOnClusterItemInfoWindowClickListener;
  private OnClusterClickListener<T> mOnClusterClickListener;

  @Deprecated
  public ClusterManagerPlugin(Context context, MapboxMap map) {
    this(context, map, new MarkerManager(map));
  }

  @Deprecated
  public ClusterManagerPlugin(Context context, MapboxMap map, MarkerManager markerManager) {
    mMap = map;
    mMarkerManager = markerManager;
    mClusterMarkers = markerManager.newCollection();
    mMarkers = markerManager.newCollection();
    mRenderer = new DefaultClusterRenderer<T>(context, map, this);
    mAlgorithm = new PreCachingAlgorithmDecorator<T>(new NonHierarchicalDistanceBasedAlgorithm<T>());
    mClusterTask = new ClusterTask();
    mRenderer.onAdd();
  }

  @Deprecated
  public MarkerManager.Collection getMarkerCollection() {
    return mMarkers;
  }

  @Deprecated
  public MarkerManager.Collection getClusterMarkerCollection() {
    return mClusterMarkers;
  }

  @Deprecated
  public MarkerManager getMarkerManager() {
    return mMarkerManager;
  }

  @Deprecated
  public void setRenderer(ClusterRenderer<T> view) {
    mRenderer.setOnClusterClickListener(null);
    mRenderer.setOnClusterItemClickListener(null);
    mClusterMarkers.clear();
    mMarkers.clear();
    mRenderer.onRemove();
    mRenderer = view;
    mRenderer.onAdd();
    mRenderer.setOnClusterClickListener(mOnClusterClickListener);
    mRenderer.setOnClusterInfoWindowClickListener(mOnClusterInfoWindowClickListener);
    mRenderer.setOnClusterItemClickListener(mOnClusterItemClickListener);
    mRenderer.setOnClusterItemInfoWindowClickListener(mOnClusterItemInfoWindowClickListener);
    cluster();
  }

  @Deprecated
  public void setAlgorithm(Algorithm<T> algorithm) {
    mAlgorithmLock.writeLock().lock();
    try {
      if (mAlgorithm != null) {
        algorithm.addItems(mAlgorithm.getItems());
      }
      mAlgorithm = new PreCachingAlgorithmDecorator<T>(algorithm);
    } finally {
      mAlgorithmLock.writeLock().unlock();
    }
    cluster();
  }

  @Deprecated
  public void setAnimation(boolean animate) {
    mRenderer.setAnimation(animate);
  }

  @Deprecated
  public ClusterRenderer<T> getRenderer() {
    return mRenderer;
  }

  @Deprecated
  public Algorithm<T> getAlgorithm() {
    return mAlgorithm;
  }

  @Deprecated
  public void clearItems() {
    mAlgorithmLock.writeLock().lock();
    try {
      mAlgorithm.clearItems();
    } finally {
      mAlgorithmLock.writeLock().unlock();
    }
  }

  @Deprecated
  public void addItems(Collection<T> items) {
    mAlgorithmLock.writeLock().lock();
    try {
      mAlgorithm.addItems(items);
    } finally {
      mAlgorithmLock.writeLock().unlock();
    }

  }

  @Deprecated
  public void addItem(T myItem) {
    mAlgorithmLock.writeLock().lock();
    try {
      mAlgorithm.addItem(myItem);
    } finally {
      mAlgorithmLock.writeLock().unlock();
    }
  }

  @Deprecated
  public void removeItem(T item) {
    mAlgorithmLock.writeLock().lock();
    try {
      mAlgorithm.removeItem(item);
    } finally {
      mAlgorithmLock.writeLock().unlock();
    }
  }

  /**
   * Force a re-cluster. You may want to call this after adding new item(s).
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public void cluster() {
    mClusterTaskLock.writeLock().lock();
    try {
      // Attempt to cancel the in-flight request.
      mClusterTask.cancel(true);
      mClusterTask = new ClusterTask();
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        mClusterTask.execute((float) mMap.getCameraPosition().zoom);
      } else {
        mClusterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (float) mMap.getCameraPosition().zoom);
      }
    } finally {
      mClusterTaskLock.writeLock().unlock();
    }
  }

  /**
   * Might re-cluster.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  @Override
  public void onCameraIdle() {
    Timber.d("OnCamerIdle");
    if (mRenderer instanceof MapboxMap.OnCameraIdleListener) {
      ((MapboxMap.OnCameraIdleListener) mRenderer).onCameraIdle();
    }

    // Don't re-compute clusters if the map has just been panned/tilted/rotated.
    CameraPosition position = mMap.getCameraPosition();
    if (mPreviousCameraPosition != null && mPreviousCameraPosition.zoom == position.zoom) {
      return;
    }
    mPreviousCameraPosition = mMap.getCameraPosition();
    Timber.e("OnCluster");
    cluster();
  }

  @Deprecated
  @Override
  public boolean onInfoWindowClick(@NonNull Marker marker) {
    getMarkerManager().onInfoWindowClick(marker);
    return true;
  }

  @Deprecated
  @Override
  public boolean onMarkerClick(Marker marker) {
    return getMarkerManager().onMarkerClick(marker);
  }

  /**
   * Runs the clustering algorithm in a background thread, then re-paints when results come back.
   */
  private class ClusterTask extends AsyncTask<Float, Void, Set<? extends Cluster<T>>> {
    @Override
    protected Set<? extends Cluster<T>> doInBackground(Float... zoom) {
      mAlgorithmLock.readLock().lock();
      try {
        return mAlgorithm.getClusters(zoom[0]);
      } finally {
        mAlgorithmLock.readLock().unlock();
      }
    }

    @Override
    protected void onPostExecute(Set<? extends Cluster<T>> clusters) {
      mRenderer.onClustersChanged(clusters);
    }
  }

  /**
   * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
   * the ClusterManagerPlugin must be added as a click listener to the map.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public void setOnClusterClickListener(OnClusterClickListener<T> listener) {
    mOnClusterClickListener = listener;
    mRenderer.setOnClusterClickListener(listener);
  }

  /**
   * Sets a callback that's invoked when a Cluster is tapped. Note: For this listener to function,
   * the ClusterManagerPlugin must be added as a info window click listener to the map.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public void setOnClusterInfoWindowClickListener(OnClusterInfoWindowClickListener<T> listener) {
    mOnClusterInfoWindowClickListener = listener;
    mRenderer.setOnClusterInfoWindowClickListener(listener);
  }

  /**
   * Sets a callback that's invoked when an individual ClusterItem is tapped. Note: For this
   * listener to function, the ClusterManagerPlugin must be added as a click listener to the map.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public void setOnClusterItemClickListener(OnClusterItemClickListener<T> listener) {
    mOnClusterItemClickListener = listener;
    mRenderer.setOnClusterItemClickListener(listener);
  }

  /**
   * Sets a callback that's invoked when an individual ClusterItem's Info Window is tapped. Note: For this
   * listener to function, the ClusterManagerPlugin must be added as a info window click listener to the map.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public void setOnClusterItemInfoWindowClickListener(OnClusterItemInfoWindowClickListener<T> listener) {
    mOnClusterItemInfoWindowClickListener = listener;
    mRenderer.setOnClusterItemInfoWindowClickListener(listener);
  }

  /**
   * Called when a Cluster is clicked.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public interface OnClusterClickListener<T extends ClusterItem> {
    public boolean onClusterClick(Cluster<T> cluster);
  }

  /**
   * Called when a Cluster's Info Window is clicked.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public interface OnClusterInfoWindowClickListener<T extends ClusterItem> {
    public void onClusterInfoWindowClick(Cluster<T> cluster);
  }

  /**
   * Called when an individual ClusterItem is clicked.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public interface OnClusterItemClickListener<T extends ClusterItem> {
    public boolean onClusterItemClick(T item);
  }

  /**
   * Called when an individual ClusterItem's Info Window is clicked.
   *
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public interface OnClusterItemInfoWindowClickListener<T extends ClusterItem> {
    public void onClusterItemInfoWindowClick(T item);
  }
}
