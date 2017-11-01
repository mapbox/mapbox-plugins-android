package com.mapbox.mapboxsdk.plugins.cluster.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * RotationLayout rotates the contents of the layout by multiples of 90 degrees.
 * <p>
 * May not work with padding.
 * </p>
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 */
public class RotationLayout extends FrameLayout {
  private int mRotation;

  public RotationLayout(Context context) {
    super(context);
  }

  public RotationLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RotationLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mRotation == 1 || mRotation == 3) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  /**
   * @param degrees the rotation, in degrees.
   */
  public void setViewRotation(int degrees) {
    mRotation = ((degrees + 360) % 360) / 90;
  }


  @Override
  public void dispatchDraw(Canvas canvas) {
    if (mRotation == 0) {
      super.dispatchDraw(canvas);
      return;
    }

    if (mRotation == 1) {
      canvas.translate(getWidth(), 0);
      canvas.rotate(90, getWidth() / 2, 0);
      canvas.translate(getHeight() / 2, getWidth() / 2);
    } else if (mRotation == 2) {
      canvas.rotate(180, getWidth() / 2, getHeight() / 2);
    } else {
      canvas.translate(0, getHeight());
      canvas.rotate(270, getWidth() / 2, 0);
      canvas.translate(getHeight() / 2, -getWidth() / 2);
    }

    super.dispatchDraw(canvas);
  }
}
