package com.mapbox.mapboxsdk.plugins.cluster.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Resembles a square sized TextView.
 * <p>
 * <p>
 * Inspired by https://github.com/googlemaps/android-maps-utils.
 * </p>
 */
public class SquareTextView extends AppCompatTextView {
  private int mOffsetTop = 0;
  private int mOffsetLeft = 0;

  public SquareTextView(Context context) {
    super(context);
  }

  public SquareTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    int dimension = Math.max(width, height);
    if (width > height) {
      mOffsetTop = width - height;
      mOffsetLeft = 0;
    } else {
      mOffsetTop = 0;
      mOffsetLeft = height - width;
    }
    setMeasuredDimension(dimension, dimension);
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.translate(mOffsetLeft / 2, mOffsetTop / 2);
    super.draw(canvas);
  }
}
