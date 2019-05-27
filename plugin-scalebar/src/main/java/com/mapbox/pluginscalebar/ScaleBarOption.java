package com.mapbox.pluginscalebar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.VisibleForTesting;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Builder class from which a scale bar is created.
 */
public class ScaleBarOption {
  public static final int REFRESH_DURATION_DEFAULT = 100;
  public static final int BAR_HEIGHT_DEFAULT = 4;
  public static final int BORDER_WIDTH_DEFAULT = 1;
  public static final int TEXT_SIZE_DEFAULT = 6;
  public static final int MARGIN_TOP_DEFAULT = 8;
  public static final int MARGIN_LEFT_DEFAULT = 10;
  public static final int TEXT_BAR_MARGIN_DEFAULT = 2;
  private final Context context;
  private int refreshDuration;
  private int textColor;
  private int primaryColor;
  private int secondaryColor;
  private float marginTop;
  private float marginLeft;
  private float textBarMargin;
  private float barHeight;
  private float borderWidth;
  private float textSize;
  private boolean isMetricUnit;

  public ScaleBarOption(Context context) {
    this.context = context;
    refreshDuration = REFRESH_DURATION_DEFAULT;
    setBarHeight(BAR_HEIGHT_DEFAULT);
    setBorderWidth(BORDER_WIDTH_DEFAULT);
    setTextSize(TEXT_SIZE_DEFAULT);
    setMarginTop(MARGIN_TOP_DEFAULT);
    setMarginLeft(MARGIN_LEFT_DEFAULT);
    setTextBarMargin(TEXT_BAR_MARGIN_DEFAULT);
    isMetricUnit = LocaleUnitResolver.isMetricSystem();
    textColor = context.getResources().getColor(android.R.color.black);
    primaryColor = context.getResources().getColor(android.R.color.black);
    secondaryColor = context.getResources().getColor(android.R.color.white);
  }

  /**
   * Build a scale bar widget instance with current option settings.
   *
   * @return The built ScaleBarWidget instance.
   */
  ScaleBarWidget build() {
    ScaleBarWidget scaleBarWidget = new ScaleBarWidget(context);
    scaleBarWidget.setBarHeight(barHeight);
    scaleBarWidget.setBorderWidth(borderWidth);
    scaleBarWidget.setMarginLeft(marginLeft);
    scaleBarWidget.setMarginTop(marginTop);
    scaleBarWidget.setTextBarMargin(textBarMargin);
    scaleBarWidget.setMetricUnit(isMetricUnit);
    scaleBarWidget.setRefreshDuration(refreshDuration);
    scaleBarWidget.setPrimaryColor(primaryColor);
    scaleBarWidget.setSecondaryColor(secondaryColor);
    scaleBarWidget.setTextColor(textColor);
    scaleBarWidget.setTextSize(textSize);
    return scaleBarWidget;
  }

  /**
   * Set the duration between two adjacent refreshment,
   * default value is {@link #REFRESH_DURATION_DEFAULT}.
   *
   * @param refreshDuration the refresh duration, in millisecond.
   * @return this
   */
  public ScaleBarOption setRefreshDuration(int refreshDuration) {
    this.refreshDuration = refreshDuration;
    return this;
  }

  /**
   * Set the text color on scale bar,
   * default value is android.R.color.black.
   *
   * @param textColor the text color on scale bar.
   * @return this.
   */
  public ScaleBarOption setTextColor(@ColorInt int textColor) {
    this.textColor = textColor;
    return this;
  }

  /**
   * Set the primary color of the scale bar, will be used to draw odd index blocks,
   * default value is android.R.color.black.
   *
   * @param primaryColor the primary color of the scale bar.
   * @return this.
   */
  public ScaleBarOption setPrimaryColor(@ColorInt int primaryColor) {
    this.primaryColor = primaryColor;
    return this;
  }

  /**
   * Set the secondary color of the scale bar, will be used to draw even index blocks,
   * default value is android.R.color.white.
   *
   * @param secondaryColor the secondaryColor color of the scale bar.
   * @return this.
   */
  public ScaleBarOption setSecondaryColor(@ColorInt int secondaryColor) {
    this.secondaryColor = secondaryColor;
    return this;
  }

  /**
   * Set the margin between scale bar and the top of mapView,
   * default value is {@link #MARGIN_TOP_DEFAULT}.
   *
   * @param marginTop the margin between scale bar and the top of mapView, in dp.
   * @return this.
   */
  public ScaleBarOption setMarginTop(float marginTop) {
    this.marginTop = convertDpToPixel(marginTop);
    return this;
  }

  /**
   * Set the height for blocks in scale bar, default value is {@link #BAR_HEIGHT_DEFAULT}.
   *
   * @param barHeight the height for blocks in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setBarHeight(float barHeight) {
    this.barHeight = convertDpToPixel(barHeight);
    return this;
  }

  /**
   * Set the border width in scale bar, default value is {@link #BORDER_WIDTH_DEFAULT}.
   *
   * @param borderWidth the border width in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setBorderWidth(float borderWidth) {
    this.borderWidth = convertDpToPixel(borderWidth);
    return this;
  }

  /**
   * Set the text size of scale bar, default size is  {@link #TEXT_SIZE_DEFAULT}.
   *
   * @param textSize the text size of scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setTextSize(float textSize) {
    this.textSize = convertDpToPixel(textSize);
    return this;
  }

  /**
   * Set whether to use metric unit or not. The default value is depends on settings of device.
   *
   * @param metricUnit whether to use metric unit or not.
   * @return this.
   */
  public ScaleBarOption setMetricUnit(boolean metricUnit) {
    isMetricUnit = metricUnit;
    return this;
  }

  /**
   * Set the left margin between scale bar and mapView,
   * default value is {@link #MARGIN_LEFT_DEFAULT}.
   *
   * @param marginLeft the left margin between scale bar and mapView, in dp.
   * @return this.
   */
  public ScaleBarOption setMarginLeft(float marginLeft) {
    this.marginLeft = convertDpToPixel(marginLeft);
    return this;
  }

  /**
   * Set the margin between text and blocks inside scale bar,
   * default value is {@link #TEXT_BAR_MARGIN_DEFAULT}.
   *
   * @param textBarMargin the margin between text and blocks inside scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setTextBarMargin(float textBarMargin) {
    this.textBarMargin = convertDpToPixel(textBarMargin);
    return this;
  }

  /**
   * This method converts dp unit to equivalent pixels, depending on device density.
   *
   * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
   * @return A float value to represent px equivalent to dp depending on device density
   */
  private float convertDpToPixel(float dp) {
    return dp * ((float) context.getResources()
      .getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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
  @VisibleForTesting
  public static class LocaleUnitResolver {

    /**
     * Returns true if the user is in a country using the metric system.
     *
     * @return true if user country is using metric, false if imperial
     */
    public static boolean isMetricSystem() {
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
