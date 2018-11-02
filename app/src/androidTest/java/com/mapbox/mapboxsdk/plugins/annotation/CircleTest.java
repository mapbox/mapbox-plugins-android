// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.activity.building.BuildingActivity;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static org.junit.Assert.*;
import static com.mapbox.mapboxsdk.style.layers.Property.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic smoke tests for Circle
 */
@RunWith(AndroidJUnit4.class)
public class CircleTest extends BaseActivityTest {

  private Circle circle;

  @Override
  protected Class getActivityClass() {
    return BuildingActivity.class;
  }

  private void setupAnnotation() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      CircleManager circleManager = new CircleManager(((BuildingActivity) rule.getActivity()).getMapView(), mapboxMap);
      circle = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    });
  }

  @Test
  public void testCircleRadius() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-radius");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleRadius(0.3f);
      assertEquals((Float) circle.getCircleRadius(), (Float) 0.3f);
    });
  }

  @Test
  public void testCircleColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(circle.getCircleColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }

  @Test
  public void testCircleBlur() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleBlur(0.3f);
      assertEquals((Float) circle.getCircleBlur(), (Float) 0.3f);
    });
  }

  @Test
  public void testCircleOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleOpacity(0.3f);
      assertEquals((Float) circle.getCircleOpacity(), (Float) 0.3f);
    });
  }

  @Test
  public void testCircleStrokeWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleStrokeWidth(0.3f);
      assertEquals((Float) circle.getCircleStrokeWidth(), (Float) 0.3f);
    });
  }

  @Test
  public void testCircleStrokeColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleStrokeColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(circle.getCircleStrokeColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }

  @Test
  public void testCircleStrokeOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleStrokeOpacity(0.3f);
      assertEquals((Float) circle.getCircleStrokeOpacity(), (Float) 0.3f);
    });
  }
}
