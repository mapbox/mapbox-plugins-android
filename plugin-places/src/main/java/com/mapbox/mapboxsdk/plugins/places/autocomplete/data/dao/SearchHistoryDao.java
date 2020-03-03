package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.dao;


import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * The Data Access Objects specifically for the search history database
 *
 * @since 0.1.0
 */
@Dao
public interface SearchHistoryDao {

  @Query("SELECT * FROM searchhistory")
  LiveData<List<SearchHistoryEntity>> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(SearchHistoryEntity searchHistory);

  @Query("DELETE FROM searchhistory")
  void deleteAllEntries();
}
