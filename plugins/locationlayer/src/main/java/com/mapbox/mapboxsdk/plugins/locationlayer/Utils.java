package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mapbox.services.commons.geojson.Point;

class Utils {

  static Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else {
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
    }
  }

  /**
   * Used for animating the user location icon
   *
   * @since 0.1.0
   */
  static class PointEvaluator implements TypeEvaluator<Point> {
    // Method is used to interpolate the user icon animation.
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
      return Point.fromCoordinates(new double[] {
        startValue.getCoordinates().getLongitude() + (
          (endValue.getCoordinates().getLongitude() - startValue.getCoordinates().getLongitude()) * fraction),
        startValue.getCoordinates().getLatitude() + (
          (endValue.getCoordinates().getLatitude() - startValue.getCoordinates().getLatitude()) * fraction)
      });
    }
  }
}
