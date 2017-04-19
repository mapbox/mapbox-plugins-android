package com.mapbox.mapboxsdk.plugins.traffic;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.CameraFunction;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stop;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.List;

import static com.mapbox.mapboxsdk.style.functions.Function.zoom;
import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.categorical;
import static com.mapbox.mapboxsdk.style.functions.stops.Stops.exponential;
import static com.mapbox.mapboxsdk.style.layers.Filter.in;
import static com.mapbox.mapboxsdk.style.layers.Filter.notIn;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * The traffic plugin allows to add Mapbox Traffic v1 to the Mapbox Android SDK v5.0.2.
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)} and provide
 * a valid instance of {@link MapView} and {@link MapboxMap}.
 * </p>
 * <p>
 * Use {@link #toggle()} to switch state of this plugin to enable or disabled.
 * Use {@link #isEnabled()} to validate if the plugin is active or not.
 * </p>
 */
public final class TrafficPlugin implements MapView.OnMapChangedListener {

  private MapboxMap mapboxMap;
  private boolean enabled;

  /**
   * Create a traffic plugin.
   *
   * @param mapView   the MapView to apply the traffic plugin to
   * @param mapboxMap the MapboxMap to apply traffic plugin with
   */
  public TrafficPlugin(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapView.addOnMapChangedListener(this);
  }

  /**
   * Returns true if the traffic plugin is currently enabled.
   *
   * @return true if enabled, false otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Toggles the traffic plugin state.
   * <p>
   * If the traffic plugin wasn't initialised yet, traffic source and layers will be added to the current map style.
   * Else visibility will be toggled based on the current state.
   * </p>
   */
  public void toggle() {
    enabled = !enabled;
    updateState();
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
    if (change == MapView.DID_FINISH_LOADING_STYLE && isEnabled()) {
      updateState();
    }
  }

  /**
   * Update the state of the traffic plugin.
   */
  private void updateState() {
    Source source = mapboxMap.getSource(SourceData.SOURCE_ID);
    if (source == null) {
      initSourceAndLayers();
      return;
    }
    setVisibility(enabled);
  }

  /**
   * Initialise the traffic source and layers
   */
  private void initSourceAndLayers() {
    // source
    VectorSource trafficSource = new VectorSource(SourceData.SOURCE_ID, SourceData.SOURCE_URL);
    mapboxMap.addSource(trafficSource);

    // functions
    Function lineColor = TrafficFunction.getLineColorFunction(TrafficColor.BASE_GREEN, TrafficColor.BASE_YELLOW,
      TrafficColor.BASE_ORANGE, TrafficColor.BASE_RED);
    Function lineColorCase = TrafficFunction.getLineColorFunction(TrafficColor.CASE_GREEN, TrafficColor.CASE_YELLOW,
      TrafficColor.CASE_ORANGE, TrafficColor.CASE_RED);
    Function motorwayOffset = TrafficFunction.getOffsetFunction(stop(5, lineOffset(0.5f)), stop(13, lineOffset(3.0f)),
      stop(18, lineOffset(7.0f)));
    Function otherOffset = TrafficFunction.getOffsetFunction(stop(7, lineOffset(0.3f)), stop(18, lineOffset(6.0f)),
      stop(22, lineOffset(100.0f)));
    CameraFunction motorwayWidth = TrafficFunction.getWidthFunction(stop(7, lineWidth(1.5f)),
      stop(18, lineWidth(20.0f)));
    CameraFunction motorwayCaseWidth = TrafficFunction.getWidthFunction(stop(7, lineWidth(3.0f)),
      stop(18, lineWidth(24.0f)));
    CameraFunction otherWidth = TrafficFunction.getWidthFunction(stop(11, lineWidth(1.0f)), stop(14, lineWidth(2.0f)),
      stop(17, lineWidth(4.0f)), stop(22, lineWidth(30.0f)));
    CameraFunction otherCaseWidth = TrafficFunction.getWidthFunction(stop(11, lineWidth(1.25f)),
      stop(14, lineWidth(2.5f)), stop(17, lineWidth(5.5f)), stop(22, lineWidth(34.0f)));

    // layers
    LineLayer motorWay = TrafficLayer.getLineLayer(MotorWay.BASE_LAYER_ID, lineColor, motorwayWidth, motorwayOffset,
      MotorWay.ZOOM_LEVEL, MotorWay.FILTER);
    LineLayer motorwayCase = TrafficLayer.getLineLayer(MotorWay.CASE_LAYER_ID, lineColorCase, motorwayCaseWidth,
      motorwayOffset, MotorWay.ZOOM_LEVEL, MotorWay.FILTER);
    LineLayer primary = TrafficLayer.getLineLayer(Primary.BASE_LAYER_ID, lineColor, otherWidth, otherOffset,
      Primary.ZOOM_LEVEL, Primary.FILTER);
    LineLayer primaryCase = TrafficLayer.getLineLayer(Primary.CASE_LAYER_ID, lineColorCase, otherCaseWidth,
      otherOffset, Primary.ZOOM_LEVEL, Primary.FILTER);
    LineLayer local = TrafficLayer.getLineLayer(Local.BASE_LAYER_ID, lineColor, otherWidth, otherOffset,
      Local.ZOOM_LEVEL, Local.FILTER);
    LineLayer localCase = TrafficLayer.getLineLayer(Local.LOCAL_CASE_LAYER_ID, lineColorCase, otherCaseWidth,
      otherOffset, Local.ZOOM_LEVEL, Local.FILTER);

    // // TODO: add above highest road label instead of bridge-motorway https://github
    // .com/mapbox/mapbox-gl-native/issues/8663
    mapboxMap.addLayerAbove(localCase, "bridge-motorway");
    mapboxMap.addLayerAbove(local, localCase.getId());
    mapboxMap.addLayerAbove(primaryCase, local.getId());
    mapboxMap.addLayerAbove(primary, primaryCase.getId());
    mapboxMap.addLayerAbove(motorwayCase, primary.getId());
    mapboxMap.addLayerAbove(motorWay, motorwayCase.getId());
  }

  /**
   * Toggles the visibility of the traffic layers.
   *
   * @param visible true for visible, false for none
   */
  private void setVisibility(boolean visible) {
    List<Layer> layers = mapboxMap.getLayers();
    String id;
    for (Layer layer : layers) {
      id = layer.getId();
      // TODO use sourceLayer filter instead
      if (id.equals(MotorWay.BASE_LAYER_ID) || id.equals(MotorWay.CASE_LAYER_ID) || id.equals(Primary.BASE_LAYER_ID)
        || id.equals(Primary.CASE_LAYER_ID) || id.equals(Primary.BASE_LAYER_ID) || id.equals(Primary.CASE_LAYER_ID)) {
        layer.setProperties(visibility(visible ? "visible" : "none"));
      }
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
  }

  private static class TrafficLayer {

    private static LineLayer getLineLayer(String lineLayerId, Function lineColor, CameraFunction lineWidth, Function
      lineOffset, float minZoom, Filter.Statement statement) {
      LineLayer lineLayer = new LineLayer(lineLayerId, SourceData.SOURCE_ID);
      lineLayer.setSourceLayer(SourceData.SOURCE_LAYER);
      lineLayer.setMinZoom(minZoom);
      lineLayer.setProperties(
        lineColor(lineColor),
        lineWidth(lineWidth),
        lineOffset(lineOffset)
      );
      lineLayer.setFilter(statement);
      return lineLayer;
    }
  }

  private static class SourceData {
    private static final String SOURCE_ID = "traffic";
    private static final String SOURCE_LAYER = "traffic";
    private static final String SOURCE_URL = "mapbox://mapbox.mapbox-traffic-v1";
  }

  private static class MotorWay {
    private static final String BASE_LAYER_ID = "traffic-motorway";
    private static final String CASE_LAYER_ID = "traffic-motorway-case";
    private static final float ZOOM_LEVEL = 5.0f;
    private static final Filter.Statement FILTER = in("class", "motorway", "trunk");
  }

  private static class Primary {
    private static final String BASE_LAYER_ID = "traffic-primary";
    private static final String CASE_LAYER_ID = "traffic-primary-case";
    private static final float ZOOM_LEVEL = 5.0f;
    private static final Filter.Statement FILTER = notIn("class", "motorway", "trunk", "service", "street");
  }

  private static class Local {
    private static final String BASE_LAYER_ID = "traffic-local";
    private static final String LOCAL_CASE_LAYER_ID = "traffic-local-case";
    private static final float ZOOM_LEVEL = 16.0f;
    private static final Filter.Statement FILTER = in("class", "street");
  }

  private static class TrafficColor {
    private static final int BASE_GREEN = Color.parseColor("#4CAF50");
    private static final int CASE_GREEN = Color.parseColor("#388E3C");
    private static final int BASE_YELLOW = Color.parseColor("#FFEB3B");
    private static final int CASE_YELLOW = Color.parseColor("#FBC02D");
    private static final int BASE_ORANGE = Color.parseColor("#FF9800");
    private static final int CASE_ORANGE = Color.parseColor("#F57C00");
    private static final int BASE_RED = Color.parseColor("#f44336");
    private static final int CASE_RED = Color.parseColor("#D32F2F");
  }
}
