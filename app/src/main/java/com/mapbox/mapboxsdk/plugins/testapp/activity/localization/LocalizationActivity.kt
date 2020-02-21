package com.mapbox.mapboxsdk.plugins.testapp.activity.localization

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.mapbox.mapboxsdk.plugins.localization.MapLocale
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.plugins.testapp.Utils
import kotlinx.android.synthetic.main.activity_localization.*

class LocalizationActivity : AppCompatActivity(), OnMapReadyCallback {

  private var localizationPlugin: LocalizationPlugin? = null
  private var mapboxMap: MapboxMap? = null
  private var mapIsLocalized: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_localization)
    mapIsLocalized = true
    Toast.makeText(this, R.string.change_language_instruction, Toast.LENGTH_LONG).show()
    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync(this)
  }

  override fun onMapReady(mapboxMap: MapboxMap) {
    mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
      this.mapboxMap = mapboxMap
      localizationPlugin = LocalizationPlugin(mapView, mapboxMap, style).also { localizationPlugin ->
        localizationPlugin.matchMapLanguageWithDeviceDefault()
      }

      fabLocalize.setOnClickListener {
        mapIsLocalized = if (mapIsLocalized) {
          localizationPlugin?.setMapLanguage(MapLocale(MapLocale.FRENCH))
          Toast.makeText(this, R.string.map_not_localized, Toast.LENGTH_SHORT).show()
          false
        } else {
          localizationPlugin?.matchMapLanguageWithDeviceDefault()
          Toast.makeText(this, R.string.map_localized, Toast.LENGTH_SHORT).show()
          true
        }
      }

      fabCamera.setOnClickListener {
        val locale = nextMapLocale
        localizationPlugin?.apply {
          setMapLanguage(locale)
          setCameraToLocaleCountry(locale, 25)
        }
      }

      fabStyles.setOnClickListener {
        mapboxMap.setStyle(Style.Builder().fromUri(Utils.nextStyle))
      }
    }
  }

  override fun onStart() {
    super.onStart()
    mapView.onStart()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onStop() {
    super.onStop()
    mapView.onStop()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_languages, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.english -> {
        localizationPlugin?.setMapLanguage(MapLocale.ENGLISH)
        return true
      }
      R.id.spanish -> {
        localizationPlugin?.setMapLanguage(MapLocale.SPANISH)
        return true
      }
      R.id.french -> {
        localizationPlugin?.setMapLanguage(MapLocale.FRENCH)
        return true
      }
      R.id.german -> {
        localizationPlugin?.setMapLanguage(MapLocale.GERMAN)
        return true
      }
      R.id.russian -> {
        localizationPlugin?.setMapLanguage(MapLocale.RUSSIAN)
        return true
      }
      R.id.chinese -> {
        localizationPlugin?.setMapLanguage(MapLocale.CHINESE)
        return true
      }
      R.id.simplified_chinese -> {
        localizationPlugin?.setMapLanguage(MapLocale.SIMPLIFIED_CHINESE)
        return true
      }
      R.id.portuguese -> {
        localizationPlugin?.setMapLanguage(MapLocale.PORTUGUESE)
        return true
      }
      R.id.arabic -> {
        localizationPlugin?.setMapLanguage(MapLocale.ARABIC)
        return true
      }
      R.id.japanese -> {
        localizationPlugin?.setMapLanguage(MapLocale.JAPANESE)
        return true
      }
      R.id.korean -> {
        localizationPlugin?.setMapLanguage(MapLocale.KOREAN)
        return true
      }
      R.id.vietnamese -> {
        localizationPlugin?.setMapLanguage(MapLocale.VIETNAMESE)
        return true
      }
      R.id.italian -> {
        localizationPlugin?.setMapLanguage(MapLocale.ITALIAN)
        return true
      }
      R.id.local -> {
        localizationPlugin?.setMapLanguage(MapLocale.LOCAL_NAME)
        return true
      }
      android.R.id.home -> {
        finish()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {

    private val LOCALES = arrayOf(MapLocale.CANADA, MapLocale.GERMANY, MapLocale.CHINA,
      MapLocale.US, MapLocale.CANADA_FRENCH, MapLocale.JAPAN, MapLocale.KOREA,
      MapLocale.FRANCE, MapLocale.SPAIN, MapLocale.VIETNAM, MapLocale.ITALY, MapLocale.UK
    )

    private var index: Int = 0

    val nextMapLocale: MapLocale
      get() {
        index++
        if (index == LOCALES.size) {
          index = 0
        }
        return LOCALES[index]
      }
  }
}