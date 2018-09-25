package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.annotation.*;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Activity showcasing adding fills using the annotation plugin
 */
public class FillActivity extends AppCompatActivity {

  private final Random random = new Random();

  private MapView mapView;
  private FillManager fillManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {
      mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

      fillManager = new FillManager(mapboxMap);
      fillManager.addClickListener(fill -> Toast.makeText(FillActivity.this,
        String.format("Fill clicked %s", fill.getId()),
        Toast.LENGTH_SHORT
      ).show());
      fillManager.addLongClickListener(fill -> Toast.makeText(FillActivity.this,
        String.format("Fill long clicked %s", fill.getId()),
        Toast.LENGTH_SHORT
      ).show());

      // create a fixed fill
      List<LatLng> innerLatLngs = new ArrayList<>();
      innerLatLngs.add(new LatLng(-10.733102, -3.363937));
      innerLatLngs.add(new LatLng(-19.716317, 1.754703));
      innerLatLngs.add(new LatLng(-21.085074, -15.747196));
      List<List<LatLng>> latLngs = new ArrayList<>();
      latLngs.add(innerLatLngs);

      FillOptions fillOptions = new FillOptions()
        .withLatLngs(latLngs)
        .withFillColor(PropertyFactory.colorToRgbaString(Color.RED));
      fillManager.createFill(fillOptions);

      // random add fills across the globe
      List<FillOptions> fillOptionsList = new ArrayList<>();
      for (int i = 0; i < 20; i++) {
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        fillOptionsList.add(new FillOptions()
          .withLatLngs(createRandomLatLngs())
          .withFillColor(PropertyFactory.colorToRgbaString(color))
        );
      }
      fillManager.createFills(fillOptionsList);
    });
  }

  private List<List<LatLng>> createRandomLatLngs() {
    List<LatLng> latLngs = new ArrayList<>();
    for (int i = 0; i < random.nextInt(10); i++) {
      latLngs.add(new LatLng((random.nextDouble() * -180.0) + 90.0,
        (random.nextDouble() * -360.0) + 180.0));
    }
    return new ArrayList<List<LatLng>>() {
      {
        add(latLngs);
      }
    };
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
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    fillManager.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}