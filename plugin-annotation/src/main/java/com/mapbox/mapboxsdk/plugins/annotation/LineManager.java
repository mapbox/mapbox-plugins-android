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
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.layers.Property;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The line manager allows to add lines to a map.
 */
public class LineManager extends AnnotationManager<LineLayer, Line, LineOptions, OnLineDragListener, OnLineClickListener, OnLineLongClickListener> {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-line-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-line-layer";

  private static final String PROPERTY_lineCap = "line-cap";
  private static final String PROPERTY_lineMiterLimit = "line-miter-limit";
  private static final String PROPERTY_lineRoundLimit = "line-round-limit";
  private static final String PROPERTY_lineTranslate = "line-translate";
  private static final String PROPERTY_lineTranslateAnchor = "line-translate-anchor";
  private static final String PROPERTY_lineDasharray = "line-dasharray";

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
    this(mapView, mapboxMap, style,
      new CoreElementProvider<LineLayer>() {
        @Override
        public LineLayer getLayer() {
          return new LineLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE);
        }

        @Override
        public GeoJsonSource getSource(@Nullable GeoJsonOptions geoJsonOptions) {
          if (geoJsonOptions != null) {
            return new GeoJsonSource(ID_GEOJSON_SOURCE, geoJsonOptions);
          } else {
            return new GeoJsonSource(ID_GEOJSON_SOURCE);
          }
        }
      },
     belowLayerId, geoJsonOptions, new DraggableAnnotationController<>(mapView, mapboxMap));
  }

  @VisibleForTesting
  LineManager(@NonNull MapView mapView, @NonNull MapboxMap mapboxMap, @NonNull Style style, @NonNull CoreElementProvider<LineLayer> coreElementProvider, @Nullable String belowLayerId, @Nullable GeoJsonOptions geoJsonOptions, DraggableAnnotationController<Line, OnLineDragListener> draggableAnnotationController) {
    super(mapView, mapboxMap, style, coreElementProvider, null, draggableAnnotationController, belowLayerId, geoJsonOptions);
  }

  @Override
  void initializeDataDrivenPropertyMap() {
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineJoin, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineOpacity, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineColor, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineWidth, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineGapWidth, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineOffset, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_lineBlur, false);
    dataDrivenPropertyUsageMap.put(LineOptions.PROPERTY_linePattern, false);
  }

  @Override
  protected void setDataDrivenPropertyIsUsed(@NonNull String property) {
    switch (property) {
      case LineOptions.PROPERTY_lineJoin:
        layer.setProperties(lineJoin(get(LineOptions.PROPERTY_lineJoin)));
        break;
      case LineOptions.PROPERTY_lineOpacity:
        layer.setProperties(lineOpacity(get(LineOptions.PROPERTY_lineOpacity)));
        break;
      case LineOptions.PROPERTY_lineColor:
        layer.setProperties(lineColor(get(LineOptions.PROPERTY_lineColor)));
        break;
      case LineOptions.PROPERTY_lineWidth:
        layer.setProperties(lineWidth(get(LineOptions.PROPERTY_lineWidth)));
        break;
      case LineOptions.PROPERTY_lineGapWidth:
        layer.setProperties(lineGapWidth(get(LineOptions.PROPERTY_lineGapWidth)));
        break;
      case LineOptions.PROPERTY_lineOffset:
        layer.setProperties(lineOffset(get(LineOptions.PROPERTY_lineOffset)));
        break;
      case LineOptions.PROPERTY_lineBlur:
        layer.setProperties(lineBlur(get(LineOptions.PROPERTY_lineBlur)));
        break;
      case LineOptions.PROPERTY_linePattern:
        layer.setProperties(linePattern(get(LineOptions.PROPERTY_linePattern)));
        break;
    }
  }

  /**
   * Create a list of lines on the map.
   * <p>
   * Lines are going to be created only for features with a matching geometry.
   * <p>
   * All supported properties are:<br>
   * LineOptions.PROPERTY_lineJoin - String<br>
   * LineOptions.PROPERTY_lineOpacity - Float<br>
   * LineOptions.PROPERTY_lineColor - String<br>
   * LineOptions.PROPERTY_lineWidth - Float<br>
   * LineOptions.PROPERTY_lineGapWidth - Float<br>
   * LineOptions.PROPERTY_lineOffset - Float<br>
   * LineOptions.PROPERTY_lineBlur - Float<br>
   * LineOptions.PROPERTY_linePattern - String<br>
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
   * LineOptions.PROPERTY_lineJoin - String<br>
   * LineOptions.PROPERTY_lineOpacity - Float<br>
   * LineOptions.PROPERTY_lineColor - String<br>
   * LineOptions.PROPERTY_lineWidth - Float<br>
   * LineOptions.PROPERTY_lineGapWidth - Float<br>
   * LineOptions.PROPERTY_lineOffset - Float<br>
   * LineOptions.PROPERTY_lineBlur - Float<br>
   * LineOptions.PROPERTY_linePattern - String<br>
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
    constantPropertyUsageMap.put(PROPERTY_lineCap, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_lineMiterLimit, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_lineRoundLimit, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_lineTranslate, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_lineTranslateAnchor, propertyValue);
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
    constantPropertyUsageMap.put(PROPERTY_lineDasharray, propertyValue);
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
