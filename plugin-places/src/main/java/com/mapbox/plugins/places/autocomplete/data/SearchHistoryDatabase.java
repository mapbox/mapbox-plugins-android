package com.mapbox.plugins.places.autocomplete.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mapbox.plugins.places.autocomplete.data.converter.CarmenFeatureConverter;
import com.mapbox.plugins.places.autocomplete.data.dao.SearchHistoryDao;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

@Database(entities = {SearchHistoryEntity.class}, version = 2)
@TypeConverters(CarmenFeatureConverter.class)
public abstract class SearchHistoryDatabase extends RoomDatabase {

  private static final String DATABASE_NAME = "com.mapbox.mapboxsdk.plugins.places.database";
  private static SearchHistoryDatabase instance;

  public abstract SearchHistoryDao searchHistoryDao();

  private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

  public static SearchHistoryDatabase getInstance(final Context context) {
    if (instance == null) {
      instance = buildDatabase(context.getApplicationContext());
      instance.updateDatabaseCreated(context.getApplicationContext());
    }
    return instance;
  }

  private static SearchHistoryDatabase buildDatabase(final Context appContext) {
    return Room.databaseBuilder(appContext,
      SearchHistoryDatabase.class, DATABASE_NAME).addCallback(new Callback() {
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        SearchHistoryDatabase database = SearchHistoryDatabase.getInstance(appContext);
        database.setDatabaseCreated();
      }
    }).fallbackToDestructiveMigration().build();
    // TODO remove fallbackToDestructiveMigration
  }

  /**
   * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
   */
  private void updateDatabaseCreated(final Context context) {
    if (context.getDatabasePath(DATABASE_NAME).exists()) {
      setDatabaseCreated();
    }
  }

  private void setDatabaseCreated() {
    isDatabaseCreated.postValue(true);
  }

  public static void insertData(final SearchHistoryDatabase database,
                                final SearchHistoryEntity searchHistory) {
    new InsertSearchEntity(database, searchHistory).execute();
  }

  public LiveData<Boolean> getDatabaseCreated() {
    return isDatabaseCreated;
  }


  private static class InsertSearchEntity extends AsyncTask<Void, Void, Void> {

    private final SearchHistoryDatabase database;
    private final SearchHistoryEntity searchHistory;

    InsertSearchEntity(SearchHistoryDatabase database, SearchHistoryEntity searchHistory) {
      this.searchHistory = searchHistory;
      this.database = database;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      database.searchHistoryDao().insert(searchHistory);
      return null;
    }
  }
}
