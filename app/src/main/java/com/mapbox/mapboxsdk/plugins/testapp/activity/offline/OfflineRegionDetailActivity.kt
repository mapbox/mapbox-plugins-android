package com.mapbox.mapboxsdk.plugins.testapp.activity.offline

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.offline.OfflineManager
import com.mapbox.mapboxsdk.offline.OfflineRegion
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.KEY_BUNDLE
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadChangeListener
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils
import com.mapbox.mapboxsdk.plugins.testapp.R
import kotlinx.android.synthetic.main.activity_offline_region_detail.*
import timber.log.Timber

/**
 * Activity showing the detail of an offline region.
 *
 *
 * This Activity can bind to the OfflineDownloadService and show progress.
 *
 *
 *
 * This Activity listens to broadcast events related to successful, canceled and errored download.
 *
 */
class OfflineRegionDetailActivity : AppCompatActivity(), OfflineDownloadChangeListener {

    private var offlinePlugin: OfflinePlugin? = null
    private var offlineRegion: OfflineRegion? = null
    private var isDownloading: Boolean = false

    /**
     * Callback invoked when the states of an offline region changes.
     */
    private val offlineRegionStatusCallback = object : OfflineRegion.OfflineRegionStatusCallback {
        override fun onStatus(status: OfflineRegionStatus) {
            isDownloading = !status.isComplete
            updateFab()
        }

        override fun onError(error: String) {
            Toast.makeText(
                    this@OfflineRegionDetailActivity,
                    "Error getting offline region state: $error",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private val offlineRegionDeleteCallback = object : OfflineRegion.OfflineRegionDeleteCallback {
        override fun onDelete() {
            Toast.makeText(
                    this@OfflineRegionDetailActivity,
                    "Region deleted.",
                    Toast.LENGTH_SHORT).show()
            finish()
        }

        override fun onError(error: String) {
            fabDelete.isEnabled = true
            Toast.makeText(
                    this@OfflineRegionDetailActivity,
                    "Error getting offline region state: $error",
                    Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_region_detail)
        mapView?.onCreate(savedInstanceState)
        offlinePlugin = OfflinePlugin.getInstance(this)

        val bundle = intent.extras
        if (bundle != null) {
            loadOfflineDownload(bundle)
        }

        fabDelete.setOnClickListener { onFabClick(it) }
    }

    private fun loadOfflineDownload(bundle: Bundle) {
        val regionId: Long
        val offlineDownload = bundle.getParcelable<OfflineDownloadOptions>(KEY_BUNDLE)
        regionId = if (offlineDownload != null) {
            // coming from notification
            offlineDownload.uuid()
        } else {
            // coming from list
            bundle.getLong(KEY_REGION_ID_BUNDLE, -1)
        }

        if (regionId != -1L) {
            loadOfflineRegion(regionId)
        }
    }

    private fun loadOfflineRegion(id: Long) {
        OfflineManager.getInstance(this)
                .listOfflineRegions(object : OfflineManager.ListOfflineRegionsCallback {

                    override fun onList(offlineRegions: Array<OfflineRegion>) {
                        for (region in offlineRegions) {
                            if (region.id == id) {
                                offlineRegion = region
                                val definition = region.definition as OfflineRegionDefinition
                                setupUI(definition)
                                return
                            }
                        }
                    }

                    override fun onError(error: String) {
                        Timber.e(error)
                    }
                })
    }

    private fun updateFab() {
        if (isDownloading) {
            fabDelete.setImageResource(R.drawable.ic_cancel)
            regionState.text = "DOWNLOADING"
        } else {
            fabDelete.setImageResource(R.drawable.ic_delete)
            regionState.text = "DOWNLOADED"
        }
    }

    private fun setupUI(definition: OfflineRegionDefinition) {
        // update map
        mapView?.getMapAsync { mapboxMap ->
            // correct style
            mapboxMap.setOfflineRegionDefinition(definition) { _ ->
                // restrict camera movement
                mapboxMap.setLatLngBoundsForCameraTarget(definition.bounds)

                // update textview data
                offlineRegion?.metadata?.let {
                    regionName.text = OfflineUtils.convertRegionName(it)
                }
                regionStyleUrl.text = definition.styleURL
                regionLatLngBounds.text = definition.bounds.toString()
                regionMinZoom.text = definition.minZoom.toString()
                regionMaxZoom.text = definition.maxZoom.toString()
                offlineRegion?.getStatus(offlineRegionStatusCallback)
            }
        }
    }

    fun onFabClick(view: View) {
        if (offlineRegion != null) {
            if (!isDownloading) {
                // delete download
                offlineRegion?.delete(offlineRegionDeleteCallback)
            } else {
                // cancel download
                val offlineDownload = offlinePlugin?.getActiveDownloadForOfflineRegion(offlineRegion)
                if (offlineDownload != null) {
                    offlinePlugin?.cancelDownload(offlineDownload)
                    isDownloading = false
                }
            }
            view.visibility = View.GONE
        }
    }

    override fun onCreate(offlineDownload: OfflineDownloadOptions) {
        Timber.e("OfflineDownloadOptions created %s", offlineDownload.hashCode())
    }

    override fun onSuccess(offlineDownload: OfflineDownloadOptions) {
        isDownloading = false
        regionStateProgress.visibility = View.INVISIBLE
        updateFab()
    }

    override fun onCancel(offlineDownload: OfflineDownloadOptions) {
        finish() // nothing to do in this screen, cancel = delete
    }

    override fun onError(offlineDownload: OfflineDownloadOptions, error: String, message: String) {
        regionStateProgress.visibility = View.INVISIBLE
        regionState.text = "ERROR"
        Toast.makeText(this, error + message, Toast.LENGTH_LONG).show()
    }

    override fun onProgress(offlineDownload: OfflineDownloadOptions, progress: Int) {
        if (offlineRegion == null) {
            return
        }

        if (offlineDownload.uuid() == offlineRegion?.id) {
            if (regionStateProgress.visibility != View.VISIBLE) {
                regionStateProgress.visibility = View.VISIBLE
            }
            isDownloading = true
            regionStateProgress.progress = progress
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        offlinePlugin?.addOfflineDownloadStateChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
        offlinePlugin?.removeOfflineDownloadStateChangeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    companion object {

        val KEY_REGION_ID_BUNDLE = "com.mapbox.mapboxsdk.plugins.offline.bundle.id"
    }
}