package com.mapbox.mapboxsdk.plugins.annotation;

import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import androidx.annotation.Nullable;

interface CoreElementProvider<L extends Layer> {

  String getLayerId();

  String getSourceId();

  L getLayer();

  GeoJsonSource getSource(@Nullable GeoJsonOptions geoJsonOptions);
}
