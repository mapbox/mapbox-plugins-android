package com.mapbox.mapboxsdk.plugins.places.picker.viewmodel;

import android.app.Application;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PlacePickerViewModel extends AndroidViewModel implements Callback<GeocodingResponse> {

  private MutableLiveData<CarmenFeature> results = new MutableLiveData<>();

  public PlacePickerViewModel(@NonNull Application application) {
    super(application);
  }

  public void reverseGeocode(Point point, String accessToken, PlacePickerOptions options) {
    MapboxGeocoding.Builder builder = MapboxGeocoding.builder();
    builder.accessToken(accessToken).query(point);
    if (options != null && options.geocodingTypes() != null) {
      builder.geocodingTypes(options.geocodingTypes());
    }
    if (options != null && options.language() != null) {
      builder.languages(options.language());
    }

    builder.build().enqueueCall(this);
  }

  @Override
  public void onResponse(@NonNull Call<GeocodingResponse> call,
                         @NonNull Response<GeocodingResponse> response) {
    if (response.body().features().isEmpty()) {
      results.setValue(null);
      return;
    }
    results.setValue(response.body().features().get(0));
  }

  @Override
  public void onFailure(@NonNull Call<GeocodingResponse> call, Throwable throwable) {
    Timber.e(throwable, "error requesting Geocoding request");
  }

  public MutableLiveData<CarmenFeature> getResults() {
    return results;
  }
}

