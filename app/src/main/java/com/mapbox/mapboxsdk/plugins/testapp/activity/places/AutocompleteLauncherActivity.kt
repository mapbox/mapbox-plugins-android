package com.mapbox.mapboxsdk.plugins.testapp.activity.places

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.JsonObject
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.exceptions.MapboxConfigurationException
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.testapp.R
import kotlinx.android.synthetic.main.activity_places_launcher.*

class AutocompleteLauncherActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var home: CarmenFeature
    private lateinit var work: CarmenFeature
    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_launcher)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {

        this.mapboxMap = mapboxMap

        mapboxMap.setStyle(Style.MAPBOX_STREETS) {

            addUserLocations()

            fabCard.setOnClickListener {
                val intent = PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken()
                                ?: throw MapboxConfigurationException())
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(this@AutocompleteLauncherActivity)
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
            }

            fabCard.setOnLongClickListener {
                PlaceAutocomplete.clearRecentHistory(this)
                Toast.makeText(this, "database cleared", Toast.LENGTH_LONG).show()
                true
            }

            fabFullScreen.setOnClickListener {
                val intent = PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken()
                                ?: throw MapboxConfigurationException())
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.WHITE)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .statusbarColor(Color.MAGENTA)
                                .build())
                        .build(this@AutocompleteLauncherActivity)
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
            }
        }
    }

    private fun addUserLocations() {
        home = CarmenFeature.builder().text("Directions to Home")
                .geometry(Point.fromLngLat(-77.015665, 38.8996419))
                .placeName("300 Massachusetts Ave NW")
                .id("directions-home")
                .properties(JsonObject())
                .build()

        work = CarmenFeature.builder().text("Directions to Work")
                .placeName("740 15th St NW")
                .geometry(Point.fromLngLat(-77.03389, 38.89985))
                .id("directions-work")
                .properties(JsonObject())
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            val feature = PlaceAutocomplete.getPlace(data)
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show()

            // Retrieve selected location's CarmenFeature
            val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)

            // Move map camera to the selected location
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                    .target(LatLng((selectedCarmenFeature.geometry() as Point).latitude(),
                            (selectedCarmenFeature.geometry() as Point).longitude()))
                    .zoom(15.5)
                    .build()), 3000)
        }
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
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    companion object {
        private val REQUEST_CODE_AUTOCOMPLETE = 1
    }
}
