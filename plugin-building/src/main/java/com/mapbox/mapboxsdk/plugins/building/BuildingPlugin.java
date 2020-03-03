package com.mapbox.mapboxsdk.plugins.building;

import android.graphics.Color;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.light.Light;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mapbox.mapboxsdk.constants.MapboxConstants.MAXIMUM_ZOOM;
import static com.mapbox.mapboxsdk.constants.MapboxConstants.MINIMUM_ZOOM;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

/**
 * The building plugin allows you to add 3d buildings FillExtrusionLayer to the Mapbox Maps SDK for
 * Android v5.1.0.
 * <p>
 * Initialise this plugin in the {@link com.mapbox.mapboxsdk.maps.OnMapReadyCallback#onMapReady(MapboxMap)}
 * and provide a valid instance of {@link MapView} and {@link MapboxMap}.
 * </p>
 * <ul>
 * <li>Use {@link #setVisibility(boolean)}} to show buildings from this plugin.</li>
 * <li>Use {@link #setColor(int)} to change the color of the buildings from this plugin.</li>
 * <li>Use {@link #setOpacity(float)} to change the opacity of the buildings from this plugin.</li>
 * </ul>
 */
public final class BuildingPlugin {

  /**
   * LAYER_ID is exposed public so it can be utilized by layerBelow and layerAbove arguments
   */
  public static final String LAYER_ID = "mapbox-android-plugin-3d-buildings";

  private FillExtrusionLayer fillExtrusionLayer;
  private boolean visible;
  private int color = Color.LTGRAY;
  private float opacity = 0.6f;
  private float minZoomLevel = 15.0f;
  private Light light;
  private Style style;

  /**
   * Create a building plugin.
   *
   * @param mapView   the MapView to apply the building plugin to
   * @param mapboxMap the MapboxMap to apply building plugin with
   * @since 0.1.0
   */
  public BuildingPlugin(@NonNull MapView mapView, @NonNull final MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null);
  }

  /**
   * Create a building plugin.
   *
   * @param mapView   the MapView to apply the building plugin to
   * @param mapboxMap the MapboxMap to apply building plugin with
   * @since 0.1.0
   */
  public BuildingPlugin(@NonNull MapView mapView, @NonNull final MapboxMap mapboxMap, @NonNull Style style,
                        @Nullable final String belowLayer) {
    this.style = style;
    if (!style.isFullyLoaded()) {
      throw new RuntimeException("The style has to be non-null and fully loaded.");
    }

    initLayer(belowLayer);

    mapView.addOnDidFinishLoadingStyleListener(new MapView.OnDidFinishLoadingStyleListener() {
      @Override
      public void onDidFinishLoadingStyle() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {
            BuildingPlugin.this.style = style;
            initLayer(belowLayer);
          }
        });
      }
    });
  }

  /**
   * Initialises and adds the fill extrusion layer used by this plugin.
   *
   * @param belowLayer optionally place the buildings layer below a provided layer id
   */
  private void initLayer(String belowLayer) {
    light = style.getLight();
    fillExtrusionLayer = new FillExtrusionLayer(LAYER_ID, "composite");
    fillExtrusionLayer.setSourceLayer("building");
    fillExtrusionLayer.setMinZoom(minZoomLevel);
    fillExtrusionLayer.setProperties(
      visibility(visible ? VISIBLE : NONE),
      fillExtrusionColor(color),
      fillExtrusionHeight(
        interpolate(
          exponential(1f),
          zoom(),
          stop(15, literal(0)),
          stop(16, get("height"))
        )
      ),
      fillExtrusionOpacity(opacity)
    );
    addLayer(fillExtrusionLayer, belowLayer);
  }

  private void addLayer(FillExtrusionLayer fillExtrusionLayer, String belowLayer) {
    if (belowLayer != null && !belowLayer.isEmpty() && style != null) {
      style.addLayerBelow(fillExtrusionLayer, belowLayer);
    } else if (style != null) {
      style.addLayer(fillExtrusionLayer);
    }
  }

  /**
   * Returns true if the traffic plugin is currently enabled.
   *
   * @return true if enabled, false otherwise
   * @since 0.2.0
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Toggles the visibility of the building layer.
   *
   * @param visible true for visible, false for none
   * @since 0.1.0
   */
  public void setVisibility(boolean visible) {
    this.visible = visible;
    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

    fillExtrusionLayer.setProperties(visibility(visible ? VISIBLE : NONE));
  }

  /**
   * Change the building opacity. Calls into changing the fill extrusion fill opacity.
   *
   * @param opacity {@code float} value between 0 (invisible) and 1 (solid)
   * @since 0.1.0
   */
  public void setOpacity(@FloatRange(from = 0.0f, to = 1.0f) float opacity) {
    this.opacity = opacity;
    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

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
    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

    fillExtrusionLayer.setProperties(fillExtrusionColor(color));
  }

  /**
   * Change the building min zoom level. This is the minimum zoom level where buildings will start
   * to show. useful to limit showing buildings at higher zoom levels.
   *
   * @param minZoomLevel a {@code float} value between the maps minimum and maximum zoom level which
   *                     defines at which level the buildings should show up
   * @since 0.1.0
   */
  public void setMinZoomLevel(@FloatRange(from = MINIMUM_ZOOM, to = MAXIMUM_ZOOM)
                                float minZoomLevel) {
    this.minZoomLevel = minZoomLevel;
    if (!style.isFullyLoaded()) {
      // We are in progress of loading a new style
      return;
    }

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


