package com.mapbox.mapboxsdk.plugins.offline.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.OfflinePluginConstants;
import com.mapbox.mapboxsdk.plugins.offline.R;
import com.mapbox.mapboxsdk.plugins.offline.model.RegionSelectionOptions;
import com.mapbox.mapboxsdk.plugins.offline.utils.ColorUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.RETURNING_DEFINITION;
import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.RETURNING_REGION_NAME;

public class OfflineActivity extends AppCompatActivity implements RegionSelectedCallback {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Hide any toolbar an apps theme might automatically place in activities. Typically creating an
    // activity style would cover this issue but this seems to prevent us from getting the users
    // application colorPrimary color.
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    getSupportActionBar().hide();
    setContentView(R.layout.mapbox_offline_activity);

    ConstraintLayout toolbar = findViewById(R.id.place_picker_toolbar);
    int color = ColorUtils.getMaterialColor(this, R.attr.colorPrimary);
    toolbar.setBackgroundColor(color);

    // Add autocomplete fragment if this is first creation
    if (savedInstanceState == null) {

      RegionSelectionFragment fragment;

      RegionSelectionOptions regionSelectionOptions
        = getIntent().getParcelableExtra(OfflinePluginConstants.REGION_SELECTION_OPTIONS);
      if (regionSelectionOptions != null) {
        fragment = RegionSelectionFragment.newInstance(regionSelectionOptions);
      } else {
        fragment = RegionSelectionFragment.newInstance();
      }

      getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment, RegionSelectionFragment.TAG).commit();

      fragment.setSelectedCallback(this);
    }
  }

  @Override
  public void onSelected(OfflineRegionDefinition definition, String regionName) {
    Intent returningIntent = new Intent();
    returningIntent.putExtra(RETURNING_DEFINITION, definition);
    returningIntent.putExtra(RETURNING_REGION_NAME, regionName);
    setResult(AppCompatActivity.RESULT_OK, returningIntent);
    finish();
  }

  @Override
  public void onCancel() {
    setResult(AppCompatActivity.RESULT_CANCELED);
    finish();
  }
}
