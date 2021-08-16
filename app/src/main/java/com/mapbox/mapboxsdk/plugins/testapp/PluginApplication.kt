package com.mapbox.mapboxsdk.plugins.testapp

import androidx.multidex.MultiDexApplication

import com.mapbox.mapboxsdk.Mapbox

import timber.log.Timber

class PluginApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }

    private fun initializeLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
