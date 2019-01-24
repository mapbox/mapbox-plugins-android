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

/**
 * A {@link MapLocale} object builds off of the {@link Locale} object and provides additional
 * geographical information particular to the Mapbox Maps SDK. Like Locale, MapLocale can be used to
 * make the <em>map locale sensitive</em>.
 * <p>
 * The {@link MapLocale} object can be used to acquire the matching Locale's map language; useful for
 * translating the map language into one of the supported ones found in {@link Languages}.
 * <p>
 * You'll also be able to get bounding box information for that same country so the map's starting
 * position target can adjust itself over the device's Locale country.
 * <p>
 * A handful of {@link MapLocale}'s are already constructed and offered through this class as static
 * variables. If a country is missing and you'd like to add it, you can use one of the
 * {@link MapLocale} constructors to build a valid map locale. Once this is done, you need to add it
 * to the Locale cache using {@link MapLocale#addMapLocale(Locale, MapLocale)} where the first
 * parameter is the {@link Locale} object which matches up with your newly created
 * {@link MapLocale}.
 *
 * @since 0.1.0
 */
public final class MapLocale {

  /*
   * Supported Mapbox map languages.
   */

  /**
   * The name (or names) used locally for the place.
   */
  public static final String LOCAL_NAME = "name";

  /**
   * English (if available)
   */
  public static final String ENGLISH = "name_en";

  /**
   * French (if available)
   */
  public static final String FRENCH = "name_fr";

  /**
   * Arabic (if available)
   */
  public static final String ARABIC = "name_ar";

  /**
   * Spanish (if available)
   */
  public static final String SPANISH = "name_es";

  /**
   * German (if available)
   */
  public static final String GERMAN = "name_de";

  /**
   * Portuguese (if available)
   */
  public static final String PORTUGUESE = "name_pt";

  /**
   * Russian (if available)
   */
  public static final String RUSSIAN = "name_ru";

  /**
   * Chinese (if available)
   */
  public static final String CHINESE = "name_zh";

  /**
   * Chinese (if available)
   */
  static final String CHINESE_V8 = "name_zh-Hant";

  /**
   * Simplified Chinese (if available)
   */
  public static final String SIMPLIFIED_CHINESE = "name_zh-Hans";

  /**
   * Japanese (if available)
   */
  public static final String JAPANESE = "name_ja";

  /**
   * Korean (if available)
   */
  public static final String KOREAN = "name_ko";

  @Retention(SOURCE)
  @StringDef( {LOCAL_NAME, ENGLISH, FRENCH, SIMPLIFIED_CHINESE, ARABIC, SPANISH, GERMAN, PORTUGUESE,
    RUSSIAN, CHINESE, JAPANESE, KOREAN})
  public @interface Languages {
  }

  /*
   * Some Country Bounding Boxes used for the default provided MapLocales.
   */

