package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.WindowManager;

import timber.log.Timber;

/**
 * This manager class handles compass events such as starting the tracking of device bearing, or when a new compass
 * update occurs.
 *
 * @since 0.1.0
 */
class CompassManager implements SensorEventListener {

  // The rate sensor events will be delivered at. As the Android documentation states,
  // this is only a hint to the system and the events might actually be received faster
  // or slower then this specified rate. Since the minimum Android API levels about 9,
  // we are able to set this value ourselves rather than using one of the provided
  // constants which deliver updates too quickly for our use case.
  private static final int SENSOR_DELAY_MICROS = 100 * 1000; // 100ms

  private final WindowManager windowManager;
  private final SensorManager sensorManager;
  private CompassListener compassListener;

  // Not all devices have a compassSensor
  @Nullable
  private Sensor compassSensor;

  private int lastAccuracy;

  // CompassManager data
  private long compassUpdateNextTimestamp = 0;

  CompassManager(Context context, CompassListener compassListener) {

    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

    compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    if (compassSensor == null) {
      Timber.d("Rotation vector sensor not supported on device, falling back to orientation.");
      compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }
    this.compassListener = compassListener;
  }

  void onStart() {
    sensorManager.registerListener(this, compassSensor, SENSOR_DELAY_MICROS);
  }

  void onStop() {
    sensorManager.unregisterListener(this, compassSensor);
  }

  boolean isSensorAvailable() {
    return compassSensor != null;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (compassListener == null) {
      return;
    }
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
      updateOrientation(event.values);

      // Update the compassUpdateNextTimestamp
      compassUpdateNextTimestamp = currentTime + LocationLayerConstants.COMPASS_UPDATE_RATE_MS;
    } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
      compassListener.onCompassChanged((event.values[0] + 360) % 360);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    if (lastAccuracy != accuracy) {
      lastAccuracy = accuracy;
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private void updateOrientation(float[] rotationVector) {
    float[] rotationMatrix = new float[9];
    SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

    final int worldAxisForDeviceAxisX;
    final int worldAxisForDeviceAxisY;

    // Remap the axes as if the device screen was the instrument panel,
    // and adjust the rotation matrix for the device orientation.
    switch (windowManager.getDefaultDisplay().getRotation()) {
      case Surface.ROTATION_0:
      default:
        worldAxisForDeviceAxisX = SensorManager.AXIS_X;
        worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
        break;
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
    }

    float[] adjustedRotationMatrix = new float[9];
    SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
      worldAxisForDeviceAxisY, adjustedRotationMatrix);

    // Transform rotation matrix into azimuth/pitch/roll
    float[] orientation = new float[3];
    SensorManager.getOrientation(adjustedRotationMatrix, orientation);

    // The x-axis is all we care about here.
    compassListener.onCompassChanged((float) Math.toDegrees(orientation[0]));
  }
}