package com.mapbox.plugins.places.autocomplete.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class StarredView extends CardView {

  private final List<CarmenFeature> results;
  private SearchResultAdapter adapter;

  public StarredView(@NonNull Context context) {
    this(context, null);
  }

  public StarredView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public StarredView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    results = new ArrayList<>();
    initialize(context);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initBackground();
    initializeResultList();
  }

  public List<CarmenFeature> getResultsList() {
    return results;
  }

  public void notifyDataSetChanged() {
    adapter.notifyDataSetChanged();
  }

  private void initializeResultList() {
    RecyclerView recyclerView = findViewById(R.id.rv_search_results);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(adapter);
  }

  private void initialize(Context context) {
    inflate(context, R.layout.layout_card_results, this);
    adapter = new SearchResultAdapter(results);
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
