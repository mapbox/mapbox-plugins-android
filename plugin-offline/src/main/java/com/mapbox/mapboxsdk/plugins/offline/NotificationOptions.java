package com.mapbox.mapboxsdk.plugins.offline;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import static android.support.v4.app.NotificationCompat.Builder;

public class NotificationOptions implements Parcelable {

  @DrawableRes
  private int smallIconRes;
  private String returnActivity;
  private String contentTitle = "Offline download";
  private String contentText = "Downloading..";

  public NotificationOptions() {
  }

  private NotificationOptions(Parcel in) {
    smallIconRes = in.readInt();
    returnActivity = in.readString();
    contentTitle = in.readString();
    contentText = in.readString();
  }

  public NotificationOptions withSmallIconRes(@DrawableRes int iconRes) {
    smallIconRes = iconRes;
    return this;
  }

  @DrawableRes
  public int getSmallIconRes() {
    return smallIconRes;
  }

  public NotificationOptions withReturnActivity(String activity) {
    returnActivity = activity;
    return this;
  }

  public Class getReturnActivity() {
    try {
      return Class.forName(returnActivity);
    } catch (ClassNotFoundException exception) {
      throw new RuntimeException("Could not resolve class for Activity.");
    }
  }

  public NotificationOptions withContentTitle(String contentTitle) {
    this.contentTitle = contentTitle;
    return this;
  }

  public String getContentTitle() {
    return contentTitle;
  }

  public NotificationOptions withContentText(String contextText) {
    this.contentText = contextText;
    return this;
  }

  public String getContentText() {
    return contentText;
  }

  Builder toNotificationBuilder(Context context, PendingIntent contentIntent, Intent cancelIntent) {
    return new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
      .setContentTitle(contentTitle)
      .setContentText(contentText)
      .setCategory(NotificationCompat.CATEGORY_PROGRESS)
      .setSmallIcon(smallIconRes)
      .setOnlyAlertOnce(true)
      .setContentIntent(contentIntent)
      .addAction(R.drawable.ic_cancel, "Cancel", PendingIntent.getService(context,
        Constants.REQ_CANCEL_DOWNLOAD, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(smallIconRes);
    parcel.writeString(returnActivity);
    parcel.writeString(contentTitle);
    parcel.writeString(contentText);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public NotificationOptions createFromParcel(Parcel in) {
      return new NotificationOptions(in);
    }

    public NotificationOptions[] newArray(int size) {
      return new NotificationOptions[size];
    }
  };
}
