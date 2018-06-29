package com.mapbox.mapboxsdk.plugins.places.autocomplete.data;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.LiveDataTestUtil;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.dao.PlacesDao;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.entity.PlaceEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.mapbox.mapboxsdk.plugins.places.autocomplete.data.TestData.SEARCH_HISTORY_ENTITY;
import static com.mapbox.mapboxsdk.plugins.places.autocomplete.data.TestData.SEARCH_HISTORY_ENTITY_TWO;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test the implementation of {@link PlacesDao}
 */
@RunWith(AndroidJUnit4.class)
public class PlaceDaoTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private PlacesDatabase database;

  private PlacesDao placeDao;

  @Before
  public void before() {
    // using an in-memory database because the information stored here disappears when the
    // process is killed
    database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
      PlacesDatabase.class)
      // allowing main thread queries, just for testing
      .allowMainThreadQueries()
      .build();

    placeDao = database.placesDao();
  }

  @After
  public void after() {
    database.close();
  }

  @Test
  public void getAll_whenNoEntityInserted() throws InterruptedException {
    List<PlaceEntity> searchHistory = LiveDataTestUtil.getValue(placeDao.getAll());
    assertTrue(searchHistory.isEmpty());
  }

  @Test
  public void getAll_afterInserting() throws InterruptedException {
    placeDao.insert(SEARCH_HISTORY_ENTITY);
    List<PlaceEntity> searchHistory = LiveDataTestUtil.getValue(placeDao.getAll());
    assertThat(searchHistory.size(), is(1));
    assertThat(searchHistory.get(0).getPlaceId(), is(SEARCH_HISTORY_ENTITY.getPlaceId()));
    assertThat(searchHistory.get(0).getCarmenFeature(),
      is(SEARCH_HISTORY_ENTITY.getCarmenFeature()));
  }

  @Test
  public void deleteSearchHistory_doesDeleteAllEntries() throws Exception {
    // Add two entries
    placeDao.insert(SEARCH_HISTORY_ENTITY);
    placeDao.insert(SEARCH_HISTORY_ENTITY_TWO);
    List<PlaceEntity> searchHistory = LiveDataTestUtil.getValue(placeDao.getAll());
    assertThat(searchHistory.size(), is(2));

    // Now delete all entries
    placeDao.deleteAllEntries();
    searchHistory = LiveDataTestUtil.getValue(placeDao.getAll());
    assertThat(searchHistory.size(), is(0));
  }
}
