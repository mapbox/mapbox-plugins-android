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
    if (typedArray.hasValue(R.styleable.mapbox_LocationLayer_mapbox_staleStateDelay)) {
      builder.staleStateDelay(typedArray.getInteger(
        R.styleable.mapbox_LocationLayer_mapbox_staleStateDelay, (int) STALE_STATE_DELAY_MS));
    }
    builder.navigationDrawable(typedArray.getResourceId(
      R.styleable.mapbox_LocationLayer_mapbox_navigationDrawable, -1));
    float elevation = typedArray.getDimension(
      R.styleable.mapbox_LocationLayer_mapbox_elevation, 0);
    builder.accuracyColor(typedArray.getColor(
      R.styleable.mapbox_LocationLayer_mapbox_accuracyColor, -1));
    builder.accuracyAlpha(typedArray.getFloat(
      R.styleable.mapbox_LocationLayer_mapbox_accuracyAlpha, ACCURACY_ALPHA_DEFAULT));
    builder.elevation(elevation);

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
   * @param context your activites context used to acquire the style resource
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
      .staleStateDelay(STALE_STATE_DELAY_MS);
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
  public abstract int navigationDrawable();

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
  public abstract long staleStateDelay();

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
     * @param navigationDrawable the drawable resource ID
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_navigationDrawable
     * @since 0.4.0
     */
    public abstract Builder navigationDrawable(@DrawableRes int navigationDrawable);

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
     * Set the delay before the location icon becomes stale. The timer begins approximately when a
     * new location update comes in and using this defined time, if an update hasn't occurred by the
     * end, the location is considered stale.
     *
     * @param delay the duration in milliseconds which it should take before the location layer is
     *              considered stale
     * @return this builder for chaining options together
     * @attr ref R.styleable#LocationLayer_staleStateDelay
     * @since 0.4.0
     */
    public abstract Builder staleStateDelay(long delay);


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
