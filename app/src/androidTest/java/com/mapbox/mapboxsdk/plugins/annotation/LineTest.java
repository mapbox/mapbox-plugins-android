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
 * Basic smoke tests for Line
 */
@RunWith(AndroidJUnit4.class)
public class LineTest extends BaseActivityTest {

  private Line line;

  @Override
  protected Class getActivityClass() {
    return BuildingActivity.class;
  }

  private void setupAnnotation() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      LineManager lineManager = new LineManager(((BuildingActivity) rule.getActivity()).getMapView(), mapboxMap);
      List<LatLng>latLngs = new ArrayList<>();
      latLngs.add(new LatLng());
      latLngs.add(new LatLng(1,1));
      line = lineManager.create(new LineOptions().withLatLngs(latLngs));
    });
  }

  @Test
  public void testLineJoin() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-join");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineJoin(LINE_JOIN_BEVEL);
      assertEquals((String) line.getLineJoin(), (String) LINE_JOIN_BEVEL);
    });
  }

  @Test
  public void testLineOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineOpacity(0.3f);
      assertEquals((Float) line.getLineOpacity(), (Float) 0.3f);
    });
  }

  @Test
  public void testLineColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(line.getLineColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }

  @Test
  public void testLineWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineWidth(0.3f);
      assertEquals((Float) line.getLineWidth(), (Float) 0.3f);
    });
  }

  @Test
  public void testLineGapWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-gap-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineGapWidth(0.3f);
      assertEquals((Float) line.getLineGapWidth(), (Float) 0.3f);
    });
  }

  @Test
  public void testLineOffset() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-offset");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineOffset(0.3f);
      assertEquals((Float) line.getLineOffset(), (Float) 0.3f);
    });
  }

  @Test
  public void testLineBlur() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLineBlur(0.3f);
      assertEquals((Float) line.getLineBlur(), (Float) 0.3f);
    });
  }

  @Test
  public void testLinePattern() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("line-pattern");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(line);

      line.setLinePattern("pedestrian-polygon");
      assertEquals((String) line.getLinePattern(), (String) "pedestrian-polygon");
    });
  }
}
