package com.mapbox.plugins.places.autocomplete.ui;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;

interface ResultClickCallback {
  void onClick(CarmenFeature carmenFeature);
}