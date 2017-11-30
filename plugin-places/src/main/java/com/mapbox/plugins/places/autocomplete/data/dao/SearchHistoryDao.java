package com.mapbox.plugins.places.autocomplete.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

import java.util.List;

@Dao
public interface SearchHistoryDao {

  @Query("SELECT * FROM searchhistory")
  LiveData<List<SearchHistoryEntity>> getAll();

  @Query("SELECT * FROM searchhistory WHERE placeId IN (:placeIds)")
  LiveData<List<SearchHistoryEntity>> loadAllByIds(String[] placeIds);

  @Query("SELECT * FROM searchhistory WHERE placeId IN (:placeId)")
  LiveData<SearchHistoryEntity> findByCarmenFeature(String placeId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<SearchHistoryEntity> searchHistory);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(SearchHistoryEntity searchHistory);

  @Delete
  void delete(SearchHistoryEntity searchHistory);
}