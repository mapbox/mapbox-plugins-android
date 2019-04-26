package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.geojson.Feature;

import java.util.Comparator;

public class SymbolComparator implements Comparator<Feature> {
  @Override
  public int compare(Feature left, Feature right) {
    return left.getProperty(SymbolOptions.PROPERTY_Z_INDEX).getAsInt() - right.getProperty(SymbolOptions.PROPERTY_Z_INDEX).getAsInt();
  }
}
