package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.ACCURACY_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_STALE_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BEARING_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BEARING_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.FOREGROUND_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.FOREGROUND_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.FOREGROUND_STALE_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.LOCATION_SOURCE;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.generateShadow;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getDrawable;
import static com.mapbox.mapboxsdk.style.functions.Function.zoom;
import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.exponential;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_MAP;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circlePitchAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleStrokeColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotationAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

final class LocationLayer implements LocationLayerAnimator.OnLayerAnimationsValuesChangeListener {

  @RenderMode.Mode
  private int renderMode;
  private boolean isStale;

  private final MapboxMap mapboxMap;
  private LocationLayerOptions options;
  private Context context;

  private final Map<String, Layer> layerMap = new HashMap<>();
  private final Map<String, GeoJsonSource> sourceMap = new HashMap<>();

  LocationLayer(MapView mapView, MapboxMap mapboxMap, LocationLayerOptions options) {
    this.mapboxMap = mapboxMap;
    this.context = mapView.getContext();
    initializeComponents(options);
    setRenderMode(RenderMode.NORMAL);
  }

  void initializeComponents(LocationLayerOptions options) {
    addLocationSource();
    addLayers();
    applyStyle(options);
  }

  void applyStyle(@NonNull LocationLayerOptions options) {
    this.options = options;
    styleForeground(options, isStale);
    styleBackground(
      getDrawable(context, options.backgroundDrawable(), options.backgroundTintColor()),
      getDrawable(context, options.backgroundDrawableStale(), options.backgroundStaleTintColor()),
      isStale);
    styleBearing(
      getDrawable(context, options.bearingDrawable(), options.bearingTintColor()));
    styleAccuracy(options.accuracyAlpha(), options.accuracyColor());
    styleShadow(ContextCompat.getDrawable(context, R.drawable.mapbox_user_icon_shadow));
  }

  void setRenderMode(@RenderMode.Mode int renderMode) {
    this.renderMode = renderMode;
    hide();

    switch (renderMode) {
      case RenderMode.NORMAL:
        styleForeground(options, isStale);
        setLayerVisibility(FOREGROUND_LAYER, true);
        setLayerVisibility(BACKGROUND_LAYER, true);
        setLayerVisibility(ACCURACY_LAYER, true);
        break;
      case RenderMode.COMPASS:
        styleForeground(options, isStale);
        setLayerVisibility(FOREGROUND_LAYER, true);
        setLayerVisibility(BACKGROUND_LAYER, true);
        setLayerVisibility(ACCURACY_LAYER, true);
        setLayerVisibility(BEARING_LAYER, true);
        break;
      case RenderMode.GPS:
        styleForeground(options, isStale);
        setLayerVisibility(FOREGROUND_LAYER, true);
        setLayerVisibility(BACKGROUND_LAYER, true);
        break;
      default:
        break;
    }
  }

  int getRenderMode() {
    return renderMode;
  }

  //
  // Layer action
  //

  void show() {
    setRenderMode(renderMode);
  }

  void hide() {
    for (Layer layer : layerMap.values()) {
      layer.setProperties(visibility(NONE));
    }
  }

  void setLayerVisibility(String layerId, boolean visible) {
    layerMap.get(layerId).setProperties(visibility(visible ? VISIBLE : NONE));
  }

  private void addLayers() {
    addSymbolLayer(SHADOW_LAYER, BACKGROUND_LAYER);
    addSymbolLayer(BACKGROUND_LAYER, FOREGROUND_LAYER);
    addSymbolLayer(FOREGROUND_LAYER, null);
    addSymbolLayer(LocationLayerConstants.BEARING_LAYER, null);
    addAccuracyLayer();
  }

  private void addSymbolLayer(String layerId, String beforeLayerId) {
    SymbolLayer layer = new SymbolLayer(layerId, LOCATION_SOURCE);
    layer.setProperties(
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconSize(zoom(
        exponential(
          stop(22f, iconSize(1f)),
          stop(12f, iconSize(1f)),
          stop(10f, iconSize(0.6f)),
          stop(0f, iconSize(0.6f))
        ).withBase(1f)
      )),
      iconRotationAlignment(ICON_ROTATION_ALIGNMENT_MAP));
    addLayerToMap(layer, beforeLayerId);
  }

  private void addAccuracyLayer() {
    CircleLayer locationAccuracyLayer = new CircleLayer(ACCURACY_LAYER, LOCATION_SOURCE)
      .withProperties(circleRadius(0f));
    addLayerToMap(locationAccuracyLayer, BACKGROUND_LAYER);
  }

  private void addLayerToMap(Layer layer, @Nullable String idBelowLayer) {
    if (idBelowLayer == null) {
      mapboxMap.addLayer(layer);
    } else {
      mapboxMap.addLayerBelow(layer, idBelowLayer);
    }
    layerMap.put(layer.getId(), layer);
  }

  void setLayerBearing(String layerId, float bearing) {
    layerMap.get(layerId).setProperties(iconRotate(bearing));
  }

  void updateAccuracyRadius(Location location) {
    CircleLayer accuracyLayer = (CircleLayer) layerMap.get(ACCURACY_LAYER);
    if (accuracyLayer != null && (renderMode == RenderMode.COMPASS || renderMode == RenderMode.NORMAL)) {
      accuracyLayer.setProperties(
        circleRadius(calculateZoomLevelRadius(location))
      );
    }
  }

