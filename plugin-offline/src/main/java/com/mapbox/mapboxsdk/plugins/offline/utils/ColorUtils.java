package com.mapbox.mapboxsdk.plugins.offline.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

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