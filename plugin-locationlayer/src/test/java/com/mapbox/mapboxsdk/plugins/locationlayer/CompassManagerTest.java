package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompassManagerTest {

    private CompassManager compassManager;

    @Mock
    private WindowManager windowManager;

    @Mock
    private SensorManager sensorManager;

    @Before
    public void setUp() throws Exception {
        compassManager = new CompassManager(windowManager, sensorManager);
    }

    @Test
    public void lastKnownCompassAccuracyStatusDefault() {
        assertEquals("Last accuracy should match", compassManager.getLastAccuracySensorStatus(), 0);
    }

    @Test
    public void lastKnownCompassAccuracyStatusValue() {
        Sensor sensor = mock(Sensor.class);
        compassManager.onAccuracyChanged(sensor, 2);
        assertEquals("Last accuracy should match", compassManager.getLastAccuracySensorStatus(), 2);
    }

    @Test
    public void whenGyroscopeIsNull_fallbackToGravity() {
        SensorManager sensorManager = mock(SensorManager.class);
        when(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null);
        new CompassManager(windowManager, sensorManager);

        verify(sensorManager, times(1)).getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Test
    public void whenGyroscopeIsNull_fallbackToMagneticField() {
        SensorManager sensorManager = mock(SensorManager.class);
        when(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null);
        new CompassManager(windowManager, sensorManager);

        verify(sensorManager, times(1)).getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
}
