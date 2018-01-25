package com.mapbox.plugins.places.autocomplete.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;

import java.util.ArrayList;
import java.util.List;

public class ResultView extends LinearLayout {

  private final List<CarmenFeature> results;
  private SearchResultAdapter adapter;

  public ResultView(@NonNull Context context) {
    this(context, null);
  }

  public ResultView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public ResultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    results = new ArrayList<>();
    initialize(context);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initializeResultList();
  }

  public List<CarmenFeature> getResultsList() {
    return results;
  }

  public void notifyDataSetChanged() {
    adapter.notifyDataSetChanged();
  }

  private void initialize(Context context) {
    inflateView(context);
    adapter = new SearchResultAdapter(getContext(), results);
  }

  void inflateView(Context context) {
    inflate(context, R.layout.mapbox_plugins_view_results, this);
  }

  public void setOnItemClickListener(ResultClickCallback onItemClickListener) {
    if (adapter != null) {
      adapter.setOnItemClickListener(onItemClickListener);
    }
  }

  private void initializeResultList() {
    RecyclerView recyclerView = findViewById(R.id.rv_search_results);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    recyclerView.addItemDecoration(new ResultItemDecoration(getContext()));
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(adapter);
  }
}
