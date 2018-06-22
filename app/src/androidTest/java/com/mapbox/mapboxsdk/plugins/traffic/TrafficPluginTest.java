package com.mapbox.mapboxsdk.plugins.traffic;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResourceTimeoutException;
import android.support.test.espresso.UiController;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TrafficActivity;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.plugins.utils.OnMapReadyIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin.Local;
import static com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin.MotorWay;
import static com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin.Primary;
import static com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin.Secondary;
import static com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin.TrafficData;
import static com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin.Trunk;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TrafficPluginTest {

  @Rule
  public ActivityTestRule<TrafficActivity> rule = new ActivityTestRule<>(TrafficActivity.class);

  private static final String ROUND = "round";

  private OnMapReadyIdlingResource idlingResource;
  private MapboxMap mapboxMap;
  private TrafficPlugin trafficPlugin;

  @Before
  public void beforeTest() {
    try {
      Timber.e("@Before: register idle resource");
      idlingResource = new OnMapReadyIdlingResource(rule.getActivity());
      Espresso.registerIdlingResources(idlingResource);
      onView(withId(android.R.id.content)).check(matches(isDisplayed()));
      mapboxMap = idlingResource.getMapboxMap();
      trafficPlugin = rule.getActivity().getTrafficPlugin();
    } catch (IdlingResourceTimeoutException idlingResourceTimeoutException) {
      Timber.e("Idling resource timed out. Couldn't not validate if map is ready.");
      throw new RuntimeException("Could not start executeTrafficTest for " + this.getClass().getSimpleName() + ".\n"
        + "The ViewHierarchy doesn't contain a view with resource id = R.id.mapView or \n"
        + "the Activity doesn't contain an instance variable with a name equal to mapboxMap.\n");
    }
  }

  @Test
  public void sanity() throws Exception {
    assertTrue(mapboxMap != null);
    assertTrue(trafficPlugin != null);
  }

  @Test
  public void enabled() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController uiController) {
        assertTrue(trafficPlugin.isVisible());
      }
    });
  }

  @Test
  public void disabled() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController uiController) {
        trafficPlugin.setVisibility(false);
        assertFalse(trafficPlugin.isVisible());
      }
    });
  }

  @Test
  public void sourceAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getSource(TrafficData.SOURCE_ID) != null);
      }
    });
  }

  @Test
  public void motorWayBaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(MotorWay.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void motorWayCaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(MotorWay.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void trunkBaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Trunk.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void trunkCaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Trunk.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void secondaryBaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Secondary.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void secondaryCaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Secondary.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void primaryBaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void primaryCaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void localBaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Local.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void localCaseLayerAdded() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Local.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void localBaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Local.BASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void localCaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Local.CASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void secondaryBaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Secondary.BASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void secondaryCaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Secondary.CASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void primaryBaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.BASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void primaryCaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.CASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void trunkBaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.BASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void trunkCaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.CASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void motorWayBaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(MotorWay.BASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void motorWayCaseLayerVisible() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(MotorWay.CASE_LAYER_ID).getVisibility().getValue().equals(Property.VISIBLE));
      }
    });
  }

  @Test
  public void localBaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Local.BASE_LAYER_ID).getMinZoom() == Local.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void localCaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Local.CASE_LAYER_ID).getMinZoom() == Local.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void secondaryBaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Secondary.BASE_LAYER_ID).getMinZoom() == Secondary.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void secondaryCaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Secondary.CASE_LAYER_ID).getMinZoom() == Secondary.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void primaryBaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.BASE_LAYER_ID).getMinZoom() == Primary.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void primaryCaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Primary.CASE_LAYER_ID).getMinZoom() == Primary.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void trunkBaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Trunk.BASE_LAYER_ID).getMinZoom() == Trunk.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void trunkCaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(Trunk.CASE_LAYER_ID).getMinZoom() == Trunk.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void motorWayBaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(MotorWay.BASE_LAYER_ID).getMinZoom() == MotorWay.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void motorWayCaseLayerMinZoom() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertTrue(mapboxMap.getLayer(MotorWay.CASE_LAYER_ID).getMinZoom() == MotorWay.ZOOM_LEVEL);
      }
    });
  }

  @Test
  public void toggleHidesLocalBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Local.BASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesLocalCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Local.CASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesSecondaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Secondary.BASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesSecondaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Secondary.CASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesPrimaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Primary.BASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesPrimaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Primary.CASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }


  @Test
  public void toggleHidesTrunkBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Trunk.BASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesTrunkCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(Trunk.CASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesMotorWayBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(MotorWay.BASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void toggleHidesMotorWayCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        trafficPlugin.setVisibility(false);
        assertTrue(mapboxMap.getLayer(MotorWay.CASE_LAYER_ID).getVisibility().getValue().equals(Property.NONE));
      }
    });
  }

  @Test
  public void reattachLocalBaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Local.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachLocalCaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Local.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachSecondaryBaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Secondary.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachSecondaryCaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Secondary.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachPrimaryBaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Primary.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachPrimaryCaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Primary.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachTrunkBaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Trunk.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachTrunkCaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(Trunk.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachMotorWayBaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(MotorWay.BASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void reattachMotorWayCaseWithLoadNewStyle() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        mapboxMap.setStyleUrl(Style.DARK);
        controller.loopMainThreadForAtLeast(500);
        assertTrue(mapboxMap.getLayer(MotorWay.CASE_LAYER_ID) != null);
      }
    });
  }

  @Test
  public void lineCapSecondaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Secondary.BASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapSecondaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Secondary.CASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapLocalBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Local.BASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapLocalCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Local.CASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapPrimaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Primary.BASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapPrimaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Primary.CASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapTrunkBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Trunk.BASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineCapTrunkCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Trunk.CASE_LAYER_ID)).getLineCap().getValue());
      }
    });
  }

  @Test
  public void lineJoinLocalBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Local.BASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinLocalCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Local.CASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinSecondaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Secondary.BASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinSecondaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Secondary.CASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinPrimaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Primary.BASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinPrimaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Primary.CASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinTrunkBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Trunk.BASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinTrunkCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(Trunk.CASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinMotorWayBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(MotorWay.BASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineJoinMotorWayCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        assertEquals(ROUND, ((LineLayer) mapboxMap.getLayerAs(MotorWay.CASE_LAYER_ID)).getLineJoin().getValue());
      }
    });
  }

  @Test
  public void lineWidthFunctionLocalBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Local.BASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionLocalCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Local.CASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }


  @Test
  public void lineWidthFunctionSecondaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Secondary.BASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionSecondaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Secondary.CASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionPrimaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Primary.BASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionPrimaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Primary.CASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionTrunkBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Trunk.BASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionTrunkCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Trunk.CASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionMotorwayBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(MotorWay.BASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineWidthFunctionMotorwayCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(MotorWay.CASE_LAYER_ID);
        assertNotNull(layer.getLineWidth());
        assertNotNull(layer.getLineWidth().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionLocalBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Local.BASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionLocalCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Local.CASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }


  @Test
  public void lineOffsetFunctionSecondaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Secondary.BASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionSecondaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Secondary.CASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionPrimaryBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Primary.BASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionPrimaryCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Primary.CASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionTrunkBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Trunk.BASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionTrunkCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(Trunk.CASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionMotorwayBaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(MotorWay.BASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  @Test
  public void lineOffsetFunctionMotorwayCaseLayer() throws Exception {
    executeTrafficTest(new TrafficPluginAction.OnPerformTrafficAction() {
      @Override
      public void onTrafficAction(TrafficPlugin trafficPlugin, MapboxMap mapboxMap, UiController controller) {
        LineLayer layer = mapboxMap.getLayerAs(MotorWay.CASE_LAYER_ID);
        assertNotNull(layer.getLineOffset());
        assertNotNull(layer.getLineOffset().getExpression());
      }
    });
  }

  // TODO add getFilter test https://github.com/mapbox/mapbox-gl-native/pull/9077

  @After
  public void afterTest() {
    Timber.e("@After: unregister idle resource");
    Espresso.unregisterIdlingResources(idlingResource);
  }

  public void executeTrafficTest(TrafficPluginAction.OnPerformTrafficAction listener) {
    onView(withId(android.R.id.content)).perform(new TrafficPluginAction(mapboxMap, trafficPlugin, listener));
  }
}
