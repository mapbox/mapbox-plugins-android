package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;

/**
 * Test activity showcasing to add a Symbol on click.
 * <p>
 * Shows how to use a OnMapClickListener and a OnMapLongClickListener
 * </p>
 */
public class PressForSymbolActivity extends AppCompatActivity {

  public static final String ID_ICON = "id-icon";
  private SymbolManager symbolManager;
  private MapView mapView;
  private MapboxMap mapboxMap;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(map -> {
      mapboxMap = map;
      mapboxMap.setCameraPosition(new CameraPosition.Builder()
        .target(new LatLng(60.169091, 24.939876))
        .zoom(12)
        .tilt(20)
        .bearing(90)
        .build()
      );

      mapboxMap.addImage(ID_ICON, generateBitmap(R.drawable.mapbox_ic_place));

      symbolManager = new SymbolManager(mapView, mapboxMap);
      symbolManager.setIconAllowOverlap(true);
      symbolManager.setTextAllowOverlap(true);

      mapboxMap.addOnMapLongClickListener(this::addSymbol);
      mapboxMap.addOnMapClickListener(this::addSymbol);
    });
  }

  private void addSymbol(LatLng point) {
    symbolManager.create(new SymbolOptions()
      .withLatLng(point)
      .withIconImage(ID_ICON)
    );
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
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
  protected void onDestroy() {
    super.onDestroy();
    mapboxMap.removeOnMapClickListener(this::addSymbol);
    mapboxMap.removeOnMapLongClickListener(this::addSymbol);
    symbolManager.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  private Bitmap generateBitmap(@DrawableRes int drawableRes) {
    Drawable drawable = getResources().getDrawable(drawableRes);
    return getBitmapFromDrawable(drawable);
  }

  static Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else {
      // width and height are equal for all assets since they are ovals.
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
    }
  }
}

