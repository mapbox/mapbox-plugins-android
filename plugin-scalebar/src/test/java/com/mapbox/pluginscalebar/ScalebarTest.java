package com.mapbox.pluginscalebar;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScalebarTest {
    private static int MAPVIEW_WIDTH = 1080;
    private ScaleBar scaleBar;
    private ScaleBarWidget scaleBarWidget;

    @Before
    public void setUp() {
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(context.getResources()).thenReturn(resources);
        when(resources.getColor(android.R.color.black)).thenReturn(0xff000000);
        when(resources.getColor(android.R.color.white)).thenReturn(0xffffffff);
        MapView mapView = mock(MapView.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                scaleBarWidget = (ScaleBarWidget) invocation.getArguments()[0];
                return null;
            }
        }).when(mapView).addView(any(View.class));
        when(mapView.getContext()).thenReturn(context);
        when(mapView.getWidth()).thenReturn(MAPVIEW_WIDTH);
        MapboxMap mapboxMap = mock(MapboxMap.class);
        Projection projection = mock(Projection.class);
        when(mapboxMap.getProjection()).thenReturn(projection);
        scaleBar = new ScaleBar(mapView, mapboxMap);
        assertNotNull(scaleBar);
        assertNotNull(scaleBarWidget);
    }

    @Test
    public void testEnable() {
        assertTrue(scaleBar.isEnabled());
        scaleBar.setEnabled(false);
        assertFalse(scaleBar.isEnabled());
    }

    @Test
    public void testVariables() {
        assertEquals(MAPVIEW_WIDTH, scaleBarWidget.getMapViewWidth());
        assertEquals(0xff000000, scaleBarWidget.getTextColor());
        assertEquals(0xff000000, scaleBarWidget.getPrimaryColor());
        assertEquals(0xffffffff, scaleBarWidget.getSecondaryColor());

        scaleBar.setColors(0xff000000, 0xff000001, 0xff000002);
        assertEquals(0xff000000, scaleBarWidget.getTextColor());
        assertEquals(0xff000001, scaleBarWidget.getPrimaryColor());
        assertEquals(0xff000002, scaleBarWidget.getSecondaryColor());
    }
}
