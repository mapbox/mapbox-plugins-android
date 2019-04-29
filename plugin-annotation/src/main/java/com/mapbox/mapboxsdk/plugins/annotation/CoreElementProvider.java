package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

interface CoreElementProvider<L extends Layer> {

  String getLayerId();

  L getLayer();

  GeoJsonSource getSource(@Nullable GeoJsonOptions geoJsonOptions);
}
