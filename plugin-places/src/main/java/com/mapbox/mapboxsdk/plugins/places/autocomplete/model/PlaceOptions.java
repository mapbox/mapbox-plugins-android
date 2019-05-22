package com.mapbox.mapboxsdk.plugins.places.autocomplete.model;

import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.api.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Create a PlaceOptions object which can be used to customize the autocomplete geocoder results and
 * the UI component. There are two modes to further change the autocomplete UI to fit your app
 * better.
 * <ul>
 * <li><strong>Full screen mode</strong>: will place all the search history, results, and injected
 * places into a view which has a width equal to the app width.</li>
 * <li><strong>Card view mode</strong>: the place  search history, results, and injected places in
 * an Android Support Library {@link android.support.v7.widget.CardView}</li>
 * </ul>
 *
 * @since 0.1.0
 */
@AutoValue
public abstract class PlaceOptions implements Parcelable {

  /**
   * Mode for launching the autocomplete view in fullscreen.
   *
   * @since 0.1.0
   */
  public static final int MODE_FULLSCREEN = 1;

  /**
   * Mode for launching the autocomplete view using Android cards for the layouts.
   *
   * @since 0.1.0
   */
  public static final int MODE_CARDS = 2;

  //
  // Geocoding options
  //

  /**
   * Bias local results based on a provided {@link Point}. This oftentimes increases accuracy in
   * the returned results.
   *
   * @return proximity a point defining the proximity you'd like to bias the results around
   * @since 0.1.0
   */
  @Nullable
  public abstract Point proximity();

  /**
   * Specify the language to use for response text and, for forward geocoding, query result
   * weighting. Options are IETF language tags comprised of a mandatory ISO 639-1 language code and
   * optionally one or more IETF subtags for country or script. More than one value can also be
   * specified, separated by commas.
   *
   * @return a string representing the language as a locale
   * @since 0.1.0
   */
  @Nullable
  public abstract String language();

  /**
   * Limit the number of results returned. The default is 10.
   *
   * @return the number of results returned by the geocoder
   * @since 0.1.0
   */
  public abstract int limit();

  /**
   * Limit the number of results returned. The default is the maximum number of
   * historical searches already saved by the Places plugin.
   *
   * @return the number of past search results returned by the geocoder
   * @since 0.9.0
   */
  @Nullable
  public abstract Integer historyCount();

  /**
   * Limit results to a bounding box. Options are in the format {@code minX,minY,maxX,maxY}.
   *
   * @return the string with the coordinate order minX,minY,maxX,maxY
   * @since 0.1.0
   */
  @Nullable
  public abstract String bbox();

  /**
   * This optionally can be set to filter the results returned inside the suggestions.
   * <p>
   * Note that {@link com.mapbox.api.geocoding.v5.GeocodingCriteria#TYPE_POI_LANDMARK} returns a
   * subset of the results returned by {@link com.mapbox.api.geocoding.v5.GeocodingCriteria#TYPE_POI}.
   * More than one type can be specified.
   * </p>
   *
   * @return the applied filter(s) or null if none were set
   * @since 0.1.0
   */
  @Nullable
  public abstract String geocodingTypes();

  /**
   * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated by
   * commas.
   *
   * @return a string with the country codes you are limiting the geocoder responses to
   * @since 0.1.0
   */
  @Nullable
  public abstract String country();

  /**
   * Optionally inject {@link CarmenFeature}s into the suggestion view so users can access points of
   * interest quickly without typing a single character. Typical places include, the users home,
   * work, or favorite POI.
   *
   * @return a list of serialized {@link CarmenFeature}s which show immediately when the
   * autocomplete UI is displayed to the user
   * @since 0.1.0
   */
  @Nullable
  public abstract List<String> injectedPlaces();

  //
  // View options
  //

  /**
   * When the {@link Builder#build(int)} method is called you can optionally pass in one of
   * {@link #MODE_CARDS} or {@link #MODE_FULLSCREEN} which determines which layout you'd like to use
   * for your autocomplete view.
   *
   * @return an integer representing either the {@link #MODE_CARDS} or {@link #MODE_FULLSCREEN} mode
   * @since 0.1.0
   */
  public abstract int viewMode();

  /**
   * Set the autocomplete's layout background color. Defaults {@link Color#TRANSPARENT}.
   *
   * @return the background color as a ColorInt
   * @since 0.1.0
   */
  public abstract int backgroundColor();

