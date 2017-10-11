package com.mapbox.mapboxsdk.plugins.markerlayer;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Source of Marker geometry data.
 * <p>
 * Wraps a geojson source and update the source with Point feature collection data.
 * </p>
 */
class MarkerSource {

  static final String SOURCE_ID = "com.mapbox.mapboxsdk.plugins.markerlayer.sourceid";

  private final List<Feature> features = new ArrayList<>();
  private final GeoJsonSource geoJsonSource;

  MarkerSource(MapboxMap map) {
    geoJsonSource = new GeoJsonSource(SOURCE_ID);
    map.addSource(geoJsonSource);
  }

  void addMarker(Marker marker, String id) {
    features.add(createFeature(marker, id));
    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));
  }

  private Feature createFeature(Marker marker, String id) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("marker-id", id);
    jsonObject.addProperty("icon-opacity", marker.getIconOpacity());
    jsonObject.addProperty("icon-rotate", marker.getIconRotate());
    jsonObject.addProperty("icon-size", marker.getIconSize());
    jsonObject.addProperty("icon-id", marker.getIconId());
    return Feature.fromGeometry(createPoint(marker), jsonObject, id);
  }

  private Point createPoint(Marker marker) {
    LatLng latLng = marker.getPosition();
    Position position = Position.fromCoordinates(latLng.getLongitude(), latLng.getLatitude(), latLng.getAltitude());
    return Point.fromCoordinates(position);
  }
}
