package com.mapbox.mapboxsdk.plugins.traffic;

import android.view.View;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.hamcrest.Matcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

class TrafficPluginAction implements ViewAction {

  private MapboxMap mapboxMap;
  private TrafficPlugin trafficPlugin;
  private OnPerformTrafficAction onPerformTrafficAction;

  TrafficPluginAction(MapboxMap mapboxMap, TrafficPlugin trafficPlugin, OnPerformTrafficAction onPerformTrafficAction) {
    this.trafficPlugin = trafficPlugin;
    this.mapboxMap = mapboxMap;
    this.onPerformTrafficAction = onPerformTrafficAction;
  }

  @Override
  public Matcher<View> getConstraints() {
    return isDisplayed();
  }

  @Override
  public String getDescription() {
    return getClass().getSimpleName();
  }

  @Override
  public void perform(UiController uiController, View view) {
    if (onPerformTrafficAction != null) {
      onPerformTrafficAction.onTrafficAction(trafficPlugin, mapboxMap, uiController);
    }
  }

  interface OnPerformTrafficAction {
    void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController uiController);
  }
}