  /**
   * Set the autocomplete's layout toolbar color. Defaults {@link Color#WHITE}.
   *
   * @return the toolbar color as a ColorInt
   * @since 0.1.0
   */
  public abstract int toolbarColor();

  /**
   * Set the autocomplete's layout status bar color. Defaults {@link Color#BLACK}.
   *
   * @return the status bar color as a ColorInt
   * @since 0.9.0
   */
  @ColorInt
  public abstract int statusbarColor();

  /**
   * Optionally set the hint string which is shown before the user inputs a search term inside the
   * top edit text.
   *
   * @return the string used inside the edit text to display to the user before they input a
   * character
   * @since 0.1.0
   */
  @Nullable
  public abstract String hint();

  /**
   * Build a new instance of the {@link PlaceOptions} class using this method. It will return the
   * {@link Builder} which can be used to customize the users experience.
   *
   * @return an instance of {@link Builder} used to create a new {@link PlaceOptions} instance
   * @since 0.1.0
   */
  public static Builder builder() {
    return new AutoValue_PlaceOptions.Builder()
      .backgroundColor(Color.TRANSPARENT)
      .toolbarColor(Color.WHITE)
      .statusbarColor(Color.BLACK)
      .limit(10);
  }

  /**
   * Build a new instance of the {@link PlaceOptions} class using this inner builder class.
   *
   * @since 0.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    private List<String> countries = new ArrayList<>();
    private List<String> injectedPlaces = new ArrayList<>();

    /**
     * Bias local results based on a provided {@link Point}. This oftentimes increases accuracy in
     * the returned results.
     *
     * @param proximity a point defining the proximity you'd like to bias the results around
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder proximity(@Nullable Point proximity);

    /**
     * Specify the language to use for response text and, for forward geocoding, query result
     * weighting. Options are IETF language tags comprised of a mandatory ISO 639-1 language code and
     * optionally one or more IETF subtags for country or script. More than one value can also be
     * specified, separated by commas.
     *
     * @param language a string representing the language as a locale
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder language(@Nullable String language);

    /**
     * This optionally can be set to filter the results returned inside the suggestions.
     * <p>
     * Note that {@link com.mapbox.api.geocoding.v5.GeocodingCriteria#TYPE_POI_LANDMARK} returns a
     * subset of the results returned by {@link com.mapbox.api.geocoding.v5.GeocodingCriteria#TYPE_POI}.
     * More than one type can be specified.
     * </p>
     *
     * @param geocodingTypes the applied filter(s) specifically from {@link GeocodingTypeCriteria}
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public Builder geocodingTypes(@NonNull @GeocodingTypeCriteria String... geocodingTypes) {
      geocodingTypes(TextUtils.join(",", geocodingTypes));
      return this;
    }

    abstract Builder geocodingTypes(String geocodingTypes);

    /**
     * Limit the number of results returned. The default is 10.
     *
     * @param limit the number of results returned by the geocoder
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder limit(@IntRange(from = 1, to = 10) int limit);

    /**
     * Limit the number of past search results shown.
     *
     * @param historyCount the number of past historical searches shown before a search starts.
     * @return this builder instance for chaining options together
     * @since 0.9.0
     */
    public abstract Builder historyCount(@Nullable @IntRange(from = 0) Integer historyCount);

