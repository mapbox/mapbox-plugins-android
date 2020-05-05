package com.mapbox.mapboxsdk.plugins.testapp.activity.annotation;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Line;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.Utils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Test activity showcasing the Polyline annotations API.
 * <p>
 * Shows how to add and remove polylines.
 * </p>
 */
public class LineChangeActivity extends AppCompatActivity {

  private static final LatLng ANDORRA = new LatLng(42.505777, 1.52529);
  private static final LatLng LUXEMBOURG = new LatLng(49.815273, 6.129583);
  private static final LatLng MONACO = new LatLng(43.738418, 7.424616);
  private static final LatLng VATICAN_CITY = new LatLng(41.902916, 12.453389);
  private static final LatLng SAN_MARINO = new LatLng(43.942360, 12.457777);
  private static final LatLng LIECHTENSTEIN = new LatLng(47.166000, 9.555373);

  private static final float FULL_ALPHA = 1.0f;
  private static final float PARTIAL_ALPHA = 0.5f;
  private static final float NO_ALPHA = 0.0f;

  private List<Line> lines;

  private LineManager lineManager;
  private MapView mapView;

  private boolean fullAlpha = true;
  private boolean visible = true;
  private boolean width = true;
  private boolean color = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_annotation);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> {
      mapboxMap.moveCamera(
        CameraUpdateFactory.newLatLngZoom(
          new LatLng(47.798202, 7.573781),
          4)
      );

      mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS), style -> {
        findViewById(R.id.fabStyles).setOnClickListener(v -> mapboxMap.setStyle(Utils.INSTANCE.getNextStyle()));

        lineManager = new LineManager(mapView, mapboxMap, style);
        lines = lineManager.create(getAllPolylines());
        lineManager.addClickListener(line -> {
          Toast.makeText(
              LineChangeActivity.this,
              "Clicked: " + line.getId(),
              Toast.LENGTH_SHORT).show();
          return false;
        });

        LineManager dottedLineManger = new LineManager(mapView, mapboxMap, style);
        dottedLineManger.create(new LineOptions()
          .withLinePattern("airfield-11")
          .withLineWidth(5.0f)
          .withGeometry(LineString.fromLngLats(new ArrayList<Point>() {{
            add(Point.fromLngLat(9.997167, 53.547476));
            add(Point.fromLngLat(12.587986, 55.675313));
          }})));
      });
    });

    View fab = findViewById(R.id.fabStyles);
    fab.setVisibility(View.VISIBLE);
    fab.setOnClickListener(view -> {
      if (lineManager != null) {
        if (lines.size() == 1) {
          // test for removing annotation
          lineManager.delete(lines.get(0));
        } else {
          // test for removing annotations
          lineManager.delete(lines);
        }
      }
      lines.clear();
      lines.add(lineManager.create(getRandomLine()));
    });
  }

  private List<LineOptions> getAllPolylines() {
    List<LineOptions> options = new ArrayList<>();
    options.add(generatePolyline(ANDORRA, LUXEMBOURG, "#F44336"));
    options.add(generatePolyline(ANDORRA, MONACO, "#FF5722"));
    options.add(generatePolyline(MONACO, VATICAN_CITY, "#673AB7"));
    options.add(generatePolyline(VATICAN_CITY, SAN_MARINO, "#009688"));
    options.add(generatePolyline(SAN_MARINO, LIECHTENSTEIN, "#795548"));
    options.add(generatePolyline(LIECHTENSTEIN, LUXEMBOURG, "#3F51B5"));
    return options;
  }

  private LineOptions generatePolyline(LatLng start, LatLng end, String color) {
    LineOptions line = new LineOptions().withLatLngs(new ArrayList<LatLng>() {{
      add(start);
      add(end);
    }});

    line.withLineColor(ColorUtils.colorToRgbaString(Color.parseColor(color)));
    line.withLineWidth(3.0f);
    return line;
  }

  public LineOptions getRandomLine() {
    final List<LineOptions> randomLines = getAllPolylines();
    Collections.shuffle(randomLines);
    return randomLines.get(0);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (lineManager != null) {
      lineManager.onDestroy();
    }

    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_change_line, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_id_remove:
        lineManager.delete(lines);
        lines.clear();
        break;
      case R.id.action_id_alpha:
        fullAlpha = !fullAlpha;
        for (Line p : lines) {
          p.setLineOpacity(fullAlpha ? FULL_ALPHA : PARTIAL_ALPHA);
        }
        break;
      case R.id.action_id_color:
        color = !color;
        for (Line p : lines) {
          p.setLineColor(color ? Color.RED : Color.BLUE);
        }
        break;
      case R.id.action_id_width:
        width = !width;
        for (Line p : lines) {
          p.setLineWidth(width ? 3.0f : 5.0f);
        }
        break;
      case R.id.action_id_visible:
        visible = !visible;
        for (Line p : lines) {
          p.setLineOpacity(visible ? (fullAlpha ? FULL_ALPHA : PARTIAL_ALPHA) : NO_ALPHA);
        }
        break;
      default:
        return super.onOptionsItemSelected(item);
    }
    lineManager.update(lines);
    return true;
  }
}