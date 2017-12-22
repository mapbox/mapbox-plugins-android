package com.mapbox.mapboxsdk.plugins.traffic;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.CameraFunction;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stop;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.functions.Function.zoom;
import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.categorical;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.exponential;
import static com.mapbox.mapboxsdk.style.layers.Filter.in;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
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
public final class TrafficPlugin implements MapView.OnMapChangedListener {

  private MapboxMap mapboxMap;
  private List<String> layerIds;
  private String belowLayer;
  private boolean visible;

  /**
   * Create a traffic plugin.
   *
   * @param mapView   the MapView to apply the traffic plugin to
   * @param mapboxMap the MapboxMap to apply traffic plugin with
   */
  public TrafficPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this(mapView, mapboxMap, null);
  }

  /**
   * Create a traffic plugin.
   *
   * @param mapView    the MapView to apply the traffic plugin to
   * @param mapboxMap  the MapboxMap to apply traffic plugin with
   * @param belowLayer the layer id where you'd like the traffic to display below
   */
  public TrafficPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @Nullable String belowLayer) {
    this.mapboxMap = mapboxMap;
    this.belowLayer = belowLayer;
    mapView.addOnMapChangedListener(this);
    updateState();
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
    List<Layer> layers = mapboxMap.getLayers();
    for (Layer layer : layers) {
      if (layerIds.contains(layer.getId())) {
        layer.setProperties(visibility(visible ? Property.VISIBLE : Property.NONE));
      }
    }
  }

  /**
   * Called when a map change events occurs.
   * <p>
   * Used to detect loading of a new style, if applicable reapply traffic source and layers.
   * </p>
   *
   * @param change the map change event that occurred
   */
  @Override
  public void onMapChanged(int change) {
    if (change == MapView.DID_FINISH_LOADING_STYLE && isVisible()) {
      updateState();
    }
  }

  /**
   * Update the state of the traffic plugin.
   */
  private void updateState() {
    Source source = mapboxMap.getSource(TrafficData.SOURCE_ID);
    if (source == null) {
      initialise();
      return;
    }
    setVisibility(visible);
  }

  /**
   * Initialise the traffic source and layers.
   */
  private void initialise() {
    layerIds = new ArrayList<>();

    try {
      addTrafficSource();
      addTrafficLayers();
    } catch (Exception exception) {
      Timber.e("Unable to attach Traffic to current style: ", exception);
    } catch (UnsatisfiedLinkError error) {
      Timber.e("Unable to load native libraries: ", error);
    }
  }

  /**
   * Adds traffic source to the map.
   */
  private void addTrafficSource() {
    VectorSource trafficSource = new VectorSource(TrafficData.SOURCE_ID, TrafficData.SOURCE_URL);
    mapboxMap.addSource(trafficSource);
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
      List<Layer> styleLayers = mapboxMap.getLayers();
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
    mapboxMap.addLayerBelow(layerCase, idAboveLayer);
    mapboxMap.addLayerAbove(layer, layerCase.getId());
    layerIds.add(layerCase.getId());
    layerIds.add(layer.getId());
  }

  private static class TrafficLayer {

    private static LineLayer getLineLayer(String lineLayerId, float minZoom, Filter.Statement statement,
                                          Function lineColor, CameraFunction lineWidth, Function lineOffset) {
      return getLineLayer(lineLayerId, minZoom, statement, lineColor, lineWidth, lineOffset, null);
    }

    private static LineLayer getLineLayer(String lineLayerId, float minZoom, Filter.Statement statement,
                                          Function lineColor, CameraFunction lineWidth, Function lineOffset,
                                          Function lineOpacity) {
      LineLayer lineLayer = new LineLayer(lineLayerId, TrafficData.SOURCE_ID);
      lineLayer.setSourceLayer(TrafficData.SOURCE_LAYER);
      lineLayer.setProperties(
        lineCap("round"),
        lineJoin("round"),
        lineColor(lineColor),
        lineWidth(lineWidth),
        lineOffset(lineOffset)
      );
      if (lineOpacity != null) {
        lineLayer.setProperties(lineOpacity(lineOpacity));
      }

      lineLayer.setFilter(statement);
      lineLayer.setMinZoom(minZoom);
      return lineLayer;
    }
  }

  private static class TrafficFunction {
    private static Function getLineColorFunction(@ColorInt int low, @ColorInt int moderate, @ColorInt int heavy,
                                                 @ColorInt int severe) {
      return Function.property(
        "congestion",
        categorical(
          stop("low", fillColor(low)),
          stop("moderate", fillColor(moderate)),
          stop("heavy", fillColor(heavy)),
          stop("severe", fillColor(severe))
        )
      ).withDefaultValue(fillColor(Color.TRANSPARENT));
    }

    private static CameraFunction getOffsetFunction(Stop... stops) {
      return zoom(exponential(stops).withBase(1.5f));
    }

    private static CameraFunction getWidthFunction(Stop... stops) {
      return zoom(exponential(stops).withBase(1.5f));
    }

    private static Function getOpacityFunction(Stop... stops) {
      return zoom(exponential(stops));
    }
  }

  static class TrafficData {
    static final String SOURCE_ID = "traffic";
    static final String SOURCE_LAYER = "traffic";
    static final String SOURCE_URL = "mapbox://mapbox.mapbox-traffic-v1";
  }

  static class TrafficType {
    static final Function FUNCTION_LINE_COLOR = TrafficFunction.getLineColorFunction(TrafficColor.BASE_GREEN,
      TrafficColor.BASE_YELLOW, TrafficColor.BASE_ORANGE, TrafficColor.BASE_RED);
    static final Function FUNCTION_LINE_COLOR_CASE = TrafficFunction.getLineColorFunction(
      TrafficColor.CASE_GREEN, TrafficColor.CASE_YELLOW, TrafficColor.CASE_ORANGE, TrafficColor.CASE_RED);
  }

  static class MotorWay extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-motorway";
    static final String CASE_LAYER_ID = "traffic-motorway-bg";
    static final float ZOOM_LEVEL = 6.0f;
    static final Filter.Statement FILTER = in("class", "motorway");
    static final CameraFunction FUNCTION_LINE_WIDTH = TrafficFunction.getWidthFunction(
      stop(6, lineWidth(0.5f)), stop(9, lineWidth(1.5f)), stop(18.0f, lineWidth(14.0f)),
      stop(20.0f, lineWidth(18.0f)));
    static final CameraFunction FUNCTION_LINE_WIDTH_CASE = TrafficFunction.getWidthFunction(
      stop(6, lineWidth(0.5f)), stop(9, lineWidth(3.0f)), stop(18.0f, lineWidth(16.0f)),
      stop(20.0f, lineWidth(20.0f)));
    static final CameraFunction FUNCTION_LINE_OFFSET = TrafficFunction.getOffsetFunction(
      stop(7, lineOffset(0.0f)), stop(9, lineOffset(1.2f)), stop(11, lineOffset(1.2f)),
      stop(18, lineOffset(10.0f)), stop(20, lineOffset(15.5f)));
  }

  static class Trunk extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-trunk";
    static final String CASE_LAYER_ID = "traffic-trunk-bg";
    static final float ZOOM_LEVEL = 6.0f;
    static final Filter.Statement FILTER = in("class", "trunk");
    static final CameraFunction FUNCTION_LINE_WIDTH = TrafficFunction.getWidthFunction(
      stop(8, lineWidth(0.75f)), stop(18, lineWidth(11f)), stop(20f, lineWidth(15.0f)));
    static final CameraFunction FUNCTION_LINE_WIDTH_CASE = TrafficFunction.getWidthFunction(
      stop(8, lineWidth(0.5f)), stop(9, lineWidth(2.25f)), stop(18.0f, lineWidth(13.0f)),
      stop(20.0f, lineWidth(17.5f)));
    static final CameraFunction FUNCTION_LINE_OFFSET = TrafficFunction.getOffsetFunction(
      stop(7, lineOffset(0.0f)), stop(9, lineOffset(1f)), stop(18, lineOffset(13f)),
      stop(20, lineOffset(18.0f)));
  }

  static class Primary extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-primary";
    static final String CASE_LAYER_ID = "traffic-primary-bg";
    static final float ZOOM_LEVEL = 6.0f;
    static final Filter.Statement FILTER = in("class", "primary");
    static final CameraFunction FUNCTION_LINE_WIDTH = TrafficFunction.getWidthFunction(
      stop(10, lineWidth(1.0f)), stop(15, lineWidth(4.0f)), stop(20, lineWidth(16f)));
    static final CameraFunction FUNCTION_LINE_WIDTH_CASE = TrafficFunction.getWidthFunction(
      stop(10, lineWidth(0.75f)), stop(15, lineWidth(6f)), stop(20.0f, lineWidth(18.0f)));
    static final CameraFunction FUNCTION_LINE_OFFSET = TrafficFunction.getOffsetFunction(
      stop(10, lineOffset(0.0f)), stop(12, lineOffset(1.5f)), stop(18, lineOffset(13f)),
      stop(20, lineOffset(16.0f)));
    static final Function FUNCTION_LINE_OPACITY_CASE = TrafficFunction.getOpacityFunction(
      stop(11, lineOpacity(0.0f)), stop(12, lineOpacity(1.0f)));
  }

  static class Secondary extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-secondary-tertiary";
    static final String CASE_LAYER_ID = "traffic-secondary-tertiary-bg";
    static final float ZOOM_LEVEL = 6.0f;
    static final Filter.Statement FILTER = in("class", "secondary", "tertiary");
    static final CameraFunction FUNCTION_LINE_WIDTH = TrafficFunction.getWidthFunction(
      stop(9, lineWidth(0.5f)), stop(18, lineWidth(9.0f)), stop(20, lineWidth(14f)));
    static final CameraFunction FUNCTION_LINE_WIDTH_CASE = TrafficFunction.getWidthFunction(
      stop(9, lineWidth(1.5f)), stop(18, lineWidth(11f)), stop(20.0f, lineWidth(16.5f)));
    static final CameraFunction FUNCTION_LINE_OFFSET = TrafficFunction.getOffsetFunction(
      stop(10, lineOffset(0.5f)), stop(15, lineOffset(5f)), stop(18, lineOffset(11f)),
      stop(20, lineOffset(14.5f)));
    static final Function FUNCTION_LINE_OPACITY_CASE = TrafficFunction.getOpacityFunction(
      stop(13, lineOpacity(0.0f)), stop(14, lineOpacity(1.0f)));
  }

  static class Local extends TrafficType {
    static final String BASE_LAYER_ID = "traffic-local";
    static final String CASE_LAYER_ID = "traffic-local-case";
    static final float ZOOM_LEVEL = 15.0f;
    static final Filter.Statement FILTER = in("class", "motorway_link", "service", "street");
    static final CameraFunction FUNCTION_LINE_WIDTH = TrafficFunction.getWidthFunction(
      stop(14, lineWidth(1.5f)), stop(20, lineWidth(13.5f)));
    static final CameraFunction FUNCTION_LINE_WIDTH_CASE = TrafficFunction.getWidthFunction(
      stop(14, lineWidth(2.5f)), stop(20, lineWidth(15.5f)));
    static final CameraFunction FUNCTION_LINE_OFFSET = TrafficFunction.getOffsetFunction(
      stop(14, lineOffset(2f)), stop(20, lineOffset(18f)));
    static final Function FUNCTION_LINE_OPACITY_CASE = TrafficFunction.getOpacityFunction(
      stop(15, lineOpacity(0.0f)), stop(16, lineOpacity(1.0f)));
  }

  static class TrafficColor {
    static final int BASE_GREEN = Color.parseColor("#39c66d");
    static final int CASE_GREEN = Color.parseColor("#059441");
    static final int BASE_YELLOW = Color.parseColor("#ff8c1a");
    static final int CASE_YELLOW = Color.parseColor("#d66b00");
    static final int BASE_ORANGE = Color.parseColor("#ff0015");
    static final int CASE_ORANGE = Color.parseColor("#bd0010");
    static final int BASE_RED = Color.parseColor("#981b25");
    static final int CASE_RED = Color.parseColor("#5f1117");
  }
}
