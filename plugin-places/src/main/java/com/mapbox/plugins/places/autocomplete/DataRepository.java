package com.mapbox.plugins.places.autocomplete;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.mapbox.plugins.places.autocomplete.data.SearchHistoryDatabase;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

import java.util.List;

/**
 * Used internally for the autocomplete view
 * <p>
 * Singleton class used for exchanging information between the data classes and the views.
 *
 * @since 0.1.0
 */
public final class DataRepository {

  private static DataRepository instance;

  private final SearchHistoryDatabase database;
  private MediatorLiveData<List<SearchHistoryEntity>> observableSearchHistory;

  private DataRepository(final SearchHistoryDatabase database) {
    this.database = database;
    observableSearchHistory = new MediatorLiveData<>();

    observableSearchHistory.addSource(database.searchHistoryDao().getAll(),
      new Observer<List<SearchHistoryEntity>>() {
        @Override
        public void onChanged(@Nullable List<SearchHistoryEntity> searchHistoryEntities) {
          if (database.getDatabaseCreated().getValue() != null) {
            observableSearchHistory.postValue(searchHistoryEntities);
          }
        }
      });
  }

  public static DataRepository getInstance(final SearchHistoryDatabase database) {
    if (instance == null) {
      instance = new DataRepository(database);
    }
    return instance;
  }

  public LiveData<List<SearchHistoryEntity>> getSearchHistory() {
    return observableSearchHistory;
  }

  public void addSearchHistoryEntity(SearchHistoryEntity searchHistory) {
    SearchHistoryDatabase.insertData(database, searchHistory);
  }
}
