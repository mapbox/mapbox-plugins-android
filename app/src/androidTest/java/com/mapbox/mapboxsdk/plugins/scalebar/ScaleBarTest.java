package com.mapbox.mapboxsdk.plugins.scalebar;


import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.pluginscalebar.ScaleBarManager;
import com.mapbox.pluginscalebar.ScaleBarOption;
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
  private ScaleBarManager scaleBarManager;
  private Activity activity;
  private ScaleBarWidget scaleBarWidget;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupScaleBar() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      scaleBarManager = new ScaleBarManager(idlingResource.getMapView(), mapboxMap);
      activity = rule.getActivity();
      scaleBarWidget = scaleBarManager.create(new ScaleBarOption(activity));
      assertNotNull(scaleBarManager);
      assertNotNull(scaleBarWidget);
    });
  }

  @Test
  public void testScaleBarEnable() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(View.VISIBLE, scaleBarWidget.getVisibility());
      assertTrue(scaleBarManager.isEnabled());
      scaleBarManager.setEnabled(false);
      assertEquals(View.GONE, scaleBarWidget.getVisibility());
      assertFalse(scaleBarManager.isEnabled());
    });
  }

  @Test
  public void testScaleBarColor() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(activity.getResources().getColor(android.R.color.black), scaleBarWidget.getTextColor());
      assertEquals(activity.getResources().getColor(android.R.color.black), scaleBarWidget.getPrimaryColor());
      assertEquals(activity.getResources().getColor(android.R.color.white), scaleBarWidget.getSecondaryColor());


      int textColor = activity.getResources().getColor(R.color.colorAccent);
      int colorPrimary = activity.getResources().getColor(R.color.colorPrimary);
      int colorSecondary = activity.getResources().getColor(R.color.colorPrimaryDark);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setTextColor(textColor);
      option.setPrimaryColor(colorPrimary);
      option.setSecondaryColor(colorSecondary);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(textColor, scaleBarWidget.getTextColor());
      assertEquals(colorPrimary, scaleBarWidget.getPrimaryColor());
      assertEquals(colorSecondary, scaleBarWidget.getSecondaryColor());
    });
  }

  @Test
  public void testScaleBarWidth() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(MapView.class, scaleBarWidget.getParent().getClass());
      MapView parent = (MapView) scaleBarWidget.getParent();
      assertEquals(parent.getWidth(), scaleBarWidget.getMapViewWidth());
    });
  }

  @Test
  public void testMargin() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(ScaleBarOption.MARGIN_LEFT_DEFAULT), scaleBarWidget.getMarginLeft(), 0);
      assertEquals(convertDpToPixel(ScaleBarOption.MARGIN_TOP_DEFAULT), scaleBarWidget.getMarginTop(), 0);
      assertEquals(convertDpToPixel(ScaleBarOption.TEXT_BAR_MARGIN_DEFAULT), scaleBarWidget.getTextBarMargin(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setMarginLeft(100);
      option.setMarginTop(50);
      option.setTextBarMargin(30);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(100), scaleBarWidget.getMarginLeft(), 0);
      assertEquals(convertDpToPixel(50), scaleBarWidget.getMarginTop(), 0);
      assertEquals(convertDpToPixel(30), scaleBarWidget.getTextBarMargin(), 0);

    });
  }

  @Test
  public void testBarHeight() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(ScaleBarOption.BAR_HEIGHT_DEFAULT), scaleBarWidget.getBarHeight(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setBarHeight(100);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(100), scaleBarWidget.getBarHeight(), 0);

    });
  }

  @Test
  public void testTextSize() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(ScaleBarOption.TEXT_SIZE_DEFAULT), scaleBarWidget.getTextSize(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setTextSize(100);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(100), scaleBarWidget.getTextSize(), 0);

    });
  }

  @Test
  public void testBorderWidth() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(ScaleBarOption.BORDER_WIDTH_DEFAULT), scaleBarWidget.getBorderWidth(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setBorderWidth(100);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(100), scaleBarWidget.getBorderWidth(), 0);

    });
  }


  @Test
  public void testRefreshDuration() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(ScaleBarOption.REFRESH_DURATION_DEFAULT, scaleBarWidget.getRefreshDuration(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setRefreshDuration(1000);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(1000, scaleBarWidget.getRefreshDuration(), 0);

    });
  }

  @Test
  public void testMetrics() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(ScaleBarOption.LocaleUnitResolver.isMetricSystem(), scaleBarWidget.isMetricUnit());

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setMetricUnit(true);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertTrue(scaleBarWidget.isMetricUnit());

      option = new ScaleBarOption(activity);
      option.setMetricUnit(false);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertFalse(scaleBarWidget.isMetricUnit());
    });
  }


  private float convertDpToPixel(float dp) {
    return dp * ((float) activity.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}
