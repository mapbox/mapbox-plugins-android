package com.mapbox.mapboxsdk.plugins.places.autocomplete.model;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

public interface Place {

  String getPlaceId();

  CarmenFeature getCarmenFeature();

  boolean getFavoriteItem();
}
