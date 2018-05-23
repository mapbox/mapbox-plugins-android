package com.mapbox.mapboxsdk.plugins.cluster.ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.mapbox.mapboxsdk.plugins.cluster.R;

/**
 * Draws a bubble with a shadow, filled with any color.
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 * @deprecated use runtime styling to cluster markers instead
 */
@Deprecated
class BubbleDrawable extends Drawable {

  private final Drawable mShadow;
  private final Drawable mMask;
  private int mColor = Color.WHITE;

  @Deprecated
  public BubbleDrawable(Resources res) {
    mMask = res.getDrawable(R.drawable.mbx_bubble_mask);
    mShadow = res.getDrawable(R.drawable.mbx_bubble_shadow);
  }

  @Deprecated
  public void setColor(int color) {
    mColor = color;
  }

  @Deprecated
  @Override
  public void draw(Canvas canvas) {
    mMask.draw(canvas);
    canvas.drawColor(mColor, PorterDuff.Mode.SRC_IN);
    mShadow.draw(canvas);
  }

  @Deprecated
  @Override
  public void setAlpha(int alpha) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public void setColorFilter(ColorFilter cf) {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Deprecated
  @Override
  public void setBounds(int left, int top, int right, int bottom) {
    mMask.setBounds(left, top, right, bottom);
    mShadow.setBounds(left, top, right, bottom);
  }

  @Deprecated
  @Override
  public void setBounds(Rect bounds) {
    mMask.setBounds(bounds);
    mShadow.setBounds(bounds);
  }

  @Deprecated
  @Override
  public boolean getPadding(Rect padding) {
    return mMask.getPadding(padding);
  }
}
