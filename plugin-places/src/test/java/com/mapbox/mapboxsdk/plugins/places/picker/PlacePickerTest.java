package com.mapbox.mapboxsdk.plugins.places.picker;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;

import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlacePickerTest {

  @Test
  public void getPlace_returnsNullWhenJsonNotFound() throws Exception {
    Intent data = new Intent();
    CarmenFeature carmenFeature = PlacePicker.getPlace(data);
    assertNull(carmenFeature);
  }

  @Test
  public void getPlace_returnsDeserializedCarmenFeature() throws Exception {
    String json = "{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"Point\","
      + "\"coordinates\":[1.0, 2.0]},\"properties\":{},\"address\":\"address\","
      + "\"matching_place_name\":\"matchingPlaceName\",\"language\":\"language\"}";

    Intent data = mock(Intent.class);
    data.putExtra(PlaceConstants.RETURNING_CARMEN_FEATURE, json);
    when(data.getStringExtra(PlaceConstants.RETURNING_CARMEN_FEATURE)).thenReturn(json);
    CarmenFeature carmenFeature = PlacePicker.getPlace(data);
    assertNotNull(carmenFeature);
    assertThat(carmenFeature.type(), equalTo("Feature"));
    assertThat(carmenFeature.id(), equalTo("id"));
    assertThat(carmenFeature.address(), equalTo("address"));
    assertThat(carmenFeature.matchingPlaceName(), equalTo("matchingPlaceName"));
    assertThat(carmenFeature.language(), equalTo("language"));
  }

  // TODO finish mocking this class
  @Test
  @Ignore
  public void getLastCameraPosition() throws Exception {
    CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(new LatLng(2.0, 3.0))
      .bearing(30)
      .zoom(20)
      .build();

    Parcel parcel = mock(Parcel.class);
    cameraPosition.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    Intent data = mock(Intent.class);
    data.putExtra(PlaceConstants.MAP_CAMERA_POSITION, cameraPosition);
    CameraPosition position = PlacePicker.getLastCameraPosition(data);
    assertNotNull(position);
  }

  @Test
  public void intentBuilder_initializesCorrectly() throws Exception {
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    assertNotNull(builder);
  }

  // TODO finish mocking this class
  @Test
  @Ignore
  public void intentBuilder_accessToken() throws Exception {
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    Intent intent = builder.accessToken("pk.XXX").build(mock(Activity.class));
    assertNotNull(intent);
    assertThat(intent.getStringExtra(PlaceConstants.ACCESS_TOKEN), equalTo("pk.XXX"));
  }
}