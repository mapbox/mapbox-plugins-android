package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SearchHistoryEntityTest {

  @Test
  public void sanity() throws Exception {
    CarmenFeature feature = CarmenFeature.builder()
      .properties(new JsonObject())
      .build();
    SearchHistoryEntity entity = new SearchHistoryEntity("placeId", feature);
    assertNotNull(entity);
  }

  @Test
  public void getPlaceId_doesReturnTheSetPlaceId() throws Exception {
    CarmenFeature feature = CarmenFeature.builder()
      .properties(new JsonObject())
      .build();
    SearchHistoryEntity entity = new SearchHistoryEntity("placeId", feature);
    assertThat(entity.getPlaceId(), equalTo("placeId"));
  }

  @Test
  public void getCarmenFeature_doesReturnTheSetCarmenFeature() throws Exception {
    CarmenFeature feature = CarmenFeature.builder()
      .id("myCarmenFeature")
      .properties(new JsonObject())
      .build();
    SearchHistoryEntity entity = new SearchHistoryEntity("placeId", feature);
    assertNotNull(entity.getCarmenFeature());
    assertThat(entity.getCarmenFeature().id(), equalTo("myCarmenFeature"));
  }
}