package com.omar.retromp3recorder.app.views;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.UnitTestBase;
import com.omar.retromp3recorder.app.presenters.MainPresenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

public class MainActivityTest extends UnitTestBase {

    @Mock
    MainPresenter mckMainPresenter;
    MainActivity spyMainActivity;
    MainActivity activity;

    @Before
    public void setupMainAct() {
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        spyMainActivity = Mockito.spy(activity);
        spyMainActivity.SetUI();
    }


    @Test
    public void PlayClickedTest() throws Exception {
        //init
        Mockito.when(spyMainActivity.getMainPresenter()).thenReturn(mckMainPresenter);
        spyMainActivity.setMainPresenter();
        //act
        (spyMainActivity.findViewById(R.id.iv_play)).performClick();

        //verify
        Mockito.verify(mckMainPresenter).PlayClicked();

    }


}