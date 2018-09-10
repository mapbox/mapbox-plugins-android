package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.test.runner.AndroidJUnit4;
import com.mapbox.mapboxsdk.plugins.testapp.activity.BuildingActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static com.mapbox.mapboxsdk.style.layers.Property.FILL_TRANSLATE_ANCHOR_MAP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Basic smoke tests for FillManager
 */
@RunWith(AndroidJUnit4.class)
public class FillManagerTest extends BaseActivityTest {

  private FillManager fillManager;

  @Override
  protected Class getActivityClass() {
    return BuildingActivity.class;
  }

  private void setupFillManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      fillManager = new FillManager(mapboxMap);
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
