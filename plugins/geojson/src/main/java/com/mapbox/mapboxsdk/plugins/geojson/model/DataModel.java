package com.mapbox.mapboxsdk.plugins.geojson.model;

import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
  private List<MarkerData> markers;
  private List<PolyData> polygons;
  private List<PolyData> polylines;
  private LatLngBounds bounds;

  public List<MarkerData> getMarkers() {
    return markers;
  }

  public void setMarkers(List<MarkerData> markers) {
    this.markers = markers;
  }

  public List<PolyData> getPolygons() {
    return polygons;
  }

  public void setPolygons(List<PolyData> polygons) {
    this.polygons = polygons;
  }

  public List<PolyData> getPolylines() {
    return polylines;
  }

  public void setPolylines(List<PolyData> polylines) {
    this.polylines = polylines;
  }

  public LatLngBounds getBounds() {
    return bounds;
  }

  public void setBounds(LatLngBounds bounds) {
    this.bounds = bounds;
  }

  /**
   * add MarkerData to list of marker
   *
   * @param markerData the instance of MarkerData class
   */
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
   */

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
   */
  public void addPolyline(PolyData polyData) {
    if (this.polylines != null) {
      this.polylines.add(polyData);
    } else {
      this.polylines = new ArrayList<>();
      this.polylines.add(polyData);
    }
  }
}
