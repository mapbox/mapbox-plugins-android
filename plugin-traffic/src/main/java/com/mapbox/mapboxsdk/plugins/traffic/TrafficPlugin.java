package com.mapbox.mapboxsdk.plugins.traffic;

import android.graphics.Color;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * The Traffic Plugin allows you to add Mapbox Traffic v1 to the Mapbox Maps SDK for Android.
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)} and provide
 * a valid instance of {@link MapView} and {@link MapboxMap}.
 * </p>
 * <p>
 * Use {@link #setVisibility(boolean)} to switch state of this plugin to enable or disabled.
 * Use {@link #isVisible()} to validate if the plugin is active or not.
 * </p>
 */
@UiThread
public final class TrafficPlugin {

  private final MapboxMap mapboxMap;
  private Style style;
  private final List<String> layerIds = new ArrayList<>();
  private final String belowLayer;
  private boolean visible;

  /**
   * Create a traffic plugin.
   *
   * @param mapView   the MapView to apply the traffic plugin to
   * @param mapboxMap the MapboxMap to apply traffic plugin with
   */
  public TrafficPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null);
  }

  /**
   * Create a traffic plugin.
   *
   * @param mapView    the MapView to apply the traffic plugin to
   * @param mapboxMap  the MapboxMap to apply traffic plugin with
   * @param belowLayer the layer id where you'd like the traffic to display below
   */
  public TrafficPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style,
                       @Nullable String belowLayer) {
    if (!style.isFullyLoaded()) {
      throw new RuntimeException("The style has to be non-null and fully loaded.");
    }

    this.mapboxMap = mapboxMap;
    this.style = style;
    this.belowLayer = belowLayer;
    mapView.addOnDidFinishLoadingStyleListener(new StyleLoadHandler(this));
  }

  /**
   * Returns true if the traffic plugin is currently enabled.
   *
   * @return true if enabled, false otherwise
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Toggles the visibility of the traffic layers.
   *
   * @param visible true for visible, false for none
   */
  public void setVisibility(boolean visible) {
    this.visible = visible;

    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

    Source source = style.getSource(TrafficData.SOURCE_ID);
    if (source == null) {
      initialise();
    }

    List<Layer> layers = style.getLayers();
    for (Layer layer : layers) {
      if (layerIds.contains(layer.getId())) {
        layer.setProperties(visibility(visible ? Property.VISIBLE : Property.NONE));
      }
    }
  }

  /**
   * Initialise the traffic source and layers.
   */
  private void initialise() {
    try {
      addTrafficSource();
      addTrafficLayers();
    } catch (Exception exception) {
      Timber.e(exception, "Unable to attach Traffic to current style: ");
    } catch (UnsatisfiedLinkError error) {
      Timber.e(error, "Unable to load native libraries: ");
    }
  }

  /**
   * Adds traffic source to the map.
   */
  private void addTrafficSource() {
    VectorSource trafficSource = new VectorSource(TrafficData.SOURCE_ID, TrafficData.SOURCE_URL);
    style.addSource(trafficSource);
  }

  /**
   * Adds traffic layers to the map.
   */
  private void addTrafficLayers() {
    addLocalLayer();
    addSecondaryLayer();
    addPrimaryLayer();
    addTrunkLayer();
    addMotorwayLayer();
  }

  /**
   * Add local layer to the map.
   */
  private void addLocalLayer() {
    LineLayer local = TrafficLayer.getLineLayer(
      Local.BASE_LAYER_ID,
      Local.ZOOM_LEVEL,
      Local.FILTER,
      Local.FUNCTION_LINE_COLOR,
      Local.FUNCTION_LINE_WIDTH,
      Local.FUNCTION_LINE_OFFSET
    );

    LineLayer localCase = TrafficLayer.getLineLayer(
      Local.CASE_LAYER_ID,
      Local.ZOOM_LEVEL,
      Local.FILTER,
      Local.FUNCTION_LINE_COLOR_CASE,
      Local.FUNCTION_LINE_WIDTH_CASE,
      Local.FUNCTION_LINE_OFFSET,
      Local.FUNCTION_LINE_OPACITY_CASE
    );

    addTrafficLayersToMap(localCase, local, placeLayerBelow());
  }

  /**
   * Attempts to find the layer which the traffic should be placed below. Depending on the style, this might not always
   * be accurate.
   */
  private String placeLayerBelow() {
    if (belowLayer == null || belowLayer.isEmpty()) {
      List<Layer> styleLayers = style.getLayers();
      Layer layer;
      for (int i = styleLayers.size() - 1; i >= 0; i--) {
        layer = styleLayers.get(i);
        if (!(layer instanceof SymbolLayer)) {
          return layer.getId();
        }
      }
    }
    return belowLayer;
  }

  /**
   * Add secondary layer to the map.
   */
  private void addSecondaryLayer() {
    LineLayer secondary = TrafficLayer.getLineLayer(
      Secondary.BASE_LAYER_ID,
      Secondary.ZOOM_LEVEL,
      Secondary.FILTER,
      Secondary.FUNCTION_LINE_COLOR,
      Secondary.FUNCTION_LINE_WIDTH,
      Secondary.FUNCTION_LINE_OFFSET
    );

    LineLayer secondaryCase = TrafficLayer.getLineLayer(
      Secondary.CASE_LAYER_ID,
      Secondary.ZOOM_LEVEL,
      Secondary.FILTER,
      Secondary.FUNCTION_LINE_COLOR_CASE,
      Secondary.FUNCTION_LINE_WIDTH_CASE,
      Secondary.FUNCTION_LINE_OFFSET,
      Secondary.FUNCTION_LINE_OPACITY_CASE
    );

    addTrafficLayersToMap(secondaryCase, secondary, getLastAddedLayerId());
  }

  /**
   * Add primary layer to the map.
   */
  private void addPrimaryLayer() {
    LineLayer primary = TrafficLayer.getLineLayer(
      Primary.BASE_LAYER_ID,
      Primary.ZOOM_LEVEL,
      Primary.FILTER,
      Primary.FUNCTION_LINE_COLOR,
      Primary.FUNCTION_LINE_WIDTH,
      Primary.FUNCTION_LINE_OFFSET
    );

    LineLayer primaryCase = TrafficLayer.getLineLayer(
      Primary.CASE_LAYER_ID,
      Primary.ZOOM_LEVEL,
      Primary.FILTER,
      Primary.FUNCTION_LINE_COLOR_CASE,
      Primary.FUNCTION_LINE_WIDTH_CASE,
      Primary.FUNCTION_LINE_OFFSET,
      Primary.FUNCTION_LINE_OPACITY_CASE
    );

    addTrafficLayersToMap(primaryCase, primary, getLastAddedLayerId());
  }

  /**
   * Add trunk layer to the map.
   */
  private void addTrunkLayer() {
    LineLayer trunk = TrafficLayer.getLineLayer(
      Trunk.BASE_LAYER_ID,
      Trunk.ZOOM_LEVEL,
      Trunk.FILTER,
      Trunk.FUNCTION_LINE_COLOR,
      Trunk.FUNCTION_LINE_WIDTH,
      Trunk.FUNCTION_LINE_OFFSET
    );

    LineLayer trunkCase = TrafficLayer.getLineLayer(
      Trunk.CASE_LAYER_ID,
      Trunk.ZOOM_LEVEL,
      Trunk.FILTER,
      Trunk.FUNCTION_LINE_COLOR_CASE,
      Trunk.FUNCTION_LINE_WIDTH_CASE,
      Trunk.FUNCTION_LINE_OFFSET
    );

    addTrafficLayersToMap(trunkCase, trunk, getLastAddedLayerId());
  }

  /**
   * Add motorway layer to the map.
   */
  private void addMotorwayLayer() {
    LineLayer motorWay = TrafficLayer.getLineLayer(
      MotorWay.BASE_LAYER_ID,
      MotorWay.ZOOM_LEVEL,
      MotorWay.FILTER,
      MotorWay.FUNCTION_LINE_COLOR,
      MotorWay.FUNCTION_LINE_WIDTH,
      MotorWay.FUNCTION_LINE_OFFSET
    );

    LineLayer motorwayCase = TrafficLayer.getLineLayer(
      MotorWay.CASE_LAYER_ID,
      MotorWay.ZOOM_LEVEL,
      MotorWay.FILTER,
      MotorWay.FUNCTION_LINE_COLOR_CASE,
      MotorWay.FUNCTION_LINE_WIDTH_CASE,
      MotorWay.FUNCTION_LINE_OFFSET
    );

    addTrafficLayersToMap(motorwayCase, motorWay, getLastAddedLayerId());
  }

  /**
   * Returns the last added layer id.
   *
   * @return the id of the last added layer
   */
  private String getLastAddedLayerId() {
    return layerIds.get(layerIds.size() - 1);
  }

  /**
   * Add Layer to the map and track the id.
   *
   * @param layer        the layer to be added to the map
   * @param idAboveLayer the id of the layer above
   */
  private void addTrafficLayersToMap(Layer layerCase, Layer layer, String idAboveLayer) {
    style.addLayerBelow(layerCase, idAboveLayer);
    style.addLayerAbove(layer, layerCase.getId());
    layerIds.add(layerCase.getId());
    layerIds.add(layer.getId());
  }

  private static class TrafficLayer {

    static LineLayer getLineLayer(String lineLayerId, float minZoom, Expression filter,
                                  Expression lineColor, Expression lineWidth, Expression lineOffset) {
      return getLineLayer(lineLayerId, minZoom, filter, lineColor, lineWidth, lineOffset, null);
    }

    static LineLayer getLineLayer(String lineLayerId, float minZoom, Expression filter,
                                  Expression lineColorExpression, Expression lineWidthExpression,
                                  Expression lineOffsetExpression, Expression lineOpacityExpression) {
      LineLayer lineLayer = new LineLayer(lineLayerId, TrafficData.SOURCE_ID);
      lineLayer.setSourceLayer(TrafficData.SOURCE_LAYER);
      lineLayer.setProperties(
        lineCap(Property.LINE_CAP_ROUND),
        lineJoin(Property.LINE_JOIN_ROUND),
        lineColor(lineColorExpression),
        lineWidth(lineWidthExpression),
        lineOffset(lineOffsetExpression)
      );
      if (lineOpacityExpression != null) {
        lineLayer.setProperties(lineOpacity(lineOpacityExpression));
      }

      lineLayer.setFilter(filter);
      lineLayer.setMinZoom(minZoom);
      return lineLayer;
    }
  }

  private static class TrafficFunction {
    static Expression getLineColorFunction(@ColorInt int low, @ColorInt int moderate, @ColorInt int heavy,
                                           @ColorInt int severe) {
      return match(get("congestion"), Expression.color(Color.TRANSPARENT),
        stop("low", Expression.color(low)),
        stop("moderate", Expression.color(moderate)),
        stop("heavy", Expression.color(heavy)),
        stop("severe", Expression.color(severe)));
    }
  }

  @VisibleForTesting
  static class TrafficData {
    static final String SOURCE_ID = "traffic";
    static final String SOURCE_LAYER = "traffic";
    static final String SOURCE_URL = "mapbox://mapbox.mapbox-traffic-v1";
  }

  private static class TrafficType {
    static final Expression FUNCTION_LINE_COLOR = TrafficFunction.getLineColorFunction(TrafficColor.BASE_GREEN,
      TrafficColor.BASE_YELLOW, TrafficColor.BASE_ORANGE, TrafficColor.BASE_RED);
    static final Expression FUNCTION_LINE_COLOR_CASE = TrafficFunction.getLineColorFunction(
      TrafficColor.CASE_GREEN, TrafficColor.CASE_YELLOW, TrafficColor.CASE_ORANGE, TrafficColor.CASE_RED);
  }

  static class MotorWay extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-motorway";
    static final String CASE_LAYER_ID = "traffic-motorway-bg";
    static final float ZOOM_LEVEL = 6.0f;
    static final Expression FILTER = match(
      get("class"), literal(false),
      stop("motorway", true)
    );

    static final Expression FUNCTION_LINE_WIDTH = interpolate(exponential(1.5f), zoom(),
      stop(6, 0.5f),
      stop(9, 1.5f),
      stop(18.0f, 14.0f),
      stop(20.0f, 18.0f)
    );

    static final Expression FUNCTION_LINE_WIDTH_CASE = interpolate(exponential(1.5f), zoom(),
      stop(6, 0.5f),
      stop(9, 3.0f),
      stop(18.0f, 16.0f),
      stop(20.0f, 20.0f)
    );

    static final Expression FUNCTION_LINE_OFFSET = interpolate(exponential(1.5f), zoom(),
      stop(7, 0.0f),
      stop(9, 1.2f),
      stop(11, 1.2f),
      stop(18, 10.0f),
      stop(20, 15.5f));
  }

  static class Trunk extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-trunk";
    static final String CASE_LAYER_ID = "traffic-trunk-bg";
    static final float ZOOM_LEVEL = 6.0f;

    static final Expression FILTER = match(
      get("class"), literal(false),
      stop("trunk", true)
    );

    static final Expression FUNCTION_LINE_WIDTH = interpolate(exponential(1.5f), zoom(),
      stop(8, 0.75f),
      stop(18, 11f),
      stop(20f, 15.0f)
    );

    static final Expression FUNCTION_LINE_WIDTH_CASE = interpolate(exponential(1.5f), zoom(),
      stop(8, 0.5f),
      stop(9, 2.25f),
      stop(18.0f, 13.0f),
      stop(20.0f, 17.5f)
    );

    static final Expression FUNCTION_LINE_OFFSET = interpolate(exponential(1.5f), zoom(),
      stop(7, 0.0f),
      stop(9, 1f),
      stop(18, 13f),
      stop(20, 18.0f));
  }

  static class Primary extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-primary";
    static final String CASE_LAYER_ID = "traffic-primary-bg";
    static final float ZOOM_LEVEL = 6.0f;

    static final Expression FILTER = match(
      get("class"), literal(false),
      stop("primary", literal(true))
    );

    static final Expression FUNCTION_LINE_WIDTH = interpolate(exponential(1.5f), zoom(),
      stop(10, 1.0f),
      stop(15, 4.0f),
      stop(20, 16f)
    );

    static final Expression FUNCTION_LINE_WIDTH_CASE = interpolate(exponential(1.5f), zoom(),
      stop(10, 0.75f),
      stop(15, 6f),
      stop(20.0f, 18.0f)
    );

    static final Expression FUNCTION_LINE_OFFSET = interpolate(exponential(1.5f), zoom(),
      stop(10, 0.0f),
      stop(12, 1.5f),
      stop(18, 13f),
      stop(20, 16.0f)
    );

    static final Expression FUNCTION_LINE_OPACITY_CASE = interpolate(exponential(1.0f), zoom(),
      stop(11, 0.0f),
      stop(12, 1.0f)
    );
  }

  static class Secondary extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-secondary-tertiary";
    static final String CASE_LAYER_ID = "traffic-secondary-tertiary-bg";
    static final float ZOOM_LEVEL = 6.0f;

    static final Expression FILTER = match(
      get("class"), literal(false),
      stop("secondary", true),
      stop("tertiary", true)
    );

    static final Expression FUNCTION_LINE_WIDTH = interpolate(exponential(1.5f), zoom(),
      stop(9, 0.5f),
      stop(18, 9.0f),
      stop(20, 14f)
    );

    static final Expression FUNCTION_LINE_WIDTH_CASE = interpolate(exponential(1.5f), zoom(),
      stop(9, 1.5f),
      stop(18, 11f),
      stop(20.0f, 16.5f)
    );

    static final Expression FUNCTION_LINE_OFFSET = interpolate(exponential(1.5f), zoom(),
      stop(10, 0.5f),
      stop(15, 5f),
      stop(18, 11f),
      stop(20, 14.5f)
    );

    static final Expression FUNCTION_LINE_OPACITY_CASE = interpolate(exponential(1.0f), zoom(),
      stop(13, 0.0f),
      stop(14, 1.0f)
    );
  }

  static class Local extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-local";
    static final String CASE_LAYER_ID = "traffic-local-case";
    static final float ZOOM_LEVEL = 15.0f;

    static final Expression FILTER = match(
      get("class"), literal(false),
      stop("motorway_link", true),
      stop("service", true),
      stop("street", true)
    );

    static final Expression FUNCTION_LINE_WIDTH = interpolate(exponential(1.5f), zoom(),
      stop(14, 1.5f),
      stop(20, 13.5f)
    );
    static final Expression FUNCTION_LINE_WIDTH_CASE = interpolate(exponential(1.5f), zoom(),
      stop(14, 2.5f),
      stop(20, 15.5f)
    );
    static final Expression FUNCTION_LINE_OFFSET = interpolate(exponential(1.5f), zoom(),
      stop(14, 2f),
      stop(20, 18f)
    );

    static final Expression FUNCTION_LINE_OPACITY_CASE = interpolate(exponential(1.0f), zoom(),
      stop(15, 0.0f),
      stop(16, 1.0f)
    );
  }

  private static class TrafficColor {
    static final int BASE_GREEN = Color.parseColor("#39c66d");
    static final int CASE_GREEN = Color.parseColor("#059441");
    static final int BASE_YELLOW = Color.parseColor("#ff8c1a");
    static final int CASE_YELLOW = Color.parseColor("#d66b00");
    static final int BASE_ORANGE = Color.parseColor("#ff0015");
    static final int CASE_ORANGE = Color.parseColor("#bd0010");
    static final int BASE_RED = Color.parseColor("#981b25");
    static final int CASE_RED = Color.parseColor("#5f1117");
  }

  private static class StyleLoadHandler implements MapView.OnDidFinishLoadingStyleListener {

    private WeakReference<TrafficPlugin> trafficPlugin;

    StyleLoadHandler(TrafficPlugin trafficPlugin) {
      this.trafficPlugin = new WeakReference<>(trafficPlugin);
    }

    @Override
    public void onDidFinishLoadingStyle() {
      TrafficPlugin trafficPlugin = this.trafficPlugin.get();
      if (trafficPlugin != null) {
        trafficPlugin.onDidFinishLoadingStyle();
      }
    }
  }

  private void onDidFinishLoadingStyle() {
    mapboxMap.getStyle(new Style.OnStyleLoaded() {
      @Override
      public void onStyleLoaded(@NonNull Style style) {
        TrafficPlugin.this.style = style;
        setVisibility(visible);
      }
    });
  }
}
