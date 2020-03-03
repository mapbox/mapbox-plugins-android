package com.mapbox.mapboxsdk.plugins.building;


import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.activity.building.BuildingActivity;
import com.mapbox.mapboxsdk.plugins.utils.OnMapReadyIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResourceTimeoutException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@Ignore public class BuildingPluginTest {

  @Rule
  public ActivityTestRule<BuildingActivity> rule = new ActivityTestRule<>(BuildingActivity.class);

  private OnMapReadyIdlingResource idlingResource;
  private BuildingPlugin buildingPlugin;
  private MapboxMap mapboxMap;

  @Before
  public void beforeTest() {
    try {
      Timber.e("@Before: register idle resource");
      idlingResource = new OnMapReadyIdlingResource(rule.getActivity());
      IdlingRegistry.getInstance().register(idlingResource);
      onView(withId(android.R.id.content)).check(matches(isDisplayed()));
      mapboxMap = idlingResource.getMapboxMap();
      buildingPlugin = rule.getActivity().getBuildingPlugin();
    } catch (IdlingResourceTimeoutException idlingResourceTimeoutException) {
      Timber.e("Idling resource timed out. Couldn't not validate if map is ready.");
      throw new RuntimeException("Could not start executeLocationLayerTest for "
        + this.getClass().getSimpleName() + ".\n The ViewHierarchy doesn't contain a view with resource id ="
        + "R.id.mapView or \n the Activity doesn't contain an instance variable with a name equal to mapboxMap.\n");
    }
  }

  @Test
  public void sanity() {
    assertTrue(mapboxMap != null);
    assertTrue(buildingPlugin != null);
  }

  @Test
  public void isVisible_returnsCorrectValueWhenVisibilityToggled() {
    executeBuildingTest((locationLayerPlugin, mapboxMap, uiController, context) -> {
      // Shouldn't be visible until we toggle the fab inside the activity
      boolean visible = buildingPlugin.isVisible();
      assertFalse(visible);

      // Now toggle the building layer visible and check that isVisible returns true
      onView(withId(R.id.fabBuilding)).perform(click());
      visible = buildingPlugin.isVisible();
      assertTrue(visible);
    });
  }

  @After
  public void afterTest() {
    Timber.e("@After: unregister idle resource");
    IdlingRegistry.getInstance().unregister(idlingResource);
  }

  public void executeBuildingTest(BuildingPluginAction.OnPerformBuildingPluginAction listener) {
    onView(withId(android.R.id.content)).perform(new BuildingPluginAction(mapboxMap, buildingPlugin,
      listener));
  }
}
