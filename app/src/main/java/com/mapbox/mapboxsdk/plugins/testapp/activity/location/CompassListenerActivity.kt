package com.mapbox.mapboxsdk.plugins.testapp.activity.location

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.CompassListener
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.R

import kotlinx.android.synthetic.main.activity_compass_listener.*
import timber.log.Timber

class CompassListenerActivity : AppCompatActivity(), OnMapReadyCallback {

    private var locationLayerPlugin: LocationLayerPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass_listener)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationLayerPlugin = LocationLayerPlugin(mapView, mapboxMap, locationEngine)
        locationLayerPlugin?.renderMode = RenderMode.COMPASS
        locationLayerPlugin?.addCompassListener(object : CompassListener {
            override fun onCompassChanged(userHeading: Float) {
                val cameraPosition = CameraPosition.Builder().bearing(userHeading.toDouble()).build()
                mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }

            override fun onCompassAccuracyChange(compassStatus: Int) {
                Timber.v("Compass reading: %d", compassStatus)
            }
        })
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        locationLayerPlugin?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        locationLayerPlugin?.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}