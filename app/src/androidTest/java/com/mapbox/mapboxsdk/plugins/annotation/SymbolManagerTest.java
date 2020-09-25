// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static org.junit.Assert.*;
import static com.mapbox.mapboxsdk.style.layers.Property.*;

/**
 * Basic smoke tests for SymbolManager
 */
@RunWith(AndroidJUnit4.class)
public class SymbolManagerTest extends BaseActivityTest {

  private SymbolManager symbolManager;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupSymbolManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      symbolManager = new SymbolManager(idlingResource.getMapView(), mapboxMap, Objects.requireNonNull(mapboxMap.getStyle()));
    });
  }

  @Test
  public void testSymbolPlacementAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("symbol-placement");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setSymbolPlacement(SYMBOL_PLACEMENT_POINT);
      assertEquals((String) symbolManager.getSymbolPlacement(), (String) SYMBOL_PLACEMENT_POINT);
    });
  }

  @Test
  public void testSymbolSpacingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("symbol-spacing");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setSymbolSpacing(2.0f);
      assertEquals((Float) symbolManager.getSymbolSpacing(), (Float) 2.0f);
    });
  }

  @Test
  public void testSymbolAvoidEdgesAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("symbol-avoid-edges");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setSymbolAvoidEdges(true);
      assertEquals((Boolean) symbolManager.getSymbolAvoidEdges(), (Boolean) true);
    });
  }

  @Test
  public void testIconAllowOverlapAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-allow-overlap");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconAllowOverlap(true);
      assertEquals((Boolean) symbolManager.getIconAllowOverlap(), (Boolean) true);
    });
  }

  @Test
  public void testIconIgnorePlacementAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-ignore-placement");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconIgnorePlacement(true);
      assertEquals((Boolean) symbolManager.getIconIgnorePlacement(), (Boolean) true);
    });
  }

  @Test
  public void testIconOptionalAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-optional");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconOptional(true);
      assertEquals((Boolean) symbolManager.getIconOptional(), (Boolean) true);
    });
  }

  @Test
  public void testIconRotationAlignmentAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-rotation-alignment");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconRotationAlignment(ICON_ROTATION_ALIGNMENT_MAP);
      assertEquals((String) symbolManager.getIconRotationAlignment(), (String) ICON_ROTATION_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testIconTextFitAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-text-fit");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconTextFit(ICON_TEXT_FIT_NONE);
      assertEquals((String) symbolManager.getIconTextFit(), (String) ICON_TEXT_FIT_NONE);
    });
  }

  @Test
  public void testIconTextFitPaddingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-text-fit-padding");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconTextFitPadding(new Float[] {0f, 0f, 0f, 0f});
      assertEquals((Float[]) symbolManager.getIconTextFitPadding(), (Float[]) new Float[] {0f, 0f, 0f, 0f});
    });
  }

  @Test
  public void testIconPaddingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-padding");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconPadding(2.0f);
      assertEquals((Float) symbolManager.getIconPadding(), (Float) 2.0f);
    });
  }

  @Test
  public void testIconKeepUprightAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-keep-upright");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconKeepUpright(true);
      assertEquals((Boolean) symbolManager.getIconKeepUpright(), (Boolean) true);
    });
  }

  @Test
  public void testIconPitchAlignmentAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-pitch-alignment");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconPitchAlignment(ICON_PITCH_ALIGNMENT_MAP);
      assertEquals((String) symbolManager.getIconPitchAlignment(), (String) ICON_PITCH_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testTextPitchAlignmentAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-pitch-alignment");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextPitchAlignment(TEXT_PITCH_ALIGNMENT_MAP);
      assertEquals((String) symbolManager.getTextPitchAlignment(), (String) TEXT_PITCH_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testTextRotationAlignmentAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-rotation-alignment");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextRotationAlignment(TEXT_ROTATION_ALIGNMENT_MAP);
      assertEquals((String) symbolManager.getTextRotationAlignment(), (String) TEXT_ROTATION_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testTextLineHeightAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-line-height");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextLineHeight(2.0f);
      assertEquals((Float) symbolManager.getTextLineHeight(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextVariableAnchorAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-variable-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextVariableAnchor(new String[]{TEXT_ANCHOR_RIGHT, TEXT_ANCHOR_TOP});
      assertEquals((String[]) symbolManager.getTextVariableAnchor(), (String[]) new String[]{TEXT_ANCHOR_RIGHT, TEXT_ANCHOR_TOP});
    });
  }

  @Test
  public void testTextMaxAngleAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-max-angle");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextMaxAngle(2.0f);
      assertEquals((Float) symbolManager.getTextMaxAngle(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextPaddingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-padding");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextPadding(2.0f);
      assertEquals((Float) symbolManager.getTextPadding(), (Float) 2.0f);
    });
  }

  @Test
  public void testTextKeepUprightAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-keep-upright");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextKeepUpright(true);
      assertEquals((Boolean) symbolManager.getTextKeepUpright(), (Boolean) true);
    });
  }

  @Test
  public void testTextAllowOverlapAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-allow-overlap");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextAllowOverlap(true);
      assertEquals((Boolean) symbolManager.getTextAllowOverlap(), (Boolean) true);
    });
  }

  @Test
  public void testTextIgnorePlacementAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-ignore-placement");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextIgnorePlacement(true);
      assertEquals((Boolean) symbolManager.getTextIgnorePlacement(), (Boolean) true);
    });
  }

  @Test
  public void testTextOptionalAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-optional");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextOptional(true);
      assertEquals((Boolean) symbolManager.getTextOptional(), (Boolean) true);
    });
  }

  @Test
  public void testIconTranslateAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-translate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconTranslate(new Float[] {0f, 0f});
      assertEquals((Float[]) symbolManager.getIconTranslate(), (Float[]) new Float[] {0f, 0f});
    });
  }

  @Test
  public void testIconTranslateAnchorAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-translate-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconTranslateAnchor(ICON_TRANSLATE_ANCHOR_MAP);
      assertEquals((String) symbolManager.getIconTranslateAnchor(), (String) ICON_TRANSLATE_ANCHOR_MAP);
    });
  }

  @Test
  public void testTextTranslateAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-translate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextTranslate(new Float[] {0f, 0f});
      assertEquals((Float[]) symbolManager.getTextTranslate(), (Float[]) new Float[] {0f, 0f});
    });
  }

  @Test
  public void testTextTranslateAnchorAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-translate-anchor");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextTranslateAnchor(TEXT_TRANSLATE_ANCHOR_MAP);
      assertEquals((String) symbolManager.getTextTranslateAnchor(), (String) TEXT_TRANSLATE_ANCHOR_MAP);
    });
  }
}
