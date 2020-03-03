package com.mapbox.mapboxsdk.plugins.offline.model;

import android.content.Context;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.plugins.offline.R;

import androidx.annotation.DrawableRes;

@AutoValue
public abstract class NotificationOptions implements Parcelable {

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

  public abstract String cancelText();

  public abstract boolean requestMapSnapshot();

  public static Builder builder(Context context) {
    return new AutoValue_NotificationOptions.Builder()
      .smallIconRes(android.R.drawable.stat_sys_download)
      .contentTitle(context.getString(R.string.mapbox_offline_notification_default_content_title))
      .contentText(context.getString(R.string.mapbox_offline_notification_default_content_text))
      .cancelText(context.getString(R.string.mapbox_offline_notification_action_cancel))
      .requestMapSnapshot(true);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder smallIconRes(int smallIconRes);

    public abstract Builder returnActivity(String returnActivity);

    public abstract Builder contentTitle(String contentTitle);

    public abstract Builder contentText(String contentText);

    public abstract Builder cancelText(String cancelText);

    public abstract Builder requestMapSnapshot(boolean requestMapSnapshot);

    public abstract NotificationOptions build();
  }
}
