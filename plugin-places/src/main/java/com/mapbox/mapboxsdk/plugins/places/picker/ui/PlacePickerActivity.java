package com.mapbox.mapboxsdk.plugins.places.picker.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.places.R;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.ClearButtonListener;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.QueryFocusListener;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.QueryListener;
import com.mapbox.mapboxsdk.plugins.places.common.PlaceConstants;
import com.mapbox.mapboxsdk.plugins.places.common.utils.ColorUtils;
import com.mapbox.mapboxsdk.plugins.places.common.utils.KeyboardUtils;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker.IntentBuilder;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.viewmodel.PlacePickerViewModel;

import java.util.List;
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
  MapboxMap.OnCameraMoveStartedListener, MapboxMap.OnMapClickListener,
  MapboxMap.OnCameraIdleListener, Observer<CarmenFeature>,
  PermissionsListener {

  public int COLLAPSED_SEARCH_CARDVIEW_HEIGHT;
  public boolean includeReverseGeocode;
  public boolean includeDeviceLocationButton;
  public Boolean resultsCardViewListIsCollapsed = true;
  private final int EXPANDED_SEARCH_CARDVIEW_HEIGHT = 555;
  private final int SPACE_BETWEEN_RESULTS_LIST_AND_COMPASS = 3;
  private PermissionsManager permissionsManager;
  private CurrentPlaceSelectionBottomSheet bottomSheet;
  private CarmenFeature carmenFeature;
  private PlacePickerViewModel viewModel;
  private PlacePickerOptions options;
  private ImageView markerImage;
  private MapboxMap mapboxMap;
  private String accessToken;
  private MapView mapView;
  private FloatingActionButton userLocationButton;
  private CardView searchCardView;
  private CardView searchResultsCardView;
  private int searchHistoryCount;
  private boolean includeSearch;
  private Integer customToolbarColor;
  private String customMapStyle;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Hide any toolbar an apps theme might automatically place in activities. Typically creating an
    // activity style would cover this issue but this seems to prevent us from getting the users
    // application colorPrimary color.
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }

    COLLAPSED_SEARCH_CARDVIEW_HEIGHT = getToolBarHeight();

    setContentView(R.layout.mapbox_activity_place_picker);

    if (savedInstanceState == null) {
      accessToken = getIntent().getStringExtra(PlaceConstants.ACCESS_TOKEN);
      options = getIntent().getParcelableExtra(PlaceConstants.PLACE_OPTIONS);
      includeReverseGeocode = options.includeReverseGeocode();
      includeDeviceLocationButton = options.includeDeviceLocationButton();
      includeSearch = options.includeSearch();
      customMapStyle = options.mapStyle();
      customToolbarColor = options.toolbarColor();
    }

    // Initialize the view model.
    viewModel = ViewModelProviders.of(this).get(PlacePickerViewModel.class);
    viewModel.getResults().observe(this, this);

    bindViews();
    if (includeSearch) {
      AppBarLayout appBarLayout = findViewById(R.id.place_picker_app_bar_layout);
      appBarLayout.setVisibility(View.GONE);
    } else {
      addBackButtonListener();
      setToolbarColor();
    }
    addPlaceSelectedButton();
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
    userLocationButton = findViewById(R.id.user_location_button);
  }

  private void bindListeners() {
    PlacePickerActivity.this.mapboxMap.addOnCameraMoveStartedListener(PlacePickerActivity.this);
    PlacePickerActivity.this.mapboxMap.addOnCameraIdleListener(PlacePickerActivity.this);
    searchResultsCardView = findViewById(R.id.optional_search_autocomplete_cardview);
  }

  private void setToolbarColor() {
    ConstraintLayout toolbar = findViewById(R.id.place_picker_toolbar);
    toolbar.setBackgroundColor(customToolbarColor != null
        ? customToolbarColor : ColorUtils.getMaterialColor(
            this, R.attr.colorPrimary));
  }

  @Override
  public void onMapReady(final MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.setStyle(new Style.Builder().fromUri(customMapStyle != null
        ? customMapStyle : Style.MAPBOX_STREETS), new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {
            adjustCameraBasedOnOptions();
            if (includeReverseGeocode) {
              // Initialize with the marker's current coordinates.
              makeReverseGeocodingSearch();
              if (includeDeviceLocationButton) {
                adjustBottomMarginOfFabLinearLayout(135);
              } else {
                adjustBottomMarginOfFabLinearLayout(7);
              }
            }

            bindListeners();

            if (options != null && includeDeviceLocationButton) {
              enableLocationComponent(style);
              addUserLocationButton();
            } else {
              userLocationButton.hide();
            }

            if (options != null) {
              if (options.startingBounds() != null) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(options.startingBounds(), 0));
              } else if (options.statingCameraPosition() != null) {
                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(options.statingCameraPosition()));
              }

              if (includeSearch) {
                searchCardView = findViewById(R.id.optional_search_autocomplete_cardview);
                searchCardView.setVisibility(View.VISIBLE);

                adjustMapCompassTopPadding(false);

                PlaceAutocompleteFragment autocompleteFragment = PlaceAutocompleteFragment.newInstance(
                  Mapbox.getAccessToken(), initPlaceOptions());

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction
                  .add(R.id.optional_search_autocomplete_cardview_fragment_container,
                    autocompleteFragment, PlaceAutocompleteFragment.TAG)
                  .commit();

                autocompleteFragment.setOnClearButtonListener(new ClearButtonListener() {
                  @Override
                  public void onClearButtonPress() {
                    adjustResultsCardViewHeight(false);
                  }

                  @Override
                  public void onCancel() {
                    finish();
                  }
                });

                // Update the search history count to determine whether the search UI should
                // be expanded when the EditText is focused on.
                autocompleteFragment.setOnHistoryCountListener(
                    new PlaceAutocompleteFragment.SearchHistoryCountListener() {
                      @Override
                      public void onNewHistoryCount(int count) {
                        searchHistoryCount = count;
                      }

                      @Override
                      public void onCancel() {
                        finish();
                      }
                    });

                // Set up what should happen when the search UI EditText is focused on
                autocompleteFragment.setOnSearchUiHasFocusListener(new QueryFocusListener() {
                  @Override
                  public void onSearchViewEditTextHasFocus() {
                    if (searchHistoryCount > 0) {
                      adjustResultsCardViewHeight(true);
                    }
                  }

                  @Override
                  public void onCancel() {
                    finish();
                  }
                });

                // Expand the search UI cardview height when typing happens in the EditText.
                autocompleteFragment.setOnSearchQueryListener(new QueryListener() {
                  @Override
                  public void onQueryChange(CharSequence charSequence) {
                    // charSequence not needed here in this activity. The cardview height
                    // will adjust once text query change has changed.
                    adjustResultsCardViewHeight(true);
                  }

                  @Override
                  public void onCancel() {
                    finish();
                  }
                });

                // Expand the search UI cardview height and move the map when a place is selected.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                  @Override
                  public void onPlaceSelected(CarmenFeature carmenFeature) {
                    PlacePickerActivity.this.carmenFeature = carmenFeature;

                    // Adjust the height of the cardview that has the list of search results
                    adjustResultsCardViewHeight(false);

                    // Hide the keyboard once a place has been selected
                    KeyboardUtils.hideKeyboard(getWindow().getDecorView());

                    // Move camera to selected location
                    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                      new LatLng(carmenFeature.center().latitude(),
                        carmenFeature.center().longitude()), mapboxMap.getCameraPosition().zoom));
                  }

                  @Override
                  public void onCancel() {
                    finish();
                  }
                });
              }
            }

            PlacePickerActivity.this.mapboxMap.addOnMapClickListener(PlacePickerActivity.this);
            PlacePickerActivity.this.mapboxMap.addOnCameraMoveStartedListener(PlacePickerActivity.this);
            PlacePickerActivity.this.mapboxMap.addOnCameraIdleListener(PlacePickerActivity.this);
          }
        });
  }

  private void adjustBottomMarginOfFabLinearLayout(int newBottom) {
    View placeChosenButton = findViewById(R.id.place_chosen_button);
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) placeChosenButton.getLayoutParams();
    params.bottomMargin = newBottom;
    placeChosenButton.setLayoutParams(params);
  }

  /**
   * Initialize {@link PlaceOptions} to use when creating an instance of {@link PlaceAutocompleteFragment}
   * if search was enabled in the {@link PlacePickerOptions}.
   *
   * @return a fully built {@link PlaceOptions} object.
   */
  private PlaceOptions initPlaceOptions() {
    int toolbarColor = customToolbarColor != null ? customToolbarColor
      : getThemePrimaryColor(PlacePickerActivity.this);
    int statusBarColor = options.statusBarColor() != null ? options.statusBarColor()
      : getThemePrimaryDarkColor(PlacePickerActivity.this);
    return PlaceOptions.builder()
      .toolbarColor(toolbarColor)
      .statusbarColor(statusBarColor)
      .hint(getString(R.string.mapbox_plugins_autocomplete_search_hint))
      .build();
  }

  @Override
  public boolean onMapClick(@NonNull LatLng point) {
    if (!resultsCardViewListIsCollapsed) {
      adjustResultsCardViewHeight(false);
      KeyboardUtils.hideKeyboard(getWindow().getDecorView());
    }
    return true;
  }

  private void adjustCameraBasedOnOptions() {
    if (options != null) {
      if (options.startingBounds() != null) {
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(options.startingBounds(), 0));
      } else if (options.statingCameraPosition() != null) {
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(options.statingCameraPosition()));
      }
    }
  }

  @SuppressWarnings( {"MissingPermission"})
  private void enableLocationComponent(@NonNull Style loadedMapStyle) {
    // Check if permissions are enabled and if not request
    if (PermissionsManager.areLocationPermissionsGranted(this)) {

      // Get an instance of the component
      LocationComponent locationComponent = mapboxMap.getLocationComponent();

      // Activate with options
      locationComponent.activateLocationComponent(
        LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

      // Enable to make component visible
      locationComponent.setLocationComponentEnabled(true);

      // Set the component's camera mode
      locationComponent.setCameraMode(CameraMode.NONE);

      // Set the component's render mode
      locationComponent.setRenderMode(RenderMode.NORMAL);

    } else {
      permissionsManager = new PermissionsManager(this);
      permissionsManager.requestLocationPermissions(this);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    Toast.makeText(this, R.string.mapbox_plugins_place_picker_user_location_permission_explanation,
      Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      mapboxMap.getStyle(new Style.OnStyleLoaded() {
        @Override
        public void onStyleLoaded(@NonNull Style style) {
          if (options != null && includeDeviceLocationButton) {
            enableLocationComponent(style);
          }
        }
      });
    }
  }

  /**
   * Adjusts the height of the {@link PlaceAutocompleteFragment} CardView.
   *
   * @param expandCard whether or not the height should be lengthened.
   */
  public void adjustResultsCardViewHeight(boolean expandCard) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      TransitionManager.beginDelayedTransition(searchResultsCardView, new TransitionSet()
        .addTransition(new ChangeBounds()));
      ViewGroup.LayoutParams params = searchResultsCardView.getLayoutParams();
      params.height = expandCard ? EXPANDED_SEARCH_CARDVIEW_HEIGHT : COLLAPSED_SEARCH_CARDVIEW_HEIGHT;
      searchResultsCardView.setLayoutParams(params);
    } else {
      searchResultsCardView.setLayoutParams(new FrameLayout.LayoutParams(
        searchResultsCardView.getMeasuredWidth(), expandCard ? EXPANDED_SEARCH_CARDVIEW_HEIGHT
        : COLLAPSED_SEARCH_CARDVIEW_HEIGHT));
    }
    adjustMapTopPadding(expandCard);
    adjustMapCompassTopPadding(expandCard);
    resultsCardViewListIsCollapsed = !expandCard;
  }

  private void adjustMapTopPadding(boolean cardExpanded) {
    int[] mapPadding = mapboxMap.getPadding();
    mapboxMap.setPadding(mapPadding[0], cardExpanded ? EXPANDED_SEARCH_CARDVIEW_HEIGHT
        : COLLAPSED_SEARCH_CARDVIEW_HEIGHT,
      mapPadding[2], mapPadding[3]);
  }

  private void adjustMapCompassTopPadding(boolean cardExpanded) {
    int[] mapPadding = mapboxMap.getPadding();
    int newTopPadding;
    newTopPadding = cardExpanded
        ? EXPANDED_SEARCH_CARDVIEW_HEIGHT + SPACE_BETWEEN_RESULTS_LIST_AND_COMPASS
        : COLLAPSED_SEARCH_CARDVIEW_HEIGHT + SPACE_BETWEEN_RESULTS_LIST_AND_COMPASS;
    mapboxMap.getUiSettings().setCompassMargins(mapPadding[0],
        newTopPadding, mapPadding[2], mapPadding[3]);
  }

  /**
   * Get the primary color from the app's theming.
   *
   * @param context this activity's context.
   * @return an int value representing the theme's primary color
   */
  private static int getThemePrimaryColor(Context context) {
    int colorAttr;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      colorAttr = android.R.attr.colorPrimary;
    } else {
      //Get colorAccent defined for AppCompat
      colorAttr = context.getResources().getIdentifier("colorPrimary", "attr",
        context.getPackageName());
    }
    TypedValue outValue = new TypedValue();
    context.getTheme().resolveAttribute(colorAttr, outValue, true);
    return outValue.data;
  }

  /**
   * Get the primary dark color from the app's theming.
   *
   * @param context this activity's context.
   * @return an int value representing the theme's primary dark color
   */
  private static int getThemePrimaryDarkColor(Context context) {
    int colorAttr;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      colorAttr = android.R.attr.colorPrimaryDark;
    } else {
      //Get colorAccent defined for AppCompat
      colorAttr = context.getResources().getIdentifier("colorPrimaryDark",
        "attr", context.getPackageName());
    }
    TypedValue outValue = new TypedValue();
    context.getTheme().resolveAttribute(colorAttr, outValue, true);
    return outValue.data;
  }

  @Override
  public void onCameraMoveStarted(int reason) {
    Timber.v("Map camera has begun moving.");
    if (markerImage.getTranslationY() == 0) {
      markerImage.animate().translationY(-75)
        .setInterpolator(new OvershootInterpolator()).setDuration(250).start();
      if (includeReverseGeocode) {
        if (bottomSheet.isShowing()) {
          bottomSheet.dismissPlaceDetails();
        }
      }
    }
  }

  @Override
  public void onCameraIdle() {
    Timber.v("Map camera is now idling.");
    markerImage.animate().translationY(0)
      .setInterpolator(new OvershootInterpolator()).setDuration(250).start();
    if (includeReverseGeocode) {
      if (includeSearch) {
        if (carmenFeature != null) {
          bottomSheet.setPlaceDetails(carmenFeature);
        }
      } else {
        bottomSheet.setPlaceDetails(null);
      }
      // Initialize with the markers current location information.
      makeReverseGeocodingSearch();
    }
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
    if (latLng != null) {
      viewModel.reverseGeocode(
        Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()),
        accessToken, options
      );
    }
  }

  private void addPlaceSelectedButton() {
    FloatingActionButton placeSelectedButton = findViewById(R.id.place_chosen_button);
    placeSelectedButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (carmenFeature == null && includeReverseGeocode) {
          Snackbar.make(bottomSheet,
            getString(R.string.mapbox_plugins_place_picker_not_valid_selection),
            LENGTH_LONG).show();
          return;
        }
        placeSelected();
      }
    });
  }

  /**
   * Bind the device location Floating Action Button to this activity's UI and animate the
   * map camera to the device's location if the button's clicked.
   */
  private void addUserLocationButton() {
    userLocationButton.show();
    userLocationButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) {
          Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
          mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
            new CameraPosition.Builder()
              .target(new LatLng(lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude()))
              .zoom(17.5)
              .build()
          ), 1400);
        } else {
          Toast.makeText(PlacePickerActivity.this,
            getString(R.string.mapbox_plugins_place_picker_user_location_not_found), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  void placeSelected() {
    Intent returningIntent = new Intent();
    if (includeReverseGeocode) {
      String json = carmenFeature.toJson();
      returningIntent.putExtra(PlaceConstants.RETURNING_CARMEN_FEATURE, json);
    }
    returningIntent.putExtra(PlaceConstants.MAP_CAMERA_POSITION, mapboxMap.getCameraPosition());
    setResult(AppCompatActivity.RESULT_OK, returningIntent);
    finish();
  }

  public int getToolBarHeight() {
    int[] attrs = new int[] {R.attr.actionBarSize};
    TypedArray ta = this.obtainStyledAttributes(attrs);
    int toolBarHeight = ta.getDimensionPixelSize(0, -1);
    ta.recycle();
    return toolBarHeight;
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
