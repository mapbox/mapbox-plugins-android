package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.ACCURACY_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BACKGROUND_STALE_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BEARING_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.BEARING_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.FOREGROUND_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.LOCATION_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.LOCATION_SOURCE;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.NAVIGATION_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PUCK_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.STALE_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getDrawable;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.generateShadow;
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

final class LocationLayer {

  private MapboxMap mapboxMap;
  private Context context;
  private float elevation;

  private final Map<String, Layer> layerMap = new HashMap<>();
  private final Map<String, GeoJsonSource> sourceMap = new HashMap<>();

  LocationLayer(MapView mapView, MapboxMap mapboxMap, LocationLayerOptions options) {
    this.context = mapView.getContext();
    this.mapboxMap = mapboxMap;
    addSources();
    addLayers();
    applyStyle(options);
  }

  private void addLayers() {
    addSymbolLayerToMap(SHADOW_LAYER, BACKGROUND_LAYER);
    addSymbolLayerToMap(BACKGROUND_LAYER, FOREGROUND_LAYER);
    addSymbolLayerToMap(FOREGROUND_LAYER, null);
    addSymbolLayerToMap(LocationLayerConstants.BEARING_LAYER, null);
    addNavigationLayer();
    addAccuracyLayer();
  }

  private void addSources() {
    addSource(LOCATION_SOURCE);
  }

  void applyStyle(@NonNull LocationLayerOptions options) {

    elevation = options.elevation();
    styleShadow(ContextCompat.getDrawable(context, R.drawable.mapbox_user_icon_shadow));

    styleForeground(
      getDrawable(context, options.foregroundDrawable(), options.foregroundTintColor()));
    styleBackground(
      getDrawable(context, options.backgroundDrawable(), options.backgroundTintColor()));
    styleForegroundStale(
      getDrawable(context, options.foregroundDrawableStale(), options.foregroundStaleTintColor()));
    styleBackgroundStale(
      getDrawable(context, options.backgroundDrawableStale(), options.backgroundStaleTintColor()));
    styleBearing(
      getDrawable(context, options.bearingDrawable(), options.bearingTintColor()));
    styleNavigation(
      ContextCompat.getDrawable(context, options.navigationDrawable()));
    styleAccuracy(options.accuracyAlpha(), options.accuracyColor());
  }

  //
  // Layer action
  //

  void setLayersVisibility(boolean visible) {
    layerMap.get(SHADOW_LAYER).setProperties(visibility(visible ? VISIBLE : NONE));
    layerMap.get(FOREGROUND_LAYER).setProperties(visibility(visible ? VISIBLE : NONE));
    layerMap.get(BACKGROUND_LAYER).setProperties(visibility(visible ? VISIBLE : NONE));
    layerMap.get(BEARING_LAYER).setProperties(visibility(visible ? VISIBLE : NONE));
    layerMap.get(ACCURACY_LAYER).setProperties(visibility(visible ? VISIBLE : NONE));
    layerMap.get(NAVIGATION_LAYER).setProperties(visibility(visible ? VISIBLE : NONE));
  }

  void setLayerVisibility(String layerId, boolean visible) {
    layerMap.get(layerId).setProperties(visibility(visible ? VISIBLE : NONE));
  }

  void setLayerBearing(String layerId, float bearing) {
    layerMap.get(layerId).setProperties(iconRotate(bearing));
  }

  private void addLayerToMap(Layer layer, @Nullable String idBelowLayer) {
    if (idBelowLayer == null) {
      mapboxMap.addLayer(layer);
    } else {
      mapboxMap.addLayerBelow(layer, idBelowLayer);
    }
    layerMap.put(layer.getId(), layer);
  }

