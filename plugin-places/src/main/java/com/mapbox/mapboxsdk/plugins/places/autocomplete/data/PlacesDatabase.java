package com.mapbox.mapboxsdk.plugins.places.autocomplete.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.converter.CarmenFeatureConverter;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.dao.PlacesDao;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity.PlaceEntity;

@Database(entities = {PlaceEntity.class}, version = 2)
@TypeConverters(CarmenFeatureConverter.class)
public abstract class PlacesDatabase extends RoomDatabase {

  private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
      // Add favorite places column to existing table
      database.execSQL("ALTER TABLE searchhistory "
        + " ADD COLUMN favorite_item INTEGER NOT NULL DEFAULT 0");
      // Create the new table
      database.execSQL(
        "CREATE TABLE places (placeId TEXT NOT NULL,"
          + " carmen_feature TEXT,"
          + " favorite_item INTEGER NOT NULL,"
          + " PRIMARY KEY(placeId))");
      // Copy the data
      database.execSQL(
        "INSERT INTO places (placeId, carmen_feature, favorite_item) "
          + "SELECT placeId, carmen_feature, favorite_item "
          + "FROM searchhistory");
      // Remove the old table
      database.execSQL("DROP TABLE searchhistory");
    }
  };

  private static final String DATABASE_NAME = "com.mapbox.mapboxsdk.plugins.places.database";
  private static PlacesDatabase instance;

  public abstract PlacesDao placesDao();

  private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

  public static PlacesDatabase getInstance(final Context context) {
    if (instance == null) {
      instance = buildDatabase(context.getApplicationContext());
      instance.updateDatabaseCreated(context.getApplicationContext());
    }
    return instance;
  }

  private static PlacesDatabase buildDatabase(final Context appContext) {
    return Room.databaseBuilder(appContext, PlacesDatabase.class, DATABASE_NAME).addCallback(
      new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
          super.onCreate(db);
          PlacesDatabase database = PlacesDatabase.getInstance(appContext);
          database.setDatabaseCreated();
        }
      })
      .addMigrations(MIGRATION_1_2)
      .build();
  }

  /**
   * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
   */
  private void updateDatabaseCreated(final Context context) {
    if (context.getDatabasePath(DATABASE_NAME).exists()) {
      setDatabaseCreated();
    }
  }

  void setDatabaseCreated() {
    isDatabaseCreated.postValue(true);
  }

  public static void insertData(final PlacesDatabase database,
                                final PlaceEntity searchHistory) {
    new DatabaseTask(database, searchHistory).execute();
  }

  public static void deleteAllData(final PlacesDatabase database) {
    new DatabaseTask(database, true).execute();
  }

  public final LiveData<Boolean> getDatabaseCreated() {
    return isDatabaseCreated;
  }

  private static class DatabaseTask extends AsyncTask<Void, Void, Void> {

    private final PlacesDatabase database;
    private PlaceEntity placeEntity;
    private boolean delete;

    DatabaseTask(PlacesDatabase database, boolean delete) {
      this.delete = delete;
      this.database = database;
    }

    DatabaseTask(PlacesDatabase database, PlaceEntity placeEntity) {
      this.placeEntity = placeEntity;
      this.database = database;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      if (delete) {
        database.placesDao().deleteAllEntries();
      } else {
        database.placesDao().insert(placeEntity);
      }
      return null;
    }
  }
}
