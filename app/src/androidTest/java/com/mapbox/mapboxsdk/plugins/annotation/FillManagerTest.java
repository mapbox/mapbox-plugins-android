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
 * Basic smoke tests for FillManager
 */
@RunWith(AndroidJUnit4.class)
public class FillManagerTest extends BaseActivityTest {

  private FillManager fillManager;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupFillManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      fillManager = new FillManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
    });
  }

  @Test
  public void testFillAntialiasAsConstant() {
    validateTestSetup();
    setupFillManager();
    Timber.i("fill-antialias");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fillManager);

      fillManager.setFillAntialias(true);
      assertEquals((Boolean) fillManager.getFillAntialias(), (Boolean) true);
    });
  }

  @Test
  public void testFillTranslateAsConstant() {
    validateTestSetup();
    setupFillManager();
    Timber.i("fill-translate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fillManager);

      fillManager.setFillTranslate(new Float[] {0f, 0f});
      assertEquals((Float[]) fillManager.getFillTranslate(), (Float[]) new Float[] {0f, 0f});
    });
  }

  @Test
  public void testFillTranslateAnchorAsConstant() {
    validateTestSetup();
    setupFillManager();
    Timber.i("fill-translate-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fillManager);

      fillManager.setFillTranslateAnchor(FILL_TRANSLATE_ANCHOR_MAP);
      assertEquals((String) fillManager.getFillTranslateAnchor(), (String) FILL_TRANSLATE_ANCHOR_MAP);
    });
  }
}
