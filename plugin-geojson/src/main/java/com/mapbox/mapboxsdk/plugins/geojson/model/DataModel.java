package com.mapbox.mapboxsdk.plugins.geojson.model;

import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DataModel {
  private List<MarkerData> markers;
  private List<PolyData> polygons;
  private List<PolyData> polylines;
  private LatLngBounds bounds;

  @Deprecated
  public List<MarkerData> getMarkers() {
    return markers;
  }

  @Deprecated
  public void setMarkers(List<MarkerData> markers) {
    this.markers = markers;
  }

  @Deprecated
  public List<PolyData> getPolygons() {
    return polygons;
  }

  @Deprecated
  public void setPolygons(List<PolyData> polygons) {
    this.polygons = polygons;
  }

  @Deprecated
  public List<PolyData> getPolylines() {
    return polylines;
  }

  @Deprecated
  public void setPolylines(List<PolyData> polylines) {
    this.polylines = polylines;
  }

  @Deprecated
  public LatLngBounds getBounds() {
    return bounds;
  }

  @Deprecated
  public void setBounds(LatLngBounds bounds) {
    this.bounds = bounds;
  }

  /**
   * add MarkerData to list of marker
   *
   * @param markerData the instance of MarkerData class
   * @deprecated this plugin has been deprecated and will no longer be supported. Similar
   * functionality is built into Runtime Styling found inside the Map SDK.
   */
  @Deprecated
  public void addMarker(MarkerData markerData) {
    if (this.markers != null) {
      this.markers.add(markerData);
    } else {
      this.markers = new ArrayList<>();
      this.markers.add(markerData);
    }
  }

  /**
   * add PolyData to list of polygon
   *
   * @param polyData the instance of PolyData class
   * @deprecated this plugin has been deprecated and will no longer be supported. Similar
   * functionality is built into Runtime Styling found inside the Map SDK.
   */
  @Deprecated
  public void addPolygon(PolyData polyData) {
    if (this.polygons != null) {
      this.polygons.add(polyData);
    } else {
      this.polygons = new ArrayList<>();
      this.polygons.add(polyData);
    }
  }

  /**
   * add PolyData to list of polyline
   *
   * @param polyData the instance of PolyData class
   * @deprecated this plugin has been deprecated and will no longer be supported. Similar
   * functionality is built into Runtime Styling found inside the Map SDK.
   */
  @Deprecated
  public void addPolyline(PolyData polyData) {
    if (this.polylines != null) {
      this.polylines.add(polyData);
    } else {
      this.polylines = new ArrayList<>();
      this.polylines.add(polyData);
    }
  }
}
