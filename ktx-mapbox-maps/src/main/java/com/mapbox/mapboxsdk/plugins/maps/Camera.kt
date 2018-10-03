package com.mapbox.mapboxsdk.plugins.maps

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.constants.MapboxConstants
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

//
// Move camera
//

/**
 * Repositions the camera to a latitude-longitude pair.
 * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
 * See CameraUpdateFactory for a set of updates.
 *
 * @param latLng The position that should be applied to the camera
 */
fun MapboxMap.moveCamera(latLng: LatLng) {
    this.moveCamera(CameraUpdateFactory.newLatLng(latLng))
}

/**
 * Repositions the camera to a latitude-longitude pair and zoom.
 * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
 * See CameraUpdateFactory for a set of updates.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 */
fun MapboxMap.moveCamera(latLng: LatLng, zoom: Double) {
    this.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
}

/**
 * Repositions the camera to a latitude-longitude pair, zoom and bearing.
 * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
 * See CameraUpdateFactory for a set of updates.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 */
fun MapboxMap.moveCamera(latLng: LatLng, zoom: Double, bearing: Double) {
    this.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).build()
            )
    )
}

/**
 * Repositions the camera to a latitude-longitude pair, zoom, bearing and tilt.
 * The move is instantaneous, and a subsequent getCameraPosition() will reflect the new position.
 * See CameraUpdateFactory for a set of updates.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param tilt The tilt that should be applied to the camera
 */
fun MapboxMap.moveCamera(latLng: LatLng, zoom: Double, bearing: Double, tilt: Double) {
    this.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build()
            )
    )
}

//
// Ease camera
//

