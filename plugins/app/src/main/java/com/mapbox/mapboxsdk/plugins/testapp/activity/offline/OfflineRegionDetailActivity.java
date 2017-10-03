package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.mapbox.mapboxsdk.plugins.offline.Callback;
import com.mapbox.mapboxsdk.plugins.offline.DownloadService;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownload;
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
public class OfflineRegionDetailActivity extends AppCompatActivity {

  private OfflinePlugin offlinePlugin;
  private OfflineRegion offlineRegion;
  private OfflineDownloadResultReceiver resultReceiver;
  private DownloadService.DownloadServiceBinder downloadServiceBinder;
  private boolean boundDownloadService = false;

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
      OfflineDownload offlineDownload = bundle.getParcelable(OfflineDownload.KEY_OBJECT);
      if (offlineDownload != null) {
        // coming from notification
        OfflineUtils.getRegion(
          OfflineManager.getInstance(this),
          offlineDownload.getRegionId(),
          offlineRegionCallback
        );
      } else {
        // coming from list activity
        OfflineUtils.getRegion(
          OfflineManager.getInstance(this),
          bundle.getLong(DownloadService.RegionConstants.ID),
          offlineRegionCallback
        );
      }
    } else {
      Toast.makeText(this, "invalid bundle for Activity", Toast.LENGTH_SHORT).show();
    }
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
        deleteView.setEnabled(false);
        offlineRegion.delete(offlineRegionDeleteCallback);
      }
    } else {
      // cancel ongoing downloads\
      OfflineDownload offlineDownload = offlinePlugin.getDownloadForRegion(offlineRegion);
      if (offlineDownload != null) {
        downloadServiceBinder.getService().cancelOngoingDownload(offlineDownload);
        stateView.setText("CANCELED");
      } else {
        Timber.e("couldn't cancel, is download already finished?");
      }
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();

    // listen to download status change updates (Success, Cancel & Error)
    registerReceiver(
      resultReceiver = new OfflineDownloadResultReceiver(),
      OfflineDownload.INTENT_FILTER
    );

    // bind Activity to Service to receive download status updates (percentage of downloaded offline region)
    Intent intent = new Intent(this, DownloadService.class);
    getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
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

    // stop listening to download status change updates
    unregisterReceiver(resultReceiver);

    // unbind from service to stop receiving download states updates
    if (boundDownloadService) {
      getApplicationContext().unbindService(connection);
      boundDownloadService = false;
    }
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

  //TODO make static inner class
  private class OfflineDownloadResultReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      String actionName = intent.getStringExtra(OfflineDownload.KEY_STATE);
      if (actionName.equals(OfflineDownload.STATE_FINISHED)) {
        OfflineDownload offlineDownload = intent.getParcelableExtra(OfflineDownload.KEY_OBJECT);
        stateView.setText("DOWNLOADED");
        progressBar.setVisibility(View.INVISIBLE);
      } else if (actionName.equals(OfflineDownload.STATE_CANCEL)) {
        finish(); // nothing to do in this screen, cancel = delete
      } else if (actionName.equals(OfflineDownload.STATE_ERROR)) {
        stateView.setText("ERROR");
        String error = intent.getStringExtra(OfflineDownload.KEY_BUNDLE_OFFLINE_REGION);
        String message = intent.getStringExtra(OfflineDownload.KEY_BUNDLE_ERROR);
        Toast.makeText(context, "Offline Download error: " + error + " " + message, Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * Connection to a the bound {@link DownloadService}. This connection is used to set a responder which is invoked
   * when the progress of a downloading offline region changes.
   */
  private final ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      downloadServiceBinder = (DownloadService.DownloadServiceBinder) service;
      downloadServiceBinder.getService().setDownloadServiceResponder(downloadServiceResponder);
      boundDownloadService = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      boundDownloadService = false;
    }
  };

  /**
   * Responder that is bound to the {@link DownloadService}. This responder is responsible for passing through the
   * percentage of downloaded tiles when the progress of a downloading offline region changes.
   */
  private final DownloadService.DownloadServiceResponder downloadServiceResponder =
    new DownloadService.DownloadServiceResponder() {
      @Override
      public void onDownloadProgressChanged(long offlineRegionId, int percentage) {
        if (offlineRegion == null) {
          return;
        }

        if (offlineRegionId == offlineRegion.getID()) {
          if (progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
          }
          progressBar.setProgress(percentage);
        }
      }
    };

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

  private final Callback<OfflineRegion> offlineRegionCallback = new Callback<OfflineRegion>() {
    @Override
    public void onResult(OfflineRegion region) {
      offlineRegion = region;
      OfflineTilePyramidRegionDefinition definition = (OfflineTilePyramidRegionDefinition) region.getDefinition();
      setupUI(definition);
    }

    @Override
    public void onError(String message) {
      Toast.makeText(
        OfflineRegionDetailActivity.this,
        "Error getting offline region state: " + message,
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
