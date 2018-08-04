package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * This manager class handles compass events such as starting the tracking of device bearing, or
 * when a new compass update occurs.
 *
 * @since 0.1.0
 */
class CompassManager implements SensorEventListener {

  // The rate sensor events will be delivered at. As the Android documentation states, this is only
  // a hint to the system and the events might actually be received faster or slower then this
  // specified rate. Since the minimum Android API levels about 9, we are able to set this value
  // ourselves rather than using one of the provided constants which deliver updates too quickly for
  // our use case. The default is set to 100ms
  private static final int SENSOR_DELAY_MICROS = 100 * 1000;

  private final WindowManager windowManager;
  private final SensorManager sensorManager;
  private final List<CompassListener> compassListeners = new ArrayList<>();

  // Not all devices have a compassSensor
  @Nullable
  private Sensor compassSensor;
  @Nullable
  private Sensor gravitySensor;
  @Nullable
  private Sensor magneticFieldSensor;

  private float[] truncatedRotationVectorValue = new float[4];
  private float[] rotationMatrix = new float[9];
  private float[] rotationVectorValue;
  private float lastHeading;
  private int lastAccuracy;

  // CompassManager data
  private long compassUpdateNextTimestamp;

  private float[] grav = new float[3];
  private float[] mag = new float[3];

  //filtering coefficient 0 < ALPHA < 1
  static final float ALPHA = 0.45f;

  /**
   * Construct a new instance of the this class. A internal compass listeners needed to separate it
   * from the cleared list of public listeners.
   */
  CompassManager(@NonNull Context context) {
    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

    compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    if (compassSensor == null) {
      Timber.d(
        "Rotation vector sensor not supported on device, falling back to orientation.");
      if (isGyroscopeAvailable()) {
        compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
      } else {
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
      }
    }
  }

  private boolean isGyroscopeAvailable() {
    return sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null;
  }

  void addCompassListener(@NonNull CompassListener compassListener) {
    if (compassListeners.isEmpty()) {
      onStart();
    }
    compassListeners.add(compassListener);
  }

  void removeCompassListener(@NonNull CompassListener compassListener) {
    compassListeners.remove(compassListener);
    if (compassListeners.isEmpty()) {
      onStop();
    }
  }

  void onStart() {
    if (isSensorAvailable()) {
      // Does nothing if the sensors already registered.
      if (compassSensor != null) {
        sensorManager.registerListener(this, compassSensor, SENSOR_DELAY_MICROS);
      }else {
        sensorManager.registerListener(this, gravitySensor, SENSOR_DELAY_MICROS);
        sensorManager.registerListener(this, magneticFieldSensor, SENSOR_DELAY_MICROS);
      }
    }
  }

  void onStop() {
    if (isSensorAvailable()) {
      sensorManager.unregisterListener(this, compassSensor);
    }
  }

  boolean isSensorAvailable() {
    return compassSensor != null;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    // check when the last time the compass was updated, return if too soon.
    long currentTime = SystemClock.elapsedRealtime();
    if (currentTime < compassUpdateNextTimestamp) {
      return;
    }
    if (lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
      Timber.d("Compass sensor is unreliable, device calibration is needed.");
      return;
    }
    if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
      rotationVectorValue = getRotationVectorFromSensorEvent(event);
      updateOrientation();

      // Update the compassUpdateNextTimestamp
      compassUpdateNextTimestamp = currentTime + LocationLayerConstants.COMPASS_UPDATE_RATE_MS;
    } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
      notifyCompassChangeListeners((event.values[0] + 360) % 360);
    } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      grav = lowPass(getRotationVectorFromSensorEvent(event), grav);
      updateOrientation();
    } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
      mag = lowPass(getRotationVectorFromSensorEvent(event), mag);
      updateOrientation();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    if (lastAccuracy != accuracy) {
      for (CompassListener compassListener : compassListeners) {
        compassListener.onCompassAccuracyChange(accuracy);
      }
      lastAccuracy = accuracy;
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private void updateOrientation() {
    if (rotationVectorValue != null) {
      SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVectorValue);
    } else {
      // Get rotation matrix given the gravity and geomagnetic matrices
      SensorManager.getRotationMatrix(rotationMatrix, null, grav, mag);
    }

    final int worldAxisForDeviceAxisX;
    final int worldAxisForDeviceAxisY;

    // Remap the axes as if the device screen was the instrument panel,
    // and adjust the rotation matrix for the device orientation.
    switch (windowManager.getDefaultDisplay().getRotation()) {
      case Surface.ROTATION_90:
        worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
        worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
        break;
      case Surface.ROTATION_180:
        worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
        worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
        break;
      case Surface.ROTATION_270:
        worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
        worldAxisForDeviceAxisY = SensorManager.AXIS_X;
        break;
      case Surface.ROTATION_0:
      default:
        worldAxisForDeviceAxisX = SensorManager.AXIS_X;
        worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
        break;
    }

    float[] adjustedRotationMatrix = new float[9];
    SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
      worldAxisForDeviceAxisY, adjustedRotationMatrix);

    // Transform rotation matrix into azimuth/pitch/roll
    float[] orientation = new float[3];
    SensorManager.getOrientation(adjustedRotationMatrix, orientation);

    // The x-axis is all we care about here.
    notifyCompassChangeListeners((float) Math.toDegrees(orientation[0]));
  }

  private void notifyCompassChangeListeners(float heading) {
    for (CompassListener compassListener : compassListeners) {
      compassListener.onCompassChanged(heading);
    }
    lastHeading = heading;
  }

  /**
   * Helper function, that filters input, considering previous values
   *
   * @param input  array of float, that contains new data
   * @param output array of float, that contains previous state
   * @return float filtered array of float
   */
  protected float[] lowPass(float[] input, float[] output) {
    if (output == null) return input;
    for (int i = 0; i < input.length; i++) {
      output[i] = output[i] + ALPHA * (input[i] - output[i]);
    }
    return output;
  }

  int getLastAccuracy() {
    return lastAccuracy;
  }

  float getLastHeading() {
    return lastHeading;
  }

  /**
   * Pulls out the rotation vector from a SensorEvent, with a maximum length
   * vector of four elements to avoid potential compatibility issues.
   *
   * @param event the sensor event
   * @return the events rotation vector, potentially truncated
   */
  @NonNull
  float[] getRotationVectorFromSensorEvent(@NonNull SensorEvent event) {
    if (event.values.length > 4) {
      // On some Samsung devices SensorManager.getRotationMatrixFromVector
      // appears to throw an exception if rotation vector has length > 4.
      // For the purposes of this class the first 4 values of the
      // rotation vector are sufficient (see crbug.com/335298 for details).
      // Only affects Android 4.3
      System.arraycopy(event.values, 0, truncatedRotationVectorValue, 0, 4);
      return truncatedRotationVectorValue;
    } else {
      return event.values;
    }
  }
}