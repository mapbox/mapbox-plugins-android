package com.mapbox.mapboxsdk.plugins.mylocationlayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

/**
 * Options exposed for you to modify the visual appearance of the My Location layer plugin. This objects automatically
 * created when a new instance of {@link MyLocationLayerPlugin} is created.
 * {@link MyLocationLayerPlugin#getMyLocationLayerOptions()} should be used instead of creating your own instance of
 * this object.
 *
 * @since 0.1.0
 */
public class MyLocationLayerOptions implements MapView.OnMapChangedListener {

  private MyLocationLayerPlugin myLocationLayer;
  private MapboxMap mapboxMap;
  private MapView mapView;

  // Paint properties
  private Float accuracyAlpha;
  @ColorInt
  private int foregroundTintColor;
  @ColorInt
  private int backgroundTintColor;
  @ColorInt
  private int accuracyTintColor;

  // Drawable resources
  private Drawable foregroundBearingDrawable;
  private Drawable foregroundDrawable;
  private Drawable backgroundDrawable;

  /**
   * Construct a {@code MyLocationLayerOptions} object. The {@link MyLocationLayerPlugin} is in charge of this, and the
   * object can be acquired using {@link MyLocationLayerPlugin#getMyLocationLayerOptions()}
   *
   * @param mapView   the MapView to apply the My Location layer plugin to
   * @param mapboxMap the MapboxMap to apply the My Location layer plugin with
   * @since 0.1.0
   */
  MyLocationLayerOptions(MyLocationLayerPlugin myLocationLayer, MapView mapView, MapboxMap mapboxMap) {
    this.myLocationLayer = myLocationLayer;
    this.mapView = mapView;
    this.mapboxMap = mapboxMap;

    mapView.addOnMapChangedListener(this);
    initialize();
  }

