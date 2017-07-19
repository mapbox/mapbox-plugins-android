package com.mapbox.mapboxsdk.plugins.traffic;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrafficPluginTest {

  @Mock
  MapView mapView;

  @Mock
  MapboxMap mapboxMap;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test(expected = NullPointerException.class)
  public void testNonNullAnnotatedArgs() {
    new TrafficPlugin(null, null);
  }

  @Test
  public void testSanity() {
    new TrafficPlugin(mapView, mapboxMap);
  }

  @Test
  public void testToggle() {
    TrafficPlugin trafficPlugin = new TrafficPlugin(mapView, mapboxMap);
    assertFalse(trafficPlugin.isVisible());
    trafficPlugin.setVisibility(true);
    assertTrue(trafficPlugin.isVisible());
  }
}
