package com.mapbox.mapboxsdk.plugins.traffic.kotlin

import android.graphics.Color
import android.support.annotation.ColorInt
import android.util.Log
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.functions.CameraFunction
import com.mapbox.mapboxsdk.style.functions.Function
import com.mapbox.mapboxsdk.style.functions.Function.zoom
import com.mapbox.mapboxsdk.style.functions.SourceFunction
import com.mapbox.mapboxsdk.style.functions.stops.Stop
import com.mapbox.mapboxsdk.style.functions.stops.Stop.stop
import com.mapbox.mapboxsdk.style.functions.stops.Stops.categorical
import com.mapbox.mapboxsdk.style.functions.stops.Stops.exponential
import com.mapbox.mapboxsdk.style.layers.Filter
import com.mapbox.mapboxsdk.style.layers.Filter.`in`
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.Source
import com.mapbox.mapboxsdk.style.sources.VectorSource
import timber.log.Timber

class TrafficPluginKt(mapView: MapView, val mapboxMap: MapboxMap) {

    private object TrafficData {
        const val SOURCE_ID = "traffic"
        const val SOURCE_LAYER = "traffic"
        const val SOURCE_URL = "mapbox://mapbox.mapbox-traffic-v1"
    }

    private object TrafficColor {
        val BASE_GREEN = Color.parseColor("#39c66d")
        val CASE_GREEN = Color.parseColor("#059441")
        val BASE_YELLOW = Color.parseColor("#ff8c1a")
        val CASE_YELLOW = Color.parseColor("#d66b00")
        val BASE_ORANGE = Color.parseColor("#ff0015")
        val CASE_ORANGE = Color.parseColor("#bd0010")
        val BASE_RED = Color.parseColor("#981b25")
        val CASE_RED = Color.parseColor("#5f1117")
    }

    private open class TrafficType {
        val FUNCTION_LINE_COLOR = TrafficFunction.obtainLineColorFunction(TrafficColor.BASE_GREEN,
                TrafficColor.BASE_YELLOW, TrafficColor.BASE_ORANGE, TrafficColor.BASE_RED)
        val FUNCTION_LINE_COLOR_CASE = TrafficFunction.obtainLineColorFunction(TrafficColor.CASE_GREEN,
                TrafficColor.CASE_YELLOW, TrafficColor.CASE_ORANGE, TrafficColor.CASE_RED)
    }

    private object MotorWay : TrafficType() {
        const val BASE_LAYER_ID = "traffic-motorway"
        const val CASE_LAYER_ID = "traffic-motorway-bg"
        const val ZOOM_LEVEL = 6.0f
        val FILTER = `in`("class", "motorway")
        val FUNCTION_LINE_WIDTH = TrafficFunction.obtainWidthFunction(
                stop(6, lineWidth(0.5f)),
                stop(9, lineWidth(1.5f)),
                stop(18.0f, lineWidth(14.0f)),
                stop(20.0f, lineWidth(18.0f))
        )
        val FUNCTION_LINE_WIDTH_CASE = TrafficFunction.obtainWidthFunction(
                stop(6, lineWidth(0.5f)),
                stop(9, lineWidth(3.0f)),
                stop(18.0f, lineWidth(16.0f)),
                stop(20.0f, lineWidth(20.0f))
        )
        val FUNCTION_LINE_OFFSET = TrafficFunction.obtainOffsetFunction(
                stop(7, lineOffset(0.0f)),
                stop(9, lineOffset(1.2f)),
                stop(11, lineOffset(1.2f)),
                stop(18, lineOffset(10.0f)),
                stop(20, lineOffset(15.5f))
        )
    }

    private object Trunk : TrafficType() {
        const val BASE_LAYER_ID = "traffic-trunk"
        const val CASE_LAYER_ID = "traffic-trunk-bg"
        const val ZOOM_LEVEL = 6.0f
        val FILTER = `in`("class", "trunk")
        val FUNCTION_LINE_WIDTH = TrafficFunction.obtainWidthFunction(
                stop(8, lineWidth(0.75f)),
                stop(18, lineWidth(11f)),
                stop(20f, lineWidth(15.0f))
        )
        val FUNCTION_LINE_WIDTH_CASE = TrafficFunction.obtainWidthFunction(
                stop(8, lineWidth(0.5f)),
                stop(9, lineWidth(2.25f)),
                stop(18.0f, lineWidth(13.0f)),
                stop(20.0f, lineWidth(17.5f))
        )
        val FUNCTION_LINE_OFFSET = TrafficFunction.obtainOffsetFunction(
                stop(7, lineOffset(0.0f)),
                stop(9, lineOffset(1f)),
                stop(18, lineOffset(13f)),
                stop(20, lineOffset(18.0f))
        )
    }

