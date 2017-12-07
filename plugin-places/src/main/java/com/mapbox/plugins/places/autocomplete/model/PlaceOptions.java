package com.mapbox.plugins.places.autocomplete.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

public class PlaceOptions implements Parcelable {

  private String hint;
  private int backgroundColor;
  private int toolbarColor;

  public PlaceOptions() {
    backgroundColor = Color.TRANSPARENT;
    toolbarColor = Color.WHITE;
  }

  private PlaceOptions(Parcel in) {
    hint = in.readString();
    backgroundColor = in.readInt();
    toolbarColor = in.readInt();
  }

  public static final Creator<PlaceOptions> CREATOR = new Creator<PlaceOptions>() {
    @Override
    public PlaceOptions createFromParcel(Parcel in) {
      return new PlaceOptions(in);
    }

    @Override
    public PlaceOptions[] newArray(int size) {
      return new PlaceOptions[size];
    }
  };

  public PlaceOptions withSearchViewHint(String hint) {
    this.hint = hint;
    return this;
  }

  public String getSearchViewHint() {
    return hint;
  }

  public PlaceOptions withBackgroundColor(@ColorInt int color) {
    this.backgroundColor = color;
    return this;
  }

  public int getBackgroundColor() {
    return backgroundColor;
  }

  public PlaceOptions withToolbarColor(@ColorInt int color) {
    this.toolbarColor = color;
    return this;
  }

  public int getToolbarColor() {
    return toolbarColor;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(hint);
    parcel.writeInt(backgroundColor);
    parcel.writeInt(toolbarColor);
  }
}
