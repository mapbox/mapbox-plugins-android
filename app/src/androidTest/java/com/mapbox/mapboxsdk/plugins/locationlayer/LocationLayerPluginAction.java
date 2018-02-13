package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

class LocationLayerPluginAction implements ViewAction {

  private MapboxMap mapboxMap;
  private LocationLayerPlugin locationLayerPlugin;
  private onPerformLocationLayerAction onPerformLocationLayerAction;

  LocationLayerPluginAction(MapboxMap mapboxMap, LocationLayerPlugin locationLayerPlugin,
                            onPerformLocationLayerAction onPerformLocationLayerAction) {
    this.locationLayerPlugin = locationLayerPlugin;
    this.mapboxMap = mapboxMap;
    this.onPerformLocationLayerAction = onPerformLocationLayerAction;
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
    if (onPerformLocationLayerAction != null) {
      onPerformLocationLayerAction.onLocationLayerAction(locationLayerPlugin, mapboxMap,
        uiController, view.getContext());
    }
  }

  interface onPerformLocationLayerAction {
    void onLocationLayerAction(LocationLayerPlugin locationLayerPlugin, MapboxMap mapboxMap,
                               UiController uiController, Context context);
  }
}