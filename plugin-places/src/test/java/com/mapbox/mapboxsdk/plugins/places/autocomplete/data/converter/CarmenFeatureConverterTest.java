package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.converter;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CarmenFeatureConverterTest {

  @Test
  public void toCarmenFeature_equalsNull() throws Exception {
    CarmenFeature carmenFeature = CarmenFeatureConverter.toCarmenFeature(null);
    assertNull(carmenFeature);
  }

  @Test
  public void toCarmenFeature_successfullyConvertsToFeature() throws Exception {
    String json = "{\"id\":\"place.9962989141465270\",\"type\":\"Feature\","
      + "\"place_type\":[\"place\"],\"relevance\":0.99,\"properties\":{\"wikidata\":\"Q65\"},"
      + "\"text\":\"Los Angeles\",\"place_name\":\"Los Angeles, California, United States\","
      + "\"bbox\":[-118.529221009603,33.901599990108,-118.121099990025,34.1612200099034],"
      + "\"center\":[-118.2439,34.0544],\"geometry\":{\"type\":\"Point\","
      + "\"coordinates\":[-118.2439,34.0544]},\"context\":[{\"id\":\"region.3591\","
      + "\"short_code\":\"US-CA\",\"wikidata\":\"Q99\",\"text\":\"California\"},"
      + "{\"id\":\"country.3145\",\"short_code\":\"us\",\"wikidata\":\"Q30\","
      + "\"text\":\"United States\"}]}";

    CarmenFeature carmenFeature = CarmenFeatureConverter.toCarmenFeature(json);
    assertNotNull(carmenFeature);
    assertThat(carmenFeature.id(), equalTo("place.9962989141465270"));
    assertThat(carmenFeature.type(), equalTo("Feature"));
    assertThat(carmenFeature.relevance(), equalTo(0.99));
    assertThat(carmenFeature.text(), equalTo("Los Angeles"));
    assertThat(carmenFeature.placeName(),
      equalTo("Los Angeles, California, United States"));
  }

  @Test
  public void fromCarmenFeature_doesSerializeCorrectly() throws Exception {
    String json = "{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"Point\","
      + "\"coordinates\":[1.0, 2.0]},\"properties\":{},\"address\":\"address\","
      + "\"matching_place_name\":\"matchingPlaceName\",\"language\":\"language\"}";

    CarmenFeature carmenFeature = CarmenFeature.builder()
      .geometry(Point.fromLngLat(1.0, 2.0))
      .address("address")
      .bbox(null)
      .id("id")
      .language("language")
      .matchingPlaceName("matchingPlaceName")
      .properties(new JsonObject())
      .build();
    String serializedFeature = CarmenFeatureConverter.fromCarmenFeature(carmenFeature);
    assertThat(serializedFeature, equalTo(json));
  }
}