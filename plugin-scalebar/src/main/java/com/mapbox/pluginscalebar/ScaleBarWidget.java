package com.mapbox.pluginscalebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.util.Pair;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * The scale widget is a visual representation of the scale bar plugin.
 */
class ScaleBarWidget extends View {
  private static int MSG_WHAT = 0;
  private static int REFRESH_DURATION = 300;
  private int textColor;
  private int primaryColor;
  private int secondaryColor;
  private int mapViewWidth;
  private int margin;
  private int maxBarWidth;
  private double distancePerPixel;
  private final Paint textPaint = new Paint();
  private final Paint barPaint = new Paint();
  private boolean isMetricSystem;
  private ArrayList<Pair<Integer, Integer>> scaleTable;
  private String unit;
  private RefreshHandler refreshHandler;

  public ScaleBarWidget(Context context) {
    super(context);
    textPaint.setAntiAlias(true);
    textPaint.setTextSize(20);
    textPaint.setTextAlign(Paint.Align.CENTER);
    barPaint.setAntiAlias(true);
    textColor = context.getResources().getColor(android.R.color.black);
    primaryColor = context.getResources().getColor(android.R.color.black);
    secondaryColor = context.getResources().getColor(android.R.color.white);
    isMetricSystem = LocaleUnitResolver.isMetricSystem();
    scaleTable = isMetricSystem ? ScaleBarConstants.metricTable : ScaleBarConstants.imperialTable;
    unit = isMetricSystem ? ScaleBarConstants.METER_UNIT : ScaleBarConstants.FEET_UNIT;
    refreshHandler = new RefreshHandler(this);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (distancePerPixel <= 0) {
      return;
    }
    textPaint.setColor(textColor);
    double maxDistance = mapViewWidth * distancePerPixel / 2;
    Pair<Integer, Integer> pair = scaleTable.get(0);
    for (int i = 1; i < scaleTable.size(); i++) {
      pair = scaleTable.get(i);
      if (pair.first > maxDistance) {
        //use the last scale here, otherwise the scale will be too large
        pair = scaleTable.get(i - 1);
        break;
      }
    }

    int unitDistance = pair.first / pair.second;
    float unitBarWidth = maxBarWidth / 2f;
    if (unitDistance == 0) {
      unitDistance = 1;
    } else {
      unitBarWidth = (float) (unitDistance / distancePerPixel);
    }
    //Drawing the surrounding borders
    barPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    barPaint.setColor(secondaryColor);
    int barHeight = 10;
    int borderWidth = 2;
    canvas.drawRect(margin - borderWidth * 2, margin - borderWidth * 2,
      margin + unitBarWidth * pair.second + borderWidth * 2, margin + barHeight + borderWidth * 2, barPaint);
    barPaint.setColor(primaryColor);
    canvas.drawRect(margin - borderWidth, margin - borderWidth,
      margin + unitBarWidth * pair.second + borderWidth, margin + barHeight + borderWidth, barPaint);

    //Drawing the fill
    barPaint.setStyle(Paint.Style.FILL);
    int i = 0;
    for (; i < pair.second; i++) {
      barPaint.setColor(i % 2 == 0 ? primaryColor : secondaryColor);
      String text = i == 0 ? String.valueOf(unitDistance * i) : unitDistance * i + unit;
      canvas.drawText(text, margin + unitBarWidth * i, margin - barHeight, textPaint);
      canvas.drawRect(margin + unitBarWidth * i, margin, margin + unitBarWidth * (1 + i),
        margin + barHeight, barPaint);
    }
    canvas.drawText(unitDistance * i + unit, margin + unitBarWidth * i, margin - barHeight, textPaint);

  }

  /**
   * Update the scale when mapView's scale has changed.
   *
   * @param metersPerPixel how many meters in each pixel.
   */
  void setDistancePerPixel(double metersPerPixel) {
    this.distancePerPixel = isMetricSystem ? metersPerPixel : metersPerPixel * ScaleBarConstants.FEET_PER_METER;
    if (!refreshHandler.hasMessages(MSG_WHAT)) {
      refreshHandler.sendEmptyMessageDelayed(MSG_WHAT, REFRESH_DURATION);
    }
  }

  /**
   * Set the width of current mapView.
   *
   * @param mapViewWidth mapView's wdith.
   */
  void setMapViewWidth(int mapViewWidth) {
    this.mapViewWidth = mapViewWidth;
    margin = mapViewWidth / 20;
    maxBarWidth = mapViewWidth / 2 - margin;
  }

  /**
   * Set colors for the scale bar.
   *
   * @param textColor      The color for the texts above scale bar.
   * @param primaryColor   The color for odd number index bars.
   * @param secondaryColor The color for even number index bars.
   */
  void setColors(@ColorInt int textColor, @ColorInt int primaryColor, @ColorInt int secondaryColor) {
    this.textColor = textColor;
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
  }

  int getTextColor() {
    return textColor;
  }

  int getPrimaryColor() {
    return primaryColor;
  }

  int getSecondaryColor() {
    return secondaryColor;
  }

  int getMapViewWidth() {
    return mapViewWidth;
  }

  /**
   * Handler class to limit the refresh frequent.
   */
  private static class RefreshHandler extends Handler {
    WeakReference<ScaleBarWidget> scaleBarWidgetWeakReference;

    RefreshHandler(ScaleBarWidget scaleBarWidget) {
      scaleBarWidgetWeakReference = new WeakReference<>(scaleBarWidget);
    }

    public void handleMessage(Message msg) {
      if (msg.what == MSG_WHAT && scaleBarWidgetWeakReference.get() != null) {
        scaleBarWidgetWeakReference.get().invalidate();
      }
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
