package com.mapbox.mapboxsdk.plugins.offline.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.mapbox.mapboxsdk.offline.R;
import com.mapbox.mapboxsdk.plugins.offline.utils.ColorUtils;

public class OfflineActivity extends AppCompatActivity {

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

      fragment = RegionSelectionFragment.newInstance();

      getSupportFragmentManager().beginTransaction()
        .add(R.id.fragment_container, fragment, RegionSelectionFragment.TAG).commit();
    }
  }
}
