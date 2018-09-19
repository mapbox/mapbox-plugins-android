// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.*;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class CircleManagerTest {

  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private CircleLayer circleLayer = mock(CircleLayer.class);
  private CircleManager circleManager;

  @Before
  public void beforeTest() {
    circleManager = new CircleManager(mapboxMap, geoJsonSource, circleLayer, null);
  }

  @Test
  public void testAddCircle() {
    Circle circle = circleManager.createCircle(new LatLng());
    assertEquals(circleManager.getAnnotations().get(0), circle);
  }

  @Test
  public void addCircles() {
    List<LatLng> latLngList = new ArrayList<>();
    latLngList.add(new LatLng());
    latLngList.add(new LatLng(1, 1));
    List<Circle> circles = circleManager.createCircles(latLngList);
    assertTrue("Returned value size should match", circles.size() == 2);
    assertTrue("Annotations size should match", circleManager.getAnnotations().size() == 2);
  }

  @Test
  public void testDeleteCircle() {
    Circle circle = circleManager.createCircle(new LatLng());
    circleManager.delete(circle);
    assertTrue(circleManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometryCircle() {
    Circle circle = circleManager.createCircle(new LatLng(12, 34));
    assertEquals(circle.getGeometry(), Point.fromLngLat(34, 12));
  }

  @Test
  public void testFeatureIdCircle() {
    Circle circleZero = circleManager.createCircle(new LatLng());
    Circle circleOne = circleManager.createCircle(new LatLng());
    assertEquals(circleZero.getFeature().get(Circle.ID_KEY).getAsLong(), 0);
    assertEquals(circleOne.getFeature().get(Circle.ID_KEY).getAsLong(), 1);
  }
}