package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.plugins.testapp.R
import com.mapbox.turf.TurfMeasurement
import kotlinx.android.synthetic.main.activity_animated_symbols.*
import timber.log.Timber
import java.util.*

class AnimateSymbolActivity : AppCompatActivity() {

  private val random = Random()

  private lateinit var mapboxMap: MapboxMap
  private lateinit var style: Style
  private lateinit var symbolManager: SymbolManager

  private lateinit var taxi: Symbol
  private lateinit var passenger: Symbol
  private val randomCars = mutableListOf<Symbol>()

  private val animators = ArrayList<Animator>()

  private val fpsList = ArrayList<Double>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_animated_symbols)

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync { map ->
      this.mapboxMap = map
      map.setOnFpsChangedListener {
        fpsList.add(it)
        fps.text = it.toString()
      }

      map.setStyle(Style.MAPBOX_STREETS) { style ->
        this.style = style

        symbolManager = SymbolManager(mapView, mapboxMap, style, WATERWAY_LAYER_ID)
        symbolManager.iconAllowOverlap = true
        symbolManager.iconIgnorePlacement = true

        taxi = symbolManager.create(
          SymbolOptions()
            .withIconImage(TAXI)
            .withLatLng(getLatLngInBounds())
        )

        passenger = symbolManager.create(SymbolOptions()
          .withIconImage(PASSENGER)
          .withSymbolSortKey(10f)
          .withLatLng(getLatLngInBounds())
        )

        for (i in 0..9) {
          randomCars.add(symbolManager.create(
            SymbolOptions()
              .withLatLng(getLatLngInBounds())
              .withIconImage(RANDOM_CAR_IMAGE_ID)
          ))
        }

        ResourcesCompat.getDrawable(resources, R.drawable.ic_car_top, theme)?.let {
          style.addImage(RANDOM_CAR_IMAGE_ID, it)
        }
        ResourcesCompat.getDrawable(resources, R.drawable.ic_business, theme)?.let {
          style.addImage(PASSENGER, it)
        }
        ResourcesCompat.getDrawable(resources, R.drawable.ic_taxi_top, theme)?.let {
          style.addImage(TAXI, it)
        }

        animateRandomRoutes()
        animateTaxi()
      }
    }
  }

  private fun animateRandomRoute(car: Symbol) {
    val next = getLatLngInBounds()
    val current = car.latLng
    val valueAnimator = ValueAnimator.ofObject(LatLngEvaluator(), car.latLng, next)
    valueAnimator.duration = (next.distanceTo(car.latLng) * 10).toLong()
    valueAnimator.addUpdateListener { animation ->
      car.latLng = animation.animatedValue as LatLng
      symbolManager.update(car)
    }
    valueAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        super.onAnimationEnd(animation)
        animateRandomRoute(car)
      }

      override fun onAnimationStart(animation: Animator?) {
        super.onAnimationStart(animation)
        car.iconRotate = getBearing(current, next)
      }
    })

    valueAnimator.interpolator = AccelerateDecelerateInterpolator()
    valueAnimator.start()
    animators.add(valueAnimator)
  }

  private fun animateRandomRoutes() {
    for (car in randomCars) {
      animateRandomRoute(car)
    }
  }

  private fun animateTaxi() {
    val next = passenger.latLng
    val valueAnimator = ValueAnimator.ofObject(LatLngEvaluator(), taxi.latLng, next)
    valueAnimator.addUpdateListener { animation ->
      taxi.latLng = animation.animatedValue as LatLng
      symbolManager.update(taxi)
    }

    valueAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        super.onAnimationEnd(animation)
        passenger.latLng = getLatLngInBounds()
        symbolManager.update(passenger)
        animateTaxi()
      }
    })

    valueAnimator.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationStart(animation: Animator) {
        super.onAnimationStart(animation)
        taxi.iconRotate = getBearing(taxi.latLng, next)
      }
    })

    valueAnimator.duration = (7 * taxi.latLng.distanceTo(next)).toLong()
    valueAnimator.interpolator = AccelerateDecelerateInterpolator()
    valueAnimator.start()

    animators.add(valueAnimator)
  }

  private fun getLatLngInBounds(): LatLng {
    val bounds = mapboxMap!!.projection.visibleRegion.latLngBounds
    val generator = Random()
    val randomLat = bounds.latSouth + generator.nextDouble() * (bounds.latNorth - bounds.latSouth)
    val randomLon = bounds.lonWest + generator.nextDouble() * (bounds.lonEast - bounds.lonWest)
    return LatLng(randomLat, randomLon)
  }

  override fun onStart() {
    super.onStart()
    mapView!!.onStart()

    Handler().postDelayed({ onBackPressed() }, 10000)
  }

  override fun onResume() {
    super.onResume()
    mapView!!.onResume()
  }

  override fun onPause() {
    super.onPause()
    mapView!!.onPause()
  }

  override fun onStop() {
    super.onStop()
    mapView!!.onStop()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView!!.onSaveInstanceState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()

    for (animator in animators) {
      animator.removeAllListeners()
      animator.cancel()
    }

    Timber.e("Amount frames ${fpsList.size}")
    Timber.e("Average FPS ${fpsList.average()}")
    mapView!!.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView!!.onLowMemory()
  }

  /**
   * Evaluator for LatLng pairs
   */
  private class LatLngEvaluator : TypeEvaluator<LatLng> {

    private val latLng = LatLng()

    override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
      latLng.latitude = startValue.latitude + (endValue.latitude - startValue.latitude) * fraction
      latLng.longitude = startValue.longitude + (endValue.longitude - startValue.longitude) * fraction
      return latLng
    }
  }

  fun getBearing(from: LatLng, to: LatLng): Float {
    return TurfMeasurement.bearing(
      Point.fromLngLat(from.longitude, from.latitude),
      Point.fromLngLat(to.longitude, to.latitude)
    ).toFloat()
  }

  companion object {
    private val TAXI = "taxi"
    private val RANDOM_CAR_IMAGE_ID = "random-car"
    private val WATERWAY_LAYER_ID = "waterway-label"
    private val PASSENGER = "passenger"
  }
}