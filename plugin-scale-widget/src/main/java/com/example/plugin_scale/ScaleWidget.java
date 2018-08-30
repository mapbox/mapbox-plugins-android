package com.example.plugin_scale;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import java.util.Locale;

/**
 * The scale widget is a visually representation of the scale plugin.
 */
public class ScaleWidget extends AppCompatTextView {

  private double metersPerPixel;

  public ScaleWidget(Context context) {
    super(context);
    Log.d(">>>>", "Creating ScaleWidget: uses metric system: %s" + LocaleUnitResolver.isMetricSystem());
  }

  public void setMetersPerPixel(double metersPerPixel) {
    if (this.metersPerPixel == metersPerPixel) {
      return;
    }
    this.metersPerPixel = metersPerPixel;
    Log.d(">>>","Setting meters per pixel %s" + metersPerPixel);
    setText(String.format(Locale.getDefault(),"Scale %s",metersPerPixel));
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
