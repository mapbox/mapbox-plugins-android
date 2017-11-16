package com.mapbox.plugins.places.autocomplete;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.places.R;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

  private List<SearchResultModel> results;

  public SearchResultAdapter(List<SearchResultModel> results) {
    this.results = results;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_search_result, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.placeNameView.setText(results.get(position).getTitle());
    holder.addressView.setText(results.get(position).getDescription());
  }

  @Override
  public int getItemCount() {
    return null != results ? results.size() : 0;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    final TextView placeNameView;
    final TextView addressView;

    ViewHolder(View itemView) {
      super(itemView);
      placeNameView = itemView.findViewById(R.id.tv_place_name);
      addressView = itemView.findViewById(R.id.tv_address);
    }
  }
}
