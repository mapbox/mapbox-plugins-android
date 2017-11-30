package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stop;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

final class LocationLayer {

  private List<String> layerIds;

  private MapboxMap mapboxMap;
  private Context context;
  private int styleRes;

  LocationLayer(MapView mapView, MapboxMap mapboxMap, @StyleRes int styleRes) {
    this.mapboxMap = mapboxMap;
    context = mapView.getContext();
    this.styleRes = styleRes;
    initialize();
  }

  private void initialize() {
    layerIds = new ArrayList<>();

    addSources();

    TypedArray typedArray = context.obtainStyledAttributes(styleRes, R.styleable.LocationLayer);

    int drawableResId = typedArray.getResourceId(R.styleable.LocationLayer_foregroundDrawable, -1);
    addForegroundLayer(ContextCompat.getDrawable(context, drawableResId));
    drawableResId = typedArray.getResourceId(R.styleable.LocationLayer_backgroundDrawable, -1);
    addBackgroundLayer(ContextCompat.getDrawable(context, drawableResId));
    drawableResId = typedArray.getResourceId(R.styleable.LocationLayer_bearingDrawable, -1);
    addBearingLayer(ContextCompat.getDrawable(context, drawableResId));
    drawableResId = typedArray.getResourceId(R.styleable.LocationLayer_navigationDrawable, -1);
    addNavigationLayer(ContextCompat.getDrawable(context, drawableResId));
    float accuracyAlpha = typedArray.getFloat(R.styleable.LocationLayer_accuracyAlpha, 0.15f);
    if (accuracyAlpha < 0 || accuracyAlpha > 1) {
      throw new UnsupportedOperationException(
        "Location layer accuracy alpha value must be between 0.0 and 1.0."
      );
    }
    int accuracyColor = typedArray.getColor(R.styleable.LocationLayer_accuracyColor,
      ContextCompat.getColor(context, R.color.mapbox_plugin_location_layer_blue));
    addAccuracyLayer(accuracyAlpha, accuracyColor);

    typedArray.recycle();
  }

  void setLayerVisibility(boolean visible) {
    List<Layer> layers = mapboxMap.getLayers();
    String id;
    for (Layer layer : layers) {
      id = layer.getId();
      if (layerIds.contains(layer.getId())) {
        // TODO use sourceLayer filter instead
        if (id.equals(LocationLayerConstants.LOCATION_ACCURACY_LAYER)
          || id.equals(LocationLayerConstants.LOCATION_BEARING_LAYER)
          || id.equals(LocationLayerConstants.LOCATION_LAYER)
          || id.equals(LocationLayerConstants.LOCATION_BACKGROUND_LAYER)
          || id.equals(LocationLayerConstants.LOCATION_NAVIGATION_LAYER)) {
          layer.setProperties(visibility(visible ? Property.VISIBLE : Property.NONE));
        }
      }
    }
  }

  void setCompassBearing(float compassBearing) {
    Layer bearingLayer = mapboxMap.getLayer(LocationLayerConstants.LOCATION_BEARING_LAYER);
    if (bearingLayer != null) {
      bearingLayer.setProperties(PropertyFactory.iconRotate(compassBearing));
    }
  }

  private void addSources() {
    // Create an empty feature collection
    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});

    if (mapboxMap.getSourceAs(LocationLayerConstants.LOCATION_SOURCE) == null) {
      mapboxMap.addSource(new GeoJsonSource(
          LocationLayerConstants.LOCATION_SOURCE,
          emptyFeature,
          new GeoJsonOptions().withMaxZoom(16))
      );
    }
  }

  private void addLocationLayerToMap(Layer layer, @Nullable String idBelowLayer) {
    if (mapboxMap.getLayerAs(layer.getId()) != null) {
      return;
    }

    if (idBelowLayer == null) {
      mapboxMap.addLayer(layer);
    } else {
      mapboxMap.addLayerBelow(layer, idBelowLayer);
    }
    layerIds.add(layer.getId());
  }

  private void addBackgroundLayer(Drawable backgroundDrawable) {
    Layer backgroundLayer = getLayer(
      LocationLayerConstants.LOCATION_BACKGROUND_LAYER,
      LocationLayerConstants.USER_LOCATION_BACKGROUND_ICON,
      backgroundDrawable
    );
    addLocationLayerToMap(backgroundLayer, LocationLayerConstants.LOCATION_LAYER);
  }

  private void addForegroundLayer(Drawable foregroundDrawable) {
    Layer foregroundLayer = getLayer(
      LocationLayerConstants.LOCATION_LAYER,
      LocationLayerConstants.USER_LOCATION_ICON,
      foregroundDrawable
    );
    addLocationLayerToMap(foregroundLayer, null);
  }

  private void addBearingLayer(Drawable bearingDrawable) {
    Layer foregroundLayer = getLayer(
      LocationLayerConstants.LOCATION_BEARING_LAYER,
      LocationLayerConstants.USER_LOCATION_BEARING_ICON,
      bearingDrawable
    );
    addLocationLayerToMap(foregroundLayer, null);
  }

  private void addNavigationLayer(Drawable navigationDrawable) {
    Bitmap bitmap = getBitmapFromDrawable(navigationDrawable);
    mapboxMap.addImage(LocationLayerConstants.USER_LOCATION_PUCK_ICON, bitmap);
    SymbolLayer navigationLayer = new SymbolLayer(
      LocationLayerConstants.LOCATION_NAVIGATION_LAYER, LocationLayerConstants.LOCATION_SOURCE)
      .withProperties(
      PropertyFactory.iconImage(LocationLayerConstants.USER_LOCATION_PUCK_ICON),
      PropertyFactory.iconAllowOverlap(true),
      PropertyFactory.iconIgnorePlacement(true),
      PropertyFactory.iconSize(Function.zoom(
        Stops.exponential(
          Stop.stop(22f, PropertyFactory.iconSize(1f)),
          Stop.stop(12f, PropertyFactory.iconSize(1f)),
          Stop.stop(10f, PropertyFactory.iconSize(0.6f)),
          Stop.stop(0f, PropertyFactory.iconSize(0.6f))
        ).withBase(1f)
      )),
      PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));

    addLocationLayerToMap(navigationLayer, null);
  }

  private void addAccuracyLayer(float accuracyAlpha, @ColorInt int accuracyColor) {
    CircleLayer locationAccuracyLayer = new CircleLayer(
      LocationLayerConstants.LOCATION_ACCURACY_LAYER, LocationLayerConstants.LOCATION_SOURCE
    ).withProperties(
      PropertyFactory.circleColor(accuracyColor),
      PropertyFactory.circleOpacity(accuracyAlpha),
      PropertyFactory.circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP),
      PropertyFactory.circleStrokeWidth(0.5f),
      PropertyFactory.circleStrokeColor(accuracyColor)
    );
    addLocationLayerToMap(locationAccuracyLayer, LocationLayerConstants.LOCATION_BACKGROUND_LAYER);
  }

  private Layer getLayer(String layerId, String image, Drawable drawable) {
    Bitmap bitmap = getBitmapFromDrawable(drawable);
    mapboxMap.addImage(image, bitmap);
    return new SymbolLayer(
      layerId, LocationLayerConstants.LOCATION_SOURCE).withProperties(
      PropertyFactory.iconImage(image),
      PropertyFactory.iconAllowOverlap(true),
      PropertyFactory.iconIgnorePlacement(true),
      PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
  }
}