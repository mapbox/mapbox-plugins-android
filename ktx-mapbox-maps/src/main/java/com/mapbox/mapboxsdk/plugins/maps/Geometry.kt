package com.mapbox.mapboxsdk.plugins.maps

import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng

/**
 * Returns a LatLng representation
 *
 * @return the latitude longitude pair
 */
inline fun LatLng.toPoint(): Point {
    return Point.fromLngLat(this.longitude, this.latitude);
}

/**
 * Returns a Point representation
 *
 * @return the point from the latitude longitude pair
 */
inline fun Point.toLatLng(): LatLng {
    return LatLng(this.latitude(), this.longitude())
}