package com.mapbox.pluginscalebar;

import android.util.Pair;

import java.util.ArrayList;

class ScaleBarConstants {
  static double FEET_PER_METER = 3.2808;
  static int KILOMETER = 1000;
  static int FEET_PER_MILE = 5280;
  static String METER_UNIT = " m";
  static String FEET_UNIT = " ft";
  static String KILOMETER_UNIT = " km";
  static String MILE_UNIT = " mi";
  static ArrayList<Pair<Integer, Integer>> metricTable = new ArrayList<Pair<Integer, Integer>>() {
    {
      add(new Pair<>(1, 2));
      add(new Pair<>(2, 2));
      add(new Pair<>(4, 2));
      add(new Pair<>(10, 2));
      add(new Pair<>(20, 2));
      add(new Pair<>(50, 2));
      add(new Pair<>(75, 3));
      add(new Pair<>(100, 2));
      add(new Pair<>(150, 2));
      add(new Pair<>(200, 2));
      add(new Pair<>(300, 3));
      add(new Pair<>(500, 2));
      add(new Pair<>(1000, 2));
      add(new Pair<>(1500, 2));
      add(new Pair<>(3000, 3));
      add(new Pair<>(5000, 2));
      add(new Pair<>(10000, 2));
      add(new Pair<>(20000, 2));
      add(new Pair<>(30000, 3));
      add(new Pair<>(50000, 2));
      add(new Pair<>(100000, 2));
      add(new Pair<>(200000, 2));
      add(new Pair<>(300000, 3));
      add(new Pair<>(400000, 2));
      add(new Pair<>(500000, 2));
      add(new Pair<>(600000, 3));
      add(new Pair<>(800000, 2));
      add(new Pair<>(1000000, 2));
      add(new Pair<>(2000000, 2));
      add(new Pair<>(3000000, 3));
      add(new Pair<>(4000000, 2));
      add(new Pair<>(5000000, 2));
      add(new Pair<>(6000000, 3));
      add(new Pair<>(8000000, 2));
      add(new Pair<>(10000000, 2));
      add(new Pair<>(12000000, 2));
      add(new Pair<>(15000000, 2));
    }
  };

  static ArrayList<Pair<Integer, Integer>> imperialTable = new ArrayList<Pair<Integer, Integer>>() {
    {
      add(new Pair<>(4, 2));
      add(new Pair<>(6, 2));
      add(new Pair<>(10, 2));
      add(new Pair<>(20, 2));
      add(new Pair<>(30, 2));
      add(new Pair<>(50, 2));
      add(new Pair<>(75, 3));
      add(new Pair<>(100, 2));
      add(new Pair<>(200, 2));
      add(new Pair<>(300, 3));
      add(new Pair<>(400, 2));
      add(new Pair<>(600, 3));
      add(new Pair<>(800, 2));
      add(new Pair<>(1000, 2));
      add(new Pair<>((int) (0.25f * FEET_PER_MILE), 2));
      add(new Pair<>((int) (0.5f * FEET_PER_MILE), 2));
      add(new Pair<>(FEET_PER_MILE, 2));
      add(new Pair<>(2 * FEET_PER_MILE, 2));
      add(new Pair<>(3 * FEET_PER_MILE, 3));
      add(new Pair<>(4 * FEET_PER_MILE, 2));
      add(new Pair<>(8 * FEET_PER_MILE, 2));
      add(new Pair<>(12 * FEET_PER_MILE, 2));
      add(new Pair<>(15 * FEET_PER_MILE, 3));
      add(new Pair<>(20 * FEET_PER_MILE, 2));
      add(new Pair<>(30 * FEET_PER_MILE, 3));
      add(new Pair<>(40 * FEET_PER_MILE, 2));
      add(new Pair<>(80 * FEET_PER_MILE, 2));
      add(new Pair<>(120 * FEET_PER_MILE, 2));
      add(new Pair<>(200 * FEET_PER_MILE, 2));
      add(new Pair<>(300 * FEET_PER_MILE, 3));
      add(new Pair<>(400 * FEET_PER_MILE, 2));
      add(new Pair<>(600 * FEET_PER_MILE, 3));
      add(new Pair<>(1000 * FEET_PER_MILE, 2));
      add(new Pair<>(1500 * FEET_PER_MILE, 3));
      add(new Pair<>(2000 * FEET_PER_MILE, 2));
      add(new Pair<>(3000 * FEET_PER_MILE, 2));
      add(new Pair<>(4000 * FEET_PER_MILE, 2));
      add(new Pair<>(5000 * FEET_PER_MILE, 2));
      add(new Pair<>(6000 * FEET_PER_MILE, 3));
      add(new Pair<>(8000 * FEET_PER_MILE, 2));
      add(new Pair<>(10000 * FEET_PER_MILE, 2));
    }
  };
}
