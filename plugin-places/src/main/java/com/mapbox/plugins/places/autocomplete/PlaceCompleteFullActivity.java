package com.mapbox.plugins.places.autocomplete;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceCompleteFullActivity extends AppCompatActivity implements
  SearchView.QueryListener, Callback<GeocodingResponse>, SearchView.BackButtonListener {

  private MapboxGeocoding.Builder geocoderBuilder;
  private ResultView searchResultView;
  private View rootView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete_full);
    bindViews();

    Intent intent = getIntent();

    rootView.setBackgroundColor(intent.getIntExtra(PlaceConstants.BACKGROUND, Color.TRANSPARENT));

    geocoderBuilder = Utils.initiateSearchQuery(intent);
    searchResultView = findViewById(R.id.searchResultView);

    SearchView searchView = findViewById(R.id.searchView);
    searchView.setBackButtonListener(this);
    searchView.setQueryListener(this);
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

  private void bindViews() {
    rootView = findViewById(R.id.root_layout);
    searchResultView = findViewById(R.id.searchResultView);
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
