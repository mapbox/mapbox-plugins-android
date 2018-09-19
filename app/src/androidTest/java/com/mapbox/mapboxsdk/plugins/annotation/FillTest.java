// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.activity.building.BuildingActivity;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

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
    return BuildingActivity.class;
  }

  private void setupAnnotation() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      FillManager fillManager = new FillManager(mapboxMap);
      List<LatLng>innerLatLngs = new ArrayList<>();
      innerLatLngs.add(new LatLng());
      innerLatLngs.add(new LatLng(1,1));
      innerLatLngs.add(new LatLng(-1,-1));
      List<List<LatLng>>latLngs = new ArrayList<>();
      latLngs.add(innerLatLngs);
      fill = fillManager.createFill(latLngs);
    });
  }

  @Test
  public void testFillOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("fill-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(fill);

      fill.setFillOpacity(0.3f);
      assertEquals((Float) fill.getFillOpacity(), (Float) 0.3f);
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
      assertEquals((String) fill.getFillColor(), (String) "rgba(0, 0, 0, 1)");
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
      assertEquals((String) fill.getFillOutlineColor(), (String) "rgba(0, 0, 0, 1)");
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
