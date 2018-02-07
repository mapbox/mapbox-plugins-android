package com.mapbox.mapboxsdk.plugins.locationlayer;

import android.content.Context;
import android.content.res.TypedArray;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationLayerOptionsTest {

  @Mock
  private Context context;
  @Mock
  private TypedArray array;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    when(context.obtainStyledAttributes(R.style.LocationLayer, R.styleable.LocationLayer))
      .thenReturn(array);
    when(array.getResourceId(R.styleable.LocationLayer_foregroundDrawable, -1))
      .thenReturn(R.drawable.mapbox_user_icon);
  }

  @Test
  public void sanity() throws Exception {
    LocationLayerOptions locationLayerOptions = LocationLayerOptions.builder(context)
      .accuracyAlpha(0.5f)
      .build();
    assertNotNull(locationLayerOptions);
  }

  @Test
  public void passingOutOfRangeAccuracyAlpha_throwsException() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Location layer accuracy alpha value must be between 0.0 and "
      + "1.0.");
    LocationLayerOptions.builder(context)
      .accuracyAlpha(2f)
      .build();
  }

  @Test
  public void negativeElevation_causesExceptionToBeThrown() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Invalid shadow size -500.0. Must be >= 0");
    LocationLayerOptions.builder(context)
      .elevation(-500)
      .build();
  }
}