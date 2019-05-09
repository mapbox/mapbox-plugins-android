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

public class LineManagerTest {

  private DraggableAnnotationController<Line, OnLineDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapView mapView = mock(MapView.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private Style style = mock(Style.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private GeoJsonSource optionedGeoJsonSource = mock(GeoJsonSource.class);
  private LineLayer lineLayer = mock(LineLayer.class);
  private LineManager lineManager;
  private CoreElementProvider<LineLayer> coreElementProvider = mock(CoreElementProvider.class);
  private GeoJsonOptions geoJsonOptions = mock(GeoJsonOptions.class);

  @Before
  public void beforeTest() {
    when(coreElementProvider.getLayer()).thenReturn(lineLayer);
    when(coreElementProvider.getSource(null)).thenReturn(geoJsonSource);
    when(coreElementProvider.getSource(geoJsonOptions)).thenReturn(optionedGeoJsonSource);
    when(style.isFullyLoaded()).thenReturn(true);
  }

  @Test
  public void testInitialization() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayer(lineLayer);
    assertTrue(lineManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : lineManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(lineLayer).setProperties(lineManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(lineLayer, times(0)).setFilter(any(Expression.class));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testInitializationOnStyleReload() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayer(lineLayer);
    assertTrue(lineManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : lineManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(lineLayer).setProperties(lineManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));

    Expression filter = Expression.literal(false);
    lineManager.setFilter(filter);

    ArgumentCaptor<MapView.OnDidFinishLoadingStyleListener> loadingArgumentCaptor = ArgumentCaptor.forClass(MapView.OnDidFinishLoadingStyleListener.class);
    verify(mapView).addOnDidFinishLoadingStyleListener(loadingArgumentCaptor.capture());
    loadingArgumentCaptor.getValue().onDidFinishLoadingStyle();

    ArgumentCaptor<Style.OnStyleLoaded> styleLoadedArgumentCaptor = ArgumentCaptor.forClass(Style.OnStyleLoaded.class);
    verify(mapboxMap).getStyle(styleLoadedArgumentCaptor.capture());

    Style newStyle = mock(Style.class);
    when(newStyle.isFullyLoaded()).thenReturn(true);
    GeoJsonSource newSource = mock(GeoJsonSource.class);
    when(coreElementProvider.getSource(null)).thenReturn(newSource);
    LineLayer newLayer = mock(LineLayer.class);
    when(coreElementProvider.getLayer()).thenReturn(newLayer);
    styleLoadedArgumentCaptor.getValue().onStyleLoaded(newStyle);

    verify(newStyle).addSource(newSource);
    verify(newStyle).addLayer(newLayer);
    assertTrue(lineManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : lineManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(newLayer).setProperties(lineManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(lineLayer).setFilter(filter);
    verify(draggableAnnotationController, times(2)).onSourceUpdated();
    verify(newSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testLayerBelowInitialization() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, "test_layer", null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayerBelow(lineLayer, "test_layer");
    assertTrue(lineManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : lineManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(lineLayer).setProperties(lineManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testGeoJsonOptionsInitialization() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, geoJsonOptions, draggableAnnotationController);
    verify(style).addSource(optionedGeoJsonSource);
    verify(style).addLayer(lineLayer);
    assertTrue(lineManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : lineManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(lineLayer).setProperties(lineManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(lineLayer, times(0)).setFilter(any(Expression.class));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(optionedGeoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testNoUpdateOnStyleReload() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, "test_layer", null, draggableAnnotationController);
    verify(geoJsonSource, times(1)).setGeoJson(any(FeatureCollection.class));

    when(style.isFullyLoaded()).thenReturn(false);
    lineManager.updateSource();
    verify(geoJsonSource, times(1)).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testAddLine() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line line = lineManager.create(new LineOptions().withLatLngs(latLngs));
    assertEquals(lineManager.getAnnotations().get(0), line);
  }

  @Test
  public void addLineFromFeatureCollection() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<Point> points = new ArrayList<>();
    points.add(Point.fromLngLat(0, 0));
    points.add(Point.fromLngLat(1, 1));
    Geometry geometry = LineString.fromLngLats(points);

    Feature feature = Feature.fromGeometry(geometry);
    feature.addStringProperty("line-join", LINE_JOIN_BEVEL);
    feature.addNumberProperty("line-opacity", 0.3f);
    feature.addStringProperty("line-color", "rgba(0, 0, 0, 1)");
    feature.addNumberProperty("line-width", 0.3f);
    feature.addNumberProperty("line-gap-width", 0.3f);
    feature.addNumberProperty("line-offset", 0.3f);
    feature.addNumberProperty("line-blur", 0.3f);
    feature.addStringProperty("line-pattern", "pedestrian-polygon");
    feature.addBooleanProperty("is-draggable", true);

    List<Line> lines = lineManager.create(FeatureCollection.fromFeature(feature));
    Line line = lines.get(0);

    assertEquals(line.geometry, geometry);
    assertEquals(line.getLineJoin(), LINE_JOIN_BEVEL);
    assertEquals(line.getLineOpacity(), 0.3f);
    assertEquals(line.getLineColor(), "rgba(0, 0, 0, 1)");
    assertEquals(line.getLineWidth(), 0.3f);
    assertEquals(line.getLineGapWidth(), 0.3f);
    assertEquals(line.getLineOffset(), 0.3f);
    assertEquals(line.getLineBlur(), 0.3f);
    assertEquals(line.getLinePattern(), "pedestrian-polygon");
    assertTrue(line.isDraggable());
  }

  @Test
  public void addLines() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
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
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    Line line = lineManager.create(new LineOptions().withLatLngs(latLngs));
    lineManager.delete(line);
    assertTrue(lineManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometryLine() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs);
    Line line = lineManager.create(options);
    assertEquals(options.getLatLngs(), latLngs);
    assertEquals(line.getLatLngs(), latLngs);
    assertEquals(options.getGeometry(), LineString.fromLngLats(new ArrayList<Point>() {{
          add(Point.fromLngLat(0, 0));
          add(Point.fromLngLat(1, 1));
    }}));
    assertEquals(line.getGeometry(), LineString.fromLngLats(new ArrayList<Point>() {{
      add(Point.fromLngLat(0, 0));
      add(Point.fromLngLat(1, 1));
    }}));
  }

  @Test
  public void testFeatureIdLine() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
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
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
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


  @Test
  public void testLineJoinLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineJoin(get("line-join")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineJoin(LINE_JOIN_BEVEL);
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineJoin(get("line-join")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineJoin(get("line-join")))));
  }

  @Test
  public void testLineOpacityLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineOpacity(get("line-opacity")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineOpacity(0.3f);
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineOpacity(get("line-opacity")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineOpacity(get("line-opacity")))));
  }

  @Test
  public void testLineColorLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineColor(get("line-color")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineColor("rgba(0, 0, 0, 1)");
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineColor(get("line-color")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineColor(get("line-color")))));
  }

  @Test
  public void testLineWidthLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineWidth(get("line-width")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineWidth(0.3f);
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineWidth(get("line-width")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineWidth(get("line-width")))));
  }

  @Test
  public void testLineGapWidthLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineGapWidth(get("line-gap-width")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineGapWidth(0.3f);
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineGapWidth(get("line-gap-width")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineGapWidth(get("line-gap-width")))));
  }

  @Test
  public void testLineOffsetLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineOffset(get("line-offset")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineOffset(0.3f);
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineOffset(get("line-offset")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineOffset(get("line-offset")))));
  }

  @Test
  public void testLineBlurLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(lineBlur(get("line-blur")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLineBlur(0.3f);
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineBlur(get("line-blur")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(lineBlur(get("line-blur")))));
  }

  @Test
  public void testLinePatternLayerProperty() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(lineLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(linePattern(get("line-pattern")))));

    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs).withLinePattern("pedestrian-polygon");
    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(linePattern(get("line-pattern")))));

    lineManager.create(options);
    verify(lineLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(linePattern(get("line-pattern")))));
  }


  @Test
  public void testLineLayerFilter() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Expression expression = Expression.eq(Expression.get("test"), "selected");
    verify(lineLayer, times(0)).setFilter(expression);

    lineManager.setFilter(expression);
    verify(lineLayer, times(1)).setFilter(expression);

    when(lineLayer.getFilter()).thenReturn(expression);
    assertEquals(expression, lineManager.getFilter());
    assertEquals(expression, lineManager.layerFilter);
  }

  @Test
  public void testClickListener() {
    OnLineClickListener listener = mock(OnLineClickListener.class);
    lineManager = new  LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(lineManager.getClickListeners().isEmpty());
    lineManager.addClickListener(listener);
    assertTrue(lineManager.getClickListeners().contains(listener));
    lineManager.removeClickListener(listener);
    assertTrue( lineManager.getClickListeners().isEmpty());
  }

  @Test
  public void testLongClickListener() {
    OnLineLongClickListener listener = mock(OnLineLongClickListener.class);
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(lineManager.getLongClickListeners().isEmpty());
    lineManager.addLongClickListener(listener);
    assertTrue(lineManager.getLongClickListeners().contains(listener));
    lineManager.removeLongClickListener(listener);
    assertTrue(lineManager.getLongClickListeners().isEmpty());
  }

  @Test
  public void testDragListener() {
    OnLineDragListener listener = mock(OnLineDragListener.class);
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(lineManager.getDragListeners().isEmpty());
    lineManager.addDragListener(listener);
    assertTrue(lineManager.getDragListeners().contains(listener));
    lineManager.removeDragListener(listener);
    assertTrue(lineManager.getDragListeners().isEmpty());
  }

  @Test
  public void testCustomData() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs);
    options.withData(new JsonPrimitive("hello"));
    Line line = lineManager.create(options);
    assertEquals(new JsonPrimitive("hello"), line.getData());
  }

  @Test
  public void testClearAll() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs);
    lineManager.create(options);
    assertEquals(1, lineManager.getAnnotations().size());
    lineManager.deleteAll();
    assertEquals(0, lineManager.getAnnotations().size());
  }

  @Test
  public void testIgnoreClearedAnnotations() {
    lineManager = new LineManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng>latLngs = new ArrayList<>();
    latLngs.add(new LatLng());
    latLngs.add(new LatLng(1,1));
    LineOptions options = new LineOptions().withLatLngs(latLngs);
     Line  line = lineManager.create(options);
    assertEquals(1, lineManager.annotations.size());

    lineManager.getAnnotations().clear();
    lineManager.updateSource();
    assertTrue(lineManager.getAnnotations().isEmpty());

    lineManager.update(line);
    assertTrue(lineManager.getAnnotations().isEmpty());
  }

}