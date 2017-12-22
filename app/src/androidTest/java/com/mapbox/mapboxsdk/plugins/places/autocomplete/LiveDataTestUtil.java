package com.mapbox.mapboxsdk.plugins.places.autocomplete;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

  /**
   * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
   * Once we got a notification via onChanged, we stop observing.
   */
  public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
    final Object[] data = new Object[1];
    final CountDownLatch latch = new CountDownLatch(1);
    Observer<T> observer = new Observer<T>() {
      @Override
      public void onChanged(@Nullable T o) {
        data[0] = o;
        latch.countDown();
        liveData.removeObserver(this);
      }
    };
    liveData.observeForever(observer);
    latch.await(2, TimeUnit.SECONDS);
    //noinspection unchecked
    return (T) data[0];
  }
}
