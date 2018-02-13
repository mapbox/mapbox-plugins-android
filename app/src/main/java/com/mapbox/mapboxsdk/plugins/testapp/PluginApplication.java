package com.mapbox.mapboxsdk.plugins.testapp;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class PluginApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }

    LeakCanary.install(this);
    initializeLogger();
    Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
  }

  private void initializeLogger() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }
}
