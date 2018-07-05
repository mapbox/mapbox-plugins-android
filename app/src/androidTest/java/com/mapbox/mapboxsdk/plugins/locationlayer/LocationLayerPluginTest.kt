package com.mapbox.mapboxsdk.plugins.locationlayer

import android.Manifest
import android.R
import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.location.Location
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerConstants.*
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.plugins.testapp.activity.SingleFragmentActivity
import com.mapbox.mapboxsdk.plugins.utils.*
import com.mapbox.mapboxsdk.plugins.utils.MapboxTestingUtils.Companion.MAPBOX_HEAVY_STYLE
import com.mapbox.mapboxsdk.plugins.utils.MapboxTestingUtils.Companion.pushSourceUpdates
import com.mapbox.mapboxsdk.plugins.utils.PluginGenerationUtil.Companion.MAP_CONNECTION_DELAY
import com.mapbox.mapboxsdk.plugins.utils.PluginGenerationUtil.Companion.MAP_RENDER_DELAY
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
class LocationLayerPluginTest {

  @Rule
  @JvmField
  val rule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

  @Rule
  @JvmField
  val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

  private lateinit var idlingResource: OnMapFragmentReadyIdlingResource
  private lateinit var styleChangeIdlingResource: StyleChangeIdlingResource
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
    styleChangeIdlingResource = StyleChangeIdlingResource()
    IdlingRegistry.getInstance().register(idlingResource)
    IdlingRegistry.getInstance().register(styleChangeIdlingResource)
    onView(withId(R.id.content)).check(matches(isDisplayed()))
    mapboxMap = idlingResource.mapboxMap
  }

  @Test
  fun locationLayerPlugin_initializesLocationEngineCorrectlyWhenOnesNotProvided() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        val locationEngine = plugin.locationEngine
        assertThat(locationEngine, notNullValue())

        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
        assertThat(locationEngine?.isConnected, `is`(true))
      }
    }

    executePluginTest(pluginAction, PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, true))
  }

  @Test
  fun settingMapStyleImmediatelyBeforeLoadingPlugin_doesStillLoadLayersProperly() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)

        assertThat(plugin.renderMode, `is`(equalTo(RenderMode.NORMAL)))
        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(false))
      }
    }

    executePluginTest(pluginAction, object : GenericPluginAction.PluginProvider<LocationLayerPlugin> {
      override fun providePlugin(mapView: MapView, mapboxMap: MapboxMap, context: Context): LocationLayerPlugin {
        // changing the style just before instantiating the plugin
        mapboxMap.setStyleUrl(Style.LIGHT)
        val plugin =
          PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false)
            .providePlugin(mapView, mapboxMap, context)
        plugin.forceLocationUpdate(location)
        return plugin
      }

      override fun isPluginDataReady(plugin: LocationLayerPlugin, mapboxMap: MapboxMap): Boolean {
        val source = mapboxMap.getSource(LOCATION_SOURCE)
        return source != null && (source as GeoJsonSource).querySourceFeatures(null).isNotEmpty()
      }
    })
  }

  @Test
  fun locationLayer_doesntShowUntilFirstLocationFix() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        // Source should be present but empty
        val mapView = fragment.view as MapView
        assertThat(mapboxMap.queryRenderedFeatures(
          RectF(0f, 0f, mapView.width.toFloat(), mapView.height.toFloat()), FOREGROUND_LAYER)
          .isEmpty(), `is`(true))

        // Force the first location update
        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)

        // Check if the puck is visible
        val latLng = LatLng(location.latitude, location.longitude)
        val point = mapboxMap.projection.toScreenLocation(latLng)
        assertThat(mapboxMap.queryRenderedFeatures(point, FOREGROUND_LAYER).isEmpty(), `is`(false))
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

        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(200)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        mapboxMap.querySourceFeatures(LOCATION_SOURCE).also {
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
  fun locationLayerOptions_loadsForegroundBitmapFromNameOption() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        val foregroundDrawable = ContextCompat.getDrawable(context, R.drawable.ic_media_play)
        mapboxMap.addImageFromDrawable("custom-foreground-bitmap", foregroundDrawable!!)

        val foregroundLayer = mapboxMap.getLayer(FOREGROUND_LAYER) as SymbolLayer
        val iconImageValue = foregroundLayer.iconImage.expression.toString()

        assertTrue(iconImageValue.contains("custom-foreground-bitmap"))
      }
    }

    val options = LocationLayerOptions.builder(fragment.activity)
            .foregroundName("custom-foreground-bitmap")
            .build()
    executePluginTest(pluginAction,
            PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, options))
  }

  @Test
  fun locationLayerOptions_loadsGpsNameWithGpsRenderMode() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.renderMode = RenderMode.GPS
        val foregroundDrawable = ContextCompat.getDrawable(context, R.drawable.ic_media_play)
        mapboxMap.addImageFromDrawable("custom-foreground-bitmap", foregroundDrawable!!)
        mapboxMap.addImageFromDrawable("custom-gps-bitmap", foregroundDrawable)

        val foregroundLayer = mapboxMap.getLayer(FOREGROUND_LAYER) as SymbolLayer
        val iconImageValue = foregroundLayer.iconImage.expression.toString()

        assertTrue(iconImageValue.contains("custom-gps-bitmap"))
      }
    }

    val options = LocationLayerOptions.builder(fragment.activity)
        .foregroundName("custom-foreground-bitmap")
        .gpsName("custom-gps-bitmap")
        .build()
    executePluginTest(pluginAction,
        PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, options))
  }

  @Test
  fun stillStaleAfterResuming() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(300) // engaging stale state
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(true))

        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(true))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(false))
      }
    }
    val options = LocationLayerOptions.builder(fragment.activity)
      .staleStateTimeout(200)
      .build()
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, options, false))
  }

  @Test
  fun stillNotStaleAfterResuming() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(false))

        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].getBooleanProperty(PROPERTY_LOCATION_STALE), `is`(false))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(true))
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity))
  }

  @Test
  fun locationLayerOptions_accuracyRingWithColor() {
    val color = Color.parseColor("#4A90E2")
    val rgbaColor = PropertyFactory.colorToRgbaString(color)

    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        // Check that the source property changes correctly
        mapboxMap.querySourceFeatures(LOCATION_SOURCE).also {
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

        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        val point: Point = mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].geometry() as Point

        assertThat(plugin.locationEngine, nullValue())
        assertEquals(point.latitude(), location.latitude, 0.1)
        assertEquals(point.longitude(), location.longitude, 0.1)
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun disablingPluginHidesPuck() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        plugin.forceLocationUpdate(location)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        val point: Point = mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].geometry() as Point
        assertEquals(point.latitude(), location.latitude, 0.1)
        assertEquals(point.longitude(), location.longitude, 0.1)

        plugin.isLocationLayerEnabled = false
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)
        assertThat(mapboxMap.querySourceFeatures(LOCATION_SOURCE).isEmpty(), `is`(true))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun lifecycle_keepsEnabledWhenStoppedAndStarted() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        assertThat(plugin.isLocationLayerEnabled, `is`(true))
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        testLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        assertThat(plugin.isLocationLayerEnabled, `is`(true))
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false))
  }

  @Test
  fun lifecycle_keepsDisabledWhenStoppedAndStarted() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        plugin.isLocationLayerEnabled = false

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        assertThat(plugin.isLocationLayerEnabled, `is`(false))
        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        assertThat(plugin.isLocationLayerEnabled, `is`(false))
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false))
  }

  @Test
  fun lifecycle_ableToChangeStyleAfterResuming() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)

        mapboxMap.setStyle(Style.DARK)
        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false))
  }

  @Test
  fun lifecycle_interruptedDuringStyleChange() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)
        mapboxMap.setStyle(Style.DARK)

        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false))
  }

  @Test
  fun lifecycle_forceLocationUpdateAfterStopped() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        plugin.forceLocationUpdate(location)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        val point: Point = mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].geometry() as Point
        assertNotEquals(point.latitude(), location.latitude, 0.1)
        assertNotEquals(point.longitude(), location.longitude, 0.1)
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false))
  }

  @Test
  fun lifecycle_lifecycleChangeRightAfterStyleReload() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {

        val testLifecycleOwner = TestLifecycleOwner()
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        testLifecycleOwner.lifecycle.addObserver(plugin)

        plugin.forceLocationUpdate(location)
        mapboxMap.setStyle(Style.LIGHT)
        testLifecycleOwner.markState(Lifecycle.State.CREATED)
        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
        testLifecycleOwner.markState(Lifecycle.State.RESUMED)
        uiController.loopMainThreadForAtLeast(MAP_RENDER_DELAY)

        val point: Point = mapboxMap.querySourceFeatures(LOCATION_SOURCE)[0].geometry() as Point
        assertEquals(point.latitude(), location.latitude, 0.1)
        assertEquals(point.longitude(), location.longitude, 0.1)

        assertThat(mapboxMap.isLayerVisible(FOREGROUND_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(BACKGROUND_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(SHADOW_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(ACCURACY_LAYER), `is`(true))
        assertThat(mapboxMap.isLayerVisible(BEARING_LAYER), `is`(false))
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false))
  }

  @Test
  fun mapChange_settingPluginStyle() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        styleChangeIdlingResource.waitForStyle(fragment.view as MapView, mapboxMap, MAPBOX_HEAVY_STYLE)
        val options = LocationLayerOptions.builder(fragment.activity)
          .accuracyColor(Color.RED)
          .build()

        pushSourceUpdates(styleChangeIdlingResource) {
          plugin.applyStyle(options)
        }

        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity))

    // Waiting for style to finish loading while pushing updates
    onView(withId(R.id.content)).check(matches(isDisplayed()))
  }

  @Test
  fun mapChange_forcingLocation() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        styleChangeIdlingResource.waitForStyle(fragment.view as MapView, mapboxMap, MAPBOX_HEAVY_STYLE)

        pushSourceUpdates(styleChangeIdlingResource) {
          plugin.forceLocationUpdate(location)
        }

        uiController.loopMainThreadForAtLeast(MAP_CONNECTION_DELAY)
      }
    }
    executePluginTest(pluginAction,
      PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity))

    // Waiting for style to finish loading while pushing updates
    onView(withId(R.id.content)).check(matches(isDisplayed()))
  }

  @Test
  fun mapChange_settingMapStyleBeforePluginCreation() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin> {
      override fun onGenericPluginAction(plugin: LocationLayerPlugin, mapboxMap: MapboxMap,
                                         uiController: UiController, context: Context) {
        val options = LocationLayerOptions.builder(fragment.activity)
          .accuracyColor(Color.RED)
          .build()

        pushSourceUpdates(styleChangeIdlingResource) {
          plugin.forceLocationUpdate(location)
          plugin.applyStyle(options)
        }
      }
    }

    executePluginTest(pluginAction, object : GenericPluginAction.PluginProvider<LocationLayerPlugin> {
      override fun providePlugin(mapView: MapView, mapboxMap: MapboxMap, context: Context): LocationLayerPlugin {
        // changing the style just before instantiating the plugin
        styleChangeIdlingResource.waitForStyle(mapView, mapboxMap, MAPBOX_HEAVY_STYLE)
        return PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity, false, null, null, false)
          .providePlugin(mapView, mapboxMap, context)
      }

      override fun isPluginDataReady(plugin: LocationLayerPlugin, mapboxMap: MapboxMap): Boolean {
        return true
      }
    })

    // Waiting for style to finish loading while pushing updates
    onView(withId(R.id.content)).check(matches(isDisplayed()))
  }

  @After
  fun afterTest() {
    Timber.e("@After: unregister idle resource")
    IdlingRegistry.getInstance().unregister(idlingResource)
    IdlingRegistry.getInstance().unregister(styleChangeIdlingResource)
  }

  private fun executePluginTest(listener: GenericPluginAction.OnPerformGenericPluginAction<LocationLayerPlugin>,
                                pluginProvider: GenericPluginAction.PluginProvider<LocationLayerPlugin> = PluginGenerationUtil.getLocationLayerPluginProvider(rule.activity)) {
    onView(withId(R.id.content)).perform(GenericPluginAction(fragment.view as MapView, mapboxMap, pluginProvider, listener))
  }
}