package com.mapbox.mapboxsdk.plugins.offline;

import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

import timber.log.Timber;

public class OfflineUtils {

  private static final String JSON_CHARSET = "UTF-8";
  private static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

  public static String convertRegionName(@NonNull byte[] metadata) {
    try {
      String json = new String(metadata, JSON_CHARSET);
      JSONObject jsonObject = new JSONObject(json);
      return jsonObject.getString(JSON_FIELD_REGION_NAME);
    } catch (Exception exception) {
      return null;
    }
  }

  public static byte[] convertRegionName(String regionName) {
    byte[] metadata = null;
    try {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(JSON_FIELD_REGION_NAME, regionName);
      String json = jsonObject.toString();
      metadata = json.getBytes(JSON_CHARSET);
    } catch (Exception exception) {
      Timber.e("Failed to encode metadata: " + exception.getMessage());
    }
    return metadata;
  }

  public static CameraPosition getCameraPosition(OfflineTilePyramidRegionDefinition definition) {
    return new CameraPosition.Builder()
      .target(definition.getBounds().getCenter())
      .zoom(definition.getMinZoom())
      .build();
  }

  public static void getRegion(OfflineManager offlineManager, final long id, final Callback<OfflineRegion> callback) {
    offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
      @Override
      public void onList(OfflineRegion[] offlineRegions) {
        for (OfflineRegion offlineRegion : offlineRegions) {
          if (offlineRegion.getID() == id) {
            if (callback != null) {
              callback.onResult(offlineRegion);
              return;
            }
          }
        }
        throw new RuntimeException("OfflineRegion not found, id not found for: " + id);
      }

      @Override
      public void onError(String error) {
        throw new RuntimeException("Error while retrieving OfflineRegion: " + error);
      }
    });
  }
}
