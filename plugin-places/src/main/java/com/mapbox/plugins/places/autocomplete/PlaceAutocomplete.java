package com.mapbox.plugins.places.autocomplete;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mapbox.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.plugins.places.autocomplete.data.SearchHistoryDatabase;
import com.mapbox.plugins.places.autocomplete.ui.PlaceAutocompleteActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * PlaceAutocomplete provides an activity that allows a user to start typing a place name or an
 * address and see place predictions appear as they type. The activity can also display recent
 * search history, injected places (as a separate list), and more.
 * <p>
 * If the user exits the autocomplete activity without choosing a place, the calling activity will
 * receive {@link Activity#RESULT_CANCELED}.
 *
 * @since 0.1.0
 */
public class PlaceAutocomplete {

  /**
   * Mode for launching the autocomplete activity in fullscreen.
   *
   * @since 0.1.0
   */
  public static final int MODE_FULLSCREEN = 1;

  /**
   * Mode for launching the autocomplete activity using Android card views for the layouts.
   *
   * @since 0.1.0
   */
  public static final int MODE_CARDS = 2;

  private PlaceAutocomplete() {
    // Class should never be initialized.
  }

  /**
   * Returns the {@link CarmenFeature} selected by the user.
   *
   * @param data the result Intent that was provided in
   *             {@link Activity#onActivityResult(int, int, Intent)}
   * @return the users selected {@link CarmenFeature}
   */
  public static CarmenFeature getPlace(Intent data) {
    String json = data.getStringExtra(PlaceConstants.RETURNING_CARMEN_FEATURE);
    return CarmenFeature.fromJson(json);
  }

  /**
   * If the search history is being displayed in the search results section you should provide a
   * setting for the user to clear their search history. Calling this method will remove all entries
   * from the database.
   *
   * @param context your application's context
   * @since 0.1.0
   */
  public static void clearRecentHistory(Context context) {
    SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(context);
    SearchHistoryDatabase.deleteAllData(database);
  }

  /**
   * Builder for the Place Autocomplete launch intent.
   * <p>
   * After setting the required and optional parameters, call {@link IntentBuilder#build(Activity)}
   * and pass the intent to {@link Activity#startActivityForResult(Intent, int)}.
   * </p>
   *
   * @since 0.1.0
   */
  public static class IntentBuilder {

    private final ArrayList<String> countries;
    private final Intent intent;
    private final int mode;

    /**
     * Creates a new builder that creates an intent to launch the autocomplete activity.
     *
     * @param mode Either {@link PlaceAutocomplete#MODE_FULLSCREEN} or
     *             {@link PlaceAutocomplete#MODE_CARDS}
     * @since 0.1.0
     */
    public IntentBuilder(@IntRange(from = 1, to = 2) int mode) {
      countries = new ArrayList<>();
      intent = new Intent();
      this.mode = mode;
    }

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link com.mapbox.services.exceptions.ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use the
     *                    Geocoding API
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder accessToken(@NonNull String accessToken) {
      intent.putExtra(PlaceConstants.ACCESS_TOKEN, accessToken);
      return this;
    }

    /**
     * Limit the results to a defined bounding box. Unlike {@link #proximity(Point)}, this will
     * strictly limit results to within the bounding box only. If simple biasing is desired rather
     * than a strict region, use proximity instead.
     *
     * @param northeast the northeast corner of the bounding box as a {@link Point}
     * @param southwest the southwest corner of the bounding box as a {@link Point}
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder boundingBoxBias(@NonNull Point northeast, @NonNull Point southwest) {
      intent.putExtra(PlaceConstants.BBOX_SOUTHWEST_POINT, southwest.toJson());
      intent.putExtra(PlaceConstants.BBOX_NORTHEAST_POINT, northeast.toJson());
      return this;
    }

    /**
     * Add a single country locale to restrict the results. This method can be called as many times
     * as needed inorder to add multiple countries.
     *
     * @param country limit geocoding results to one
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder country(Locale country) {
      countries.add(country.getCountry());
      return this;
    }

    /**
     * Limit results to one or more countries. Options are ISO 3166 alpha 2 country codes separated
     * by commas.
     *
     * @param country limit geocoding results to one
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder country(String... country) {
      countries.addAll(Arrays.asList(country));
      return this;
    }

    /**
     * Bias local results based on a provided {@link Point}. This oftentimes increases accuracy in
     * the returned results.
     *
     * @param proximity a point defining the proximity you'd like to bias the results around
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder proximity(@NonNull Point proximity) {
      intent.putExtra(PlaceConstants.PROXIMITY, proximity.toJson());
      return this;
    }

    /**
     * This optionally can be set to filter the results returned back after making the geocoding
     * request. A null value can't be passed in and only values defined in
     * {@link GeocodingTypeCriteria} are allowed.
     * <p>
     * Note that {@link GeocodingTypeCriteria#TYPE_POI_LANDMARK} returns a subset of the results
     * returned by {@link GeocodingTypeCriteria#TYPE_POI}. More than one type can be specified.
     * </p>
     *
     * @param geocodingTypes optionally filter the result types by one or more defined types inside
     *                       the {@link GeocodingTypeCriteria}
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder types(@NonNull @GeocodingTypeCriteria String... geocodingTypes) {
      String types = TextUtils.join(",", geocodingTypes);
      intent.putExtra(PlaceConstants.TYPE, types);
      return this;
    }

    /**
     * This optionally specifies the desired response language for user queries. Results that match
     * the requested language are favored over results in other languages. If more than one language
     * tag is supplied separating them by a comma, text in all requested languages will be returned.
     * <p>
     * Any valid IETF language tag can be submitted, and a best effort will be made to return
     * results in the requested language or languages, falling back first to similar and then to
     * common languages in the event that text is not available in the requested language. In the
     * event a fallback language is used, the language field will have a different value than the
     * one requested.
     * <p>
     * Translation availability varies by language and region, for a full list of supported regions,
     * see the link provided below.
     *
     * @param languages a String specifying the language or languages you'd like results to support
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#request-format">Supported languages</a>
     * @since 0.1.0
     */
    public IntentBuilder language(String languages) {
      intent.putExtra(PlaceConstants.LANGUAGE, languages);
      return this;
    }

    /**
     * This optionally specifies the maximum number of results to return. the default is 5 and the
     * maximum is 10.
     *
     * @param limit the number of returned results
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder limit(@IntRange(from = 1, to = 10) int limit) {
      intent.putExtra(PlaceConstants.LIMIT, limit);
      return this;
    }

    /**
     * Insert a list of {@link CarmenFeature}s which you'd like to highlight without the user
     * performing a query. These will show up in a different list view.
     *
     * @param carmenFeatures a list of {@link CarmenFeature}s which you'd like to highlight in the
     *                       results view
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder injectPlaces(List<CarmenFeature> carmenFeatures) {
      ArrayList<String> serialized = new ArrayList<>(carmenFeatures.size());
      for (CarmenFeature carmenFeature : carmenFeatures) {
        carmenFeature.properties().addProperty(PlaceConstants.SAVED_PLACE, true);
        serialized.add(carmenFeature.toJson());
      }
      intent.putStringArrayListExtra(PlaceConstants.INJECTED_PLACES, serialized);
      return this;
    }

    /**
     * Set the background color of the activity.
     *
     * @param backgroundColor the color you'd like the background to show as
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public IntentBuilder backgroundColor(@ColorInt int backgroundColor) {
      intent.putExtra(PlaceConstants.BACKGROUND, backgroundColor);
      return this;
    }

    /**
     * Retrieves the current Intent as configured by the Builder.
     *
     * @param activity the current activity which you are launching the autocomplete activity from
     * @return the current Intent configured by this builder
     * @since 0.1.0
     */
    public Intent build(Activity activity) {
      intent.putStringArrayListExtra(PlaceConstants.COUNTRIES, countries);
      intent.putExtra(PlaceConstants.MODE, mode);
      intent.setClass(activity, PlaceAutocompleteActivity.class);
      return intent;
    }
  }
}
