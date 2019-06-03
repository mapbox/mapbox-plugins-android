package com.mapbox.pluginstylejson

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mapbox.geojson.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.VectorSource
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_FEATURE
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_FEATURE_COLLECTION
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_GEOMETRY_COLLECTION
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_LINE_STRING
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_MULTI_POINT
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_MULTI_POLYGON
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_NULTI_LINE_STRING
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_POINT
import com.mapbox.pluginstylejson.StyleJsonContants.GEO_JSON_POLYGON

class StyleJsonParser {
      private val gson = Gson()

    internal fun parserGeoJsonSource(id: String, json: String): GeoJsonSource {
        val geoJsonSource = GeoJsonSource(id)
        val source = gson.fromJson<JsonObject>(json, JsonObject::class.java)
        if (source.has("data")) {
            val data = source.getAsJsonObject("data")
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

    internal fun parserVectorSource(id: String, json: String): VectorSource {

    }
}
