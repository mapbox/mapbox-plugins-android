package com.mapbox.mapboxsdk.plugins.utils

import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

fun MapboxMap.queryLocationSourceFeatures(sourceId: String): List<Feature> {
  return this.getSourceAs<GeoJsonSource>(sourceId)?.querySourceFeatures(null) as List<Feature>
}

fun MapboxMap.isLayerVisible(layerId: String): Boolean {
  return this.getLayer(layerId)?.visibility?.value?.equals(Property.VISIBLE)!!
}