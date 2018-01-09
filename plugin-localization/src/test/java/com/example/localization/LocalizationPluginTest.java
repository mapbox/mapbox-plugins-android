package com.example.localization;


import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class LocalizationPluginTest {
  @Mock
  MapboxMap mapboxMap;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test(expected = NullPointerException.class)
  public void testNonNullAnnotatedArgs() {
    new LocalizationPlugin(null);
  }

  @Test
  public void testSanity() {
    new LocalizationPlugin(mapboxMap);
  }
}