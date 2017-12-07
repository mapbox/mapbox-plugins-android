package com.mapbox.plugins.places.autocomplete.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.DataRepository;
import com.mapbox.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.plugins.places.autocomplete.PlaceConstants;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;
import com.mapbox.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.plugins.places.autocomplete.viewmodel.PlaceAutocompleteViewModel;
import com.mapbox.plugins.places.common.KeyboardUtils;

import java.util.List;

public class PlaceAutocompleteFragment extends Fragment implements ResultClickCallback,
  SearchView.QueryListener, SearchView.BackButtonListener,
  ViewTreeObserver.OnScrollChangedListener {

  public static final String TAG = "PlaceAutocompleteFragment";

  private PlaceSelectionListener placeSelectionListener;
  private PlaceAutocompleteViewModel viewModel;
  private ResultView searchHistoryView;
  private ResultView searchResultView;
  private ScrollView resultScrollView;
  private PlaceOptions placeOptions;
  private ResultView starredView;
  private SearchView searchView;
  private View dropShadowView;
  private View rootView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    int mode = getActivity().getIntent().getIntExtra(PlaceConstants.MODE, 1);
    rootView = inflater.inflate(
      mode == PlaceAutocomplete.MODE_CARDS ? R.layout.fragment_autocomplete_card
        : R.layout.fragment_autocomplete_full,
      container, false);

    bindViews();
    bindClickListeners();
    return rootView;
  }

  public void setPlaceOptions(@NonNull PlaceOptions placeOptions) {
    this.placeOptions = placeOptions;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    resultScrollView.getViewTreeObserver().addOnScrollChangedListener(this);

    rootView.setBackgroundColor(placeOptions.getBackgroundColor());
  }

  @Override
  public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
    super.onInflate(context, attrs, savedInstanceState);

    TypedArray typedArray = getActivity().obtainStyledAttributes(attrs, R.styleable.PlaceAutocomplete);
    String accessToken = typedArray.getString(R.styleable.PlaceAutocomplete_accessToken);
    getActivity().getIntent().putExtra(PlaceConstants.ACCESS_TOKEN, accessToken);
    typedArray.recycle();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Intent intent = getActivity().getIntent();

    // View model
    PlaceAutocompleteViewModel.Factory factory = new PlaceAutocompleteViewModel.Factory(
      getActivity().getApplication(), intent);
    viewModel = ViewModelProviders.of(this, factory).get(PlaceAutocompleteViewModel.class);
    viewModel.buildGeocodingRequest();

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
    viewModel.saveCarmenFeatureToDatabase(carmenFeature);
    if (placeSelectionListener != null) {
      placeSelectionListener.onPlaceSelected(carmenFeature);
    }
  }

  @Override
  public void onDestroyView() {
    if (resultScrollView != null) {
      resultScrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
    }
    placeSelectionListener = null;
    super.onDestroyView();
  }

  @Override
  public void onBackButtonPress() {
    if (placeSelectionListener != null) {
      placeSelectionListener.onCancel();
    }
  }

  public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
    placeSelectionListener = listener;
  }

  private void bindClickListeners() {
    searchHistoryView.setOnItemClickListener(this);
    searchResultView.setOnItemClickListener(this);
    starredView.setOnItemClickListener(this);
    searchView.setBackButtonListener(this);
    searchView.setQueryListener(this);
  }

  private void bindViews() {
    searchHistoryView = rootView.findViewById(R.id.searchHistoryResultsView);
    resultScrollView = rootView.findViewById(R.id.scroll_view_results);
    searchResultView = rootView.findViewById(R.id.searchResultView);
    dropShadowView = rootView.findViewById(R.id.scroll_drop_shadow);
    starredView = rootView.findViewById(R.id.favoriteResultView);
    searchView = rootView.findViewById(R.id.searchView);
    rootView = rootView.findViewById(R.id.root_layout);
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
