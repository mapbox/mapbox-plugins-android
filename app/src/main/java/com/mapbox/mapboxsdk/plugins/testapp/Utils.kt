package com.mapbox.mapboxsdk.plugins.testapp

import android.location.Location

import com.mapbox.mapboxsdk.constants.Style

import java.util.Random

import timber.log.Timber

/**
 * Useful utilities used throughout the testapp.
 */
object Utils {

    private val STYLES = arrayOf(Style.MAPBOX_STREETS, Style.OUTDOORS, Style.LIGHT, Style.DARK, Style.SATELLITE_STREETS)

    private var index: Int = 0

    /**
     * Utility to cycle through map styles. Useful to test if runtime styling source and layers transfer over to new
     * style.
     *
     * @return a string ID representing the map style
     */
    val nextStyle: String
        get() {
            index++
            if (index == STYLES.size) {
                index = 0
            }
            return STYLES[index]
        }

    /**
     * Utility for getting a random coordinate inside a provided bounding box and creates a [Location] from it.
     *
     * @param bbox double array forming the bounding box in the order of `[minx, miny, maxx, maxy]`
     * @return a [Location] object using the random coordinate
     */
    fun getRandomLocation(bbox: DoubleArray): Location {
        val random = Random()
        val randomLat = bbox[1] + (bbox[3] - bbox[1]) * random.nextDouble()
        val randomLon = bbox[0] + (bbox[2] - bbox[0]) * random.nextDouble()
        val location = Location("random-loc")
        location.longitude = randomLon
        location.latitude = randomLat
        Timber.d("getRandomLatLng: %s", location.toString())
        return location
    }
}