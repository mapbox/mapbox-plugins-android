package com.mapbox.pluginstylejson;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class StyleJsonParser {
  private Gson gson = new Gson();

  GeoJsonSource parserGeoJsonSource(@NonNull String id, @NonNull String json) {
    GeoJsonSource geoJsonSource = new GeoJsonSource(id);
    JsonObject source = gson.fromJson(json, JsonObject.class);
    if (source.has("data")) {
      JsonObject data = source.getAsJsonObject("data");
      String dataString = data.getAsString();
      if (dataString.startsWith("http")) {
        geoJsonSource.setUrl(dataString);
      } else {
        String type = data.get("type").getAsString();
        if ("Feature".equals(type)) {
          geoJsonSource.setGeoJson(Feature.fromJson(dataString));
        } else if ("FeatureCollection".equals(type)) {
          geoJsonSource.setGeoJson(FeatureCollection.fromJson(dataString));
        } else if ("GeometryCollection".equals(type)) {
          geoJsonSource.setGeoJson(GeometryCollection.fromJson(dataString));
        } else if ("LineString".equals(type)) {
          geoJsonSource.setGeoJson(LineString.fromJson(dataString));
        } else if ("MultiLineString".equals(type)) {
          geoJsonSource.setGeoJson(MultiLineString.fromJson(dataString));
        } else if ("MultiPoint".equals(type)) {
          geoJsonSource.setGeoJson(MultiPoint.fromJson(dataString));
        } else if ("MultiPolygon".equals(type)) {
          geoJsonSource.setGeoJson(MultiPolygon.fromJson(dataString));
        } else if ("Point".equals(type)) {
          geoJsonSource.setGeoJson(Point.fromJson(dataString));
        } else if ("Polygon".equals(type)) {
          geoJsonSource.setGeoJson(Polygon.fromJson(dataString));
        }
      }
    }

    return geoJsonSource;
  }
}
