package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

interface CoreElementProvider<L extends Layer> {

  L getLayer();

  GeoJsonSource getSource();
}