    /**
     * Limit results to a bounding box.
     *
     * @param southwest a {@link Point} which defines the southwest corner of this bounding box
     * @param northeast a {@link Point} which defines the northeast corner of this bounding box
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public Builder bbox(Point southwest, Point northeast) {
      bbox(southwest.longitude(), southwest.latitude(),
        northeast.longitude(), northeast.latitude());
      return this;
    }

    /**
     * Limit results to a bounding box.
     *
     * @param minX a longitude coordinate which defines the right of the bounding box when the map's
     *             cardinal direction is north
     * @param minY a latitude coordinate which defines the bottom of the bounding box when the map's
     *             cardinal direction is north
     * @param maxX a longitude coordinate which defines the left of the bounding box when the map's
     *             cardinal direction is north
     * @param maxY a latitude coordinate which defines the top of the bounding box when the map's
     *             cardinal direction is north
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public Builder bbox(@FloatRange(from = -180, to = 180) double minX,
                        @FloatRange(from = -90, to = 90) double minY,
                        @FloatRange(from = -180, to = 180) double maxX,
                        @FloatRange(from = -90, to = 90) double maxY) {
      bbox(String.format(Locale.US, "%s,%s,%s,%s",
        TextUtils.formatCoordinate(minX),
        TextUtils.formatCoordinate(minY),
        TextUtils.formatCoordinate(maxX),
        TextUtils.formatCoordinate(maxY))
      );
      return this;
    }

    /**
     * Limit results to a bounding box.
     *
     * @param bbox a string which describes the bounding box using the format
     *             {@code minX,minY,maxX,maxY}
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder bbox(@NonNull String bbox);

    /**
     * Limit results to one or more countries by passing in a locale.
     *
     * @param country a locale you are limiting the geocoder responses to
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public Builder country(Locale country) {
      countries.add(country.getCountry());
      return this;
    }

    /**
     * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
     * by commas.
     *
     * @param country a string array with the country codes you are limiting the geocoder responses
     *                to
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public Builder country(String... country) {
      countries.addAll(Arrays.asList(country));
      return this;
    }

    /**
     * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
     * by commas.
     *
     * @param country a string with the country codes you are limiting the geocoder responses to
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder country(String country);

    /**
     * Optionally inject {@link CarmenFeature}s into the suggestion view so users can access points
     * of interest quickly without typing a single character. Typical places include, the users
     * home, work, or favorite POI.
     *
     * @param carmenFeature a list of serialized {@link CarmenFeature}s which show immediately when
     *                      the autocomplete UI is displayed to the user
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public Builder addInjectedFeature(CarmenFeature carmenFeature) {
      carmenFeature.properties().addProperty(PlaceConstants.SAVED_PLACE, true);
      injectedPlaces.add(carmenFeature.toJson());
      return this;
    }

    /**
     * Optionally inject {@link CarmenFeature}s into the suggestion view so users can access points
     * of interest quickly without typing a single character. Typical places include, the users
     * home, work, or favorite POI.
     *
     * @param injectedPlaces a list of serialized {@link CarmenFeature}s
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    abstract Builder injectedPlaces(List<String> injectedPlaces);

    abstract Builder viewMode(int mode);

    /**
     * Set the autocomplete's layout background color. Defaults {@link Color#TRANSPARENT}.
     *
     * @param backgroundColor the background color as a ColorInt
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder backgroundColor(@ColorInt int backgroundColor);

    /**
     * Set the autocomplete's layout toolbar color. Defaults {@link Color#WHITE}.
     *
     * @param toolbarColor the views toolbar color as a ColorInt
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder toolbarColor(@ColorInt int toolbarColor);

    /**
     * Set the autocomplete's layout status bar color. Defaults {@link Color#BLACK}.
     *
     * @param statusbarColor the views status bar color as a ColorInt
     * @return this builder instance for chaining options together
     * @since 0.9.0
     */
    public abstract Builder statusbarColor(@ColorInt int statusbarColor);

    /**
     * Optionally set the hint string which is shown before the user inputs a search term inside the
     * top edit text.
     *
     * @param hint the string used inside the edit text to display to the user before they input a
     *             character
     * @return this builder instance for chaining options together
     * @since 0.1.0
     */
    public abstract Builder hint(@Nullable String hint);

    abstract PlaceOptions autoBuild();

    /**
     * Build a new instance of the {@link PlaceOptions} class using the characteristics set in this
     * {@link Builder}.
     *
     * @return a new instance of the {@link PlaceOptions} class
     * @since 0.1.0
     */
    public PlaceOptions build() {
      return build(PlaceOptions.MODE_FULLSCREEN);
    }

    /**
     * Build a new instance of the {@link PlaceOptions} class using the characteristics set in this
     * {@link Builder}.
     *
     * @param mode pass in which mode you'd like the view to be in when displayed to the user; Can
     *             be either {@link #MODE_FULLSCREEN} or {@link #MODE_CARDS}
     * @return a new instance of the {@link PlaceOptions} class
     * @since 0.1.0
     */
    public PlaceOptions build(@IntRange(from = 1, to = 2) int mode) {

      if (!countries.isEmpty()) {
        country(TextUtils.join(",", countries.toArray()));
      }

      injectedPlaces(injectedPlaces);
      viewMode(mode);

      return autoBuild();
    }
  }
}
