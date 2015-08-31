package com.omar.retromp3recorder.app.test;

import android.support.v7.appcompat.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


/**
 * Created by omar on 8/29/15.
 */


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class UnitTestBase {
    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);  }

    @Test
    public void emptyTest() throws Exception {

    }

}
