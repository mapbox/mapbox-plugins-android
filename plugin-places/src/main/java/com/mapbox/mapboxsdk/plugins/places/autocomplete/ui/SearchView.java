package com.mapbox.mapboxsdk.plugins.places.autocomplete.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mapbox.mapboxsdk.places.R;

public class SearchView extends LinearLayout implements ImageButton.OnClickListener, TextWatcher,
  LifecycleObserver, View.OnFocusChangeListener {

  @Nullable
  private BackButtonListener backButtonListener;
  @Nullable
  private ClearButtonListener clearButtonListener;
  @Nullable
  private QueryListener queryListener;
  @Nullable
  private QueryFocusListener focusListener;

  private final ImageView backButton;
  private final ImageView clearButton;
  private final EditText searchEditText;
  
  public SearchView(@NonNull Context context) {
    this(context, null);
  }

  public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflate(context, R.layout.mapbox_view_search, this);
    backButton = findViewById(R.id.button_search_back);
    clearButton = findViewById(R.id.button_search_clear);
    searchEditText = findViewById(R.id.edittext_search);
    initialize();
  }

  private void initialize() {
    backButton.setOnClickListener(this);
    clearButton.setOnClickListener(this);
    searchEditText.addTextChangedListener(this);
    searchEditText.setOnFocusChangeListener(this);
    ((LifecycleOwner) getContext()).getLifecycle().addObserver(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.button_search_back) {
      if (backButtonListener != null) {
        backButtonListener.onBackButtonPress();
      }
    } else {
      searchEditText.getText().clear();
      if (clearButtonListener != null) {
        clearButtonListener.onClearButtonPress();
      }
    }
  }

  @Override
  public void onFocusChange(View view, boolean hasFocus) {
    if (view.getId() == searchEditText.getId()) {
      if (focusListener != null) {
        focusListener.onSearchViewEditTextHasFocus();
      }
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  public void onDestroy() {
    backButtonListener = null;
    queryListener = null;
    focusListener = null;
    clearButtonListener = null;
  }

  public void setHint(String hint) {
    searchEditText.setHint(hint);
  }

  @Override
  public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
    if (queryListener != null) {
      queryListener.onQueryChange(charSequence);
    }
    clearButton.setVisibility(charSequence.length() > 0 ? View.VISIBLE : INVISIBLE);
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    // Not used
  }

  @Override
  public void afterTextChanged(Editable editable) {
    // Not used
  }

  public void setBackButtonListener(@Nullable BackButtonListener backButtonListener) {
    this.backButtonListener = backButtonListener;
  }

  public void setClearButtonListener(@Nullable ClearButtonListener clearButtonListener) {
    this.clearButtonListener = clearButtonListener;
  }

  public void setQueryListener(@Nullable QueryListener queryListener) {
    this.queryListener = queryListener;
  }

  public void setQueryFocusListener(@Nullable QueryFocusListener focusListener) {
    this.focusListener = focusListener;
  }
}