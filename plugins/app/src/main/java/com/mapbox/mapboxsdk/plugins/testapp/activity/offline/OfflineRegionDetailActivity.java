package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.DownloadService;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownload;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownloadChangeListener;
import com.mapbox.mapboxsdk.plugins.offline.OfflinePlugin;
import com.mapbox.mapboxsdk.plugins.offline.OfflineUtils;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Activity showing the detail of an offline region.
 * <p>
 * This Activity can bind to the DownloadService and show progress.
 * </p>
 * <p>
 * This Activity listens to broadcast events related to successful, canceled and errored download.
 * </p>
 */
public class OfflineRegionDetailActivity extends AppCompatActivity implements OfflineDownloadChangeListener {

  private OfflinePlugin offlinePlugin;
  private OfflineRegion offlineRegion;

  @BindView(R.id.mapView)
  MapView mapView;

  @BindView(R.id.fab_delete)
  FloatingActionButton deleteView;

  @BindView(R.id.region_state)
  TextView stateView;

  @BindView(R.id.region_state_progress)
  ProgressBar progressBar;

  @BindView(R.id.region_name)
  TextView nameView;

  @BindView(R.id.region_style_url)
  TextView styleView;

  @BindView(R.id.region_min_zoom)
  TextView minZoomView;

  @BindView(R.id.region_max_zoom)
  TextView maxZoomView;

  @BindView(R.id.region_lat_lng_bounds)
  TextView latLngBoundsView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offline_region_detail);
    ButterKnife.bind(this);
    mapView.onCreate(savedInstanceState);

    offlinePlugin = OfflinePlugin.getInstance();

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      long regionId;
      OfflineDownload offlineDownload = bundle.getParcelable(OfflineDownload.KEY_OBJECT);
      if (offlineDownload != null) {
        // coming from notification
        regionId = offlineDownload.getRegionId();
      } else {
        // coming from list
        regionId = bundle.getLong(DownloadService.RegionConstants.ID, -1);
      }

      if (regionId != -1) {
        loadOfflineRegion(regionId);
      }
    }
  }

  private void loadOfflineRegion(final long id) {
    OfflineManager.getInstance(this)
      .listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {

        @Override
        public void onList(OfflineRegion[] offlineRegions) {
          for (OfflineRegion region : offlineRegions) {
            Timber.e("REGION ID %s", region.getID());
            if (region.getID() == id) {
              offlineRegion = region;
              OfflineTilePyramidRegionDefinition definition =
                (OfflineTilePyramidRegionDefinition) region.getDefinition();
              setupUI(definition);
              return;
            }
          }
          throw new RuntimeException("OfflineRegion not found, id not found for: " + id);
        }

        @Override
        public void onError(String error) {
          throw new RuntimeException("Error while retrieving OfflineRegion: " + error);
        }
      });
  }


  private void setupUI(final OfflineTilePyramidRegionDefinition definition) {
    // update map
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        // correct style
        mapboxMap.setStyle(definition.getStyleURL());

        // position map on top of offline region
        CameraPosition cameraPosition = OfflineUtils.getCameraPosition(definition);
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // restrict camera movement
        mapboxMap.setMinZoomPreference(definition.getMinZoom());
        mapboxMap.setMaxZoomPreference(definition.getMaxZoom());
        mapboxMap.setLatLngBoundsForCameraTarget(definition.getBounds());
      }
    });

    // update textview data
    nameView.setText(OfflineUtils.convertRegionName(offlineRegion.getMetadata()));
    styleView.setText(definition.getStyleURL());
    latLngBoundsView.setText(definition.getBounds().toString());
    minZoomView.setText(String.valueOf(definition.getMinZoom()));
    maxZoomView.setText(String.valueOf(definition.getMaxZoom()));
    offlineRegion.getStatus(offlineRegionStatusCallback);
  }

  @OnClick(R.id.fab_delete)
  public void onFabClick(View view) {
    if (view.getTag() == null || (boolean) view.getTag()) {
      if (offlineRegion != null) {
        offlineRegion.delete(offlineRegionDeleteCallback);
        view.setVisibility(View.GONE);
      }
    } else {
      // cancel ongoing download
      OfflineDownload offlineDownload = offlinePlugin.getActiveDownloadForOfflineRegion(offlineRegion);
      if (offlineDownload != null) {
        offlinePlugin.cancelDownload(this, offlineDownload);
        stateView.setText("CANCELED");
        view.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onCreate(OfflineDownload offlineDownload) {
    Timber.e("OfflineDownload created %s", offlineDownload.hashCode());
  }

  @Override
  public void onSuccess(OfflineDownload offlineDownload) {
    stateView.setText("DOWNLOADED");
    progressBar.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onCancel(OfflineDownload offlineDownload) {
    finish(); // nothing to do in this screen, cancel = delete
  }

  @Override
  public void onError(OfflineDownload offlineDownload, String error, String message) {
    progressBar.setVisibility(View.INVISIBLE);
    stateView.setText("ERROR");
  }

  @Override
  public void onProgress(OfflineDownload offlineDownload, int progress) {
    if (offlineRegion == null) {
      return;
    }

    if (offlineDownload.getRegionId() == offlineRegion.getID()) {
      if (progressBar.getVisibility() != View.VISIBLE) {
        progressBar.setVisibility(View.VISIBLE);
      }
      progressBar.setProgress(progress);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
    offlinePlugin.addOfflineDownloadStateChangeListener(this);
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
    offlinePlugin.removeOfflineDownloadStateChangeListener(this);
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

  /**
   * Callback invoked when the states of an offline region changes.
   */
  private final OfflineRegion.OfflineRegionStatusCallback offlineRegionStatusCallback =
    new OfflineRegion.OfflineRegionStatusCallback() {
      @Override
      public void onStatus(OfflineRegionStatus status) {
        int downloadState = status.getDownloadState();
        if (downloadState == OfflineRegion.STATE_ACTIVE) {
          FloatingActionButton actionButton = findViewById(R.id.fab_delete);
          if (!status.isComplete()) {
            // set fab to cancel
            actionButton.setImageResource(R.drawable.ic_cancel_black_24dp);
            findViewById(R.id.fab_delete).setTag(false);
            stateView.setText("DOWNLOADING");
          } else {
            // set fab to delete
            actionButton.setImageResource(R.drawable.ic_delete);
            findViewById(R.id.fab_delete).setTag(true);
            stateView.setText("DOWNLOADED");
          }
        } else {
          stateView.setText("PAUSED");
        }
      }

      @Override
      public void onError(String error) {
        Toast.makeText(
          OfflineRegionDetailActivity.this,
          "Error getting offline region state: " + error,
          Toast.LENGTH_SHORT).show();
      }
    };

  private final OfflineRegion.OfflineRegionDeleteCallback offlineRegionDeleteCallback =
    new OfflineRegion.OfflineRegionDeleteCallback() {
      @Override
      public void onDelete() {
        Toast.makeText(
          OfflineRegionDetailActivity.this,
          "Region deleted.",
          Toast.LENGTH_SHORT).show();
        finish();
      }

      @Override
      public void onError(String error) {
        deleteView.setEnabled(true);
        Toast.makeText(
          OfflineRegionDetailActivity.this,
          "Error getting offline region state: " + error,
          Toast.LENGTH_SHORT).show();
      }
    };
}
