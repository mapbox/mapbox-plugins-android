package com.mapbox.plugins.places.autocomplete;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.views.ResultView;
import com.mapbox.plugins.places.autocomplete.views.SearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceCompleteFullActivity extends AppCompatActivity implements
  SearchView.QueryListener, Callback<GeocodingResponse>, SearchView.BackButtonListener {

  private MapboxGeocoding.Builder geocoderBuilder;
  private ResultView searchResultView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete_full);

    Intent intent = getIntent();
    geocoderBuilder = geocoderBuilder();
    geocoderBuilder.limit(intent.getIntExtra("limit", 5));
    searchResultView = findViewById(R.id.searchResultView);

    SearchView searchView = findViewById(R.id.searchView);
    searchView.setBackButtonListener(this);
    searchView.setQueryListener(this);
  }

  private MapboxGeocoding.Builder geocoderBuilder() {
    return MapboxGeocoding.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .autocomplete(true);
  }

  @Override
  public void onQueryChange(CharSequence charSequence) {
    String query = charSequence.toString();
    if (query.isEmpty()) {
      searchResultView.getResultsList().clear();
      searchResultView.notifyDataSetChanged();
      return;
    }
    geocoderBuilder.query(query)
      .build().enqueueCall(this);
  }

  @Override
  public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
    if (response.isSuccessful()) {
      searchResultView.getResultsList().clear();
      searchResultView.getResultsList().addAll(response.body().features());
      searchResultView.notifyDataSetChanged();
    }
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable t) {

  }


  @Override
  protected void onDestroy() {
//    searchBar.removeBackButtonListener();
//    searchBar.removeQueryListener();
    super.onDestroy();
  }

  @Override
  public void onBackButtonPress() {
    finish();
  }
}
