package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
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
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_ACCURACY_ALPHA;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_ACCURACY_COLOR;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_ACCURACY_RADIUS;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_COMPASS_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_FOREGROUND_ICON_OFFSET;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_GPS_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_LOCATION_STALE;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_SHADOW_ICON_OFFSET;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.generateShadow;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getDrawable;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.switchCase;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
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

  private final MapboxMap mapboxMap;
  private LocationLayerOptions options;
  private Context context;

  private final Map<String, Layer> layerMap = new HashMap<>();
  @SuppressLint("Range")
  private Feature locationFeature = Feature.fromGeometry(
    Point.fromLngLat(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
  );
  private GeoJsonSource locationSource;
  private boolean isSourceInitialized;

  LocationLayer(MapView mapView, MapboxMap mapboxMap, LocationLayerOptions options) {
    this.mapboxMap = mapboxMap;
    this.context = mapView.getContext();
    initializeComponents(options);
    setRenderMode(RenderMode.NORMAL);
  }

  void initializeComponents(LocationLayerOptions options) {
    prepareLocationSource();
    addLocationSource();
    addLayers();
    applyStyle(options);
  }

  void applyStyle(@NonNull LocationLayerOptions options) {
    this.options = options;

    float elevation = options.elevation();
    // Only add icon elevation if the values greater than 0.
    if (elevation > 0) {
      styleShadow(options);
    }
    styleForeground(options);
    styleBackground(options);
    styleBearing(options);
    styleAccuracy(options.accuracyAlpha(), options.accuracyColor());
  }

  void setRenderMode(@RenderMode.Mode int renderMode) {
    this.renderMode = renderMode;
    boolean isStale = locationFeature.getBooleanProperty(PROPERTY_LOCATION_STALE);

    switch (renderMode) {
      case RenderMode.NORMAL:
        styleForeground(options);
        setLayerVisibility(SHADOW_LAYER, true);
        setLayerVisibility(FOREGROUND_LAYER, true);
        setLayerVisibility(BACKGROUND_LAYER, true);
        setLayerVisibility(ACCURACY_LAYER, !isStale);
        setLayerVisibility(BEARING_LAYER, false);
        break;
      case RenderMode.COMPASS:
        styleForeground(options);
        setLayerVisibility(SHADOW_LAYER, true);
        setLayerVisibility(FOREGROUND_LAYER, true);
        setLayerVisibility(BACKGROUND_LAYER, true);
        setLayerVisibility(ACCURACY_LAYER, !isStale);
        setLayerVisibility(BEARING_LAYER, true);
        break;
      case RenderMode.GPS:
        styleForeground(options);
        setLayerVisibility(SHADOW_LAYER, false);
        setLayerVisibility(FOREGROUND_LAYER, true);
        setLayerVisibility(BACKGROUND_LAYER, true);
        setLayerVisibility(ACCURACY_LAYER, false);
        setLayerVisibility(BEARING_LAYER, false);
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
    for (String layerId : layerMap.keySet()) {
      setLayerVisibility(layerId, false);
    }
  }

  private void setLayerVisibility(String layerId, boolean visible) {
    Layer layer = layerMap.get(layerId);
    String targetVisibility = visible ? VISIBLE : NONE;
    if (!layer.getVisibility().value.equals(targetVisibility)) {
      layer.setProperties(visibility(visible ? VISIBLE : NONE));
    }
  }

  private void addLayers() {
    addSymbolLayer(SHADOW_LAYER, BACKGROUND_LAYER);
    addSymbolLayer(BACKGROUND_LAYER, FOREGROUND_LAYER);
    addSymbolLayer(FOREGROUND_LAYER, null);
    addSymbolLayer(BEARING_LAYER, null);
    addAccuracyLayer();
  }

  private void addSymbolLayer(String layerId, String beforeLayerId) {
    SymbolLayer layer = new SymbolLayer(layerId, LOCATION_SOURCE);
    layer.setProperties(
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconSize(
        interpolate(exponential(1f), zoom(),
          stop(0f, 0.6f),
          stop(18f, 1.2f)
        )
      ),
      iconRotationAlignment(ICON_ROTATION_ALIGNMENT_MAP),
      iconRotate(
        match(literal(layerId), literal(0f),
          stop(FOREGROUND_LAYER, get(PROPERTY_GPS_BEARING)),
          stop(BACKGROUND_LAYER, get(PROPERTY_GPS_BEARING)),
          stop(SHADOW_LAYER, get(PROPERTY_GPS_BEARING)),
          stop(BEARING_LAYER, get(PROPERTY_COMPASS_BEARING))
        )
      ),
      iconImage(
        match(literal(layerId), literal(""),
          stop(FOREGROUND_LAYER, switchCase(
            get(PROPERTY_LOCATION_STALE), literal(FOREGROUND_STALE_ICON),
            literal(FOREGROUND_ICON))),
          stop(BACKGROUND_LAYER, switchCase(
            get(PROPERTY_LOCATION_STALE), literal(BACKGROUND_STALE_ICON),
            literal(BACKGROUND_ICON))),
          stop(SHADOW_LAYER, literal(SHADOW_ICON)),
          stop(BEARING_LAYER, literal(BEARING_ICON))
        )
      ),
      iconOffset(
        match(literal(layerId), literal(new Float[] {0f, 0f}),
          stop(literal(FOREGROUND_LAYER), get(PROPERTY_FOREGROUND_ICON_OFFSET)),
          stop(literal(SHADOW_LAYER), get(PROPERTY_SHADOW_ICON_OFFSET))
        )
      )
    );
    addLayerToMap(layer, beforeLayerId);
  }

  private void addAccuracyLayer() {
    CircleLayer locationAccuracyLayer = new CircleLayer(ACCURACY_LAYER, LOCATION_SOURCE)
      .withProperties(
        circleRadius(get(PROPERTY_ACCURACY_RADIUS)),
        circleColor(get(PROPERTY_ACCURACY_COLOR)),
        circleOpacity(get(PROPERTY_ACCURACY_ALPHA)),
        circleStrokeColor(get(PROPERTY_ACCURACY_COLOR)),
        circlePitchAlignment(Property.CIRCLE_PITCH_ALIGNMENT_MAP)
      );
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

  private void setBearingProperty(String propertyId, float bearing) {
    locationFeature.addNumberProperty(propertyId, bearing);
    refreshSource();
  }

  void updateAccuracyRadius(Location location) {
    if (renderMode == RenderMode.COMPASS || renderMode == RenderMode.NORMAL) {
      locationFeature.addNumberProperty(PROPERTY_ACCURACY_RADIUS, calculateZoomLevelRadius(location));
      refreshSource();
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

  void updateForegroundOffset(double tilt) {
    JsonArray foregroundJsonArray = new JsonArray();
    foregroundJsonArray.add(0f);
    foregroundJsonArray.add((float) (-0.05 * tilt));
    locationFeature.addProperty(PROPERTY_FOREGROUND_ICON_OFFSET, foregroundJsonArray);

    JsonArray backgroundJsonArray = new JsonArray();
    backgroundJsonArray.add(0f);
    backgroundJsonArray.add((float) (0.05 * tilt));
    locationFeature.addProperty(PROPERTY_SHADOW_ICON_OFFSET, backgroundJsonArray);

    refreshSource();
  }

  void updateForegroundBearing(float bearing) {
    if (renderMode != RenderMode.GPS) {
      setBearingProperty(PROPERTY_GPS_BEARING, bearing);
    }
  }

  //
  // Source actions
  //

  private void prepareLocationSource() {
    locationFeature.addNumberProperty(PROPERTY_GPS_BEARING, 0f);
    locationFeature.addNumberProperty(PROPERTY_COMPASS_BEARING, 0f);
    locationFeature.addBooleanProperty(PROPERTY_LOCATION_STALE, false);

    locationSource = new GeoJsonSource(
      LOCATION_SOURCE,
      locationFeature,
      new GeoJsonOptions().withMaxZoom(16)
    );
  }

  private void addLocationSource() {
    mapboxMap.addSource(locationSource);
    isSourceInitialized = true;
  }

  private void refreshSource() {
    locationSource.setGeoJson(locationFeature);
  }

  private void setLocationPoint(Point locationPoint) {
    JsonObject properties = locationFeature.properties();
    if (properties != null) {
      locationFeature = Feature.fromGeometry(locationPoint, properties);
      locationSource.setGeoJson(locationFeature);
    }

    if (!isSourceInitialized) {
      addLocationSource();
    }
  }

  //
  // Styling
  //

  private void styleBackground(LocationLayerOptions options) {
    Drawable backgroundDrawable =
      getDrawable(context, options.backgroundDrawable(), options.backgroundTintColor());
    Drawable backgroundDrawableStale =
      getDrawable(context, options.backgroundDrawableStale(), options.backgroundStaleTintColor());

    mapboxMap.addImage(BACKGROUND_ICON, getBitmapFromDrawable(backgroundDrawable));
    mapboxMap.addImage(BACKGROUND_STALE_ICON, getBitmapFromDrawable(backgroundDrawableStale));
  }

  private void styleShadow(LocationLayerOptions options) {
    Drawable shadowDrawable = ContextCompat.getDrawable(context, R.drawable.mapbox_user_icon_shadow);
    mapboxMap.addImage(SHADOW_ICON, generateShadow(shadowDrawable, options.elevation()));
  }

  private void styleBearing(LocationLayerOptions options) {
    Drawable bearingDrawable = getDrawable(context, options.bearingDrawable(), options.bearingTintColor());
    mapboxMap.addImage(BEARING_ICON, getBitmapFromDrawable(bearingDrawable));
  }

  private void styleAccuracy(float accuracyAlpha, @ColorInt int accuracyColor) {
    locationFeature.addNumberProperty(PROPERTY_ACCURACY_ALPHA, accuracyAlpha);
    locationFeature.addNumberProperty(PROPERTY_ACCURACY_COLOR, accuracyColor);
    refreshSource();
  }

  private void styleForeground(Drawable foregroundDrawable, Drawable foregroundDrawableStale) {
    mapboxMap.addImage(FOREGROUND_ICON, getBitmapFromDrawable(foregroundDrawable));
    mapboxMap.addImage(FOREGROUND_STALE_ICON, getBitmapFromDrawable(foregroundDrawableStale));
  }

  private void styleForeground(@NonNull LocationLayerOptions options) {
    if (renderMode == RenderMode.GPS) {
      styleForegroundGPS(options);
    } else {
      styleForeground(
        getDrawable(context, options.foregroundDrawable(), options.foregroundTintColor()),
        getDrawable(context, options.foregroundDrawableStale(), options.foregroundStaleTintColor()));
    }
  }

  private void styleForegroundGPS(LocationLayerOptions options) {
    styleForeground(
      getDrawable(context, options.gpsDrawable(), options.foregroundTintColor()),
      getDrawable(context, options.gpsDrawable(), options.foregroundStaleTintColor()));
  }

  void setLocationsStale(boolean isStale) {
    // If options has stale state disabled, just return here.
    if (!options.enableStaleState()) {
      return;
    }
    locationFeature.addBooleanProperty(PROPERTY_LOCATION_STALE, isStale);
    refreshSource();
    if (renderMode != RenderMode.GPS) {
      setLayerVisibility(ACCURACY_LAYER, !isStale);
    }
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
      setBearingProperty(PROPERTY_GPS_BEARING, gpsBearing);
    }
  }

  @Override
  public void onNewCompassBearingValue(float compassBearing) {
    if (renderMode == RenderMode.COMPASS) {
      setBearingProperty(PROPERTY_COMPASS_BEARING, compassBearing);
    }
  }
}