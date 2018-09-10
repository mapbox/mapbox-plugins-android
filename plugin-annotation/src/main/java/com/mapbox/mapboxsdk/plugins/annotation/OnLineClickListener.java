package com.mapbox.mapboxsdk.plugins.annotation;

/**
 * Interface definition for a callback to be invoked when a line has been clicked.
 */
public interface OnLineClickListener {

  /**
   * Invoked when a line has been clicked.
   *
   * @param line that has been clicked
   */
  void onLineClick(Line line);

}
