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

public class SymbolManagerTest {

  private DraggableAnnotationController<Symbol, OnSymbolDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapView mapView = mock(MapView.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private Style style = mock(Style.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private GeoJsonSource optionedGeoJsonSource = mock(GeoJsonSource.class);
  private SymbolLayer symbolLayer = mock(SymbolLayer.class);
  private SymbolManager symbolManager;
  private CoreElementProvider<SymbolLayer> coreElementProvider = mock(CoreElementProvider.class);
  private GeoJsonOptions geoJsonOptions = mock(GeoJsonOptions.class);

  @Before
  public void beforeTest() {
    when(coreElementProvider.getLayer()).thenReturn(symbolLayer);
    when(coreElementProvider.getSource(null)).thenReturn(geoJsonSource);
    when(coreElementProvider.getSource(geoJsonOptions)).thenReturn(optionedGeoJsonSource);
    when(style.isFullyLoaded()).thenReturn(true);
  }

  @Test
  public void testInitialization() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayer(symbolLayer);
    assertTrue(symbolManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : symbolManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(symbolLayer).setProperties(symbolManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(symbolLayer, times(0)).setFilter(any(Expression.class));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testInitializationOnStyleReload() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayer(symbolLayer);
    assertTrue(symbolManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : symbolManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(symbolLayer).setProperties(symbolManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));

    Expression filter = Expression.literal(false);
    symbolManager.setFilter(filter);

    ArgumentCaptor<MapView.OnDidFinishLoadingStyleListener> loadingArgumentCaptor = ArgumentCaptor.forClass(MapView.OnDidFinishLoadingStyleListener.class);
    verify(mapView).addOnDidFinishLoadingStyleListener(loadingArgumentCaptor.capture());
    loadingArgumentCaptor.getValue().onDidFinishLoadingStyle();

    ArgumentCaptor<Style.OnStyleLoaded> styleLoadedArgumentCaptor = ArgumentCaptor.forClass(Style.OnStyleLoaded.class);
    verify(mapboxMap).getStyle(styleLoadedArgumentCaptor.capture());

    Style newStyle = mock(Style.class);
    when(newStyle.isFullyLoaded()).thenReturn(true);
    GeoJsonSource newSource = mock(GeoJsonSource.class);
    when(coreElementProvider.getSource(null)).thenReturn(newSource);
    SymbolLayer newLayer = mock(SymbolLayer.class);
    when(coreElementProvider.getLayer()).thenReturn(newLayer);
    styleLoadedArgumentCaptor.getValue().onStyleLoaded(newStyle);

    verify(newStyle).addSource(newSource);
    verify(newStyle).addLayer(newLayer);
    assertTrue(symbolManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : symbolManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(newLayer).setProperties(symbolManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(symbolLayer).setFilter(filter);
    verify(draggableAnnotationController, times(2)).onSourceUpdated();
    verify(newSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testLayerBelowInitialization() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, "test_layer", null, draggableAnnotationController);
    verify(style).addSource(geoJsonSource);
    verify(style).addLayerBelow(symbolLayer, "test_layer");
    assertTrue(symbolManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : symbolManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(symbolLayer).setProperties(symbolManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(geoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testGeoJsonOptionsInitialization() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, geoJsonOptions, draggableAnnotationController);
    verify(style).addSource(optionedGeoJsonSource);
    verify(style).addLayer(symbolLayer);
    assertTrue(symbolManager.dataDrivenPropertyUsageMap.size() > 0);
    for (Boolean value : symbolManager.dataDrivenPropertyUsageMap.values()) {
      assertFalse(value);
    }
    verify(symbolLayer).setProperties(symbolManager.constantPropertyUsageMap.values().toArray(new PropertyValue[0]));
    verify(symbolLayer, times(0)).setFilter(any(Expression.class));
    verify(draggableAnnotationController).onSourceUpdated();
    verify(optionedGeoJsonSource).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testNoUpdateOnStyleReload() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, "test_layer", null, draggableAnnotationController);
    verify(geoJsonSource, times(1)).setGeoJson(any(FeatureCollection.class));

    when(style.isFullyLoaded()).thenReturn(false);
    symbolManager.updateSource();
    verify(geoJsonSource, times(1)).setGeoJson(any(FeatureCollection.class));
  }

  @Test
  public void testAddSymbol() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    assertEquals(symbolManager.getAnnotations().get(0), symbol);
  }

  @Test
  public void addSymbolFromFeatureCollection() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Geometry geometry = Point.fromLngLat(10, 10);

    Feature feature = Feature.fromGeometry(geometry);
    feature.addNumberProperty("icon-size", 0.3f);
    feature.addStringProperty("icon-image", "undefined");
    feature.addNumberProperty("icon-rotate", 0.3f);
    feature.addProperty("icon-offset", convertArray(new Float[] {0f, 0f}));
    feature.addStringProperty("icon-anchor", ICON_ANCHOR_CENTER);
    feature.addStringProperty("text-field", "");
    feature.addProperty("text-font", convertArray(new String[]{"Open Sans Regular", "Arial Unicode MS Regular"}));
    feature.addNumberProperty("text-size", 0.3f);
    feature.addNumberProperty("text-max-width", 0.3f);
    feature.addNumberProperty("text-letter-spacing", 0.3f);
    feature.addStringProperty("text-justify", TEXT_JUSTIFY_LEFT);
    feature.addStringProperty("text-anchor", TEXT_ANCHOR_CENTER);
    feature.addNumberProperty("text-rotate", 0.3f);
    feature.addStringProperty("text-transform", TEXT_TRANSFORM_NONE);
    feature.addProperty("text-offset", convertArray(new Float[] {0f, 0f}));
    feature.addNumberProperty("icon-opacity", 0.3f);
    feature.addStringProperty("icon-color", "rgba(0, 0, 0, 1)");
    feature.addStringProperty("icon-halo-color", "rgba(0, 0, 0, 1)");
    feature.addNumberProperty("icon-halo-width", 0.3f);
    feature.addNumberProperty("icon-halo-blur", 0.3f);
    feature.addNumberProperty("text-opacity", 0.3f);
    feature.addStringProperty("text-color", "rgba(0, 0, 0, 1)");
    feature.addStringProperty("text-halo-color", "rgba(0, 0, 0, 1)");
    feature.addNumberProperty("text-halo-width", 0.3f);
    feature.addNumberProperty("text-halo-blur", 0.3f);
    feature.addNumberProperty("z-index", 2);
    feature.addBooleanProperty("is-draggable", true);

    List<Symbol> symbols = symbolManager.create(FeatureCollection.fromFeature(feature));
    Symbol symbol = symbols.get(0);

    assertEquals(symbol.geometry, geometry);
    assertEquals(symbol.getIconSize(), 0.3f);
    assertEquals(symbol.getIconImage(), "undefined");
    assertEquals(symbol.getIconRotate(), 0.3f);
    PointF iconOffsetExpected = new PointF(new Float[] {0f, 0f}[0], new Float[] {0f, 0f}[1]);
    assertEquals(iconOffsetExpected.x, symbol.getIconOffset().x);
    assertEquals(iconOffsetExpected.y, symbol.getIconOffset().y);
    assertEquals(symbol.getIconAnchor(), ICON_ANCHOR_CENTER);
    assertEquals(symbol.getTextField(), "");
    assertTrue(Arrays.equals(symbol.getTextFont(), new String[]{"Open Sans Regular", "Arial Unicode MS Regular"}));
    assertEquals(symbol.getTextSize(), 0.3f);
    assertEquals(symbol.getTextMaxWidth(), 0.3f);
    assertEquals(symbol.getTextLetterSpacing(), 0.3f);
    assertEquals(symbol.getTextJustify(), TEXT_JUSTIFY_LEFT);
    assertEquals(symbol.getTextAnchor(), TEXT_ANCHOR_CENTER);
    assertEquals(symbol.getTextRotate(), 0.3f);
    assertEquals(symbol.getTextTransform(), TEXT_TRANSFORM_NONE);
    PointF textOffsetExpected = new PointF(new Float[] {0f, 0f}[0], new Float[] {0f, 0f}[1]);
    assertEquals(textOffsetExpected.x, symbol.getTextOffset().x);
    assertEquals(textOffsetExpected.y, symbol.getTextOffset().y);
    assertEquals(symbol.getIconOpacity(), 0.3f);
    assertEquals(symbol.getIconColor(), "rgba(0, 0, 0, 1)");
    assertEquals(symbol.getIconHaloColor(), "rgba(0, 0, 0, 1)");
    assertEquals(symbol.getIconHaloWidth(), 0.3f);
    assertEquals(symbol.getIconHaloBlur(), 0.3f);
    assertEquals(symbol.getTextOpacity(), 0.3f);
    assertEquals(symbol.getTextColor(), "rgba(0, 0, 0, 1)");
    assertEquals(symbol.getTextHaloColor(), "rgba(0, 0, 0, 1)");
    assertEquals(symbol.getTextHaloWidth(), 0.3f);
    assertEquals(symbol.getTextHaloBlur(), 0.3f);
    assertEquals(symbol.getZIndex(), 2);
    assertTrue(symbol.isDraggable());
  }

  @Test
  public void addSymbols() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    List<LatLng> latLngList = new ArrayList<>();
    latLngList.add(new LatLng());
    latLngList.add(new LatLng(1, 1));
    List< SymbolOptions> options = new ArrayList<>();
    for (LatLng latLng : latLngList) {
      options.add(new  SymbolOptions().withLatLng(latLng));
    }
    List<Symbol> symbols = symbolManager.create(options);
    assertTrue("Returned value size should match", symbols.size() == 2);
    assertTrue("Annotations size should match", symbolManager.getAnnotations().size() == 2);
  }

  @Test
  public void testDeleteSymbol() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    symbolManager.delete(symbol);
    assertTrue(symbolManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometrySymbol() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    LatLng latLng = new LatLng(12, 34);
    SymbolOptions options = new SymbolOptions().withLatLng(latLng);
    Symbol symbol = symbolManager.create(options);
    assertEquals(options.getLatLng(), latLng);
    assertEquals(symbol.getLatLng(), latLng);
    assertEquals(options.getGeometry(), Point.fromLngLat(34, 12));
    assertEquals(symbol.getGeometry(), Point.fromLngLat(34, 12));
  }

  @Test
  public void testFeatureIdSymbol() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Symbol symbolZero = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    Symbol symbolOne = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    assertEquals(symbolZero.getFeature().get(Symbol.ID_KEY).getAsLong(), 0);
    assertEquals(symbolOne.getFeature().get(Symbol.ID_KEY).getAsLong(), 1);
  }

  @Test
  public void testSymbolDraggableFlag() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Symbol symbolZero = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));

