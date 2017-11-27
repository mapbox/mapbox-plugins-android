package com.mapbox.plugins.places.autocomplete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mapbox.places.R;

public class SearchView extends LinearLayout implements ImageButton.OnClickListener, TextWatcher {

  @Nullable
  private BackButtonListener backButtonListener;
  @Nullable
  private QueryListener queryListener;

  private final ImageButton backButton;
  private final ImageButton clearButton;
  private final EditText searchEditText;

  public SearchView(@NonNull Context context) {
    this(context, null);
  }

  public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflate(context, R.layout.view_search, this);
    backButton = findViewById(R.id.button_search_back);
    clearButton = findViewById(R.id.button_search_clear);
    searchEditText = findViewById(R.id.edittext_search);
    initialize();
  }

  private void initialize() {
    backButton.setOnClickListener(this);
    clearButton.setOnClickListener(this);
    searchEditText.addTextChangedListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.button_search_back) {
      if (backButtonListener != null) {
        backButtonListener.onBackButtonPress();
      }
    } else {
      searchEditText.getText().clear();
    }
  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (queryListener != null) {
      queryListener.onQueryChange(charSequence);
    }
    clearButton.setVisibility(charSequence.length() > 0 ? View.VISIBLE : INVISIBLE);
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    // Not used
  }

  @Override
  public void afterTextChanged(Editable editable) {
    // Not used
  }

  public void setBackButtonListener(@Nullable BackButtonListener backButtonListener) {
    this.backButtonListener = backButtonListener;
  }

  public void removeBackButtonListener() {
    backButtonListener = null;
  }

  public void setQueryListener(@Nullable QueryListener queryListener) {
    this.queryListener = queryListener;
  }

  public void removeQueryListener() {
    queryListener = null;
  }

  public interface QueryListener {
    void onQueryChange(CharSequence charSequence);
  }

  public interface BackButtonListener {
    void onBackButtonPress();
  }
}