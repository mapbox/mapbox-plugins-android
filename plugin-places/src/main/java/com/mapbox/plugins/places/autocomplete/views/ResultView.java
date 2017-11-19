package com.mapbox.plugins.places.autocomplete.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;
import com.mapbox.plugins.places.autocomplete.SearchResultAdapter;
import com.mapbox.plugins.places.common.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

public class ResultView extends LinearLayout implements NestedScrollView.OnScrollChangeListener {

  private NestedScrollView searchResultsScrollView;
  private final List<CarmenFeature> results;
  private SearchResultAdapter adapter;
  private View dropShadow;

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
    searchResultsScrollView = findViewById(R.id.nested_scroll_view_search_results);
    dropShadow = findViewById(R.id.scroll_drop_shadow);
    initBackground();
    initializeResultList();
    searchResultsScrollView.setOnScrollChangeListener(this);
  }

  @Override
  public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    KeyboardUtils.hideKeyboard(searchResultsScrollView);

    if (v.canScrollVertically(-1)) {
      dropShadow.setVisibility(VISIBLE);
      // Show elevation
    } else {
      dropShadow.setVisibility(INVISIBLE);
      // Remove elevation
    }
  }

  public List<CarmenFeature> getResultsList() {
    return results;
  }

  public void notifyDataSetChanged() {
    adapter.notifyDataSetChanged();
  }

  private void initialize(Context context) {
    inflate(context, R.layout.layout_cardview_search_result, this);
    adapter = new SearchResultAdapter(results);
  }

  private void initializeResultList() {
    RecyclerView recyclerView = findViewById(R.id.rv_search_results);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
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
