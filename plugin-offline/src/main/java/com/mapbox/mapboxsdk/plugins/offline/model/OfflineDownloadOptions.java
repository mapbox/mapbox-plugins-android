package com.mapbox.mapboxsdk.plugins.offline.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadService;

import java.util.UUID;

/**
 * This model class wraps the offline region definition with notifications options and the offline
 * region metadata. It is a companion object to {@link OfflineRegion} with regionId and
 * {@link OfflineDownloadService} with serviceId.
 *
 * @since 0.1.0
 */
@AutoValue
public abstract class OfflineDownloadOptions implements Parcelable {

  @NonNull
  public abstract OfflineTilePyramidRegionDefinition definition();

  public abstract NotificationOptions notificationOptions();

  @Nullable
  public abstract String regionName();

  @SuppressWarnings("mutable")
  public abstract byte[] metadata();

  public abstract long regionId();

  public abstract int progress();

  @NonNull
  public abstract Long uuid();

  public static Builder builder() {
    return new AutoValue_OfflineDownloadOptions.Builder()
      .uuid(UUID.randomUUID().getMostSignificantBits())
      .metadata(new byte[] {})
      .progress(0)
      .regionId(-1);
    // TODO user must provide a notificationOptions object
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    abstract Builder uuid(@NonNull Long uuid);

    public abstract Builder progress(int progress);

    public abstract Builder definition(@NonNull OfflineTilePyramidRegionDefinition definition);

    public abstract Builder notificationOptions(NotificationOptions notificationOptions);

    public abstract Builder regionName(@Nullable String regionName);

    public abstract Builder metadata(@NonNull byte[] metadata);

    public abstract Builder regionId(long regionId);

    public abstract OfflineDownloadOptions build();
  }
}