  /**
   * USA Bounding box excluding Hawaii and Alaska extracted from Open Street Map
   */
  static final LatLngBounds USA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(49.388611, -124.733253))
    .include(new LatLng(24.544245, -66.954811)).build();

  /**
   * UK Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds UK_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(59.360249, -8.623555))
    .include(new LatLng(49.906193, 1.759)).build();

  /**
   * Canada Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds CANADA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(83.110626, -141.0))
    .include(new LatLng(41.67598, -52.636291)).build();

  /**
   * China Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds CHINA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(53.56086, 73.557693))
    .include(new LatLng(15.775416, 134.773911)).build();

  /**
   * Germany Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds GERMANY_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(55.055637, 5.865639))
    .include(new LatLng(47.275776, 15.039889)).build();

  /**
   * Korea Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds KOREA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(38.612446, 125.887108))
    .include(new LatLng(33.190945, 129.584671)).build();

  /**
   * Japan Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds JAPAN_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(45.52314, 122.93853))
    .include(new LatLng(24.249472, 145.820892)).build();

  /**
   * France Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds FRANCE_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(51.092804, -5.142222))
    .include(new LatLng(41.371582, 9.561556)).build();

  /**
   * Peoples Republic of China Bounding Box extracted from Open Street Map
   */
  static final LatLngBounds PRC_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(53.56086, 73.557693))
    .include(new LatLng(15.775416, 134.773911)).build();

  /**
   * Russian Bounding box extracted from Open Street Map
   */
  static final LatLngBounds RUSSIA_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(81.856903, -168.997849))
    .include(new LatLng(41.185902, 19.638861)).build();

  /**
   * Spain Bounding box extracted from Open Street Map
   */
  static final LatLngBounds SPAIN_BBOX = new LatLngBounds.Builder()
    .include(new LatLng(27.4335426, -18.3936845))
    .include(new LatLng(43.9933088, 4.5918885)).build();

  /*
   * Some MapLocales already defined (these match with the predefined ones in the Locale class)
   */

  /**
   * Useful constant for country.
   */
  public static final MapLocale FRANCE = new MapLocale(FRENCH, FRANCE_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale GERMANY = new MapLocale(GERMAN, GERMANY_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale JAPAN = new MapLocale(JAPANESE, JAPAN_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale KOREA = new MapLocale(KOREAN, KOREA_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale CHINA = new MapLocale(SIMPLIFIED_CHINESE, CHINA_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale PRC = new MapLocale(SIMPLIFIED_CHINESE, PRC_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale UK = new MapLocale(ENGLISH, UK_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale US = new MapLocale(ENGLISH, USA_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale CANADA = new MapLocale(ENGLISH, CANADA_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale CANADA_FRENCH = new MapLocale(FRENCH, CANADA_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale RUSSIA = new MapLocale(RUSSIAN, RUSSIA_BBOX);

  /**
   * Useful constant for country.
   */
  public static final MapLocale SPAIN = new MapLocale(SPANISH, SPAIN_BBOX);

  /**
   * Maps out the Matching pair of {@link Locale} and {@link MapLocale}. In other words, if I have a
   * {@link Locale#CANADA}, this should be matched up with {@link MapLocale#CANADA}.
   */
  private static final Map<Locale, MapLocale> LOCALE_SET;

  static {
    LOCALE_SET = new HashMap<>();
    LOCALE_SET.put(Locale.US, MapLocale.US);
    LOCALE_SET.put(Locale.CANADA_FRENCH, MapLocale.CANADA_FRENCH);
    LOCALE_SET.put(Locale.CANADA, MapLocale.CANADA);
    LOCALE_SET.put(Locale.CHINA, MapLocale.CHINA);
    LOCALE_SET.put(Locale.PRC, MapLocale.PRC);
    LOCALE_SET.put(Locale.UK, MapLocale.UK);
    LOCALE_SET.put(Locale.JAPAN, MapLocale.JAPAN);
    LOCALE_SET.put(Locale.KOREA, MapLocale.KOREA);
    LOCALE_SET.put(Locale.GERMANY, MapLocale.GERMANY);
    LOCALE_SET.put(Locale.FRANCE, MapLocale.FRANCE);
    LOCALE_SET.put(new Locale("ru", "RU"), RUSSIA);
    LOCALE_SET.put(new Locale("es", "ES"), SPAIN);
  }

  private final LatLngBounds countryBounds;
  private final String mapLanguage;

  /**
   * Construct a new MapLocale instance using one of the map languages found in {@link Languages}.
   *
   * @param mapLanguage a non-null string which is allowed from {@link Languages}
   * @since 0.1.0
   */
  public MapLocale(@NonNull String mapLanguage) {
    this(mapLanguage, null);
  }

  /**
   * Construct a new MapLocale instance by passing in a LatLngBounds object.
   *
   * @param countryBounds non-null {@link LatLngBounds} object which wraps around the country
   * @since 0.1.0
   */
  public MapLocale(@NonNull LatLngBounds countryBounds) {
    this(LOCAL_NAME, countryBounds);
  }

  /**
   * /**
   * Construct a new MapLocale instance using one of the map languages found in {@link Languages}
   * and also passing in a LatLngBounds object.
   *
   * @param mapLanguage   a non-null string which is allowed from {@link Languages}
   * @param countryBounds {@link LatLngBounds} object which wraps around the country
   * @since 0.1.0
   */
  public MapLocale(@NonNull @Languages String mapLanguage, @Nullable LatLngBounds countryBounds) {
    this.countryBounds = countryBounds;
    this.mapLanguage = mapLanguage;
  }

  /**
   * Returns the Map Language which can be fed directly into {@code textField} in runtime styling to
   * change language.
   *
   * @return a string representing the map language code.
   * @since 0.1.0
   */
  @NonNull
  public String getMapLanguage() {
    return mapLanguage;
  }

  /**
   * Returns a {@link LatLngBounds} which represents the viewport bounds which allow for the entire
   * viewing of a country within the devices viewport.
   *
   * @return a {@link LatLngBounds} which can be used when user locations unknown but locale is
   * @since 0.1.0
   */
  @Nullable
  public LatLngBounds getCountryBounds() {
    return countryBounds;
  }

  /**
   * When creating a new MapLocale, you'll need to associate a {@link Locale} so that
   * {@link Locale#getDefault()} will find the correct corresponding {@link MapLocale}.
   *
   * @param locale    a valid {@link Locale} instance shares a 1 to 1 relationship with the
   *                  {@link MapLocale}
   * @param mapLocale the {@link MapLocale} which shares a 1 to 1 relationship with the
   *                  {@link Locale}
   * @since 0.1.0
   */
  public static void addMapLocale(@NonNull Locale locale, @NonNull MapLocale mapLocale) {
    LOCALE_SET.put(locale, mapLocale);
  }

  /**
   * Passing in a Locale, you are able to receive the {@link MapLocale} object which it is currently
   * paired with. If this returns null, there was no matching {@link MapLocale} to go along with the
   * passed in Locale. If you expected a non-null result, you should make sure you used
   * {@link #addMapLocale(Locale, MapLocale)} before making this call.
   *
   * @param locale the locale which you'd like to receive its matching {@link MapLocale} if one exists
   * @since 0.1.0
   */
  @Nullable
  public static MapLocale getMapLocale(@NonNull Locale locale) {
    return getMapLocale(locale, false);
  }

  /**
   * Passing in a Locale, you are able to receive the {@link MapLocale} object which it is currently
   * paired with. If this returns null, there was no matching {@link MapLocale} to go along with the
   * passed in Locale. If you expected a non-null result, you should make sure you used
   * {@link #addMapLocale(Locale, MapLocale)} before making this call.
   *
   * @param locale         the locale which you'd like to receive its matching {@link MapLocale} if one exists
   * @param acceptFallback whether the locale should fallback to the first declared that matches the language,
   *                       the fallback locale can be added with {@link #addMapLocale(Locale, MapLocale)}
   * @return the matching {@link MapLocale} if one exists, otherwise null
   * @see #getMapLocaleFallback(Locale)
   * @since 0.1.0
   */
  @Nullable
  public static MapLocale getMapLocale(@NonNull Locale locale, boolean acceptFallback) {
    MapLocale foundLocale = LOCALE_SET.get(locale);
    if (acceptFallback && foundLocale == null) {
      foundLocale = getMapLocaleFallback(locale);
    }
    return foundLocale;
  }

  /**
   * Passing in a Locale, you are able to receive the {@link MapLocale} object which it is currently
   * paired with as a fallback. If this returns null, there was no matching {@link MapLocale} to go along with the
   * passed in Locale. If you expected a non-null result, you should make sure you used
   * {@link #addMapLocale(Locale, MapLocale)} before making this call.
   *
   * @param locale the locale which you'd like to receive its matching {@link MapLocale}(fallback) if one exists
   * @return the matching {@link MapLocale} if one exists, otherwise null
   * @since 0.1.0
   */
  @Nullable
  private static MapLocale getMapLocaleFallback(@NonNull Locale locale) {
    String fallbackCode = locale.getLanguage().substring(0, 2);
    MapLocale foundMapLocale = null;

    for (Locale possibleLocale : LOCALE_SET.keySet()) {
      if (possibleLocale.getLanguage().equals(fallbackCode)) {
        foundMapLocale = LOCALE_SET.get(possibleLocale);
        break;
      }
    }
    return foundMapLocale;
  }
}