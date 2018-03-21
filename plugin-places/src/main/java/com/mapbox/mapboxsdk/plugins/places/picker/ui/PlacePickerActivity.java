package com.mapbox.mapboxsdk.plugins.places.picker.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.places.R;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;
import com.mapbox.mapboxsdk.plugins.places.common.utils.ColorUtils;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker.IntentBuilder;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.viewmodel.PlacePickerViewModel;

import java.util.Locale;

import timber.log.Timber;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * Do not use this class directly, instead create an intent using the {@link IntentBuilder} inside
 * the {@link PlacePicker} class.
 *
 * @since 0.2.0
 */
public class PlacePickerActivity extends AppCompatActivity implements OnMapReadyCallback,
  MapboxMap.OnCameraMoveStartedListener, MapboxMap.OnCameraIdleListener, Observer<CarmenFeature> {

  CurrentPlaceSelectionBottomSheet bottomSheet;
  CarmenFeature carmenFeature;
  private PlacePickerViewModel viewModel;
  private PlacePickerOptions options;
  private ImageView markerImage;
  private MapboxMap mapboxMap;
  private String accessToken;
  private MapView mapView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Hide any toolbar an apps theme might automatically place in activities. Typically creating an
    // activity style would cover this issue but this seems to prevent us from getting the users
    // application colorPrimary color.
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    getSupportActionBar().hide();
    setContentView(R.layout.mapbox_activity_place_picker);

    if (savedInstanceState == null) {
      accessToken = getIntent().getStringExtra(PlaceConstants.ACCESS_TOKEN);
      options = getIntent().getParcelableExtra(PlaceConstants.PLACE_OPTIONS);
    }

    // Initialize the view model.
    viewModel = ViewModelProviders.of(this).get(PlacePickerViewModel.class);
    viewModel.getResults().observe(this, this);

    bindViews();
    addBackButtonListener();
    addChosenLocationButton();
    customizeViews();

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  private void addBackButtonListener() {
    ImageView backButton = findViewById(R.id.mapbox_place_picker_toolbar_back_button);
    backButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void bindViews() {
    mapView = findViewById(R.id.map_view);
    bottomSheet = findViewById(R.id.mapbox_plugins_picker_bottom_sheet);
    markerImage = findViewById(R.id.mapbox_plugins_image_view_marker);
  }

  private void customizeViews() {
    ConstraintLayout toolbar = findViewById(R.id.place_picker_toolbar);
    if (options != null && options.toolbarColor() != null) {
      toolbar.setBackgroundColor(options.toolbarColor());
    } else {
      int color = ColorUtils.getMaterialColor(this, R.attr.colorPrimary);
      toolbar.setBackgroundColor(color);
    }
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;

    if (options != null) {
      if (options.startingBounds() != null) {
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(options.startingBounds(), 0));
      } else if (options.statingCameraPosition() != null) {
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(options.statingCameraPosition()));
      }
    }

    // Initialize with the markers current location information.
    makeReverseGeocodingSearch();

    mapboxMap.addOnCameraMoveStartedListener(this);
    mapboxMap.addOnCameraIdleListener(this);
  }

  @Override
  public void onCameraMoveStarted(int reason) {
    Timber.v("Map camera has begun moving.");
    if (markerImage.getTranslationY() == 0) {
      markerImage.animate().yBy(-75)
        .setInterpolator(new OvershootInterpolator()).setDuration(250).start();
      if (bottomSheet.isShowing()) {
        bottomSheet.dismissPlaceDetails();
      }
    }
  }

  @Override
  public void onCameraIdle() {
    Timber.v("Map camera is now idling.");
    markerImage.animate().yBy(Math.abs(markerImage.getTranslationY()))
      .setInterpolator(new OvershootInterpolator()).setDuration(250).start();
    bottomSheet.setPlaceDetails(null);
    makeReverseGeocodingSearch();
  }

  @Override
  public void onChanged(@Nullable CarmenFeature carmenFeature) {
    if (carmenFeature == null) {
      carmenFeature = CarmenFeature.builder().placeName(
        String.format(Locale.US, "[%f, %f]",
          mapboxMap.getCameraPosition().target.getLatitude(),
          mapboxMap.getCameraPosition().target.getLongitude())
      ).text("No address found").properties(new JsonObject()).build();
    }
    this.carmenFeature = carmenFeature;
    bottomSheet.setPlaceDetails(carmenFeature);
  }

  private void makeReverseGeocodingSearch() {
    LatLng latLng = mapboxMap.getCameraPosition().target;
    viewModel.reverseGeocode(
      Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()),
      accessToken, options
    );
  }

  private void addChosenLocationButton() {
    FloatingActionButton placeSelectedButton = findViewById(R.id.place_chosen_button);
    placeSelectedButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (carmenFeature == null) {
          Snackbar.make(bottomSheet,
            getString(R.string.mapbox_plugins_place_picker_not_valid_selection),
            LENGTH_LONG).show();
          return;
        }
        placeSelected();
      }
    });
  }

  void placeSelected() {
    String json = carmenFeature.toJson();
    Intent returningIntent = new Intent();
    returningIntent.putExtra(PlaceConstants.RETURNING_CARMEN_FEATURE, json);
    returningIntent.putExtra(PlaceConstants.MAP_CAMERA_POSITION, mapboxMap.getCameraPosition());
    setResult(AppCompatActivity.RESULT_OK, returningIntent);
    finish();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}
