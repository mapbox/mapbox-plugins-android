package com.mapbox.mapboxsdk.maps

import android.graphics.PointF

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.maps.fromScreenLocation
import com.mapbox.mapboxsdk.plugins.maps.toScreenLocation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Projection extension function tests")
class ProjectionExtensionFunctionTest {

    private val annotationManager: AnnotationManager = mockk()
    private val projection: Projection = mockk()
    private val mapboxMap: MapboxMap

    init {
        every { annotationManager.bind(any()) } answers { annotationManager }
        every { projection.toScreenLocation(any()) } answers { PointF() }
        every { projection.fromScreenLocation(any()) } answers { LatLng() }
        mapboxMap = MapboxMap(null, null, null, projection, null, annotationManager, null)
    }

    @Test
    fun `to screen location`() {
        val latLng = LatLng()
        mapboxMap.toScreenLocation(latLng)
        verify(exactly = 1) { projection.toScreenLocation(latLng) }
    }

    @Test
    fun `from screen location`() {
        val pointF = PointF()
        mapboxMap.fromScreenLocation(pointF)
        verify(exactly = 1) { projection.fromScreenLocation(pointF) }
    }
}