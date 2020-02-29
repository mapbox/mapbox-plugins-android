package com.mapbox.mapboxsdk.plugins.offline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.model.RegionSelectionOptions;
import com.mapbox.mapboxsdk.plugins.offline.ui.OfflineActivity;

import androidx.annotation.NonNull;

import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.RETURNING_DEFINITION;
import static com.mapbox.mapboxsdk.plugins.offline.offline.OfflineConstants.RETURNING_REGION_NAME;

/**
 * While the offline plugin includes a service for optimally launching an offline download session,
 * the plugin also includes UI components which also assist in providing a way for your app users to
 * select the region in which they'd like to download the region they desire.
 * <p>
 * This specific class is used to build an intent which launches the {@link OfflineActivity} which
 * provides your users with a way to define the region which they'd like to download. This includes
 * automatically providing the region definition with a name; Whether that's the default name or the
 * name of the region the map's camera position is currently located.
 * </p><p>
 * The provided Intent builder in this class should be built with the options you would like to
 * provide, then passed into the {@link Activity#startActivityForResult(Intent, int)} or related
 * methods. Inside the same activity being used to launch this activity you'll need to
 * {@code @Override} {@link Activity#onActivityResult(int, int, Intent)} and use the provided static
 * methods also provided in this class.
 * </p><p>
 * Note that if you are using this exclusively inside your app with a Mapbox Map not already being
 * used somewhere else, you'll need to provide a Mapbox access token by overriding your applications
 * {@code onCreate} method and placing {@link com.mapbox.mapboxsdk.Mapbox#getInstance(Context, String)}
 * inside the override method.
 * </p>
 *
 * @since 0.1.0
 */
public class OfflineRegionSelector {

  private OfflineRegionSelector() {
    // No Instances
  }

  /**
   * Use this method to take the returning {@link Intent} data and construct a {@link OfflineDownloadOptions}
   * instance which can be used for starting a new offline region download.
   *
   * @param data     the {@link Activity#startActivityForResult(Intent, int)} which this method should
   *                 be used in provides the returning intent which should be provided in this param
   * @param metadata Add additional metadata to the {@link OfflineRegionDefinition}, note
   *                 to make sure not to override the region definition name if you still wish to use
   *                 it
   * @return a new {@link OfflineDownloadOptions} instance which can be used to launch the download
   * service using
   * {@link com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin#startDownload(OfflineDownloadOptions)}
   * @since 0.1.0
   */
  public static OfflineDownloadOptions getOfflineDownloadOptions(final Intent data, byte[] metadata) {
    return OfflineDownloadOptions.builder()
      .definition(getRegionDefinition(data))
      .regionName(getRegionName(data))
      .metadata(metadata)
      .build();
  }

  /**
   * Use this method to take the returning {@link Intent} data and construct a {@link OfflineDownloadOptions}
   * instance which can be used for starting a new offline region download.
   *
   * @param data                the {@link Activity#startActivityForResult(Intent, int)} which this
   *                            method should be used in provides the returning intent which should
   *                            be provided in this param
   * @param notificationOptions the {@link NotificationOptions} object you've constructed to be used
   *                            when launching the offline region download service.
   * @return a new {@link OfflineDownloadOptions} instance which can be used to launch the download
   * service using
   * {@link com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin#startDownload(OfflineDownloadOptions)}
   * @since 0.1.0
   */
  public static OfflineDownloadOptions getOfflineDownloadOptions(final Intent data,
                                                                 NotificationOptions notificationOptions) {
    return OfflineDownloadOptions.builder()
      .definition(getRegionDefinition(data))
      .regionName(getRegionName(data))
      .notificationOptions(notificationOptions)
      .build();
  }

  /**
   * Use this method to take the returning {@link Intent} data and construct a {@link OfflineDownloadOptions}
   * instance which can be used for starting a new offline region download.
   *
   * @param data                the {@link Activity#startActivityForResult(Intent, int)} which this
   *                            method should be used in provides the returning intent which should
   *                            be provided in this param
   * @param notificationOptions the {@link NotificationOptions} object you've constructed to be used
   *                            when launching the offline region download service.
   * @param metadata            Add additional metadata to the {@link OfflineRegionDefinition},
   *                            note to make sure not to override the region definition name if you
   *                            still wish to use it
   * @return a new {@link OfflineDownloadOptions} instance which can be used to launch the download
   * service using
   * {@link com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin#startDownload(OfflineDownloadOptions)}
   * @since 0.1.0
   */
  public static OfflineDownloadOptions getOfflineDownloadOptions(final Intent data,
                                                                 NotificationOptions notificationOptions,
                                                                 byte[] metadata) {
    return OfflineDownloadOptions.builder()
      .definition(getRegionDefinition(data))
      .regionName(getRegionName(data))
      .notificationOptions(notificationOptions)
      .metadata(metadata)
      .build();
  }

  /**
   * Returns the {@link OfflineRegionDefinition} which was created when the user was
   * inside the {@link OfflineActivity}.
   *
   * @param data the {@link Activity#startActivityForResult(Intent, int)} which this method should
   *             be used in provides the returning intent which should be provided in this param
   * @return the {@link OfflineRegionDefinition} which was created inside the
   * {@link OfflineActivity}
   * @since 0.1.0
   */
  public static OfflineRegionDefinition getRegionDefinition(Intent data) {
    return data.getParcelableExtra(RETURNING_DEFINITION);
  }

  /**
   * The {@link OfflineActivity} class will try to provide a region name which is either the default
   * region naming string or, depending on the map's camera position and where it is positioned over
   * the map.
   *
   * @param data the {@link Activity#startActivityForResult(Intent, int)} which this method should
   *             be used in provides the returning intent which should be provided in this param
   * @return either a string containing the default region name or the actual region name which the
   * map's camera position was last placed over
   * @since 0.1.0
   */
  public static String getRegionName(@NonNull Intent data) {
    return data.getStringExtra(RETURNING_REGION_NAME);
  }

  /**
   * Useful for building an {@link Intent} which can be used to launch the {@link OfflineActivity}
   * allowing your app user to select a region which they'd like to download.
   *
   * @since 0.1.0
   */
  public static class IntentBuilder {

    Intent intent;

    /**
     * Construct a new instance of this builder.
     *
     * @since 0.1.0
     */
    public IntentBuilder() {
      intent = new Intent();
    }

    public IntentBuilder regionSelectionOptions(RegionSelectionOptions regionSelectionOptions) {
      intent.putExtra(OfflinePluginConstants.REGION_SELECTION_OPTIONS, regionSelectionOptions);
      return this;
    }

    /**
     * Build a new {@link Intent} object which should be used to launch the {@link OfflineActivity}.
     *
     * @param activity pass in the current activity which you intend to use
     *                 {@link Activity#startActivityForResult(Intent, int)} in.
     * @return a new {@link Intent} which should be used to launch the {@link OfflineActivity}
     * @since 0.1.0
     */
    public Intent build(Activity activity) {
      intent.setClass(activity, OfflineActivity.class);
      return intent;
    }
  }
}
