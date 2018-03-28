package com.mapbox.mapboxsdk.plugins.offline.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;

public final class ColorUtils {

  private ColorUtils() {
    // No instances
  }

  @ColorInt
  public static int getMaterialColor(@NonNull final Context context, @AttrRes final int attributeRes) {
    TypedValue typedValue = new TypedValue();
    Resources.Theme theme = context.getTheme();
    theme.resolveAttribute(attributeRes, typedValue, true);
    return typedValue.data;
  }
}