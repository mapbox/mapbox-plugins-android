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

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;

public class CircleManagerTest {

  private DraggableAnnotationController<Circle, OnCircleDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private CircleLayer circleLayer = mock(CircleLayer.class);
  private CircleManager circleManager;

  @Before
  public void beforeTest() {
    circleManager = new CircleManager(mapboxMap, geoJsonSource, circleLayer, null, draggableAnnotationController);
  }

  @Test
  public void testAddCircle() {
    Circle circle = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    assertEquals(circleManager.getAnnotations().get(0), circle);
  }

  @Test
  public void addCircles() {
    List<LatLng> latLngList = new ArrayList<>();
    latLngList.add(new LatLng());
    latLngList.add(new LatLng(1, 1));
    List< CircleOptions> options = new ArrayList<>();
    for (LatLng latLng : latLngList) {
      options.add(new  CircleOptions().withLatLng(latLng));
    }
    List<Circle> circles = circleManager.create(options);
    assertTrue("Returned value size should match", circles.size() == 2);
    assertTrue("Annotations size should match", circleManager.getAnnotations().size() == 2);
  }

  @Test
  public void testDeleteCircle() {
    Circle circle = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    circleManager.delete(circle);
    assertTrue(circleManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometryCircle() {
    Circle circle = circleManager.create(new CircleOptions().withLatLng(new LatLng(12, 34)));
    assertEquals(circle.getGeometry(), Point.fromLngLat(34, 12));
  }

  @Test
  public void testFeatureIdCircle() {
    Circle circleZero = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    Circle circleOne = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    assertEquals(circleZero.getFeature().get(Circle.ID_KEY).getAsLong(), 0);
    assertEquals(circleOne.getFeature().get(Circle.ID_KEY).getAsLong(), 1);
  }

  @Test
  public void testCircleDraggableFlag() {
    Circle circleZero = circleManager.create(new CircleOptions().withLatLng(new LatLng()));

    assertFalse(circleZero.isDraggable());
    circleZero.setDraggable(true);
    assertTrue(circleZero.isDraggable());
    circleZero.setDraggable(false);
    assertFalse(circleZero.isDraggable());
  }
}