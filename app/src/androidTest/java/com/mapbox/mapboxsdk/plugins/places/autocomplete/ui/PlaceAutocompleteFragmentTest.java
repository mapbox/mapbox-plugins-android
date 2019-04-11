package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.data.TestData;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.activity.SingleFragmentActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@Ignore public class PlaceAutocompleteFragmentTest {

  private static final String ACCESS_TOKEN = "pk.XXX";
  private static final Integer HISTORY_COUNT = 5;

  private PlaceAutocompleteFragment placeAutocompleteFragment;

  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityRule = new ActivityTestRule<>(
    SingleFragmentActivity.class, true, true);

  @Before
  public void init() {
    PlaceOptions.Builder builder = PlaceOptions.builder();
    // Inject a bunch of data to ensure result scroll view is greater than the device screen height
    for (int i = 0; i < 50; i++) {
      builder.addInjectedFeature(TestData.CARMEN_FEATURE);
    }

    // For style test
    builder.backgroundColor(Color.BLUE);
    builder.historyCount(HISTORY_COUNT);
    builder.hint("foobar");

    placeAutocompleteFragment = PlaceAutocompleteFragment.newInstance(
      ACCESS_TOKEN, builder.build(PlaceOptions.MODE_CARDS)
    );
    activityRule.getActivity().setFragment(placeAutocompleteFragment);
  }

  // TODO
  @Test
  @Ignore
  public void newInstance_doesPutAccessTokenInBundle() throws Exception {
    assertThat(placeAutocompleteFragment.getAccessToken(), equalTo(ACCESS_TOKEN));
  }

  @Test
  public void styleView_setHistoryCountIsCorrect() throws Exception {
    assertThat(placeAutocompleteFragment.getHistoryCount(), equalTo(HISTORY_COUNT));
  }
  
  @Test
  public void onCreateView_doesInflateCorrectModeView() throws Exception {
    onView(withId(R.id.cardView)).check(matches(isDisplayed()));
  }

  @Test
  public void onScrollChanged_hidesDropShadowWhenVerticalZero() throws Exception {
    onView(withId(R.id.scroll_drop_shadow)).check(matches(not(isDisplayed())));
  }

  @Test
  public void onScrollChanged_showsDropShadowWhenVerticalNotZero() throws Exception {
    onView(withId(R.id.scroll_view_results)).perform(ViewActions.swipeUp());
    onView(withId(R.id.scroll_drop_shadow)).check(matches(isDisplayed()));
  }

  @Test
  public void onBackButtonPress_doesInvokeOnCancelCallback() throws Exception {
    PlaceSelectionListener listener = mock(PlaceSelectionListener.class);
    placeAutocompleteFragment.setOnPlaceSelectedListener(listener);
    placeAutocompleteFragment.onBackButtonPress();
    verify(listener, times(1)).onCancel();
  }

  //
  // Offline state test
  //

  // Note, device should also not have data enabled for test to pass. Cannot currently find a way to
  // disable this however.
  @Test
  public void offline_doesShowCorrectViewWhenDeviceOffline() throws Exception {
    WifiManager wifi = (WifiManager) activityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
    wifi.setWifiEnabled(false);

    onView(withId(R.id.edittext_search)).perform(typeText("W"));
    onView(withId(R.id.offlineResultView)).check(matches(isDisplayed()));
  }

  @Test
  public void offline_doesShowCorrectViewWhenDeviceGoesBackOnline() throws Exception {
    WifiManager wifi = (WifiManager) activityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
    wifi.setWifiEnabled(true);

    onView(withId(R.id.edittext_search)).perform(typeText("W"));
    onView(withId(R.id.offlineResultView)).check(matches(not((isDisplayed()))));
  }

  // TODO fix
  @Test
  @Ignore
  public void styleView_doesSetBackgroundColorCorrectly() throws Exception {
    onView(withId(R.id.root_layout)).check(matches(hasBackground(Color.BLUE)));
  }

  @Test
  public void styleView_doesSetToolbarColorCorrectly() throws Exception {
    // TODO
  }

  @Test
  public void styleView_doesChangeEditTextHintToOption() throws Exception {
    onView(withId(R.id.edittext_search)).check(matches(withHint("foobar")));
  }
}
