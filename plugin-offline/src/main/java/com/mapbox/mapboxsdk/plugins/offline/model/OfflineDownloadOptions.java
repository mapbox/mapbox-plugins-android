package com.mapbox.mapboxsdk.plugins.offline.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadService;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This model class wraps the offline region definition with notifications options and the offline
 * region metadata. It is a companion object to {@link OfflineRegion} with regionId and
 * {@link OfflineDownloadService} with serviceId.
 *
 * @since 0.1.0
 */
@AutoValue
public abstract class OfflineDownloadOptions implements Parcelable {

  /**
   * The download definition which is used to actually launch the download inside the service. This
   * object particularly contains information such as the region bounding box, zoom levels, pixel
   * density, etc.
   *
   * @return the Map SDK Offline region definition which can be used to acquire region download
   * definition information
   * @since 0.1.0
   */
  @NonNull
  public abstract OfflineRegionDefinition definition();

  /**
   * Launching the service requires a notification, using the {@link NotificationOptions} class
   * allows you to customize this notification and will be displayed for the offline download region
   * while the service is running in the background.
   *
   * @return the {@link NotificationOptions} which will be used during the download process
   * @since 0.1.0
   */
  public abstract NotificationOptions notificationOptions();

  /**
   * A convenient method for providing the offline region a name which can then be used later to
   * identify the specific region in the database of offline region tiles.
   *
   * @return the string which represents this offline regions name
   * @since 0.1.0
   */
  @Nullable
  public abstract String regionName();

  /**
   * Metadata can optionally be added to your offline download region which can contain any
   * additional information you desire.
   *
   * @return a byte array representing the optional metadata
   * @since 0.1.0
   */
  @SuppressWarnings("mutable")
  public abstract byte[] metadata();

  /**
   * Current download progress for this specific offline region. This will be 0 until the download
   * actually starts and will be a value between 0 and 100 once the download begins.
   * {@link OfflinePlugin#onProgressChanged(OfflineDownloadOptions, int)} should be used during the
   * download session to get progress updates.
   *
   * @return an integer value between 0 and 100 representing the download progress
   * @since 0.1.0
   */
  public abstract int progress();

  /**
   * A region id which the service provides so internally identifying a particular region is easier
   * during the download session. You'll rarely, if ever, need to use this information.
   *
   * @return a long, unique, identifier for this offline download region
   * @since 0.1.0
   */
  @NonNull
  public abstract Long uuid();

  /**
   * Used to build a new instance of this class.
   *
   * @return this classes builder class
   * @since 0.1.0
   */
  public static Builder builder() {
    return new AutoValue_OfflineDownloadOptions.Builder()
      .uuid(UUID.randomUUID().getMostSignificantBits())
      .metadata(new byte[] {})
      .progress(0);
    // TODO user must provide a notificationOptions object
  }

  /**
   * A convenient way to build a new instance of this class using all of the same values this current
   * instance has. Use this when you wish to modify a single entry.
   *
   * @return a new {@link Builder} instance with all the same values this classes instance contains
   * @since 0.1.0
   */
  public abstract Builder toBuilder();

  /**
   * The Builder class in charge of constructing this class and setting the values accordingly.
   *
   * @since 0.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * <strong>Internal usage only</strong>
     * <p>
     * A region id which the service provides so internally identifying a particular region is easier
     * during the download session.
     * </p>
     *
     * @param uuid a long, unique, identifier for this offline download region provided by the
     *             download service.
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public abstract Builder uuid(@NonNull Long uuid);

    /**
     * Current download progress for this specific offline region. This will be 0 until the download
     * actually starts and will be a value between 0 and 100 once the download begins.
     * {@link OfflinePlugin#onProgressChanged(OfflineDownloadOptions, int)} should be used during the
     * download session to get progress updates.
     *
     * @param progress an integer value between 0 and 100 representing the download progress
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public abstract Builder progress(int progress);

    /**
     * The download definition which is used to actually launch the download inside the service. This
     * object particularly contains information such as the region bounding box, zoom levels, pixel
     * density, etc.
     *
     * @param definition the Map SDK Offline region definition which can be used to acquire region
     *                   download definition information
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public abstract Builder definition(@NonNull OfflineRegionDefinition definition);

    /**
     * Launching the service requires a notification, using the {@link NotificationOptions} class
     * allows you to customize this notification and will be displayed for the offline download region
     * while the service is running in the background.
     *
     * @param notificationOptions the {@link NotificationOptions} which will be used during the
     *                            download process
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public abstract Builder notificationOptions(NotificationOptions notificationOptions);

    /**
     * A convenient method for providing the offline region a name which can then be used later to
     * identify the specific region in the database of offline region tiles.
     *
     * @param regionName the string which represents this offline regions name
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public abstract Builder regionName(@Nullable String regionName);

    /**
     * Metadata can optionally be added to your offline download region which can contain any
     * additional information you desire.
     *
     * @param metadata a byte array representing the optional metadata
     * @return this builder for chaining options together
     * @since 0.1.0
     */
    public abstract Builder metadata(@NonNull byte[] metadata);

    /**
     * Build a new {@link OfflineDownloadOptions} instance using the information and values provided
     * inside this builder class
     *
     * @return a new instance of the {@link OfflineDownloadOptions} which is using the values you
     * provided in this builder
     * @since 0.1.0
     */
    public abstract OfflineDownloadOptions build();
  }
}
