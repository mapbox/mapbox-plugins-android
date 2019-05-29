package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;

/**
 * This click interface notifies when the clear button
 * is clickedin the {@link SearchView} to clear the query text in the EditText.
 */
public interface ClearButtonListener {

  void onClearButtonPress();

  void onCancel();

}