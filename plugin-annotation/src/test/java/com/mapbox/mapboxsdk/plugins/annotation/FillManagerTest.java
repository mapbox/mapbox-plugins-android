// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.*;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import android.graphics.PointF;

import static com.mapbox.mapboxsdk.plugins.annotation.ConvertUtils.convertArray;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.Property.*;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;
import static junit.framework.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class FillManagerTest {

  private DraggableAnnotationController<Fill, OnFillDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private FillLayer fillLayer = mock(FillLayer.class);
  private FillManager fillManager;

  @Before
  public void beforeTest() {
    fillManager = new FillManager(mapboxMap, geoJsonSource, fillLayer, null, draggableAnnotationController);
  }

  @Test
  public void testAddFill() {
    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    innerLatLngs.add(new LatLng());
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    Fill fill = fillManager.create(new FillOptions().withLatLngs(latLngs));
    assertEquals(fillManager.getAnnotations().get(0), fill);
  }

  @Test
  public void addFillFromFeatureCollection() {
    List<Point> innerPoints = new ArrayList<>();
    innerPoints.add(Point.fromLngLat(0, 0));
    innerPoints.add(Point.fromLngLat(1, 1));
    innerPoints.add(Point.fromLngLat(-1, -1));
    innerPoints.add(Point.fromLngLat(0, 0));
    List<List<Point>> points = new ArrayList<>();
    points.add(innerPoints);
    Geometry geometry = Polygon.fromLngLats(points);

    Feature feature = Feature.fromGeometry(geometry);
    feature.addNumberProperty("fill-opacity", 0.3f);
    feature.addStringProperty("fill-color", "rgba(0, 0, 0, 1)");
    feature.addStringProperty("fill-outline-color", "rgba(0, 0, 0, 1)");
    feature.addStringProperty("fill-pattern", "pedestrian-polygon");
    feature.addBooleanProperty("is-draggable", true);

    List<Fill> fills = fillManager.create(FeatureCollection.fromFeature(feature));
    Fill fill = fills.get(0);

    assertEquals(fill.geometry, geometry);
    assertEquals(fill.getFillOpacity(), 0.3f);
    assertEquals(fill.getFillColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    assertEquals(fill.getFillOutlineColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    assertEquals(fill.getFillPattern(), "pedestrian-polygon");
    assertTrue(fill.isDraggable());
  }

  @Test
  public void addFills() {
    List<List<LatLng>> latLngListOne = new ArrayList<>();
    latLngListOne.add(new ArrayList<LatLng>() {{
      add(new LatLng(2, 2));
      add(new LatLng(2, 3));
    }});
    latLngListOne.add(new ArrayList<LatLng>() {{
      add(new LatLng(1, 1));
      add(new LatLng(2, 3));
    }});

    List<List<LatLng>> latLngListTwo = new ArrayList<>();
    latLngListTwo.add(new ArrayList<LatLng>() {{
      add(new LatLng(5, 7));
      add(new LatLng(2, 3));
    }});
    latLngListTwo.add(new ArrayList<LatLng>() {{
      add(new LatLng(1, 1));
      add(new LatLng(3, 9));
    }});

    List<List<List<LatLng>>> latLngList = new ArrayList<List<List<LatLng>>>(){{
      add(latLngListOne);
      add(latLngListTwo);
    }};
    List<FillOptions> options = new ArrayList<>();
    for (List<List<LatLng>> lists : latLngList) {
      options.add(new FillOptions().withLatLngs(lists));
    }
    List<Fill> fills = fillManager.create(options);
    assertTrue("Returned value size should match", fills.size() == 2);
    assertTrue("Annotations size should match", fillManager.getAnnotations().size() == 2);
  }

  @Test
  public void testDeleteFill() {
    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    Fill fill = fillManager.create(new FillOptions().withLatLngs(latLngs));
    fillManager.delete(fill);
    assertTrue(fillManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometryFill() {
    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    Fill fill = fillManager.create(new FillOptions().withLatLngs(latLngs));
    assertEquals(fill.getGeometry(), Polygon.fromLngLats(new ArrayList<List<Point>>() {{
      add(new ArrayList<Point>() {{
        add(Point.fromLngLat(0, 0));
        add(Point.fromLngLat(1, 1));
        add(Point.fromLngLat(-1, -1));
      }});
    }}));
  }

  @Test
  public void testFeatureIdFill() {
    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    Fill fillZero = fillManager.create(new FillOptions().withLatLngs(latLngs));
    Fill fillOne = fillManager.create(new FillOptions().withLatLngs(latLngs));
    assertEquals(fillZero.getFeature().get(Fill.ID_KEY).getAsLong(), 0);
    assertEquals(fillOne.getFeature().get(Fill.ID_KEY).getAsLong(), 1);
  }

  @Test
  public void testFillDraggableFlag() {
    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    Fill fillZero = fillManager.create(new FillOptions().withLatLngs(latLngs));

    assertFalse(fillZero.isDraggable());
    fillZero.setDraggable(true);
    assertTrue(fillZero.isDraggable());
    fillZero.setDraggable(false);
    assertFalse(fillZero.isDraggable());
  }


  @Test
  public void testFillOpacityLayerProperty() {
    verify(fillLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(fillOpacity(get("fill-opacity")))));

    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    FillOptions options = new FillOptions().withLatLngs(latLngs).withFillOpacity(0.3f);
    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillOpacity(get("fill-opacity")))));

    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillOpacity(get("fill-opacity")))));
  }

  @Test
  public void testFillColorLayerProperty() {
    verify(fillLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(fillColor(get("fill-color")))));

    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    FillOptions options = new FillOptions().withLatLngs(latLngs).withFillColor("rgba(0, 0, 0, 1)");
    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillColor(get("fill-color")))));

    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillColor(get("fill-color")))));
  }

  @Test
  public void testFillOutlineColorLayerProperty() {
    verify(fillLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(fillOutlineColor(get("fill-outline-color")))));

    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    FillOptions options = new FillOptions().withLatLngs(latLngs).withFillOutlineColor("rgba(0, 0, 0, 1)");
    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillOutlineColor(get("fill-outline-color")))));

    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillOutlineColor(get("fill-outline-color")))));
  }

  @Test
  public void testFillPatternLayerProperty() {
    verify(fillLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(fillPattern(get("fill-pattern")))));

    List<LatLng>innerLatLngs = new ArrayList<>();
    innerLatLngs.add(new LatLng());
    innerLatLngs.add(new LatLng(1,1));
    innerLatLngs.add(new LatLng(-1,-1));
    List<List<LatLng>>latLngs = new ArrayList<>();
    latLngs.add(innerLatLngs);
    FillOptions options = new FillOptions().withLatLngs(latLngs).withFillPattern("pedestrian-polygon");
    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillPattern(get("fill-pattern")))));

    fillManager.create(options);
    verify(fillLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(fillPattern(get("fill-pattern")))));
  }

}