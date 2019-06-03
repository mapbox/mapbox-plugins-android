package com.mapbox.pluginstylejson

import com.google.gson.JsonObject
import com.mapbox.mapboxsdk.style.layers.*

class LayerJsonParser {

    internal fun parserBackgroundLayer(id: String, json: JsonObject): BackgroundLayer {
        return BackgroundLayer(id)
    }

    internal fun parserCircleLayer(id: String, json: JsonObject): CircleLayer {
        return CircleLayer(id, "")
    }

    internal fun parserCustomLayer(id: String, json: JsonObject): CustomLayer {
        return CustomLayer(id, 0)
    }

    internal fun parserFillExtrusionLayer(id: String, json: JsonObject): FillExtrusionLayer {
        return FillExtrusionLayer(id, "")
    }

    internal fun parserFillLayer(id: String, json: JsonObject): FillLayer {
        return FillLayer(id, "")
    }

    internal fun parserHeatmapLayer(id: String, json: JsonObject): HeatmapLayer {
        return HeatmapLayer(id, "")
    }

    internal fun parserHillshadeLayer(id: String, json: JsonObject): HillshadeLayer {
        return HillshadeLayer(id, "")
    }

    internal fun parserLineLayer(id: String, json: JsonObject): LineLayer {
        return LineLayer(id, "")
    }

    internal fun parserRasterLayer(id: String, json: JsonObject): RasterLayer {
        return RasterLayer(id, "")
    }

    internal fun parserSymbolLayer(id: String, json: JsonObject): SymbolLayer {
        return SymbolLayer(id, "")
    }

}