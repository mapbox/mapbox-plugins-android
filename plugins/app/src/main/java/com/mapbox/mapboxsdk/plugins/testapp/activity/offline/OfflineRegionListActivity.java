package com.mapbox.mapboxsdk.plugins.testapp.activity.offline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.plugins.offline.DownloadService;
import com.mapbox.mapboxsdk.plugins.offline.OfflineUtils;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activity showing a list of offline regions.
 */
public class OfflineRegionListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  private OfflineRegionAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_offline_region_list);

    ListView listView = (ListView) findViewById(R.id.listView);
    listView.setAdapter(adapter = new OfflineRegionAdapter(this));
    listView.setEmptyView(findViewById(android.R.id.empty));
    listView.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    OfflineRegion region = adapter.getItem(position);
    Intent intent = new Intent(this, OfflineRegionDetailActivity.class);
    intent.putExtra(DownloadService.RegionConstants.ID, region.getID());
    startActivity(intent);
  }

  private void delete(OfflineRegion region) {
    region.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
      @Override
      public void onDelete() {
        Toast.makeText(
          OfflineRegionListActivity.this,
          "Region deleted",
          Toast.LENGTH_SHORT
        ).show();
        loadOfflineRegions();
      }

      @Override
      public void onError(String error) {
        Toast.makeText(
          OfflineRegionListActivity.this,
          "Region deletion failed with " + error,
          Toast.LENGTH_LONG
        ).show();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    loadOfflineRegions();
  }

  private void loadOfflineRegions() {
    OfflineManager.getInstance(this).listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
      @Override
      public void onList(OfflineRegion[] offlineRegions) {
        if (offlineRegions != null && offlineRegions.length > 0) {
          adapter.setOfflineRegions(Arrays.asList(offlineRegions));
        }
      }

      @Override
      public void onError(String error) {
        Toast.makeText(OfflineRegionListActivity.this, "Error loading regions " + error, Toast.LENGTH_LONG).show();
      }
    });
  }

  private static class OfflineRegionAdapter extends BaseAdapter {

    private Context context;
    private List<OfflineRegion> offlineRegions;
    private OfflineRegion currentRegion;

    OfflineRegionAdapter(Context ctx) {
      context = ctx;
      offlineRegions = new ArrayList<>();
    }

    void setOfflineRegions(List<OfflineRegion> offlineRegions) {
      this.offlineRegions = offlineRegions;
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return offlineRegions.size();
    }

    @Override
    public OfflineRegion getItem(int position) {
      return offlineRegions.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;

      if (convertView == null) {
        holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.item_feature, parent, false);
        holder.text = (TextView) convertView.findViewById(R.id.nameView);
        holder.subText = (TextView) convertView.findViewById(R.id.descriptionView);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }

      currentRegion = getItem(position);
      holder.text.setText(OfflineUtils.convertRegionName(currentRegion.getMetadata()));
      holder.subText.setText(((OfflineTilePyramidRegionDefinition) currentRegion.getDefinition()).getStyleURL());
      return convertView;
    }

    static class ViewHolder {
      TextView text;
      TextView subText;
    }
  }
}