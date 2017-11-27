package com.mapbox.plugins.places.autocomplete;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.mapbox.geocoding.v5.models.CarmenFeature;

import java.util.ArrayList;
import java.util.List;

class RecentSearchAsyncTask extends AsyncTask<Void, Void, Void> {

  private final SearchHistoryDatabase database;
  private List<CarmenFeature> carmenFeatures = new ArrayList<>();
  private ResultView recentSearchResults;
  private RecentSearch recentSearch;

  RecentSearchAsyncTask(@NonNull SearchHistoryDatabase database) {
    this.database = database;
  }

  RecentSearchAsyncTask(@NonNull SearchHistoryDatabase database,
                        @Nullable ResultView recentSearchResults) {
    this.database = database;
    this.recentSearchResults = recentSearchResults;
  }

  RecentSearchAsyncTask(@NonNull SearchHistoryDatabase database,
                        @Nullable RecentSearch recentSearch) {
    this.database = database;
    this.recentSearch = recentSearch;
  }

  @Override
  protected Void doInBackground(Void... voids) {

    if (recentSearchResults != null) {
      for (RecentSearch recentSearch : database.recentSearchesDao().getAll()) {
        CarmenFeature carmenFeature = CarmenFeature.fromJson(recentSearch.getCarmenFeature());
        carmenFeatures.add(carmenFeature);
      }
    } else if (recentSearch != null) {
      RecentSearch sameRecentSearch = database.recentSearchesDao()
        .findByCarmenFeature(recentSearch.getPlaceId());
      if (sameRecentSearch != null) {
        return null;
      }

      database.recentSearchesDao().insertAll(recentSearch);
    } else {
      for (RecentSearch recentSearch : database.recentSearchesDao().getAll()) {
        database.recentSearchesDao().delete(recentSearch);
      }
    }

    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    if (recentSearchResults != null) {
      recentSearchResults.getResultsList().addAll(carmenFeatures);
      if (!carmenFeatures.isEmpty()) {
        recentSearchResults.setVisibility(View.VISIBLE);
      }
    }
  }
}