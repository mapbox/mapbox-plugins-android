package com.mapbox.mapboxsdk.plugins.places.autocomplete.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.DataRepository;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.PlacesDatabase;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity.PlaceEntity;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PlaceAutocompleteViewModel extends AndroidViewModel
  implements Callback<GeocodingResponse> {

  public final MutableLiveData<GeocodingResponse> geocodingLiveData;
  private MapboxGeocoding.Builder geocoderBuilder;
  private PlaceOptions placeOptions;

  PlaceAutocompleteViewModel(@NonNull Application application, @NonNull PlaceOptions placeOptions) {
    super(application);
    this.placeOptions = placeOptions;
    geocodingLiveData = new MutableLiveData<>();
  }

  public void buildGeocodingRequest(String accessToken) {
    geocoderBuilder = MapboxGeocoding.builder().autocomplete(true);
    geocoderBuilder.accessToken(accessToken);
    geocoderBuilder.limit(placeOptions.limit());

    // Proximity
    Point proximityPoint = placeOptions.proximity();
    if (proximityPoint != null) {
      geocoderBuilder.proximity(proximityPoint);
    }

    // Language
    String languageJson = placeOptions.language();
    if (languageJson != null) {
      geocoderBuilder.languages(languageJson);
    }

    // Type
    String typeJson = placeOptions.geocodingTypes();
    if (typeJson != null) {
      geocoderBuilder.geocodingTypes(typeJson);
    }

    // Countries
    String countriesJson = placeOptions.country();
    if (countriesJson != null) {
      geocoderBuilder.country(countriesJson);
    }

    // Bounding box
    String bbox = placeOptions.bbox();
    if (bbox != null) {
      geocoderBuilder.bbox(bbox);
    }
  }

  public void onQueryChange(CharSequence sequence) {
    String query = sequence.toString();
    if (query.isEmpty()) {
      return;
    }
    if (geocoderBuilder != null) {
      geocoderBuilder.query(query).build().enqueueCall(this);
    } else {
      throw new NullPointerException("An access token must be set before a geocoding query can be "
        + "made.");
    }
  }

  public List<CarmenFeature> getFavoritePlaces() {
    List<String> serialized = placeOptions.injectedPlaces();
    List<CarmenFeature> favoriteFeatures = new ArrayList<>();
    if (serialized == null || serialized.isEmpty()) {
      return favoriteFeatures;
    }
    for (String serializedCarmenFeature : serialized) {
      favoriteFeatures.add(CarmenFeature.fromJson(serializedCarmenFeature));
    }
    return favoriteFeatures;
  }

  @Override
  public void onResponse(@NonNull Call<GeocodingResponse> call,
                         @NonNull Response<GeocodingResponse> response) {
    if (response.isSuccessful()) {
      geocodingLiveData.setValue(response.body());
    } else {
      geocodingLiveData.setValue(null);
    }
  }

  @Override
  public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
    Timber.e(throwable);
    geocodingLiveData.setValue(null);
  }

  public PlacesDatabase getDatabase() {
    return PlacesDatabase.getInstance(this.getApplication().getApplicationContext());
  }

  public void saveCarmenFeatureToDatabase(CarmenFeature carmenFeature, boolean favoriteItem) {
    // Check that the carmenFeature hasn't already been added
    if (carmenFeature.properties().has(PlaceConstants.SAVED_PLACE)) {
      return;
    }
    PlaceEntity searchHistory = new PlaceEntity(carmenFeature.id(), carmenFeature, favoriteItem);
    DataRepository.getInstance(getDatabase()).addSearchHistoryEntity(searchHistory);
  }

  public static class Factory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;
    private final PlaceOptions placeOptions;

    public Factory(@NonNull Application application, @NonNull PlaceOptions placeOptions) {
      this.application = application;
      this.placeOptions = placeOptions;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@Nullable Class<T> modelClass) {
      //noinspection unchecked
      return (T) new PlaceAutocompleteViewModel(application, placeOptions);
    }
  }
}
