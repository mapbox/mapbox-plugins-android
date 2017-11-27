package com.mapbox.plugins.places.autocomplete;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecentSearchesDao {
  @Query("SELECT * FROM recentsearch")
  List<RecentSearch> getAll();

  @Query("SELECT * FROM recentsearch WHERE placeId IN (:placeIds)")
  List<RecentSearch> loadAllByIds(String[] placeIds);

  @Query("SELECT * FROM recentsearch WHERE placeId IN (:placeId)")
  RecentSearch findByCarmenFeature(String placeId);

  @Insert
  void insertAll(RecentSearch... recentSearch);

  @Delete
  void delete(RecentSearch recentSearch);
}