package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.plugins.offline.MapboxOffline;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity showing a form to configure the download of an offline region.
 */
public class OfflineDownloadActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offline_download);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.launch_offline_region_selector_button)
  public void onOfflineRegionSelectorButtonClicked() {
    Intent intent = new MapboxOffline.IntentBuilder()
      .build(this);
    startActivity(intent);
  }
}