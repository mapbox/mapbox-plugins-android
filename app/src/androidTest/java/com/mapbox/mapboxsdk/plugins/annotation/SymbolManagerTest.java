// This file is generated.

package com.mapbox.mapboxsdk.plugins.annotation;

import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.testapp.activity.building.BuildingActivity;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import timber.log.Timber;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static com.mapbox.mapboxsdk.style.expressions.Expression.*;
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
    return BuildingActivity.class;
  }

  private void setupSymbolManager() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      symbolManager = new SymbolManager(((BuildingActivity) rule.getActivity()).getMapView(), mapboxMap);
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
      assertEquals(symbolManager.getSymbolPlacement(), (String) SYMBOL_PLACEMENT_POINT);
    });
  }

  @Test
  public void testSymbolSpacingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("symbol-spacing");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setSymbolSpacing(0.3f);
      assertEquals(symbolManager.getSymbolSpacing(), (Float) 0.3f);
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
      assertEquals(symbolManager.getSymbolAvoidEdges(), (Boolean) true);
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
      assertEquals(symbolManager.getIconAllowOverlap(), (Boolean) true);
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
      assertEquals(symbolManager.getIconIgnorePlacement(), (Boolean) true);
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
      assertEquals(symbolManager.getIconOptional(), (Boolean) true);
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
      assertEquals(symbolManager.getIconRotationAlignment(), (String) ICON_ROTATION_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testIconSizeAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-size");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconSizeExpression(get("hello"));
      assertEquals(symbolManager.getIconSizeExpression(), number(get("hello")));
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
      assertEquals(symbolManager.getIconTextFit(), (String) ICON_TEXT_FIT_NONE);
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
      assertEquals(symbolManager.getIconTextFitPadding(), (Float[]) new Float[] {0f, 0f, 0f, 0f});
    });
  }

  @Test
  public void testIconImageAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-image");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconImageExpression(get("hello"));
      assertEquals(symbolManager.getIconImageExpression(), Expression.toString(get("hello")));
    });
  }

  @Test
  public void testIconRotateAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-rotate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconRotateExpression(get("hello"));
      assertEquals(symbolManager.getIconRotateExpression(), number(get("hello")));
    });
  }

  @Test
  public void testIconPaddingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-padding");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconPadding(0.3f);
      assertEquals(symbolManager.getIconPadding(), (Float) 0.3f);
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
      assertEquals(symbolManager.getIconKeepUpright(), (Boolean) true);
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
      assertEquals(symbolManager.getIconPitchAlignment(), (String) ICON_PITCH_ALIGNMENT_MAP);
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
      assertEquals(symbolManager.getTextPitchAlignment(), (String) TEXT_PITCH_ALIGNMENT_MAP);
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
      assertEquals(symbolManager.getTextRotationAlignment(), (String) TEXT_ROTATION_ALIGNMENT_MAP);
    });
  }

  @Test
  public void testTextSizeAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-size");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextSizeExpression(get("hello"));
      assertEquals(symbolManager.getTextSizeExpression(), number(get("hello")));
    });
  }

  @Test
  public void testTextMaxWidthAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-max-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextMaxWidthExpression(get("hello"));
      assertEquals(symbolManager.getTextMaxWidthExpression(), number(get("hello")));
    });
  }

  @Test
  public void testTextLineHeightAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-line-height");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextLineHeight(0.3f);
      assertEquals(symbolManager.getTextLineHeight(), (Float) 0.3f);
    });
  }

  @Test
  public void testTextLetterSpacingAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-letter-spacing");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextLetterSpacingExpression(get("hello"));
      assertEquals(symbolManager.getTextLetterSpacingExpression(), number(get("hello")));
    });
  }

  @Test
  public void testTextMaxAngleAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-max-angle");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextMaxAngle(0.3f);
      assertEquals(symbolManager.getTextMaxAngle(), (Float) 0.3f);
    });
  }

  @Test
  public void testTextRotateAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-rotate");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextRotateExpression(get("hello"));
      assertEquals(symbolManager.getTextRotateExpression(), number(get("hello")));
    });
  }

  @Test
  public void testTextPaddingAsConstant() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-padding");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextPadding(0.3f);
      assertEquals(symbolManager.getTextPadding(), (Float) 0.3f);
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
      assertEquals(symbolManager.getTextKeepUpright(), (Boolean) true);
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
      assertEquals(symbolManager.getTextAllowOverlap(), (Boolean) true);
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
      assertEquals(symbolManager.getTextIgnorePlacement(), (Boolean) true);
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
      assertEquals(symbolManager.getTextOptional(), (Boolean) true);
    });
  }

  @Test
  public void testIconOpacityAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconOpacityExpression(get("hello"));
      assertEquals(symbolManager.getIconOpacityExpression(), number(get("hello")));
    });
  }

  @Test
  public void testIconHaloWidthAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-halo-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconHaloWidthExpression(get("hello"));
      assertEquals(symbolManager.getIconHaloWidthExpression(), number(get("hello")));
    });
  }

  @Test
  public void testIconHaloBlurAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("icon-halo-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setIconHaloBlurExpression(get("hello"));
      assertEquals(symbolManager.getIconHaloBlurExpression(), number(get("hello")));
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
      assertEquals(symbolManager.getIconTranslate(), (Float[]) new Float[] {0f, 0f});
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
      assertEquals(symbolManager.getIconTranslateAnchor(), (String) ICON_TRANSLATE_ANCHOR_MAP);
    });
  }

  @Test
  public void testTextOpacityAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-opacity");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextOpacityExpression(get("hello"));
      assertEquals(symbolManager.getTextOpacityExpression(), number(get("hello")));
    });
  }

  @Test
  public void testTextHaloWidthAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-halo-width");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextHaloWidthExpression(get("hello"));
      assertEquals(symbolManager.getTextHaloWidthExpression(), number(get("hello")));
    });
  }

  @Test
  public void testTextHaloBlurAsExpression() {
    validateTestSetup();
    setupSymbolManager();
    Timber.i("text-halo-blur");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertNotNull(symbolManager);

      symbolManager.setTextHaloBlurExpression(get("hello"));
      assertEquals(symbolManager.getTextHaloBlurExpression(), number(get("hello")));
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
      assertEquals(symbolManager.getTextTranslate(), (Float[]) new Float[] {0f, 0f});
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
      assertEquals(symbolManager.getTextTranslateAnchor(), (String) TEXT_TRANSLATE_ANCHOR_MAP);
    });
  }
}
