package com.mapbox.mapboxsdk.plugins.offline.utils;

import android.os.Build;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

/**
 * Utilities useful for threading
 *
 * @since 0.1.0
 */
public final class ThreadUtils {

  private ThreadUtils() {
    // No instances
  }

  /**
   * Safely destroys the provided thread.
   *
   * @param thread the thread in which to destroy
   * @since 0.1.0
   */
  public static void destroyThread(@NonNull HandlerThread thread) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      thread.quitSafely();
    } else {
      thread.quit();
    }
  }
}
