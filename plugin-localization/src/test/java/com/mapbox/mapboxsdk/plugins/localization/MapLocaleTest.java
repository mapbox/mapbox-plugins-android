package com.mapbox.mapboxsdk.plugins.localization;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class MapLocaleTest {

  @Test
  public void sanity() throws Exception {
    MapLocale locale = new MapLocale(MapLocale.FRENCH, MapLocale.FRANCE_BBOX);
    assertThat(locale.getMapLanguage(), equalTo(MapLocale.FRENCH));
    assertThat(locale.getCountryBounds(), equalTo(MapLocale.FRANCE_BBOX));
  }

  @Test
  public void addMapLocale_doesGetAddedAndReferencedCorrectly() throws Exception {
    Locale locale = new Locale("foo", "bar");
    MapLocale mapLocale = new MapLocale("abc");
    MapLocale.addMapLocale(locale, mapLocale);
    MapLocale mapLocale1 = MapLocale.getMapLocale(locale, false);
    Assert.assertThat(mapLocale1.getMapLanguage(), equalTo("abc"));
  }

  @Test
  public void regionalVariantsCanFallbackCorrectly() throws Exception {
    Locale localeHN = new Locale("es_HN", "Honduras");
    MapLocale mapLocale1 = MapLocale.getMapLocale(localeHN, true);
    assertThat(mapLocale1.getMapLanguage(), equalTo(MapLocale.SPANISH));

    Locale localeUS = new Locale("es_US", "United States of America");
    MapLocale mapLocale2 = MapLocale.getMapLocale(localeUS, true);
    assertThat(mapLocale2.getMapLanguage(), equalTo(MapLocale.SPANISH));
  }

  @Test
  public void regionalVariantsNoFallbackWhenNotRequested() throws Exception {
    Locale localeHN = new Locale("es_HN", "Honduras");
    MapLocale mapLocale = MapLocale.getMapLocale(localeHN, false);
    assertNull(mapLocale);
  }
}
