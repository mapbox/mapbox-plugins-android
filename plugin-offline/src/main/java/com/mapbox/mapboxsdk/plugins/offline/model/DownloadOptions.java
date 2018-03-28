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
public abstract class DownloadOptions implements Parcelable {

  @NonNull
  public abstract OfflineTilePyramidRegionDefinition definition();

  public abstract NotificationOptions notificationOptions();

  @Nullable
  public abstract String regionName();

  public abstract byte[] metadata();

  @NonNull
  public abstract Long uuid();

  public static Builder builder() {
    return new AutoValue_DownloadOptions.Builder()
      .metadata(new byte[] {})
      .uuid(UUID.randomUUID().getMostSignificantBits());
    // TODO user must provide a notificationOptions object¬
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    abstract Builder uuid(@NonNull Long uuid);

    public abstract Builder definition(@NonNull OfflineTilePyramidRegionDefinition definition);

    public abstract Builder notificationOptions(NotificationOptions notificationOptions);

    public abstract Builder regionName(@Nullable String regionName);

    public abstract Builder metadata(@NonNull byte[] metadata);

    public abstract DownloadOptions build();

  }
}
