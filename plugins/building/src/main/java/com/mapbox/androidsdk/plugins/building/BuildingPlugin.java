package com.mapbox.androidsdk.plugins.building;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.Stop;
import com.mapbox.mapboxsdk.style.functions.stops.Stops;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.light.Light;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * The building plugin allows to add 3d buildings FillExtrusionLayer to the Mapbox Android SDK v5.1.0.
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)} and provide
 * a valid instance of {@link MapView} and {@link MapboxMap}.
 * </p>
 * <ul>
 * <li>Use {@link #setVisibility(boolean)}} to show buildings from this plugin.</li>
 * <li>Use {@link #setColor(int)} to change the color of the buildings from this plugin.</li>
 * <li>Use {@link #setOpacity(float)} to change the opacity of the buildings from this plugin.</li>
 * </ul>
 */
public final class BuildingPlugin {

  private static final String LAYER_ID = "mapbox-android-plugin-3d-buildings";

  private FillExtrusionLayer fillExtrusionLayer;
  private boolean visible = false;
  private int color = Color.LTGRAY;
  private float opacity = 0.6f;
  private float minZoomLevel = 15.0f;
  private Light light;

  /**
   * Create a building plugin.
   *
   * @param mapView   the MapView to apply the building plugin to
   * @param mapboxMap the MapboxMap to apply building plugin with
   * @since 0.1.0
   */
  public BuildingPlugin(@NonNull MapView mapView, @NonNull final MapboxMap mapboxMap) {
    initLayer(mapboxMap);
    mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
      @Override
      public void onMapChanged(int change) {
        if (change == MapView.DID_FINISH_LOADING_STYLE && mapboxMap.getLayer(LAYER_ID) == null) {
          initLayer(mapboxMap);
        }
      }
    });
  }

  /**
   * Initialises and adds the fill extrusion layer used by this plugin.
   *
   * @param mapboxMap the MapboxMap instance to add the layer to
   */
  private void initLayer(MapboxMap mapboxMap) {
    light = mapboxMap.getLight();
    fillExtrusionLayer = new FillExtrusionLayer(LAYER_ID, "composite");
    fillExtrusionLayer.setSourceLayer("building");
    fillExtrusionLayer.setMinZoom(minZoomLevel);
    fillExtrusionLayer.setProperties(
      visibility(visible ? Property.VISIBLE : Property.NONE),
      fillExtrusionColor(color),
      fillExtrusionHeight(Function.composite(
        "height",
        Stops.exponential(
          Stop.stop(15f, 0f, fillExtrusionHeight(0f)),
          Stop.stop(16f, 0f, fillExtrusionHeight(0f)),
          Stop.stop(16f, 1000f, fillExtrusionHeight(1000f))
        ))),
      fillExtrusionOpacity(opacity)
    );
    mapboxMap.addLayer(fillExtrusionLayer);
  }

  /**
   * Toggles the visibility of the building layer.
   *
   * @param visible true for visible, false for none
   * @since 0.1.0
   */
  public void setVisibility(boolean visible) {
    this.visible = visible;
    fillExtrusionLayer.setProperties(visibility(visible ? Property.VISIBLE : Property.NONE));
  }

  /**
   * Change the building opacity. Calls into changing the fill extrusion fill opacity.
   *
   * @param opacity {@code float} value between 0 (invisible) and 1 (solid)
   * @since 0.1.0
   */
  public void setOpacity(@FloatRange(from = 0.0f, to = 1.0f) float opacity) {
    this.opacity = opacity;
    fillExtrusionLayer.setProperties(fillExtrusionOpacity(opacity));
  }

  /**
   * Change the building color. Calls into changing the fill extrusion fill color.
   *
   * @param color an {@code Int} value which represents a color
   * @since 0.1.0
   */
  public void setColor(@ColorInt int color) {
    this.color = color;
    fillExtrusionLayer.setProperties(fillExtrusionColor(color));
  }

  /**
   * Change the building min zoom level. This is the minimum zoom level where buildings will start to show. useful to
   * limit showing buildings at higher zoom levels.
   *
   * @param minZoomLevel a {@code float} value between the maps minimum and maximum zoom level which defines at which
   *                     level the buildings should show up
   * @since 0.1.0
   */
  public void setMinZoomLevel(@FloatRange(from = MapboxConstants.MINIMUM_ZOOM, to = MapboxConstants.MAXIMUM_ZOOM)
                                float minZoomLevel) {
    this.minZoomLevel = minZoomLevel;
    fillExtrusionLayer.setMinZoom(minZoomLevel);
  }

  /**
   * Get the light source that is illuminating the building.
   *
   * @return the light source
   * @since 0.1.0
   */
  public Light getLight() {
    return light;
  }
}


