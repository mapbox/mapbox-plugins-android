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
 * Basic smoke tests for Fill
 */
@RunWith(AndroidJUnit4.class)
public class FillTest extends BaseActivityTest {

  private Fill fill;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupAnnotation() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      FillManager fillManager = new FillManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
      List<LatLng>innerLatLngs = new ArrayList<>();
      innerLatLngs.add(new LatLng());
      innerLatLngs.add(new LatLng(1,1));
      innerLatLngs.add(new LatLng(-1,-1));
      List<List<LatLng>>latLngs = new ArrayList<>();
      latLngs.add(innerLatLngs);
      fill = fillManager.create(new FillOptions().withLatLngs(latLngs));
    });
  }

  @Test
  public void testFillOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);

      fill.setFillOpacity(2.0f);
      assertEquals((Float) fill.getFillOpacity(), (Float) 2.0f);
    });
  }

  @Test
  public void testFillColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);

      fill.setFillColor("rgba(0, 0, 0, 1)");
      assertEquals(fill.getFillColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testFillColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);
      fill.setFillColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(fill.getFillColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testFillOutlineColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-outline-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);

      fill.setFillOutlineColor("rgba(0, 0, 0, 1)");
      assertEquals(fill.getFillOutlineColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testFillOutlineColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-outline-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);
      fill.setFillOutlineColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(fill.getFillOutlineColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testFillPattern() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-pattern");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);

      fill.setFillPattern("pedestrian-polygon");
      assertEquals((String) fill.getFillPattern(), (String) "pedestrian-polygon");
    });
  }
}
