package com.mapbox.mapboxsdk.plugins.locationlayer

import android.Manifest
import android.content.Context
import android.location.Location
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.LOCATION_SOURCE
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.activity.location.LocationLayerModesActivity
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.GenericPluginAction
import com.mapbox.mapboxsdk.utils.OnMapReadyIdlingResource
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber


@RunWith(AndroidJUnit4::class)
@LargeTest
class LocationLayerPluginTest {

  @Rule
  @JvmField
  val rule = ActivityTestRule(LocationLayerModesActivity::class.java)

  @Rule
  @JvmField
  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

  private var idlingResource: OnMapReadyIdlingResource? = null
  private var mapboxMap: MapboxMap? = null
  private var location: Location? = null

  @Before
  fun beforeTest() {
    Timber.e("@Before: register idle resource")
    // If idlingResource is null, throw Kotlin exception
    idlingResource = OnMapReadyIdlingResource(rule.activity)
    IdlingRegistry.getInstance().register(idlingResource!!)
    Espresso.onView(ViewMatchers.withId(android.R.id.content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    mapboxMap = idlingResource!!.mapboxMap

    location = Location("test")
    location!!.latitude = 1.0
    location!!.longitude = 2.0
  }

  @Test
  fun forceLocationUpdate_doesMoveLocationLayerIconToCorrectPosition() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        plugin?.renderMode = RenderMode.NORMAL
        plugin?.locationEngine = null
        plugin?.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(5000)
        val source: GeoJsonSource? = mapboxMap?.getSourceAs(LOCATION_SOURCE)
        val features: MutableList<Feature>? = source?.querySourceFeatures(null)
        val point: Point = features?.get(0)?.geometry() as Point
        assertThat(plugin?.locationEngine, nullValue())
        assertThat(point.latitude(), `is`(equalTo(1.0)))
        assertThat(point.longitude(), `is`(equalTo(2.0)))
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
    Espresso.onView(ViewMatchers.withId(android.R.id.content)).perform(GenericPluginAction(mapboxMap, rule.activity.locationLayerPlugin, listener))
  }
}
