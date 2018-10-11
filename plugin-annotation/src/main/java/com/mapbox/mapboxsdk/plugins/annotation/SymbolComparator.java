package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.geojson.Feature;

import java.util.Comparator;

import static com.mapbox.mapboxsdk.plugins.annotation.Symbol.Z_INDEX;

public class SymbolComparator implements Comparator<Feature> {
  @Override
  public int compare(Feature left, Feature right) {
    return left.getProperty(Z_INDEX).getAsInt() - right.getProperty(Z_INDEX).getAsInt();
  }
}
