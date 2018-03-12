package com.mapbox.mapboxsdk.plugins.places.picker.model;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;

public class PlacePickerOptionsTest {

  @Test
  public void builder() throws Exception {
    PlaceOptions.Builder builder = PlaceOptions.builder();
    assertNotNull(builder);
  }

  @Test
  public void builder_geocodingTypes() {
    PlaceOptions placeOptions = PlaceOptions.builder()
      .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS, GeocodingCriteria.TYPE_DISTRICT).build();
    Assert.assertThat(placeOptions.geocodingTypes(), equalTo("address,district"));
  }
}