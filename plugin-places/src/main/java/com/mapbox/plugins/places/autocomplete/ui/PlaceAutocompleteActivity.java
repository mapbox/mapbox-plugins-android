package com.mapbox.plugins.places.autocomplete.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.DataRepository;
import com.mapbox.plugins.places.autocomplete.PlaceConstants;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;
import com.mapbox.plugins.places.autocomplete.viewmodel.PlaceAutocompleteViewModel;
import com.mapbox.plugins.places.common.KeyboardUtils;

import java.util.List;

public class PlaceAutocompleteActivity extends AppCompatActivity implements ResultClickCallback,
  SearchView.QueryListener, SearchView.BackButtonListener,
  ViewTreeObserver.OnScrollChangedListener {

  private PlaceAutocompleteViewModel viewModel;
  private ResultView searchHistoryView;
  private ResultView searchResultView;
  private ScrollView resultScrollView;
  private ResultView starredView;
  private SearchView searchView;
  private View dropShadowView;
  private View rootView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    setActivityContentView(intent);
    bindViews();
    bindClickListeners();

    // View model
    PlaceAutocompleteViewModel.Factory factory = new PlaceAutocompleteViewModel.Factory(
      getApplication(), intent);
    viewModel = ViewModelProviders.of(this, factory).get(PlaceAutocompleteViewModel.class);
    viewModel.buildGeocodingRequest();

    // Theme settings
    rootView.setBackgroundColor(intent.getIntExtra(PlaceConstants.BACKGROUND, Color.TRANSPARENT));
    resultScrollView.getViewTreeObserver().addOnScrollChangedListener(this);

    updateFavoritePlacesView();
    subscribe();
  }

  @Override
  public void onScrollChanged() {
    if (resultScrollView != null) {
      if (resultScrollView.getScrollY() != 0) {
        KeyboardUtils.hideKeyboard(resultScrollView);
      }
      dropShadowView.setVisibility(
        resultScrollView.canScrollVertically(-1) ? View.VISIBLE : View.INVISIBLE
      );
    }
  }

  @Override
  public void onQueryChange(CharSequence charSequence) {
    viewModel.onQueryChange(charSequence);
    if (charSequence.length() <= 0) {
      searchResultView.getResultsList().clear();
      searchResultView.setVisibility(
        searchResultView.getResultsList().isEmpty() ? View.GONE : View.VISIBLE
      );
      searchResultView.notifyDataSetChanged();
    }
  }

  @Override
  public void onClick(CarmenFeature carmenFeature) {
    Intent intent = viewModel.onItemClicked(carmenFeature);
    setResult(AppCompatActivity.RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onDestroy() {
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

  private void setActivityContentView(Intent intent) {
    if (intent.getIntExtra(PlaceConstants.MODE, 1) == 2) {
      setContentView(R.layout.activity_complete_card);
    } else {
      setContentView(R.layout.activity_complete_full);
    }
  }

  private void bindClickListeners() {
    searchHistoryView.setOnItemClickListener(this);
    searchResultView.setOnItemClickListener(this);
    starredView.setOnItemClickListener(this);
    searchView.setBackButtonListener(this);
    searchView.setQueryListener(this);
  }

  private void bindViews() {
    searchHistoryView = findViewById(R.id.searchHistoryResultsView);
    resultScrollView = findViewById(R.id.scroll_view_results);
    searchResultView = findViewById(R.id.searchResultView);
    dropShadowView = findViewById(R.id.scroll_drop_shadow);
    starredView = findViewById(R.id.favoriteResultView);
    searchView = findViewById(R.id.searchView);
    rootView = findViewById(R.id.root_layout);
  }

  private void updateSearchHistoryView(@Nullable List<SearchHistoryEntity> searchHistoryEntities) {
    searchHistoryView.getResultsList().clear();
    if (searchHistoryEntities != null) {
      for (SearchHistoryEntity entity : searchHistoryEntities) {
        searchHistoryView.getResultsList().add(entity.getCarmenFeature());
      }
    }
    searchHistoryView.notifyDataSetChanged();
    searchHistoryView.setVisibility(
      searchHistoryView.getResultsList().isEmpty() ? View.GONE : View.VISIBLE
    );
  }

  private void updateSearchResultView(@Nullable GeocodingResponse response) {
    searchResultView.getResultsList().clear();
    searchResultView.getResultsList().addAll(response.features());
    searchResultView.setVisibility(
      searchResultView.getResultsList().isEmpty() ? View.GONE : View.VISIBLE
    );
    searchResultView.notifyDataSetChanged();
  }

  private void updateFavoritePlacesView() {
    List<CarmenFeature> favoriteFeatures = viewModel.getFavoritePlaces();
    starredView.getResultsList().addAll(favoriteFeatures);
  }

  private void subscribe() {
    viewModel.geocodingLiveData.observe(this, new Observer<GeocodingResponse>() {
      @Override
      public void onChanged(@Nullable GeocodingResponse geocodingResponse) {
        updateSearchResultView(geocodingResponse);
      }
    });

    // Subscribe to the search history database
    DataRepository.getInstance(viewModel.getDatabase()).getSearchHistory().observe(this,
      new Observer<List<SearchHistoryEntity>>() {
        @Override
        public void onChanged(@Nullable List<SearchHistoryEntity> searchHistoryEntities) {
          updateSearchHistoryView(searchHistoryEntities);
        }
      });
  }
}