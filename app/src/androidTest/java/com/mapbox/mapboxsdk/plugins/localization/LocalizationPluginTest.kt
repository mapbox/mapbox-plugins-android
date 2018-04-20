package com.mapbox.mapboxsdk.plugins.localization

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.testapp.activity.LocalizationActivity
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.utils.GenericPluginAction
import com.mapbox.mapboxsdk.utils.OnMapReadyIdlingResource
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
@LargeTest
class LocalizationPluginTest {

  @Rule
  @JvmField
  val rule = ActivityTestRule(LocalizationActivity::class.java)

  private var idlingResource: OnMapReadyIdlingResource? = null
  private var localizationPlugin: LocalizationPlugin? = null
  private var mapboxMap: MapboxMap? = null

  @Before
  fun beforeTest() {
    Timber.e("@Before: register idle resource")
    // If idlingResource is null, throw Kotlin exception
    idlingResource = OnMapReadyIdlingResource(rule.activity)
    IdlingRegistry.getInstance().register(idlingResource!!)
    onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    mapboxMap = idlingResource!!.mapboxMap
    localizationPlugin = rule.activity.localizationPlugin
  }

  //
  // Style Changes
  //
  
  @Test
  fun styleChanged_theCurrentLanguageRemainsTheSame() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("country-label-lg")!!

        // Change the language
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        var language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
        mapboxMap.setStyleUrl(Style.DARK)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // Country Labels
  //

  @Test
  fun languageChanged_changesCountryLabelLgLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("country-label-lg")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesCountryLabelMdLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("country-label-md")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesCountryLabelSmLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("country-label-sm")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // State Labels
  //

  @Test
  fun languageChanged_changesStateLabelLgLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("state-label-lg")!!
        var language = layer.textField.expression!!.toArray()[5].toString()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.expression!!.toArray()[5].toString()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesStateLabelMdLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("state-label-md")!!
        var language = layer.textField.expression!!.toArray()[5].toString()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.expression!!.toArray()[5].toString()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesStateLabelSmLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("state-label-sm")!!
        var language = layer.textField.expression!!.toArray()[5].toString()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.expression!!.toArray()[5].toString()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // Marine Labels
  //

  @Test
  fun languageChanged_changesMarineLabelLgPtLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("marine-label-lg-pt")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesMarineLabelLgLnLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("marine-label-lg-ln")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesMarineLabelMdPtLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("marine-label-md-pt")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesMarineLabelMdLnLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("marine-label-md-ln")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesMarineLabelSmPtLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("marine-label-sm-pt")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesMarineLabelSmLnLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("marine-label-sm-ln")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  //
  // City Labels
  //

  @Test
  fun languageChanged_changesPlaceCityLgnLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("place-city-lg-n")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesPlaceCityLgsLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("place-city-lg-s")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesPlaceCityMdnLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("place-city-md-n")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesPlaceCityMdsLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("place-city-md-s")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @Test
  fun languageChanged_changesPlaceCitySmLanguage() {
    val pluginAction = object : GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin> {
      override fun onGenericPluginAction(plugin: LocalizationPlugin?, mapboxMap: MapboxMap?,
                                         uiController: UiController, context: Context) {
        val layer: SymbolLayer = mapboxMap?.getLayerAs("place-city-sm")!!
        var language = layer.textField.getValue()

        assertThat(language, equalTo("{name_en}"))
        plugin?.setMapLanguage(MapLocale.RUSSIAN)
        language = layer.textField.getValue()
        assertThat(language, equalTo("{name_ru}"))
      }
    }
    executePluginTest(pluginAction)
  }

  @After
  fun afterTest() {
    Timber.e("@After: unregister idle resource")
    IdlingRegistry.getInstance().unregister(idlingResource!!)
  }

  private fun executePluginTest(listener: GenericPluginAction.OnPerformGenericPluginAction<LocalizationPlugin>) {
    onView(withId(android.R.id.content)).perform(GenericPluginAction(mapboxMap, localizationPlugin, listener))
  }
}
