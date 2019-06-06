package com.mapbox.pluginscalebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * The scale widget is a visual representation of the scale bar plugin.
 */
public class ScaleBarWidget extends View {
  private static int MSG_WHAT = 0;
  private final Paint textPaint = new Paint();
  private final Paint barPaint = new Paint();
  private int refreshInterval;
  private int textColor;
  private int primaryColor;
  private int secondaryColor;
  private int mapViewWidth;
  private float marginLeft;
  private float marginTop;
  private float textBarMargin;
  private float maxBarWidth;
  private float barHeight;
  private float borderWidth;
  private float textSize;
  private double distancePerPixel;
  private boolean isMetricUnit;
  private ArrayList<Pair<Integer, Integer>> scaleTable;
  private String unit;
  private final RefreshHandler refreshHandler;

  ScaleBarWidget(@NonNull Context context) {
    super(context);
    textPaint.setAntiAlias(true);
    textPaint.setTextAlign(Paint.Align.CENTER);
    barPaint.setAntiAlias(true);
    refreshHandler = new RefreshHandler(this);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (distancePerPixel <= 0) {
      return;
    }
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

    canvas.drawRect(marginLeft - borderWidth * 2,
      textBarMargin + textSize + marginTop - borderWidth * 2,
      marginLeft + unitBarWidth * pair.second + borderWidth * 2,
      textBarMargin + textSize + marginTop + barHeight + borderWidth * 2,
      barPaint);
    barPaint.setColor(primaryColor);
    canvas.drawRect(marginLeft - borderWidth,
      textBarMargin + textSize + marginTop - borderWidth,
      marginLeft + unitBarWidth * pair.second + borderWidth,
      textBarMargin + textSize + marginTop + barHeight + borderWidth,
      barPaint);

    //Drawing the fill
    barPaint.setStyle(Paint.Style.FILL);
    int i = 0;
    for (; i < pair.second; i++) {
      barPaint.setColor(i % 2 == 0 ? primaryColor : secondaryColor);
      String text = i == 0 ? String.valueOf(unitDistance * i) : unitDistance * i + unit;
      canvas.drawText(text,
        marginLeft + unitBarWidth * i,
        textSize + marginTop,
        textPaint);
      canvas.drawRect(marginLeft + unitBarWidth * i,
        textBarMargin + textSize + marginTop,
        marginLeft + unitBarWidth * (1 + i),
        textBarMargin + textSize + marginTop + barHeight,
        barPaint);
    }
    canvas.drawText(unitDistance * i + unit,
      marginLeft + unitBarWidth * i,
      textSize + marginTop,
      textPaint);
  }

  /**
   * Update the scale when mapView's scale has changed.
   *
   * @param metersPerPixel how many meters in each pixel.
   */
  void setDistancePerPixel(double metersPerPixel) {
    this.distancePerPixel = isMetricUnit ? metersPerPixel : metersPerPixel * ScaleBarConstants.FEET_PER_METER;
    if (!refreshHandler.hasMessages(MSG_WHAT)) {
      refreshHandler.sendEmptyMessageDelayed(MSG_WHAT, refreshInterval);
    }
  }

  /**
   * Get plugin's minimum refresh interval, in millisecond.
   *
   * @return refresh duration
   */
  public int getRefreshInterval() {
    return refreshInterval;
  }

  /**
   * Set plugin's minimum refresh interval, in millisecond.
   *
   * @param refreshInterval the refresh duration.
   */
  public void setRefreshInterval(int refreshInterval) {
    this.refreshInterval = refreshInterval;
  }

  /**
   * Get the margin between text and blocks.
   *
   * @return margin between text and blocks, in pixel.
   */
  public float getTextBarMargin() {
    return textBarMargin;
  }

  /**
   * Set the margin between text and blocks inside scale bar.
   *
   * @param textBarMargin the margin between text and blocks inside scale bar, in pixel.
   */
  public void setTextBarMargin(float textBarMargin) {
    this.textBarMargin = textBarMargin;
  }

  /**
   * Get the left margin between scale bar and mapView.
   *
   * @return the left margin between scale bar and mapView, in pixel
   */
  public float getMarginLeft() {
    return marginLeft;
  }

  /**
   * Set the left margin between scale bar and mapView.
   *
   * @param marginLeft the left margin between scale bar and mapView, in pixel.
   */
  public void setMarginLeft(float marginLeft) {
    this.marginLeft = marginLeft;
    maxBarWidth = mapViewWidth / 2f - marginLeft;
  }

