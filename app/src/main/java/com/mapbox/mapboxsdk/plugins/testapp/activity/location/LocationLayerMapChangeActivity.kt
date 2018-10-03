package com.mapbox.mapboxsdk.plugins.testapp.activity.location

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.plugins.testapp.Utils

import kotlinx.android.synthetic.main.activity_location_layer_map_change.*

class LocationLayerMapChangeActivity : AppCompatActivity(), OnMapReadyCallback {

    private var locationPlugin: LocationLayerPlugin? = null
    private var mapboxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_layer_map_change)
        mapView.setStyleUrl(Utils.nextStyle)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fabStyles.setOnClickListener{
            mapboxMap?.setStyleUrl(Utils.nextStyle)
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        locationPlugin = LocationLayerPlugin(mapView, mapboxMap)
        locationPlugin?.let {
            it.renderMode = RenderMode.COMPASS
            lifecycle.addObserver(it)
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
}