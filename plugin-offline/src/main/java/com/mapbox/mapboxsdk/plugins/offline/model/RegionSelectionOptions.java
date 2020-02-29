package com.mapbox.mapboxsdk.plugins.offline.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.plugins.offline.ui.OfflineActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Options specific to the Region Selection UI component.
 *
 * @since 0.2.0
 */
@AutoValue
public abstract class RegionSelectionOptions implements Parcelable {

  /**
   * Define the map's starting camera position by providing a bounding box
   *
   * @return the bounding box which is where the {@link OfflineActivity} initial map's camera
   * position will be placed
   * @since 0.2.0
   */
  @Nullable
  public abstract LatLngBounds startingBounds();

  /**
   * Define the map's starting camera position by providing a camera position.
   *
   * @return the camera position which is where the {@link OfflineActivity} initial map's camera
   * position will be placed
   * @since 0.2.0
   */
  @Nullable
  public abstract CameraPosition statingCameraPosition();

  /**
   * A convenient way to build a new instance of this class using all of the same values this current
   * instance has. Use this when you wish to modify a single entry.
   *
   * @return a new {@link Builder} instance with all the same values this classes instance contains
   * @since 0.2.0
   */
  public abstract Builder toBuilder();

  /**
   * Used to build a new instance of this class.
   *
   * @return this classes builder class
   * @since 0.2.0
   */
  public static Builder builder() {
    return new AutoValue_RegionSelectionOptions.Builder();
  }

  /**
   * The Builder class in charge of constructing this class and setting the values accordingly.
   *
   * @since 0.2.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Define the map's starting camera position by providing a bounding box
     *
     * @param bounds the bounding box which is where the {@link OfflineActivity} initial map's camera
     *               position will be placed
     * @return this builder for chaining options together
     * @since 0.2.0
     */
    public abstract Builder startingBounds(@NonNull LatLngBounds bounds);

    /**
     * Define the map's starting camera position by providing a camera position.
     *
     * @param cameraPosition the camera position which is where the {@link OfflineActivity} initial
     *                       map's camera position will be placed
     * @return this builder for chaining options together
     * @since 0.2.0
     */
    public abstract Builder statingCameraPosition(@NonNull CameraPosition cameraPosition);

    /**
     * Build a new {@link RegionSelectionOptions} instance using the information and values provided
     * inside this builder class
     *
     * @return a new instance of the {@link RegionSelectionOptions} which is using the values you
     * provided in this builder
     * @since 0.2.0
     */
    public abstract RegionSelectionOptions build();
  }
}