  /**
   * Get the bar height for blocks.
   *
   * @return the height for blocks in scale bar, in pixel.
   */
  public float getBarHeight() {
    return barHeight;
  }

  /**
   * Set the height for blocks in scale bar.
   *
   * @param barHeight the height for blocks in scale bar, in pixel.
   */
  public void setBarHeight(float barHeight) {
    this.barHeight = barHeight;
  }

  /**
   * Get the margin between scale bar and the top of mapView,
   *
   * @return the margin between scale bar and the top of mapView, in pixel.
   */
  public float getMarginTop() {
    return marginTop;
  }

  /**
   * Set the margin between scale bar and the top of mapView.
   *
   * @param marginTop the margin between scale bar and the top of mapView, in pixel.
   */
  public void setMarginTop(float marginTop) {
    this.marginTop = marginTop;
  }

  /**
   * Get the border width in scale bar.
   *
   * @return the border width in scale bar, in pixel
   */
  public float getBorderWidth() {
    return borderWidth;
  }

  /**
   * Set the border width in scale bar.
   *
   * @param borderWidth the border width in scale bar, in pixel.
   */
  public void setBorderWidth(float borderWidth) {
    this.borderWidth = borderWidth;
  }

  /**
   * Get the text size of scale bar.
   *
   * @return the text size of scale bar, in pixel.
   */
  public float getTextSize() {
    return textSize;
  }

  /**
   * Set the text size of scale bar.
   *
   * @param textSize the text size of scale bar, in pixel.
   */
  public void setTextSize(float textSize) {
    this.textSize = textSize;
    textPaint.setTextSize(textSize);
  }

  /**
   * Get the current setting for metrix unit.
   *
   * @return true if using metrix unit, otherwise false.
   */
  public boolean isMetricUnit() {
    return isMetricUnit;
  }

  /**
   * Set whether to use metric unit or not.
   *
   * @param metricUnit whether to use metric unit or not.
   */
  public void setMetricUnit(boolean metricUnit) {
    isMetricUnit = metricUnit;
    scaleTable = isMetricUnit ? ScaleBarConstants.metricTable : ScaleBarConstants.imperialTable;
    unit = isMetricUnit ? ScaleBarConstants.METER_UNIT : ScaleBarConstants.FEET_UNIT;
  }

  /**
   * Get the color for text.
   *
   * @return the color for text.
   */
  public int getTextColor() {
    return textColor;
  }

  /**
   * Set the text color on scale bar,
   *
   * @param textColor the text color on scale bar.
   */
  public void setTextColor(@ColorInt int textColor) {
    this.textColor = textColor;
    textPaint.setColor(textColor);
  }

  /**
   * Get the primary color of the scale bar.
   *
   * @return the primary color of the scale bar.
   */
  public int getPrimaryColor() {
    return primaryColor;
  }

  /**
   * Set the primary color of the scale bar, will be used to draw odd index blocks,
   *
   * @param primaryColor the primary color of the scale bar, in pixel.
   */
  public void setPrimaryColor(@ColorInt int primaryColor) {
    this.primaryColor = primaryColor;
  }

  /**
   * Get the secondary color of the scale bar.
   *
   * @return the secondary color of the scale bar.
   */
  public int getSecondaryColor() {
    return secondaryColor;
  }

  /**
   * Set the secondary color of the scale bar, will be used to draw even index blocks,
   *
   * @param secondaryColor the secondaryColor color of the scale bar, in pixel.
   */
  public void setSecondaryColor(@ColorInt int secondaryColor) {
    this.secondaryColor = secondaryColor;
  }

  /**
   * Get the width of current mapView.
   *
   * @return the width of current mapView.
   */
  public int getMapViewWidth() {
    return mapViewWidth;
  }

  /**
   * Set the width of current mapView.
   *
   * @param mapViewWidth mapView's width in pixel.
   */
  void setMapViewWidth(int mapViewWidth) {
    this.mapViewWidth = mapViewWidth;
    maxBarWidth = mapViewWidth / 2f - marginLeft;
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
      ScaleBarWidget scaleBarWidget = scaleBarWidgetWeakReference.get();
      if (msg.what == MSG_WHAT && scaleBarWidget != null) {
        scaleBarWidget.invalidate();
      }
    }
  }

}
