package com.mapbox.plugins.places.autocomplete;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mapbox.geocoding.v5.MapboxGeocoding;
import com.mapbox.geocoding.v5.models.GeocodingResponse;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.views.SearchBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlacesCompleteActivity extends AppCompatActivity implements
  SearchBarView.QueryListener, Callback<GeocodingResponse> {

  private MapboxGeocoding.Builder geocoderBuilder;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_places_complete);

    Intent intent = getIntent();

    View view = findViewById(R.id.root_layout);
    view.setBackgroundColor(intent.getIntExtra("backgroundColor", Color.GRAY));

    geocoderBuilder = geocoderBuilder();

    geocoderBuilder.limit(intent.getIntExtra("limit", 5));


    SearchBarView searchBar = findViewById(R.id.cardview_search);
    searchBar.addQueryChangeListener(this);
  }


  private MapboxGeocoding.Builder geocoderBuilder() {
    return MapboxGeocoding.builder()
      .accessToken("pk.eyJ1IjoiY2FtbWFjZSIsImEiOiI5OGQxZjRmZGQ2YjU3Mzk1YjJmZTQ5ZDY2MTg1NDJiOCJ9.hIFoCKGAGOwQkKyVPvrxvQ")
      .autocomplete(true);
  }


  @Override
  public void onQueryChange(CharSequence charSequence) {
    String query = charSequence.toString();
    if (query.isEmpty()) {
      return;
    }
    geocoderBuilder.query(query)
      .build().enqueueCall(this);
  }

  @Override
  public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
    System.out.println(response.body().features().get(0).placeName());
  }

  @Override
  public void onFailure(Call<GeocodingResponse> call, Throwable t) {

  }
}