package com.mapbox.plugins.places.picker.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacePickerViewModel extends AndroidViewModel implements Callback<GeocodingResponse> {

  public MutableLiveData<CarmenFeature> results = new MutableLiveData<>();

  public PlacePickerViewModel(@NonNull Application application) {
    super(application);
  }

  public void reverseGeocode(Point point, String accessToken) {
    MapboxGeocoding query = MapboxGeocoding.builder()
      .accessToken(accessToken)
      .query(point)
      .build();
    query.enqueueCall(this);
  }

  @Override
  public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
    if (response.body().features().isEmpty()) {
      results.setValue(null);
      return;
    }
    results.setValue(response.body().features().get(0));
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable t) {

  }
}

