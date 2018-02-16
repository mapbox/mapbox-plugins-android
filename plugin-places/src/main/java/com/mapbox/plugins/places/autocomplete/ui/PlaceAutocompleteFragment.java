package com.mapbox.plugins.places.autocomplete.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.DataRepository;
import com.mapbox.plugins.places.common.PlaceConstants;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;
import com.mapbox.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.plugins.places.autocomplete.viewmodel.PlaceAutocompleteViewModel;
import com.mapbox.plugins.places.common.utils.KeyboardUtils;

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
  private String accessToken;
  private View rootView;
  private int mode;

  public static PlaceAutocompleteFragment newInstance(@NonNull String accessToken) {
    PlaceAutocompleteFragment fragment = new PlaceAutocompleteFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PlaceConstants.ACCESS_TOKEN, accessToken);
    fragment.setArguments(bundle);
    return fragment;
  }

  public static PlaceAutocompleteFragment newInstance(@NonNull String accessToken,
                                                      @Nullable PlaceOptions placeOptions) {
    PlaceAutocompleteFragment fragment = new PlaceAutocompleteFragment();
    Bundle bundle = new Bundle();
    bundle.putString(PlaceConstants.ACCESS_TOKEN, accessToken);
    bundle.putParcelable(PlaceConstants.PLACE_OPTIONS, placeOptions);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    accessToken = bundle.getString(PlaceConstants.ACCESS_TOKEN);
    placeOptions = bundle.getParcelable(PlaceConstants.PLACE_OPTIONS);
    if (placeOptions == null) {
      // Increase geocoding limit
      placeOptions = PlaceOptions.builder().limit(10).build();
    }

    mode = placeOptions.viewMode();
    rootView = inflater.inflate(
      mode == PlaceOptions.MODE_CARDS ? R.layout.mapbox_fragment_autocomplete_card
        : R.layout.mapbox_fragment_autocomplete_full,
      container, false);

    bindViews();
    bindClickListeners();
    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    resultScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
    styleView();
  }

  private void styleView() {
    if (placeOptions != null && rootView != null) {
      rootView.setBackgroundColor(placeOptions.backgroundColor());

      View toolbar = rootView.findViewById(R.id.toolbar);
      if (toolbar != null) {
        toolbar.setBackgroundColor(placeOptions.toolbarColor());
      }

      searchView = rootView.findViewById(R.id.searchView);
      searchView.setHint(placeOptions.hint() == null
        ? getString(R.string.mapbox_plugins_autocomplete_search_hint) : placeOptions.hint());
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // View model
    PlaceAutocompleteViewModel.Factory factory = new PlaceAutocompleteViewModel.Factory(
      getActivity().getApplication(), placeOptions
      );
    viewModel = ViewModelProviders.of(this, factory).get(PlaceAutocompleteViewModel.class);
    if (accessToken != null) {
      viewModel.buildGeocodingRequest(accessToken);
    }

    updateFavoritePlacesView();
    subscribe();
  }

  @Override
  public void onScrollChanged() {
    if (resultScrollView != null) {
      if (resultScrollView.getScrollY() != 0) {
        KeyboardUtils.hideKeyboard(resultScrollView);
      }
      if (mode == PlaceOptions.MODE_FULLSCREEN) {
        return;
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
