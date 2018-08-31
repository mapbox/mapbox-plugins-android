package com.example.plugin_scale;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.View;

import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import timber.log.Timber;

/**
 * The scale widget is a visually representation of the scale plugin.
 */
public class ScaleWidget extends GridLayout {

  private double metersPerPixel;

  private TextView scaleTexts[];
  private View scaleBars[];


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
      {800000, 2},
      {1000000, 2},
      {10000000, 2},
    };

  public ScaleWidget(Context context) {
    this(context, null);
  }

  public ScaleWidget(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public ScaleWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.layout, this);

    scaleBars  = new View[]{
      findViewById(R.id.scale_size1),
      findViewById(R.id.scale_size2),
      findViewById(R.id.scale_size3)};

    scaleTexts =  new TextView[]{
      findViewById(R.id.text_view0),
      findViewById(R.id.text_view1),
      findViewById(R.id.text_view2),
      findViewById(R.id.text_view3)};
  }

  public void setMetersPerPixel(double metersPerPixel, int screenWidth) {

    if (this.metersPerPixel == metersPerPixel) {
      return;
    }

    this.metersPerPixel = metersPerPixel;
    double max = screenWidth * .8 * metersPerPixel;
    double min = screenWidth * .5 * metersPerPixel;

    Timber.e(">>>> screenWidth=%s metersPerPixel=%s", screenWidth, metersPerPixel);

    int [][]scales = scaleMetricTable;
    int scale = -1;
    int numBars = 0;
    for (int i = 0; i < scales.length; i++) {
      int currScale = scales[i][0];
      if (currScale <= max) {
        scale = currScale;
        numBars = scales[i][1];
        if (currScale >= min) {
          break;
        }
      }
    }
    Timber.e(">>>> MIN= %s MAX=%S scale=%s numBars=%s", min, max, scale, numBars);

    updateScaleWidget(metersPerPixel, scale, numBars);
  }

  private void updateScaleWidget(double metersPerPixel, int scale, int numBars) {

    if (scale < 1) {
      // textView.setText("");
      return;
    }

    double pixels =  ((double)scale) / metersPerPixel;
    getLayoutParams().width = (int)(pixels);

    // scaleTexts[0].setText("0 m");
    int scalePerBar = scale / numBars;
    int pixelsPerBar = (int) ((double)scalePerBar / metersPerPixel);
    Timber.e(">>>> UPDATE SCALE scalePerBar=%s pixelsPerBar=%s numBars=%s", scalePerBar, pixelsPerBar, numBars);
    for (int i = 0, curScale = scalePerBar; i < numBars; i++, curScale += scalePerBar) {
       scaleBars[i].getLayoutParams().width = pixelsPerBar;
       scaleTexts[i+1].setText("" + curScale + " m");

      Timber.e(">>>>Setting scale i=%s curScale=%s", i, curScale);
    }
    if (numBars == 2) {
      scaleBars[2].getLayoutParams().width = 0;
      scaleTexts[3].setText("");
    }
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
