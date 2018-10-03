package com.mapbox.mapboxsdk.maps

import android.graphics.PointF
import android.graphics.RectF

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.plugins.maps.queryRenderedFeatures
import com.mapbox.mapboxsdk.plugins.maps.toScreenLocation
import io.mockk.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Query extension function tests")
class QueryExtensionFunctionTest {

    private val annotationManager: AnnotationManager = mockk()
    private val nativeMapView: NativeMapView = mockk()
    private val projection: Projection = mockk()
    private val mapboxMap: MapboxMap
    private val pointF: PointF = PointF()
    private val rectF: RectF =  RectF()

    init {
        mockkStatic("com.mapbox.mapboxsdk.plugins.maps.ProjectionKt")
        every { Projection(nativeMapView).toScreenLocation(any(), any()) } returns rectF
        every { annotationManager.bind(any()) } answers { annotationManager }
        every { projection.toScreenLocation(any()) } answers { pointF }
        every { projection.toScreenLocation(pointF, pointF) } answers { rectF }
        every { nativeMapView.queryRenderedFeatures(any<PointF>(), any(), any()) } answers { listOf() }
        every { nativeMapView.queryRenderedFeatures(any<RectF>(), any(), any()) } answers { listOf() }
        mapboxMap = MapboxMap(nativeMapView, null, null, projection, null, annotationManager, null)
    }

    @Test
    fun `query with LatLng`() {
        val latLng = LatLng()
        mapboxMap.queryRenderedFeatures(latLng, "test")
        verify(exactly = 1) { nativeMapView.queryRenderedFeatures(pointF, arrayOf("test"), null) }
    }

    @Test
    fun `query with LatLngBounds`() {
        val latLngBounds = LatLngBounds.from(0.0, 0.0, 0.0, 0.0)
        mapboxMap.queryRenderedFeatures(latLngBounds, "test")
        verify(exactly = 1) { nativeMapView.queryRenderedFeatures(rectF, arrayOf("test"), null) }
    }
}