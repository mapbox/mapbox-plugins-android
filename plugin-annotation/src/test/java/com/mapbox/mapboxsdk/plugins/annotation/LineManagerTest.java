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

public class LineManagerTest {

  private DraggableAnnotationController<Line, OnLineDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private LineLayer lineLayer = mock(LineLayer.class);
  private LineManager lineManager;

  @Before
  public void beforeTest() {
    lineManager = new LineManager(mapboxMap, geoJsonSource, lineLayer, null, draggableAnnotationController);
  }

  @Test
  public void testAddLine() {
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line line = lineManager.create(new LineOptions().withLatLngs(latLngs));
    assertEquals(lineManager.getAnnotations().get(0), line);
  }

  @Test
  public void addLines() {
    List<List<LatLng>> latLngList = new ArrayList<>();
    latLngList.add(new ArrayList<LatLng>() {{
      add(new LatLng(2, 2));
      add(new LatLng(2, 3));
    }});
    latLngList.add(new ArrayList<LatLng>() {{
      add(new LatLng(1, 1));
      add(new LatLng(2, 3));
    }});
    List<LineOptions> options = new ArrayList<>();
    for (List<LatLng> latLngs : latLngList) {
      options.add(new LineOptions().withLatLngs(latLngs));
    }
    List<Line> lines = lineManager.create(options);
    assertTrue("Returned value size should match", lines.size() == 2);
    assertTrue("Annotations size should match", lineManager.getAnnotations().size() == 2);
  }

  @Test
  public void testDeleteLine() {
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line line = lineManager.create(new LineOptions().withLatLngs(latLngs));
    lineManager.delete(line);
    assertTrue(lineManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometryLine() {
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line line = lineManager.create(new LineOptions().withLatLngs(latLngs));
    assertEquals(line.getGeometry(), LineString.fromLngLats(new ArrayList<Point>() {{
      add(Point.fromLngLat(0, 0));
      add(Point.fromLngLat(1, 1));
    }}));
  }

  @Test
  public void testFeatureIdLine() {
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line lineZero = lineManager.create(new LineOptions().withLatLngs(latLngs));
    Line lineOne = lineManager.create(new LineOptions().withLatLngs(latLngs));
    assertEquals(lineZero.getFeature().get(Line.ID_KEY).getAsLong(), 0);
    assertEquals(lineOne.getFeature().get(Line.ID_KEY).getAsLong(), 1);
  }

  @Test
  public void testLineDraggableFlag() {
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line lineZero = lineManager.create(new LineOptions().withLatLngs(latLngs));

    assertFalse(lineZero.isDraggable());
    lineZero.setDraggable(true);
    assertTrue(lineZero.isDraggable());
    lineZero.setDraggable(false);
    assertFalse(lineZero.isDraggable());
  }
}