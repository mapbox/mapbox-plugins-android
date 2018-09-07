package com.mapbox.mapboxsdk.plugins.annotation;

/**
 * Interface definition for a callback to be invoked when a symbol has been clicked.
 */
public interface OnSymbolClickListener {

  /**
   * Invoked when a symbol has been clicked.
   *
   * @param symbol that has been clicked
   */
  void onSymbolClick(Symbol symbol);

}
