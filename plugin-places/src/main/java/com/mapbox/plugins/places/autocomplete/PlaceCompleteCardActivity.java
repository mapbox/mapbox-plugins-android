package com.mapbox.plugins.places.autocomplete;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.common.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class PlaceCompleteCardActivity extends AppCompatActivity implements
  SearchView.QueryListener, Callback<GeocodingResponse>, SearchView.BackButtonListener,
  ViewTreeObserver.OnScrollChangedListener, OnCardItemClickListener {

  private MapboxGeocoding.Builder geocoderBuilder;
  private ResultView searchResultView;
  private ResultView starredView;
  private ResultView recentSearchResults;
  private ScrollView resultScrollView;
  private SearchView searchView;
  private SearchHistoryDatabase database;
  private View rootView;
  private View dropShadow;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complete_card);
    bindViews();
    bindClickListeners();

    Intent intent = getIntent();

    // Theme settings
    rootView.setBackgroundColor(intent.getIntExtra(PlaceConstants.BACKGROUND, Color.TRANSPARENT));

    fillFavoritePlacesList(intent);
    geocoderBuilder = Utils.initiateSearchQuery(intent);

    // Get and populate the recent history list
    database = Room.databaseBuilder(getApplicationContext(),
      SearchHistoryDatabase.class, PlaceConstants.SEARCH_HISTORY_DATABASE_NAME).build();
    new RecentSearchAsyncTask(database, recentSearchResults).execute();

    resultScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
  }

  private void fillFavoritePlacesList(@NonNull Intent intent) {
    List<String> serialized = intent.getStringArrayListExtra(PlaceConstants.INJECTED_PLACES);
    if (serialized == null || serialized.isEmpty()) {
      return;
    }
    List<CarmenFeature> starredFeatures = new ArrayList<>();
    for (String serializedCarmenFeature : serialized) {
      starredFeatures.add(CarmenFeature.fromJson(serializedCarmenFeature));
    }
    starredView.getResultsList().addAll(starredFeatures);
  }

  private void bindClickListeners() {
    recentSearchResults.setOnItemClickListener(this);
    searchResultView.setOnItemClickListener(this);
    starredView.setOnItemClickListener(this);
    searchView.setBackButtonListener(this);
    searchView.setQueryListener(this);
  }

  private void bindViews() {
    rootView = findViewById(R.id.root_layout);
    searchResultView = findViewById(R.id.searchResultView);
    resultScrollView = findViewById(R.id.scroll_view_results);
    recentSearchResults = findViewById(R.id.recentSearchResults);
    starredView = findViewById(R.id.starredView);
    searchView = findViewById(R.id.searchView);
    dropShadow = findViewById(R.id.scroll_drop_shadow);
  }

  @Override
  public void onScrollChanged() {
    if (resultScrollView != null) {
      if (resultScrollView.getScrollY() != 0) {
        KeyboardUtils.hideKeyboard(resultScrollView);
      }
      dropShadow.setVisibility(
        resultScrollView.canScrollVertically(-1) ? VISIBLE : INVISIBLE
      );
    }
  }

  @Override
  public void onQueryChange(CharSequence charSequence) {
    String query = charSequence.toString();
    if (query.isEmpty()) {
      searchResultView.getResultsList().clear();
      searchResultView.setVisibility(searchResultView.getResultsList().isEmpty() ? View.GONE : VISIBLE);
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
      searchResultView.setVisibility(searchResultView.getResultsList().isEmpty() ? View.GONE : VISIBLE);
      searchResultView.notifyDataSetChanged();
    }
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {

  }

  @Override
  public void onItemClick(CarmenFeature carmenFeature) {
    String json = carmenFeature.toJson();
    if (!carmenFeature.properties().has(PlaceConstants.SAVED_PLACE)) {
      RecentSearch recentSearch = new RecentSearch(carmenFeature.id(), json);
      new RecentSearchAsyncTask(database, recentSearch).execute();
    }

    Intent intent = new Intent();
    intent.putExtra(PlaceConstants.RETURNING_CARMEN_FEATURE, json);
    setResult(AppCompatActivity.RESULT_OK, intent);
    finish();
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
    setResult(AppCompatActivity.RESULT_CANCELED);
    finish();
  }
}