package com.mapbox.plugins.places.common.utils;

import com.mapbox.geocoding.v5.models.CarmenFeature;

public class GeocodingUtils {

  private GeocodingUtils() {
    // No Instances.
  }

  public static String removeNameFromAddress(CarmenFeature carmenFeature) {
    String address = carmenFeature.placeName();
    return address.replace((carmenFeature.text().concat(", ")), "");
  }
}