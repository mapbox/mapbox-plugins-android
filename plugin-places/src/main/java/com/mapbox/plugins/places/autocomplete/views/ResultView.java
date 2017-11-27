package com.mapbox.plugins.places.autocomplete.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.OnCardItemClickListener;
import com.mapbox.plugins.places.autocomplete.SearchResultAdapter;

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
    adapter = new SearchResultAdapter(results);
  }

  void inflateView(Context context) {
    inflate(context, R.layout.view_results, this);
  }

  public void setOnItemClickListener(OnCardItemClickListener onItemClickListener) {
    if (adapter != null) {
      adapter.setOnItemClickListener(onItemClickListener);
    }
  }

  private void initializeResultList() {
//    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.line_divider);
//    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), VERTICAL);
//    dividerItemDecoration.setDrawable(drawable);

    RecyclerView recyclerView = findViewById(R.id.rv_search_results);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setNestedScrollingEnabled(false);
//    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setAdapter(adapter);
  }
}
