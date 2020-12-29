// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.graphics.PointF;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static org.junit.Assert.*;
import static com.mapbox.mapboxsdk.style.layers.Property.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic smoke tests for Symbol
 */
@RunWith(AndroidJUnit4.class)
public class SymbolTest extends BaseActivityTest {

  private Symbol symbol;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupAnnotation() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      SymbolManager symbolManager = new SymbolManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
      symbol = symbolManager.create(new SymbolOptions().withLatLng(new LatLng()));
    });
  }

  @Test
  public void testSymbolSortKey() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("symbol-sort-key");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setSymbolSortKey(2.0f);
      assertEquals((Float) symbol.getSymbolSortKey(), (Float) 2.0f);
    });
  }

  @Test
  public void testIconSize() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-size");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconSize(2.0f);
      assertEquals((Float) symbol.getIconSize(), (Float) 2.0f);
    });
  }

  @Test
  public void testIconImage() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-image");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconImage("undefined");
      assertEquals((String) symbol.getIconImage(), (String) "undefined");
    });
  }

  @Test
  public void testIconRotate() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-rotate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconRotate(2.0f);
      assertEquals((Float) symbol.getIconRotate(), (Float) 2.0f);
    });
  }

  @Test
  public void testIconOffset() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-offset");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconOffset(new PointF(1.0f, 1.0f));
      assertEquals(symbol.getIconOffset(), new PointF(1.0f, 1.0f));
    });
  }

  @Test
  public void testIconAnchor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconAnchor(ICON_ANCHOR_CENTER);
      assertEquals((String) symbol.getIconAnchor(), (String) ICON_ANCHOR_CENTER);
    });
  }

  @Test
  public void testTextField() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-field");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextField("");
      assertEquals((String) symbol.getTextField(), (String) "");
    });
  }

  @Test
  public void testTextFont() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-font");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextFont(new String[]{"Open Sans Regular", "Arial Unicode MS Regular"});
      assertEquals((String[]) symbol.getTextFont(), (String[]) new String[]{"Open Sans Regular", "Arial Unicode MS Regular"});
    });
  }

  @Test
  public void testTextSize() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-size");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextSize(2.0f);
      assertEquals((Float) symbol.getTextSize(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextMaxWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-max-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextMaxWidth(2.0f);
      assertEquals((Float) symbol.getTextMaxWidth(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextLetterSpacing() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-letter-spacing");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextLetterSpacing(2.0f);
      assertEquals((Float) symbol.getTextLetterSpacing(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextJustify() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-justify");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextJustify(TEXT_JUSTIFY_AUTO);
      assertEquals((String) symbol.getTextJustify(), (String) TEXT_JUSTIFY_AUTO);
    });
  }

  @Test
  public void testTextRadialOffset() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-radial-offset");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextRadialOffset(2.0f);
      assertEquals((Float) symbol.getTextRadialOffset(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextAnchor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextAnchor(TEXT_ANCHOR_CENTER);
      assertEquals((String) symbol.getTextAnchor(), (String) TEXT_ANCHOR_CENTER);
    });
  }

  @Test
  public void testTextRotate() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-rotate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextRotate(2.0f);
      assertEquals((Float) symbol.getTextRotate(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextTransform() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-transform");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextTransform(TEXT_TRANSFORM_NONE);
      assertEquals((String) symbol.getTextTransform(), (String) TEXT_TRANSFORM_NONE);
    });
  }

  @Test
  public void testTextOffset() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-offset");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextOffset(new PointF(1.0f, 1.0f));
      assertEquals(symbol.getTextOffset(), new PointF(1.0f, 1.0f));
    });
  }

  @Test
  public void testIconOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconOpacity(2.0f);
      assertEquals((Float) symbol.getIconOpacity(), (Float) 2.0f);
    });
  }

  @Test
  public void testIconColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconColor("rgba(0, 0, 0, 1)");
      assertEquals(symbol.getIconColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testIconColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);
      symbol.setIconColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(symbol.getIconColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testIconHaloColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-halo-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconHaloColor("rgba(0, 0, 0, 1)");
      assertEquals(symbol.getIconHaloColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testIconHaloColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-halo-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);
      symbol.setIconHaloColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(symbol.getIconHaloColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testIconHaloWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-halo-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconHaloWidth(2.0f);
      assertEquals((Float) symbol.getIconHaloWidth(), (Float) 2.0f);
    });
  }

  @Test
  public void testIconHaloBlur() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("icon-halo-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setIconHaloBlur(2.0f);
      assertEquals((Float) symbol.getIconHaloBlur(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextOpacity() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextOpacity(2.0f);
      assertEquals((Float) symbol.getTextOpacity(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextColor("rgba(0, 0, 0, 1)");
      assertEquals(symbol.getTextColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testTextColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);
      symbol.setTextColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(symbol.getTextColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testTextHaloColor() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-halo-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextHaloColor("rgba(0, 0, 0, 1)");
      assertEquals(symbol.getTextHaloColor(), "rgba(0, 0, 0, 1)");
    });
  }

  @Test
  public void testTextHaloColorAsInt() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-halo-color");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);
      symbol.setTextHaloColor(ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
      assertEquals(symbol.getTextHaloColorAsInt(), ColorUtils.rgbaToColor("rgba(0, 0, 0, 1)"));
    });
  }


  @Test
  public void testTextHaloWidth() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-halo-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextHaloWidth(2.0f);
      assertEquals((Float) symbol.getTextHaloWidth(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextHaloBlur() {
    validateTestSetup();
    setupAnnotation();
    Timber.i("text-halo-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbol);

      symbol.setTextHaloBlur(2.0f);
      assertEquals((Float) symbol.getTextHaloBlur(), (Float) 2.0f);
    });
  }
}
