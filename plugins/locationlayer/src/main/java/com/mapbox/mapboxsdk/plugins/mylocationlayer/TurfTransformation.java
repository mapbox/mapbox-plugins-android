package com.mapbox.mapboxsdk.plugins.mylocationlayer;

import android.support.annotation.NonNull;

import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class TurfTransformation {

  public static Polygon circle(@NonNull Position center, double radius) {
    return circle(center, radius, 64, TurfConstants.UNIT_DEFAULT);
  }

  public static Polygon circle(@NonNull Position center, double radius, String units) {
    return circle(center, radius, 64, units);
  }

  public static Polygon circle(@NonNull Position center, double radius, int steps, String units) {
    List<Position> coordinates = new ArrayList<>();
    for (int i = 0; i < steps; i++) {
      coordinates.add(TurfMeasurement.destination(center, radius, i * 360 / steps, units));
    }

    List<List<Position>> coordinate = new ArrayList<>();
    coordinate.add(coordinates);
    return Polygon.fromCoordinates(coordinate);
  }
}
