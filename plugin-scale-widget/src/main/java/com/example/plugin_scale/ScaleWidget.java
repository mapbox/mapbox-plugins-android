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

  public ScaleWidget(Context context) {
    this(context, null);
  }

  public ScaleWidget(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public ScaleWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeView();
  }

  private void initializeView() {
    inflate(getContext(), R.layout.layout, this);
    textView = findViewById(R.id.text_view);
    scaleSize = findViewById(R.id.scale_size);
  }

  public void setMetersPerPixel(double metersPerPixel, int screenWidth) {
    if (this.metersPerPixel == metersPerPixel) {
      return;
    }
    this.metersPerPixel = metersPerPixel;
    Log.d(">>>","Setting meters per pixel %s" + metersPerPixel);
//    textView.setText(String.format(Locale.getDefault(),"Scale %s",metersPerPixel));


    double max = screenWidth * .8;
    double min = screenWidth * .5;


    int scale = -1;
    for (int currScale : scales) {
      double pixels = currScale / metersPerPixel;
      if (pixels <= max) {
        scale = currScale;
        if (pixels >= min) {
          break;
        }
      }
    }

    int pixels = scale / ((int) metersPerPixel);
    scaleSize.getLayoutParams().width = pixels;
    LayoutParams layoutParams = (LayoutParams) scaleSize.getLayoutParams();
    layoutParams.width = pixels;

    scaleSize.setLayoutParams(layoutParams);
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
