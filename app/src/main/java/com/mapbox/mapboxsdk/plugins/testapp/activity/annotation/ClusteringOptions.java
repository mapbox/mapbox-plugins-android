package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;

import java.util.HashMap;

/**
 * Builder class for configuring multi-layer clustering with Layer objects in an AnnotationManager.
 *
 * @see GeoJsonOptions
 * @see <a href="https://github.com/mapbox/mapbox-gl-native/blob/2a8359b0280e861450f9bdf390a01e0363202b3a/platform/android/MapboxGLAndroidSDK/src/main/java/com/mapbox/mapboxsdk/style/sources/GeoJsonOptions.java">The source code</a>
 */

public class ClusteringOptions extends HashMap<String, Object> {

    /**
     * GeoJsonOptions object containing any cluster properties for the GeoJsonSource
     *
     * @param geoJsonOptions the geoJsonOptions object
     * @return the current instance for chaining
     */
    @NonNull
    public ClusteringOptions withGeoJsonOptions(GeoJsonOptions geoJsonOptions) {
        this.put("geoJsonOptions", geoJsonOptions);
        return this;
    }

    /**
     * Point ranges used to define layers.
     *
     * @param pointRanges array of integers representing the lower bound on number of points in a cluster
     * @return the current instance for chaining
     */
    @NonNull
    public ClusteringOptions withPointRanges(int[] pointRanges) {
        this.put("pointRanges", pointRanges);
        return this;
    }
}
