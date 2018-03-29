package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.google.auto.value.AutoValue;

/**
 * This class exposes options for the Location Layer Plugin. The options can be set by defining a
 * style in your apps style.xml file and passing in directly into the {@link LocationLayerPlugin}
 * class. Alternatively, if properties need to be changed at runtime depending on a specific state,
 * you can build an instance of this class, setting the values you desire, and then passing it into
 * either the {@link LocationLayerPlugin} constructor (if it isn't initialized yet) or
 * {@link LocationLayerPlugin#applyStyle(LocationLayerOptions)}.
 * <p>
 * When the {@link #createFromAttributes(Context, int)} methods called, any attributes not found
 * inside the style will revert back to using their default set values. Likewise, when building a
 * new {@link LocationLayerOptions} class using the builder, any options neglecting to be set will
 * reset to their default values.
 * <p>
 * If you would like to keep your custom style changes while modifying a single attribute, you can
 * get the currently used options object using {@link LocationLayerPlugin#getLocationLayerOptions()}
 * and it's {@code toBuilder} method to modify a single entry while also maintaining the other
 * settings. Once your modifications have been made, you'll need to pass it back into the location
 * layer plugin using {@link LocationLayerPlugin#applyStyle(LocationLayerOptions)}.
 *
 * @since 0.4.0
 */
@AutoValue
public abstract class LocationLayerOptions implements Parcelable {

  /**
   * Default accuracy alpha
   */
  private static final float ACCURACY_ALPHA_DEFAULT = 0.15f;

  /**
   * Default max map zoom
   */
  private static final float MAX_ZOOM_DEFAULT = 18;

  /**
   * Default min map zoom
   */
  private static final float MIN_ZOOM_DEFAULT = 2;

  /**
   * Default map padding
   */
  private static final int[] PADDING_DEFAULT = {0, 0, 0, 0};

  /**
   * The default value which is used when the stale state is enabled
   */
  private static final long STALE_STATE_DELAY_MS = 30000;

