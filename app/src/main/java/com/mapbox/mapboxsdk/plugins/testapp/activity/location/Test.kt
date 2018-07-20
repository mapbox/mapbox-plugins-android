package com.mapbox.mapboxsdk.plugins.testapp.activity.location

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.maps.MapFragment
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.testapp.R

class Test : AppCompatActivity(), MapFragment.OnMapViewReadyCallback {
  private lateinit var mapView: MapView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test)

    var mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentByTag("tag") as SupportMapFragment?
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance()
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, mapFragment, "tag")
        .commit()
    }

    mapFragment?.getMapAsync {
      val plugin = LocationLayerPlugin(mapView, it)
      lifecycle.addObserver(plugin)
      it.addOnMapClickListener {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, BlankFragment.newInstance())
          .addToBackStack("transaction")
          .commit()
      }
    }
  }

  override fun onMapViewReady(mapView: MapView?) {
    this.mapView = mapView!!
  }
}
