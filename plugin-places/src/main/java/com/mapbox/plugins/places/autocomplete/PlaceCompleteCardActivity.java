package com.mapbox.plugins.places.autocomplete;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.views.SearchView;
import com.mapbox.plugins.places.autocomplete.views.ResultView;
import com.mapbox.plugins.places.common.KeyboardUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceCompleteCardActivity extends AppCompatActivity implements
  SearchView.QueryListener, Callback<GeocodingResponse>, SearchView.BackButtonListener,
  ViewTreeObserver.OnScrollChangedListener {

  private MapboxGeocoding.Builder geocoderBuilder;
  private ResultView searchResultView;
  private ResultView starredView;
  private ResultView recentSearchResults;
  private ScrollView resultScrollView;
  private SearchView searchView;
  private CarmenFeature carmenFeature;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete_card);

    Intent intent = getIntent();

    View view = findViewById(R.id.root_layout);
    view.setBackgroundColor(intent.getIntExtra("backgroundColor", Color.TRANSPARENT));

    geocoderBuilder = geocoderBuilder();
    geocoderBuilder.limit(intent.getIntExtra("limit", 5));

    searchResultView = findViewById(R.id.searchResultView);
    resultScrollView = findViewById(R.id.scroll_view_results);
    recentSearchResults = findViewById(R.id.recentSearchResults);
    starredView = findViewById(R.id.starredView);
    searchView = findViewById(R.id.searchView);

    carmenFeature = CarmenFeature.builder().text("Directions to Home")
      .placeName("300 Massachusetts Ave NW")
      .properties(new JsonObject())
      .build();
    starredView.getResultsList().add(carmenFeature);

    carmenFeature = CarmenFeature.builder().text("Directions to Work")
      .placeName("1509 16th St NW")
      .properties(new JsonObject())
      .build();
    starredView.getResultsList().add(carmenFeature);

    for (int i = 0; i < 10; i++) {
      carmenFeature = CarmenFeature.builder().text("My Recent Search")
        .placeName("1234 John St NW")
        .properties(new JsonObject())
        .build();
      recentSearchResults.getResultsList().add(carmenFeature);
    }











    starredView.getResultsList().add(carmenFeature);
    searchView.setBackButtonListener(this);
    searchView.setQueryListener(this);


    resultScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
  }

  @Override
  public void onScrollChanged() {
    if (resultScrollView != null && resultScrollView.getScrollY() != 0) {
      KeyboardUtils.hideKeyboard(resultScrollView);
    }
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
      searchResultView.setVisibility(searchResultView.getResultsList().isEmpty() ? View.GONE : View.VISIBLE);
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
      searchResultView.setVisibility(searchResultView.getResultsList().isEmpty() ? View.GONE : View.VISIBLE);
      searchResultView.notifyDataSetChanged();
    }
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable t) {

  }


  @Override
  protected void onDestroy() {
    if (searchView != null) {
      searchView.removeBackButtonListener();
      searchView.removeQueryListener();
    }
    if (resultScrollView != null) {
      resultScrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
    }
    super.onDestroy();
  }

  @Override
  public void onBackButtonPress() {
    finish();
  }
}