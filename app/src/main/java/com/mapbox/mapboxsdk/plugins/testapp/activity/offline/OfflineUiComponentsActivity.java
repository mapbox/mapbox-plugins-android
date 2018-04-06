package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin;
import com.mapbox.mapboxsdk.plugins.offline.OfflineRegionSelector;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfflineUiComponentsActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 9384;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offline_ui_components);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.launch_offline_region_selector_button)
  public void onOfflineRegionSelectorButtonClicked() {
    Intent intent = new OfflineRegionSelector.IntentBuilder()
      .build(this);
    startActivityForResult(intent, REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

      NotificationOptions.Builder builder = NotificationOptions.builder(this)
        .contentTitle("Downloading: ")
        .returnActivity(OfflineUiComponentsActivity.class.getName())
        .smallIconRes(android.R.drawable.stat_sys_download);

      if (OfflineRegionSelector.getRegionName(data) != null) {
        builder.contentText(OfflineRegionSelector.getRegionName(data));
      }

      OfflineDownloadOptions options = OfflineRegionSelector.getOfflineDownloadOptions(data, builder.build());
      OfflinePlugin.getInstance(this).startDownload(options);

      Toast.makeText(this,
        String.format(Locale.US, "Region name: %s", OfflineRegionSelector.getRegionName(data)),
        Toast.LENGTH_LONG).show();
    } else if (resultCode == Activity.RESULT_CANCELED) {
      Toast.makeText(this, "user canceled out of region selector", Toast.LENGTH_LONG)
        .show();
    }
  }
}
