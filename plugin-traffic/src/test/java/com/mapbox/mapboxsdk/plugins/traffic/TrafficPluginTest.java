package com.mapbox.mapboxsdk.plugins.traffic;

import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.log.LoggerDefinition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrafficPluginTest {

  @Mock
  private MapView mapView;

  @Mock
  private MapboxMap mapboxMap;

  @Mock
  private Style style;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test(expected = RuntimeException.class)
  public void testNonNullAnnotatedArgs() {
    new TrafficPlugin(null, null, null);
  }

  @Test
  public void testSanity() {
    when(style.isFullyLoaded()).thenReturn(true);
    new TrafficPlugin(mapView, mapboxMap, style);
  }

  @Test
  public void testToggle() {
    Logger.setLoggerDefinition(mock(LoggerDefinition.class));
    when(style.isFullyLoaded()).thenReturn(true);
    TrafficPlugin trafficPlugin = new TrafficPlugin(mapView, mapboxMap, style);
    assertFalse(trafficPlugin.isVisible());
    trafficPlugin.setVisibility(true);
    assertTrue(trafficPlugin.isVisible());
  }

  @Test(expected = RuntimeException.class)
  public void testNotLoadedStyle() {
    when(style.isFullyLoaded()).thenReturn(false);
    new TrafficPlugin(mapView, mapboxMap, style);
  }
}
