package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class ImageLoaderTest {

  @Rule
  public ActivityTestRule<TestActivity> rule = new ActivityTestRule<>(TestActivity.class);

  private MapView mapView;
  private SymbolManager symbolManager;
  private CountDownLatch countDownLatch = new CountDownLatch(1);

  @Before
  public void setUp() {
    mapView = rule.getActivity().mapView;
  }

  @Test
  public void testImageLoadingString() throws TimeoutException {
    rule.getActivity().runOnUiThread(() -> {
      mapView.getMapAsync(mapboxMap -> {
        mapboxMap.moveCamera(CameraUpdateFactory.zoomBy(3));
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
          symbolManager = new SymbolManager(mapView, mapboxMap, style);
          SymbolOptions symbolOptions = new SymbolOptions()
            .withLatLng(new LatLng())
            .withIconImage("mapbox_compass_icon");
          mapView.addOnDidBecomeIdleListener(() -> {
            assertNotNull(style.getImage("mapbox_compass_icon"));
            countDownLatch.countDown();
          });
          symbolManager.create(symbolOptions);
        });
      });
    });
    try {
      boolean timeout = !countDownLatch.await(10, TimeUnit.SECONDS);
      if (timeout) {
        throw new TimeoutException();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testImageLoadingId() throws TimeoutException {
    rule.getActivity().runOnUiThread(() -> {
      mapView.getMapAsync(mapboxMap -> {
        mapboxMap.moveCamera(CameraUpdateFactory.zoomBy(3));
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
          symbolManager = new SymbolManager(mapView, mapboxMap, style);
          SymbolOptions symbolOptions = new SymbolOptions()
            .withLatLng(new LatLng())
            .withIconImage(rule.getActivity(), R.drawable.mapbox_compass_icon);
          mapView.addOnDidBecomeIdleListener(() -> {
            assertNotNull(style.getImage("mapbox_compass_icon"));
            countDownLatch.countDown();
          });
          symbolManager.create(symbolOptions);
        });
      });
    });
    try {
      boolean timeout = !countDownLatch.await(10, TimeUnit.SECONDS);
      if (timeout) {
        throw new TimeoutException();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testInvalidImageLoading() throws TimeoutException {
    rule.getActivity().runOnUiThread(() -> {
      mapView.getMapAsync(mapboxMap -> {
        mapboxMap.moveCamera(CameraUpdateFactory.zoomBy(3));
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
          symbolManager = new SymbolManager(mapView, mapboxMap, style);
          SymbolOptions symbolOptions = new SymbolOptions()
            .withLatLng(new LatLng())
            .withIconImage("unknown");
          mapView.addOnDidBecomeIdleListener(() -> {
            assertNull(style.getImage("unknown"));
            countDownLatch.countDown();
          });
          symbolManager.create(symbolOptions);
        });
      });
    });
    try {
      boolean timeout = !countDownLatch.await(10, TimeUnit.SECONDS);
      if (timeout) {
        throw new TimeoutException();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

