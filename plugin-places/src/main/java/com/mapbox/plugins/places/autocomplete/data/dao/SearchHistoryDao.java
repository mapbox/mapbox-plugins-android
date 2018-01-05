package com.mapbox.plugins.places.autocomplete.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

import java.util.List;

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
