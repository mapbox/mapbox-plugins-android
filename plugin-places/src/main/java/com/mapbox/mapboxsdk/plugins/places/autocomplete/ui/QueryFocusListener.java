package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;

/**
 * This interface is used to notify when the search UI EditText is focused on
 * in the {@link SearchView}. This is helpful for adjusting the cardview height in
 * {@link com.mapbox.mapboxsdk.plugins.places.picker.ui.PlacePickerActivity}
 * as a user interacts with the search UI in the toolbar.
 */
public interface QueryFocusListener {

  void onSearchViewEditTextHasFocus();

  void onCancel();

}