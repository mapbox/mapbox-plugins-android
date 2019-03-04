package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.maps.Projection;

public abstract class Annotation<T extends Geometry> {

  static final String ID_KEY = "id";
  protected JsonObject jsonObject;
  protected T geometry;
  private boolean isDraggable;

  Annotation(long id, JsonObject jsonObject, T geometry) {
    this.jsonObject = jsonObject;
    this.jsonObject.addProperty(ID_KEY, id);
    this.geometry = geometry;
  }

  /**
   * Set the geometry of an annotation, geometry type depends on the generic attribute.
   *
   * @param geometry an instance of a geometry type
   */
  public void setGeometry(T geometry) {
    this.geometry = geometry;
  }

  /**
   * Get the geometry of an annotation, type of geometry returned depends on the generic attribute.
   *
   * @return the geometry of the annotation
   * @throws IllegalStateException if geometry hasn't been initialised
   */
  public T getGeometry() {
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

  abstract void setUsedDataDrivenProperties();

  abstract String getName();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Annotation<?> that = (Annotation<?>) o;

    if (isDraggable != that.isDraggable) {
      return false;
    }
    if (!jsonObject.equals(that.jsonObject)) {
      return false;
    }
    return geometry.equals(that.geometry);
  }

  @Override
  public int hashCode() {
    int result = jsonObject.hashCode();
    result = 31 * result + geometry.hashCode();
    result = 31 * result + (isDraggable ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return getName()
      + "{geometry=" + geometry
      + ", properties=" + jsonObject
      + ", isDraggable=" + isDraggable
      + '}';
  }
}