package com.mapbox.mapboxsdk.plugins.offline;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

public class NotificationOptions implements Parcelable {

  @DrawableRes
  private int smallIconRes;
  private String returnActivity;
  private String contentTitle = "Offline download";
  private String contextText = "Downloading..";
  private String ticker = "Downloading map for offline use";

  public NotificationOptions() {
  }

  public NotificationOptions(Parcel in) {
    smallIconRes = in.readInt();
    returnActivity = in.readString();
    contentTitle = in.readString();
    contextText = in.readString();
    ticker = in.readString();
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
    this.contextText = contextText;
    return this;
  }

  public String getContextText() {
    return contextText;
  }

  public NotificationOptions withTicker(String ticker) {
    this.ticker = ticker;
    return this;
  }

  public String getTicker() {
    return ticker;
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
    parcel.writeString(contextText);
    parcel.writeString(ticker);
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
