package com.mapbox.mapboxsdk.plugins.testapp.activity.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.widget.Toast;

import com.mapbox.mapboxsdk.plugins.locationpicker.LocationPickerActivity;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Requester activity for get location from Mapbox map.
 */
public class RequestLocationActivity extends AppCompatActivity {
  public static final int MAP_BUTTON_REQUEST_CODE = 1001;
  private static final double latitude = 37.769145;
  private static final double longitude = -122.447244;
  @BindView(R.id.switch_return_on_back_press)
  SwitchCompat switchReturnOnBackPress;
  @BindView(R.id.switch_show_satellite_button)
  SwitchCompat switchShowSatelliteButton;
  @BindView(R.id.switch_show_mylocation_button)
  SwitchCompat switchShowMylocationButton;
  @BindView(R.id.text_coordinates)
  AppCompatTextView textCoordinates;
  @BindView(R.id.text_address)
  AppCompatTextView textAddress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_request_location);
    ButterKnife.bind(this);
  }

  /**
   * create intent with posted parameters and start {@link LocationPickerActivity}
   */
  @OnClick(R.id.button_request_location)
  public void requestLocation() {
    Intent locationPickerIntent = new LocationPickerActivity.Builder()
      .withLocation(latitude, longitude)
      .withMapboxToken(getString(R.string.mapbox_access_token))
      .withReturnOnBackPressed(switchReturnOnBackPress.isChecked())
      .withSatelliteView(switchShowSatelliteButton.isChecked())
      .withMyLocationView(switchShowMylocationButton.isChecked())
      .build(getApplicationContext());
    startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MAP_BUTTON_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        double latitude = data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0);
        double longitude = data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0);
        String address = data.getStringExtra(LocationPickerActivity.ADDRESS);
        String latString = String.format(Locale.US, "%2.5f", latitude) + "° N";
        String lngString = String.format(Locale.US, "%2.5f", longitude) + "° E";
        textCoordinates.setText(String.format("%s , %s", latString, lngString));
        textAddress.setText(address);
      } else {
        textCoordinates.setText("-");
        textAddress.setText("-");
        Toast.makeText(this, "Request pick the location canceled.", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
