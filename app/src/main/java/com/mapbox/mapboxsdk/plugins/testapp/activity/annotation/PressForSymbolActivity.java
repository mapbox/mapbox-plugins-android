package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.mapboxsdk.style.layers.Property;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;

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
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
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
      mapboxMap.addOnMapLongClickListener(this::addSymbol);
      mapboxMap.addOnMapClickListener(this::addSymbol);
      mapboxMap.setStyle(getStyleBuilder(Style.MAPBOX_STREETS), style -> {
        findViewById(R.id.fabStyles).setOnClickListener(v ->
          mapboxMap.setStyle(getStyleBuilder(Utils.INSTANCE.getNextStyle())));

        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
      });
    });
  }

  private boolean addSymbol(LatLng point) {
    if (symbolManager == null) {
      return false;
    }

    symbolManager.create(new SymbolOptions()
      .withLatLng(point)
      .withIconImage(ID_ICON)
      .withIconAnchor(ICON_ANCHOR_BOTTOM)
    );
    return true;
  }

  private Style.Builder getStyleBuilder(@NonNull String styleUrl) {
    return new Style.Builder().fromUri(styleUrl)
      .withImage(ID_ICON, generateBitmap(R.drawable.mapbox_ic_place));
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

    if (symbolManager != null) {
      symbolManager.onDestroy();
    }

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

