package com.mapbox.mapboxsdk.plugins.testapp.activity.places

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import com.mapbox.mapboxsdk.plugins.testapp.R

import kotlinx.android.synthetic.main.activity_picker_launcher.*

class PickerLauncherActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_launcher)
        fabLocationPicker.setOnClickListener { _ ->
            Mapbox.getAccessToken()?.let {
                startActivityForResult(
                        PlacePicker.IntentBuilder()
                                .accessToken(it)
                                .placeOptions(PlacePickerOptions.builder()
                                        .statingCameraPosition(CameraPosition.Builder()
                                                .target(LatLng(40.7544, -73.9862))
                                                .zoom(16.0)
                                                .build())
                                        .build())
                                .build(this), REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val carmenFeature = PlacePicker.getPlace(data)
            Toast.makeText(this, carmenFeature?.placeName(), Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val REQUEST_CODE = 5678
    }
}
