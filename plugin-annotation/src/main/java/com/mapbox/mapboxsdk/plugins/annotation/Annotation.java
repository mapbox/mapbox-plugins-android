package com.mapbox.mapboxsdk.plugins.annotation;

import com.google.gson.JsonObject;
import com.mapbox.geojson.Geometry;

public abstract class Annotation {

  public static final String ID_KEY = "id";
  protected final JsonObject jsonObject = new JsonObject();
  protected Geometry geometry;

  public Annotation(long id) {
    this.jsonObject.addProperty(ID_KEY, id);
  }

  public void setGeometry(Geometry geometry){
    this.geometry = geometry;
  }

  Geometry getGeometry() {
    if (geometry == null) {
      throw new IllegalStateException();
    }
    return geometry;
  }

  public long getId() {
    return jsonObject.get(ID_KEY).getAsLong();
  }

  JsonObject getFeature() {
    return jsonObject;
  }

  abstract void update();
}