  /**
   * Provides all the default values when the My Location layer plugin is first constructed or if the map style changes.
   *
   * @since 0.1.0
   */
  private void initialize() {

    // Create an empty feature collection
    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});

    // Create the sources
    GeoJsonSource locationSource = new GeoJsonSource(MyLocationLayerConstants.LOCATION_SOURCE, emptyFeature);
    mapboxMap.addSource(locationSource);
    GeoJsonSource locationAccuracySource
      = new GeoJsonSource(MyLocationLayerConstants.LOCATION_ACCURACY_SOURCE, emptyFeature);
    mapboxMap.addSource(locationAccuracySource);

    // If style changes, the previous options should be restored instead of overwritten.
    if (foregroundDrawable == null) {
      foregroundDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_icon);
    }
    if (foregroundBearingDrawable == null) {
      foregroundBearingDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_icon);
    }
    setForegroundDrawable(foregroundDrawable, foregroundBearingDrawable);

    setForegroundTintColor((foregroundTintColor == 0)
      ? ContextCompat.getColor(mapView.getContext(), R.color.mapbox_blue) : foregroundTintColor
    );

    if (backgroundDrawable == null) {
      backgroundDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_stroke_icon);
    }
    setBackgroundDrawable(backgroundDrawable);
    setBackgroundTintColor((backgroundTintColor == 0) ? Color.WHITE : backgroundTintColor);
    setAccuracyAlpha((accuracyAlpha == null) ? 0.15f : accuracyAlpha);
    setAccuracyTintColor((accuracyTintColor == 0)
      ? ContextCompat.getColor(mapView.getContext(), R.color.mapbox_blue) : accuracyTintColor);
  }

  /**
   * Overriding to handle when the map style changes.
   *
   * @param change map change constant
   * @since 0.1.0
   */
  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_LOADING_STYLE && myLocationLayer.isMyLocationEnabled()) {
      initialize();
    }
  }

  /**
   * Set the foreground drawable of the My Location layer. This is the blue circle/dot by default, the bearing drawable
   * includes the chevron/arrow.
   *
   * @param foregroundDrawable        the drawable to show as foreground without bearing
   * @param foregroundBearingDrawable the drawable to show as foreground when bearing is enabled
   * @since 0.1.0
   */
  public void setForegroundDrawable(Drawable foregroundDrawable, Drawable foregroundBearingDrawable) {
    // Remove previous icon if one has been added already
    if (this.foregroundDrawable != null) {
      mapboxMap.removeImage(MyLocationLayerConstants.USER_LOCATION_ICON);
    }
    if (this.foregroundBearingDrawable != null) {
      mapboxMap.removeImage(MyLocationLayerConstants.USER_LOCATION_BEARING_ICON);
    }

    this.foregroundDrawable = foregroundDrawable;
    this.foregroundBearingDrawable = foregroundBearingDrawable;
    // Add the location icon image to the map
    Bitmap icon = getBitmapFromDrawable(foregroundDrawable);
    mapboxMap.addImage(MyLocationLayerConstants.USER_LOCATION_ICON, icon);
    Bitmap bearingIcon = getBitmapFromDrawable(foregroundBearingDrawable);
    mapboxMap.addImage(MyLocationLayerConstants.USER_LOCATION_BEARING_ICON, bearingIcon);

    if (mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_LAYER) == null) {
      SymbolLayer locationLayer = new SymbolLayer(
        MyLocationLayerConstants.LOCATION_LAYER, MyLocationLayerConstants.LOCATION_SOURCE
      ).withProperties(
        PropertyFactory.iconImage(MyLocationLayerConstants.USER_LOCATION_ICON),
        PropertyFactory.iconAllowOverlap(true),
        PropertyFactory.iconIgnorePlacement(true),
        PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
      mapboxMap.addLayer(locationLayer);
    }
  }

  /**
   * Get the foreground drawable when bearing is disabled.
   *
   * @return the drawable used as foreground
   * @since 0.1.0
   */
  public Drawable getForegroundDrawable() {
    return foregroundDrawable;
  }

  /**
   * Get the foreground drawable when bearing is enabled.
   *
   * @return the bearing drawable used as foreground
   * @since 0.1.0
   */
  public Drawable getForegroundBearingDrawable() {
    return foregroundBearingDrawable;
  }

  /**
   * Set the foreground tint color. The color will tint both the foreground and the bearing foreground drawable.
   *
   * @param foregroundTintColor the color to tint the foreground drawable
   * @since 0.1.0
   */
  public void setForegroundTintColor(@ColorInt int foregroundTintColor) {
    this.foregroundTintColor = foregroundTintColor;
    if (foregroundDrawable != null) {
      DrawableCompat.setTint(foregroundDrawable, foregroundTintColor);
      Bitmap userLocationIcon = getBitmapFromDrawable(foregroundDrawable);
      mapboxMap.removeImage(MyLocationLayerConstants.USER_LOCATION_ICON);
      mapboxMap.addImage(MyLocationLayerConstants.USER_LOCATION_ICON, userLocationIcon);
    }
    if (foregroundBearingDrawable != null) {
      DrawableCompat.setTint(foregroundBearingDrawable, foregroundTintColor);
      Bitmap userBearingIcon = getBitmapFromDrawable(foregroundBearingDrawable);
      mapboxMap.addImage(MyLocationLayerConstants.USER_LOCATION_BEARING_ICON, userBearingIcon);
    }
  }

  /**
   * Get the foreground tint color.
   *
   * @return the foreground tint color provided as a {@link ColorInt}
   * @since 0.1.0
   */
  public int getForegroundTintColor() {
    return foregroundTintColor;
  }

  /**
   * Set the background drawable of My Location layer.
   *
   * @param backgroundDrawable the drawable to show as background
   * @since 0.1.0
   */
  public void setBackgroundDrawable(Drawable backgroundDrawable) {
    this.backgroundDrawable = backgroundDrawable;

    Bitmap bitmap = getBitmapFromDrawable(backgroundDrawable);
    mapboxMap.addImage(MyLocationLayerConstants.USER_LOCATION_BACKGROUND_ICON, bitmap);

    if (mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_BACKGROUND_LAYER) == null) {
      SymbolLayer locationBackgroundLayer = new SymbolLayer(
        MyLocationLayerConstants.LOCATION_BACKGROUND_LAYER, MyLocationLayerConstants.LOCATION_SOURCE
      ).withProperties(
        PropertyFactory.iconImage(MyLocationLayerConstants.USER_LOCATION_BACKGROUND_ICON),
        PropertyFactory.iconAllowOverlap(true),
        PropertyFactory.iconIgnorePlacement(true),
        PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
      mapboxMap.addLayerBelow(locationBackgroundLayer, MyLocationLayerConstants.LOCATION_LAYER);
    }
  }

  /**
   * Get the background drawable of My Location layer.
   *
   * @return the drawable used as background
   * @since 0.1.0
   */
  public Drawable getBackgroundDrawable() {
    return backgroundDrawable;
  }

  /**
   * Set the background tint color.
   *
   * @param backgroundTintColor the color to tint the background
   * @since 0.1.0
   */
  public void setBackgroundTintColor(@ColorInt int backgroundTintColor) {
    this.backgroundTintColor = backgroundTintColor;
    DrawableCompat.setTint(backgroundDrawable, backgroundTintColor);
    Bitmap bitmap = getBitmapFromDrawable(backgroundDrawable);
    mapboxMap.addImage(MyLocationLayerConstants.USER_LOCATION_BACKGROUND_ICON, bitmap);
  }

  /**
   * Get the background tint color.
   *
   * @return the background tint color
   * @since 0.1.0
   */
  public int getBackgroundTintColor() {
    return backgroundTintColor;
  }

  /**
   * Set the alpha value of the accuracy circle of My Location layer. Default is {@code 0.15f} and can range from 0.0
   * to 1.0.
   *
   * @param accuracyAlpha the alpha value to set
   * @since 0.1.0
   */
  public void setAccuracyAlpha(@FloatRange(from = 0, to = 1.0) float accuracyAlpha) {
    this.accuracyAlpha = accuracyAlpha;
    Layer layer = mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_ACCURACY_LAYER);
    if (layer == null) {
      FillLayer locationAccuracyLayer = new FillLayer(
        MyLocationLayerConstants.LOCATION_ACCURACY_LAYER, MyLocationLayerConstants.LOCATION_ACCURACY_SOURCE
      ).withProperties(
        PropertyFactory.fillColor(Color.parseColor("#4882C6")),
        PropertyFactory.fillOpacity(accuracyAlpha)
      );
      mapboxMap.addLayerBelow(locationAccuracyLayer, MyLocationLayerConstants.LOCATION_BACKGROUND_LAYER);
    } else {
      layer.setProperties(
        PropertyFactory.fillOpacity(accuracyAlpha)
      );
    }
  }

  /**
   * Get the alpha value of the accuracy circle of My Location layer.
   *
   * @return the alpha value
   * @since 0.1.0
   */
  public float getAccuracyAlpha() {
    return accuracyAlpha;
  }

  /**
   * Set the accuracy tint color of My Location layer.
   *
   * @param accuracyTintColor the accuracy tint color
   * @since 0.1.0
   */
  public void setAccuracyTintColor(@ColorInt int accuracyTintColor) {
    this.accuracyTintColor = accuracyTintColor;
    Layer layer = mapboxMap.getLayer(MyLocationLayerConstants.LOCATION_ACCURACY_LAYER);
    if (layer != null) {
      layer.setProperties(
        PropertyFactory.fillColor(accuracyTintColor)
      );
    }
  }

  /**
   * Get the accuracy tint color of My Location layer.
   *
   * @return the tint color used for accuracy
   * @since 0.1.0
   */
  public int getAccuracyTintColor() {
    return accuracyTintColor;
  }

  /**
   * Converts drawable file to a bitmap file ready to be consumed by {@link MapboxMap#addImage(String, Bitmap)}
   *
   * @param drawable any {@link Drawable} file.
   * @return the drawable converted to a {@link Bitmap} file.
   * @since 0.1.0
   */
  private static Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else {
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
    }
  }
}