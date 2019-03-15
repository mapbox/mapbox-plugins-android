// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.plugins.annotation.Symbol.Z_INDEX;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The line manager allows to add lines to a map.
 */
public class LineManager extends AnnotationManager<LineLayer, Line, LineOptions, OnLineDragListener, OnLineClickListener, OnLineLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-line-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-line-layer";

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap the map object to add lines to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null);
  }

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap the map object to add lines to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   */
  @UiThread
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId) {
    this(mapView, mapboxMap, style,
      new CoreElementProvider<LineLayer>() {
        @Override
        public LineLayer getLayer() {
          return new LineLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE);
        }

        @Override
        public GeoJsonSource getSource() {
          return new GeoJsonSource(ID_GEOJSON_SOURCE);
        }
      },
     belowLayerId, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap     the map object to add lines to
   * @param style a valid a fully loaded style object
   */
  @VisibleForTesting
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<LineLayer> coreElementProvider, @Nullable String belowLayerId, DraggableAnnotationController<Line, OnLineDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put("line-join", false);
    dataDrivenPropertyUsageMap.put("line-opacity", false);
    dataDrivenPropertyUsageMap.put("line-color", false);
    dataDrivenPropertyUsageMap.put("line-width", false);
    dataDrivenPropertyUsageMap.put("line-gap-width", false);
    dataDrivenPropertyUsageMap.put("line-offset", false);
    dataDrivenPropertyUsageMap.put("line-blur", false);
    dataDrivenPropertyUsageMap.put("line-pattern", false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case "line-join":
        layer.setProperties(lineJoin(get("line-join")));
        break;
      case "line-opacity":
        layer.setProperties(lineOpacity(get("line-opacity")));
        break;
      case "line-color":
        layer.setProperties(lineColor(get("line-color")));
        break;
      case "line-width":
        layer.setProperties(lineWidth(get("line-width")));
        break;
      case "line-gap-width":
        layer.setProperties(lineGapWidth(get("line-gap-width")));
        break;
      case "line-offset":
        layer.setProperties(lineOffset(get("line-offset")));
        break;
      case "line-blur":
        layer.setProperties(lineBlur(get("line-blur")));
        break;
      case "line-pattern":
        layer.setProperties(linePattern(get("line-pattern")));
        break;
    }
  }

  /**
   * Create a list of lines on the map.
   * <p>
   * Lines are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link LineOptions#fromFeature(Feature)}.
   *
   * @param json the GeoJSON defining the list of lines to build
   * @return the list of built lines
   */
  @UiThread
  public List<Line> create(@NonNull String json) {
    return create(FeatureCollection.fromJson(json));
  }

  /**
   * Create a list of lines on the map.
   * <p>
   * Lines are going to be created only for features with a matching geometry.
   * <p>
   * You can inspect a full list of supported feature properties in {@link LineOptions#fromFeature(Feature)}.
   *
   * @param featureCollection the featureCollection defining the list of lines to build
   * @return the list of built lines
   */
  @UiThread
  public List<Line> create(@NonNull FeatureCollection featureCollection) {
    List<Feature> features = featureCollection.features();
    List<LineOptions> options = new ArrayList<>();
    if (features != null) {
      for (Feature feature : features) {
        LineOptions option = LineOptions.fromFeature(feature);
        if (option != null) {
          options.add(option);
        }
      }
    }
    return create(options);
  }

  /**
   * Get the layer id of the annotation layer.
   *
   * @return the layer id
   */
  @Override
  String getAnnotationLayerId() {
    return ID_GEOJSON_LAYER;
  }

  /**
   * Get the key of the id of the annotation.
   *
   * @return the key of the id of the annotation
   */
  @Override
  String getAnnotationIdKey() {
    return Line.ID_KEY;
  }

  // Property accessors
  /**
   * Get the LineCap property
   * <p>
   * The display of line endings.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getLineCap() {
    return layer.getLineCap().value;
  }

  /**
   * Set the LineCap property
   * <p>
   * The display of line endings.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setLineCap(@Property.LINE_CAP String value) {
    PropertyValue propertyValue = lineCap(value);
    constantPropertyUsageMap.put("line-cap", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the LineMiterLimit property
   * <p>
   * Used to automatically convert miter joins to bevel joins for sharp angles.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineMiterLimit() {
    return layer.getLineMiterLimit().value;
  }

  /**
   * Set the LineMiterLimit property
   * <p>
   * Used to automatically convert miter joins to bevel joins for sharp angles.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setLineMiterLimit( Float value) {
    PropertyValue propertyValue = lineMiterLimit(value);
    constantPropertyUsageMap.put("line-miter-limit", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the LineRoundLimit property
   * <p>
   * Used to automatically convert round joins to miter joins for shallow angles.
   * </p>
   *
   * @return property wrapper value around Float
   */
  public Float getLineRoundLimit() {
    return layer.getLineRoundLimit().value;
  }

  /**
   * Set the LineRoundLimit property
   * <p>
   * Used to automatically convert round joins to miter joins for shallow angles.
   * </p>
   *
   * @param value property wrapper value around Float
   */
  public void setLineRoundLimit( Float value) {
    PropertyValue propertyValue = lineRoundLimit(value);
    constantPropertyUsageMap.put("line-round-limit", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the LineTranslate property
   * <p>
   * The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getLineTranslate() {
    return layer.getLineTranslate().value;
  }

  /**
   * Set the LineTranslate property
   * <p>
   * The geometry's offset. Values are [x, y] where negatives indicate left and up, respectively.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setLineTranslate( Float[] value) {
    PropertyValue propertyValue = lineTranslate(value);
    constantPropertyUsageMap.put("line-translate", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the LineTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#lineTranslate}.
   * </p>
   *
   * @return property wrapper value around String
   */
  public String getLineTranslateAnchor() {
    return layer.getLineTranslateAnchor().value;
  }

  /**
   * Set the LineTranslateAnchor property
   * <p>
   * Controls the frame of reference for {@link PropertyFactory#lineTranslate}.
   * </p>
   *
   * @param value property wrapper value around String
   */
  public void setLineTranslateAnchor(@Property.LINE_TRANSLATE_ANCHOR String value) {
    PropertyValue propertyValue = lineTranslateAnchor(value);
    constantPropertyUsageMap.put("line-translate-anchor", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Get the LineDasharray property
   * <p>
   * Specifies the lengths of the alternating dashes and gaps that form the dash pattern. The lengths are later scaled by the line width. To convert a dash length to density-independent pixels, multiply the length by the current line width. Note that GeoJSON sources with `lineMetrics: true` specified won't render dashed lines to the expected scale. Also note that zoom-dependent expressions will be evaluated only at integer zoom levels.
   * </p>
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getLineDasharray() {
    return layer.getLineDasharray().value;
  }

  /**
   * Set the LineDasharray property
   * <p>
   * Specifies the lengths of the alternating dashes and gaps that form the dash pattern. The lengths are later scaled by the line width. To convert a dash length to density-independent pixels, multiply the length by the current line width. Note that GeoJSON sources with `lineMetrics: true` specified won't render dashed lines to the expected scale. Also note that zoom-dependent expressions will be evaluated only at integer zoom levels.
   * </p>
   *
   * @param value property wrapper value around Float[]
   */
  public void setLineDasharray( Float[] value) {
    PropertyValue propertyValue = lineDasharray(value);
    constantPropertyUsageMap.put("line-dasharray", propertyValue);
    layer.setProperties(propertyValue);
  }

  /**
   * Set filter on the managed lines.
   *
   * @param expression expression
   */
   @Override
  public void setFilter(@NonNull Expression expression) {
    layerFilter = expression;
    layer.setFilter(layerFilter);
  }

  /**
   * Get filter of the managed lines.
   *
   * @return expression
   */
  @Nullable
  public Expression getFilter() {
    return layer.getFilter();
  }
}
