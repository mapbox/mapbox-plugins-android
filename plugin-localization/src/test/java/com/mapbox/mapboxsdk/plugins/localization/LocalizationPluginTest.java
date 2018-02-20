package com.mapbox.mapboxsdk.plugins.localization;


import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;

import static org.mockito.Mockito.mock;

public class LocalizationPluginTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void sanity() throws Exception {

  }

  @Test
  public void setMapLanguage_does() throws Exception {
    Locale.setDefault(Locale.CANADA);
    LocalizationPlugin localizationPlugin = new LocalizationPlugin(mock(MapboxMap.class));
    localizationPlugin.setMapLanguage();

  }

  @Test(expected = NullPointerException.class)
  public void setMapLanguage_doesThrowNullPointerException() throws Exception {
    LocalizationPlugin localizationPlugin = new LocalizationPlugin(mock(MapboxMap.class));
    localizationPlugin.setMapLanguage(new Locale("foo", "bar"));
  }

  //  @Test
//  public void getDefault_doesNotMatchMapLocale() throws Exception {
//    Locale.setDefault(new Locale("foo", "bar"));
//    LocalizationPlugin.setDefaultMapLocale(MapLocale.FRANCE);
//    MapLocale defaultLocale = LocalizationPlugin.getDefault();
//    assertThat(defaultLocale.getMapLanguage(), equalTo(MapLocale.FRENCH));
//    assertThat(defaultLocale.getCountryBounds(), equalTo(MapLocale.FRANCE_BBOX));
//  }
//
//  @Test
//  public void getDefault_doesMatchToCorrectMapLocale() throws Exception {
//    Locale.setDefault(Locale.FRANCE);
//    assertThat(LocalizationPlugin.getDefault().getMapLanguage(), equalTo(MapLocale.FRENCH));
//  }
//
//  @Test
//  public void getMapLocale_doesReturnCustomLocale() throws Exception {
//    Locale locale = new Locale("foo", "bar");
//    MapLocale mapLocale = new MapLocale("name_foo");
//    LocalizationPlugin.addMapLocale(locale, mapLocale);
//    MapLocale finalLocale = LocalizationPlugin.getMapLocale(locale);
//    assertThat(finalLocale.getMapLanguage(), equalTo("name_foo"));
//  }
}