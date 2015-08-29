package com.omar.retromp3recorder.app.test;

import com.omar.retromp3recorder.app.BuildConfig;
import  org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by omar on 8/29/15.
 */


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class UnitTestBase {
    @Test
    public void emptyTest() throws Exception {

    }

}
