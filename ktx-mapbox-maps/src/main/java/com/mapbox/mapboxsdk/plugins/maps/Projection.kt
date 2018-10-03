package com.mapbox.mapboxsdk.plugins.maps

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Projection

/**
 * Returns the geographic location that corresponds to a screen location.
 * The screen location is specified in screen pixels (not display pixels) relative to the
 * top left of the map (not the top left of the whole screen).
 *
 * @param point A Point on the screen in screen pixels.
 * @return The LatLng corresponding to the point on the screen, or null if the ray through
 * the given screen point does not intersect the ground plane.
 */
fun MapboxMap.fromScreenLocation(point: PointF): LatLng {
    return this.projection.fromScreenLocation(point)
}

/**
 * Returns a screen location that corresponds to a geographical coordinate (LatLng).
 * The screen location is in screen pixels (not display pixels) relative to the top left
 * of the map (not of the whole screen).
 *
 * @param location A LatLng on the map to convert to a screen location.
 * @return A Point representing the screen location in screen pixels.
 */
fun MapboxMap.toScreenLocation(latLng: LatLng): PointF {
    return this.projection.toScreenLocation(latLng)
}

/**
 *
 */
fun Projection.toScreenLocation(topRight: PointF, bottomLeft: PointF): RectF {
    return RectF(bottomLeft.x, topRight.y, topRight.x, bottomLeft.y)
}