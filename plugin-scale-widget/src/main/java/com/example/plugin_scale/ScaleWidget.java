package com.example.plugin_scale;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * The scale widget is a visually representation of the scale plugin.
 */
public class ScaleWidget extends LinearLayout {

  private double metersPerPixel;
  private TextView textView;
  private View scaleSize;
  private final int[] scales = {10, 50, 100, 250, 500, 750, 1000, 5000, 10000,100000};

  private final static int[][] scaleMetricTable =
    { {1, 2},
      {2, 2},
      {4, 2},
      {10, 2},
      {20, 2},
      {50, 2},
      {75, 3},
      {100, 2},
      {150, 2},
      {200, 2},
      {300, 3},
      {500, 2},
      {1000, 2},
      {1500, 2},
      {3000, 3},
      {5000, 2},
      {10000, 2},
      {20000, 2},
      {30000, 3},
      {50000, 2},
      {100000, 2},
      {200000, 2},
      {300000, 3},
      {400000, 2},
      {500000, 2},
      {600000, 3},
      {800000, 2}
    };

  public ScaleWidget(Context context) {
    this(context, null);
  }

  public ScaleWidget(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public ScaleWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setMetersPerPixel(double metersPerPixel, int screenWidth) {
    if (this.metersPerPixel == metersPerPixel) {
      return;
    }
    this.metersPerPixel = metersPerPixel;
    Log.d(">>>","Setting meters per pixel %s" + metersPerPixel);

    double max = screenWidth * .8;
    double min = screenWidth * .5;

    int [][]scales = scaleMetricTable;
    int scale = -1;
    int numBars = 0;
    for (int i = 0; i < scales.length; i++) {
      int currScale = scales[i][0];

      double pixels = currScale / metersPerPixel;
      if (pixels <= max) {
        scale = currScale;
        numBars = scales[i][1];
        if (pixels >= min) {
          break;
        }
      }
    }

    updateScaleWidget(metersPerPixel, scale, numBars);
  }

  private void updateScaleWidget(double metersPerPixel, int scale, int numBars) {
    double pixels = scale / metersPerPixel;

    if (textView == null) {
      inflate(getContext(), R.layout.layout, this);

      textView = findViewById(R.id.text_view);
      scaleSize = findViewById(R.id.scale_size);
    }

    scaleSize.getLayoutParams().width = (int) pixels;

    if (scale < 1) {
      textView.setText("");
      return;
    }
    textView.setText("" + scale + " m");
  }

  /**
   * Helper class to determine the user measuring system.
   * <p>
   * Currently supports differentiating between metric vs imperial system.
   * </p>
   * <p>
   * Depends on {@link Locale#getDefault()} which returns the locale used at application startup.
   * </p>
   */
  private static class LocaleUnitResolver {

    /**
     * Returns true if the user is in a country using the metric system.
     *
     * @return true if user country is using metric, false if imperial
     */
    static boolean isMetricSystem() {
      String countryCode = Locale.getDefault().getCountry().toUpperCase();
      switch (countryCode) {
        case ImperialCountryCode.US:
        case ImperialCountryCode.LIBERIA:
        case ImperialCountryCode.MYANMAR:
          return false;
        default:
          return true;
      }
    }

    /**
     * Data class containing uppercase country codes for countries using the imperial system.
     */
    static class ImperialCountryCode {
      static final String US = "US";
      static final String MYANMAR = "MM";
      static final String LIBERIA = "LR";
    }
  }
}
