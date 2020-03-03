package com.mapbox.mapboxsdk.plugins.testapp.activity.ktx.maps

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.maps.queryRenderedFeatures
import com.mapbox.mapboxsdk.plugins.testapp.R
import kotlinx.android.synthetic.main.activity_maps_ktx.*

class MapboxKtxActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private var mapboxMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_ktx)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            mapboxMap.addOnMapClickListener(this)
            Toast.makeText(this, "Click on the map", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapClick(point: LatLng): Boolean {
        val features = mapboxMap?.queryRenderedFeatures(point)
        features?.first().let {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
        return true
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
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
        mapboxMap?.removeOnMapClickListener(this)
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}