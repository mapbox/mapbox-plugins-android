package com.mapbox.mapboxsdk.plugins.scalebar;


import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.pluginscalebar.ScaleBar;
import com.mapbox.pluginscalebar.ScaleBarWidget;

import org.junit.Test;
import org.junit.runner.RunWith;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Basic smoke tests for ScaleBar
 */
@RunWith(AndroidJUnit4.class)
public class ScaleBarTest extends BaseActivityTest {
  private ScaleBar scaleBar;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupScaleBar() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      scaleBar = new ScaleBar(idlingResource.getMapView(), mapboxMap);
    });
  }

  @Test
  public void testScaleBarEnable() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(scaleBar);
      ScaleBarWidget scaleBarWidget = scaleBar.getScaleBarWidget();
      assertNotNull(scaleBarWidget);
      assertEquals(View.VISIBLE, scaleBarWidget.getVisibility());
      assertTrue(scaleBar.isEnabled());
      scaleBar.setEnabled(false);
      assertEquals(View.GONE, scaleBarWidget.getVisibility());
      assertFalse(scaleBar.isEnabled());
    });
  }

  @Test
  public void testScaleBarColor() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(scaleBar);
      ScaleBarWidget scaleBarWidget = scaleBar.getScaleBarWidget();
      assertNotNull(scaleBarWidget);
      Activity activity = rule.getActivity();
      assertEquals(activity.getResources().getColor(android.R.color.black), scaleBarWidget.getTextColor());
      assertEquals(activity.getResources().getColor(android.R.color.black), scaleBarWidget.getPrimaryColor());
      assertEquals(activity.getResources().getColor(android.R.color.white), scaleBarWidget.getSecondaryColor());
      scaleBar.setColors(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
      assertEquals(activity.getResources().getColor(R.color.colorAccent), scaleBarWidget.getTextColor());
      assertEquals(activity.getResources().getColor(R.color.colorPrimary), scaleBarWidget.getPrimaryColor());
      assertEquals(activity.getResources().getColor(R.color.colorPrimaryDark), scaleBarWidget.getSecondaryColor());
    });
  }

  @Test
  public void testScaleBarWidth() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(scaleBar);
      ScaleBarWidget scaleBarWidget = scaleBar.getScaleBarWidget();
      assertNotNull(scaleBarWidget);
      assertEquals(MapView.class, scaleBarWidget.getParent().getClass());
      MapView parent = (MapView) scaleBarWidget.getParent();
      assertEquals(parent.getWidth(), scaleBarWidget.getMapViewWidth());
    });
  }

}
