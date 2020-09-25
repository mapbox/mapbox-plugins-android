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
 * Basic smoke tests for LineManager
 */
@RunWith(AndroidJUnit4.class)
public class LineManagerTest extends BaseActivityTest {

  private LineManager lineManager;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupLineManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      lineManager = new LineManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
    });
  }

  @Test
  public void testLineCapAsConstant() {
    validateTestSetup();
    setupLineManager();
    Timber.i("line-cap");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(lineManager);

      lineManager.setLineCap(LINE_CAP_BUTT);
      assertEquals((String) lineManager.getLineCap(), (String) LINE_CAP_BUTT);
    });
  }

  @Test
  public void testLineMiterLimitAsConstant() {
    validateTestSetup();
    setupLineManager();
    Timber.i("line-miter-limit");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(lineManager);

      lineManager.setLineMiterLimit(2.0f);
      assertEquals((Float) lineManager.getLineMiterLimit(), (Float) 2.0f);
    });
  }

  @Test
  public void testLineRoundLimitAsConstant() {
    validateTestSetup();
    setupLineManager();
    Timber.i("line-round-limit");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(lineManager);

      lineManager.setLineRoundLimit(2.0f);
      assertEquals((Float) lineManager.getLineRoundLimit(), (Float) 2.0f);
    });
  }

  @Test
  public void testLineTranslateAsConstant() {
    validateTestSetup();
    setupLineManager();
    Timber.i("line-translate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(lineManager);

      lineManager.setLineTranslate(new Float[] {0f, 0f});
      assertEquals((Float[]) lineManager.getLineTranslate(), (Float[]) new Float[] {0f, 0f});
    });
  }

  @Test
  public void testLineTranslateAnchorAsConstant() {
    validateTestSetup();
    setupLineManager();
    Timber.i("line-translate-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(lineManager);

      lineManager.setLineTranslateAnchor(LINE_TRANSLATE_ANCHOR_MAP);
      assertEquals((String) lineManager.getLineTranslateAnchor(), (String) LINE_TRANSLATE_ANCHOR_MAP);
    });
  }

  @Test
  public void testLineDasharrayAsConstant() {
    validateTestSetup();
    setupLineManager();
    Timber.i("line-dasharray");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(lineManager);

      lineManager.setLineDasharray(new Float[] {});
      assertEquals((Float[]) lineManager.getLineDasharray(), (Float[]) new Float[] {});
    });
  }
}
