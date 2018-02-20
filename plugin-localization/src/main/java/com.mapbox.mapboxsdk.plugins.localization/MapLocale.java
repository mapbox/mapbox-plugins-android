package com.mapbox.mapboxsdk.plugins.localization;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public final class MapLocale {

  /**
   * The supported map languages
   */
  public static final String LOCAL_NAME = "name";
  public static final String ENGLISH = "name_en";
  public static final String FRENCH = "name_fr";
  public static final String SIMPLIFIED_CHINESE = "name_zh-Hans";
  public static final String ARABIC = "name_ar";
  public static final String SPANISH = "name_es";
  public static final String GERMAN = "name_de";
  public static final String PORTUGUESE = "name_pt";
  public static final String RUSSIAN = "name_ru";
  public static final String CHINESE = "name_zh";

  @Retention(SOURCE)
  @StringDef( {LOCAL_NAME, ENGLISH, FRENCH, SIMPLIFIED_CHINESE, ARABIC, SPANISH, GERMAN, PORTUGUESE,
    RUSSIAN, CHINESE})
  public @interface Languages {
  }

  /**
   * Some convenient bbox values for the default provided MapLocale's.
   */
  static final LatLngBounds USA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(49.388611, -124.733253))
    .include(new LatLng(24.544245, -66.954811)).build();
  static final LatLngBounds UK_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(59.360249, -8.623555))
    .include(new LatLng(49.906193, 1.759)).build();
  static final LatLngBounds CANADA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(83.110626, -141.0))
    .include(new LatLng(41.67598, -52.636291)).build();
  static final LatLngBounds CHINA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(53.56086, 73.557693))
    .include(new LatLng(15.775416, 134.773911)).build();
  static final LatLngBounds GERMANY_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(55.055637, 5.865639))
    .include(new LatLng(47.275776, 15.039889)).build();
  static final LatLngBounds KOREA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(38.612446, 125.887108))
    .include(new LatLng(33.190945, 129.584671)).build();
  static final LatLngBounds JAPAN_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(45.52314, 122.93853))
    .include(new LatLng(24.249472, 145.820892)).build();
  static final LatLngBounds FRANCE_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(51.092804, -5.142222))
    .include(new LatLng(41.371582, 9.561556)).build();
  static final LatLngBounds ITALY_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(47.095196, 6.614889))
    .include(new LatLng(36.652779, 18.513445)).build();
  static final LatLngBounds PRC_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(53.56086, 73.557693))
    .include(new LatLng(15.775416, 134.773911)).build();
  static final LatLngBounds TAIWAN_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(25.29825, 119.534691))
    .include(new LatLng(21.901806, 122.000443)).build();

  /**
   * Some countries already defined
   */
  public static final MapLocale FRANCE = new MapLocale(FRENCH, FRANCE_BBOX);
  public static final MapLocale GERMANY = new MapLocale(GERMAN, GERMANY_BBOX);
  public static final MapLocale ITALY = new MapLocale(LOCAL_NAME, ITALY_BBOX);
  public static final MapLocale JAPAN = new MapLocale(LOCAL_NAME, JAPAN_BBOX);
  public static final MapLocale KOREA = new MapLocale(LOCAL_NAME, KOREA_BBOX);
  public static final MapLocale CHINA = new MapLocale(SIMPLIFIED_CHINESE, CHINA_BBOX);
  public static final MapLocale PRC = new MapLocale(SIMPLIFIED_CHINESE, CHINA_BBOX);
  public static final MapLocale TAIWAN = new MapLocale(CHINESE, TAIWAN_BBOX);
  public static final MapLocale UK = new MapLocale(ENGLISH, UK_BBOX);
  public static final MapLocale US = new MapLocale(ENGLISH, USA_BBOX);
  public static final MapLocale CANADA = new MapLocale(ENGLISH, CANADA_BBOX);
  public static final MapLocale CANADA_FRENCH = new MapLocale(FRENCH, CANADA_BBOX);


  private static final Map<Locale, MapLocale> LOCALE_SET;

  static {
    LOCALE_SET = new HashMap<>();
    LOCALE_SET.put(Locale.US, MapLocale.US);
    LOCALE_SET.put(Locale.CANADA_FRENCH, MapLocale.CANADA_FRENCH);
    LOCALE_SET.put(Locale.CANADA, MapLocale.CANADA);
    LOCALE_SET.put(Locale.CHINA, MapLocale.CHINA);
    LOCALE_SET.put(Locale.TAIWAN, MapLocale.TAIWAN);
    LOCALE_SET.put(Locale.PRC, MapLocale.PRC);
    LOCALE_SET.put(Locale.ITALY, MapLocale.ITALY);
    LOCALE_SET.put(Locale.UK, MapLocale.UK);
    LOCALE_SET.put(Locale.JAPAN, MapLocale.JAPAN);
    LOCALE_SET.put(Locale.KOREA, MapLocale.KOREA);
    LOCALE_SET.put(Locale.GERMANY, MapLocale.GERMANY);
    LOCALE_SET.put(Locale.FRANCE, MapLocale.FRANCE);
  }

  private final LatLngBounds countryBounds;
  private final String mapLanguage;

  public MapLocale(@NonNull @Languages String mapLanguage) {
    this(mapLanguage, null);
  }

  public MapLocale(@NonNull LatLngBounds countryBounds) {
    this(LOCAL_NAME, countryBounds);
  }

  public MapLocale(@NonNull @Languages String mapLanguage, @Nullable LatLngBounds countryBounds) {
    this.countryBounds = countryBounds;
    this.mapLanguage = mapLanguage;
  }

  @NonNull
  public String getMapLanguage() {
    return mapLanguage;
  }

  @Nullable
  public LatLngBounds getCountryBounds() {
    return countryBounds;
  }

  public static void addMapLocale(@NonNull Locale locale, @NonNull MapLocale mapLocale) {
    LOCALE_SET.put(locale, mapLocale);
  }

  @Nullable
  public static MapLocale getMapLocale(@NonNull Locale locale) {
    return LOCALE_SET.get(locale);
  }
}