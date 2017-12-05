package com.mapbox.mapboxsdk.plugins.locationpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Location Picker activity enables you to easily select one location from Mapbox map.
 * return coordinates of selected location with address of it to the requester activity.
 */
public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener {
  public static final String MAPBOX_TOKEN = "mapbox_token";
  public static final String LATITUDE = "latitude";
  public static final String LONGITUDE = "longitude";
  public static final String ADDRESS = "address";
  public static final String BACK_PRESSED_RETURN = "back_pressed_return";
  public static final String ENABLE_SATELLITE_VIEW = "enable_satellite_view";
  private MapView mapView;
  private MapboxMap mMap;
  private TextView address;
  private TextView coordinates;
  private FrameLayout locationInfoLayout;
  private ContentLoadingProgressBar progressBar;
  private boolean enableSatelliteView;
  private boolean enableBackPressedReturn;
  private Marker currentMarker;
  private LatLng postedLocation;
  private String currentAddress;
  private LatLng currentPosition;
  private Location lastLocation;
  private String mapboxToken;

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getValuesFromBundle();
    Mapbox.getInstance(this, mapboxToken);
    setContentView(R.layout.activity_location_picker);
    setUpMap(savedInstanceState);
    setUpMainVariables();
    setUpFloatingButtons();
  }

  /**
   * get posted value from requester activity and set in local variables.
   */
  private void getValuesFromBundle() {
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      if (postedLocation == null) {
        postedLocation = new LatLng();
      }
      postedLocation.setLatitude(bundle.getDouble(LATITUDE));
      postedLocation.setLongitude(bundle.getDouble(LONGITUDE));
      mapboxToken = (bundle.getString(MAPBOX_TOKEN));
      enableBackPressedReturn = bundle.getBoolean(BACK_PRESSED_RETURN);
      enableSatelliteView = bundle.getBoolean(ENABLE_SATELLITE_VIEW);
    }
  }

  /**
   * setup Mapbox
   *
   * @param savedInstanceState get from onCreate function.
   */
  private void setUpMap(Bundle savedInstanceState) {
    mapView = findViewById(R.id.mapView);
    mapView.setStyleUrl(Style.MAPBOX_STREETS);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  /**
   * setup UI declaration
   */
  private void setUpMainVariables() {
    progressBar = findViewById(R.id.loading_progress_bar);
    progressBar.setVisibility(View.GONE);
    locationInfoLayout = findViewById(R.id.location_info);
    address = findViewById(R.id.address);
    coordinates = findViewById(R.id.coordinates);
  }

  /**
   * setup Floating buttons and set onClick listener of them
   */
  private void setUpFloatingButtons() {
    FloatingActionButton buttonCurrentLocation = findViewById(R.id.button_current_location);
    buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (lastLocation != null) {
          LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
          setNewMapMarker(latLng);
          reverseGeocode(latLng);
        } else {
          Toast.makeText(LocationPickerActivity.this, "location not found, please wait.", Toast.LENGTH_SHORT).show();
        }
      }
    });
    FloatingActionButton buttonAccept = findViewById(R.id.button_accept);
    buttonAccept.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        returnCurrentPosition();
      }
    });
    final FloatingActionButton buttonSatellite = findViewById(R.id.button_satellite);
    buttonSatellite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mapView.setStyleUrl(Style.MAPBOX_STREETS.equals(mMap.getStyleUrl()) ? Style.SATELLITE : Style.MAPBOX_STREETS);
        buttonSatellite.setImageResource(
          !Style.MAPBOX_STREETS.equals(mMap.getStyleUrl()) ? R.drawable.ic_satellite_on : R.drawable.ic_satellite_off);
      }
    });
    buttonSatellite.setVisibility(enableSatelliteView ? View.VISIBLE : View.GONE);
  }

  /**
   * return coordinates of current position as latitude & longitude with address of it if provided.
   * return RESULT_CANCELED if current position is null.
   */
  private void returnCurrentPosition() {
    if (currentPosition != null) {
      Intent returnIntent = new Intent();
      returnIntent.putExtra(LATITUDE, currentPosition.getLatitude());
      returnIntent.putExtra(LONGITUDE, currentPosition.getLongitude());
      if (currentAddress != null) {
        returnIntent.putExtra(ADDRESS, currentAddress);
      }
      setResult(RESULT_OK, returnIntent);
    } else {
      setResult(RESULT_CANCELED);
    }
    finish();
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    mMap = mapboxMap;
    mMap.setMyLocationEnabled(true);
    Mapbox.getLocationEngine().addLocationEngineListener(this);
    Mapbox.getLocationEngine().requestLocationUpdates();
    setCurrentPositionLocation();
    setMapClickListener();
  }

  /**
   * if location from requester is'nt null, add marker of it to map and get address of it from reverseGeocode service.
   */
  private void setCurrentPositionLocation() {
    if (postedLocation != null) {
      setNewMapMarker(postedLocation);
      reverseGeocode(postedLocation);
    }
  }

  /**
   * if currentMarker is null add new marker to Map with position of param. otherwise move currentMarker
   * position to new value.
   *
   * @param latLng coordinates of marker.
   */
  private void setNewMapMarker(LatLng latLng) {
    if (mMap != null) {
      if (currentMarker != null) {
        currentMarker.setPosition(latLng);
      } else {
        currentMarker = addMarker(latLng);
      }
      CameraPosition cameraPosition =
        new CameraPosition.Builder().target(latLng)
          .zoom(15)
          .build();
      mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 200);
    }
  }

  /**
   * add one marker to Map and return marker instance.
   *
   * @param latLng coordinates of marker.
   * @return Marker
   */
  private Marker addMarker(LatLng latLng) {
    return mMap.addMarker(new MarkerOptions().position(latLng));
  }

  /**
   * get address of point with reverse geocode service of mapbox and set result in UI.
   *
   * @param point the coordinates that use in reverse geocode for getting address.
   */
  private void reverseGeocode(final LatLng point) {
    hideAddressLayout();
    try {
      MapboxGeocoding client = new MapboxGeocoding.Builder()
        .setAccessToken(mapboxToken)
        .setCoordinates(Position.fromCoordinates(point.getLongitude(), point.getLatitude()))
        .setGeocodingType(GeocodingCriteria.TYPE_ADDRESS)
        .build();

      client.enqueueCall(new Callback<GeocodingResponse>() {
        @Override
        public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
          List<CarmenFeature> results = response.body().getFeatures();
          if (results.size() > 0) {
            CarmenFeature feature = results.get(0);
            currentPosition = point;
            currentAddress = feature.getPlaceName();
            setCoordinatesInfo(currentPosition);
            setLocationAddress(currentAddress);
            showAddressLayout();
          } else {
            currentPosition = null;
            currentAddress = null;
            Toast.makeText(LocationPickerActivity.this, "Address not found!", Toast.LENGTH_SHORT).show();
          }
          progressBar.hide();
        }

        @Override
        public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
          Log.e("", "Geocoding Failure: " + throwable.getMessage());
          progressBar.hide();
        }
      });
    } catch (ServicesException servicesException) {
      Log.e("", "Error geocoding: " + servicesException.toString());
      servicesException.printStackTrace();
      progressBar.hide();
    }
  }

  /**
   * before getting result from reverse geocode service the UI will be hidden.
   */
  private void hideAddressLayout() {
    locationInfoLayout.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
    progressBar.show();
  }

  /**
   * show value of latitude & longitude in the UI.
   *
   * @param latLng current position
   */
  private void setCoordinatesInfo(LatLng latLng) {
    String lat = String.format(Locale.US, "%2.5f", latLng.getLatitude()) + "° N";
    String lng = String.format(Locale.US, "%2.5f", latLng.getLongitude()) + "° E";
    coordinates.setText(String.format("%s , %s", lat, lng));
  }

  /**
   * set address of current position in the UI.
   *
   * @param address the address of current position
   */
  private void setLocationAddress(String address) {
    this.address.setText(address);
  }

  /**
   * after get result from reverse geocode service the UI of it will be show.
   */
  private void showAddressLayout() {
    locationInfoLayout.setVisibility(View.VISIBLE);
  }

  /**
   * setup map click listener for get coordinates of tabbed position and send it to reverse geocode service.
   */
  private void setMapClickListener() {
    mMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull LatLng point) {
        setNewMapMarker(point);
        reverseGeocode(point);
      }
    });
  }

  /**
   * if enableBackPressedReturn is true return current position to requester activity.
   * else return RESULT_CANCELED
   */
  @Override
  public void onBackPressed() {
    if (enableBackPressedReturn) {
      returnCurrentPosition();
    } else {
      setResult(RESULT_CANCELED);
      finish();
    }
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
    Mapbox.getLocationEngine().removeLocationEngineListener(this);
    Mapbox.getLocationEngine().removeLocationUpdates();
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

  @Override
  public void onConnected() {

  }

  @Override
  public void onLocationChanged(Location location) {
    lastLocation = location;
  }

  public static class Builder {
    private Double locationLatitude;
    private Double locationLongitude;
    private boolean enableSatelliteView = true;
    private boolean returnOnBackPressed = false;
    private String mapboxToken = null;

    public Builder() {
    }

    /**
     * set first position of requester
     *
     * @param latitude  the latitude of position.
     * @param longitude the longitude of position.
     * @return instance of {@link Builder} class.
     */
    public Builder withLocation(double latitude, double longitude) {
      this.locationLatitude = latitude;
      this.locationLongitude = longitude;
      return this;
    }

    /**
     * set first position of requester
     *
     * @param latLng coordinates of position.
     * @return instance of {@link Builder} class.
     */
    public Builder withLocation(LatLng latLng) {
      if (latLng != null) {
        this.locationLatitude = latLng.getLatitude();
        this.locationLongitude = latLng.getLongitude();
      }
      return this;
    }

    /**
     * enable showing satellite switcher mode.
     *
     * @param enableSatelliteView if true, show satellite switcher button.
     * @return instance of {@link Builder} class.
     */
    public Builder withSatelliteView(boolean enableSatelliteView) {
      this.enableSatelliteView = enableSatelliteView;
      return this;
    }

    /**
     * enable return result from activity when press back button/
     *
     * @param returnOnBackPressed if false, return RESULT_CANCELED to requester activity.
     * @return instance of {@link Builder} class.
     */
    public Builder withReturnOnBackPressed(boolean returnOnBackPressed) {
      this.returnOnBackPressed = returnOnBackPressed;
      return this;
    }

    /**
     * set Mapbox token.
     *
     * @param token the token of Mapbox.
     * @return instance of {@link Builder} class.
     */
    public Builder withMapboxToken(String token) {
      this.mapboxToken = token;
      return this;
    }

    /**
     * build intent with set parameter on it.
     *
     * @param context the instance of {@link Context}
     * @return instance of intent with parameters.
     */

    public Intent build(Context context) {
      Intent intent = new Intent(context, LocationPickerActivity.class);
      if (locationLatitude != null) {
        intent.putExtra(LATITUDE, locationLatitude);
      }
      if (locationLongitude != null) {
        intent.putExtra(LONGITUDE, locationLongitude);
      }
      intent.putExtra(BACK_PRESSED_RETURN, returnOnBackPressed);
      intent.putExtra(ENABLE_SATELLITE_VIEW, enableSatelliteView);
      if (mapboxToken != null) {
        intent.putExtra(MAPBOX_TOKEN, mapboxToken);
      }
      return intent;
    }
  }
}
