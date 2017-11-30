package com.mapbox.plugins.places.autocomplete.ui;

import com.mapbox.geocoding.v5.models.CarmenFeature;

interface ResultClickCallback {
  void onClick(CarmenFeature carmenFeature);
}