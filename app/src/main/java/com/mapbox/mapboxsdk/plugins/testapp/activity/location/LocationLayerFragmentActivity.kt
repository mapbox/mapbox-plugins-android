package com.mapbox.mapboxsdk.plugins.testapp.activity.location

import android.app.Fragment
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.testapp.R
import kotlinx.android.synthetic.main.activity_location_layer_fragment.*

class LocationLayerFragmentActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_location_layer_fragment)

    if (savedInstanceState == null) {
      fragmentManager
        .beginTransaction()
        .replace(R.id.container, LocationLayerFragment.newInstance(), LocationLayerFragment.TAG)
        .commit()
    }

    fab.setOnClickListener {
      val fragment = fragmentManager.findFragmentByTag(EmptyFragment.TAG)
      if (fragment == null) {
        fragmentManager
          .beginTransaction()
          .replace(R.id.container, EmptyFragment.newInstance(), EmptyFragment.TAG)
          .addToBackStack("transaction2")
          .commit()
      } else {
        this.onBackPressed()
      }
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (!PermissionsManager.areLocationPermissionsGranted(this)) {
      val permissionsManager = PermissionsManager(object : PermissionsListener {
        override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
          // no impl
        }

        override fun onPermissionResult(granted: Boolean) {
          if (!granted) {
            finish()
          }
        }
      })
      permissionsManager.requestLocationPermissions(this)
    }
  }

  class LocationLayerFragment : Fragment(), LocationEngineListener {
    companion object {
      const val TAG = "LLFragment"
      fun newInstance(): LocationLayerFragment {
        return LocationLayerFragment()
      }
    }

    lateinit var mapView: MapView
    lateinit var mapboxMap: MapboxMap
    var plugin: LocationLayerPlugin? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      mapView = MapView(inflater.context)
      return mapView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
      mapView.onCreate(savedInstanceState)
      mapView.getMapAsync {
        mapboxMap = it
        plugin = LocationLayerPlugin(mapView, mapboxMap)
        plugin?.locationEngine?.addLocationEngineListener(this)
      }
    }

    override fun onLocationChanged(location: Location?) {
      if (location != null) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location), 12.0))
        plugin?.locationEngine?.removeLocationEngineListener(this)
      }
    }

    override fun onConnected() {
      // no impl
    }

    override fun onStart() {
      super.onStart()
      mapView.onStart()
      plugin?.onStart()
    }

    override fun onResume() {
      super.onResume()
      mapView.onResume()
    }

    override fun onPause() {
      super.onPause()
      mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
      super.onSaveInstanceState(outState)
      mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
      super.onStop()
      plugin?.onStop()
      mapView.onStop()
    }

    override fun onLowMemory() {
      super.onLowMemory()
      mapView.onLowMemory()
    }

    override fun onDestroyView() {
      super.onDestroyView()
      mapView.onDestroy()
      plugin?.locationEngine?.removeLocationEngineListener(this)
    }
  }

  class EmptyFragment : Fragment() {
    companion object {
      const val TAG = "EmptyFragment"
      fun newInstance(): EmptyFragment {
        return EmptyFragment()
      }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
      val textView = TextView(inflater?.context)
      textView.text = "This is an empty Fragment"
      return textView
    }
  }
}
