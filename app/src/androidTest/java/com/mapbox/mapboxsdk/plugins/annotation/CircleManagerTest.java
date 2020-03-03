// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
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
    return TestActivity.class;
  }

  private void setupCircleManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      circleManager = new CircleManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
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
      assertEquals((Float[]) circleManager.getCircleTranslate(), (Float[]) new Float[] {0f, 0f});
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
      assertEquals((String) circleManager.getCircleTranslateAnchor(), (String) CIRCLE_TRANSLATE_ANCHOR_MAP);
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
      assertEquals((String) circleManager.getCirclePitchScale(), (String) CIRCLE_PITCH_SCALE_MAP);
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
      assertEquals((String) circleManager.getCirclePitchAlignment(), (String) CIRCLE_PITCH_ALIGNMENT_MAP);
    });
  }
}
