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
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The line manager allows to add lines to a map.
 */
public class LineManager extends AnnotationManager<LineLayer, Line, LineOptions, OnLineDragListener, OnLineClickListener, OnLineLongClickListener> {

  private static final String PROPERTY_LINE_CAP = "line-cap";
  private static final String PROPERTY_LINE_MITER_LIMIT = "line-miter-limit";
  private static final String PROPERTY_LINE_ROUND_LIMIT = "line-round-limit";
  private static final String PROPERTY_LINE_TRANSLATE = "line-translate";
  private static final String PROPERTY_LINE_TRANSLATE_ANCHOR = "line-translate-anchor";
  private static final String PROPERTY_LINE_DASHARRAY = "line-dasharray";

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap the map object to add lines to
   * @param style a valid a fully loaded style object
   */
  @UiThread
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style) {
    this(mapView, mapboxMap, style, null, null);
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
    this(mapView, mapboxMap, style, belowLayerId, null);
  }

  /**
   * Create a line manager, used to manage lines.
   *
   * @param mapboxMap the map object to add lines to
   * @param style a valid a fully loaded style object
   * @param belowLayerId the id of the layer above the circle layer
   * @param geoJsonOptions options for the internal source
   */
  @UiThread
  public LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions) {
    this(mapView, mapboxMap, style, new LineElementProvider(), belowLayerId, geoJsonOptions, new DraggableAnnotationController<Line, OnLineDragListener>(mapView, mapboxMap));
  }

  @VisibleForTesting
  LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<LineLayer> coreElementProvider, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions, DraggableAnnotationController<Line, OnLineDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId, geoJsonOptions);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_JOIN, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_OPACITY, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_COLOR, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_WIDTH, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_GAP_WIDTH, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_OFFSET, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_BLUR, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_LINE_PATTERN, false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case LineOptions.PROPERTY_LINE_JOIN:
        layer.setProperties(lineJoin(get(LineOptions.PROPERTY_LINE_JOIN)));
        break;
      case LineOptions.PROPERTY_LINE_OPACITY:
        layer.setProperties(lineOpacity(get(LineOptions.PROPERTY_LINE_OPACITY)));
        break;
      case LineOptions.PROPERTY_LINE_COLOR:
        layer.setProperties(lineColor(get(LineOptions.PROPERTY_LINE_COLOR)));
        break;
      case LineOptions.PROPERTY_LINE_WIDTH:
        layer.setProperties(lineWidth(get(LineOptions.PROPERTY_LINE_WIDTH)));
        break;
      case LineOptions.PROPERTY_LINE_GAP_WIDTH:
        layer.setProperties(lineGapWidth(get(LineOptions.PROPERTY_LINE_GAP_WIDTH)));
        break;
      case LineOptions.PROPERTY_LINE_OFFSET:
        layer.setProperties(lineOffset(get(LineOptions.PROPERTY_LINE_OFFSET)));
        break;
      case LineOptions.PROPERTY_LINE_BLUR:
        layer.setProperties(lineBlur(get(LineOptions.PROPERTY_LINE_BLUR)));
        break;
      case LineOptions.PROPERTY_LINE_PATTERN:
        layer.setProperties(linePattern(get(LineOptions.PROPERTY_LINE_PATTERN)));
        break;
    }
  }

  /**
   * Create a list of lines on the map.
   * <p>
   * Lines are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * LineOptions.PROPERTY_LINE_JOIN - String<br>
   * LineOptions.PROPERTY_LINE_OPACITY - Float<br>
   * LineOptions.PROPERTY_LINE_COLOR - String<br>
   * LineOptions.PROPERTY_LINE_WIDTH - Float<br>
   * LineOptions.PROPERTY_LINE_GAP_WIDTH - Float<br>
   * LineOptions.PROPERTY_LINE_OFFSET - Float<br>
   * LineOptions.PROPERTY_LINE_BLUR - Float<br>
   * LineOptions.PROPERTY_LINE_PATTERN - String<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the line should be draggable, false otherwise
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
   * All supported properties are:<br>
   * LineOptions.PROPERTY_LINE_JOIN - String<br>
   * LineOptions.PROPERTY_LINE_OPACITY - Float<br>
   * LineOptions.PROPERTY_LINE_COLOR - String<br>
   * LineOptions.PROPERTY_LINE_WIDTH - Float<br>
   * LineOptions.PROPERTY_LINE_GAP_WIDTH - Float<br>
   * LineOptions.PROPERTY_LINE_OFFSET - Float<br>
   * LineOptions.PROPERTY_LINE_BLUR - Float<br>
   * LineOptions.PROPERTY_LINE_PATTERN - String<br>
   * Learn more about above properties in the <a href="https://www.mapbox.com/mapbox-gl-js/style-spec/">Style specification</a>.
   * <p>
   * Out of spec properties:<br>
   * "is-draggable" - Boolean, true if the line should be draggable, false otherwise
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
    constantPropertyUsageMap.put(PROPERTY_LINE_CAP, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_LINE_MITER_LIMIT, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_LINE_ROUND_LIMIT, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_LINE_TRANSLATE, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_LINE_TRANSLATE_ANCHOR, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_LINE_DASHARRAY, propertyValue);
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
