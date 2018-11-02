package com.mapbox.mapboxsdk.plugins.testapp.activity.building

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar

import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.style.light.Position

import kotlinx.android.synthetic.main.activity_building.*
import timber.log.Timber

/**
 * Activity showcasing building plugin integration
 */
class BuildingActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapboxMap: MapboxMap? = null
    private var buildingPlugin: BuildingPlugin? = null
    private var isEnabled: Boolean = false
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building)
        this.mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fabBuilding.setOnClickListener{ _ ->
            buildingPlugin?.let {
                isEnabled = !isEnabled
                it.setVisibility(isEnabled)
                Timber.e("Building plugin is enabled :%s", isEnabled)
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        buildingPlugin = BuildingPlugin(mapView, mapboxMap)
        buildingPlugin?.setMinZoomLevel(15f)
        fabBuilding.visibility = View.VISIBLE
        initLightSeekbar()
    }

    // See https://en.wikipedia.org/wiki/Spherical_coordinate_system for more information on these values
    private fun initLightSeekbar() {
        seekbarLightRadialCoordinate.max = 24 // unknown?
        seekbarLightAzimuthalAngle.max = 180 // unknown?
        seekbarLightPolarAngle.max = 180 // polar angle ranges from 0 to 180 degrees

        val positionChangeListener = PositionChangeListener()
        seekbarLightRadialCoordinate.setOnSeekBarChangeListener(positionChangeListener)
        seekbarLightAzimuthalAngle.setOnSeekBarChangeListener(positionChangeListener)
        seekbarLightPolarAngle.setOnSeekBarChangeListener(positionChangeListener)
    }

    private inner class PositionChangeListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            invalidateLightPosition()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // Only listening to positionChange for onProgress.
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // Only listening to positionChange for onProgress.
        }
    }

    private fun invalidateLightPosition() {
        val light = mapboxMap?.light
        val radialCoordinate = seekbarLightRadialCoordinate.progress.toFloat() / 20
        val azimuthalAngle = seekbarLightAzimuthalAngle.progress.toFloat()
        val polarAngle = seekbarLightPolarAngle.progress.toFloat()
        light?.position = Position(radialCoordinate, azimuthalAngle, polarAngle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_building_min_zoom -> {
                buildingPlugin?.setMinZoomLevel(14f)
                return true
            }
            R.id.menu_building_opacity -> {
                buildingPlugin?.setOpacity(1.0f)
                return true
            }
            R.id.menu_building_color -> {
                buildingPlugin?.setColor(Color.RED)
                return true
            }
            R.id.menu_building_style -> {
                mapboxMap?.setStyle(Style.DARK)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_building, menu)
        return super.onCreateOptionsMenu(menu)
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

    fun getBuildingPlugin(): BuildingPlugin? {
        return buildingPlugin
    }
}
