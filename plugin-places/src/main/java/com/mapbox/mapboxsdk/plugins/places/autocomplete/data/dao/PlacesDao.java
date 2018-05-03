package com.mapbox.mapboxsdk.plugins.places.autocomplete.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity.PlaceEntity;

import java.util.List;

/**
 * The Data Access Objects specifically for the search history and favorites database
 *
 * @since 0.1.0
 */
@Dao
public interface PlacesDao {

  @Query("SELECT * FROM places")
  LiveData<List<PlaceEntity>> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(PlaceEntity placeEntity);

  @Query("DELETE FROM places")
  void deleteAllEntries();
}
