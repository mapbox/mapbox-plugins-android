package com.mapbox.mapboxsdk.plugins.locationlayer

import android.Manifest
import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.RectF
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
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.*
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.activity.SingleFragmentActivity
import com.mapbox.mapboxsdk.plugins.utils.GenericPluginAction
import com.mapbox.mapboxsdk.plugins.utils.OnMapFragmentReadyIdlingResource
import com.mapbox.mapboxsdk.plugins.utils.PluginGenerationUtil
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.junit.*
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
  val rule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

  @Rule
  @JvmField
  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

  private lateinit var idlingResource: OnMapFragmentReadyIdlingResource
  private lateinit var fragment: SupportMapFragment
  private lateinit var mapboxMap: MapboxMap
  private val location: Location by lazy {
    val initLocation = Location("test")
    initLocation.latitude = 15.0
    initLocation.longitude = 17.0
    initLocation
  }

  @Before
  fun beforeTest() {

    // Create a default support map fragment and pass it into the empty activity
    fragment = SupportMapFragment.newInstance()
    rule.activity.setFragment(fragment)

    Timber.e("@Before: register idle resource")
    // If idlingResource is null, throw Kotlin exception
    idlingResource = OnMapFragmentReadyIdlingResource(fragment)
    IdlingRegistry.getInstance().register(idlingResource)
    Espresso.onView(ViewMatchers.withId(R.id.content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    mapboxMap = idlingResource.mapboxMap
  }

  @Test
  fun locationLayerPlugin_initializesLocationEngineCorrectlyWhenOnesNotProvided() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        val locationEngine = plugin.locationEngine
        assertThat(locationEngine, notNullValue())

        uiController.loopMainThreadForAtLeast(500)
        assertThat(locationEngine?.isConnected, `is`(equalTo(true)))
      }
    }

    executePluginTest(pluginAction, PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, true))
  }

  @Test
  fun settingMapStyleImmediatelyBeforeLoadingPlugin_doesStillLoadLayersProperly() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        plugin.forceLocationUpdate(location)

        assertThat(plugin.renderMode, `is`(equalTo(RenderMode.NORMAL)))
        assertThat(isLayerVisible(FOREGROUND_LAYER), `is`(Matchers.equalTo(true)))
        assertThat(isLayerVisible(BACKGROUND_LAYER), `is`(Matchers.equalTo(true)))
        assertThat(isLayerVisible(SHADOW_LAYER), `is`(Matchers.equalTo(true)))
        assertThat(isLayerVisible(ACCURACY_LAYER), `is`(Matchers.equalTo(true)))
        assertThat(isLayerVisible(BEARING_LAYER), `is`(Matchers.equalTo(false)))
      }
    }

    executePluginTest(pluginAction, object : GenericPluginAction.PluginProvider<LocationLayerPlugin> {
      override fun providePlugin(mapView: MapView, mapboxMap: MapboxMap, context: Context): LocationLayerPlugin {
        // changing the style just before instantiating the plugin
        mapboxMap.setStyleUrl(Style.SATELLITE)
        return PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity).providePlugin(mapView, mapboxMap, context)
      }

      override fun isDataReady(plugin: LocationLayerPlugin, mapboxMap: MapboxMap): Boolean {
        val source = mapboxMap.getSource(LOCATION_SOURCE)
        return source != null && (source as GeoJsonSource).querySourceFeatures(null).isNotEmpty()
      }
    })
  }

  private fun isLayerVisible(layerId: String): Boolean {
    return mapboxMap.getLayer(layerId)?.visibility?.value?.equals(Property.VISIBLE)!!
  }

  @Test
  fun locationLayer_doesntShowUntilFirstLocationFix() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        // Source should be present but empty
        val source: GeoJsonSource? = mapboxMap.getSourceAs(LOCATION_SOURCE)
        assertThat(source, notNullValue())
        assert(queryLocationSourceRenderedFeatures().isEmpty())

        // Force the first location update
        plugin.forceLocationUpdate(location)

        // Check if the puck is visible
        val latLng = LatLng(location.latitude, location.longitude)
        val point = mapboxMap.projection.toScreenLocation(latLng)
        assert(mapboxMap.queryRenderedFeatures(point, LocationLayerConstants.FOREGROUND_ICON).isNotEmpty())
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // Location Layer Options
  //

  @Test
  fun locationLayerOptions_disablingStaleStateDoesWorkCorrectly() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        queryLocationSourceRenderedFeatures().also {
          it.forEach {
            assertThat(it.getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(false))
          }
        }
      }
    }

    val options = LocationLayerOptions.builder(fragment.activity)
      .staleStateTimeout(200)
      .enableStaleState(false)
      .build()
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, options))
  }

  @Test
  fun locationLayerOptions_accuracyRingWithColor() {
    val color = Color.parseColor("#4A90E2")
    val rgbaColor = PropertyFactory.colorToRgbaString(color)

    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        // Check that the source property changes correctly
        queryLocationSourceRenderedFeatures().also {
          it.forEach {
            assertThat(it.getStringProperty(PROPERTY_ACCURACY_COLOR), `is`(equalTo(rgbaColor)))
          }
        }
      }
    }

    val options = LocationLayerOptions.builder(fragment.activity)
      .accuracyColor(color)
      .build()
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, options))
  }

  @Test
  fun forceLocationUpdate_doesMoveLocationLayerIconToCorrectPosition() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        plugin.forceLocationUpdate(location)

        val point: Point = queryLocationSourceRenderedFeatures()[0].geometry() as Point

        Assert.assertThat(plugin.locationEngine, nullValue())
        Assert.assertThat(point.latitude(), `is`(equalTo(location.latitude)))
        Assert.assertThat(point.longitude(), `is`(equalTo(location.longitude)))
      }
    }
    executePluginTest(pluginAction)
  }

  @After
  fun afterTest() {
    Timber.e("@After: unregister idle resource")
    IdlingRegistry.getInstance().unregister(idlingResource)
  }

  private fun queryLocationSourceRenderedFeatures(): List<Feature> {
    val mapView = fragment.view as MapView
    return mapboxMap.queryRenderedFeatures(RectF(0f, 0f, mapView.width.toFloat(), mapView.height.toFloat()),
      FOREGROUND_LAYER,
      BACKGROUND_LAYER,
      SHADOW_LAYER,
      ACCURACY_LAYER,
      BEARING_LAYER)
  }

  private fun executePluginTest(listener: GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin>,
                                pluginProvider: GenericPluginAction.PluginProvider<LocationLayerPlugin> = PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity)) {
    Espresso.onView(ViewMatchers.withId(R.id.content)).perform(GenericPluginAction(fragment.view as MapView, mapboxMap, pluginProvider, listener))
  }
}