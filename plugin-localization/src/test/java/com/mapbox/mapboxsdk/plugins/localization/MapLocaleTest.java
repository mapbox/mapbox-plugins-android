package com.mapbox.mapboxsdk.plugins.localization;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MapLocaleTest {

  @Test
  public void sanity() throws Exception {
    MapLocale locale = new MapLocale("foo", MapLocale.FRANCE_BBOX);
    assertThat(locale.getMapLanguage(), equalTo(MapLocale.FRENCH));
    assertThat(locale.getCountryBounds(), equalTo(MapLocale.FRANCE_BBOX));
  }

  @Test
  public void addMapLocale_doesGetAddedAndReferencedCorrectly() throws Exception {
    Locale locale = new Locale("foo", "bar");
    MapLocale mapLocale = new MapLocale("abc");
    MapLocale.addMapLocale(locale, mapLocale);
    MapLocale mapLocale1 = MapLocale.getMapLocale(locale);
    Assert.assertThat(mapLocale1.getMapLanguage(), equalTo("abc"));
  }
}
