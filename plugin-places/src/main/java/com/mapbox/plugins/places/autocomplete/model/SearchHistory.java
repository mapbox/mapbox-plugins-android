package com.mapbox.plugins.places.autocomplete.model;

import com.mapbox.geocoding.v5.models.CarmenFeature;

public interface SearchHistory {
  String getPlaceId();

  CarmenFeature getCarmenFeature();
}
