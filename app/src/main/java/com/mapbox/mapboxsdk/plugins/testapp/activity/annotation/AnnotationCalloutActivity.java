package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Random;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconKeepUpright;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class AnnotationCalloutActivity extends AppCompatActivity {

  private static final String ID_ICON_AIRPORT = "airport";

  private MapView mapView;
  private MapboxMap mapboxMap;
  private SymbolManager symbolManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
      this.mapboxMap = mapboxMap;
      findViewById(R.id.fabStyles).setOnClickListener(v -> mapboxMap.setStyle(Utils.INSTANCE.getNextStyle()));

      mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(2));

      style.addImage(ID_ICON_AIRPORT,
        BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_airplanemode_active_black_24dp)),
        true);

      CalloutManager calloutManager = new CalloutManager(mapView, style);

      // create symbol manager
      GeoJsonOptions geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);
      symbolManager = new SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions);
      symbolManager.addClickListener(calloutManager);

      calloutManager.attachListener(mapboxMap);


      // set non data driven properties
      symbolManager.setIconAllowOverlap(true);
      symbolManager.setTextAllowOverlap(true);
      symbolManager.setIconKeepUpright(false);

      // create a symbol
      SymbolOptions symbolOptions = new SymbolOptions()
        .withLatLng(new LatLng(6.687337, 0.381457))
        .withIconImage(ID_ICON_AIRPORT)
        .withIconSize(1.3f)
        .withIconAnchor(Property.ICON_ANCHOR_CENTER)
        .withSymbolSortKey(10.0f)
        .withData(new Gson().fromJson("{ \"title\":\"Hold the door..\", \"message\":\"Holder the door! Hodor!\" }", JsonObject.class));
      symbolManager.create(symbolOptions);


      // create a symbol
      SymbolOptions symbolOptions2 = new SymbolOptions()
        .withLatLng(new LatLng(55, 35))
        .withIconImage(ID_ICON_AIRPORT)
        .withIconSize(1.3f)
        .withIconAnchor(Property.ICON_ANCHOR_CENTER)
        .withSymbolSortKey(10.0f)
        .withData(new Gson().fromJson("{ \"title\":\"1... 2... 3...\", \"message\":\"Liftoff!\" }", JsonObject.class));
      symbolManager.create(symbolOptions2);


    }));
  }

  private class CalloutManager implements OnSymbolClickListener, MapboxMap.OnMapClickListener {

    private static final String TAG = "CalloutManager";
    private MapView mapView;

    private GeoJsonSource geoJsonSource;
    private SymbolLayer symbolLayer;
    private Bitmap currentCallout;
    private Style style;
    private boolean sameEvent;
    private Symbol currentSymbol;

    public CalloutManager(MapView mapView, Style style) {
      this.mapView = mapView;
      geoJsonSource = new GeoJsonSource("com.mapbox.callout");
      symbolLayer = new SymbolLayer("com.mapbox.callout", "com.mapbox.callout");
      this.style = style;
      style.addSource(geoJsonSource);
      style.addLayer(symbolLayer);
    }

    @Override
    public void onAnnotationClick(Symbol symbol) {
      Logger.v(TAG, "onAnnotationClick: " + symbol.toString());
      if (symbol.isSelected() && currentCallout != null) {
        // clicking on same selected symbol
        hideCallout();
      } else {
        if (currentSymbol != null) {
          currentSymbol.setSelected(false);
        }
        currentSymbol = symbol;

        JsonObject data = (JsonObject) symbol.getData();
        String title = data.get("title").getAsString();
        String message = data.get("message").getAsString();

        View callout = LayoutInflater.from(mapView.getContext()).inflate(R.layout.mapbox_infowindow_content, null);
        ((TextView) callout.findViewById(R.id.infowindow_title)).setText(title);
        ((TextView) callout.findViewById(R.id.infowindow_description)).setText(message);
        showCalloutView(symbol.getGeometry(), callout);
      }
      symbol.setSelected(!symbol.isSelected());
      sameEvent = true;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
      if (currentCallout != null && !sameEvent) {
        currentSymbol.setSelected(false);
        hideCallout();
      }
      sameEvent = false;
      Logger.v(TAG, "onMapClick: " + point.toString());
      return false;
    }

    private void hideCallout() {
      //currentCallout.recycle();
      geoJsonSource.setGeoJson((FeatureCollection) null);
      currentCallout = null;
    }

    public void showCalloutView(@NonNull Geometry geometry, @NonNull View view) {
      if (currentCallout != null) {
        //currentCallout.recycle();
      }

      String id = new Random().nextInt() + "com.mapbox.callout";

      geoJsonSource.setGeoJson(geometry);
      currentCallout = SymbolGenerator.generate(view);
      style.addImage(id, currentCallout);
      symbolLayer.setProperties(
        iconAllowOverlap(true),
        iconIgnorePlacement(true),
        iconImage(id),
        iconOffset(new Float[] {66.0f, -57.0f}),
        iconKeepUpright(true)
      );
    }

    public void attachListener(MapboxMap mapboxMap) {
      mapboxMap.addOnMapClickListener(this);
    }
  }


  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (symbolManager != null) {
      symbolManager.onDestroy();
    }

    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }


  /**
   * Utility class to generate Bitmaps for Symbol.
   */
  private static class SymbolGenerator {

    /**
     * Generate a Bitmap from an Android SDK View.
     *
     * @param view the View to be drawn to a Bitmap
     * @return the generated bitmap
     */
    public static Bitmap generate(@NonNull View view) {
      int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
      view.measure(measureSpec, measureSpec);

      int measuredWidth = view.getMeasuredWidth();
      int measuredHeight = view.getMeasuredHeight();

      view.layout(0, 0, measuredWidth, measuredHeight);
      Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
      bitmap.eraseColor(Color.TRANSPARENT);
      Canvas canvas = new Canvas(bitmap);
      view.draw(canvas);
      return bitmap;
    }
  }
}
