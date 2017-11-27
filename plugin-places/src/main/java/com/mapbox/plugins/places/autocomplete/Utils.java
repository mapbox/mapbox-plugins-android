package com.mapbox.plugins.places.autocomplete;

import android.content.Intent;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geojson.Point;

public class Utils {

  private Utils() {
  }

  static MapboxGeocoding.Builder initiateSearchQuery(Intent intent) {
    MapboxGeocoding.Builder geocoderBuilder = MapboxGeocoding.builder().autocomplete(true);
    geocoderBuilder.accessToken(intent.getStringExtra(PlaceConstants.ACCESS_TOKEN));
    geocoderBuilder.limit(intent.getIntExtra(PlaceConstants.LIMIT, 5));

    // Proximity
    String proximityPointJson = intent.getStringExtra(PlaceConstants.PROXIMITY);
    if (proximityPointJson != null) {
      geocoderBuilder.proximity(Point.fromJson(proximityPointJson));
    }

    // Language
    String languageJson = intent.getStringExtra(PlaceConstants.LANGUAGE);
    if (languageJson != null) {
      geocoderBuilder.languages(languageJson);
    }

    // Type
    String typeJson = intent.getStringExtra(PlaceConstants.TYPE);
    if (typeJson != null) {
      geocoderBuilder.geocodingTypes(typeJson);
    }

    // Countries
    String countriesJson = intent.getStringExtra(PlaceConstants.COUNTRIES);
    if (countriesJson != null) {
      geocoderBuilder.geocodingTypes(countriesJson);
    }

    // Bounding box
    String southwest = intent.getStringExtra(PlaceConstants.BBOX_SOUTHWEST_POINT);
    String northeast = intent.getStringExtra(PlaceConstants.BBOX_NORTHEAST_POINT);
    if (southwest != null && northeast != null) {
      Point southwestPoint = Point.fromJson(southwest);
      Point northeastPoint = Point.fromJson(northeast);
      geocoderBuilder.bbox(southwestPoint, northeastPoint);
    }
    return geocoderBuilder;
  }
}
