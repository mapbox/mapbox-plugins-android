package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
    public void lastKnownCompassBearingAccuracyDefault() {
        assertEquals("Last accuracy should match", compassManager.getLastAccuracy(), 0);
    }

    @Test
    public void lastKnownCompassBearingAccuracyValue() {
        Sensor sensor = mock(Sensor.class);
        compassManager.onAccuracyChanged(sensor, 2);
        assertEquals("Last accuracy should match", compassManager.getLastAccuracy(), 2);
    }
}
