package com.mapbox.plugins.places.autocomplete;

import com.mapbox.geocoding.v5.models.CarmenFeature;

public interface OnCardItemClickListener {
  void onItemClick(CarmenFeature carmenFeature);
}