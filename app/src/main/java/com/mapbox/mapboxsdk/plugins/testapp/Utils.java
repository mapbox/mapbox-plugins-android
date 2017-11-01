package com.mapbox.mapboxsdk.plugins.testapp;

import android.location.Location;

import com.mapbox.mapboxsdk.constants.Style;

import java.util.Random;

import timber.log.Timber;

/**
 * Useful utilities used throughout the testapp.
 */
public class Utils {

  private static final String[] STYLES = new String[] {
    Style.MAPBOX_STREETS,
    Style.OUTDOORS,
    Style.LIGHT,
    Style.DARK,
    Style.SATELLITE_STREETS
  };

  private static int index;

  /**
   * Utility to cycle through map styles. Useful to test if runtime styling source and layers transfer over to new
   * style.
   *
   * @return a string ID representing the map style
   */
  public static String getNextStyle() {
    index++;
    if (index == STYLES.length) {
      index = 0;
    }
    return STYLES[index];
  }

  /**
   * Utility for getting a random coordinate inside a provided bounding box and creates a {@link Location} from it.
   *
   * @param bbox double array forming the bounding box in the order of {@code [minx, miny, maxx, maxy]}
   * @return a {@link Location} object using the random coordinate
   */
  public static Location getRandomLocation(double[] bbox) {
    Random random = new Random();

    double randomLat = bbox[1] + (bbox[3] - bbox[1]) * random.nextDouble();
    double randomLon = bbox[0] + (bbox[2] - bbox[0]) * random.nextDouble();

    Location location = new Location("random-loc");
    location.setLongitude(randomLon);
    location.setLatitude(randomLat);
    Timber.d("getRandomLatLng: %s", location.toString());
    return location;
  }
}