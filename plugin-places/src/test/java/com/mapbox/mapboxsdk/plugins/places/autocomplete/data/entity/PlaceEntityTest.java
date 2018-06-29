package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PlaceEntityTest {

  @Test
  public void sanity() {
    CarmenFeature feature = CarmenFeature.builder()
      .properties(new JsonObject())
      .build();
    PlaceEntity entity = new PlaceEntity("placeId", feature, false);
    assertNotNull(entity);
  }

  @Test
  public void getPlaceId_doesReturnTheSetPlaceId() {
    CarmenFeature feature = CarmenFeature.builder()
      .properties(new JsonObject())
      .build();
    PlaceEntity entity = new PlaceEntity("placeId", feature, false);
    assertThat(entity.getPlaceId(), equalTo("placeId"));
  }

  @Test
  public void getCarmenFeature_doesReturnTheSetCarmenFeature() {
    CarmenFeature feature = CarmenFeature.builder()
      .id("myCarmenFeature")
      .properties(new JsonObject())
      .build();
    PlaceEntity entity = new PlaceEntity("placeId", feature, false);
    assertNotNull(entity.getCarmenFeature());
    assertThat(entity.getCarmenFeature().id(), equalTo("myCarmenFeature"));
  }

  @Test
  public void getFavoriteItem_doesReturnTheSetFavoriteItemValue() {
    CarmenFeature feature = CarmenFeature.builder()
      .id("myCarmenFeature")
      .properties(new JsonObject())
      .build();
    PlaceEntity entity = new PlaceEntity("placeId", feature, true);
    assertNotNull(entity.getCarmenFeature());
    assertThat(entity.getFavoriteItem(), equalTo(true));
  }
}