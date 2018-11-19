// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.testapp.activity.building.BuildingActivity;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static com.mapbox.mapboxsdk.style.expressions.Expression.*;
import static org.junit.Assert.*;
import static com.mapbox.mapboxsdk.style.layers.Property.*;

/**
 * Basic smoke tests for CircleManager
 */
@RunWith(AndroidJUnit4.class)
public class CircleManagerTest extends BaseActivityTest {

  private CircleManager circleManager;

  @Override
  protected Class getActivityClass() {
    return BuildingActivity.class;
  }

  private void setupCircleManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      circleManager = new CircleManager(((BuildingActivity) rule.getActivity()).getMapView(), mapboxMap);
    });
  }

  @Test
  public void testCircleRadiusAsExpression() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-radius");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleRadiusExpression(get("hello"));
      assertEquals(circleManager.getCircleRadiusExpression(), number(get("hello")));
    });
  }

  @Test
  public void testCircleBlurAsExpression() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleBlurExpression(get("hello"));
      assertEquals(circleManager.getCircleBlurExpression(), number(get("hello")));
    });
  }

  @Test
  public void testCircleOpacityAsExpression() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleOpacityExpression(get("hello"));
      assertEquals(circleManager.getCircleOpacityExpression(), number(get("hello")));
    });
  }

  @Test
  public void testCircleTranslateAsConstant() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-translate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleTranslate(new Float[] {0f, 0f});
      assertEquals(circleManager.getCircleTranslate(), (Float[]) new Float[] {0f, 0f});
    });
  }

  @Test
  public void testCircleTranslateAnchorAsConstant() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-translate-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleTranslateAnchor(CIRCLE_TRANSLATE_ANCHOR_MAP);
      assertEquals(circleManager.getCircleTranslateAnchor(), (String) CIRCLE_TRANSLATE_ANCHOR_MAP);
    });
  }

  @Test
  public void testCirclePitchScaleAsConstant() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-pitch-scale");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCirclePitchScale(CIRCLE_PITCH_SCALE_MAP);
      assertEquals(circleManager.getCirclePitchScale(), (String) CIRCLE_PITCH_SCALE_MAP);
    });
  }

  @Test
  public void testCirclePitchAlignmentAsConstant() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-pitch-alignment");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCirclePitchAlignment(CIRCLE_PITCH_ALIGNMENT_MAP);
      assertEquals(circleManager.getCirclePitchAlignment(), (String) CIRCLE_PITCH_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testCircleStrokeWidthAsExpression() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-stroke-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleStrokeWidthExpression(get("hello"));
      assertEquals(circleManager.getCircleStrokeWidthExpression(), number(get("hello")));
    });
  }

  @Test
  public void testCircleStrokeOpacityAsExpression() {
    validateTestSetup();
    setupCircleManager();
    Timber.i("circle-stroke-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(circleManager);

      circleManager.setCircleStrokeOpacityExpression(get("hello"));
      assertEquals(circleManager.getCircleStrokeOpacityExpression(), number(get("hello")));
    });
  }
}
