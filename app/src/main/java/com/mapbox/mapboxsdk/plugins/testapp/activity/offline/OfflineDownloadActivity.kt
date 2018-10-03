package com.mapbox.mapboxsdk.plugins.testapp.activity.offline

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast

import com.mapbox.mapboxsdk.constants.MapboxConstants
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils
import com.mapbox.mapboxsdk.plugins.testapp.R

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_offline_download.*

/**
 * Activity showing a form to configure the download of an offline region.
 */
class OfflineDownloadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_download)
        initUi()
        initSeekbarListeners()
        fabStartDownload.setOnClickListener { onDownloadRegion() }
    }

    private fun initUi() {
        initEditTexts()
        initSeekbars()
        initSpinner()
        initZoomLevelTextviews()
    }

    private fun initEditTexts() {
        editTextRegionName.setText("Region name")
        editTextLatNorth.setText("40.7589372691904")
        editTextLonEast.setText("-73.96024123810196")
        editTextLatSouth.setText("40.740763489055496")
        editTextLonWest.setText("-73.97569076188057")
    }

    private fun initSeekbars() {
        val maxZoom = MapboxConstants.MAXIMUM_ZOOM.toInt()
        seekbarMinZoom.max = maxZoom
        seekbarMinZoom.progress = 16
        seekbarMaxZoom.max = maxZoom
        seekbarMaxZoom.progress = 19
    }

    private fun initSpinner() {
        val styles = ArrayList<String>()
        styles.add(Style.MAPBOX_STREETS)
        styles.add(Style.DARK)
        styles.add(Style.LIGHT)
        styles.add(Style.OUTDOORS)
        val spinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, styles)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStyleUrl.adapter = spinnerArrayAdapter
    }

    private fun initZoomLevelTextviews() {
        textViewMaxText.text = getString(R.string.max_zoom_textview, seekbarMaxZoom.progress)
        textViewMinText.text = getString(R.string.min_zoom_textview, seekbarMinZoom.progress)
    }

    private fun initSeekbarListeners() {
        seekbarMaxZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textViewMaxText.text = getString(R.string.max_zoom_textview, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        seekbarMinZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textViewMinText.text = getString(R.string.min_zoom_textview, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    fun onDownloadRegion() {
        // get data from UI
        val regionName = editTextRegionName.text.toString()
        val latitudeNorth = editTextLatNorth.text.toString().toDouble()
        val longitudeEast = editTextLonEast.text.toString().toDouble()
        val latitudeSouth = editTextLatSouth.text.toString().toDouble()
        val longitudeWest = editTextLonWest.text.toString().toDouble()
        val styleUrl = spinnerStyleUrl.selectedItem as String
        val maxZoom = seekbarMaxZoom.progress.toFloat()
        val minZoom = seekbarMinZoom.progress.toFloat()

        if (!validCoordinates(latitudeNorth, longitudeEast, latitudeSouth, longitudeWest)) {
            Toast.makeText(this, "coordinates need to be in valid range", Toast.LENGTH_LONG).show()
            return
        }

        // create offline definition from data
        val definition = OfflineTilePyramidRegionDefinition(
                styleUrl,
                LatLngBounds.Builder()
                        .include(LatLng(latitudeNorth, longitudeEast))
                        .include(LatLng(latitudeSouth, longitudeWest))
                        .build(),
                minZoom.toDouble(),
                maxZoom.toDouble(),
                resources.displayMetrics.density
        )

        // customize notification appearance
        val notificationOptions = NotificationOptions.builder(this)
                .smallIconRes(R.drawable.mapbox_logo_icon)
                .returnActivity(OfflineRegionDetailActivity::class.java.name)
                .build()

        // start offline download
        OfflinePlugin.getInstance(this).startDownload(
                OfflineDownloadOptions.builder()
                        .definition(definition)
                        .metadata(OfflineUtils.convertRegionName(regionName))
                        .notificationOptions(notificationOptions)
                        .build()
        )
    }

    private fun validCoordinates(latitudeNorth: Double, longitudeEast: Double, latitudeSouth: Double,
                                 longitudeWest: Double): Boolean {
        if (latitudeNorth < -90 || latitudeNorth > 90) {
            return false
        } else if (longitudeEast < -180 || longitudeEast > 180) {
            return false
        } else if (latitudeSouth < -90 || latitudeSouth > 90) {
            return false
        } else if (longitudeWest < -180 || longitudeWest > 180) {
            return false
        }
        return true
    }
}