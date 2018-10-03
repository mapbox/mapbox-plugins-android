package com.mapbox.mapboxsdk.plugins.offline;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.activity.offline.OfflineDownloadActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@Ignore public class OfflinePluginTest {

  @Rule
  public ActivityTestRule<OfflineDownloadActivity> rule = new ActivityTestRule<>(OfflineDownloadActivity.class);

  private OfflinePlugin plugin;

  @Before
  public void setUp() {
    plugin = OfflinePlugin.getInstance(rule.getActivity());
  }

  @Test
  public void sanity() {
    assertThat(plugin, notNullValue());
  }

  @Test
  public void getActiveDownloads_doesReflectCorrectListSize() {
    // Ensure our current list is empty
    List<OfflineDownloadOptions> options = plugin.getActiveDownloads();
    assertThat(options.size(), equalTo(0));

    // Initiate the offline download and ensure the active download list has one item
    onView(withId(R.id.fabStartDownload)).perform(click());
    options = plugin.getActiveDownloads();
    assertThat(options.size(), equalTo(1));
  }


}
