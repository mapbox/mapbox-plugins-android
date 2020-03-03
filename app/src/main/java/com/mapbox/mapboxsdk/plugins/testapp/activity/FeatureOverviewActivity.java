package com.mapbox.mapboxsdk.plugins.testapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.plugins.testapp.R;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

/**
 * Activity showing a RecyclerView with Activities generated from AndroidManifest.xml
 */
public class FeatureOverviewActivity extends AppCompatActivity implements PermissionsListener {

  private static final String KEY_STATE_FEATURES = "featureList";

  private RecyclerView recyclerView;
  private FeatureSectionAdapter sectionAdapter;
  private List<Feature> features;
  private PermissionsManager permissionsManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feature_overview);

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    recyclerView.setHasFixedSize(true);

    ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, view) -> {
      if (!sectionAdapter.isSectionHeaderPosition(position)) {
        int itemPosition = sectionAdapter.getConvertedPosition(position);
        Feature feature = features.get(itemPosition);
        startFeature(feature);
      }
    });

    if (savedInstanceState == null) {
      loadFeatures();
    } else {
      features = savedInstanceState.getParcelableArrayList(KEY_STATE_FEATURES);
      onFeaturesLoaded(features);
    }

    // Check for location permission
    permissionsManager = new PermissionsManager(this);
    if (!PermissionsManager.areLocationPermissionsGranted(this)) {
      recyclerView.setEnabled(false);
      permissionsManager.requestLocationPermissions(this);
    }
  }

  private void loadFeatures() {
    try {
      new LoadFeatureTask(this).execute(
        getPackageManager().getPackageInfo(getPackageName(),
          PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA));
    } catch (PackageManager.NameNotFoundException exception) {
      Timber.e(exception, "Could not resolve package info");
    }
  }

  private void onFeaturesLoaded(List<Feature> featuresList) {
    features = featuresList;

    List<Section> sections = new ArrayList<>();
    String currentCat = "";
    for (int i = 0; i < features.size(); i++) {
      String category = features.get(i).getCategory();
      if (!currentCat.equals(category)) {
        sections.add(new Section(i, category));
        currentCat = category;
      }
    }

    Section[] dummy = new Section[sections.size()];
    sectionAdapter = new FeatureSectionAdapter(
      this, R.layout.section_feature, R.id.section_text, new FeatureAdapter(features));
    sectionAdapter.setSections(sections.toArray(dummy));
    recyclerView.setAdapter(sectionAdapter);
  }

  private void startFeature(Feature feature) {
    Intent intent = new Intent();
    intent.setComponent(new ComponentName(getPackageName(), feature.getName()));
    startActivity(intent);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    Toast.makeText(this, "This app needs location permissions in order to show its functionality.",
      Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      recyclerView.setEnabled(true);
    } else {
      Toast.makeText(this, "You didn't grant location permissions.",
        Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(KEY_STATE_FEATURES, (ArrayList<Feature>) features);
  }

  private static class LoadFeatureTask extends AsyncTask<PackageInfo, Void, List<Feature>> {

    private WeakReference<FeatureOverviewActivity> contextWeakReference;

    LoadFeatureTask(FeatureOverviewActivity context) {
      this.contextWeakReference = new WeakReference<>(context);
    }

    @Override
    protected List<Feature> doInBackground(PackageInfo... params) {
      List<Feature> features = new ArrayList<>();
      PackageInfo app = params[0];

      Context context = contextWeakReference.get();
      if (context != null) {
        String packageName = context.getPackageName();
        String metaDataKey = context.getResources().getString(R.string.category);
        for (ActivityInfo info : app.activities) {
          if (info.labelRes != 0 && info.name.startsWith(packageName)
            && !info.name.equals(FeatureOverviewActivity.class.getName())) {
            String label = context.getString(info.labelRes);
            String description = resolveString(context, info.descriptionRes);
            String category = resolveMetaData(info.metaData, metaDataKey);
            if (category != null) {
              features.add(new Feature(info.name, label, description, category));
            }
          }
        }
      }

      if (!features.isEmpty()) {
        Comparator<Feature> comparator = (lhs, rhs) -> {
          int result = lhs.getCategory().compareToIgnoreCase(rhs.getCategory());
          if (result == 0) {
            result = lhs.getLabel().compareToIgnoreCase(rhs.getLabel());
          }
          return result;
        };
        Collections.sort(features, comparator);
      }

      return features;
    }

    private String resolveMetaData(Bundle bundle, String key) {
      String category = null;
      if (bundle != null) {
        category = bundle.getString(key);
      }
      return category;
    }

    private String resolveString(Context context, @StringRes int stringRes) {
      try {
        return context.getString(stringRes);
      } catch (Resources.NotFoundException exception) {
        return "-";
      }
    }

    @Override
    protected void onPostExecute(List<Feature> features) {
      super.onPostExecute(features);
      FeatureOverviewActivity activity = contextWeakReference.get();
      if (activity != null) {
        activity.onFeaturesLoaded(features);
      }
    }
  }

  private static class Feature implements Parcelable {

    private String name;
    private String label;
    private String description;
    private String category;

    Feature(String name, String label, String description, String category) {
      this.name = name;
      this.label = label;
      this.description = description;
      this.category = category;
    }

    private Feature(Parcel in) {
      name = in.readString();
      label = in.readString();
      description = in.readString();
      category = in.readString();
    }

    String getName() {
      return name;
    }

    String getSimpleName() {
      String[] split = name.split("\\.");
      return split[split.length - 1];
    }

    String getLabel() {
      return label != null ? label : getSimpleName();
    }

    String getDescription() {
      return description != null ? description : "-";
    }

    String getCategory() {
      return category;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      out.writeString(name);
      out.writeString(label);
      out.writeString(description);
      out.writeString(category);
    }

    public static final Parcelable.Creator<Feature> CREATOR = new Parcelable.Creator<Feature>() {
      @Override
      public Feature createFromParcel(Parcel in) {
        return new Feature(in);
      }

      @Override
      public Feature[] newArray(int size) {
        return new Feature[size];
      }
    };
  }

  private static class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    private List<Feature> features;

    static class ViewHolder extends RecyclerView.ViewHolder {

      TextView labelView;
      TextView descriptionView;

      ViewHolder(View view) {
        super(view);
        Typeface typeface = FontCache.get("Roboto-Regular.ttf", view.getContext());
        labelView = view.findViewById(R.id.nameView);
        labelView.setTypeface(typeface);
        descriptionView = view.findViewById(R.id.descriptionView);
        descriptionView.setTypeface(typeface);
      }
    }

    FeatureAdapter(List<Feature> features) {
      this.features = features;
    }

    @NonNull
    @Override
    public FeatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.labelView.setText(features.get(position).getLabel());
      holder.descriptionView.setText(features.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
      return features.size();
    }
  }

  private static class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    static Typeface get(String name, Context context) {
      Typeface tf = fontCache.get(name);
      if (tf == null) {
        try {
          tf = Typeface.createFromAsset(context.getAssets(), name);
          fontCache.put(name, tf);
        } catch (Exception exception) {
          Timber.e("Font not found");
        }
      }
      return tf;
    }
  }

  private static class FeatureSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 0;

    private final Context context;
    private final SparseArray<Section> sections;
    private final RecyclerView.Adapter adapter;

    @LayoutRes
    private final int sectionRes;

    @IdRes
    private final int textRes;

    private boolean valid = true;

    FeatureSectionAdapter(Context ctx, int sectionResourceId, int textResourceId,
                          RecyclerView.Adapter baseAdapter) {
      context = ctx;
      sectionRes = sectionResourceId;
      textRes = textResourceId;
      adapter = baseAdapter;
      sections = new SparseArray<>();
      adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
          valid = adapter.getItemCount() > 0;
          notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
          valid = adapter.getItemCount() > 0;
          notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
          valid = adapter.getItemCount() > 0;
          notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
          valid = adapter.getItemCount() > 0;
          notifyItemRangeRemoved(positionStart, itemCount);
        }
      });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int typeView) {
      if (typeView == SECTION_TYPE) {
        final View view = LayoutInflater.from(context).inflate(sectionRes, parent, false);
        return new SectionViewHolder(view, textRes);
      } else {
        return adapter.onCreateViewHolder(parent, typeView - 1);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder sectionViewHolder, int position) {
      if (isSectionHeaderPosition(position)) {
        String cleanTitle = sections.get(position).title.toString().replace("_", " ");
        ((SectionViewHolder) sectionViewHolder).title.setText(cleanTitle);
      } else {
        //noinspection unchecked
        adapter.onBindViewHolder(sectionViewHolder, getConvertedPosition(position));
      }
    }

    @Override
    public int getItemViewType(int position) {
      return isSectionHeaderPosition(position)
        ? SECTION_TYPE
        : adapter.getItemViewType(getConvertedPosition(position)) + 1;
    }

    void setSections(Section[] sections) {
      this.sections.clear();

      Arrays.sort(sections, (section, section1) -> Integer.compare(section.firstPosition, section1.firstPosition));

      int offset = 0;
      for (Section section : sections) {
        section.sectionedPosition = section.firstPosition + offset;
        this.sections.append(section.sectionedPosition, section);
        ++offset;
      }

      notifyDataSetChanged();
    }

    int getConvertedPosition(int sectionedPosition) {
      if (isSectionHeaderPosition(sectionedPosition)) {
        return RecyclerView.NO_POSITION;
      }

      int offset = 0;
      for (int i = 0; i < sections.size(); i++) {
        if (sections.valueAt(i).sectionedPosition > sectionedPosition) {
          break;
        }
        --offset;
      }
      return sectionedPosition + offset;
    }

    boolean isSectionHeaderPosition(int position) {
      return sections.get(position) != null;
    }


    @Override
    public long getItemId(int position) {
      return isSectionHeaderPosition(position)
        ? Integer.MAX_VALUE - sections.indexOfKey(position)
        : adapter.getItemId(getConvertedPosition(position));
    }

    @Override
    public int getItemCount() {
      return (valid ? adapter.getItemCount() + sections.size() : 0);
    }
  }

  private static class SectionViewHolder extends RecyclerView.ViewHolder {

    private TextView title;

    SectionViewHolder(@NonNull View view, @IdRes int textRes) {
      super(view);
      title = view.findViewById(textRes);
      title.setTypeface(FontCache.get("Roboto-Medium.ttf", view.getContext()));
    }
  }

  private static class Section {
    int firstPosition;
    int sectionedPosition;
    CharSequence title;

    Section(int firstPosition, CharSequence title) {
      this.firstPosition = firstPosition;
      this.title = title;
    }

    public CharSequence getTitle() {
      return title;
    }
  }

  private static class ItemClickSupport {
    private final RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (onItemClickListener != null) {
          RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
          onItemClickListener.onItemClicked(recyclerView, holder.getAdapterPosition(), view);
        }
      }
    };
    private RecyclerView.OnChildAttachStateChangeListener attachListener =
      new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
          if (onItemClickListener != null) {
            view.setOnClickListener(onClickListener);
          }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
      };

    private ItemClickSupport(RecyclerView recyclerView) {
      this.recyclerView = recyclerView;
      this.recyclerView.setTag(R.id.item_click_support, this);
      this.recyclerView.addOnChildAttachStateChangeListener(attachListener);
    }

    static ItemClickSupport addTo(RecyclerView view) {
      ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
      if (support == null) {
        support = new ItemClickSupport(view);
      }
      return support;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
      onItemClickListener = listener;
    }

    interface OnItemClickListener {

      void onItemClicked(RecyclerView recyclerView, int position, View view);
    }
  }
}