package com.mapbox.mapboxsdk.maps

import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.maps.toLatLng
import com.mapbox.mapboxsdk.plugins.maps.toPoint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Geometry extension function tests")
class GeometryExtensionFunctionTest {

    @Nested
    @DisplayName("Point data")
    internal inner class PointDataTests {

        @Test
        fun `LatLng to Point`() {
            val expectedPoint = Point.fromLngLat(25.0, 10.0)
            val latLng = LatLng(10.0, 25.0)
            assertThat(latLng.toPoint()).isEqualTo(expectedPoint)
        }

        @Test
        fun `Point to LatLng`() {
            val expectedLatLng = LatLng(25.0, 10.0)
            val point = Point.fromLngLat(10.0, 25.0)
            assertThat(point.toLatLng()).isEqualTo(expectedLatLng)
        }
    }
}