package com.mapbox.mapboxsdk.plugins.localization;


import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class LocalizationPluginTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    LocalizationPlugin localizationPlugin = new LocalizationPlugin(mock(MapView.class), mock(MapboxMap.class));
    assertNotNull(localizationPlugin);
  }

  @Test(expected = NullPointerException.class)
  public void setMapLanguage_localePassedInNotValid() throws Exception {
    LocalizationPlugin localizationPlugin = new LocalizationPlugin(mock(MapView.class), mock(MapboxMap.class));
    localizationPlugin.setMapLanguage(new Locale("foo", "bar"));
  }
}