/**
 * Gradually move the camera by the default duration to a latitude-longitude pair.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, callback: MapboxMap.CancelableCallback) {
    this.easeCamera(CameraUpdateFactory.newLatLng(latLng), MapboxConstants.ANIMATION_DURATION, callback)
}

/**
 * Gradually move the camera to a latitude-longitude pair.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param duration The duration that the easing should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, duration: Int = MapboxConstants.ANIMATION_DURATION,
                                callback: MapboxMap.CancelableCallback? = null) {
    this.easeCamera(CameraUpdateFactory.newLatLng(latLng), duration, callback)
}

/**
 * Gradually move the camera by the default duration to a latitude-longitude pair and zoom.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, zoom: Double, callback: MapboxMap.CancelableCallback) {
    this.easeCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), MapboxConstants.ANIMATION_DURATION, callback)
}

/**
 * Gradually move the camera to a latitude-longitude pair and zoom.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param duration The duration that the easing should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, zoom: Double, duration: Int = MapboxConstants.ANIMATION_DURATION,
                                callback: MapboxMap.CancelableCallback? = null) {
    this.easeCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), duration, callback)
}

/**
 * Gradually move the camera by the default duration to a latitude-longitude pair, zoom and bearing.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, zoom: Double, bearing: Double,
                                callback: MapboxMap.CancelableCallback) {
    this.easeCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).build()
            ), MapboxConstants.ANIMATION_DURATION, callback
    )
}

/**
 * Gradually move the camera to a latitude-longitude pair, zoom and bearing.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param duration The duration that the easing should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, zoom: Double, bearing: Double,
                                duration: Int = MapboxConstants.ANIMATION_DURATION,
                                callback: MapboxMap.CancelableCallback? = null) {
    this.easeCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).build()
            ), duration, callback
    )
}

/**
 * Gradually move the camera by the default duration to a latitude-longitude pair, zoom, bearing and tilt.
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param tilt The tilt that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, zoom: Double, bearing: Double, tilt: Double,
                                callback: MapboxMap.CancelableCallback) {
    this.easeCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build()
            ), MapboxConstants.ANIMATION_DURATION, callback
    )
}

/**
 * Gradually move the camera to a latitude-longitude pair, zoom, bearing and tilt
 * If getCameraPosition is called during the animation, it will return the current location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param tilt The tilt that should be applied to the camera
 * @param duration The duration that the easing should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.easeCamera(latLng: LatLng, zoom: Double, bearing: Double, tilt: Double,
                                duration: Int = MapboxConstants.ANIMATION_DURATION,
                                callback: MapboxMap.CancelableCallback? = null) {
    this.easeCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build()
            ), duration, callback
    )
}

//
// Animate camera
//

/**
 * Animate the camera to a new latitude-longitude pair using a transition animation that evokes powered flight.
 * The animation will last the default amount of time. During the animation, a call to getCameraPosition
 * returns an intermediate location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, callback: MapboxMap.CancelableCallback) {
    this.animateCamera(CameraUpdateFactory.newLatLng(latLng), callback)
}

/**
 * Animate the camera to a new latitude-longitude pair using a transition animation that evokes powered flight.
 * During the animation, a call to getCameraPosition returns an intermediate location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param duration The duration that the animation should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, duration: Int = MapboxConstants.ANIMATION_DURATION,
                                   callback: MapboxMap.CancelableCallback? = null) {
    this.animateCamera(CameraUpdateFactory.newLatLng(latLng), duration, callback)
}

/**
 * Animate the camera to a new latitude-longitude pair and zoom using a transition animation that evokes powered flight.
 * The animation will last the default amount of time. During the animation, a call to getCameraPosition
 * returns an intermediate location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, zoom: Double, callback: MapboxMap.CancelableCallback) {
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), callback)
}

/**
 * Animate the camera to a new latitude-longitude pair and zoom using a transition animation that evokes powered flight.
 * During the animation, a call to getCameraPosition returns an intermediate location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param duration The duration that the animation should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, zoom: Double, duration: Int = MapboxConstants.ANIMATION_DURATION,
                                   callback: MapboxMap.CancelableCallback? = null) {
    this.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), duration, callback)
}

/**
 * Animate the camera to a new latitude-longitude pair, zoom and bearing using a transition animation that evokes
 * powered flight. The animation will last the default amount of time. During the animation, a call to
 * getCameraPosition returns an intermediate location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, zoom: Double, bearing: Double,
                                   callback: MapboxMap.CancelableCallback) {
    this.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).build()
            ), callback
    )
}

/**
 * Animate the camera to a new latitude-longitude pair, zoom and bearing using a transition animation that evokes
 * powered flight. During the animation, a call to getCameraPosition returns an intermediate location of the camera
 * in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param duration The duration that the animation should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, zoom: Double, bearing: Double,
                                   duration: Int = MapboxConstants.ANIMATION_DURATION,
                                   callback: MapboxMap.CancelableCallback? = null) {
    this.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).build()
            ), duration, callback
    )
}

/**
 * Animate the camera to a new latitude-longitude pair, zoom and bearing using a transition animation that evokes
 * powered flight. The animation will last the default amount of time. During the animation, a call to
 * getCameraPosition returns an intermediate location of the camera in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param tilt The tilt that should be applied to the camera
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, zoom: Double, bearing: Double, tilt: Double,
                                   callback: MapboxMap.CancelableCallback) {
    this.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build()
            ), callback
    )
}

/**
 * Animate the camera to a new latitude-longitude pair, zoom, bearing and tilt using a transition animation that evokes
 * powered flight. During the animation, a call to getCameraPosition returns an intermediate location of the camera
 * in flight.
 *
 * @param latLng The position that should be applied to the camera
 * @param zoom The zoom that should be applied to the camera
 * @param bearing The bearing that should be applied to the camera
 * @param tilt The tilt that should be applied to the camera
 * @param duration The duration that the animation should take
 * @param callback The callback to be invoked when easing has finished or has been cancelled
 */
fun MapboxMap.animateCamera(latLng: LatLng, zoom: Double, bearing: Double, tilt: Double,
                                   duration: Int = MapboxConstants.ANIMATION_DURATION,
                                   callback: MapboxMap.CancelableCallback? = null) {
    this.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(latLng).zoom(zoom).bearing(bearing).tilt(tilt).build()
            ), duration, callback
    )
}