package com.mapbox.mapboxsdk.plugins.testapp.activity.scalebar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.pluginscalebar.ScaleBarManager
import com.mapbox.pluginscalebar.ScaleBarOption
import kotlinx.android.synthetic.main.activity_scalebar.*

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
            }
        }
    }

    private fun addScalebar(mapboxMap: MapboxMap) {
        val scaleBarManager = ScaleBarManager(mapView, mapboxMap)
        val scaleBarOption = ScaleBarOption(this)
        scaleBarOption
                .setTextColor(this.resources.getColor(R.color.mapboxRed))
                .setTextSize(8f)
                .setBarHeight(5f)
                .setBorderWidth(2f)
                .setMetricUnit(true)
                .setRefreshDuration(100)
                .setMarginTop(5f)
                .setMarginLeft(6f)
                .setTextBarMargin(5f)

        scaleBarManager.create(scaleBarOption)
        fabScaleWidget.setOnClickListener {
            scaleBarManager.isEnabled = !scaleBarManager.isEnabled
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
