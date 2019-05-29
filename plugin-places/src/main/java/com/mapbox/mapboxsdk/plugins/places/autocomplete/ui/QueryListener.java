package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;

/**
 * This click interface notifies when the search query text in the
 * {@link SearchView} EditText changes.
 */
public interface QueryListener {

  void onQueryChange(CharSequence charSequence);

  void onCancel();

}
