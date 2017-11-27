package com.mapbox.plugins.places.autocomplete;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RecentSearch.class}, version = 1)
public abstract class SearchHistoryDatabase extends RoomDatabase {
  public abstract RecentSearchesDao recentSearchesDao();
}





