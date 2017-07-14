package com.mapbox.mapboxsdk.plugins.locationlayer;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class LocationLayerTest {

  @Mock
  MapView mapView;

  @Mock
  MapboxMap mapboxMap;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test(expected = NullPointerException.class)
  public void testNonNullAnnotatedArgs() {
    new LocationLayerPlugin(null, null, null);
  }

  //  @Test
  //  public void testSanity() {
  //    new LocationLayerPlugin(mapView, mapboxMap);
  //  }
}

