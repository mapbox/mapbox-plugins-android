package com.mapbox.plugins.places.autocomplete;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.geocoding.v5.models.CarmenFeature;
import com.mapbox.places.R;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

  private final List<CarmenFeature> results;
  private OnCardItemClickListener onItemClickListener;

  public SearchResultAdapter(List<CarmenFeature> results) {
    this.results = results;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate( R.layout.item_search_result, parent, false);
    return new ViewHolder(view);
  }

  public void setOnItemClickListener(OnCardItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (onItemClickListener != null) {
      holder.bind(results.get(position), onItemClickListener);
    }

    if (results.get(position).text() != null) {
      holder.placeNameView.setText(results.get(position).text());
    }

    if (results.get(position).properties().has("address")) {
      holder.addressView.setText(results.get(position).properties().getAsJsonPrimitive("address").getAsString());
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

  static class ViewHolder extends RecyclerView.ViewHolder {

    final TextView placeNameView;
    final TextView addressView;

    ViewHolder(View itemView) {
      super(itemView);
      placeNameView = itemView.findViewById(R.id.tv_place_name);
      addressView = itemView.findViewById(R.id.tv_address);
    }

    public void bind(final CarmenFeature carmenFeature, final OnCardItemClickListener listener) {
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onItemClick(carmenFeature);
        }
      });
    }
  }
}
