package com.mapbox.plugins.places.autocomplete;

import com.mapbox.geocoding.v5.models.CarmenFeature;

/**
 * Used internally to detect when a user has selected an item from one of the results list.
 *
 * @since 0.1.0
 */
interface OnCardItemClickListener {
  void onItemClick(CarmenFeature carmenFeature);
}