package com.mapbox.mapboxsdk.plugins.geojson;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnMarkerEventListener;
import com.mapbox.mapboxsdk.plugins.geojson.model.DataModel;
import com.mapbox.mapboxsdk.plugins.geojson.model.MarkerData;
import com.mapbox.mapboxsdk.plugins.geojson.model.PolyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * The GeoJson plugin enables you to easily load GeoJSON data into your project from a URL, an asset file, or path.
 * <p>
 * using {@link GeoJsonPluginBuilder} class for building {@link GeoJsonPlugin} with specific variables
 * with {@link Context}
 * with {@link MapboxMap}
 * and one the file's name, the file's path, or a URL that points to a GeoJSON file.
 * </p>
 */

public class GeoJsonPlugin {
  private Context context;
  private MapboxMap map;
  private String fileName;
  private String filePath;
  private String url;
  private String geoJson;
  private OnLoadingGeoJsonListener loadFilePath;
  private OnLoadingGeoJsonListener loadFileAssets;
  private OnLoadingGeoJsonListener loadURL;
  private OnMarkerEventListener markerEventListener;
  @ColorInt
  private int fillColor;
  @ColorInt
  private int stockColor;
  private int width;
  private boolean isRandomFillColor;
  private boolean isRandomStockColor;
  private HashMap<Marker, MarkerData> markerCollectionHashMap = new HashMap<>();

  /**
   * @param context             the context of parent activity
   * @param map                 the MapboxMap to apply elements to
   * @param fileName            the file name that located in the Assets folder
   * @param filePath            the path of file located on the device
   * @param url                 the URL of the GeoJSON file
   * @param loadFilePath        the instance of onLoadingGeoJsonListener used for detecting when the file
   *                            is loaded via a certain path
   * @param loadFileAssets      the instance of onLoadingGeoJsonListener used for detecting when the file
   *                            is located in the assets folder
   * @param loadURL             the instance of onLoadingGeoJsonListener used for detecting when the file
   *                            is loaded via specific URL
   * @param fillColor           the fill color of polygons that are drawn on the map
   * @param stockColor          the stock color of polygons and polylines that are drawn on the map
   * @param width               the width of the polyline
   * @param isRandomFillColor   if true, the fill color will be random
   * @param isRandomStockColor  if true, the stock color will be random
   * @param markerEventListener the instance of OnMarkerEventListener used to detect a marker click event
   */
  public GeoJsonPlugin(Context context, MapboxMap map, String fileName, String filePath, String url,
                       OnLoadingGeoJsonListener loadFilePath, OnLoadingGeoJsonListener loadFileAssets,
                       OnLoadingGeoJsonListener loadURL, int fillColor, int stockColor, int width,
                       boolean isRandomFillColor, boolean isRandomStockColor,
                       OnMarkerEventListener markerEventListener) {
    this.context = context;
    this.map = map;
    this.loadFilePath = loadFilePath;
    this.loadFileAssets = loadFileAssets;
    this.loadURL = loadURL;
    this.markerEventListener = markerEventListener;
    this.fillColor = fillColor;
    this.stockColor = stockColor;
    this.width = width;
    this.isRandomFillColor = isRandomFillColor;
    this.isRandomStockColor = isRandomStockColor;
    setFilePath(filePath);
    setAssetsName(fileName);
    setUrl(url);
  }

  /**
   * Updates the GeoJSON source from a URL and draws the GeoJSON on the map
   *
   * @param url the URL of GeoJson file
   */
  public void setUrl(String url) {
    this.url = url;
    if (url != null) {
      if (!TextUtils.isEmpty(url)) {
        loadGeoJson();
      }
    }
  }

  /**
   * Updates the GeoJSON source from the assets folder file and draws the GeoJSON on the map
   *
   * @param fileName the name of file in Assets folder
   */
  public void setAssetsName(String fileName) {
    this.fileName = fileName;
    if (fileName != null) {
      if (!TextUtils.isEmpty(fileName)) {
        new DrawGeoJsonFromAssets().execute();
      }
    }
  }

  /**
   * Updates the GeoJSON source from an external storage path and draws the GeoJSON on the map
   *
   * @param filePath the path of file in external storage
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
    if (filePath != null) {
      if (!TextUtils.isEmpty(filePath)) {
        new DrawGeoJsonFromPath().execute();
      }
    }
  }

  /**
   * Load the GeoJSON from a URL and pass the GeoJSON as String to the {@link ParseGeoJsonFromString} AsyncTask class.
   */
  private void loadGeoJson() {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(url)
      .build();
    if (loadURL != null) {
      loadURL.onPreLoading();
    }
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        loadURL.onLoadFailed(e);
      }

