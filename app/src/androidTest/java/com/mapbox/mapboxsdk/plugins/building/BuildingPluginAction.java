package com.mapbox.mapboxsdk.plugins.building;

import android.content.Context;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

class BuildingPluginAction implements ViewAction {

  private MapboxMap mapboxMap;
  private BuildingPlugin buildingPlugin;
  private OnPerformBuildingPluginAction onPerformBuildingPluginAction;

  BuildingPluginAction(MapboxMap mapboxMap, BuildingPlugin buildingPlugin,
                       OnPerformBuildingPluginAction onPerformBuildingPluginAction) {
    this.buildingPlugin = buildingPlugin;
    this.mapboxMap = mapboxMap;
    this.onPerformBuildingPluginAction = onPerformBuildingPluginAction;
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
    if (onPerformBuildingPluginAction != null) {
      onPerformBuildingPluginAction.onBuildingPluginAction(buildingPlugin, mapboxMap,
        uiController, view.getContext());
    }
  }

  interface OnPerformBuildingPluginAction {
    void onBuildingPluginAction(BuildingPlugin locationLayerPlugin, MapboxMap mapboxMap,
                               UiController uiController, Context context);
  }
}