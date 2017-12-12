package com.mapbox.plugins.places.autocomplete.ui;

import com.mapbox.geocoding.v5.models.CarmenFeature;

public interface PlaceSelectionListener {

  public void onPlaceSelected(CarmenFeature carmenFeature);

  public void onCancel();
}