      @Override
      public void onResponse(Call call, Response response) {
        try {
          geoJson = response.body().string();
          if (isJSONValid(geoJson)) {
            new ParseGeoJsonFromString().execute();
          } else {
            triggerOnLoadFailed(loadURL, new IllegalStateException(geoJson));
          }
        } catch (IOException exception) {
          loadURL.onLoadFailed(exception);
        }
      }
    });
  }

  /**
   * AsyncTask that parses a GeoJSON String to {@link JSONObject}
   */
  private class ParseGeoJsonFromString extends AsyncTask<Void, Void, DataModel> {
    @Override
    protected DataModel doInBackground(Void... voids) {
      return parseGeoJsonString(geoJson);
    }

    @Override
    protected void onPostExecute(DataModel dataModel) {
      super.onPostExecute(dataModel);
      drawOnMap(dataModel);
      if (loadURL != null) {
        loadURL.onLoaded();
      }
    }
  }

  /**
   * AsyncTask that loads a GeoJSON file from a special path
   */
  private class DrawGeoJsonFromPath extends AsyncTask<Void, Void, DataModel> {
    @Override
    protected void onPreExecute() {
      if (loadFilePath != null) {
        loadFilePath.onPreLoading();
      }
    }

    @Override
    protected DataModel doInBackground(Void... voids) {
      DataModel dataModel = null;
      try {
        InputStream inputStream = new FileInputStream(filePath);
        dataModel = parseGeoJsonInputStream(inputStream, loadFilePath);
      } catch (FileNotFoundException exception) {
        triggerOnLoadFailed(loadFilePath, exception);
      }
      return dataModel;
    }

    @Override
    protected void onPostExecute(DataModel dataModel) {
      super.onPostExecute(dataModel);
      if (dataModel != null) {
        drawOnMap(dataModel);
        if (loadFilePath != null) {
          loadFilePath.onLoaded();
        }
      }
    }
  }

  /**
   * AsyncTask that loads a GeoJSON file from the assets folder
   */
  private class DrawGeoJsonFromAssets extends AsyncTask<Void, Void, DataModel> {
    @Override
    protected void onPreExecute() {
      if (loadFileAssets != null) {
        loadFileAssets.onPreLoading();
      }
    }

    @Override
    protected DataModel doInBackground(Void... voids) {
      DataModel dataModel = null;
      try {
        InputStream inputStream = context.getAssets().open(fileName);
        dataModel = parseGeoJsonInputStream(inputStream, loadFileAssets);
      } catch (IOException exception) {
        triggerOnLoadFailed(loadFileAssets, exception);
      }

      return dataModel;
    }

    @Override
    protected void onPostExecute(DataModel dataModel) {
      super.onPostExecute(dataModel);
      if (dataModel != null) {
        drawOnMap(dataModel);
        if (loadFileAssets != null) {
          loadFileAssets.onLoaded();
        }
      }
    }
  }

  private void triggerOnLoadFailed(final OnLoadingGeoJsonListener listener, final Exception e) {
    if (listener != null) {
      ((Activity) context).runOnUiThread(new Runnable() {
        public void run() {
          listener.onLoadFailed(e);
        }
      });
    }
  }

  /**
   * @param geoJson String of the GeoJSON file
   * @return DataModel list of polylines, polygons, and point with bounded
   */
  private DataModel parseGeoJsonString(String geoJson) {
    int pointCount = 0;
    DataModel dataModel = new DataModel();
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    FeatureCollection featureCollection = FeatureCollection.fromJson(geoJson);
    List<Feature> listFeature = featureCollection.features();
    for (Feature feature : listFeature) {
      String featureType = feature.geometry().type();
      if (!TextUtils.isEmpty(featureType)) {
        if (featureType.equalsIgnoreCase("LineString")) {
          List<LatLng> latLngs = new ArrayList<>();
          LineString lineString = (LineString) feature.geometry().coordinates();
          List<Point> coordinates = lineString.coordinates();
          for (Point point : coordinates) {
            LatLng latLng = new LatLng(point.latitude(), point.longitude());
            latLngs.add(latLng);
            pointCount++;
            builder.include(latLng);
          }
          PolyData polylinePolyData = new PolyData();
          polylinePolyData.setPoints(latLngs);
          polylinePolyData.setType(featureType);
          dataModel.addPolyline(polylinePolyData);
        } else if (featureType.equalsIgnoreCase("Point")) {
          Point point = (Point) feature.geometry();
          LatLng latLng = new LatLng(point.latitude(), point.longitude());
          MarkerData markerData = new MarkerData();
          markerData.setPoint(latLng);
          markerData.setProperties(feature.properties());
          dataModel.addMarker(markerData);
          pointCount++;
          builder.include(latLng);
        } else if (featureType.equalsIgnoreCase("Polygon")) {
          List<LatLng> latLngs = new ArrayList<>();
          Polygon polygon = (Polygon) feature.geometry();
          List<Point> listPoint = polygon.coordinates().get(0);
          for (Point point : listPoint) {
            LatLng latLng = new LatLng(point.latitude(), point.longitude());
            latLngs.add(latLng);
            pointCount++;
            builder.include(latLng);
          }
          PolyData polygonPolyData = new PolyData();
          polygonPolyData.setPoints(latLngs);
          polygonPolyData.setType(featureType);
          dataModel.addPolygon(polygonPolyData);
        } else {
          //TODO
        }
      }
    }
    if (pointCount > 1) {
      dataModel.setBounds(builder.build());
    }
    return dataModel;
  }

  /**
   * Converts a InputStream to a String and passes it to a parseGeoJsonString method
   *
   * @param inputStream the input stream of GeoJSON file
   * @param listener    the instance of onLoadingGeoJsonListener
   * @return DataModel that generated by parseGeoJsonString function
   */
  private DataModel parseGeoJsonInputStream(InputStream inputStream, OnLoadingGeoJsonListener listener) {
    DataModel dataModel = null;
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
      StringBuilder sb = new StringBuilder();
      int cp;
      while ((cp = rd.read()) != -1) {
        sb.append((char) cp);
      }
      inputStream.close();
      if (!isJSONValid(sb.toString())) {
        triggerOnLoadFailed(listener, new IllegalStateException("GeoJSON string is not valid"));
      } else {
        dataModel = parseGeoJsonString(sb.toString());
      }
    } catch (IOException exception) {
      Timber.e(exception, "Exception InputStream To String: ");
    }
    return dataModel;
  }

  /**
   * Drawing {@link DataModel} element on to the map
   *
   * @param dataModel list of polylines, polygons, and points
   */
  private void drawOnMap(DataModel dataModel) {
    if (dataModel != null) {
      List<MarkerData> markers = dataModel.getMarkers();
      if (markers != null) {
        if (!markers.isEmpty()) {
          for (MarkerData markerData : dataModel.getMarkers()) {
            Marker marker = map.addMarker(new MarkerOptions()
              .position(markerData.getPoint()));
            markerCollectionHashMap.put(marker, markerData);
          }
        }
      }
      List<PolyData> polygons = dataModel.getPolygons();
      if (polygons != null) {
        if (!polygons.isEmpty()) {
          for (PolyData polyData : polygons) {
            if (isRandomFillColor) {
              fillColor = getRandomMaterialColor("400");
            }
            if (isRandomStockColor) {
              stockColor = getRandomMaterialColor("400");
            }
            map.addPolygon(new PolygonOptions()
              .addAll(polyData.getPoints())
              .fillColor(fillColor)
              .strokeColor(stockColor));
          }
        }
      }
      List<PolyData> polylines = dataModel.getPolylines();
      if (polylines != null) {
        if (!polylines.isEmpty()) {
          for (PolyData polyData : polylines) {
            if (isRandomStockColor) {
              stockColor = getRandomMaterialColor("400");
            }
            map.addPolyline(new PolylineOptions()
              .addAll(polyData.getPoints())
              .color(stockColor)
              .width(width));
          }
        }
      }
      if (dataModel.getBounds() != null) {
        map.easeCamera(CameraUpdateFactory.newLatLngBounds(dataModel.getBounds(), 50), 500);
      }


      //set onMarkerClick Listener and pass properties of marker to the MarkerEventListener
      map.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
          MarkerData markerData = markerCollectionHashMap.get(marker);
          if (markerEventListener != null) {
            markerEventListener.onMarkerClickListener(marker, markerData.getProperties());
          }
          return false;
        }
      });
    }
  }

  /**
   * @param typeColor the type of color as String
   * @return random color as an integer value
   */
  private int getRandomMaterialColor(String typeColor) {
    int returnColor = Color.GRAY;
    int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());
    if (arrayId != 0) {
      TypedArray colors = context.getResources().obtainTypedArray(arrayId);
      int index = (int) (Math.random() * colors.length());
      returnColor = colors.getColor(index, Color.GRAY);
      colors.recycle();
    }
    return returnColor;
  }

  /**
   * Validates the GeoJSON String before parsing it
   *
   * @param jsonString the string of the GeoJSON file
   * @return returns true if the String is valid JSON
   */
  private boolean isJSONValid(String jsonString) {
    try {
      new JSONObject(jsonString);
    } catch (JSONException ex) {
      try {
        new JSONArray(jsonString);
      } catch (JSONException ex1) {
        return false;
      }
    }
    return true;
  }
}
