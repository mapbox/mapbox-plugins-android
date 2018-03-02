package com.mapbox.mapboxsdk.plugins.places.autocomplete;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.SearchHistoryDatabase;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteActivity;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;

import java.util.ArrayList;

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
public final class PlaceAutocomplete {

  private PlaceAutocomplete() {
    // No instances
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

    /**
     * Creates a new builder that creates an intent to launch the autocomplete activity.
     *
     * @since 0.1.0
     */
    public IntentBuilder() {
      countries = new ArrayList<>();
      intent = new Intent();
    }

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link com.mapbox.core.exceptions.ServicesException} will be thrown.
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

    public IntentBuilder placeOptions(PlaceOptions placeOptions) {
      intent.putExtra(PlaceConstants.PLACE_OPTIONS, placeOptions);
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
      intent.setClass(activity, PlaceAutocompleteActivity.class);
      return intent;
    }
  }
}
