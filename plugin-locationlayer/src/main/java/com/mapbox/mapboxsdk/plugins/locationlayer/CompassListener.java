package com.mapbox.mapboxsdk.plugins.locationlayer;

/**
 * Callbacks related to the compass
 *
 * @since 0.1.0
 */
public interface CompassListener {

  /**
   * Callback's invoked when a new compass update occurs. You can listen into the compass updates
   * using {@link LocationLayerPlugin#addCompassListener(CompassListener)} and implementing these
   * callbacks. Note that this interface is also used internally to to update the UI chevron/arrow.
   *
   * @param userHeading the new compass heading
   * @since 0.1.0
   */
  void onCompassChanged(float userHeading);

  /**
   * This gets invoked when the compass accuracy status changes from one value to another. It
   * provides an integer value which is identical to the {@code SensorManager} class constants:
   * <ul>
   * <li>{@link android.hardware.SensorManager#SENSOR_STATUS_NO_CONTACT}</li>
   * <li>{@link android.hardware.SensorManager#SENSOR_STATUS_UNRELIABLE}</li>
   * <li>{@link android.hardware.SensorManager#SENSOR_STATUS_ACCURACY_LOW}</li>
   * <li>{@link android.hardware.SensorManager#SENSOR_STATUS_ACCURACY_MEDIUM}</li>
   * <li>{@link android.hardware.SensorManager#SENSOR_STATUS_ACCURACY_HIGH}</li>
   * </ul>
   *
   * @param compassStatus the new accuracy of this sensor, one of
   * {@code SensorManager.SENSOR_STATUS_*}
   * @since 0.2.0
   */
  void onCompassAccuracyChange(int compassStatus);
}

