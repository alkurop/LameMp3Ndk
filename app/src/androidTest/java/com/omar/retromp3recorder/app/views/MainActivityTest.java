package com.omar.retromp3recorder.app.views;

import com.omar.retromp3recorder.app.test.UnitTestBase;

import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

public class MainActivityTest extends UnitTestBase {




@Test
    public void testSetUI() throws Exception {

      MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get();
      MainActivity spy = Mockito.spy(activity);

    }


}