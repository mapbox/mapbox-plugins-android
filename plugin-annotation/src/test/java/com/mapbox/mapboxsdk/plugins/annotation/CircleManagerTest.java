// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import com.google.gson.JsonPrimitive;
import com.mapbox.geojson.*;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.*;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
  private MapView mapView = mock(MapView.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private Style style = mock(Style.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private GeoJsonSource optionedGeoJsonSource = mock(GeoJsonSource.class);
  private CircleLayer circleLayer = mock(CircleLayer.class);
  private CircleManager circleManager;
  private CoreElementProvider<CircleLayer> coreElementProvider = mock(CoreElementProvider.class);
  private GeoJsonOptions geoJsonOptions = mock(GeoJsonOptions.class);

  @Before
  public void beforeTest() {
    when(coreElementProvider.getLayer()).thenReturn(circleLayer);
    when(coreElementProvider.getSource(null)).thenReturn(geoJsonSource);
    when(coreElementProvider.getSource(geoJsonOptions)).thenReturn(optionedGeoJsonSource);
    when(style.isFullyLoaded()).thenReturn(true);
  }

  @Test
  public void testInitialization() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayer(circleLayer);
    assertTrue(circleManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : circleManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(circleLayer).setProperties(circleManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(circleLayer, times(0)).setFilter(any(Expression.class));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testInitializationOnStyleReload() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayer(circleLayer);
    assertTrue(circleManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : circleManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(circleLayer).setProperties(circleManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));

    Expression filter = Expression.literal(false);
    circleManager.setFilter(filter);

    ArgumentCaptor<MapView.OnDidFinishLoadingStyleListener> loadingArgumentCaptor = ArgumentCaptor.forClass(MapView.OnDidFinishLoadingStyleListener.class);
    verify(mapView).addOnDidFinishLoadingStyleListener(loadingArgumentCaptor.capture());
    loadingArgumentCaptor.getValue().onDidFinishLoadingStyle();

    ArgumentCaptor<Style.OnStyleLoaded> styleLoadedArgumentCaptor = ArgumentCaptor.forClass(Style.OnStyleLoaded.class);
    verify(mapboxMap).getStyle(styleLoadedArgumentCaptor.capture());

    Style newStyle = mock(Style.class);
    when(newStyle.isFullyLoaded()).thenReturn(true);
    GeoJsonSource newSource = mock(GeoJsonSource.class);
    when(coreElementProvider.getSource(null)).thenReturn(newSource);
    CircleLayer newLayer = mock(CircleLayer.class);
    when(coreElementProvider.getLayer()).thenReturn(newLayer);
    styleLoadedArgumentCaptor.getValue().onStyleLoaded(newStyle);

    verify(newStyle).addSource(newSource);
    verify(newStyle).addLayer(newLayer);
    assertTrue(circleManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : circleManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(newLayer).setProperties(circleManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(circleLayer).setFilter(filter);
    verify(draggableAnnotationController, times(2)).onSourceUpdated();
    verify(newSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testLayerBelowInitialization() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, "test_layer", null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayerBelow(circleLayer, "test_layer");
    assertTrue(circleManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : circleManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(circleLayer).setProperties(circleManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testGeoJsonOptionsInitialization() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, geoJsonOptions, draggableAnnotationController);
    verify(style).addSource(optionedGeoJsonSource);
    verify(style).addLayer(circleLayer);
    assertTrue(circleManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : circleManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(circleLayer).setProperties(circleManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(circleLayer, times(0)).setFilter(any(Expression.class));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(optionedGeoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testNoUpdateOnStyleReload() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, "test_layer", null, draggableAnnotationController);
    verify(geoJsonSource, times(1)).setGeoJson(any(FeatureCollection.class));

    when(style.isFullyLoaded()).thenReturn(false);
    circleManager.updateSource();
    verify(geoJsonSource, times(1)).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testAddCircle() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Circle circle = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    assertEquals(circleManager.getAnnotations().get(0), circle);
  }

  @Test
  public void addCircleFromFeatureCollection() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
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
    assertEquals(circle.getCircleColor(), "rgba(0, 0, 0, 1)");
    assertEquals(circle.getCircleBlur(), 0.3f);
    assertEquals(circle.getCircleOpacity(), 0.3f);
    assertEquals(circle.getCircleStrokeWidth(), 0.3f);
    assertEquals(circle.getCircleStrokeColor(), "rgba(0, 0, 0, 1)");
    assertEquals(circle.getCircleStrokeOpacity(), 0.3f);
    assertTrue(circle.isDraggable());
  }

  @Test
  public void addCircles() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
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
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Circle circle = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    circleManager.delete(circle);
    assertTrue(circleManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometryCircle() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    LatLng latLng = new LatLng(12, 34);
    CircleOptions options = new CircleOptions().withLatLng(latLng);
    Circle circle = circleManager.create(options);
    assertEquals(options.getLatLng(), latLng);
    assertEquals(circle.getLatLng(), latLng);
    assertEquals(options.getGeometry(), Point.fromLngLat(34, 12));
    assertEquals(circle.getGeometry(), Point.fromLngLat(34, 12));
  }

  @Test
  public void testFeatureIdCircle() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Circle circleZero = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    Circle circleOne = circleManager.create(new CircleOptions().withLatLng(new LatLng()));
    assertEquals(circleZero.getFeature().get(Circle.ID_KEY).getAsLong(), 0);
    assertEquals(circleOne.getFeature().get(Circle.ID_KEY).getAsLong(), 1);
  }

  @Test
  public void testCircleDraggableFlag() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Circle circleZero = circleManager.create(new CircleOptions().withLatLng(new LatLng()));

    assertFalse(circleZero.isDraggable());
    circleZero.setDraggable(true);
    assertTrue(circleZero.isDraggable());
    circleZero.setDraggable(false);
    assertFalse(circleZero.isDraggable());
  }


  @Test
  public void testCircleRadiusLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleRadius(get("circle-radius")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleRadius(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleRadius(get("circle-radius")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleRadius(get("circle-radius")))));
  }

  @Test
  public void testCircleColorLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleColor(get("circle-color")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleColor("rgba(0, 0, 0, 1)");
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleColor(get("circle-color")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleColor(get("circle-color")))));
  }

  @Test
  public void testCircleBlurLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleBlur(get("circle-blur")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleBlur(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleBlur(get("circle-blur")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleBlur(get("circle-blur")))));
  }

  @Test
  public void testCircleOpacityLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleOpacity(get("circle-opacity")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleOpacity(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleOpacity(get("circle-opacity")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleOpacity(get("circle-opacity")))));
  }

  @Test
  public void testCircleStrokeWidthLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleStrokeWidth(get("circle-stroke-width")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleStrokeWidth(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeWidth(get("circle-stroke-width")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeWidth(get("circle-stroke-width")))));
  }

  @Test
  public void testCircleStrokeColorLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleStrokeColor(get("circle-stroke-color")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleStrokeColor("rgba(0, 0, 0, 1)");
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeColor(get("circle-stroke-color")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeColor(get("circle-stroke-color")))));
  }

  @Test
  public void testCircleStrokeOpacityLayerProperty() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(circleLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(circleStrokeOpacity(get("circle-stroke-opacity")))));

    CircleOptions options = new CircleOptions().withLatLng(new LatLng()).withCircleStrokeOpacity(0.3f);
    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeOpacity(get("circle-stroke-opacity")))));

    circleManager.create(options);
    verify(circleLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(circleStrokeOpacity(get("circle-stroke-opacity")))));
  }


  @Test
  public void testCircleLayerFilter() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Expression expression = Expression.eq(Expression.get("test"), "selected");
    verify(circleLayer, times(0)).setFilter(expression);

    circleManager.setFilter(expression);
    verify(circleLayer, times(1)).setFilter(expression);

    when(circleLayer.getFilter()).thenReturn(expression);
    assertEquals(expression, circleManager.getFilter());
    assertEquals(expression, circleManager.layerFilter);
  }

  @Test
  public void testClickListener() {
    OnCircleClickListener listener = mock(OnCircleClickListener.class);
    circleManager = new  CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(circleManager.getClickListeners().isEmpty());
    circleManager.addClickListener(listener);
    assertTrue(circleManager.getClickListeners().contains(listener));
    circleManager.removeClickListener(listener);
    assertTrue( circleManager.getClickListeners().isEmpty());
  }

  @Test
  public void testLongClickListener() {
    OnCircleLongClickListener listener = mock(OnCircleLongClickListener.class);
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(circleManager.getLongClickListeners().isEmpty());
    circleManager.addLongClickListener(listener);
    assertTrue(circleManager.getLongClickListeners().contains(listener));
    circleManager.removeLongClickListener(listener);
    assertTrue(circleManager.getLongClickListeners().isEmpty());
  }

  @Test
  public void testDragListener() {
    OnCircleDragListener listener = mock(OnCircleDragListener.class);
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(circleManager.getDragListeners().isEmpty());
    circleManager.addDragListener(listener);
    assertTrue(circleManager.getDragListeners().contains(listener));
    circleManager.removeDragListener(listener);
    assertTrue(circleManager.getDragListeners().isEmpty());
  }

  @Test
  public void testCustomData() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    CircleOptions options = new CircleOptions().withLatLng(new LatLng());
    options.withData(new JsonPrimitive("hello"));
    Circle circle = circleManager.create(options);
    assertEquals(new JsonPrimitive("hello"), circle.getData());
  }

  @Test
  public void testClearAll() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    CircleOptions options = new CircleOptions().withLatLng(new LatLng());
    circleManager.create(options);
    assertEquals(1, circleManager.getAnnotations().size());
    circleManager.deleteAll();
    assertEquals(0, circleManager.getAnnotations().size());
  }

  @Test
  public void testIgnoreClearedAnnotations() {
    circleManager = new CircleManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    CircleOptions options = new CircleOptions().withLatLng(new LatLng());
     Circle  circle = circleManager.create(options);
    assertEquals(1, circleManager.annotations.size());

    circleManager.getAnnotations().clear();
    circleManager.updateSource();
    assertTrue(circleManager.getAnnotations().isEmpty());

    circleManager.update(circle);
    assertTrue(circleManager.getAnnotations().isEmpty());
  }

}