    private object Primary : TrafficType() {
        const val BASE_LAYER_ID = "traffic-primary"
        const val CASE_LAYER_ID = "traffic-primary-bg"
        const val ZOOM_LEVEL = 6.0f
        val FILTER = `in`("class", "primary")
        val FUNCTION_LINE_WIDTH = TrafficFunction.obtainWidthFunction(
                stop(10, lineWidth(1.0f)),
                stop(15, lineWidth(4.0f)),
                stop(20, lineWidth(16f))
        )
        val FUNCTION_LINE_WIDTH_CASE = TrafficFunction.obtainWidthFunction(
                stop(10, lineWidth(0.75f)),
                stop(15, lineWidth(6f)),
                stop(20.0f, lineWidth(18.0f))
        )
        val FUNCTION_LINE_OFFSET = TrafficFunction.obtainOffsetFunction(
                stop(10, lineOffset(0.0f)),
                stop(12, lineOffset(1.5f)),
                stop(18, lineOffset(13f)),
                stop(20, lineOffset(16.0f))
        )
        val FUNCTION_LINE_OPACITY_CASE = TrafficFunction.obtainOpacityFunction(
                stop(11, lineOpacity(0.0f)),
                stop(12, lineOpacity(1.0f))
        )
    }

    private object Secondary : TrafficType() {
        const val BASE_LAYER_ID = "traffic-secondary-tertiary"
        const val CASE_LAYER_ID = "traffic-secondary-tertiary-bg"
        const val ZOOM_LEVEL = 6.0f
        val FILTER = `in`("class", "secondary", "tertiary")
        val FUNCTION_LINE_WIDTH = TrafficFunction.obtainWidthFunction(
                stop(9, lineWidth(0.5f)),
                stop(18, lineWidth(9.0f)),
                stop(20, lineWidth(14f))
        )
        val FUNCTION_LINE_WIDTH_CASE = TrafficFunction.obtainWidthFunction(
                stop(9, lineWidth(1.5f)),
                stop(18, lineWidth(11f)),
                stop(20.0f, lineWidth(16.5f))
        )
        val FUNCTION_LINE_OFFSET = TrafficFunction.obtainOffsetFunction(
                stop(10, lineOffset(0.5f)),
                stop(15, lineOffset(5f)),
                stop(18, lineOffset(11f)),
                stop(20, lineOffset(14.5f))
        )
        val FUNCTION_LINE_OPACITY_CASE = TrafficFunction.obtainOpacityFunction(
                stop(13, lineOpacity(0.0f)),
                stop(14, lineOpacity(1.0f))
        )
    }

    private object Local : TrafficType() {
        const val BASE_LAYER_ID = "traffic-local"
        const val LOCAL_CASE_LAYER_ID = "traffic-local-case"
        const val ZOOM_LEVEL = 15.0f
        val FILTER = `in`("class", "motorway_link", "service", "street")
        val FUNCTION_LINE_WIDTH = TrafficFunction.obtainWidthFunction(
                stop(14, lineWidth(1.5f)),
                stop(20, lineWidth(13.5f))
        )
        val FUNCTION_LINE_WIDTH_CASE = TrafficFunction.obtainWidthFunction(
                stop(14, lineWidth(2.5f)),
                stop(20, lineWidth(15.5f))
        )
        val FUNCTION_LINE_OFFSET = TrafficFunction.obtainOffsetFunction(
                stop(14, lineOffset(2f)),
                stop(20, lineOffset(18f))
        )
        val FUNCTION_LINE_OPACITY_CASE = TrafficFunction.obtainOpacityFunction(
                stop(15, lineOpacity(0.0f)),
                stop(16, lineOpacity(1.0f))
        )
    }

