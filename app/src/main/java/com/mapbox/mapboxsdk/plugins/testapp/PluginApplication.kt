package com.mapbox.mapboxsdk.plugins.testapp

import android.app.Application

import com.mapbox.mapboxsdk.Mapbox
import com.squareup.leakcanary.LeakCanary

import timber.log.Timber

class PluginApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
        initializeLogger()
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }

    private fun initializeLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
