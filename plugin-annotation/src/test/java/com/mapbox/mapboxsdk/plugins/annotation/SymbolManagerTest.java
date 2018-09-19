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

public class SymbolManagerTest {

  private MapboxMap mapboxMap = mock(MapboxMap.class);
  private GeoJsonSource geoJsonSource = mock(GeoJsonSource.class);
  private SymbolLayer symbolLayer = mock(SymbolLayer.class);
  private SymbolManager symbolManager;

  @Before
  public void beforeTest() {
    symbolManager = new SymbolManager(mapboxMap, geoJsonSource, symbolLayer, null);
  }

  @Test
  public void testAddSymbol() {
    Symbol symbol = symbolManager.createSymbol(new LatLng());
    assertEquals(symbolManager.getAnnotations().get(0), symbol);
  }

  @Test
  public void addSymbols() {
    List<LatLng> latLngList = new ArrayList<>();
    latLngList.add(new LatLng());
    latLngList.add(new LatLng(1, 1));
    List<Symbol> symbols = symbolManager.createSymbols(latLngList);
    assertTrue("Returned value size should match", symbols.size() == 2);
    assertTrue("Annotations size should match", symbolManager.getAnnotations().size() == 2);
  }

  @Test
  public void testDeleteSymbol() {
    Symbol symbol = symbolManager.createSymbol(new LatLng());
    symbolManager.delete(symbol);
    assertTrue(symbolManager.getAnnotations().size() == 0);
  }

  @Test
  public void testGeometrySymbol() {
    Symbol symbol = symbolManager.createSymbol(new LatLng(12, 34));
    assertEquals(symbol.getGeometry(), Point.fromLngLat(34, 12));
  }

  @Test
  public void testFeatureIdSymbol() {
    Symbol symbolZero = symbolManager.createSymbol(new LatLng());
    Symbol symbolOne = symbolManager.createSymbol(new LatLng());
    assertEquals(symbolZero.getFeature().get(Symbol.ID_KEY).getAsLong(), 0);
    assertEquals(symbolOne.getFeature().get(Symbol.ID_KEY).getAsLong(), 1);
  }
}