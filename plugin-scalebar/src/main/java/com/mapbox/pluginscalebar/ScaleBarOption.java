package com.mapbox.pluginscalebar;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.VisibleForTesting;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Builder class from which a scale bar is created.
 */
public class ScaleBarOption {
  public static final int REFRESH_INTERVAL_DEFAULT = 15;

  private final Context context;
  private int refreshInterval;
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
    refreshInterval = REFRESH_INTERVAL_DEFAULT;
    setBarHeight(R.dimen.mapbox_scale_bar_height);
    setBorderWidth(R.dimen.mapbox_scale_bar_border_width);
    setTextSize(R.dimen.mapbox_scale_bar_text_size);
    setMarginTop(R.dimen.mapbox_scale_bar_margin_top);
    setMarginLeft(R.dimen.mapbox_scale_bar_margin_left);
    setTextBarMargin(R.dimen.mapbox_scale_bar_text_margin);
    isMetricUnit = LocaleUnitResolver.isMetricSystem();
    setTextColor(android.R.color.black);
    setPrimaryColor(android.R.color.black);
    setSecondaryColor(android.R.color.white);
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
    scaleBarWidget.setRefreshInterval(refreshInterval);
    scaleBarWidget.setPrimaryColor(primaryColor);
    scaleBarWidget.setSecondaryColor(secondaryColor);
    scaleBarWidget.setTextColor(textColor);
    scaleBarWidget.setTextSize(textSize);
    return scaleBarWidget;
  }

  /**
   * Set plugin's minimum refresh interval,
   * default value is {@link #REFRESH_INTERVAL_DEFAULT}.
   *
   * @param refreshInterval the min refresh interval, in millisecond.
   * @return this
   */
  public ScaleBarOption setRefreshInterval(int refreshInterval) {
    this.refreshInterval = refreshInterval;
    return this;
  }

  /**
   * Set the text color on scale bar,
   * default value is android.R.color.black.
   *
   * @param textColor the text color on scale bar.
   * @return this.
   */
  public ScaleBarOption setTextColor(@ColorRes int textColor) {
    this.textColor = context.getResources().getColor(textColor);
    return this;
  }

  /**
   * Set the primary color of the scale bar, will be used to draw odd index blocks,
   * default value is android.R.color.black.
   *
   * @param primaryColor the primary color of the scale bar.
   * @return this.
   */
  public ScaleBarOption setPrimaryColor(@ColorRes int primaryColor) {
    this.primaryColor = context.getResources().getColor(primaryColor);
    return this;
  }

  /**
   * Set the secondary color of the scale bar, will be used to draw even index blocks,
   * default value is android.R.color.white.
   *
   * @param secondaryColor the secondaryColor color of the scale bar.
   * @return this.
   */
  public ScaleBarOption setSecondaryColor(@ColorRes int secondaryColor) {
    this.secondaryColor = context.getResources().getColor(secondaryColor);
    return this;
  }

  /**
   * Set the margin between scale bar and the top of mapView.
   *
   * @param marginTop the margin between scale bar and the top of mapView, in pixel.
   * @return this.
   */
  public ScaleBarOption setMarginTop(float marginTop) {
    this.marginTop = marginTop;
    return this;
  }

  /**
   * Set the margin between scale bar and the top of mapView.
   *
   * @param marginTop the margin between scale bar and the top of mapView, in dp.
   * @return this.
   */
  public ScaleBarOption setMarginTop(@DimenRes int marginTop) {
    this.marginTop = convertDpToPixel(context.getResources().getDimension(marginTop));
    return this;
  }

  /**
   * Set the height for blocks in scale bar.
   *
   * @param barHeight the height for blocks in scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOption setBarHeight(float barHeight) {
    this.barHeight = barHeight;
    return this;
  }

  /**
   * Set the height for blocks in scale bar.
   *
   * @param barHeight the height for blocks in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setBarHeight(@DimenRes int barHeight) {
    this.barHeight = convertDpToPixel(context.getResources().getDimension(barHeight));
    return this;
  }

  /**
   * Set the border width in scale bar.
   *
   * @param borderWidth the border width in scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOption setBorderWidth(float borderWidth) {
    this.borderWidth = borderWidth;
    return this;
  }

  /**
   * Set the border width in scale bar.
   *
   * @param borderWidth the border width in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setBorderWidth(@DimenRes int borderWidth) {
    this.borderWidth = convertDpToPixel(context.getResources().getDimension(borderWidth));
    return this;
  }

  /**
   * Set the text size of scale bar.
   *
   * @param textSize the text size of scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOption setTextSize(float textSize) {
    this.textSize = textSize;
    return this;
  }

  /**
   * Set the text size of scale bar.
   *
   * @param textSize the text size of scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setTextSize(@DimenRes int textSize) {
    this.textSize = convertDpToPixel(context.getResources().getDimension(textSize));
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
   * Set the left margin between scale bar and mapView.
   *
   * @param marginLeft the left margin between scale bar and mapView, in pixel.
   * @return this.
   */
  public ScaleBarOption setMarginLeft(float marginLeft) {
    this.marginLeft = marginLeft;
    return this;
  }

  /**
   * Set the left margin between scale bar and mapView.
   *
   * @param marginLeft the left margin between scale bar and mapView, in dp.
   * @return this.
   */
  public ScaleBarOption setMarginLeft(@DimenRes int marginLeft) {
    this.marginLeft = convertDpToPixel(context.getResources().getDimension(marginLeft));
    return this;
  }

  /**
   * Set the margin between text and blocks inside scale bar.
   *
   * @param textBarMargin the margin between text and blocks inside scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOption setTextBarMargin(float textBarMargin) {
    this.textBarMargin = textBarMargin;
    return this;
  }

  /**
   * Set the margin between text and blocks inside scale bar.
   *
   * @param textBarMargin the margin between text and blocks inside scale bar, in dp.
   * @return this.
   */
  public ScaleBarOption setTextBarMargin(@DimenRes int textBarMargin) {
    this.textBarMargin = convertDpToPixel(context.getResources().getDimension(textBarMargin));
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
