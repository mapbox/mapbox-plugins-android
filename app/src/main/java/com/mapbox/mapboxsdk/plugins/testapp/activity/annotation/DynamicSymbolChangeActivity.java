package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;

/**
 * Test activity showcasing updating a Marker position, title, icon and snippet.
 */
public class DynamicSymbolChangeActivity extends AppCompatActivity {

  private static final LatLng LAT_LNG_CHELSEA = new LatLng(51.481670, -0.190849);
  private static final LatLng LAT_LNG_ARSENAL = new LatLng(51.555062, -0.108417);

  private static final String ID_ICON_1 = "com.mapbox.annotationplugin.icon.1";
  private static final String ID_ICON_2 = "com.mapbox.annotationplugin.icon.2";

  private SymbolManager symbolManager;
  private MapView mapView;
  private MapboxMap mapboxMap;
  private Symbol symbol;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);

    mapView = findViewById(R.id.mapView);
    mapView.setTag(false);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {
      DynamicSymbolChangeActivity.this.mapboxMap = mapboxMap;

      LatLng target = new LatLng(51.506675, -0.128699);

      mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder()
          .bearing(90)
          .tilt(40)
          .zoom(10)
          .target(target)
          .build()
      ));

      mapboxMap.addImage(ID_ICON_1, generateBitmap(R.drawable.mapbox_ic_place));
      mapboxMap.addImage(ID_ICON_2, generateBitmap(R.drawable.mapbox_ic_offline));

      symbolManager = new SymbolManager(mapView, mapboxMap);
      symbolManager.setIconAllowOverlap(true);
      symbolManager.setTextAllowOverlap(true);

      // Create Symbol
      SymbolOptions SymbolOptions = new SymbolOptions()
        .withLatLng(LAT_LNG_CHELSEA)
        .withIconImage(ID_ICON_1);

      symbol = symbolManager.create(SymbolOptions);
    });

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setVisibility(MapView.VISIBLE);
    fab.setOnClickListener(view -> {
      if (mapboxMap != null) {
        updateSymbol();
      }
    });
  }

  private void updateSymbol() {
    // update model
    boolean first = (boolean) mapView.getTag();
    mapView.setTag(!first);

    // update symbol
    symbol.setLatLng(first ? LAT_LNG_CHELSEA : LAT_LNG_ARSENAL);
    symbol.setIconImage(first ? ID_ICON_1 : ID_ICON_2);
    symbolManager.update(symbol);
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
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
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
