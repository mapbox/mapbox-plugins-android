package com.mapbox.plugins.places.autocomplete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mapbox.places.R;

public class SearchBarView extends CardView implements ImageButton.OnClickListener, TextWatcher {

  ImageButton backButton;
  ImageButton clearButton;
  EditText searchEditText;

  public SearchBarView(@NonNull Context context) {
    this(context, null);
  }

  public SearchBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public SearchBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  private void initialize(Context context) {
    inflate(context, R.layout.layout_cardview_search_bar, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    backButton = findViewById(R.id.button_search_back);
    searchEditText = findViewById(R.id.edittext_search);
    clearButton = findViewById(R.id.button_search_clear);
    initBackground();

    backButton.setOnClickListener(this);
    clearButton.setOnClickListener(this);
    searchEditText.addTextChangedListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.button_search_back) {
      Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
    } else {
      searchEditText.getText().clear();
    }
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    clearButton.setVisibility(charSequence.length() > 0 ? View.VISIBLE : INVISIBLE);
  }

  @Override
  public void afterTextChanged(Editable editable) {

  }


  private void initBackground() {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
      // Hide the background
      getBackground().setAlpha(0);
    } else {
      setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }
  }


}
