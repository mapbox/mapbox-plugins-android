package com.mapbox.mapboxsdk.plugins.geojson;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnMarkerEventListener;

public class GeoJsonPluginBuilder {
    private Context context;
    private MapboxMap map;
    private String fileNameAssets;
    private String filePath;
    private String url;
    private OnLoadingGeoJsonListener loadFilePath;
    private OnLoadingGeoJsonListener loadFileAssets;
    private OnLoadingGeoJsonListener loadURL;
    private OnMarkerEventListener markerEventListener;
    @ColorInt
    private
    int fillColor = Color.argb(50, 0, 0, 250);
    @ColorInt
    private
    int stockColor = Color.argb(80, 250, 0, 0);
    private int width = 3;
    private boolean isRandomFillColor = false;
    private boolean isRandomStockColor = false;

    /**
     * @param context the context of parent activity
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withContext(@NonNull Context context) {
        this.context = context;
        return this;
    }

    /**
     * @param map the MapboxMap
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withMap(@NonNull MapboxMap map) {
        this.map = map;
        return this;
    }

    /**
     * @param fileNameAssets the file name of GeoJson in Assets folder
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withAssetsName(String fileNameAssets) {
        this.fileNameAssets = fileNameAssets;
        return this;
    }

    /**
     * @param filePath the path of GeoJson file
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * @param url the url of GeoJson
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @param loadFileAssets the instance of onLoadingGeoJsonListener class
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withOnLoadingFileAssets(OnLoadingGeoJsonListener loadFileAssets) {
        this.loadFileAssets = loadFileAssets;
        return this;
    }


    /**
     * @param loadFilePath the instance of onLoadingGeoJsonListener class
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withOnLoadingFilePath(OnLoadingGeoJsonListener loadFilePath) {
        this.loadFilePath = loadFilePath;
        return this;
    }

    /**
     * @param loadURL the instance of onLoadingGeoJsonListener class
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withOnLoadingURL(OnLoadingGeoJsonListener loadURL) {
        this.loadURL = loadURL;
        return this;
    }

    /**
     * @param markerClickListener the instance of OnMarkerEventListener class
     * @return instance of GeoJsonPluginBuilder class
     */

    public GeoJsonPluginBuilder withMarkerClickListener(OnMarkerEventListener markerClickListener) {
        this.markerEventListener = markerClickListener;
        return this;
    }

    /**
     * @param fillColor the fill color of polygon. default value is Color.argb(50, 0, 0, 250)
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withPolygonFillColor(@ColorInt int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    /**
     * @param stockColor the stock color of polyline & polygon. default value is Color.argb(80, 250, 0, 0);
     * @return instance of GeoJsonPluginBuilder class
     */

    public GeoJsonPluginBuilder withStockColor(@ColorInt int stockColor) {
        this.stockColor = stockColor;
        return this;
    }

    /**
     * @param width the stock width of polyline & polygon. default value is 3
     * @return instance of GeoJsonPluginBuilder class
     */

    public GeoJsonPluginBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * set isRandomFillColor to true.
     *
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withRandomFillColor() {
        this.isRandomFillColor = true;
        return this;
    }

    /**
     * set isRandomStockColor to true.
     *
     * @return instance of GeoJsonPluginBuilder class
     */
    public GeoJsonPluginBuilder withRandomStockColor() {
        this.isRandomStockColor = true;
        return this;
    }


    /**
     * @return instance of GeoJsonPlugin
     */
    public GeoJsonPlugin build() {
        return new GeoJsonPlugin(context, map, fileNameAssets, filePath, url, loadFilePath, loadFileAssets, loadURL, fillColor, stockColor, width, isRandomFillColor, isRandomStockColor, markerEventListener);
    }
}