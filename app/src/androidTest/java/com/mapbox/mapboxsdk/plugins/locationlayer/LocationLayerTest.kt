package com.mapbox.mapboxsdk.plugins.locationlayer

import android.Manifest
import android.content.Context
import android.location.Location
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.rule.GrantPermissionRule.grant
import android.support.test.runner.AndroidJUnit4
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.*
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.activity.SingleActivity
import com.mapbox.mapboxsdk.plugins.utils.GenericPluginAction
import com.mapbox.mapboxsdk.plugins.utils.OnMapReadyIdlingResource
import com.mapbox.mapboxsdk.plugins.utils.PluginGenerationUtil
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
@LargeTest
class LocationLayerTest {

  @Rule
  @JvmField
  val rule = ActivityTestRule(SingleActivity::class.java)

  @Rule
  @JvmField
  val permissionRule: GrantPermissionRule = grant(Manifest.permission.ACCESS_FINE_LOCATION)

  private lateinit var idlingResource: OnMapReadyIdlingResource
  private lateinit var mapboxMap: MapboxMap
  private val location: Location by lazy {
    val initLocation = Location("test")
    initLocation.latitude = 15.0
    initLocation.longitude = 17.0
    initLocation
  }

  @Before
  fun beforeTest() {
    Timber.e("@Before: register idle resource")
    // If idlingResource is null, throw Kotlin exception
    idlingResource = OnMapReadyIdlingResource(rule.activity)
    IdlingRegistry.getInstance().register(idlingResource)
    onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    mapboxMap = idlingResource.mapboxMap
  }

  //
  // Location Source
  //

  @Test
  fun renderModeNormal_sourceDoesGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.NORMAL
        assertThat(mapboxMap.getSource(LOCATION_SOURCE), notNullValue())
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // Location Layers
  //

  @Test
  fun renderModeNormal_trackingNormalLayersDoGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.NORMAL
        assertThat(isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(SHADOW_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(ACCURACY_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
      }
    }
    executePluginTest(pluginAction)
  }

  private fun isLayerVisible(layerId: String): Boolean {
    return mapboxMap.getLayer(layerId)?.visibility?.value?.equals(Property.VISIBLE)!!
  }

  @Test
  fun renderModeCompass_bearingLayersDoGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.COMPASS
        assertThat(isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(SHADOW_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(ACCURACY_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BEARING_LAYER), `is`(equalTo(true)))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun renderModeGps_navigationLayersDoGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.GPS
        assertThat(isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(SHADOW_LAYER), `is`(equalTo(false)))
        assertThat(isLayerVisible(ACCURACY_LAYER), `is`(equalTo(false)))
        assertThat(isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun whenLocationLayerPluginDisabled_doesSetAllLayersToVisibilityNone() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.NORMAL
        plugin.isLocationLayerEnabled = false

        // Check that all layers visibilities are set to none
        assertThat(isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(false)))
        assertThat(isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(false)))
        assertThat(isLayerVisible(SHADOW_LAYER), `is`(equalTo(false)))
        assertThat(isLayerVisible(ACCURACY_LAYER), `is`(equalTo(false)))
        assertThat(isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun onMapChange_locationLayerLayersDoGetRedrawn() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.NORMAL
        mapboxMap.setStyleUrl(Style.SATELLITE)

        uiController.loopMainThreadForAtLeast(1000)

        assertThat(plugin.renderMode, `is`(equalTo(RenderMode.NORMAL)))

        // Check that the Source has been re-added to the new map style
        val source: GeoJsonSource? = mapboxMap.getSourceAs(LOCATION_SOURCE)
        assertThat(source, notNullValue())

        // Check that all layers visibilities are set to visible
        assertThat(isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(SHADOW_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(ACCURACY_LAYER), `is`(equalTo(true)))
        assertThat(isLayerVisible(BEARING_LAYER), `is`(equalTo(true)))
      }
    }
    executePluginTest(pluginAction)
  }

//
// Stale state test
//

  @Test
  fun whenDrawableChanged_continuesUsingStaleIcons() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.applyStyle(LocationLayerOptions.builder(context).staleStateTimeout(100).build())
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(200)


        var foregroundLayer: SymbolLayer? = mapboxMap.getLayerAs(FOREGROUND_LAYER)
        assertThat(foregroundLayer?.iconImage?.getValue(), `is`(equalTo(FOREGROUND_ICON)))

        var backgroundLayer: SymbolLayer? = mapboxMap.getLayerAs(BACKGROUND_LAYER)
        assertThat(backgroundLayer?.iconImage?.getValue(), `is`(equalTo(BACKGROUND_ICON)))

        mapboxMap.setStyleUrl(Style.SATELLITE)
        uiController.loopMainThreadForAtLeast(1000)

        foregroundLayer = mapboxMap.getLayerAs(FOREGROUND_LAYER)
        assertThat(foregroundLayer?.iconImage?.getValue(), `is`(equalTo(FOREGROUND_STALE_ICON)))

        backgroundLayer = mapboxMap.getLayerAs(BACKGROUND_LAYER)
        assertThat(backgroundLayer?.iconImage?.getValue(), `is`(equalTo(BACKGROUND_STALE_ICON)))
      }
    }
    executePluginTest(pluginAction)
  }

  @After
  fun afterTest() {
    Timber.e("@After: unregister idle resource")
    IdlingRegistry.getInstance().unregister(idlingResource)
  }

  private fun executePluginTest(listener: GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin>) {
    onView(withId(android.R.id.content)).perform(GenericPluginAction(idlingResource.mapView, mapboxMap, PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity), listener))
  }
}
