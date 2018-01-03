package com.mapbox.mapboxsdk.plugins.testapp.activity.places;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.plugins.places.details.PlaceDetailsBottomSheet;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback,
  MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener, Callback<GeocodingResponse> {

  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.placeDetails)
  PlaceDetailsBottomSheet placeDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    ButterKnife.bind(this);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    mapboxMap.setOnMapClickListener(this);
    mapboxMap.setOnMapLongClickListener(this);
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {

    placeDetails.setPlaceDetails(null);

    MapboxGeocoding geocoding = MapboxGeocoding.builder()
      .accessToken(Mapbox.getAccessToken())
      .query(Point.fromLngLat(point.getLongitude(), point.getLatitude()))
      .build();

    geocoding.enqueueCall(this);
  }

  @Override
  public void onMapLongClick(@NonNull LatLng point) {
    placeDetails.dismissPlaceDetails();
  }

  @Override
  public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
    if (response.body().features().isEmpty()) {
      placeDetails.setPlaceDetails(null);
      return;
    }
    placeDetails.setPlaceDetails(response.body().features().get(0));
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
    Timber.e(throwable);
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
