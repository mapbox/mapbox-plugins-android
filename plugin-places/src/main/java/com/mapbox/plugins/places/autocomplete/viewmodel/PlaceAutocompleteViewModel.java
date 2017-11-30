package com.mapbox.plugins.places.autocomplete.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.plugins.places.autocomplete.DataRepository;
import com.mapbox.plugins.places.autocomplete.PlaceConstants;
import com.mapbox.plugins.places.autocomplete.data.SearchHistoryDatabase;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;
import com.mapbox.plugins.places.autocomplete.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceAutocompleteViewModel extends AndroidViewModel
  implements Callback<GeocodingResponse> {

  public final MutableLiveData<GeocodingResponse> geocodingLiveData = new MutableLiveData<>();
  private MapboxGeocoding.Builder geocoderBuilder;
  private final Intent intent;

  PlaceAutocompleteViewModel(@NonNull Application application, @NonNull Intent intent) {
    super(application);
    this.intent = intent;
  }

  public void buildGeocodingRequest() {
    geocoderBuilder = Utils.initiateSearchQuery(intent);
  }

  public void onQueryChange(CharSequence sequence) {
    String query = sequence.toString();
    if (query.isEmpty()) {
      return;
    }
    geocoderBuilder.query(query).build().enqueueCall(this);
  }

  public Intent onItemClicked(CarmenFeature carmenFeature) {
    saveCarmenFeatureToDatabase(carmenFeature);

    String json = carmenFeature.toJson();
    Intent returningIntent = new Intent();
    returningIntent.putExtra(PlaceConstants.RETURNING_CARMEN_FEATURE, json);
    return returningIntent;
  }

  public List<CarmenFeature> getFavoritePlaces() {
    List<String> serialized = intent.getStringArrayListExtra(PlaceConstants.INJECTED_PLACES);
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
    }
  }

  @Override
  public void onFailure(@NonNull Call<GeocodingResponse> call,
                        @NonNull Throwable throwable) {

  }

  public SearchHistoryDatabase getDatabase() {
    return SearchHistoryDatabase.getInstance(this.getApplication().getApplicationContext());
  }

  private void saveCarmenFeatureToDatabase(CarmenFeature carmenFeature) {
    // Check that the carmenFeature hasn't already been added
    if (carmenFeature.properties().has(PlaceConstants.SAVED_PLACE)) {
      return;
    }
    SearchHistoryEntity searchHistory = new SearchHistoryEntity(carmenFeature.id(), carmenFeature);
    DataRepository.getInstance(getDatabase()).addSearchHistoryEntity(searchHistory);
  }

  public static class Factory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;
    private final Intent intent;

    public Factory(@NonNull Application application, @NonNull Intent intent) {
      this.application = application;
      this.intent = intent;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@Nullable Class<T> modelClass) {
      //noinspection unchecked
      return (T) new PlaceAutocompleteViewModel(application, intent);
    }
  }
}
