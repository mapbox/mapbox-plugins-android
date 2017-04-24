package com.mapbox.mapboxsdk.plugins.locationview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotationAlignment;

public class LocationViewPlugin implements LocationEngineListener {

  private Context context;
  private Location location;
  private MapboxMap mapboxMap;
  private LocationEngine locationSource;

  public LocationViewPlugin(@NonNull Context context, @NonNull MapboxMap mapboxMap) {
    this.context = context;
    this.mapboxMap = mapboxMap;


    mapboxMap.addImage(LocationViewIcons.USER_LOCATION_ICON, getBitmapFromDrawable(context, R.drawable.mapbox_user_icon));

    FeatureCollection f = FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(Point.fromCoordinates(new double[] {-77.03750, 38.89730}))});
    GeoJsonSource locationSource = new GeoJsonSource(LocationViewSources.LOCATION_SOURCE, f);
    mapboxMap.addSource(locationSource);

    SymbolLayer symbolLayer = new SymbolLayer(LocationViewLayers.LOCATION_LAYER, LocationViewSources.LOCATION_SOURCE).withProperties(
//      iconImage(myBearingTrackingMode == MyBearingTracking.NONE ? LocationViewIcons.USER_LOCATION_ICON : USER_LOCATION_BEARING_ICON),
      iconImage(LocationViewIcons.USER_LOCATION_ICON),
      iconAllowOverlap(true),
      iconIgnorePlacement(true),
      iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP));
    mapboxMap.addLayer(symbolLayer);

  }

  public void setMyLocationEnabled(boolean enable) {
    if (locationSource == null) {
      locationSource = LocationSource.getLocationEngine(context);
    }

    if (enable) {

      locationSource.addLocationEngineListener(this);
      locationSource.setPriority(LocationEnginePriority.HIGH_ACCURACY);
      locationSource.activate();

      // Set an initial location if one available
      Location lastLocation = locationSource.getLastLocation();

      if (lastLocation != null) {
        setLocation(lastLocation);
      }
    } else {
      // Location enabled has been set to false
      location = null;
      locationSource.removeLocationEngineListener(this);
      locationSource.deactivate();
    }
  }

  @Override
  public void onConnected() {
    Timber.v("LocationEngine connected.");
    locationSource.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    Timber.v("Location changed: %s", location.toString());
    setLocation(location);
  }

  private void setLocation(Location location) {
    if (location == null) {
      this.location = null;
      return;
    }
    this.location = location;

    // Create new GeoJson object with the new user location
    FeatureCollection featureCollection = FeatureCollection.fromFeatures(
      new Feature[] {Feature.fromGeometry(Point.fromCoordinates(new double[] {location.getLongitude(), location.getLatitude()}))}
    );

    // Update the location source with new GeoJson object
    GeoJsonSource locationSource = mapboxMap.getSourceAs(LocationViewSources.LOCATION_SOURCE);
    locationSource.setGeoJson(featureCollection);
  }



















  public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);

    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);

      return bitmap;
    } else {
      throw new IllegalArgumentException("unsupported drawable type");
    }
  }

  private static class LocationViewSources {
    public static final String LOCATION_SOURCE = "location-source";
  }

  private static class LocationViewLayers {
    private static final String LOCATION_LAYER = "location-layer";
  }

  private static class LocationViewIcons {
    private static final String USER_LOCATION_ICON = "user-location-icon";
  }
}
