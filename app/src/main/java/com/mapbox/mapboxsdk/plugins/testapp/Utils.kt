package com.mapbox.mapboxsdk.plugins.testapp

import android.content.Context
import android.location.Location
import com.mapbox.core.utils.TextUtils
import com.mapbox.mapboxsdk.maps.Style
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.Charset
import java.util.*

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

  @Throws(IOException::class)
  fun loadStringFromAssets(context: Context, fileName: String): String {
    if (TextUtils.isEmpty(fileName)) {
      throw NullPointerException("No GeoJSON File Name passed in.")
    }
    val `is` = context.assets.open(fileName)
    val rd = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))
    return readAll(rd)
  }

  @Throws(IOException::class)
  private fun readAll(rd: Reader): String {
    val sb = StringBuilder()
    rd.forEachLine {
      sb.append(it)
    }
    return sb.toString()
  }
}