    assertFalse(symbolZero.isDraggable());
    symbolZero.setDraggable(true);
    assertTrue(symbolZero.isDraggable());
    symbolZero.setDraggable(false);
    assertFalse(symbolZero.isDraggable());
  }


  @Test
  public void testIconSizeLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconSize(get("icon-size")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconSize(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconSize(get("icon-size")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconSize(get("icon-size")))));
  }

  @Test
  public void testIconImageLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconImage(get("icon-image")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconImage("undefined");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconImage(get("icon-image")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconImage(get("icon-image")))));
  }

  @Test
  public void testIconRotateLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconRotate(get("icon-rotate")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconRotate(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconRotate(get("icon-rotate")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconRotate(get("icon-rotate")))));
  }

  @Test
  public void testIconOffsetLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconOffset(get("icon-offset")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconOffset(new Float[] {0f, 0f});
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOffset(get("icon-offset")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOffset(get("icon-offset")))));
  }

  @Test
  public void testIconAnchorLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconAnchor(get("icon-anchor")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconAnchor(ICON_ANCHOR_CENTER);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconAnchor(get("icon-anchor")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconAnchor(get("icon-anchor")))));
  }

  @Test
  public void testTextFieldLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textField(get("text-field")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextField("");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textField(get("text-field")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textField(get("text-field")))));
  }

  @Test
  public void testTextFontLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textFont(get("text-font")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextFont(new String[]{"Open Sans Regular", "Arial Unicode MS Regular"});
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textFont(get("text-font")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textFont(get("text-font")))));
  }

  @Test
  public void testTextSizeLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textSize(get("text-size")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextSize(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textSize(get("text-size")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textSize(get("text-size")))));
  }

  @Test
  public void testTextMaxWidthLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textMaxWidth(get("text-max-width")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextMaxWidth(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textMaxWidth(get("text-max-width")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textMaxWidth(get("text-max-width")))));
  }

  @Test
  public void testTextLetterSpacingLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textLetterSpacing(get("text-letter-spacing")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextLetterSpacing(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textLetterSpacing(get("text-letter-spacing")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textLetterSpacing(get("text-letter-spacing")))));
  }

  @Test
  public void testTextJustifyLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textJustify(get("text-justify")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextJustify(TEXT_JUSTIFY_LEFT);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textJustify(get("text-justify")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textJustify(get("text-justify")))));
  }

  @Test
  public void testTextAnchorLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textAnchor(get("text-anchor")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextAnchor(TEXT_ANCHOR_CENTER);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textAnchor(get("text-anchor")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textAnchor(get("text-anchor")))));
  }

  @Test
  public void testTextRotateLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textRotate(get("text-rotate")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextRotate(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textRotate(get("text-rotate")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textRotate(get("text-rotate")))));
  }

  @Test
  public void testTextTransformLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textTransform(get("text-transform")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextTransform(TEXT_TRANSFORM_NONE);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textTransform(get("text-transform")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textTransform(get("text-transform")))));
  }

  @Test
  public void testTextOffsetLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textOffset(get("text-offset")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextOffset(new Float[] {0f, 0f});
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOffset(get("text-offset")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOffset(get("text-offset")))));
  }

  @Test
  public void testIconOpacityLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconOpacity(get("icon-opacity")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconOpacity(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOpacity(get("icon-opacity")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOpacity(get("icon-opacity")))));
  }

  @Test
  public void testIconColorLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconColor(get("icon-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconColor(get("icon-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconColor(get("icon-color")))));
  }

  @Test
  public void testIconHaloColorLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconHaloColor(get("icon-halo-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconHaloColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloColor(get("icon-halo-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloColor(get("icon-halo-color")))));
  }

  @Test
  public void testIconHaloWidthLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconHaloWidth(get("icon-halo-width")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconHaloWidth(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloWidth(get("icon-halo-width")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloWidth(get("icon-halo-width")))));
  }

  @Test
  public void testIconHaloBlurLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconHaloBlur(get("icon-halo-blur")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconHaloBlur(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloBlur(get("icon-halo-blur")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloBlur(get("icon-halo-blur")))));
  }

  @Test
  public void testTextOpacityLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textOpacity(get("text-opacity")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextOpacity(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOpacity(get("text-opacity")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOpacity(get("text-opacity")))));
  }

  @Test
  public void testTextColorLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textColor(get("text-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textColor(get("text-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textColor(get("text-color")))));
  }

  @Test
  public void testTextHaloColorLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textHaloColor(get("text-halo-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextHaloColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloColor(get("text-halo-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloColor(get("text-halo-color")))));
  }

  @Test
  public void testTextHaloWidthLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textHaloWidth(get("text-halo-width")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextHaloWidth(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloWidth(get("text-halo-width")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloWidth(get("text-halo-width")))));
  }

  @Test
  public void testTextHaloBlurLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textHaloBlur(get("text-halo-blur")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextHaloBlur(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloBlur(get("text-halo-blur")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloBlur(get("text-halo-blur")))));
  }

  @Test
  public void testSymbolZOrderLayerProperty() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng());
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE))));
  }

  @Test
  public void testSymbolLayerFilter() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    Expression expression = Expression.eq(Expression.get("test"), "selected");
    verify(symbolLayer, times(0)).setFilter(expression);

    symbolManager.setFilter(expression);
    verify(symbolLayer, times(1)).setFilter(expression);

    when(symbolLayer.getFilter()).thenReturn(expression);
    assertEquals(expression, symbolManager.getFilter());
    assertEquals(expression, symbolManager.layerFilter);
  }

  @Test
  public void testClickListener() {
    OnSymbolClickListener listener = mock(OnSymbolClickListener.class);
    symbolManager = new  SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(symbolManager.getClickListeners().isEmpty());
    symbolManager.addClickListener(listener);
    assertTrue(symbolManager.getClickListeners().contains(listener));
    symbolManager.removeClickListener(listener);
    assertTrue( symbolManager.getClickListeners().isEmpty());
  }

  @Test
  public void testLongClickListener() {
    OnSymbolLongClickListener listener = mock(OnSymbolLongClickListener.class);
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(symbolManager.getLongClickListeners().isEmpty());
    symbolManager.addLongClickListener(listener);
    assertTrue(symbolManager.getLongClickListeners().contains(listener));
    symbolManager.removeLongClickListener(listener);
    assertTrue(symbolManager.getLongClickListeners().isEmpty());
  }

  @Test
  public void testDragListener() {
    OnSymbolDragListener listener = mock(OnSymbolDragListener.class);
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    assertTrue(symbolManager.getDragListeners().isEmpty());
    symbolManager.addDragListener(listener);
    assertTrue(symbolManager.getDragListeners().contains(listener));
    symbolManager.removeDragListener(listener);
    assertTrue(symbolManager.getDragListeners().isEmpty());
  }

  @Test
  public void testCustomData() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng());
    options.withData(new JsonPrimitive("hello"));
    Symbol symbol = symbolManager.create(options);
    assertEquals(new JsonPrimitive("hello"), symbol.getData());
  }

  @Test
  public void testClearAll() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng());
    symbolManager.create(options);
    assertEquals(1, symbolManager.getAnnotations().size());
    symbolManager.deleteAll();
    assertEquals(0, symbolManager.getAnnotations().size());
  }

  @Test
  public void testIgnoreClearedAnnotations() {
    symbolManager = new SymbolManager(mapView, mapboxMap, style, coreElementProvider, null, null, draggableAnnotationController);
    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng());
     Symbol  symbol = symbolManager.create(options);
    assertEquals(1, symbolManager.annotations.size());

    symbolManager.getAnnotations().clear();
    symbolManager.updateSource();
    assertTrue(symbolManager.getAnnotations().isEmpty());

    symbolManager.update(symbol);
    assertTrue(symbolManager.getAnnotations().isEmpty());
  }

}