package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;

/**
 * Options exposed for you to modify the visual appearance of the My Location layer plugin. This objects automatically
 * created when a new instance of {@link LocationLayerPlugin} is created.
 * {@link LocationLayerPlugin#getMyLocationLayerOptions()} should be used instead of creating your own instance of
 * this object.
 *
 * @since 0.1.0
 */
public class LocationLayerOptions implements MapView.OnMapChangedListener {

  private LocationLayerPlugin myLocationLayer;
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

  // Text Annotations
  private String textAnnotation;
  private String navigigationTextAnnotation;

  // Drawable resources
  private Drawable foregroundBearingDrawable;
  private Drawable foregroundPuckDrawable;
  private Drawable foregroundDrawable;
  private Drawable backgroundDrawable;

  /**
   * Construct a {@code LocationLayerOptions} object. The {@link LocationLayerPlugin} is in charge of this, and the
   * object can be acquired using {@link LocationLayerPlugin#getMyLocationLayerOptions()}
   *
   * @param mapView   the MapView to apply the My Location layer plugin to
   * @param mapboxMap the MapboxMap to apply the My Location layer plugin with
   * @since 0.1.0
   */
  LocationLayerOptions(LocationLayerPlugin myLocationLayer, MapView mapView, MapboxMap mapboxMap) {
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
  void initialize() {

    // Create an empty feature collection
    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});

    // Create the sources
    if (mapboxMap.getSource(LocationLayerConstants.LOCATION_SOURCE) == null) {
      GeoJsonSource locationSource = new GeoJsonSource(LocationLayerConstants.LOCATION_SOURCE, emptyFeature);
      mapboxMap.addSource(locationSource);
    }
    if (mapboxMap.getSource(LocationLayerConstants.LOCATION_ACCURACY_SOURCE) == null) {
      GeoJsonSource locationAccuracySource
        = new GeoJsonSource(LocationLayerConstants.LOCATION_ACCURACY_SOURCE, emptyFeature);
      mapboxMap.addSource(locationAccuracySource);
    }

