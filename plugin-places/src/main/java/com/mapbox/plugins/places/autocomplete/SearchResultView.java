package com.mapbox.plugins.places.autocomplete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mapbox.places.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultView extends CardView {

  public SearchResultView(@NonNull Context context) {
    this(context, null);
  }

  public SearchResultView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public SearchResultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  private void initialize(Context context) {
    inflate(context, R.layout.layout_cardview_search_result, this);
    initBackground();
    initializeResultList();
  }

  private void initializeResultList() {
    List<SearchResultModel> results = new ArrayList<>();
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));
    results.add(new SearchResultModel("hello", "world"));

    RecyclerView recyclerView = findViewById(R.id.rv_search_results);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(new SearchResultAdapter(results));


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
