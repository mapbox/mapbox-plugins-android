package com.mapbox.pluginstylejson

import com.google.gson.JsonObject
import com.mapbox.geojson.*
import com.mapbox.mapboxsdk.style.sources.*
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_FEATURE
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_FEATURE_COLLECTION
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_GEOMETRY_COLLECTION
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_LINE_STRING
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_MULTI_POINT
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_MULTI_POLYGON
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_NULTI_LINE_STRING
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_POINT
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_POLYGON

class SourceJsonParser {

    internal fun parserGeoJsonSource(id: String, json: JsonObject): GeoJsonSource {
        val geoJsonSource = GeoJsonSource(id)
        if (json.has("data")) {
            val data = json.getAsJsonObject("data")
            val dataString = data.asString
            if (dataString.startsWith("http")) {
                geoJsonSource.url = dataString
            } else {
                val type = data.get("type").asString
                when (type) {
                    GEO_JSON_FEATURE -> geoJsonSource.setGeoJson(Feature.fromJson(dataString))
                    GEO_JSON_FEATURE_COLLECTION -> geoJsonSource.setGeoJson(FeatureCollection.fromJson(dataString))
                    GEO_JSON_GEOMETRY_COLLECTION -> geoJsonSource.setGeoJson(GeometryCollection.fromJson(dataString))
                    GEO_JSON_LINE_STRING -> geoJsonSource.setGeoJson(LineString.fromJson(dataString))
                    GEO_JSON_NULTI_LINE_STRING -> geoJsonSource.setGeoJson(MultiLineString.fromJson(dataString))
                    GEO_JSON_MULTI_POINT -> geoJsonSource.setGeoJson(MultiPoint.fromJson(dataString))
                    GEO_JSON_MULTI_POLYGON -> geoJsonSource.setGeoJson(MultiPolygon.fromJson(dataString))
                    GEO_JSON_POINT -> geoJsonSource.setGeoJson(Point.fromJson(dataString))
                    GEO_JSON_POLYGON -> geoJsonSource.setGeoJson(Polygon.fromJson(dataString))
                }
            }
        }

        return geoJsonSource
    }

    internal fun parserVectorSource(id: String, json: JsonObject): VectorSource {
        return VectorSource(id, "")
    }

    internal fun parserCustomGeometrySource(id: String, json: JsonObject): CustomGeometrySource {
        return CustomGeometrySource(id, null)
    }

    internal fun parserImageSource(id: String, json: JsonObject): ImageSource {
        return ImageSource(id, null, 0)
    }

    internal fun parserRasterDemSource(id: String, json: JsonObject): RasterDemSource {
        return RasterDemSource(id, "")
    }

    internal fun parserRasterSource(id: String, json: JsonObject): RasterSource {
        return RasterSource(id, "")
    }
}
