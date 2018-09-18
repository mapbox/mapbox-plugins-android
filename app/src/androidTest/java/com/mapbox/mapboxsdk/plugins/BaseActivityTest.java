package com.mapbox.mapboxsdk.plugins;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResourceTimeoutException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction;
import com.mapbox.mapboxsdk.plugins.annotation.WaitAction;
import com.mapbox.mapboxsdk.plugins.utils.OnMapReadyIdlingResource;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public abstract class BaseActivityTest {

  @Rule
  public ActivityTestRule<Activity> rule = new ActivityTestRule<>(getActivityClass());

  @Rule
  public TestName testName = new TestName();

  protected MapboxMap mapboxMap;
  protected OnMapReadyIdlingResource idlingResource;

  @Before
  public void beforeTest() {
    try {
      Timber.e("@Before %s: register idle resource", testName.getMethodName());
      idlingResource = new OnMapReadyIdlingResource(rule.getActivity());
      IdlingRegistry.getInstance().register(idlingResource);
      checkViewIsDisplayed(android.R.id.content);
      mapboxMap = idlingResource.getMapboxMap();
    } catch (IdlingResourceTimeoutException idlingResourceTimeoutException) {
      Timber.e("Idling resource timed out. Couldn't not validate if map is ready.");
      throw new RuntimeException("Could not start test for " + getActivityClass().getSimpleName() + ".\n"
        + "The ViewHierarchy doesn't contain a view with resource id = android.R.id.content or \n"
        + "the Activity doesn't contain an instance variable with a name equal to mapboxMap.\n"
        + "You can resolve this issue by adding the requirements above or\n add "
        + getActivityClass().getSimpleName() + " to the platform/android/scripts/exclude-activity-gen.json to blacklist"
        + " the Activity from being generated.\n");
    }
  }

  protected void validateTestSetup() {
    Assert.assertTrue("Device is not connected to the Internet.", isConnected(rule.getActivity()));
    checkViewIsDisplayed(android.R.id.content);
    Assert.assertNotNull(mapboxMap);
  }

  protected MapboxMap getMapboxMap() {
    return mapboxMap;
  }

  protected abstract Class getActivityClass();

  protected void checkViewIsDisplayed(int id) {
    onView(withId(id)).check(matches(isDisplayed()));
  }

  protected void waitAction() {
    waitAction(500);
  }

  protected void waitAction(long waitTime) {
    onView(withId(android.R.id.content)).perform(new WaitAction(waitTime));
  }

  static boolean isConnected(Context context) {
    ConnectivityManager connectivityManager
      = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  protected ViewInteraction onMapView() {
    return onView(withId(android.R.id.content));
  }

  protected MapboxMapAction getMapboxMapAction(MapboxMapAction.OnInvokeActionListener onInvokeActionListener) {
    return new MapboxMapAction(onInvokeActionListener, mapboxMap);
  }

  @After
  public void afterTest() {
    Timber.e("@After test: unregister idle resource");
    IdlingRegistry.getInstance().unregister(idlingResource);
  }
}

