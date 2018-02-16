package com.mapbox.plugins.places.common.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtils {

  private KeyboardUtils() {
  }

  public static void hideKeyboard(View view) {
    InputMethodManager inputMethodManager
      = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void showKeyboard(View view) {
    InputMethodManager inputMethodManager
      = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.showSoftInput(view, 0);
  }
}