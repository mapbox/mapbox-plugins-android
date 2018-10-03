package com.mapbox.mapboxsdk.plugins.testapp.activity.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.plugins.testapp.Utils

import kotlinx.android.synthetic.main.activity_location_manual_update.*
import timber.log.Timber

class ManualLocationUpdatesActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener {

    private var locationLayerPlugin: LocationLayerPlugin? = null
    private var locationEngine: LocationEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_manual_update)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fabToggleManualLocation.setOnClickListener {
            locationLayerPlugin?.let {
                fabToggleManualLocation.setImageResource(if (it.locationEngine == null)
                    R.drawable.ic_location
                else
                    R.drawable.ic_location_disabled)
            }
        }

        fabToggleManualLocation.setOnClickListener {
            locationLayerPlugin?.forceLocationUpdate(
                    Utils.getRandomLocation(doubleArrayOf(-77.1825, 38.7825, -76.9790, 39.0157)))
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine?.let {
            it.addLocationEngineListener(this)
            it.priority = LocationEnginePriority.HIGH_ACCURACY
            it.activate()
        }

        locationLayerPlugin = LocationLayerPlugin(mapView, mapboxMap)
        locationLayerPlugin?.let {
            it.renderMode = RenderMode.NORMAL
            lifecycle.addObserver(it)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }

    override fun onLocationChanged(location: Location) {
        Timber.d("Location change occurred: %s", location.toString())
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
        locationEngine?.let {
            it.requestLocationUpdates()
            it.addLocationEngineListener(this)
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
}