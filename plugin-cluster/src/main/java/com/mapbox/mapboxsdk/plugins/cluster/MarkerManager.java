package com.mapbox.mapboxsdk.plugins.cluster;

import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Keeps track of collections of markers on the map. Delegates all Marker-related events to each
 * collection's individually managed listeners.
 * <p/>
 * All marker operations (adds and removes) should occur via its collection class. That is, don't
 * add a marker via a collection, then remove it via Marker.remove()
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
public class MarkerManager implements MapboxMap.OnInfoWindowClickListener, MapboxMap.OnMarkerClickListener,
  MapboxMap.InfoWindowAdapter {

  private final MapboxMap mMap;

  private final Map<String, Collection> mNamedCollections = new HashMap<String, Collection>();
  private final Map<Marker, Collection> mAllMarkers = new HashMap<Marker, Collection>();

  @Deprecated
  public MarkerManager(MapboxMap map) {
    this.mMap = map;
  }

  @Deprecated
  public Collection newCollection() {
    return new Collection();
  }

  /**
   * Create a new named collection, which can later be looked up by {@link #getCollection(String)}
   *
   * @param id a unique id for this collection.
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public Collection newCollection(String id) {
    if (mNamedCollections.get(id) != null) {
      throw new IllegalArgumentException("collection id is not unique: " + id);
    }
    Collection collection = new Collection();
    mNamedCollections.put(id, collection);
    return collection;
  }

  /**
   * Gets a named collection that was created by {@link #newCollection(String)}
   *
   * @param id the unique id for this collection.
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public Collection getCollection(String id) {
    return mNamedCollections.get(id);
  }

  @Deprecated
  @Override
  public View getInfoWindow(Marker marker) {
    Collection collection = mAllMarkers.get(marker);
    if (collection != null && collection.mInfoWindowAdapter != null) {
      return collection.mInfoWindowAdapter.getInfoWindow(marker);
    }
    return null;
  }

  @Deprecated
  @Override
  public boolean onInfoWindowClick(Marker marker) {
    Collection collection = mAllMarkers.get(marker);
    if (collection != null && collection.mInfoWindowClickListener != null) {
      collection.mInfoWindowClickListener.onInfoWindowClick(marker);
    }
    return true;
  }

  @Deprecated
  @Override
  public boolean onMarkerClick(Marker marker) {
    Collection collection = mAllMarkers.get(marker);
    if (collection != null && collection.mMarkerClickListener != null) {
      return collection.mMarkerClickListener.onMarkerClick(marker);
    }
    return false;
  }

  /**
   * Removes a marker from its collection.
   *
   * @param marker the marker to remove.
   * @return true if the marker was removed.
   * @deprecated use runtime styling to cluster markers instead
   */
  @Deprecated
  public boolean remove(Marker marker) {
    Collection collection = mAllMarkers.get(marker);
    return collection != null && collection.remove(marker);
  }

  @Deprecated
  public class Collection {
    private final Set<Marker> mMarkers = new HashSet<Marker>();
    private MapboxMap.OnInfoWindowClickListener mInfoWindowClickListener;
    private MapboxMap.OnMarkerClickListener mMarkerClickListener;
    //    private MapboxMap.OnMarkerDragListener mMarkerDragListener;
    private MapboxMap.InfoWindowAdapter mInfoWindowAdapter;

    @Deprecated
    public Collection() {
    }

    @Deprecated
    public Marker addMarker(MarkerOptions opts) {
      Marker marker = mMap.addMarker(opts);
      mMarkers.add(marker);
      mAllMarkers.put(marker, Collection.this);
      return marker;
    }

    @Deprecated
    public boolean remove(Marker marker) {
      if (mMarkers.remove(marker)) {
        mAllMarkers.remove(marker);
        marker.remove();
        return true;
      }
      return false;
    }

    @Deprecated
    public void clear() {
      for (Marker marker : mMarkers) {
        marker.remove();
        mAllMarkers.remove(marker);
      }
      mMarkers.clear();
    }

    @Deprecated
    public java.util.Collection<Marker> getMarkers() {
      return Collections.unmodifiableCollection(mMarkers);
    }

    @Deprecated
    public void setOnInfoWindowClickListener(MapboxMap.OnInfoWindowClickListener infoWindowClickListener) {
      mInfoWindowClickListener = infoWindowClickListener;
    }

    @Deprecated
    public void setOnMarkerClickListener(MapboxMap.OnMarkerClickListener markerClickListener) {
      mMarkerClickListener = markerClickListener;
    }

    @Deprecated
    public void setOnInfoWindowAdapter(MapboxMap.InfoWindowAdapter infoWindowAdapter) {
      mInfoWindowAdapter = infoWindowAdapter;
    }
  }
}