  private float calculateZoomLevelRadius(Location location) {
    if (location == null) {
      return 0;
    }
    double metersPerPixel = mapboxMap.getProjection().getMetersPerPixelAtLatitude(
      location.getLatitude());
    return (float) (location.getAccuracy() * (1 / metersPerPixel));
  }

  //
  // Source actions
  //

  private void addLocationSource() {
    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});
    GeoJsonSource locationSource = new GeoJsonSource(
      LOCATION_SOURCE,
      emptyFeature,
      new GeoJsonOptions().withMaxZoom(16));
    mapboxMap.addSource(locationSource);
    sourceMap.put(LOCATION_SOURCE, locationSource);
  }

  private void setLocationPoint(Point locationPoint) {
    sourceMap.get(LOCATION_SOURCE).setGeoJson(locationPoint);
  }

  //
  // Styling
  //

  private void styleBackground(Drawable backgroundDrawable, Drawable backgroundDrawableStale, boolean isStale) {
    mapboxMap.addImage(BACKGROUND_ICON, getBitmapFromDrawable(backgroundDrawable));
    mapboxMap.addImage(BACKGROUND_STALE_ICON, getBitmapFromDrawable(backgroundDrawableStale));
    layerMap.get(BACKGROUND_LAYER).setProperties(
      iconImage(isStale ? BACKGROUND_STALE_ICON : BACKGROUND_ICON));
  }

  private void styleShadow(Drawable shadowDrawable) {
    mapboxMap.addImage(SHADOW_ICON, generateShadow(shadowDrawable, options.elevation()));
    layerMap.get(SHADOW_LAYER).setProperties(iconImage(SHADOW_ICON));
  }

  private void styleBearing(Drawable bearingDrawable) {
    mapboxMap.addImage(BEARING_ICON, getBitmapFromDrawable(bearingDrawable));
    layerMap.get(BEARING_LAYER).setProperties(
      iconImage(BEARING_ICON));
  }

  private void styleAccuracy(float accuracyAlpha, @ColorInt int accuracyColor) {
    layerMap.get(ACCURACY_LAYER).setProperties(
      circleColor(accuracyColor),
      circleOpacity(accuracyAlpha),
      circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP),
      circleStrokeColor(accuracyColor)
    );
  }

  private void styleForeground(Drawable foregroundDrawable, Drawable foregroundDrawableStale, boolean isStale) {
    mapboxMap.addImage(FOREGROUND_ICON, getBitmapFromDrawable(foregroundDrawable));
    mapboxMap.addImage(FOREGROUND_STALE_ICON, getBitmapFromDrawable(foregroundDrawableStale));

    layerMap.get(FOREGROUND_LAYER).setProperties(
      iconImage(isStale
        ? FOREGROUND_STALE_ICON : FOREGROUND_ICON),
      iconRotate(90f));
  }

  private void styleForeground(@NonNull LocationLayerOptions options, boolean isStale) {
    if (renderMode == RenderMode.GPS) {
      styleForegroundGPS(isStale);
    } else {
      styleForeground(
        getDrawable(context, options.foregroundDrawable(), options.foregroundTintColor()),
        getDrawable(context, options.foregroundDrawableStale(), options.foregroundStaleTintColor()),
        isStale);
    }
  }

  private void styleForegroundGPS(boolean isStale) {
    styleForeground(
      getDrawable(context, options.gpsDrawable(), options.foregroundTintColor()),
      getDrawable(context, options.gpsDrawable(), options.foregroundStaleTintColor()),
      isStale);
  }

  void updateForegroundOffset(double tilt) {
    layerMap.get(FOREGROUND_LAYER).setProperties(
      iconOffset(new Float[] {0f, (float) (-0.05 * tilt)}));
    layerMap.get(SHADOW_LAYER).setProperties(
      iconOffset(new Float[] {0f, (float) (0.05 * tilt)}));
  }

  void setLocationsStale(boolean isStale) {
    this.isStale = isStale;
    layerMap.get(FOREGROUND_LAYER).setProperties(iconImage(isStale ? FOREGROUND_STALE_ICON : FOREGROUND_ICON));
    layerMap.get(BACKGROUND_LAYER).setProperties(iconImage(isStale ? BACKGROUND_STALE_ICON : BACKGROUND_ICON));
    layerMap.get(ACCURACY_LAYER).setProperties(visibility(isStale && renderMode != RenderMode.GPS ? NONE : VISIBLE));
  }

  //
  // Map click event
  //

  boolean onMapClick(LatLng point) {
    PointF screenLoc = mapboxMap.getProjection().toScreenLocation(point);
    List<Feature> features = mapboxMap.queryRenderedFeatures(screenLoc,
      BACKGROUND_LAYER,
      FOREGROUND_LAYER,
      BEARING_LAYER
    );
    return !features.isEmpty();
  }

  @Override
  public void onNewLatLngValue(LatLng latLng) {
    Point point = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
    setLocationPoint(point);
  }

  @Override
  public void onNewGpsBearingValue(float gpsBearing) {
    if (renderMode == RenderMode.GPS) {
      setLayerBearing(LocationLayerConstants.FOREGROUND_LAYER, gpsBearing);
      setLayerBearing(LocationLayerConstants.BACKGROUND_LAYER, gpsBearing);
    }
  }

  @Override
  public void onNewCompassBearingValue(float compassBearing) {
    if (renderMode == RenderMode.COMPASS) {
      setLayerBearing(LocationLayerConstants.BEARING_LAYER, compassBearing);
    }
  }
}