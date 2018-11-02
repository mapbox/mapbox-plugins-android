package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.maps.Projection;

public abstract class Annotation {

  static final String ID_KEY = "id";
  protected JsonObject jsonObject;
  protected Geometry geometry;
  private boolean isDraggable;

  Annotation(long id, JsonObject jsonObject, Geometry geometry) {
    this.jsonObject = jsonObject;
    this.jsonObject.addProperty(ID_KEY, id);
    this.geometry = geometry;
  }

  void setGeometry(Geometry geometry) {
    this.geometry = geometry;
  }

  Geometry getGeometry() {
    if (geometry == null) {
      throw new IllegalStateException();
    }
    return geometry;
  }

  /**
   * Returns this annotation's internal ID.
   *
   * @return annotation's internal ID
   */
  public long getId() {
    return jsonObject.get(ID_KEY).getAsLong();
  }

  JsonObject getFeature() {
    return jsonObject;
  }

  /**
   * Returns whether this annotation is draggable, meaning it can be dragged across the screen when touched and moved.
   *
   * @return draggable when touched
   */
  public boolean isDraggable() {
    return isDraggable;
  }

  /**
   * Set whether this annotation should be draggable,
   * meaning it can be dragged across the screen when touched and moved.
   *
   * @param draggable should be draggable
   */
  public void setDraggable(boolean draggable) {
    isDraggable = draggable;
  }

  @Nullable
  abstract Geometry getOffsetGeometry(@NonNull Projection projection, @NonNull MoveDistancesObject moveDistancesObject,
                                      float touchAreaShiftX, float touchAreaShiftY);
}
