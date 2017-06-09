package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

final class LocationLayer implements MapView.OnMapChangedListener {

  private List<String> layerIds;

  private MapboxMap mapboxMap;
  private Context context;
  private boolean visible;
  private int styleRes;

  LocationLayer(MapView mapView, MapboxMap mapboxMap, @StyleRes int styleRes) {
    this.mapboxMap = mapboxMap;
    context = mapView.getContext();
    this.styleRes = styleRes;
    mapView.addOnMapChangedListener(this);
    initialize();
  }

  private void initialize() {
    layerIds = new ArrayList<>();

    addSources();

    TypedArray typedArray = context.obtainStyledAttributes(styleRes, R.styleable.LocationLayer);
    addForegroundLayer(typedArray.getDrawable(R.styleable.LocationLayer_foregroundDrawable));
    addBackgroundLayer(typedArray.getDrawable(R.styleable.LocationLayer_backgroundDrawable));
    addBearingLayer(typedArray.getDrawable(R.styleable.LocationLayer_bearingDrawable));
    addNavigationLayer(typedArray.getDrawable(R.styleable.LocationLayer_navigationDrawable));

    @FloatRange(from = 0, to = 1.0)
    float accuracyAlpha = typedArray.getFloat(R.styleable.LocationLayer_accuracyAlpha, 0.15f);
    @ColorInt
    int accuracyColor = typedArray.getColor(R.styleable.LocationLayer_accuracyColor,
      ContextCompat.getColor(context, R.color.mapbox_plugin_location_layer_blue));
    addAccuracyLayer(accuracyAlpha, accuracyColor);

    typedArray.recycle();
  }


  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_LOADING_STYLE && visible) {
      Source source = mapboxMap.getSource(LocationLayerConstants.LOCATION_SOURCE);
      if (source == null) {
        initialize();
      }
    }
  }

  void setLayerVisibility(boolean visible) {
    this.visible = visible;
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

  private void addSources() {
    // Create an empty feature collection
    FeatureCollection emptyFeature = FeatureCollection.fromFeatures(new Feature[] {});

    GeoJsonSource locationSource = new GeoJsonSource(LocationLayerConstants.LOCATION_SOURCE, emptyFeature);
    mapboxMap.addSource(locationSource);

    GeoJsonSource locationAccuracySource
      = new GeoJsonSource(LocationLayerConstants.LOCATION_ACCURACY_SOURCE, emptyFeature);
    mapboxMap.addSource(locationAccuracySource);
  }

  private void addLocationLayerToMap(Layer layer, @Nullable String idBelowLayer) {
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
    Layer navigationLayer = getLayer(
      LocationLayerConstants.LOCATION_NAVIGATION_LAYER,
      LocationLayerConstants.USER_LOCATION_PUCK_ICON,
      navigationDrawable
    );
    addLocationLayerToMap(navigationLayer, null);
  }

  private void addAccuracyLayer(float accuracyAlpha, @ColorInt int accuracyColor) {
    FillLayer locationAccuracyLayer = new FillLayer(
      LocationLayerConstants.LOCATION_ACCURACY_LAYER, LocationLayerConstants.LOCATION_ACCURACY_SOURCE
    ).withProperties(
      PropertyFactory.fillColor(accuracyColor),
      PropertyFactory.fillOpacity(accuracyAlpha)
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