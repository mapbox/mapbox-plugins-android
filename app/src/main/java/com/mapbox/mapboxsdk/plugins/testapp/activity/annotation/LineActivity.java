package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
 * Activity showcasing adding lines using the annotation plugin
 */
public class LineActivity extends AppCompatActivity {

  private final Random random = new Random();

  private MapView mapView;
  private LineManager lineManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {
      mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

      lineManager = new LineManager(mapView, mapboxMap);
      lineManager.addClickListener(line -> Toast.makeText(LineActivity.this,
        String.format("Line clicked %s", line.getId()),
        Toast.LENGTH_SHORT
      ).show());
      lineManager.addLongClickListener(line -> Toast.makeText(LineActivity.this,
        String.format("Line long clicked %s", line.getId()),
        Toast.LENGTH_SHORT
      ).show());

      // create a fixed line
      List<LatLng> latLngs = new ArrayList<>();
      latLngs.add(new LatLng(-2.178992, -4.375974));
      latLngs.add(new LatLng(-4.107888, -7.639772));
      latLngs.add(new LatLng(2.798737, -11.439207));
      LineOptions lineOptions = new LineOptions()
        .withLatLngs(latLngs)
        .withLineColor(PropertyFactory.colorToRgbaString(Color.RED))
        .withLineWidth(5.0f);
      lineManager.create(lineOptions);

      // random add lines across the globe
      List<List<LatLng>> lists = new ArrayList<>();
      for (int i = 0; i < 100; i++) {
        lists.add(createRandomLatLngs());
      }

      List<LineOptions> lineOptionsList = new ArrayList<>();
      for (List<LatLng> list : lists) {
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        lineOptionsList.add(new LineOptions().withLatLngs(list).withLineColor(PropertyFactory.colorToRgbaString(color)));
      }
      lineManager.create(lineOptionsList);
    });
  }

  private List<LatLng> createRandomLatLngs() {
    List<LatLng> latLngs = new ArrayList<>();
    for (int i = 0; i < random.nextInt(10); i++) {
      latLngs.add(new LatLng((random.nextDouble() * -180.0) + 90.0,
        (random.nextDouble() * -360.0) + 180.0));
    }
    return latLngs;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_line, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_action_draggable) {
      for (int i = 0; i < lineManager.getAnnotations().size(); i++) {
        Line line = lineManager.getAnnotations().get(i);
        line.setDraggable(!line.isDraggable());
      }
      return true;
    }
    return false;
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
    lineManager.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}