  /**
   * Construct a new Location Layer Options class using the attributes found within a style
   * resource. It's important to note that you only need to define the attributes you plan to
   * change and can safely ignore the other attributes which will be set to their default value.
   *
   * @param context  your activity's context used for acquiring resources
   * @param styleRes the style id where your custom attributes are defined
   * @return a new {@link LocationLayerOptions} object with the settings you defined in your style
   * resource
   * @since 0.4.0
   */
  public static LocationLayerOptions createFromAttributes(@NonNull Context context,
                                                          @StyleRes int styleRes) {

    final TypedArray typedArray = context.obtainStyledAttributes(
      styleRes, R.styleable.mapbox_LocationLayer);

    LocationLayerOptions.Builder builder = builder();

    builder.foregroundDrawable(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_foregroundDrawable, -1));
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_foregroundTintColor)) {
      builder.foregroundTintColor(typedArray.getColor(
        R.styleable.mapbox_LocationLayer_mapbox_foregroundTintColor, -1));
    }
    builder.backgroundDrawable(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_backgroundDrawable, -1));
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_backgroundTintColor)) {
      builder.backgroundTintColor(typedArray.getColor(
        R.styleable.mapbox_LocationLayer_mapbox_backgroundTintColor, -1));
    }
    builder.foregroundDrawableStale(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_foregroundDrawableStale, -1));
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_foregroundStaleTintColor)) {
      builder.foregroundStaleTintColor(typedArray.getColor(
        R.styleable.mapbox_LocationLayer_mapbox_foregroundStaleTintColor, -1));
    }
    builder.backgroundDrawableStale(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_backgroundDrawableStale, -1));
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_backgroundStaleTintColor)) {
      builder.backgroundStaleTintColor(typedArray.getColor(
        R.styleable.mapbox_LocationLayer_mapbox_backgroundStaleTintColor, -1));
    }
    builder.bearingDrawable(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_bearingDrawable, -1));
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_bearingTintColor)) {
      builder.bearingTintColor(typedArray.getColor(
        R.styleable.mapbox_LocationLayer_mapbox_bearingTintColor, -1));
    }
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_enableStaleState)) {
      builder.enableStaleState(typedArray.getBoolean(
        R.styleable.mapbox_LocationLayer_mapbox_enableStaleState, true));
    }
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_staleStateTimeout)) {
      builder.staleStateTimeout(typedArray.getInteger(
        R.styleable.mapbox_LocationLayer_mapbox_staleStateTimeout, (int) STALE_STATE_DELAY_MS));
    }
    builder.gpsDrawable(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_navigationDrawable, -1));
    float elevation = typedArray.getDimension(
      R.styleable.mapbox_LocationLayer_mapbox_elevation, 0);
    builder.accuracyColor(typedArray.getColor(
      R.styleable.mapbox_LocationLayer_mapbox_accuracyColor, -1));
    builder.accuracyAlpha(typedArray.getFloat(
      R.styleable.mapbox_LocationLayer_mapbox_accuracyAlpha, ACCURACY_ALPHA_DEFAULT));
    builder.elevation(elevation);

    builder.trackingInitialMoveThreshold(typedArray.getInteger(
      R.styleable.mapbox_LocationLayer_mapbox_trackingInitialMoveThreshold,
      (int) context.getResources().getDimension(R.dimen.mapbox_locationLayerTrackingInitialMoveThreshold)));

    builder.trackingInitialMoveThreshold(typedArray.getInteger(
      R.styleable.mapbox_LocationLayer_mapbox_trackingMultiFingerMoveThreshold,
      (int) context.getResources().getDimension(R.dimen.mapbox_locationLayerTrackingMultiFingerMoveThreshold)));

    builder.padding(new int[] {
      typedArray.getInt(R.styleable.mapbox_LocationLayer_mapbox_iconPaddingLeft, 0),
      typedArray.getInt(R.styleable.mapbox_LocationLayer_mapbox_iconPaddingTop, 0),
      typedArray.getInt(R.styleable.mapbox_LocationLayer_mapbox_iconPaddingRight, 0),
      typedArray.getInt(R.styleable.mapbox_LocationLayer_mapbox_iconPaddingBottom, 0),
    });

    builder.maxZoom(typedArray.getFloat(R.styleable.mapbox_LocationLayer_mapbox_maxZoom, MAX_ZOOM_DEFAULT));
    builder.minZoom(typedArray.getFloat(R.styleable.mapbox_LocationLayer_mapbox_minZoom, MIN_ZOOM_DEFAULT));

    typedArray.recycle();

    return builder.build();
  }

  /**
   * Takes the currently constructed {@link LocationLayerOptions} object and provides it's builder
   * with all the values set matching the values in this instance. This allows you to modify a
   * single attribute and then rebuild the object.
   *
   * @return the Location Layer builder which contains the values defined in this current instance
   * as defaults.
   * @since 0.4.0
   */
  public abstract Builder toBuilder();

  /**
   * Build a new instance of the {@link LocationLayerOptions} class with all the attributes set
   * automatically to their defined defaults in this library. This allows you to adjust a few
   * attributes while leaving the rest alone and maintaining their default behavior.
   *
   * @param context your activates context used to acquire the style resource
   * @return the Location Layer builder which contains the default values defined by the style
   * resource
   * @since 0.4.0
   */
  public static Builder builder(Context context) {
    return LocationLayerOptions.createFromAttributes(context,
      R.style.mapbox_LocationLayer).toBuilder();
  }

  // Internal builder
  private static Builder builder() {
    return new AutoValue_LocationLayerOptions.Builder()
      .enableStaleState(true)
      .staleStateTimeout(STALE_STATE_DELAY_MS)
      .maxZoom(MAX_ZOOM_DEFAULT)
      .minZoom(MIN_ZOOM_DEFAULT)
      .padding(PADDING_DEFAULT);
  }

  /**
   * Set the opacity of the accuracy view to a value from 0 to 1, where 0 means the accuracy view is
   * completely transparent and 1 means the view is completely opaque.
   *
   * @return the opacity of the accuracy view
   * @attr ref R.styleable#LocationLayer_accuracyAlpha
   * @since 0.4.0
   */
  public abstract float accuracyAlpha();

  /**
   * Solid color to use as the accuracy view color property.
   *
   * @return the color of the accuracy view
   * @attr ref R.styleable#LocationLayer_accuracyColor
   * @since 0.4.0
   */
  @ColorInt
  public abstract int accuracyColor();

  /**
   * Defines the drawable used for the stale background icon.
   *
   * @return the drawable resource ID
   * @attr ref R.styleable#LocationLayer_backgroundDrawableStale
   * @since 0.4.0
   */
  @DrawableRes
  public abstract int backgroundDrawableStale();

  /**
   * Defines the drawable used for the stale foreground icon.
   *
   * @return the drawable resource ID
   * @attr ref R.styleable#LocationLayer_foregroundDrawableStale
   * @since 0.4.0
   */
  @DrawableRes
  public abstract int foregroundDrawableStale();

  /**
   * Defines the drawable used for the navigation state icon.
   *
   * @return the drawable resource ID
   * @attr ref R.styleable#LocationLayer_navigationDrawable
   * @since 0.4.0
   */
  @DrawableRes
  public abstract int gpsDrawable();

  /**
   * Supply a Drawable that is to be rendered on top of all of the content in the Location Layer
   * Plugin layer stack.
   *
   * @return the drawable resource used for the foreground layer
   * @attr ref R.styleable#LocationLayer_foregroundDrawable
   * @since 0.4.0
   */
  @DrawableRes
  public abstract int foregroundDrawable();

  /**
   * Defines the drawable used for the background state icon.
   *
   * @return the drawable resource ID
   * @attr ref R.styleable#LocationLayer_backgroundDrawable
   * @since 0.4.0
   */
  @DrawableRes
  public abstract int backgroundDrawable();

  /**
   * Defines the drawable used for the bearing icon.
   *
   * @return the drawable resource ID
   * @attr ref R.styleable#LocationLayer_bearingDrawable
   * @since 0.4.0
   */
  @DrawableRes
  public abstract int bearingDrawable();

  /**
   * Defines the bearing icon color as an integer.
   *
   * @return the color integer resource
   * @attr ref R.styleable#LocationLayer_bearingTintColor
   * @since 0.4.0
   */
  @ColorInt
  @Nullable
  public abstract Integer bearingTintColor();

  /**
   * Defines the foreground color as an integer.
   *
   * @return the color integer resource
   * @attr ref R.styleable#LocationLayer_foregroundTintColor
   * @since 0.4.0
   */
  @ColorInt
  @Nullable
  public abstract Integer foregroundTintColor();

  /**
   * Defines the background color as an integer.
   *
   * @return the color integer resource
   * @attr ref R.styleable#LocationLayer_backgroundTintColor
   * @since 0.4.0
   */
  @ColorInt
  @Nullable
  public abstract Integer backgroundTintColor();

  /**
   * Defines the foreground stale color as an integer.
   *
   * @return the color integer resource
   * @attr ref R.styleable#LocationLayer_foregroundStaleTintColor
   * @since 0.4.0
   */
  @ColorInt
  @Nullable
  public abstract Integer foregroundStaleTintColor();

  /**
   * Defines the background stale color as an integer.
   *
   * @return the color integer resource
   * @attr ref R.styleable#LocationLayer_backgroundStaleTintColor
   * @since 0.4.0
   */
  @ColorInt
  @Nullable
  public abstract Integer backgroundStaleTintColor();

  /**
   * Sets the base elevation of this view, in pixels.
   *
   * @return the elevation currently set for the location layer icon
   * @attr ref R.styleable#LocationLayer_elevation
   * @since 0.4.0
   */
  @Dimension
  public abstract float elevation();

  /**
   * Enable or disable to stale state mode. This mode indicates to the user that the location being
   * displayed on the map hasn't been updated in a specific amount of time.
   *
   * @return whether the stale state mode is enabled or not
   * @attr ref R.styleable#LocationLayer_enableStaleState
   * @since 0.4.0
   */
  public abstract boolean enableStaleState();

  /**
   * Set the delay before the location icon becomes stale. The timer begins approximately when a new
   * location update comes in and using this defined time, if an update hasn't occured by the end,
   * the location is considered stale.
   *
   * @return the duration in milliseconds which it should take before the location layer is
   * considered stale
   * @attr ref R.styleable#LocationLayer_staleStateDelay
   * @since 0.4.0
   */
  public abstract long staleStateTimeout();

  /**
   * Sets the distance from the edges of the map view’s frame to the edges of the map
   * view’s logical viewport.
   * </p>
   * <p>
   * When the value of this property is equal to {0,0,0,0}, viewport
   * properties such as `centerCoordinate` assume a viewport that matches the map
   * view’s frame. Otherwise, those properties are inset, excluding part of the
   * frame from the viewport. For instance, if the only the top edge is inset, the
   * map center is effectively shifted downward.
   * </p>
   *
   * @return integer array of padding values
   * @since 0.5.0
   */
  @SuppressWarnings("mutable")
  public abstract int[] padding();

  /**
   * The maximum zoom level the map can be displayed at.
   *
   * @return the maximum zoom level
   * @since 0.5.0
   */
  public abstract double maxZoom();

  /**
   * The minimum zoom level the map can be displayed at.
   *
   * @return the minimum zoom level
   * @since 0.5.0
   */
  public abstract double minZoom();

  /**
   * Minimum single pointer movement in pixels required to break camera tracking.
   *
   * @return the minimum movement
   * @since 0.5.0
   */
  public abstract float trackingInitialMoveThreshold();

  /**
   * Minimum multi pointer movement in pixels required to break camera tracking (for example during scale gesture).
   *
   * @return the minimum movement
   * @since 0.5.0
   */
  public abstract float trackingMultiFingerMoveThreshold();

  /**
   * Builder class for constructing a new instance of {@link LocationLayerOptions}.
   *
   * @since 0.4.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Set the opacity of the accuracy view to a value from 0 to 1, where 0 means the accuracy view
     * is completely transparent and 1 means the view is completely opaque.
     *
     * @param accuracyAlpha the opacity of the accuracy view
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_accuracyAlpha
     * @since 0.4.0
     */
    public abstract Builder accuracyAlpha(@FloatRange(from = 0, to = 1.0) float accuracyAlpha);

    /**
     * Solid color to use as the accuracy view color property.
     *
     * @param accuracyColor the color of the accuracy view
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_accuracyColor
     * @since 0.4.0
     */
    public abstract Builder accuracyColor(@ColorInt int accuracyColor);

    /**
     * Defines the drawable used for the stale foreground icon.
     *
     * @param foregroundDrawableStale the drawable resource ID
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_foregroundDrawableStale
     * @since 0.4.0
     */
    public abstract Builder foregroundDrawableStale(@DrawableRes int foregroundDrawableStale);

    /**
     * Defines the foreground stale color as an integer.
     *
     * @param foregroundStaleTintColor the color integer resource
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_foregroundStaleTintColor
     * @since 0.4.0
     */
    public abstract Builder foregroundStaleTintColor(@ColorInt Integer foregroundStaleTintColor);

    /**
     * Defines the drawable used for the stale background icon.
     *
     * @param backgroundDrawableStale the drawable resource ID
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_backgroundDrawableStale
     * @since 0.4.0
     */
    public abstract Builder backgroundDrawableStale(@DrawableRes int backgroundDrawableStale);

    /**
     * Defines the background stale color as an integer.
     *
     * @param backgroundStaleTintColor the color integer resource
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_backgroundStaleTintColor
     * @since 0.4.0
     */
    public abstract Builder backgroundStaleTintColor(@ColorInt Integer backgroundStaleTintColor);

    /**
     * Defines the drawable used for the navigation state icon.
     *
     * @param gpsDrawable the drawable resource ID
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_navigationDrawable
     * @since 0.4.0
     */
    public abstract Builder gpsDrawable(@DrawableRes int gpsDrawable);

    /**
     * Supply a Drawable that is to be rendered on top of all of the content in the Location Layer
     * Plugin layer stack.
     *
     * @param foregroundDrawable the drawable resource used for the foreground layer
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_foregroundDrawable
     * @since 0.4.0
     */
    public abstract Builder foregroundDrawable(@DrawableRes int foregroundDrawable);

    /**
     * Defines the drawable used for the background state icon.
     *
     * @param backgroundDrawable the drawable resource ID
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_backgroundDrawable
     * @since 0.4.0
     */
    public abstract Builder backgroundDrawable(@DrawableRes int backgroundDrawable);

    /**
     * Defines the drawable used for the bearing icon.
     *
     * @param bearingDrawable the drawable resource ID
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_bearingDrawable
     * @since 0.4.0
     */
    public abstract Builder bearingDrawable(@DrawableRes int bearingDrawable);

    /**
     * Defines the bearing icon color as an integer.
     *
     * @param bearingTintColor the color integer resource
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_bearingTintColor
     * @since 0.4.0
     */
    public abstract Builder bearingTintColor(@ColorInt Integer bearingTintColor);

    /**
     * Defines the foreground color as an integer.
     *
     * @param foregroundTintColor the color integer resource
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_foregroundTintColor
     * @since 0.4.0
     */
    public abstract Builder foregroundTintColor(@ColorInt Integer foregroundTintColor);

    /**
     * Defines the background color as an integer.
     *
     * @param backgroundTintColor the color integer resource
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_backgroundTintColor
     * @since 0.4.0
     */
    public abstract Builder backgroundTintColor(@ColorInt Integer backgroundTintColor);

    /**
     * Sets the base elevation of this view, in pixels.
     *
     * @param elevation the elevation currently set for the location layer icon
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_elevation
     * @since 0.4.0
     */
    public abstract Builder elevation(@Dimension float elevation);

    /**
     * Enable or disable to stale state mode. This mode indicates to the user that the location
     * being displayed on the map hasn't been updated in a specific amount of time.
     *
     * @param enabled whether the stale state mode is enabled or not
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_enableStaleState
     * @since 0.4.0
     */
    public abstract Builder enableStaleState(boolean enabled);

    /**
     * Set the timeout before the location icon becomes stale. The timer begins approximately when a
     * new location update comes in and using this defined time, if an update hasn't occurred by the
     * end, the location is considered stale.
     *
     * @param timeout the duration in milliseconds which it should take before the location layer is
     *                considered stale
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_staleStateTimeout
     * @since 0.4.0
     */
    public abstract Builder staleStateTimeout(long timeout);

    /**
     * Sets the distance from the edges of the map view’s frame to the edges of the map
     * view’s logical viewport.
     * </p>
     * <p>
     * When the value of this property is equal to {0,0,0,0}, viewport
     * properties such as `centerCoordinate` assume a viewport that matches the map
     * view’s frame. Otherwise, those properties are inset, excluding part of the
     * frame from the viewport. For instance, if the only the top edge is inset, the
     * map center is effectively shifted downward.
     * </p>
     *
     * @param padding The margins for the map in pixels (left, top, right, bottom).
     * @since 0.5.0
     */
    public abstract Builder padding(int[] padding);

    /**
     * Sets the maximum zoom level the map can be displayed at.
     * <p>
     * The default maximum zoomn level is 22. The upper bound for this value is 25.5.
     *
     * @param maxZoom The new maximum zoom level.
     * @since 0.5.0
     */
    public abstract Builder maxZoom(double maxZoom);

    /**
     * Sets the minimum zoom level the map can be displayed at.
     *
     * @param minZoom The new minimum zoom level.
     */
    public abstract Builder minZoom(double minZoom);

    /**
     * Sets minimum single pointer movement (map pan) in pixels required to break camera tracking.
     *
     * @param moveThreshold the minimum movement
     * @since 0.5.0
     */
    public abstract Builder trackingInitialMoveThreshold(float moveThreshold);

    /**
     * Sets minimum multi pointer movement (map pan) in pixels required to break camera tracking
     * (for example during scale gesture).
     *
     * @param moveThreshold the minimum movement
     * @since 0.5.0
     */
    public abstract Builder trackingMultiFingerMoveThreshold(float moveThreshold);

    abstract LocationLayerOptions autoBuild();

    /**
     * Build a new instance of this {@link LocationLayerOptions} class.
     *
     * @return a new instance of {@link LocationLayerOptions}
     * @since 0.4.0
     */
    public LocationLayerOptions build() {
      LocationLayerOptions locationLayerOptions = autoBuild();
      if (locationLayerOptions.accuracyAlpha() < 0 || locationLayerOptions.accuracyAlpha() > 1) {
        throw new IllegalArgumentException(
          "Location layer accuracy alpha value must be between 0.0 and 1.0.");
      }

      if (locationLayerOptions.elevation() < 0f) {
        throw new IllegalArgumentException("Invalid shadow size "
          + locationLayerOptions.elevation() + ". Must be >= 0");
      }

      return locationLayerOptions;
    }
  }
}
