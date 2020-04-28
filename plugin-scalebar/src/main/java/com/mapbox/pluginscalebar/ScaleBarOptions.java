package com.mapbox.pluginscalebar;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;

import java.util.Locale;

/**
 * Builder class from which a scale bar is created.
 */
public class ScaleBarOptions {
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
  private float ratio;
  private boolean showTextBorder;
  private float textBorderWidth;

  public ScaleBarOptions(@NonNull Context context) {
    this.context = context;
    refreshInterval = REFRESH_INTERVAL_DEFAULT;
    setBarHeight(R.dimen.mapbox_scale_bar_height);
    setBorderWidth(R.dimen.mapbox_scale_bar_border_width);
    setTextSize(R.dimen.mapbox_scale_bar_text_size);
    setMarginTop(R.dimen.mapbox_scale_bar_margin_top);
    setMarginLeft(R.dimen.mapbox_scale_bar_margin_left);
    setTextBarMargin(R.dimen.mapbox_scale_bar_text_margin);
    setTextBorderWidth(R.dimen.mapbox_scale_bar_text_border_width);
    setShowTextBorder(true);
    isMetricUnit = LocaleUnitResolver.isMetricSystem();
    setTextColor(android.R.color.black);
    setPrimaryColor(android.R.color.black);
    setSecondaryColor(android.R.color.white);
    setMaxWidthRatio(0.5f);
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
    scaleBarWidget.setRatio(ratio);
    scaleBarWidget.setShowTextBorder(showTextBorder);
    scaleBarWidget.setTextBorderWidth(textBorderWidth);
    return scaleBarWidget;
  }

  /**
   * Set plugin's minimum refresh interval,
   * default value is {@link #REFRESH_INTERVAL_DEFAULT}.
   *
   * @param refreshInterval the min refresh interval, in millisecond.
   * @return this
   */
  public ScaleBarOptions setRefreshInterval(int refreshInterval) {
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
  public ScaleBarOptions setTextColor(@ColorRes int textColor) {
    this.textColor = ContextCompat.getColor(context, textColor);
    return this;
  }

  /**
   * Set the primary color of the scale bar, will be used to draw odd index blocks,
   * default value is android.R.color.black.
   *
   * @param primaryColor the primary color of the scale bar.
   * @return this.
   */
  public ScaleBarOptions setPrimaryColor(@ColorRes int primaryColor) {
    this.primaryColor = ContextCompat.getColor(context, primaryColor);
    return this;
  }

  /**
   * Set the secondary color of the scale bar, will be used to draw even index blocks,
   * default value is android.R.color.white.
   *
   * @param secondaryColor the secondaryColor color of the scale bar.
   * @return this.
   */
  public ScaleBarOptions setSecondaryColor(@ColorRes int secondaryColor) {
    this.secondaryColor = ContextCompat.getColor(context, secondaryColor);
    return this;
  }

  /**
   * Set the margin between scale bar and the top of mapView.
   *
   * @param marginTop the margin between scale bar and the top of mapView, in pixel.
   * @return this.
   */
  public ScaleBarOptions setMarginTop(float marginTop) {
    this.marginTop = marginTop;
    return this;
  }

  /**
   * Set the margin between scale bar and the top of mapView.
   *
   * @param marginTop the margin between scale bar and the top of mapView, in dp.
   * @return this.
   */
  public ScaleBarOptions setMarginTop(@DimenRes int marginTop) {
    this.marginTop = context.getResources().getDimension(marginTop);
    return this;
  }

  /**
   * Set the height for blocks in scale bar.
   *
   * @param barHeight the height for blocks in scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOptions setBarHeight(float barHeight) {
    this.barHeight = barHeight;
    return this;
  }

  /**
   * Set the height for blocks in scale bar.
   *
   * @param barHeight the height for blocks in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOptions setBarHeight(@DimenRes int barHeight) {
    this.barHeight = context.getResources().getDimension(barHeight);
    return this;
  }

  /**
   * Set the border width in scale bar.
   *
   * @param borderWidth the border width in scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOptions setBorderWidth(float borderWidth) {
    this.borderWidth = borderWidth;
    return this;
  }

  /**
   * Set the border width in scale bar.
   *
   * @param borderWidth the border width in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOptions setBorderWidth(@DimenRes int borderWidth) {
    this.borderWidth = context.getResources().getDimension(borderWidth);
    return this;
  }

  /**
   * Set the border width for texts in scale bar.
   *
   * @param textBorderWidth the border width for texts in scale bar, in dp.
   * @return this.
   */
  public ScaleBarOptions setTextBorderWidth(@DimenRes int textBorderWidth) {
    this.textBorderWidth = context.getResources().getDimension(textBorderWidth);
    return this;
  }


  /**
   * Set the border width for texts in scale bar.
   *
   * @param textBorderWidth the border width for texts in scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOptions setTextBorderWidth(float textBorderWidth) {
    this.textBorderWidth = textBorderWidth;
    return this;
  }

  /**
   * Set whether to show the text border.
   *
   * @param showTextBorder whether to show the text border or not.
   * @return this.
   */
  public ScaleBarOptions setShowTextBorder(boolean showTextBorder) {
    this.showTextBorder = showTextBorder;
    return this;
  }

  /**
   * Set the text size of scale bar.
   *
   * @param textSize the text size of scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOptions setTextSize(float textSize) {
    this.textSize = textSize;
    return this;
  }

  /**
   * Set the text size of scale bar.
   *
   * @param textSize the text size of scale bar, in dp.
   * @return this.
   */
  public ScaleBarOptions setTextSize(@DimenRes int textSize) {
    this.textSize = context.getResources().getDimension(textSize);
    return this;
  }

  /**
   * Set whether to use metric unit or not. The default value is depends on settings of device.
   *
   * @param metricUnit whether to use metric unit or not.
   * @return this.
   */
  public ScaleBarOptions setMetricUnit(boolean metricUnit) {
    isMetricUnit = metricUnit;
    return this;
  }

  /**
   * Set the left margin between scale bar and mapView.
   *
   * @param marginLeft the left margin between scale bar and mapView, in pixel.
   * @return this.
   */
  public ScaleBarOptions setMarginLeft(float marginLeft) {
    this.marginLeft = marginLeft;
    return this;
  }

  /**
   * Set the left margin between scale bar and mapView.
   *
   * @param marginLeft the left margin between scale bar and mapView, in dp.
   * @return this.
   */
  public ScaleBarOptions setMarginLeft(@DimenRes int marginLeft) {
    this.marginLeft = context.getResources().getDimension(marginLeft);
    return this;
  }

  /**
   * Set the margin between text and blocks inside scale bar.
   *
   * @param textBarMargin the margin between text and blocks inside scale bar, in pixel.
   * @return this.
   */
  public ScaleBarOptions setTextBarMargin(float textBarMargin) {
    this.textBarMargin = textBarMargin;
    return this;
  }

  /**
   * Set the margin between text and blocks inside scale bar.
   *
   * @param textBarMargin the margin between text and blocks inside scale bar, in dp.
   * @return this.
   */
  public ScaleBarOptions setTextBarMargin(@DimenRes int textBarMargin) {
    this.textBarMargin = context.getResources().getDimension(textBarMargin);
    return this;
  }

  /**
   * Set the ratio of scale bar max width compared with MapView width.
   *
   * @param ratio the ratio scale bar will use, must be in the range from 0.1f to 1.0f.
   */
  public ScaleBarOptions setMaxWidthRatio(@FloatRange(from = 0.1f, to = 1.0f) float ratio) {
    this.ratio = ratio;
    return this;
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
