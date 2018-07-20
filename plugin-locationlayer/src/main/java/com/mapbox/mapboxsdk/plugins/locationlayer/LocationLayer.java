package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;

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
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_BACKGROUND_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_BACKGROUND_STALE_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_BEARING_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_COMPASS_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_FOREGROUND_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_FOREGROUND_ICON_OFFSET;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_FOREGROUND_STALE_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_GPS_BEARING;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_LOCATION_STALE;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.PROPERTY_SHADOW_ICON_OFFSET;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_ICON;
import static com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.SHADOW_LAYER;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.generateShadow;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.plugins.locationlayer.Utils.getDrawable;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
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
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.colorToRgbaString;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotationAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

final class LocationLayer implements PluginAnimator.OnLayerAnimationsValuesChangeListener {

  @RenderMode.Mode
  private int renderMode;

  private final MapboxMap mapboxMap;
  private LocationLayerOptions options;
  private Context context;

  private final List<String> layerMap = new ArrayList<>();
  private Feature locationFeature;
  private GeoJsonSource locationSource;

  private boolean isHidden;

  LocationLayer(MapView mapView, MapboxMap mapboxMap, LocationLayerOptions options) {
    this.mapboxMap = mapboxMap;
    this.context = mapView.getContext();
    generateLocationFeature(options);
    initializeComponents(options);
    setRenderMode(RenderMode.NORMAL);
  }

  void initializeComponents(LocationLayerOptions options) {
    addLocationSource();
    addLayers();
    applyStyle(options);

    if (isHidden) {
      hide();
    } else {
      show();
    }
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
    styleScaling(options);
    determineIconsSource(options);
  }

  void setRenderMode(@RenderMode.Mode int renderMode) {
    this.renderMode = renderMode;

    if (!isHidden) {
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

      determineIconsSource(options);
    }
  }

  int getRenderMode() {
    return renderMode;
  }

  //
  // Layer action
  //

  void show() {
    isHidden = false;
    setRenderMode(renderMode);
  }

  void hide() {
    isHidden = true;
    for (String layerId : layerMap) {
      setLayerVisibility(layerId, false);
    }
  }

  private void setLayerVisibility(String layerId, boolean visible) {
    Layer layer = mapboxMap.getLayer(layerId);
    if (layer != null) {
      String targetVisibility = visible ? VISIBLE : NONE;
      if (!layer.getVisibility().value.equals(targetVisibility)) {
        layer.setProperties(visibility(visible ? VISIBLE : NONE));
      }
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
            get(PROPERTY_LOCATION_STALE), get(PROPERTY_FOREGROUND_STALE_ICON),
            get(PROPERTY_FOREGROUND_ICON))),
          stop(BACKGROUND_LAYER, switchCase(
            get(PROPERTY_LOCATION_STALE), get(PROPERTY_BACKGROUND_STALE_ICON),
            get(PROPERTY_BACKGROUND_ICON))),
          stop(SHADOW_LAYER, literal(SHADOW_ICON)),
          stop(BEARING_LAYER, get(PROPERTY_BEARING_ICON))
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
    layerMap.add(layer.getId());
  }

  private void setBearingProperty(String propertyId, float bearing) {
    locationFeature.addNumberProperty(propertyId, bearing);
    refreshSource();
  }

  void updateAccuracyRadius(float accuracy) {
    if (renderMode == RenderMode.COMPASS || renderMode == RenderMode.NORMAL) {
      locationFeature.addNumberProperty(PROPERTY_ACCURACY_RADIUS, accuracy);
      refreshSource();
    }
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

  @SuppressLint("Range")
  private void generateLocationFeature(LocationLayerOptions options) {
    if (locationFeature == null) {
      locationFeature = Feature.fromGeometry(
        Point.fromLngLat(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
      );
      locationFeature.addNumberProperty(PROPERTY_GPS_BEARING, 0f);
      locationFeature.addNumberProperty(PROPERTY_COMPASS_BEARING, 0f);
      locationFeature.addBooleanProperty(PROPERTY_LOCATION_STALE, options.enableStaleState());
    }
  }

  private void addLocationSource() {
    locationSource = new GeoJsonSource(
      LOCATION_SOURCE,
      locationFeature,
      new GeoJsonOptions().withMaxZoom(16)
    );

    mapboxMap.addSource(locationSource);
  }

  private void refreshSource() {
    GeoJsonSource source = mapboxMap.getSourceAs(LOCATION_SOURCE);
    if (source != null) {
      locationSource.setGeoJson(locationFeature);
    }
  }

  private void setLocationPoint(Point locationPoint) {
    JsonObject properties = locationFeature.properties();
    if (properties != null) {
      locationFeature = Feature.fromGeometry(locationPoint, properties);
      refreshSource();
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
    locationFeature.addStringProperty(PROPERTY_ACCURACY_COLOR, colorToRgbaString(accuracyColor));
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

  private void styleScaling(LocationLayerOptions options) {
    for (String layerId : layerMap) {
      Layer layer = mapboxMap.getLayer(layerId);
      if (layer != null && layer instanceof SymbolLayer) {
        layer.setProperties(
          iconSize(
            interpolate(linear(), zoom(),
              stop(options.minZoom(), options.minZoomIconScale()),
              stop(options.maxZoom(), options.maxZoomIconScale())
            )
          )
        );
      }
    }
  }

  private void determineIconsSource(LocationLayerOptions options) {
    String foregroundIconString = buildIconString(
      renderMode == RenderMode.GPS ? options.gpsName() : options.foregroundName(), FOREGROUND_ICON);
    String foregroundStaleIconString = buildIconString(options.foregroundStaleName(), FOREGROUND_STALE_ICON);
    String backgroundIconString = buildIconString(options.backgroundName(), BACKGROUND_ICON);
    String backgroundStaleIconString = buildIconString(options.backgroundStaleName(), BACKGROUND_STALE_ICON);
    String bearingIconString = buildIconString(options.bearingName(), BEARING_ICON);

    locationFeature.addStringProperty(PROPERTY_FOREGROUND_ICON, foregroundIconString);
    locationFeature.addStringProperty(PROPERTY_BACKGROUND_ICON, backgroundIconString);
    locationFeature.addStringProperty(PROPERTY_FOREGROUND_STALE_ICON, foregroundStaleIconString);
    locationFeature.addStringProperty(PROPERTY_BACKGROUND_STALE_ICON, backgroundStaleIconString);
    locationFeature.addStringProperty(PROPERTY_BEARING_ICON, bearingIconString);
    refreshSource();
  }

  private String buildIconString(@Nullable String bitmapName, @NonNull String drawableName) {
    if (bitmapName != null) {
      return bitmapName;
    }
    return drawableName;
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

  @Override
  public void onNewAccuracyRadiusValue(float accuracyRadiusValue) {
    updateAccuracyRadius(accuracyRadiusValue);
  }
}