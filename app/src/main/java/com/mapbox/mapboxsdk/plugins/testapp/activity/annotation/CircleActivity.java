package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleLongClickListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Activity showcasing adding circles using the annotation plugin
 */
public class CircleActivity extends AppCompatActivity {

  private final Random random = new Random();

  private MapView mapView;
  private CircleManager circleManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {
      mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

      // create circle manager
      circleManager = new CircleManager(mapboxMap);
      circleManager.addClickListener(circle -> Toast.makeText(CircleActivity.this,
        String.format("Circle clicked %s", circle.getId()),
        Toast.LENGTH_SHORT
      ).show());
      circleManager.addLongClickListener(circle -> Toast.makeText(CircleActivity.this,
        String.format("Circle long clicked %s", circle.getId()),
        Toast.LENGTH_SHORT
      ).show());

      // create a fixed circle
      Circle circle = circleManager.createCircle(new LatLng(6.687337, 0.381457));
      circle.setCircleColor(PropertyFactory.colorToRgbaString(Color.YELLOW));
      circle.setCircleRadius(12f);

      // random add circles across the globe
      List<LatLng> latLngList = new ArrayList<>();
      for (int i = 0; i < 20; i++) {
        latLngList.add(createRandomLatLng());
      }
      List<Circle> circles = circleManager.createCircles(latLngList);
      for (Circle currentCircle : circles) {
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        currentCircle.setCircleColor(PropertyFactory.colorToRgbaString(color));
        currentCircle.setCircleRadius(8f);
      }
    });
  }

  private LatLng createRandomLatLng() {
    return new LatLng((random.nextDouble() * -180.0) + 90.0,
      (random.nextDouble() * -360.0) + 180.0);
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
    circleManager.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}