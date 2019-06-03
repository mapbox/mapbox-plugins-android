package com.mapbox.pluginstylejson

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.sources.Source
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_BACKGROUND
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_CUSTOM
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_CYCLE
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_FILL
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_FILL_EXTRUSION
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_HEATMAP
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_HILLSHADE
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_LINE
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_RASTER
import com.mapbox.pluginstylejson.StyleJsonContants.LAYER_SYMBOL
import com.mapbox.pluginstylejson.StyleJsonContants.SOURCE_CUSTOM_GEOMETRY
import com.mapbox.pluginstylejson.StyleJsonContants.SOURCE_GEO_JSON
import com.mapbox.pluginstylejson.StyleJsonContants.SOURCE_IMAGE
import com.mapbox.pluginstylejson.StyleJsonContants.SOURCE_RASTER
import com.mapbox.pluginstylejson.StyleJsonContants.SOURCE_RASTER_DEM
import com.mapbox.pluginstylejson.StyleJsonContants.SOURCE_VECTOR

class StyleJsonPlugin {
    private val gson = Gson()
    private val layerJsonParser = LayerJsonParser()
    private val sourceJsonParser = SourceJsonParser()

    fun parserLayer(content: String): Layer? {
        val source = gson.fromJson<JsonObject>(content, JsonObject::class.java)
        val id = source.get("id")
        id?.let {
            val type = source.get("type")
            type?.let {
                return when (type.asString) {
                    LAYER_BACKGROUND -> layerJsonParser.parserBackgroundLayer(id.asString, source)
                    LAYER_CYCLE -> layerJsonParser.parserCircleLayer(id.asString, source)
                    LAYER_CUSTOM -> layerJsonParser.parserCustomLayer(id.asString, source)
                    LAYER_FILL_EXTRUSION -> layerJsonParser.parserFillExtrusionLayer(id.asString, source)
                    LAYER_FILL -> layerJsonParser.parserFillLayer(id.asString, source)
                    LAYER_HEATMAP -> layerJsonParser.parserHeatmapLayer(id.asString, source)
                    LAYER_HILLSHADE -> layerJsonParser.parserHillshadeLayer(id.asString, source)
                    LAYER_LINE -> layerJsonParser.parserLineLayer(id.asString, source)
                    LAYER_RASTER -> layerJsonParser.parserRasterLayer(id.asString, source)
                    LAYER_SYMBOL -> layerJsonParser.parserSymbolLayer(id.asString, source)
                    else -> null
                }
            }
        }
        return null
    }

    fun parserSource(id: String, content: String): Source? {
        val source = gson.fromJson<JsonObject>(content, JsonObject::class.java)
        val type = source.get("type")
        type?.let {
            return when (type.asString) {
                SOURCE_CUSTOM_GEOMETRY -> sourceJsonParser.parserCustomGeometrySource(id, source)
                SOURCE_GEO_JSON -> sourceJsonParser.parserGeoJsonSource(id, source)
                SOURCE_IMAGE -> sourceJsonParser.parserImageSource(id, source)
                SOURCE_RASTER_DEM -> sourceJsonParser.parserRasterDemSource(id, source)
                SOURCE_RASTER -> sourceJsonParser.parserRasterSource(id, source)
                SOURCE_VECTOR -> sourceJsonParser.parserVectorSource(id, source)
                else -> null
            }
        }
        return null
    }

}