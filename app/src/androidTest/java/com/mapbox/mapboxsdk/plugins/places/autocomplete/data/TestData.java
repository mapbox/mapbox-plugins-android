package com.mapbox.mapboxsdk.plugins.places.autocomplete.data;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

final class TestData {

  static final CarmenFeature CARMEN_FEATURE = CarmenFeature.builder()
    .id("placeId")
    .address("address")
    .language("language")
    .placeName("placeName")
    .relevance(1.0)
    .text("text")
    .properties(new JsonObject())
    .build();

  static final CarmenFeature CARMEN_FEATURE_TWO = CarmenFeature.builder()
    .id("placeIdTwo")
    .address("addressTwo")
    .language("languageTwo")
    .placeName("placeNameTwo")
    .relevance(0.5)
    .text("textTwo")
    .properties(new JsonObject())
    .build();

  static final SearchHistoryEntity SEARCH_HISTORY_ENTITY
    = new SearchHistoryEntity(CARMEN_FEATURE.placeName(), CARMEN_FEATURE);

  static final SearchHistoryEntity SEARCH_HISTORY_ENTITY_TWO
    = new SearchHistoryEntity(CARMEN_FEATURE_TWO.placeName(), CARMEN_FEATURE_TWO);
}
