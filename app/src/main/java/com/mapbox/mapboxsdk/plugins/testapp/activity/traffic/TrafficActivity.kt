package com.mapbox.mapboxsdk.plugins.testapp.activity.traffic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.plugins.testapp.Utils
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin

import kotlinx.android.synthetic.main.activity_traffic.*
import timber.log.Timber

/**
 * Activity showcasing TrafficPlugin plugin integration
 */
class TrafficActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapboxMap: MapboxMap? = null
    private var trafficPlugin: TrafficPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fabTraffic.setOnClickListener { _ ->
            trafficPlugin?.let {
                it.setVisibility(!it.isVisible)
                Timber.d("Traffic plugin is enabled : %s", it.isVisible)
            }
        }

        fabStyles.setOnClickListener {
            mapboxMap?.setStyle(Style.Builder().fromUri(Utils.nextStyle))
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            this.trafficPlugin = TrafficPlugin(mapView, mapboxMap, it)
            this.trafficPlugin?.setVisibility(true) // Enable the traffic view by default
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

    fun getTrafficPlugin(): TrafficPlugin? {
        return trafficPlugin
    }
}