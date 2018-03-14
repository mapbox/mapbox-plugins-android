package com.mapbox.mapboxsdk.plugins.offline.model;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.plugins.offline.R;

@AutoValue
public abstract class NotificationOptions implements Parcelable {

  public static final int NOTIFICATION_ID = 5938;

  @DrawableRes
  public abstract int smallIconRes();

  abstract String returnActivity();

  public Class getReturnActivity() {
    try {
      return Class.forName(returnActivity());
    } catch (ClassNotFoundException exception) {
      throw new IllegalArgumentException("The returning class name " + returnActivity()
        + " cannot be found.");
    }
  }

  public abstract String contentTitle();

  public abstract String contentText();

  public static Builder builder(Context context) {
    return new AutoValue_NotificationOptions.Builder()
      .contentTitle(context.getString(R.string.mapbox_offline_notification_default_content_title))
      .contentText(context.getString(R.string.mapbox_offline_notification_default_content_text));
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder smallIconRes(int smallIconRes);

    public abstract Builder returnActivity(String returnActivity);

    public abstract Builder contentTitle(String contentTitle);

    public abstract Builder contentText(String contentText);

    public abstract NotificationOptions build();
  }
}
