package com.mapbox.pluginscalebar

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Projection
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ScaleBarPluginTest {
  @MockK
  lateinit var mapView: MapView

  @MockK
  lateinit var projection: Projection

  @MockK
  lateinit var mapboxMap: MapboxMap

  @MockK
  lateinit var scaleBarOptions: ScaleBarOptions

  @MockK
  lateinit var scaleBarWidget: ScaleBarWidget

  @MockK
  lateinit var context: Context

  @MockK
  lateinit var resources: Resources
  @MockK
  lateinit var displayMetrics: DisplayMetrics
  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxUnitFun = true)
    displayMetrics.density = 2f
    every { mapView.width } returns 1000
    every { projection.getMetersPerPixelAtLatitude(CameraPosition.DEFAULT.target.latitude) } returns 100_000.0
    every { mapboxMap.projection } returns projection
    every { mapboxMap.cameraPosition } returns CameraPosition.DEFAULT
    every { scaleBarOptions.build() } returns scaleBarWidget
    every { mapView.context} returns context
    every { mapView.pixelRatio } returns 2f
    every { context.resources} returns resources
    every { resources.displayMetrics} returns displayMetrics
  }

  @Test
  fun sanity() {
    assertNotNull(mapView)
    assertNotNull(mapboxMap)
    assertNotNull(scaleBarOptions)
    assertNotNull(scaleBarWidget)
  }

  @Test
  fun create_isEnabled() {
    val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
    scaleBarPlugin.create(scaleBarOptions)

    assertTrue(scaleBarPlugin.isEnabled)
    verify { scaleBarWidget.visibility = View.VISIBLE }
    verify(exactly = 1) { mapboxMap.addOnCameraMoveListener(scaleBarPlugin.cameraMoveListener) }
    verify(exactly = 1) { mapboxMap.addOnCameraIdleListener(scaleBarPlugin.cameraIdleListener) }
  }

  @Test
  fun disable() {
    val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
    scaleBarPlugin.create(scaleBarOptions)
    scaleBarPlugin.isEnabled = false

    assertFalse(scaleBarPlugin.isEnabled)
    verify { scaleBarWidget.visibility = View.GONE }
    verify { mapboxMap.removeOnCameraMoveListener(scaleBarPlugin.cameraMoveListener) }
    verify { mapboxMap.removeOnCameraIdleListener(scaleBarPlugin.cameraIdleListener) }
  }

  @Test
  fun enable() {
    val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
    scaleBarPlugin.create(scaleBarOptions)
    verify(exactly = 1) { mapboxMap.addOnCameraMoveListener(scaleBarPlugin.cameraMoveListener) }
    verify(exactly = 1) { mapboxMap.addOnCameraIdleListener(scaleBarPlugin.cameraIdleListener) }
    verify(exactly = 1) { scaleBarWidget.visibility = View.VISIBLE }
    scaleBarPlugin.isEnabled = false
    scaleBarPlugin.isEnabled = true

    assertTrue(scaleBarPlugin.isEnabled)
    verify(exactly = 2) { scaleBarWidget.visibility = View.VISIBLE }
    verify(exactly = 2) { mapboxMap.addOnCameraMoveListener(scaleBarPlugin.cameraMoveListener) }
    verify(exactly = 2) { mapboxMap.addOnCameraIdleListener(scaleBarPlugin.cameraIdleListener) }
  }

  @Test
  fun disable_enable_widgetIsNull() {
    val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
    scaleBarPlugin.isEnabled = false
    scaleBarPlugin.isEnabled = true

    verify(exactly = 0) { mapboxMap.addOnCameraMoveListener(scaleBarPlugin.cameraMoveListener) }
    verify(exactly = 0) { mapboxMap.addOnCameraIdleListener(scaleBarPlugin.cameraIdleListener) }
  }

  @Test
  fun disableBeforeCreate_ignoreResults() {
    val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
    scaleBarPlugin.isEnabled = false
    scaleBarPlugin.create(scaleBarOptions)

    assertTrue(scaleBarPlugin.isEnabled)
    verify { scaleBarWidget.visibility = View.VISIBLE }
    verify(exactly = 1) { mapboxMap.addOnCameraMoveListener(scaleBarPlugin.cameraMoveListener) }
    verify(exactly = 1) { mapboxMap.addOnCameraIdleListener(scaleBarPlugin.cameraIdleListener) }
  }

  @Test
  fun toggled_invalidateWidget() {
    val scaleBarPlugin = ScaleBarPlugin(mapView, mapboxMap)
    scaleBarPlugin.create(scaleBarOptions)
    verify(exactly = 1) { mapboxMap.cameraPosition }
    verify(exactly = 1) { scaleBarWidget.setDistancePerPixel(50_000.0) }
    scaleBarPlugin.isEnabled = false
    scaleBarPlugin.isEnabled = true

    verify(exactly = 2) { mapboxMap.cameraPosition }
    verify(exactly = 2) { scaleBarWidget.setDistancePerPixel(50_000.0) }
  }
}