    private object TrafficFunction {
        fun obtainLineColorFunction(@ColorInt low: Int, @ColorInt moderate: Int,
                                    @ColorInt heavy: Int, @ColorInt severe: Int): SourceFunction<String, String> {
            return Function.property(
                    "congestion",
                    categorical(
                            stop("low", fillColor(low)),
                            stop("moderate", fillColor(moderate)),
                            stop("heavy", fillColor(heavy)),
                            stop("severe", fillColor(severe))
                    )
            ).withDefaultValue(fillColor(Color.TRANSPARENT))
        }

        fun obtainOffsetFunction(vararg stops: Stop<Number, Float>): CameraFunction<out Number, Float> {
            return zoom(exponential(*stops).withBase(1.5f))
        }

        fun obtainWidthFunction(vararg stops: Stop<Number, Float>): CameraFunction<out Number, Float> {
            return zoom(exponential(*stops).withBase(1.5f))
        }

        fun obtainOpacityFunction(vararg stops: Stop<Number, Float>): Function<out Number, Float> {
            return zoom(exponential(*stops))
        }
    }

    private object TrafficLayer {
        fun obtainLineLayer(lineLayerId: String, minZoom: Float, statement: Filter.Statement,
                            lineColor: Function<String, String>, lineWidth: CameraFunction<out Number, Float>,
                            lineOffset: Function<out Number, Float>, lineOpacity: Function<out Number, Float>? = null): LineLayer {
            val lineLayer: LineLayer = LineLayer(lineLayerId, TrafficData.SOURCE_ID)
            lineLayer.minZoom = minZoom
            lineLayer.setFilter(statement)
            lineLayer.setSourceLayer(TrafficData.SOURCE_LAYER)
            lineLayer.setProperties(
                    lineColor(lineColor),
                    lineWidth(lineWidth),
                    lineOffset(lineOffset),
                    lineCap("round"),
                    lineJoin("round")
            )
            if (lineOpacity != null) {
                lineLayer.setProperties(lineOpacity(lineOpacity))
            }
            return lineLayer
        }
    }

    val mapChangedCallback = MapView.OnMapChangedListener { change ->
        if (change == MapView.DID_FINISH_LOADING_STYLE && isEnabled) {
            updateState()
        }
    }
    var isEnabled = false
    val layerIds = mutableListOf<String>()

    init {
        mapView.addOnMapChangedListener(mapChangedCallback)
    }

    fun toggle() {
        isEnabled = !isEnabled
        updateState()
    }

    private fun updateState() {
        val source: Source? = mapboxMap.getSource(TrafficData.SOURCE_ID)
        if (source == null) {
            initialise()
            return
        }
        establishVisibility(isEnabled)
    }

    private fun initialise() {
        addTrafficSource()
        addTrafficLayers()
    }

    private fun addTrafficSource() {
        val trafficSource: VectorSource = VectorSource(TrafficData.SOURCE_ID, TrafficData.SOURCE_URL)
        mapboxMap.addSource(trafficSource)
    }

    private fun addTrafficLayers() {
        try {
            addLocalLayer()
            addSecondaryLayer()
            addPrimaryLayer()
            addTrunkLayer()
            addMotorwayLayer()
        } catch (exception: Exception) {
            Timber.e("Unable to attach Traffic Layers to current style.")
        }
    }

    private fun addLocalLayer() {
        val localCase: LineLayer = TrafficLayer.obtainLineLayer(
                Local.LOCAL_CASE_LAYER_ID,
                Local.ZOOM_LEVEL,
                Local.FILTER,
                Local.FUNCTION_LINE_COLOR_CASE,
                Local.FUNCTION_LINE_WIDTH_CASE,
                Local.FUNCTION_LINE_OFFSET,
                Local.FUNCTION_LINE_OPACITY_CASE
        )
        val local: LineLayer = TrafficLayer.obtainLineLayer(
                Local.BASE_LAYER_ID,
                Local.ZOOM_LEVEL,
                Local.FILTER,
                Local.FUNCTION_LINE_COLOR,
                Local.FUNCTION_LINE_WIDTH,
                Local.FUNCTION_LINE_OFFSET
        )
        addTrafficLayersToMap(localCase, local, "bridge-motorway");
    }

    private fun establishVisibility(visible: Boolean) {
        val layers: List<Layer> = mapboxMap.layers
        for (layer in layers) {
            if (layerIds.contains(layer.id)) {
                layer.setProperties(visibility(if (visible) "visible" else "none"))
            }
        }
    }

