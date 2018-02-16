package com.mapbox.plugins.places.autocomplete.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;
import com.mapbox.plugins.places.common.PlaceConstants;

import java.util.List;

public class SearchResultAdapter
  extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {

  private static final String ADDRESS = "address";

  @Nullable
  private ResultClickCallback resultClickCallback;


  private final List<CarmenFeature> results;
  private final Context context;

  public SearchResultAdapter(Context context, List<CarmenFeature> results) {
    this.results = results;
    this.context = context;
  }

  @Override
  public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.mapbox_item_search_result, parent, false);
    return new SearchViewHolder(view);
  }

  public void setOnItemClickListener(ResultClickCallback resultClickCallback) {
    this.resultClickCallback = resultClickCallback;
  }

  @Override
  public void onBindViewHolder(SearchViewHolder holder, int position) {
    if (resultClickCallback != null) {
      holder.bind(results.get(position), resultClickCallback);
    }

    if (results.get(position).properties().has(PlaceConstants.SAVED_PLACE)) {
      holder.placeNameView.setTextColor(ContextCompat.getColor(context, R.color.mapbox_plugins_bright_blue));
    }

    if (results.get(position).text() != null) {
      holder.placeNameView.setText(results.get(position).text());
    }

    if (results.get(position).properties().has(ADDRESS)) {
      holder.addressView.setText(results.get(position).properties()
        .getAsJsonPrimitive(ADDRESS).getAsString());
    } else if (results.get(position).placeName() != null) {
      holder.addressView.setText(results.get(position).placeName());
    } else {
      holder.addressView.setHeight(0);
    }
  }

  @Override
  public int getItemCount() {
    return null != results ? results.size() : 0;
  }

  static class SearchViewHolder extends RecyclerView.ViewHolder {

    private final TextView placeNameView;
    private final TextView addressView;

    SearchViewHolder(View itemView) {
      super(itemView);
      placeNameView = itemView.findViewById(R.id.tv_place_name);
      addressView = itemView.findViewById(R.id.tv_address);
    }

    public void bind(final CarmenFeature carmenFeature, final ResultClickCallback listener) {
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onClick(carmenFeature);
        }
      });
    }
  }
}
