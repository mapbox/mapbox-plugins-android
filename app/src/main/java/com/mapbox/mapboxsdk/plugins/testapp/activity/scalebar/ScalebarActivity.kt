package com.mapbox.mapboxsdk.plugins.testapp.activity.scalebar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.turf.TurfMeasurement
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.pluginscalebar.ScaleBarOptions
import com.mapbox.pluginscalebar.ScaleBarPlugin
import kotlinx.android.synthetic.main.activity_scalebar.*

import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

import com.mapbox.mapboxsdk.style.layers.LineLayer

import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.turf.TurfConstants

/**
 * Activity showing a scalebar used on a MapView.
 */
class ScalebarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scalebar)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                addScalebar(mapboxMap)
                setupTestLine(it)
            }
        }
    }

    private fun addScalebar(mapboxMap: MapboxMap) {
        val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
        val scaleBarOptions = ScaleBarOptions(this)
        scaleBarOptions
                .setTextColor(android.R.color.black)
                .setTextSize(40f)
                .setBarHeight(5f)
                .setBorderWidth(2f)
                .setRefreshInterval(15)
                .setMarginTop(15f)
                .setMarginLeft(16f)
                .setTextBarMargin(15f)
                .setMaxWidthRatio(0.5f)
                .setShowTextBorder(true)
                .setTextBorderWidth(5f)

        scaleBarPlugin.create(scaleBarOptions)
        fabScaleWidget.setOnClickListener {
            scaleBarPlugin.isEnabled = !scaleBarPlugin.isEnabled
        }
    }

    private fun setupTestLine(style: Style) {
        val source = GeoJsonSource("source-id")
        val lineLayer = LineLayer("layer-id", source.id)
        val startPoint: Point = Point.fromLngLat(-122.447244, 37.769145)
        val endPoint: Point = TurfMeasurement.destination(startPoint, 200.0, 90.0, TurfConstants.UNIT_METERS)
        val pointList: List<Point> = listOf(startPoint, endPoint)
        source.setGeoJson(LineString.fromLngLats(pointList))
        style.addSource(source)
        style.addLayer(lineLayer)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
