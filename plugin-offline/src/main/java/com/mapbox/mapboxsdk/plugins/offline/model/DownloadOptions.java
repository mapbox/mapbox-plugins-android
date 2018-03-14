package com.mapbox.mapboxsdk.plugins.offline.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflineDownloadService;

/**
 * This model class wraps the offline region definition with notifications options and the offline
 * region metadata. It is a companion object to {@link OfflineRegion} with regionId and
 * {@link OfflineDownloadService} with serviceId.
 *
 * @since 0.1.0
 */
@AutoValue
public abstract class DownloadOptions implements Parcelable {

  private int progress;

  @NonNull
  public abstract OfflineTilePyramidRegionDefinition definition();

  public abstract NotificationOptions notificationOptions();

  @Nullable
  public abstract String regionName();

  public abstract byte[] metadata();

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public int getProgress() {
    return progress;
  }

  public static Builder builder() {
    return new AutoValue_DownloadOptions.Builder()
      .metadata(new byte[]{});
    // TODO user must provide a notificationOptions object¬
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder definition(@NonNull OfflineTilePyramidRegionDefinition definition);

    public abstract Builder notificationOptions(NotificationOptions notificationOptions);

    public abstract Builder regionName(@Nullable String regionName);

    public abstract Builder metadata(@NonNull byte[] metadata);

    public abstract DownloadOptions build();

  }
}
