package com.mapbox.mapboxsdk.plugins.places.autocomplete.data;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.LiveDataTestUtil;
import com.mapbox.plugins.places.autocomplete.data.SearchHistoryDatabase;
import com.mapbox.plugins.places.autocomplete.data.dao.SearchHistoryDao;
import com.mapbox.plugins.places.autocomplete.data.entity.SearchHistoryEntity;

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
 * Test the implementation of {@link SearchHistoryDao}
 */
@RunWith(AndroidJUnit4.class)
public class SearchHistoryDaoTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private SearchHistoryDatabase database;

  private SearchHistoryDao searchHistoryDao;

  @Before
  public void before() throws Exception {
    // using an in-memory database because the information stored here disappears when the
    // process is killed
    database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
      SearchHistoryDatabase.class)
      // allowing main thread queries, just for testing
      .allowMainThreadQueries()
      .build();

    searchHistoryDao = database.searchHistoryDao();
  }

  @After
  public void after() throws Exception {
    database.close();
  }

  @Test
  public void getAll_whenNoEntityInserted() throws Exception {
    List<SearchHistoryEntity> searchHistory = LiveDataTestUtil.getValue(searchHistoryDao.getAll());
    assertTrue(searchHistory.isEmpty());
  }

  @Test
  public void getAll_afterInserting() throws Exception {
    searchHistoryDao.insert(SEARCH_HISTORY_ENTITY);
    List<SearchHistoryEntity> searchHistory = LiveDataTestUtil.getValue(searchHistoryDao.getAll());
    assertThat(searchHistory.size(), is(1));
    assertThat(searchHistory.get(0).getPlaceId(), is(SEARCH_HISTORY_ENTITY.getPlaceId()));
    assertThat(searchHistory.get(0).getCarmenFeature(),
      is(SEARCH_HISTORY_ENTITY.getCarmenFeature()));
  }

  @Test
  public void deleteSearchHistory_doesDeleteAllEntries() throws Exception {
    // Add two entries
    searchHistoryDao.insert(SEARCH_HISTORY_ENTITY);
    searchHistoryDao.insert(SEARCH_HISTORY_ENTITY_TWO);
    List<SearchHistoryEntity> searchHistory = LiveDataTestUtil.getValue(searchHistoryDao.getAll());
    assertThat(searchHistory.size(), is(2));

    // Now delete all entries
    searchHistoryDao.deleteAllEntries();
    searchHistory = LiveDataTestUtil.getValue(searchHistoryDao.getAll());
    assertThat(searchHistory.size(), is(0));
  }
}
