package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.plugins.testapp.R;

import butterknife.ButterKnife;

public class AutocompleteFragmentActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_fragment);
    ButterKnife.bind(this);

  }
}
