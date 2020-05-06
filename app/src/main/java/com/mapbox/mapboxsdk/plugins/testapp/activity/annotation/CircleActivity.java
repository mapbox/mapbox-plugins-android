package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnCircleDragListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

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

    TextView draggableInfoTv = findViewById(R.id.draggable_position_tv);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
      findViewById(R.id.fabStyles).setOnClickListener(v -> mapboxMap.setStyle(Utils.INSTANCE.getNextStyle()));

      mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

      // create circle manager
      circleManager = new CircleManager(mapView, mapboxMap, style);
      circleManager.addClickListener(circle -> {
        Toast.makeText(CircleActivity.this,
            String.format("Circle clicked %s", circle.getId()),
            Toast.LENGTH_SHORT
        ).show();
        return false;
      });
      circleManager.addLongClickListener(circle -> {
        Toast.makeText(CircleActivity.this,
            String.format("Circle long clicked %s", circle.getId()),
            Toast.LENGTH_SHORT
        ).show();
        return false;
      });

      // create a fixed circle
      CircleOptions circleOptions = new CircleOptions()
        .withLatLng(new LatLng(6.687337, 0.381457))
        .withCircleColor(ColorUtils.colorToRgbaString(Color.YELLOW))
        .withCircleRadius(12f)
        .withDraggable(true);
      circleManager.create(circleOptions);

      // random add circles across the globe
      List<CircleOptions> circleOptionsList = new ArrayList<>();
      for (int i = 0; i < 20; i++) {
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        circleOptionsList.add(new CircleOptions()
          .withLatLng(createRandomLatLng())
          .withCircleColor(ColorUtils.colorToRgbaString(color))
          .withCircleRadius(8f)
          .withDraggable(true)
        );
      }
      circleManager.create(circleOptionsList);

      try {
        circleManager.create(Utils.INSTANCE.loadStringFromAssets(this, "annotations.json"));
      } catch (IOException e) {
        throw new RuntimeException("Unable to parse annotations.json");
      }

      circleManager.addDragListener(new OnCircleDragListener() {
        @Override
        public void onAnnotationDragStarted(Circle annotation) {
          draggableInfoTv.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnnotationDrag(Circle annotation) {
          draggableInfoTv.setText(String.format(
            Locale.US,
            "ID: %s\nLatLng:%f, %f",
            annotation.getId(),
            annotation.getLatLng().getLatitude(), annotation.getLatLng().getLongitude()));
        }

        @Override
        public void onAnnotationDragFinished(Circle annotation) {
          draggableInfoTv.setVisibility(View.GONE);
        }
      });
    }));
  }

  private LatLng createRandomLatLng() {
    return new LatLng((random.nextDouble() * -180.0) + 90.0,
      (random.nextDouble() * -360.0) + 180.0);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_circle, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_action_draggable) {
      for (int i = 0; i < circleManager.getAnnotations().size(); i++) {
        Circle circle = circleManager.getAnnotations().get(i);
        circle.setDraggable(!circle.isDraggable());
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

    if (circleManager != null) {
      circleManager.onDestroy();
    }

    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}