    private fun addTrafficLayersToMap(layerCase: Layer, layer: Layer, idAboveLayer: String) {
        mapboxMap.addLayerAbove(layerCase, idAboveLayer)
        mapboxMap.addLayerAbove(layer, layerCase.id)
        layerIds.add(layerCase.id)
        layerIds.add(layer.id)
    }

    private fun addSecondaryLayer() {
        val secondaryCase: LineLayer = TrafficLayer.obtainLineLayer(
                Secondary.CASE_LAYER_ID,
                Secondary.ZOOM_LEVEL,
                Secondary.FILTER,
                Secondary.FUNCTION_LINE_COLOR_CASE,
                Secondary.FUNCTION_LINE_WIDTH_CASE,
                Secondary.FUNCTION_LINE_OFFSET,
                Secondary.FUNCTION_LINE_OPACITY_CASE
        )
        val secondary: LineLayer = TrafficLayer.obtainLineLayer(
                Secondary.BASE_LAYER_ID,
                Secondary.ZOOM_LEVEL,
                Secondary.FILTER,
                Secondary.FUNCTION_LINE_COLOR,
                Secondary.FUNCTION_LINE_WIDTH,
                Secondary.FUNCTION_LINE_OFFSET
        )
        addTrafficLayersToMap(secondaryCase, secondary, obtainLastAddedLayerId())
    }

    private fun obtainLastAddedLayerId(): String {
        return layerIds[layerIds.size - 1]
    }

    private fun addPrimaryLayer() {
        val primaryCase: LineLayer = TrafficLayer.obtainLineLayer(
                Primary.CASE_LAYER_ID,
                Primary.ZOOM_LEVEL,
                Primary.FILTER,
                Primary.FUNCTION_LINE_COLOR_CASE,
                Primary.FUNCTION_LINE_WIDTH_CASE,
                Primary.FUNCTION_LINE_OFFSET,
                Primary.FUNCTION_LINE_OPACITY_CASE
        )
        val primary: LineLayer = TrafficLayer.obtainLineLayer(
                Primary.BASE_LAYER_ID,
                Primary.ZOOM_LEVEL,
                Primary.FILTER,
                Primary.FUNCTION_LINE_COLOR,
                Primary.FUNCTION_LINE_WIDTH,
                Primary.FUNCTION_LINE_OFFSET
        )
        addTrafficLayersToMap(primaryCase, primary, obtainLastAddedLayerId())
    }

    private fun addTrunkLayer() {
        val trunkCase: LineLayer = TrafficLayer.obtainLineLayer(
                Trunk.CASE_LAYER_ID,
                Trunk.ZOOM_LEVEL,
                Trunk.FILTER,
                Trunk.FUNCTION_LINE_COLOR_CASE,
                Trunk.FUNCTION_LINE_WIDTH_CASE,
                Trunk.FUNCTION_LINE_OFFSET
        )
        val trunk: LineLayer = TrafficLayer.obtainLineLayer(
                Trunk.BASE_LAYER_ID,
                Trunk.ZOOM_LEVEL,
                Trunk.FILTER,
                Trunk.FUNCTION_LINE_COLOR,
                Trunk.FUNCTION_LINE_WIDTH,
                Trunk.FUNCTION_LINE_OFFSET
        )
        addTrafficLayersToMap(trunkCase, trunk, obtainLastAddedLayerId())
    }

    private fun addMotorwayLayer() {
        val motorwayCase: LineLayer = TrafficLayer.obtainLineLayer(
                MotorWay.CASE_LAYER_ID,
                MotorWay.ZOOM_LEVEL,
                MotorWay.FILTER,
                MotorWay.FUNCTION_LINE_COLOR_CASE,
                MotorWay.FUNCTION_LINE_WIDTH_CASE,
                MotorWay.FUNCTION_LINE_OFFSET
        )
        val motorway: LineLayer = TrafficLayer.obtainLineLayer(
                MotorWay.BASE_LAYER_ID,
                MotorWay.ZOOM_LEVEL,
                MotorWay.FILTER,
                MotorWay.FUNCTION_LINE_COLOR,
                MotorWay.FUNCTION_LINE_WIDTH,
                MotorWay.FUNCTION_LINE_OFFSET
        )
        addTrafficLayersToMap(motorwayCase, motorway, obtainLastAddedLayerId())
    }
}

