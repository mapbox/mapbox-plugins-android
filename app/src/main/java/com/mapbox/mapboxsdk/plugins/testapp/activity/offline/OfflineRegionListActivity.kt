package com.mapbox.mapboxsdk.plugins.testapp.activity.offline

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.offline.OfflineManager
import com.mapbox.mapboxsdk.offline.OfflineRegion
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils
import com.mapbox.mapboxsdk.plugins.testapp.R
import java.util.*

/**
 * Activity showing a list of offline regions.
 */
class OfflineRegionListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var adapter: OfflineRegionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_region_list)

        val listView = findViewById<ListView>(R.id.listView)
        adapter = OfflineRegionAdapter()
        listView.adapter = adapter
        listView.emptyView = findViewById(android.R.id.empty)
        listView.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val region = adapter.getItem(position)
        val intent = Intent(this, OfflineRegionDetailActivity::class.java)
        intent.putExtra(OfflineRegionDetailActivity.KEY_REGION_ID_BUNDLE, region.id)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        loadOfflineRegions()
    }

    private fun loadOfflineRegions() {
        OfflineManager.getInstance(this).listOfflineRegions(object : OfflineManager.ListOfflineRegionsCallback {
            override fun onList(offlineRegions: Array<OfflineRegion>?) {
                if (offlineRegions != null) {
                    adapter.setOfflineRegions(Arrays.asList(*offlineRegions))
                }
            }

            override fun onError(error: String) {
                Toast.makeText(this@OfflineRegionListActivity, "Error loading regions $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private class OfflineRegionAdapter : BaseAdapter() {
        private var offlineRegions: List<OfflineRegion> = ArrayList()
        private var currentRegion: OfflineRegion? = null

        init {
            offlineRegions
        }

        internal fun setOfflineRegions(offlineRegions: List<OfflineRegion>) {
            this.offlineRegions = offlineRegions
            notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return offlineRegions.size
        }

        override fun getItem(position: Int): OfflineRegion {
            return offlineRegions[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View? {
            var convertView = view
            val holder: ViewHolder

            if (convertView == null) {
                holder = ViewHolder()
                convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_feature, parent, false)
                holder.text = convertView?.findViewById(R.id.nameView)
                holder.subText = convertView.findViewById(R.id.descriptionView)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            currentRegion = getItem(position)
            currentRegion?.metadata?.let {
                holder.text?.text = OfflineUtils.convertRegionName(it)
            }
            holder.subText?.text = (currentRegion?.definition as OfflineRegionDefinition).styleURL
            return convertView
        }

        internal class ViewHolder {
            var text: TextView? = null
            var subText: TextView? = null
        }
    }
}