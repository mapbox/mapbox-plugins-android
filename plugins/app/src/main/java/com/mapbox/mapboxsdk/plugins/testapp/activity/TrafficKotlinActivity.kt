package com.mapbox.mapboxsdk.plugins.testapp.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.plugins.traffic.kotlin.TrafficPluginKt

class TrafficKotlinActivity : AppCompatActivity() {

    lateinit private var mapView: MapView
    lateinit private var mapboxMap: MapboxMap
    lateinit private var stylesFab: FloatingActionButton
    lateinit private var trafficFab: FloatingActionButton
    lateinit private var trafficPlugin: TrafficPluginKt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        mapView = findViewById(R.id.mapView) as MapView
        stylesFab = findViewById(R.id.fabStyles) as FloatingActionButton
        stylesFab.setOnClickListener {
            mapboxMap.setStyleUrl(StyleCycle.obtainNextStyle())
        }
        trafficFab = findViewById(R.id.fabTraffic) as FloatingActionButton
        trafficFab.setOnClickListener {
            trafficPlugin.toggle()
        }
        mapView.setStyleUrl(StyleCycle.obtainStyle())
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            trafficPlugin = TrafficPluginKt(mapView, mapboxMap)
            trafficPlugin.toggle()
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

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState!!)
    }

    private object StyleCycle {
        val STYLES = listOf(
                Style.MAPBOX_STREETS,
                Style.OUTDOORS,
                Style.LIGHT,
                Style.DARK,
                Style.SATELLITE_STREETS
        )
        var index = 0

        fun obtainNextStyle(): String {
            index++
            if (index == STYLES.size) {
                index = 0
            }
            return obtainStyle()
        }

        fun obtainStyle() = STYLES[index]
    }
}
