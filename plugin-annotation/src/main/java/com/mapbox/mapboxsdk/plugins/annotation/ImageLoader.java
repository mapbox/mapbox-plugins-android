package com.mapbox.mapboxsdk.plugins.annotation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.lang.ref.WeakReference;

/**
 * ImageLoader is responsible for automating loading of drawable resources to the style.
 * These images are loaded through the iconImage configuration of SymbolOptions and
 * leverages the OnStyleImageMissingListener to lazily load images.
 */
public class ImageLoader implements MapView.OnStyleImageMissingListener {

  private static final String TAG = "ImageLoader";
  private WeakReference<MapView> mapViewWeakReference;

  ImageLoader(MapView mapView) {
    this.mapViewWeakReference = new WeakReference<>(mapView);
  }

  @Override
  public void onStyleImageMissing(@NonNull final String id) {
    if (id.isEmpty()) {
      return;
    }

    MapView mapView = mapViewWeakReference.get();
    if (mapView != null) {
      Context context = mapView.getContext();
      if (context != null) {
        getDrawable(context, mapView, id);
      }
    }
  }

  private void getDrawable(@NonNull Context context, @NonNull MapView mapView, String id) {
    Resources resources = context.getResources();
    int drawableId = resources.getIdentifier(id, "drawable", context.getPackageName());
    try {
      Drawable drawable = ResourcesCompat.getDrawable(resources, drawableId, context.getTheme());
      final Bitmap bitmap = BitmapUtils.getBitmapFromDrawable(drawable);
      if (bitmap != null) {
        addBitmapToMap(mapView, id, bitmap);
      }
    } catch (Resources.NotFoundException exception) {
      Logger.e(TAG, String.format(
        "Image lookup fail, adding image missing %s failed with exception:", id
      ), exception);
    }
  }

  private void addBitmapToMap(@NonNull MapView mapView, @NonNull final String id, @NonNull final Bitmap bitmap) {
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {
            style.addImage(id, bitmap, true);
          }
        });
      }
    });
  }
}