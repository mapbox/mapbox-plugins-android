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
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.*
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.mapboxsdk.plugins.testapp.activity.location.LocationLayerModesActivity
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.utils.GenericPluginAction
import com.mapbox.mapboxsdk.utils.OnMapReadyIdlingResource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
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
  val rule = ActivityTestRule(LocationLayerModesActivity::class.java)

  @Rule
  @JvmField
  val permissionRule: GrantPermissionRule = grant(Manifest.permission.ACCESS_FINE_LOCATION)

  private var idlingResource: OnMapReadyIdlingResource? = null
  private var mapboxMap: MapboxMap? = null
  private var location: Location? = null

  @Before
  fun beforeTest() {
    Timber.e("@Before: register idle resource")
    // If idlingResource is null, throw Kotlin exception
    idlingResource = OnMapReadyIdlingResource(rule.activity)
    IdlingRegistry.getInstance().register(idlingResource!!)
    onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    mapboxMap = idlingResource!!.mapboxMap

    location = Location("test")
    location!!.latitude = 1.0
    location!!.longitude = 2.0
  }

  @Test
  fun sanity() {
    assertThat(mapboxMap, notNullValue())
    assertThat(rule.activity.locationLayerPlugin, notNullValue())
  }

  //
  // Location Source
  //

  @Test
  fun renderModeNormal_sourceDoesGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.NORMAL
        assertThat(mapboxMap?.getSource(LOCATION_SOURCE), notNullValue())
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
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.NORMAL
        assertThat(mapboxMap?.getLayer(ACCURACY_LAYER), notNullValue())
        assertThat(mapboxMap?.getLayer(BACKGROUND_LAYER), notNullValue())
        assertThat(mapboxMap?.getLayer(FOREGROUND_LAYER), notNullValue())
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun renderModeCompass_bearingLayersDoGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.COMPASS
        assertThat(mapboxMap?.getLayer(ACCURACY_LAYER), notNullValue())
        assertThat(mapboxMap?.getLayer(BACKGROUND_LAYER), notNullValue())
        assertThat(mapboxMap?.getLayer(FOREGROUND_LAYER), notNullValue())
        assertThat(mapboxMap?.getLayer(BEARING_LAYER), notNullValue())
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun renderModeGps_navigationLayersDoGetAdded() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.GPS
        assertThat(mapboxMap?.getLayer(FOREGROUND_LAYER), notNullValue())
        assertThat(mapboxMap?.getImage(FOREGROUND_ICON), notNullValue())
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun whenLocationLayerPluginDisabled_doesSetAllLayersToVisibilityNone() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.NORMAL
        assertThat(mapboxMap?.getLayer(FOREGROUND_LAYER), notNullValue())
        plugin?.setLocationLayerEnabled(false)

        // Check that all layers visibilities are set to none
        val foregroundLayer: SymbolLayer? = mapboxMap?.getLayerAs(FOREGROUND_LAYER)
        val backgroundLayer: SymbolLayer? = mapboxMap?.getLayerAs(BACKGROUND_LAYER)
        val shadowLayer: SymbolLayer? = mapboxMap?.getLayerAs(SHADOW_LAYER)
        val accuracyLayer: CircleLayer? = mapboxMap?.getLayerAs(ACCURACY_LAYER)
        val bearingLayer: SymbolLayer? = mapboxMap?.getLayerAs(BEARING_LAYER)

        assertThat(foregroundLayer?.visibility?.value, `is`(equalTo(Property.NONE)))
        assertThat(backgroundLayer?.visibility?.value, `is`(equalTo(Property.NONE)))
        assertThat(shadowLayer?.visibility?.value, `is`(equalTo(Property.NONE)))
        assertThat(accuracyLayer?.visibility?.value, `is`(equalTo(Property.NONE)))
        assertThat(bearingLayer?.visibility?.value, `is`(equalTo(Property.NONE)))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun onMapChange_locationLayerLayersDoGetRedrawn() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.NORMAL
        assertThat(mapboxMap?.getLayer(FOREGROUND_LAYER), notNullValue())

        // Add map change listener
        val mapView: MapView? = rule.activity.findViewById(R.id.map_view)
        mapView?.addOnMapChangedListener {
          if (it == MapView.DID_FINISH_LOADING_STYLE) {
            assertThat(plugin?.renderMode, `is`(equalTo(RenderMode.NORMAL)))
            // Check that all layers visibilities are set to none
            val foregroundLayer: SymbolLayer? = mapboxMap?.getLayerAs(FOREGROUND_LAYER)
            val backgroundLayer: SymbolLayer? = mapboxMap?.getLayerAs(BACKGROUND_LAYER)
            val shadowLayer: SymbolLayer? = mapboxMap?.getLayerAs(SHADOW_LAYER)
            val accuracyLayer: SymbolLayer? = mapboxMap?.getLayerAs(ACCURACY_LAYER)
            val bearingLayer: SymbolLayer? = mapboxMap?.getLayerAs(BEARING_LAYER)

            assertThat(foregroundLayer?.visibility?.value, `is`(equalTo(Property.VISIBLE)))
            assertThat(backgroundLayer?.visibility?.value, `is`(equalTo(Property.VISIBLE)))
            assertThat(shadowLayer?.visibility?.value, `is`(equalTo(Property.VISIBLE)))
            assertThat(accuracyLayer?.visibility?.value, `is`(equalTo(Property.VISIBLE)))
            assertThat(bearingLayer?.visibility?.value, `is`(equalTo(Property.VISIBLE)))
          }
        }
        mapboxMap?.setStyleUrl(Style.SATELLITE)
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // Accuracy Ring
  //

  @Test
  fun renderModeGps_accuracyLayerVisibilitySetToNone() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.GPS
        val accuracyLayer: CircleLayer? = mapboxMap?.getLayerAs(ACCURACY_LAYER)
        assertThat(accuracyLayer?.visibility?.value, `is`(equalTo(Property.NONE)))
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
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.NORMAL
        plugin?.applyStyle(LocationLayerOptions.builder(context).staleStateTimeout(100).build())
        plugin?.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(200)
        rule.activity.toggleStyle()
        val symbolLayer: SymbolLayer? = mapboxMap?.getLayerAs(FOREGROUND_LAYER)
        assertThat(symbolLayer?.iconImage?.getValue(), `is`(not(equalTo(FOREGROUND_ICON))))
      }
    }
    executePluginTest(pluginAction)
  }

  @After
  fun afterTest() {
    Timber.e("@After: unregister idle resource")
    IdlingRegistry.getInstance().unregister(idlingResource!!)
  }

  private fun executePluginTest(listener: GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin>) {
    onView(withId(android.R.id.content)).perform(GenericPluginAction(mapboxMap, rule.activity.locationLayerPlugin, listener))
  }
}
