package com.mapbox.mapboxsdk.plugins.locationlayer

import android.Manifest
import android.R
import android.content.Context
import android.location.Location
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.activity.SingleFragmentActivity
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.utils.GenericPluginAction
import com.mapbox.mapboxsdk.utils.OnMapFragmentReadyIdlingResource
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

/**
 * Test class that uses a map fragment to keep onMapReady actions isolated to within the test
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class IsolatedActivityPluginTest {

  @Rule
  @JvmField
  var rule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

  @Rule
  @JvmField
  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

  private var idlingResource: OnMapFragmentReadyIdlingResource? = null
  private var fragment: SupportMapFragment? = null
  private var mapboxMap: MapboxMap? = null
  private var location: Location? = null
  private var mapView: MapView? = null

  @Before
  fun beforeTest() {

    // Create a default support map fragment and pass it into the empty activity
    fragment = SupportMapFragment.newInstance()
    rule.activity.setFragment(fragment)

    Timber.e("@Before: register idle resource")
    // If idlingResource is null, throw Kotlin exception
    idlingResource = OnMapFragmentReadyIdlingResource(fragment)
    IdlingRegistry.getInstance().register(idlingResource!!)
    Espresso.onView(ViewMatchers.withId(R.id.content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    mapboxMap = idlingResource!!.mapboxMap

    location = Location("test")
    location!!.latitude = 1.0
    location!!.longitude = 2.0
  }

  @Test
  fun settingMapStyleImmediatelyBeforeLoadingPlugin_doesStillLoadLayersProperly() {
    // Using Empty fragment activity to test since Mapbox Style change needs to happen immediately.

    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        // Sets the map Style first and then the plugin rather than initializing the plugin first,
        // then changing the style.
        mapboxMap?.setStyleUrl(Style.SATELLITE)

        mapView = fragment?.view as MapView?
        val locationLayerPlugin = LocationLayerPlugin(mapView!!, mapboxMap!!, null)
        locationLayerPlugin.forceLocationUpdate(location)

        uiController.loopMainThreadForAtLeast(500)

        // Add map change listener
        assertThat(locationLayerPlugin.renderMode, CoreMatchers.`is`(Matchers.equalTo(RenderMode.NORMAL)))
        // Check that all layers visibilities are set to none
        val foregroundLayer: SymbolLayer? = mapboxMap.getLayerAs(LocationLayerConstants.FOREGROUND_LAYER)
        val backgroundLayer: SymbolLayer? = mapboxMap.getLayerAs(LocationLayerConstants.BACKGROUND_LAYER)
        val shadowLayer: SymbolLayer? = mapboxMap.getLayerAs(LocationLayerConstants.SHADOW_LAYER)
        val accuracyLayer: CircleLayer? = mapboxMap.getLayerAs(LocationLayerConstants.ACCURACY_LAYER)

        assertThat(foregroundLayer?.visibility?.value, CoreMatchers.`is`(Matchers.equalTo(Property.VISIBLE)))
        assertThat(backgroundLayer?.visibility?.value, CoreMatchers.`is`(Matchers.equalTo(Property.VISIBLE)))
        assertThat(shadowLayer?.visibility?.value, CoreMatchers.`is`(Matchers.equalTo(Property.VISIBLE)))
        assertThat(accuracyLayer?.visibility?.value, CoreMatchers.`is`(Matchers.equalTo(Property.VISIBLE)))
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
    Espresso.onView(ViewMatchers.withId(R.id.content)).perform(GenericPluginAction(mapboxMap, null, listener))
  }
}