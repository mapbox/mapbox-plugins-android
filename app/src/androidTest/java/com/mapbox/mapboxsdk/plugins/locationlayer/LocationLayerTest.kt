package com.mapbox.mapboxsdk.plugins.locationlayer

import android.Manifest
import android.R
import android.content.Context
import android.location.Location
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
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
import com.mapbox.mapboxsdk.plugins.utils.*
import com.mapbox.mapboxsdk.plugins.utils.MapboxTestingUtils.Companion.MAPBOX_HEAVY_STYLE
import com.mapbox.mapboxsdk.plugins.utils.MapboxTestingUtils.Companion.pushSourceUpdates
import com.mapbox.mapboxsdk.plugins.utils.PluginGenerationUtil.Companion.MAP_CONNECTION_DELAY
import com.mapbox.mapboxsdk.plugins.utils.PluginGenerationUtil.Companion.MAP_RENDER_DELAY
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
  private lateinit var styleChangeIdlingResource: StyleChangeIdlingResource
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
    styleChangeIdlingResource = StyleChangeIdlingResource()
    IdlingRegistry.getInstance().register(idlingResource)
    IdlingRegistry.getInstance().register(styleChangeIdlingResource)
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
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
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
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun renderModeCompass_bearingLayersDoGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.COMPASS
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(equalTo(true)))
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
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(equalTo(false)))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(equalTo(false)))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
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
        plugin.forceLocationUpdate(location)
        plugin.isLocationLayerEnabled = false
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        // Check that all layers visibilities are set to none
        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(false)))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(false)))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(equalTo(false)))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(equalTo(false)))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
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
        plugin.forceLocationUpdate(location)
        mapboxMap.setStyleUrl(Style.LIGHT)
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)

        assertThat(plugin.renderMode, `is`(equalTo(RenderMode.NORMAL)))

        // Check that the Source has been re-added to the new map style
        val source: GeoJsonSource? = mapboxMap.getSourceAs(LOCATION_SOURCE)
        assertThat(source, notNullValue())

        // Check that all layers visibilities are set to visible
        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(equalTo(true)))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(equalTo(false)))
      }
    }
    executePluginTest(pluginAction)
  }

//
// Stale state test
//

  @Test
  fun whenStyleChanged_continuesUsingStaleIcons() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.applyStyle(LocationLayerOptions.builder(context).staleStateTimeout(100).build())
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(200)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(true))

        mapboxMap.setStyleUrl(Style.LIGHT)
        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)

        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(true))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun whenStyleChanged_staleStateChanges() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.applyStyle(LocationLayerOptions.builder(context).staleStateTimeout(1).build())
        styleChangeIdlingResource.waitForStyle(idlingResource.mapView, mapboxMap, MAPBOX_HEAVY_STYLE)
        pushSourceUpdates(styleChangeIdlingResource) {
          plugin.forceLocationUpdate(location)
        }
      }
    }
    executePluginTest(pluginAction)

    // Waiting for style to finish loading while pushing updates
    Espresso.onView(ViewMatchers.withId(R.id.content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
  }

  @Test
  fun whenStyleChanged_layerVisibilityUpdates() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        styleChangeIdlingResource.waitForStyle(idlingResource.mapView, mapboxMap, MAPBOX_HEAVY_STYLE)
        var show = true
        pushSourceUpdates(styleChangeIdlingResource) {
          plugin.isLocationLayerEnabled = show
          show = !show
        }

        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
      }
    }
    executePluginTest(pluginAction)

    // Waiting for style to finish loading while pushing updates
    Espresso.onView(ViewMatchers.withId(R.id.content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
  }

  @After
  fun afterTest() {
    Timber.e("@After: unregister idle resource")
    IdlingRegistry.getInstance().unregister(idlingResource)
    IdlingRegistry.getInstance().unregister(styleChangeIdlingResource)
  }

  private fun executePluginTest(listener: GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin>) {
    onView(withId(android.R.id.content)).perform(GenericPluginAction(idlingResource.mapView, mapboxMap, PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity), listener))
  }
}
