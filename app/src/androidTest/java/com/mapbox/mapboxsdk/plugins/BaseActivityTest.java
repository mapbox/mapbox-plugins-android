package com.mapbox.mapboxsdk.plugins;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction;
import com.mapbox.mapboxsdk.plugins.annotation.WaitAction;
import com.mapbox.mapboxsdk.plugins.utils.OnMapReadyIdlingResource;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResourceTimeoutException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
      IdlingRegistry.getInstance().register(idlingResource = new OnMapReadyIdlingResource(rule.getActivity()));
      Espresso.onIdle();
      mapboxMap = idlingResource.getMapboxMap();
    } catch (IdlingResourceTimeoutException idlingResourceTimeoutException) {
      throw new RuntimeException(String.format("Could not start %s test for %s.\n  Either the ViewHierarchy doesn't "
          + "contain a view with resource id = R.id.mapView or \n the hosting Activity wasn't in an idle state.",
        testName.getMethodName(),
        getActivityClass().getSimpleName())
      );
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

