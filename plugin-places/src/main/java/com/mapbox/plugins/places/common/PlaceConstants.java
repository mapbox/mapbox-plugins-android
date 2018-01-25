package com.mapbox.plugins.places.common;

public final class PlaceConstants {

  public static final String RETURNING_CARMEN_FEATURE
    = "com.mapbox.mapboxsdk.plugins.places.carmenfeat";
  public static final String ACCESS_TOKEN = "com.mapbox.mapboxsdk.plugins.places.accessToken";
  public static final String COUNTRIES = "com.mapbox.mapboxsdk.plugins.places.countries";
  public static final String SAVED_PLACE = "com.mapbox.mapboxsdk.plugins.places.savedcarmenfeat";
  public static final String MODE = "com.mapbox.mapboxsdk.plugins.places.mode";
  public static final String PLACE_OPTIONS = "com.mapbox.mapboxsdk.plugins.places.placeOptions";

  private PlaceConstants() {
    throw new AssertionError("No Instances.");
  }
}
