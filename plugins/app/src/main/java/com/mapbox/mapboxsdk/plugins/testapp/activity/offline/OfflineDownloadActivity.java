package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.NotificationOptions;
import com.mapbox.mapboxsdk.plugins.offline.OfflineDownloadOptions;
import com.mapbox.mapboxsdk.plugins.offline.OfflinePlugin;
import com.mapbox.mapboxsdk.plugins.offline.OfflineUtils;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity showing a form to configure the download of an offline region.
 */
public class OfflineDownloadActivity extends AppCompatActivity {

  @BindView(R.id.edittext_region_name)
  EditText regionNameView;

  @BindView(R.id.edittext_lat_north)
  EditText latNorthView;

  @BindView(R.id.edittext_lon_east)
  EditText lonEastView;

  @BindView(R.id.edittext_lat_south)
  EditText latSouthView;

  @BindView(R.id.edittext_lon_west)
  EditText lonWestView;

  @BindView(R.id.spinner_style_url)
  Spinner styleUrlView;

  @BindView(R.id.seekbar_min_zoom)
  SeekBar minZoomView;

  @BindView(R.id.seekbar_max_zoom)
  SeekBar maxZoomView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offline);
    ButterKnife.bind(this);
    initUi();
  }

  private void initUi() {
    initEditTexts();
    initSeekbars();
    initSpinner();
  }

  private void initEditTexts() {
    regionNameView.setText("Region name");
    latNorthView.setText("40.7589372691904");
    lonEastView.setText("-73.96024123810196");
    latSouthView.setText("40.740763489055496");
    lonWestView.setText("-73.97569076188057");
  }

  private void initSeekbars() {
    int maxZoom = (int) MapboxConstants.MAXIMUM_ZOOM;
    minZoomView.setMax(maxZoom);
    minZoomView.setProgress(14);
    maxZoomView.setMax(maxZoom);
    maxZoomView.setProgress(maxZoom);
  }

  private void initSpinner() {
    List<String> styles = new ArrayList<>();
    styles.add(Style.MAPBOX_STREETS);
    styles.add(Style.DARK);
    styles.add(Style.LIGHT);
    styles.add(Style.OUTDOORS);
    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styles);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    styleUrlView.setAdapter(spinnerArrayAdapter);
  }

  @OnClick(R.id.fab)
  public void onDownloadRegion() {
    // get data from UI
    String regionName = regionNameView.getText().toString();
    double latitudeNorth = Double.parseDouble(latNorthView.getText().toString());
    double longitudeEast = Double.parseDouble(lonEastView.getText().toString());
    double latitudeSouth = Double.parseDouble(latSouthView.getText().toString());
    double longitudeWest = Double.parseDouble(lonWestView.getText().toString());
    String styleUrl = (String) styleUrlView.getSelectedItem();
    float maxZoom = maxZoomView.getProgress();
    float minZoom = minZoomView.getProgress();

    // create offline definition from data
    OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
      styleUrl,
      new LatLngBounds.Builder()
        .include(new LatLng(latitudeNorth, longitudeEast))
        .include(new LatLng(latitudeSouth, longitudeWest))
        .build(),
      minZoom,
      maxZoom,
      getResources().getDisplayMetrics().density
    );

    // customise notification appearance
    NotificationOptions notificationOptions = new NotificationOptions()
      .withSmallIconRes(R.drawable.mapbox_logo_icon)
      .withReturnActivity(OfflineRegionDetailActivity.class.getName());

    // start offline download
    OfflinePlugin.getInstance().startDownload(this,
      new OfflineDownloadOptions()
        .withDefinition(definition)
        .withMetadata(OfflineUtils.convertRegionName(regionName))
        .withNotificationOptions(notificationOptions)
    );
  }
}