    // If style changes, the previous options should be restored instead of overwritten.
    if (foregroundDrawable == null) {
      foregroundDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_icon);
    }
    if (foregroundBearingDrawable == null) {
      foregroundBearingDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_bearing_icon);
    }
    if (foregroundPuckDrawable == null) {
      foregroundPuckDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_puck_icon);
    }
    setForegroundDrawable(foregroundDrawable, foregroundBearingDrawable, foregroundPuckDrawable);

    setForegroundTintColor((foregroundTintColor == 0)
      ? ContextCompat.getColor(mapView.getContext(), R.color.mapbox_plugin_location_layer_blue) : foregroundTintColor
    );

    if (backgroundDrawable == null) {
      backgroundDrawable = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_user_stroke_icon);
    }
    setBackgroundDrawable(backgroundDrawable);
    setBackgroundTintColor((backgroundTintColor == 0) ? Color.WHITE : backgroundTintColor);
    setAccuracyAlpha((accuracyAlpha == null) ? 0.15f : accuracyAlpha);
    setAccuracyTintColor((accuracyTintColor == 0)
      ? ContextCompat.getColor(mapView.getContext(), R.color.mapbox_plugin_location_layer_blue) : accuracyTintColor);
  }

  /**
   * Overriding to handle when the map style changes.
   *
   * @param change map change constant
   * @since 0.1.0
   */
  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_LOADING_STYLE && myLocationLayer.getMyLocationMode() != LocationLayerMode.NONE) {
      initialize();
    }
  }

  /**
   * Set the foreground drawable of the My Location layer. This is the blue circle/dot by default, the bearing drawable
   * includes the chevron/arrow.
   *
   * @param foregroundDrawable        the drawable to show as foreground without bearing
   * @param foregroundBearingDrawable the drawable to show as foreground when bearing is enabled
   * @param foregroundPuckDrawable    the drawable to show as foreground when navigation is enabled
   * @since 0.1.0
   */
  public void setForegroundDrawable(Drawable foregroundDrawable, Drawable foregroundBearingDrawable,
                                    Drawable foregroundPuckDrawable) {
    // Remove previous icon if one has been added already
    if (this.foregroundDrawable != null) {
      mapboxMap.removeImage(LocationLayerConstants.USER_LOCATION_ICON);
    }
    if (this.foregroundBearingDrawable != null) {
      mapboxMap.removeImage(LocationLayerConstants.USER_LOCATION_BEARING_ICON);
    }
    if (this.foregroundPuckDrawable != null) {
      mapboxMap.removeImage(LocationLayerConstants.USER_LOCATION_PUCK_ICON);
    }

    this.foregroundDrawable = foregroundDrawable;
    this.foregroundBearingDrawable = foregroundBearingDrawable;
    this.foregroundPuckDrawable = foregroundPuckDrawable;
    // Add the location icon image to the map
    Bitmap icon = getBitmapFromDrawable(foregroundDrawable);
    mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_ICON, icon);
    Bitmap bearingIcon = getBitmapFromDrawable(foregroundBearingDrawable);
    mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_BEARING_ICON, bearingIcon);
    Bitmap puckIcon = getBitmapFromDrawable(foregroundPuckDrawable);
    mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_PUCK_ICON, puckIcon);

    if (mapboxMap.getLayer(LocationLayerConstants.LOCATION_LAYER) == null) {
      SymbolLayer locationLayer = new SymbolLayer(
        LocationLayerConstants.LOCATION_LAYER, LocationLayerConstants.LOCATION_SOURCE
      ).withProperties(
        PropertyFactory.iconImage(LocationLayerConstants.USER_LOCATION_ICON),
        PropertyFactory.iconAllowOverlap(true),
        PropertyFactory.iconIgnorePlacement(true),
        PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
      mapboxMap.addLayer(locationLayer);
    }
  }

  /**
   * Get the foreground drawable when {@link LocationLayerMode#TRACKING} mode is enabled.
   *
   * @return the drawable used as foreground
   * @since 0.1.0
   */
  public Drawable getForegroundDrawable() {
    return foregroundDrawable;
  }

  /**
   * Get the foreground drawable when {@link LocationLayerMode#COMPASS} mode is enabled.
   *
   * @return the bearing drawable used as foreground
   * @since 0.1.0
   */
  public Drawable getForegroundBearingDrawable() {
    return foregroundBearingDrawable;
  }

  /**
   * Get the foreground drawable when {@link LocationLayerMode#NAVIGATION} mode is enabled.
   *
   * @return the puck drawable used as foreground
   * @since 0.1.0
   */
  public Drawable getForegroundPuckDrawable() {
    return foregroundPuckDrawable;
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
      mapboxMap.removeImage(LocationLayerConstants.USER_LOCATION_ICON);
      mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_ICON, userLocationIcon);
    }
    if (foregroundBearingDrawable != null) {
      DrawableCompat.setTint(foregroundBearingDrawable, foregroundTintColor);
      Bitmap userBearingIcon = getBitmapFromDrawable(foregroundBearingDrawable);
      mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_BEARING_ICON, userBearingIcon);
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
    mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_BACKGROUND_ICON, bitmap);

    if (mapboxMap.getLayer(LocationLayerConstants.LOCATION_BACKGROUND_LAYER) == null) {
      SymbolLayer locationBackgroundLayer = new SymbolLayer(
        LocationLayerConstants.LOCATION_BACKGROUND_LAYER, LocationLayerConstants.LOCATION_SOURCE
      ).withProperties(
        PropertyFactory.iconImage(LocationLayerConstants.USER_LOCATION_BACKGROUND_ICON),
        PropertyFactory.iconAllowOverlap(true),
        PropertyFactory.iconIgnorePlacement(true),
        PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
      mapboxMap.addLayerBelow(locationBackgroundLayer, LocationLayerConstants.LOCATION_LAYER);
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
    mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_BACKGROUND_ICON, bitmap);
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
    Layer layer = mapboxMap.getLayer(LocationLayerConstants.LOCATION_ACCURACY_LAYER);
    if (layer == null) {
      FillLayer locationAccuracyLayer = new FillLayer(
        LocationLayerConstants.LOCATION_ACCURACY_LAYER, LocationLayerConstants.LOCATION_ACCURACY_SOURCE
      ).withProperties(
        PropertyFactory.fillColor(Color.parseColor("#4882C6")),
        PropertyFactory.fillOpacity(accuracyAlpha)
      );
      mapboxMap.addLayerBelow(locationAccuracyLayer, LocationLayerConstants.LOCATION_BACKGROUND_LAYER);
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
    Layer layer = mapboxMap.getLayer(LocationLayerConstants.LOCATION_ACCURACY_LAYER);
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
   * Optionally place a string annotation beneath the My Location layer icon. Recommended to use this with either the
   * geocoder inside Mapbox Java or querying the map to show the users current address.
   *
   * @param textAnnotation the string you'd like to be placed below the location icon
   * @since 0.1.0
   */
  public void setLocationTextAnnotation(@NonNull String textAnnotation) {
    this.textAnnotation = textAnnotation;
    Layer layer = mapboxMap.getLayer(LocationLayerConstants.LOCATION_TEXT_ANNOTATION_LAYER);
    if (layer == null) {
      layer = new SymbolLayer(
        LocationLayerConstants.LOCATION_TEXT_ANNOTATION_LAYER, LocationLayerConstants.LOCATION_SOURCE
      ).withProperties(
        PropertyFactory.textSize(12f),
        PropertyFactory.textHaloColor(Color.WHITE),
        PropertyFactory.textHaloWidth(0.5f),
        PropertyFactory.textPadding(2f),
        PropertyFactory.textColor(
          ContextCompat.getColor(mapView.getContext(), R.color.mapbox_plugin_location_layer_blue)
        ),
        PropertyFactory.textOffset(new Float[] {0f, 1f}),
        PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
        PropertyFactory.textField(textAnnotation),
        PropertyFactory.textMaxWidth(8f),
        PropertyFactory.textOpacity(Function.zoom(Stops.exponential(
          stop(14.5f, PropertyFactory.textOpacity(0f)),
          stop(15f, PropertyFactory.textOpacity(1f))
        ))));
      mapboxMap.addLayer(layer);
    } else {
      layer.setProperties(
        PropertyFactory.textField(textAnnotation)
      );
    }
  }

  /**
   * Recieve the current set {@code String} being used for the tracking/compass annotation. This is only shown when
   * either {@link LocationLayerMode#TRACKING} or {@link LocationLayerMode#COMPASS} mode are being used. It will
   * return {@code null} if {@link LocationLayerOptions#setLocationTextAnnotation(String)} hasn't been called yet.
   *
   * @return String being used for the location annotation
   * @since 0.1.0
   */
  public String getLocationTextAnnotation() {
    return textAnnotation;
  }

  /**
   * Optionally, set a text annotation to show below the navigation puck/icon. Typically this would be either the
   * current street name or the duration left till a users maneuver if being used during a navigation session. This will
   * only show when {@link LocationLayerMode#NAVIGATION}'s being used.
   *
   * @param navigigationTextAnnotation NonNull String value to be used for the annotation below the user puck/icon.
   * @since 0.1.0
   */
  public void setNavigationTextAnnotation(@NonNull String navigigationTextAnnotation) {
    this.navigigationTextAnnotation = navigigationTextAnnotation;
    Layer layer = mapboxMap.getLayer(LocationLayerConstants.NAVIGATION_ANNOTATION_LAYER);
    if (layer == null) {
      Drawable icon = ContextCompat.getDrawable(mapView.getContext(), R.drawable.mapbox_text_annotation_bg);
      mapboxMap.addImage(LocationLayerConstants.NAVIGATION_ANNOTATION_BACKGROUND_ICON, getBitmapFromDrawable(icon));
      layer = new SymbolLayer(
        LocationLayerConstants.NAVIGATION_ANNOTATION_LAYER, LocationLayerConstants.LOCATION_SOURCE
      ).withProperties(
        PropertyFactory.textSize(16f),
        PropertyFactory.textPadding(2f),
        PropertyFactory.textColor(Color.WHITE),
        PropertyFactory.textOffset(new Float[] {0f, 2.5f}),
        PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
        PropertyFactory.textField(textAnnotation),
        PropertyFactory.textMaxWidth(8f),
        PropertyFactory.iconTextFit(Property.ICON_TEXT_FIT_HEIGHT),
        PropertyFactory.iconTextFitPadding(new Float[] {8f, 15f, 8f, 15f}),
        PropertyFactory.iconImage(LocationLayerConstants.NAVIGATION_ANNOTATION_BACKGROUND_ICON)
      );
      mapboxMap.addLayer(layer);
    } else {
      layer.setProperties(
        PropertyFactory.textField(textAnnotation)
      );
    }
  }

  /**
   * Recieve the current set {@code String} being used for the navigation annotation. This is only shown when
   * {@link LocationLayerMode#NAVIGATION} mode is being used. It will return {@code null} if
   * {@link LocationLayerOptions#setNavigationTextAnnotation(String)} hasn't been called yet.
   *
   * @return String being used for the navigation annotation
   * @since 0.1.0
   */
  public String getNavigationTextAnnotation() {
    return navigigationTextAnnotation;
  }

  /**
   * When the user wants to disable location tracking, such as when {@link LocationLayerMode#NONE} is called, all the
   * layers and sources should be removed from the map.
   *
   * @since 0.1.0
   */
  void removeLayerAndSources() {
    for (Layer layer : mapboxMap.getLayers()) {
      if (layer.getId().contains("mapbox-location")) {
        mapboxMap.removeLayer(layer);
      }
    }

    for (Source source : mapboxMap.getSources()) {
      if (source.getId().contains("mapbox-location")) {
        mapboxMap.removeSource(source);
      }
    }
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