  private void addNavigationLayer() {
    SymbolLayer navigationLayer = new SymbolLayer(NAVIGATION_LAYER, LOCATION_SOURCE).withProperties(
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

    addLayerToMap(navigationLayer, null);
  }

  private void addAccuracyLayer() {
    CircleLayer locationAccuracyLayer = new CircleLayer(ACCURACY_LAYER, LOCATION_SOURCE)
      .withProperties(circleRadius(0f));
    addLayerToMap(locationAccuracyLayer, BACKGROUND_LAYER);
  }

  void updateAccuracyRadius(Location location) {
    CircleLayer accuracyLayer = (CircleLayer) mapboxMap.getLayer(ACCURACY_LAYER);
    if (accuracyLayer != null && accuracyLayer.getVisibility().isValue()) {
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

  void setLocationPoint(Point locationPoint) {
    sourceMap.get(LOCATION_SOURCE).setGeoJson(locationPoint);
  }

  private void addSource(String sourceId) {
    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});
    GeoJsonSource locationSource = new GeoJsonSource(
      sourceId,
      emptyFeature,
      new GeoJsonOptions().withMaxZoom(16));
    mapboxMap.addSource(locationSource);
    sourceMap.put(sourceId, locationSource);
  }

  private void addSymbolLayerToMap(String layerId, String beforeLayerId) {
    SymbolLayer layer = new SymbolLayer(layerId, LOCATION_SOURCE);
    layer.setProperties(
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconRotationAlignment(ICON_ROTATION_ALIGNMENT_MAP),
      iconSize(zoom(
        exponential(
          stop(16f, iconSize(1.2f)),
          stop(0f, iconSize(1f))
        )
      ))
    );
    addLayerToMap(layer, beforeLayerId);
  }

  //
  // Styling
  //

  private void styleBackground(Drawable backgroundDrawable) {
    mapboxMap.addImage(BACKGROUND_ICON, getBitmapFromDrawable(backgroundDrawable));
    layerMap.get(BACKGROUND_LAYER).setProperties(
      iconImage(BACKGROUND_ICON));
  }

  private void styleShadow(Drawable shadowDrawable) {
    mapboxMap.addImage(SHADOW_ICON, generateShadow(shadowDrawable, elevation));
    layerMap.get(SHADOW_LAYER).setProperties(
      iconImage(SHADOW_ICON));
  }

  private void styleForeground(Drawable foregroundDrawable) {
    mapboxMap.addImage(LOCATION_ICON, getBitmapFromDrawable(foregroundDrawable));
    layerMap.get(FOREGROUND_LAYER).setProperties(
      iconImage(LOCATION_ICON),
      iconRotate(90f));
  }

  private void styleNavigation(Drawable navigationDrawable) {
    mapboxMap.addImage(PUCK_ICON, getBitmapFromDrawable(navigationDrawable));
    layerMap.get(NAVIGATION_LAYER).setProperties(iconImage(PUCK_ICON));
  }

  private void styleBearing(Drawable bearingDrawable) {
    mapboxMap.addImage(BEARING_ICON, getBitmapFromDrawable(bearingDrawable));
    layerMap.get(BEARING_LAYER).setProperties(
      iconImage(BEARING_ICON));
  }

  private void styleForegroundStale(Drawable foregroundDrawableStale) {
    mapboxMap.addImage(STALE_ICON, getBitmapFromDrawable(foregroundDrawableStale));
  }

  private void styleBackgroundStale(Drawable backgroundDrawableStale) {
    mapboxMap.addImage(BACKGROUND_STALE_ICON,
      getBitmapFromDrawable(backgroundDrawableStale));
  }

  private void styleAccuracy(float accuracyAlpha, @ColorInt int accuracyColor) {
    layerMap.get(ACCURACY_LAYER).setProperties(
      circleColor(accuracyColor),
      circleOpacity(accuracyAlpha),
      circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP),
      circleStrokeColor(accuracyColor)
    );
  }

  void updateForegroundOffset(double tilt) {
    layerMap.get(FOREGROUND_LAYER).setProperties(
      iconOffset(new Float[] {0f, (float) (-0.05 * tilt)}));
    layerMap.get(SHADOW_LAYER).setProperties(
      iconOffset(new Float[] {0f, (float) (0.05 * tilt)}));
  }

  void updateForegroundBearing(float bearing) {
    layerMap.get(FOREGROUND_LAYER).setProperties(iconRotate(bearing));
    layerMap.get(SHADOW_LAYER).setProperties(iconRotate(bearing));
  }

  void locationsStale(boolean stale) {
    layerMap.get(FOREGROUND_LAYER).setProperties(iconImage(stale ? STALE_ICON : LOCATION_ICON));
    layerMap.get(BACKGROUND_LAYER).setProperties(iconImage(stale ? BACKGROUND_STALE_ICON : BACKGROUND_ICON));
    layerMap.get(ACCURACY_LAYER).setProperties(visibility(stale ? NONE : VISIBLE));
  }

  //
  // Map click event
  //

  boolean onMapClick(LatLng point) {
    PointF screenLoc = mapboxMap.getProjection().toScreenLocation(point);
    List<Feature> features = mapboxMap.queryRenderedFeatures(screenLoc,
      BACKGROUND_LAYER,
      FOREGROUND_LAYER,
      BEARING_LAYER,
      NAVIGATION_LAYER
    );
    return !features.isEmpty();
  }
}