package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;


import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.maboxsdk.plugins.SingleFragmentActivity;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PlaceAutocompleteFragmentTest {

  private static final String ACCESS_TOKEN = "pk.XXX";

  private PlaceAutocompleteFragment placeAutocompleteFragment;

  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityRule = new ActivityTestRule<>(
    SingleFragmentActivity.class, true, true);

  @Before
  public void init() {
    placeAutocompleteFragment = PlaceAutocompleteFragment.newInstance(
      ACCESS_TOKEN, PlaceOptions.builder().build(PlaceOptions.MODE_CARDS)
    );
    activityRule.getActivity().setFragment(placeAutocompleteFragment);
  }

  @Test
  public void newInstance_doesPutAccessTokenInBundle() throws Exception {
    assertThat(placeAutocompleteFragment.getAccessToken(), equalTo(ACCESS_TOKEN));
  }

  @Test
  public void onCreateView_doesInflateCorrectModeView() throws Exception {
    onView(withId(R.id.cardView)).check(matches(isDisplayed()));
  }

  @Test
  public void onScrollChanged_showsDropShadowWhyVerticalNotZero() throws Exception {
    onView(withId(R.id.scroll_view_results)).perform(ViewActions.scrollTo());
    onView(withId(R.id.scroll_drop_shadow)).check(matches(isDisplayed()));
  }
}
