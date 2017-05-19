package com.mapbox.mapboxsdk.plugins.mylocationlayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;

/**
 * This manager class handles compass events such as starting the tracking of device bearing, or when a new compass
 * update occurs.
 *
 * @since 0.1.0
 */
class CompassManager implements SensorEventListener {

  private final SensorManager sensorManager;
  private CompassListener compassListener;

  private Sensor rotationVectorSensor;
  private float[] matrix = new float[9];
  private float[] orientation = new float[3];

  // CompassManager data
  private long compassUpdateNextTimestamp = 0;

  CompassManager(Context context, CompassListener compassListener) {
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    this.compassListener = compassListener;
  }

  void onStart() {
    sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
  }

  void onStop() {
    sensorManager.unregisterListener(this, rotationVectorSensor);
  }

  boolean isSensorAvailable() {
    return rotationVectorSensor != null;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {

    // check when the last time the compass was updated, return if too soon.
    long currentTime = SystemClock.elapsedRealtime();
    if (currentTime < compassUpdateNextTimestamp) {
      return;
    }

    if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

      // calculate the rotation matrix
      SensorManager.getRotationMatrixFromVector(matrix, event.values);
      SensorManager.getOrientation(matrix, orientation);

      float magneticHeading = (float) Math.toDegrees(SensorManager.getOrientation(matrix, orientation)[0]);
      compassListener.onCompassChanged(magneticHeading);

      compassUpdateNextTimestamp = currentTime + LocationLayerConstants.COMPASS_UPDATE_RATE_MS;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }


}