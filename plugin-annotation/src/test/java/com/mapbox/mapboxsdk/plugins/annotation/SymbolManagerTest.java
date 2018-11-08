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

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.Property.*;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;
import static junit.framework.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class SymbolManagerTest {

  private DraggableAnnotationController<Symbol, OnSymbolDragListener> draggableAnnotationController = mock(DraggableAnnotationController.class);
  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private SymbolLayer symbolLayer = mock(SymbolLayer.class);
  private SymbolManager symbolManager;

  @Before
  public void beforeTest() {
    symbolManager = new SymbolManager(mapboxMap, geoJsonSource, symbolLayer, null, draggableAnnotationController);
  }

  @Test
  public void testAddSymbol() {
    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    assertEquals(symbolManager.getAnnotations().get(0), symbol);
  }

  @Test
  public void addSymbols() {
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
    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    symbolManager.delete(symbol);
    assertTrue(symbolManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometrySymbol() {
    Symbol symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng(12, 34)));
    assertEquals(symbol.getGeometry(), Point.fromLngLat(34, 12));
  }

  @Test
  public void testFeatureIdSymbol() {
    Symbol symbolZero = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    Symbol symbolOne = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    assertEquals(symbolZero.getFeature().get(Symbol.ID_KEY).getAsLong(), 0);
    assertEquals(symbolOne.getFeature().get(Symbol.ID_KEY).getAsLong(), 1);
  }

  @Test
  public void testSymbolDraggableFlag() {
    Symbol symbolZero = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));

    assertFalse(symbolZero.isDraggable());
    symbolZero.setDraggable(true);
    assertTrue(symbolZero.isDraggable());
    symbolZero.setDraggable(false);
    assertFalse(symbolZero.isDraggable());
  }


  @Test
  public void testIconSizeLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconSize(get("icon-size")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconSize(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconSize(get("icon-size")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconSize(get("icon-size")))));
  }

  @Test
  public void testIconImageLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconImage(get("icon-image")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconImage("undefined");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconImage(get("icon-image")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconImage(get("icon-image")))));
  }

  @Test
  public void testIconRotateLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconRotate(get("icon-rotate")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconRotate(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconRotate(get("icon-rotate")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconRotate(get("icon-rotate")))));
  }

  @Test
  public void testIconOffsetLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconOffset(get("icon-offset")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconOffset(new Float[] {0f, 0f});
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOffset(get("icon-offset")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOffset(get("icon-offset")))));
  }

  @Test
  public void testIconAnchorLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconAnchor(get("icon-anchor")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconAnchor(ICON_ANCHOR_CENTER);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconAnchor(get("icon-anchor")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconAnchor(get("icon-anchor")))));
  }

  @Test
  public void testTextFieldLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textField(get("text-field")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextField("");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textField(get("text-field")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textField(get("text-field")))));
  }

  @Test
  public void testTextFontLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textFont(get("text-font")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextFont(new String[]{"Open Sans Regular", "Arial Unicode MS Regular"});
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textFont(get("text-font")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textFont(get("text-font")))));
  }

  @Test
  public void testTextSizeLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textSize(get("text-size")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextSize(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textSize(get("text-size")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textSize(get("text-size")))));
  }

  @Test
  public void testTextMaxWidthLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textMaxWidth(get("text-max-width")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextMaxWidth(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textMaxWidth(get("text-max-width")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textMaxWidth(get("text-max-width")))));
  }

  @Test
  public void testTextLetterSpacingLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textLetterSpacing(get("text-letter-spacing")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextLetterSpacing(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textLetterSpacing(get("text-letter-spacing")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textLetterSpacing(get("text-letter-spacing")))));
  }

  @Test
  public void testTextJustifyLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textJustify(get("text-justify")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextJustify(TEXT_JUSTIFY_LEFT);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textJustify(get("text-justify")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textJustify(get("text-justify")))));
  }

  @Test
  public void testTextAnchorLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textAnchor(get("text-anchor")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextAnchor(TEXT_ANCHOR_CENTER);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textAnchor(get("text-anchor")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textAnchor(get("text-anchor")))));
  }

  @Test
  public void testTextRotateLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textRotate(get("text-rotate")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextRotate(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textRotate(get("text-rotate")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textRotate(get("text-rotate")))));
  }

  @Test
  public void testTextTransformLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textTransform(get("text-transform")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextTransform(TEXT_TRANSFORM_NONE);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textTransform(get("text-transform")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textTransform(get("text-transform")))));
  }

  @Test
  public void testTextOffsetLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textOffset(get("text-offset")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextOffset(new Float[] {0f, 0f});
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOffset(get("text-offset")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOffset(get("text-offset")))));
  }

  @Test
  public void testIconOpacityLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconOpacity(get("icon-opacity")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconOpacity(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOpacity(get("icon-opacity")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconOpacity(get("icon-opacity")))));
  }

  @Test
  public void testIconColorLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconColor(get("icon-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconColor(get("icon-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconColor(get("icon-color")))));
  }

  @Test
  public void testIconHaloColorLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconHaloColor(get("icon-halo-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconHaloColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloColor(get("icon-halo-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloColor(get("icon-halo-color")))));
  }

  @Test
  public void testIconHaloWidthLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconHaloWidth(get("icon-halo-width")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconHaloWidth(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloWidth(get("icon-halo-width")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloWidth(get("icon-halo-width")))));
  }

  @Test
  public void testIconHaloBlurLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(iconHaloBlur(get("icon-halo-blur")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withIconHaloBlur(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloBlur(get("icon-halo-blur")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(iconHaloBlur(get("icon-halo-blur")))));
  }

  @Test
  public void testTextOpacityLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textOpacity(get("text-opacity")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextOpacity(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOpacity(get("text-opacity")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textOpacity(get("text-opacity")))));
  }

  @Test
  public void testTextColorLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textColor(get("text-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textColor(get("text-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textColor(get("text-color")))));
  }

  @Test
  public void testTextHaloColorLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textHaloColor(get("text-halo-color")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextHaloColor("rgba(0, 0, 0, 1)");
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloColor(get("text-halo-color")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloColor(get("text-halo-color")))));
  }

  @Test
  public void testTextHaloWidthLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textHaloWidth(get("text-halo-width")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextHaloWidth(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloWidth(get("text-halo-width")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloWidth(get("text-halo-width")))));
  }

  @Test
  public void testTextHaloBlurLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(textHaloBlur(get("text-halo-blur")))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng()).withTextHaloBlur(0.3f);
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloBlur(get("text-halo-blur")))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(textHaloBlur(get("text-halo-blur")))));
  }

  @Test
  public void testSymbolZOrderLayerProperty() {
    verify(symbolLayer, times(0)).setProperties(argThat(new PropertyValueMatcher(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE))));

    SymbolOptions options = new SymbolOptions().withLatLng(new LatLng());
    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE))));

    symbolManager.create(options);
    verify(symbolLayer, times(1)).setProperties(argThat(new PropertyValueMatcher(symbolZOrder(Property.SYMBOL_Z_ORDER_SOURCE))));
  }
}