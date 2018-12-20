// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
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

public class CircleManagerTest {

  private DraggableAnnotationController<Circle, OnCircleDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private Style style = mock(Style.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private CircleLayer circleLayer = mock(CircleLayer.class);
  private CircleManager circleManager;

  @Before
  public void beforeTest() {
    when(style.isFullyLoaded()).thenReturn(true);
    circleManager = new CircleManager(mapboxMap, style, geoJsonSource, circleLayer, null, draggableAnnotationController);
  }

  @Test
  public void testAddCircle() {
    Circle circle = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    assertEquals(circleManager.getAnnotations().get(0), circle);
  }

  @Test
  public void addCircleFromFeatureCollection() {
    Geometry geometry = Point.fromLngLat(10, 10);

    Feature feature = Feature.fromGeometry(geometry);
    feature.addNumberProperty("circle-radius", 0.3f);
    feature.addStringProperty("circle-color", "rgba(0, 0, 0, 1)");
    feature.addNumberProperty("circle-blur", 0.3f);
    feature.addNumberProperty("circle-opacity", 0.3f);
    feature.addNumberProperty("circle-stroke-width", 0.3f);
    feature.addStringProperty("circle-stroke-color", "rgba(0, 0, 0, 1)");
    feature.addNumberProperty("circle-stroke-opacity", 0.3f);
    feature.addBooleanProperty("is-draggable", true);

    List<Circle> circles = circleManager.create(FeatureCollection.fromFeature(feature));
    Circle circle = circles.get(0);

    assertEquals(circle.geometry, geometry);
    assertEquals(circle.getCircleRadius(), 0.3f);
    assertEquals(circle.getCircleColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    assertEquals(circle.getCircleBlur(), 0.3f);
    assertEquals(circle.getCircleOpacity(), 0.3f);
    assertEquals(circle.getCircleStrokeWidth(), 0.3f);
    assertEquals(circle.getCircleStrokeColor(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    assertEquals(circle.getCircleStrokeOpacity(), 0.3f);
    assertTrue(circle.isDraggable());
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


  @Test
  public void testCircleRadiusLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleRadius(get("circle-radius")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleRadius(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleRadius(get("circle-radius")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleRadius(get("circle-radius")))));
  }

  @Test
  public void testCircleColorLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleColor(get("circle-color")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleColor("rgba(0, 0, 0, 1)");
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleColor(get("circle-color")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleColor(get("circle-color")))));
  }

  @Test
  public void testCircleBlurLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleBlur(get("circle-blur")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleBlur(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleBlur(get("circle-blur")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleBlur(get("circle-blur")))));
  }

  @Test
  public void testCircleOpacityLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleOpacity(get("circle-opacity")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleOpacity(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleOpacity(get("circle-opacity")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleOpacity(get("circle-opacity")))));
  }

  @Test
  public void testCircleStrokeWidthLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleStrokeWidth(get("circle-stroke-width")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleStrokeWidth(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeWidth(get("circle-stroke-width")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeWidth(get("circle-stroke-width")))));
  }

  @Test
  public void testCircleStrokeColorLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleStrokeColor(get("circle-stroke-color")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleStrokeColor("rgba(0, 0, 0, 1)");
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeColor(get("circle-stroke-color")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeColor(get("circle-stroke-color")))));
  }

  @Test
  public void testCircleStrokeOpacityLayerProperty() {
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleStrokeOpacity(get("circle-stroke-opacity")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleStrokeOpacity(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeOpacity(get("circle-stroke-opacity")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeOpacity(get("circle-stroke-opacity")))));
  }


  @Test
  public void testCircleLayerFilter() {
    Expression expression = Expression.eq(Expression.get("test"), "selected");
    verify(circleLayer, times(0)).setFilter(expression);

    circleManager.setFilter(expression);
    verify(circleLayer, times(1)).setFilter(expression);

    when(circleLayer.getFilter()).thenReturn(expression);
    assertEquals(expression, circleManager.getFilter());
  }
}