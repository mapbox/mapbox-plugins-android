package com.mapbox.plugins.places.details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static com.mapbox.plugins.places.common.GeocodingUtils.removeNameFromAddress;

public class PlaceDetailsBottomSheet extends CoordinatorLayout {

  private BottomSheetBehavior bottomSheetBehavior;
  private CoordinatorLayout rootView;
  private TextView placeNameTextView;
  private TextView placeAddressTextView;
  private ProgressBar placeProgressBar;

  public PlaceDetailsBottomSheet(Context context) {
    this(context, null);
  }

  public PlaceDetailsBottomSheet(Context context, AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public PlaceDetailsBottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  private void initialize(Context context) {
    rootView = (CoordinatorLayout) inflate(context, R.layout.view_bottom_sheet_container, this);
    bottomSheetBehavior = BottomSheetBehavior.from(rootView.findViewById(R.id.root_bottom_sheet));
    bottomSheetBehavior.setHideable(true);
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    bindViews();
  }

  private void bindViews() {
    placeNameTextView = findViewById(R.id.text_view_place_name);
    placeAddressTextView = findViewById(R.id.text_view_place_address);
    placeProgressBar = findViewById(R.id.progress_bar_place);
  }

  public void setPlaceDetails(@Nullable CarmenFeature carmenFeature) {
    if (!isShowing()) {
      toggleBottomSheet();
    }
    if (carmenFeature == null) {
      placeNameTextView.setText("");
      placeAddressTextView.setText("");
      placeProgressBar.setVisibility(VISIBLE);
      return;
    }
    placeProgressBar.setVisibility(INVISIBLE);

    placeNameTextView.setText(carmenFeature.text() == null ? "Dropped Pin" : carmenFeature.text());
    placeAddressTextView.setText(removeNameFromAddress(carmenFeature));
  }

  public void dismissPlaceDetails() {
    toggleBottomSheet();
  }

  public boolean isShowing() {
    return bottomSheetBehavior.getState() != STATE_HIDDEN;
  }

  private void toggleBottomSheet() {
    bottomSheetBehavior.setPeekHeight(rootView.findViewById(R.id.bottom_sheet_header).getHeight());
    bottomSheetBehavior.setHideable(isShowing());
    bottomSheetBehavior.setState(isShowing() ? STATE_HIDDEN : STATE_COLLAPSED);
  }
}
