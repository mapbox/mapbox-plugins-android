package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPluginBuilder;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnMarkerEventListener;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity showcasing GeoJson plugin integration
 */
public class GeoJsonActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = "GeoJsonActivity";
    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.fabURL)
    FloatingActionButton urlFab;

    @BindView(R.id.fabAssets)
    FloatingActionButton assetsFab;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MapboxMap mapboxMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geojson);
        ButterKnife.bind(this);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED,
                android.graphics.PorterDuff.Mode.SRC_IN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.6546, 51.6680), 7));

    }

    @OnClick(R.id.fabURL)
    public void onURLFabClick() {
        mapboxMap.clear();
        if (mapboxMap != null) {
            new GeoJsonPluginBuilder()
                    .withContext(this)
                    .withMap(mapboxMap)
                    .withUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson")
                    .withOnLoadingURL(new OnLoadingGeoJsonListener() {
                        @Override
                        public void onPreLoading() {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoaded() {
                            Toast.makeText(GeoJsonActivity.this, "GeoJson data from url loaded", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onLoadFailed(Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GeoJsonActivity.this, "Error occur during load GeoJson data from url. see logcat", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    })
                    .withMarkerClickListener(new OnMarkerEventListener() {
                        @Override
                        public void onMarkerClickListener(Marker marker, JSONObject properties) {
                            try {
                                Toast.makeText(GeoJsonActivity.this, properties.getString("title"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .draw();
        }
    }

    @OnClick(R.id.fabAssets)
    public void onAssetsFabClick() {
        if (mapboxMap != null) {
            mapboxMap.clear();
            new GeoJsonPluginBuilder()
                    .withContext(this)
                    .withMap(mapboxMap)
                    .withFileNameAssets("IRN.geo.json")
                    .withOnLoadingFileAssets(new OnLoadingGeoJsonListener() {
                        @Override
                        public void onPreLoading() {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoaded() {
                            Toast.makeText(GeoJsonActivity.this, "GeoJson data from assets loaded", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onLoadFailed(Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GeoJsonActivity.this, "Error occur during load GeoJson data from assets. see logcat", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    })
                    .draw();
            requestExternalStoragePermission();
        }
    }

    /**
     * draw GeoJson file from path. please locate some GeoJson file in your device for test it.
     */
    private void drawFromPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IRN.geo.json";
        new GeoJsonPluginBuilder()
                .withContext(this)
                .withMap(mapboxMap)
                .withFilePath(path)
                .withOnLoadingFilePath(new OnLoadingGeoJsonListener() {
                    @Override
                    public void onPreLoading() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoaded() {
                        Toast.makeText(GeoJsonActivity.this, "GeoJson data from path loaded", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadFailed(Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(GeoJsonActivity.this, "Error occur during load GeoJson data from path. see logcat", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                })
                .draw();
    }

    /**
     * request for ExternalStoragePermission. if permission is granted you can uncomment drawFromPath function
     */
    private void requestExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                //drawFromPath();
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            //drawFromPath();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //drawFromPath();
        }
    }
}