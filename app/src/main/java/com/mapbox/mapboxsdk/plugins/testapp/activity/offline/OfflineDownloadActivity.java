package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.plugins.offline.MapboxOffline;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity showing a form to configure the download of an offline region.
 */
public class OfflineDownloadActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 9384;

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
    startActivityForResult(intent, REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
      Toast.makeText(this,
        String.format(Locale.US, "Region name: %s", MapboxOffline.getRegionName(data)),
        Toast.LENGTH_LONG).show();
    } else if (resultCode == Activity.RESULT_CANCELED) {
      Toast.makeText(this, "user canceled out of region selector", Toast.LENGTH_LONG)
        .show();
    }


  }
}