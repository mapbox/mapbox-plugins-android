package com.mapbox.mapboxsdk.plugins.places.autocomplete;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.PlacesDatabase;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity.PlaceEntity;

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

  private final PlacesDatabase database;
  MediatorLiveData<List<PlaceEntity>> observablePlaces;

  private DataRepository(final PlacesDatabase database) {
    this.database = database;
    observablePlaces = new MediatorLiveData<>();

    observablePlaces.addSource(database.placesDao().getAll(),
      new Observer<List<PlaceEntity>>() {
        @Override
        public void onChanged(@Nullable List<PlaceEntity> placeEntities) {
          if (database.getDatabaseCreated().getValue() != null) {
            observablePlaces.postValue(placeEntities);
          }
        }
      });
  }

  public static DataRepository getInstance(final PlacesDatabase database) {
    if (instance == null) {
      instance = new DataRepository(database);
    }
    return instance;
  }

  public LiveData<List<PlaceEntity>> getPlaces() {
    return observablePlaces;
  }

  public void addSearchHistoryEntity(PlaceEntity placeEntity) {
    PlacesDatabase.insertData(database, placeEntity);
  }
}
