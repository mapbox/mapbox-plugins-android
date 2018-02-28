package com.mapbox.mapboxsdk.plugins.offline.ui;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.R;
import com.mapbox.mapboxsdk.style.sources.VectorSource;
import com.mapbox.services.commons.geojson.Feature;

import java.util.List;

import timber.log.Timber;

public class RegionSelectionFragment extends Fragment implements OnMapReadyCallback,
  MapboxMap.OnCameraIdleListener {

  public static final String TAG = "OfflineRegionSelectionFragment";
  private static final String[] LAYER_IDS = new String[] {
    "place-city-lg-n", "place-city-lg-s", "place-city-md-n", "place-city-md-s", "place-city-sm"
  };
  private static final String[] SOURCE_LAYER_IDS = new String[] {
    "place_label", "state_label", "country_label"
  };

  private TextView regionNameTextView;
  private View selectionBoxView;
  private MapboxMap mapboxMap;
  private String regionName;
  private MapView mapView;
  private View rootView;
  private RectF boundingBox;

  public static RegionSelectionFragment newInstance() {
    return new RegionSelectionFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.mapbox_offline_region_selection_fragment, container, false);
    mapView = rootView.findViewById(R.id.mapbox_offline_region_selection_map_view);
    regionNameTextView = rootView.findViewById(R.id.mapbox_offline_region_name_text_view);
    selectionBoxView = rootView.findViewById(R.id.mapbox_offline_scrim_view);
    int top = selectionBoxView.getTop() - mapView.getTop();
    int left = selectionBoxView.getLeft() - mapView.getLeft();
    boundingBox = new RectF(left, top, left + selectionBoxView.getWidth(), top + selectionBoxView.getHeight());
    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.addOnCameraIdleListener(this);
  }

  @Override
  public void onCameraIdle() {
    Timber.v("Camera moved");
    regionName = getOfflineRegionName();
    regionNameTextView.setText(regionName);
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
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
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
  public void onDestroyView() {
    super.onDestroyView();
    mapView.onDestroy();
  }

  public String getOfflineRegionName() {
    List<Feature> featureList = mapboxMap.queryRenderedFeatures(boundingBox, LAYER_IDS);
    if (featureList.isEmpty()) {
      Timber.v("Rendered features empty, attempting to query vector source.");
      VectorSource source = mapboxMap.getSourceAs("composite");
      if (source != null) {
        featureList = source.querySourceFeatures(SOURCE_LAYER_IDS, null);
      }
    }
    if (!featureList.isEmpty() && featureList.get(0).getProperties().has("name")) {
      return featureList.get(0).getStringProperty("name");
    }
    return getString(R.string.mapbox_offline_default_region_name);
  }
}
