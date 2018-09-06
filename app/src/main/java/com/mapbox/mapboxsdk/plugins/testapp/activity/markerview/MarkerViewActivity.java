package com.mapbox.mapboxsdk.plugins.testapp.activity.markerview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.Random;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MarkerViewActivity extends AppCompatActivity implements MapboxMap.OnMapLongClickListener,
  MapboxMap.OnMapClickListener {

  private final Random random = new Random();
  private MarkerViewManager markerViewManager;
  private MapView mapView;
  private MarkerView marker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);
    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {
      mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

      markerViewManager = new MarkerViewManager(mapView, mapboxMap);
      createCustomMarker();
      createRandomMarkers();

      mapboxMap.addOnMapLongClickListener(MarkerViewActivity.this);
      mapboxMap.addOnMapClickListener(MarkerViewActivity.this);
    });
  }

  private void createCustomMarker() {
    // create a custom animation marker view
    final View customView = createCustomAnimationView();
    marker = new MarkerView(new LatLng(), customView);
    markerViewManager.addMarker(marker);
  }

  private View createCustomAnimationView() {
    View customView = LayoutInflater.from(this).inflate(R.layout.marker_view, null);
    customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
    View icon = customView.findViewById(R.id.imageview);
    View animationView = customView.findViewById(R.id.animation_layout);
    icon.setOnClickListener(v -> {
      ValueAnimator anim = ValueAnimator.ofInt(animationView.getMeasuredWidth(), 350);
      anim.setInterpolator(new AccelerateDecelerateInterpolator());
      anim.addUpdateListener(valueAnimator -> {
        int val = (Integer) valueAnimator.getAnimatedValue();
        ViewGroup.LayoutParams layoutParams = animationView.getLayoutParams();
        layoutParams.width = val;
        animationView.setLayoutParams(layoutParams);
      });
      anim.setDuration(1250);
      anim.start();
    });
    return customView;
  }

  private void createRandomMarkers() {
    for (int i = 0; i < 20; i++) {
      final ImageView imageView = new ImageView(MarkerViewActivity.this);
      imageView.setImageResource(R.drawable.ic_car);
      imageView.setLayoutParams(new FrameLayout.LayoutParams(56, 56));
      MarkerView markerView = new MarkerView(createRandomLatLng(), imageView);
      markerViewManager.addMarker(markerView);
    }
  }

  private LatLng createRandomLatLng() {
    return new LatLng((random.nextDouble() * -180.0) + 90.0,
      (random.nextDouble() * -360.0) + 180.0);
  }

  @Override
  public void onMapLongClick(@NonNull LatLng point) {
    if (marker != null) {
      markerViewManager.removeMarker(marker);
      marker = null;
    }
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    if (marker != null) {
      marker.setLatLng(point);
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
    markerViewManager.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}
