// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

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
    return TestActivity.class;
  }

  private void setupAnnotation() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      CircleManager circleManager = new CircleManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
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

      circle.setCircleRadius(2.0f);
      assertEquals((Float) circle.getCircleRadius(), (Float) 2.0f);
    });
  }

  @Test
  public void testCircleColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleColor("rgba(0, 0, 0, 1)");
      assertEquals(circle.getCircleColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testCircleColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);
      circle.setCircleColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(circle.getCircleColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testCircleBlur() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleBlur(2.0f);
      assertEquals((Float) circle.getCircleBlur(), (Float) 2.0f);
    });
  }

  @Test
  public void testCircleOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleOpacity(2.0f);
      assertEquals((Float) circle.getCircleOpacity(), (Float) 2.0f);
    });
  }

  @Test
  public void testCircleStrokeWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleStrokeWidth(2.0f);
      assertEquals((Float) circle.getCircleStrokeWidth(), (Float) 2.0f);
    });
  }

  @Test
  public void testCircleStrokeColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleStrokeColor("rgba(0, 0, 0, 1)");
      assertEquals(circle.getCircleStrokeColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testCircleStrokeColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);
      circle.setCircleStrokeColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(circle.getCircleStrokeColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testCircleStrokeOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("circle-stroke-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circle);

      circle.setCircleStrokeOpacity(2.0f);
      assertEquals((Float) circle.getCircleStrokeOpacity(), (Float) 2.0f);
    });
  }
}
