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

@AutoValue
public abstract class LocationLayerOptions implements Parcelable {

  /**
   * Default accuracy alpha
   */
  private static final float ACCURACY_ALPHA_DEFAULT = 0.15f;

  public static LocationLayerOptions createFromAttributes(@NonNull Context context,
                                                          @StyleRes int styleRes) {

    final TypedArray typedArray = context.obtainStyledAttributes(
      styleRes, R.styleable.LocationLayer);

    LocationLayerOptions.Builder builder = builder();

    builder.foregroundDrawable(typedArray.getResourceId(
      R.styleable.LocationLayer_foregroundDrawable, -1));
    if (typedArray.hasValue(R.styleable.LocationLayer_foregroundTintColor)) {
      builder.foregroundTintColor(typedArray.getColor(
        R.styleable.LocationLayer_foregroundTintColor, -1));
    }
    builder.backgroundDrawable(typedArray.getResourceId(
      R.styleable.LocationLayer_backgroundDrawable, -1));
    if (typedArray.hasValue(R.styleable.LocationLayer_backgroundTintColor)) {
      builder.backgroundTintColor(typedArray.getColor(
        R.styleable.LocationLayer_backgroundTintColor, -1));
    }
    builder.foregroundDrawableStale(typedArray.getResourceId(
      R.styleable.LocationLayer_foregroundDrawableStale, -1));
    if (typedArray.hasValue(R.styleable.LocationLayer_foregroundStaleTintColor)) {
      builder.foregroundStaleTintColor(typedArray.getColor(
        R.styleable.LocationLayer_foregroundStaleTintColor, -1));
    }
    builder.backgroundDrawableStale(typedArray.getResourceId(
      R.styleable.LocationLayer_backgroundDrawableStale, -1));
    if (typedArray.hasValue(R.styleable.LocationLayer_backgroundStaleTintColor)) {
      builder.backgroundStaleTintColor(typedArray.getColor(
        R.styleable.LocationLayer_backgroundStaleTintColor, -1));
    }
    builder.bearingDrawable(typedArray.getResourceId(
      R.styleable.LocationLayer_bearingDrawable, -1));
    if (typedArray.hasValue(R.styleable.LocationLayer_bearingTintColor)) {
      builder.bearingTintColor(typedArray.getColor(
        R.styleable.LocationLayer_bearingTintColor, -1));
    }
    builder.navigationDrawable(typedArray.getResourceId(
      R.styleable.LocationLayer_navigationDrawable, -1));
    float elevation = typedArray.getDimension(
      R.styleable.LocationLayer_elevation, 0);
    builder.accuracyColor(typedArray.getColor(
      R.styleable.LocationLayer_accuracyColor, -1));

    float accuracyAlpha = typedArray.getFloat(
      R.styleable.LocationLayer_accuracyAlpha, ACCURACY_ALPHA_DEFAULT);
    if (accuracyAlpha < 0 || accuracyAlpha > 1) {
      throw new IllegalArgumentException(
        "Location layer accuracy alpha value must be between 0.0 and 1.0.");
    }
    builder.accuracyAlpha(accuracyAlpha);

    if (elevation < 0f) {
      throw new IllegalArgumentException("Invalid shadow size " + elevation + ". Must be >= 0");
    }
    builder.elevation(elevation);

    typedArray.recycle();

    return builder.build();
  }

  public abstract Builder toBuilder();

  public static Builder builder(Context context) {
    return LocationLayerOptions.createFromAttributes(context, R.style.LocationLayer).toBuilder();
  }

  private static Builder builder() {
    return new AutoValue_LocationLayerOptions.Builder();
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

  @ColorInt
  public abstract int accuracyColor();

  @DrawableRes
  public abstract int backgroundDrawableStale();

  @DrawableRes
  public abstract int foregroundDrawableStale();

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

  @DrawableRes
  public abstract int backgroundDrawable();

  @DrawableRes
  public abstract int bearingDrawable();

  @ColorInt
  @Nullable
  public abstract Integer foregroundTintColor();

  @ColorInt
  @Nullable
  public abstract Integer backgroundTintColor();

  @Dimension
  public abstract float elevation();

  @ColorInt
  @Nullable
  public abstract Integer foregroundStaleTintColor();

  @ColorInt
  @Nullable
  public abstract Integer backgroundStaleTintColor();

  @ColorInt
  @Nullable
  public abstract Integer bearingTintColor();

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

    public abstract Builder accuracyColor(@ColorInt int accuracyColor);

    public abstract Builder foregroundDrawableStale(@DrawableRes int foregroundDrawableStale);

    public abstract Builder foregroundStaleTintColor(@ColorInt Integer foregroundStaleTintColor);

    public abstract Builder backgroundDrawableStale(@DrawableRes int backgroundDrawableStale);

    public abstract Builder backgroundStaleTintColor(@ColorInt Integer backgroundStaleTintColor);

    public abstract Builder navigationDrawable(@DrawableRes int navigationDrawable);

    /**
     * Supply a Drawable that is to be rendered on top of all of the content in the Location Layer
     * Plugin layer stack.
     *
     * @param foregroundDrawable the drawable resource used for the foreground layer
     * @attr ref R.styleable#LocationLayer_foregroundDrawable
     * @since 0.4.0
     */
    public abstract Builder foregroundDrawable(@DrawableRes int foregroundDrawable);

    public abstract Builder backgroundDrawable(@DrawableRes int backgroundDrawable);

    public abstract Builder bearingDrawable(@DrawableRes int bearingDrawable);

    public abstract Builder bearingTintColor(@ColorInt Integer bearingTintColor);

    public abstract Builder foregroundTintColor(@ColorInt Integer foregroundTintColor);

    public abstract Builder backgroundTintColor(@ColorInt Integer backgroundTintColor);

    public abstract Builder elevation(@Dimension float elevation);

    public abstract LocationLayerOptions build();
  }
}
