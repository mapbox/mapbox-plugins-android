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
 */
class BubbleDrawable extends Drawable {

  private final Drawable mShadow;
  private final Drawable mMask;
  private int mColor = Color.WHITE;

  public BubbleDrawable(Resources res) {
    mMask = res.getDrawable(R.drawable.amu_bubble_mask);
    mShadow = res.getDrawable(R.drawable.amu_bubble_shadow);
  }

  public void setColor(int color) {
    mColor = color;
  }

  @Override
  public void draw(Canvas canvas) {
    mMask.draw(canvas);
    canvas.drawColor(mColor, PorterDuff.Mode.SRC_IN);
    mShadow.draw(canvas);
  }

  @Override
  public void setAlpha(int alpha) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public void setBounds(int left, int top, int right, int bottom) {
    mMask.setBounds(left, top, right, bottom);
    mShadow.setBounds(left, top, right, bottom);
  }

  @Override
  public void setBounds(Rect bounds) {
    mMask.setBounds(bounds);
    mShadow.setBounds(bounds);
  }

  @Override
  public boolean getPadding(Rect padding) {
    return mMask.getPadding(padding);
  }
}
