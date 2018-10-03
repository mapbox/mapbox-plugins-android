package com.mapbox.mapboxsdk.plugins.testapp.activity.location

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ListPopupWindow
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerOptions
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.OnCameraTrackingChangedListener
import com.mapbox.mapboxsdk.plugins.locationlayer.OnLocationLayerClickListener
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.R

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_location_layer_mode.*

class LocationLayerModesActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener, OnLocationLayerClickListener, OnCameraTrackingChangedListener {

    private var locationLayerPlugin: LocationLayerPlugin? = null
    private var locationEngine: LocationEngine? = null
    private var mapboxMap: MapboxMap? = null
    private var customStyle: Boolean = false

    @CameraMode.Mode
    private var cameraMode = CameraMode.TRACKING

    @RenderMode.Mode
    private var renderMode = RenderMode.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_layer_mode)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        if (savedInstanceState != null) {
            cameraMode = savedInstanceState.getInt(SAVED_STATE_CAMERA)
            renderMode = savedInstanceState.getInt(SAVED_STATE_RENDER)
        }

        buttonLocationMode.setOnClickListener { _ ->
            locationLayerPlugin?.let {
                showModeListDialog()
            }
        }

        buttonLocationTracking.setOnClickListener { _ ->
            locationLayerPlugin?.let {
                showTrackingListDialog()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine?.let {
            it.priority = LocationEnginePriority.HIGH_ACCURACY
            it.fastestInterval = 1000
            it.addLocationEngineListener(this)
            it.activate()
        }

        val padding: IntArray = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            intArrayOf(0, 750, 0, 0)
        } else {
            intArrayOf(0, 250, 0, 0)
        }

        val options = LocationLayerOptions.builder(this)
                .padding(padding)
                .layerBelow("waterway-label")
                .build()
        locationLayerPlugin = LocationLayerPlugin(mapView, mapboxMap, locationEngine, options)
        locationLayerPlugin?.let {
            it.addOnLocationClickListener(this)
            it.addOnCameraTrackingChangedListener(this)
            it.cameraMode = cameraMode
            setRendererMode(renderMode)
            lifecycle.addObserver(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_location, menu)
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (locationLayerPlugin == null) {
            return super.onOptionsItemSelected(item)
        }

        when (item.itemId) {
            R.id.action_style_change -> {
                toggleStyle()
                return true
            }
            R.id.action_map_style_change -> {
                toggleMapStyle()
                return true
            }
            R.id.action_plugin_disable -> {
                locationLayerPlugin?.isLocationLayerEnabled = false
                return true
            }
            R.id.action_plugin_enabled -> {
                locationLayerPlugin?.isLocationLayerEnabled = true
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    fun toggleStyle() {
        customStyle = !customStyle
        locationLayerPlugin?.applyStyle(if (customStyle)
            R.style.CustomLocationLayer
        else
            R.style.mapbox_LocationLayer)
    }

    fun toggleMapStyle() {
        val styleUrl = if (mapboxMap?.styleUrl?.contentEquals(Style.DARK) == true) Style.LIGHT else Style.DARK
        mapboxMap?.setStyle(styleUrl)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
        locationEngine?.let {
            it.addLocationEngineListener(this)
            if (it.isConnected) {
                it.requestLocationUpdates()
            } else {
                it.activate()
            }
        }
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
        locationEngine?.let {
            it.removeLocationEngineListener(this)
            it.removeLocationUpdates()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
        outState.putInt(SAVED_STATE_CAMERA, cameraMode)
        outState.putInt(SAVED_STATE_RENDER, renderMode)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        locationEngine?.deactivate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @SuppressLint("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }

    override fun onLocationChanged(location: Location) {
        // no impl
    }

    override fun onLocationLayerClick() {
        Toast.makeText(this, "OnLocationLayerClick", Toast.LENGTH_LONG).show()
    }

    private fun showModeListDialog() {
        val modes = ArrayList<String>()
        modes.add("Normal")
        modes.add("Compass")
        modes.add("GPS")
        val profileAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, modes)
        val listPopup = ListPopupWindow(this)
        listPopup.setAdapter(profileAdapter)
        listPopup.anchorView = buttonLocationMode
        listPopup.setOnItemClickListener { parent, itemView, position, id ->
            val selectedMode = modes[position]
            buttonLocationMode.text = selectedMode
            if (selectedMode.contentEquals("Normal")) {
                setRendererMode(RenderMode.NORMAL)
            } else if (selectedMode.contentEquals("Compass")) {
                setRendererMode(RenderMode.COMPASS)
            } else if (selectedMode.contentEquals("GPS")) {
                setRendererMode(RenderMode.GPS)
            }
            listPopup.dismiss()
        }
        listPopup.show()
    }

    private fun setRendererMode(@RenderMode.Mode mode: Int) {
        renderMode = mode
        locationLayerPlugin?.renderMode = mode
        when (mode) {
            RenderMode.NORMAL -> buttonLocationMode.text = "Normal"
            RenderMode.COMPASS -> buttonLocationMode.text = "Compass"
            RenderMode.GPS -> buttonLocationMode.text = "Gps"
        }
    }

    private fun showTrackingListDialog() {
        val trackingTypes = ArrayList<String>()
        trackingTypes.add("None")
        trackingTypes.add("Tracking")
        trackingTypes.add("Tracking Compass")
        trackingTypes.add("Tracking GPS")
        trackingTypes.add("Tracking GPS North")
        val profileAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, trackingTypes)
        val listPopup = ListPopupWindow(this)
        listPopup.setAdapter(profileAdapter)
        listPopup.anchorView = buttonLocationTracking
        listPopup.setOnItemClickListener { _, _, position, _ ->
            val selectedTrackingType = trackingTypes[position]
            buttonLocationTracking.text = selectedTrackingType
            if (selectedTrackingType.contentEquals("None")) {
                locationLayerPlugin?.cameraMode = CameraMode.NONE
            } else if (selectedTrackingType.contentEquals("Tracking")) {
                locationLayerPlugin?.cameraMode = CameraMode.TRACKING
            } else if (selectedTrackingType.contentEquals("Tracking Compass")) {
                locationLayerPlugin?.cameraMode = CameraMode.TRACKING_COMPASS
            } else if (selectedTrackingType.contentEquals("Tracking GPS")) {
                locationLayerPlugin?.cameraMode = CameraMode.TRACKING_GPS
            } else if (selectedTrackingType.contentEquals("Tracking GPS North")) {
                locationLayerPlugin?.cameraMode = CameraMode.TRACKING_GPS_NORTH
            }
            listPopup.dismiss()

            if (locationLayerPlugin?.cameraMode != CameraMode.NONE) {
                locationLayerPlugin?.zoomWhileTracking(15.0, 750, object : MapboxMap.CancelableCallback {
                    override fun onCancel() {
                        // No impl
                    }

                    override fun onFinish() {
                        locationLayerPlugin?.tiltWhileTracking(45.0)
                    }
                })
            } else {
                mapboxMap?.easeCamera(CameraUpdateFactory.tiltTo(0.0))
            }
        }
        listPopup.show()
    }

    override fun onCameraTrackingDismissed() {
        buttonLocationTracking.text = "None"
    }

    override fun onCameraTrackingChanged(currentMode: Int) {
        this.cameraMode = currentMode

        when (cameraMode) {
            CameraMode.NONE -> buttonLocationTracking.text = "None"
            CameraMode.TRACKING -> buttonLocationTracking.text = "Tracking"
            CameraMode.TRACKING_COMPASS -> buttonLocationTracking.text = "Tracking Compass"
            CameraMode.TRACKING_GPS -> buttonLocationTracking.text = "Tracking GPS"
            CameraMode.TRACKING_GPS_NORTH -> buttonLocationTracking.text = "Tracking GPS North"
        }
    }

    companion object {

        private val SAVED_STATE_CAMERA = "saved_state_camera"
        private val SAVED_STATE_RENDER = "saved_state_render"
    }
}