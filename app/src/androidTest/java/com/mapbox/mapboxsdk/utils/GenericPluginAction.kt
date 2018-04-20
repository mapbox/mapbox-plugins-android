package com.mapbox.mapboxsdk.utils

import android.content.Context
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.view.View
import com.mapbox.mapboxsdk.maps.MapboxMap
import org.hamcrest.Matcher

class GenericPluginAction<T>(private val mapboxMap: MapboxMap?, private val plugin: T?,
                             private val onPerformGenericPluginAction: OnPerformGenericPluginAction<T>?) : ViewAction {

  override fun getConstraints(): Matcher<View> {
    return isDisplayed()
  }

  override fun getDescription(): String {
    return javaClass.simpleName
  }

  override fun perform(uiController: UiController, view: View) {
    onPerformGenericPluginAction?.onGenericPluginAction(plugin, mapboxMap, uiController,
        view.context)
  }

  interface OnPerformGenericPluginAction<T> {
    fun onGenericPluginAction(plugin: T?, mapboxMap: MapboxMap?, uiController: UiController, context: Context)
  }
}