package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import timber.log.Timber;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

/**
 * Test activity showcasing adding a large amount of Symbols.
 */
public class ClusteringActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MapView mapView;
    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clustering);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::initMap);
    }

    private void initMap(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(38.87031, -77.00897), 10
                )
        );

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS), style -> {
            findViewById(R.id.fabStyles).setOnClickListener(v -> mapboxMap.setStyle(Utils.INSTANCE.getNextStyle()));
            /* Create a GeoJsonOptions instance to specify the clustering parameters. */
            GeoJsonOptions geoJsonOptions = new GeoJsonOptions()
                    .withCluster(true)
                    .withClusterMaxZoom(14)
                    .withClusterRadius(10);
//            symbolManager = new SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions);
//            symbolManager.setIconAllowOverlap(true);
//            loadData(0);
            addClusteredGeoJsonSource(style);
        });
    }

    private void initLayerIcons(@NonNull Style loadedMapStyle) {
//        loadedMapStyle.addImage("single-quake-icon-id", BitmapUtils.getBitmapFromDrawable(
//                getResources().getDrawable(R.drawable.single_quake_icon)));
//        loadedMapStyle.addImage("quake-triangle-icon-id", BitmapUtils.getBitmapFromDrawable(
//                getResources().getDrawable(R.drawable.earthquake_triangle)));
    }

    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) {
// Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            loadedMapStyle.addSource(
// Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
// 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                    new GeoJsonSource("earthquakes",
                            new URI("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );
        } catch (URISyntaxException uriSyntaxException) {
            Timber.e("Check the URL %s" , uriSyntaxException.getMessage());
        }

//Creating a SymbolLayer icon layer for single data/icon points
        loadedMapStyle.addLayer(new SymbolLayer("unclustered-points", "earthquakes").withProperties(
                iconImage("fire-station-11"),
                iconSize(
                        division(
                                get("mag"), literal(4.0f)
                        )
                )
        ));

// Use the earthquakes GeoJSON source to create three point ranges.
        int[] layers = new int[] {150, 20, 0};

        for (int i = 0; i < layers.length; i++) {
//Add clusters' SymbolLayers images
            SymbolLayer symbolLayer = new SymbolLayer("cluster-" + i, "earthquakes");

            symbolLayer.setProperties(
                    iconImage("fire-station-11")
            );
            Expression pointCount = toNumber(get("point_count"));

// Add a filter to the cluster layer that hides the icons based on "point_count"
            symbolLayer.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i]))
                    ) : all(has("point_count"),
                            gte(pointCount, literal(layers[i])),
                            lt(pointCount, literal(layers[i - 1]))
                    )
            );
            loadedMapStyle.addLayer(symbolLayer);
        }

//Add a SymbolLayer for the cluster data number point count
        loadedMapStyle.addLayer(new SymbolLayer("count", "earthquakes").withProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.BLACK),
                textIgnorePlacement(true),
