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
import static org.hamcrest.CoreMatchers.containsString;
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

  @Test
  public void setMapLanguage_localePassedInNotValid() throws Exception {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage(containsString("has no matching MapLocale object. You need to create"));
    LocalizationPlugin localizationPlugin = new LocalizationPlugin(mock(MapView.class), mock(MapboxMap.class));
    localizationPlugin.setMapLanguage(new Locale("foo", "bar"));
  }
}