// The .5f offset moves the data numbers down a little bit so that they're
// in the middle of the triangle cluster image
                textOffset(new Float[] {0f, .5f}),
                textAllowOverlap(true)
        ));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mapboxMap.getStyle() == null || !mapboxMap.getStyle().isFullyLoaded()) {
            return;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing selected, nothing to do!
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
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
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

//    private SymbolManager symbolManager;
//    private List<Symbol> symbols = new ArrayList<>();
//
//    private MapboxMap mapboxMap;
//    private MapView mapView;
//    private FeatureCollection locations;
//    private ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_clustering);
//
//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this::initMap);
//    }
//
//    private void initMap(MapboxMap mapboxMap) {
//        this.mapboxMap = mapboxMap;
//        mapboxMap.moveCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                        new LatLng(38.87031, -77.00897), 10
//                )
//        );
//
//        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS), style -> {
//            findViewById(R.id.fabStyles).setOnClickListener(v -> mapboxMap.setStyle(Utils.INSTANCE.getNextStyle()));
//            /* Create a GeoJsonOptions instance to specify the clustering parameters. */
//            GeoJsonOptions geoJsonOptions = new GeoJsonOptions()
//                    .withCluster(true)
//                    .withClusterMaxZoom(14)
//                    .withClusterRadius(10);
//            symbolManager = new SymbolManager(mapView, mapboxMap, style, null, geoJsonOptions);
//            symbolManager.setIconAllowOverlap(true);
//            loadData(0);
//            addClusteredGeoJsonSource(style);
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
//                this, R.array.bulk_marker_list, android.R.layout.simple_spinner_item);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        getMenuInflater().inflate(R.menu.menu_bulk_symbol, menu);
//        MenuItem item = menu.findItem(R.id.spinner);
//        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//        spinner.setAdapter(spinnerAdapter);
//        spinner.setSelection(0, false);
//        spinner.setOnItemSelectedListener(ClusteringActivity.this);
//        return true;
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if (mapboxMap.getStyle() == null || !mapboxMap.getStyle().isFullyLoaded()) {
//            return;
//        }
//
//        loadData(position);
//    }
//
//    private void loadData(int position) {
//        int amount = Integer.valueOf(getResources().getStringArray(R.array.bulk_marker_list)[position]);
//        if (locations == null) {
//            if (progressDialog == null) {
//                progressDialog = ProgressDialog.show(this, "Loading", "Fetching markers", false, true);
//            }
//            new LoadLocationTask(this, amount).execute();
//        } else {
//            showMarkers(amount);
//        }
//    }
//
//    private void onLatLngListLoaded(FeatureCollection featureCollection, int amount) {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//        locations = featureCollection;
//        showMarkers(amount);
//    }
//
//    private void showMarkers(int amount) {
//        if (mapboxMap == null || locations == null || locations.features() == null || mapView.isDestroyed()) {
//            return;
//        }
//
//        // delete old symbols
//        symbolManager.delete(symbols);
//
//        if (locations.features().size() < amount) {
//            amount = locations.features().size();
//        }
//
//        showSymbols(amount);
//    }
//
//    private void showSymbols(int amount) {
//        List<SymbolOptions> options = new ArrayList<>();
//        Random random = new Random();
//        int randomIndex;
//
//        List<Feature> features = locations.features();
//        if (features == null) {
//            return;
//        }
//
//        for (int i = 0; i < amount; i++) {
//            randomIndex = random.nextInt(features.size());
//            Feature feature = features.get(randomIndex);
//            options.add(new SymbolOptions()
//                    .withGeometry((Point) feature.geometry())
//                    .withIconImage("fire-station-11")
//            );
//        }
//        symbols = symbolManager.create(options);
//    }
//
//    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) {
//        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
//        try {
//            loadedMapStyle.addSource(
//                    // Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
//                    // 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
//                    new GeoJsonSource("earthquakes",
//                            new URI("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
//                            new GeoJsonOptions()
//                                    .withCluster(true)
//                                    .withClusterMaxZoom(14)
//                                    .withClusterRadius(50)
//                    )
//            );
//        } catch (URISyntaxException uriSyntaxException) {
//            Timber.e("Check the URL %s", uriSyntaxException.getMessage());
//        }
//
//        //Creating a SymbolLayer icon layer for single data/icon points
//        loadedMapStyle.addLayer(new SymbolLayer("unclustered-points", "earthquakes").withProperties(
//                iconImage("fire-station-11"),
//                iconSize(
//                        division(
//                                get("mag"), literal(4.0f)
//                        )
//                )
//        ));
//
//        // Use the earthquakes GeoJSON source to create three point ranges.
//        int[] layers = new int[]{150, 20, 0};
//
//        for (int i = 0; i < layers.length; i++) {
//            //Add clusters' SymbolLayers images
//            SymbolLayer symbolLayer = new SymbolLayer("cluster-" + i, "earthquakes");
//
//            symbolLayer.setProperties(
//                    iconImage("fire-station-11")
//            );
//            Expression pointCount = toNumber(get("point_count"));
//
//            // Add a filter to the cluster layer that hides the icons based on "point_count"
//            symbolLayer.setFilter(
//                    i == 0
//                            ? all(has("point_count"),
//                            gte(pointCount, literal(layers[i]))
//                    ) : all(has("point_count"),
//                            gte(pointCount, literal(layers[i])),
//                            lt(pointCount, literal(layers[i - 1]))
//                    )
//            );
//            loadedMapStyle.addLayer(symbolLayer);
//        }
//
//        //Add a SymbolLayer for the cluster data number point count
//        loadedMapStyle.addLayer(new SymbolLayer("count", "earthquakes").withProperties(
//                textField(Expression.toString(get("point_count"))),
//                textSize(12f),
//                textColor(Color.BLACK),
//                textIgnorePlacement(true),
//                // The .5f offset moves the data numbers down a little bit so that they're
//                // in the middle of the triangle cluster image
//                textOffset(new Float[]{0f, .5f}),
//                textAllowOverlap(true)
//        ));
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        // nothing selected, nothing to do!
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (symbolManager != null) {
//            symbolManager.onDestroy();
//        }
//
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    private static class LoadLocationTask extends AsyncTask<Void, Integer, FeatureCollection> {
//
//        private WeakReference<ClusteringActivity> activity;
//        private int amount;
//
//        private LoadLocationTask(ClusteringActivity activity, int amount) {
//            this.amount = amount;
//            this.activity = new WeakReference<>(activity);
//        }
//
//        @Override
//        protected FeatureCollection doInBackground(Void... params) {
//            ClusteringActivity activity = this.activity.get();
//            if (activity != null) {
//                String json = null;
//                try {
//                    json = GeoParseUtil.loadStringFromAssets(activity.getApplicationContext());
//                } catch (IOException exception) {
//                    Timber.e(exception, "Could not add markers");
//                }
//
//                if (json != null) {
//                    return FeatureCollection.fromJson(json);
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(FeatureCollection locations) {
//            super.onPostExecute(locations);
//            ClusteringActivity activity = this.activity.get();
//            if (activity != null) {
//                activity.onLatLngListLoaded(locations, amount);
//            }
//        }
//    }
//
//    public static class GeoParseUtil {
//
//        static String loadStringFromAssets(final Context context) throws IOException {
//            InputStream is = context.getAssets().open("points.geojson");
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//            return readAll(rd);
//        }
//
//        private static String readAll(Reader rd) throws IOException {
//            StringBuilder sb = new StringBuilder();
//            int cp;
//            while ((cp = rd.read()) != -1) {
//                sb.append((char) cp);
//            }
//            return sb.toString();
//        }
//    }
}
