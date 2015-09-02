package com.omar.retromp3recorder.app.views;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.UnitTestBase;
import com.omar.retromp3recorder.app.presenters.MainPresenter;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;


@SuppressLint("NewApi")
public class MainActivityTest extends UnitTestBase {
    private MainActivity spyMainActivity;
    private MainActivity activity;

    @Mock
    private MainPresenter mckMainPresenter;


    @Before
    public void setupMainAct() {
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        spyMainActivity = Mockito.spy(activity);
        spyMainActivity.SetUI();
        Mockito.when(spyMainActivity.getMainPresenter()).thenReturn(mckMainPresenter);
        spyMainActivity.setMainPresenter();

    }


    @Test
    public void PlayClickedTest() throws Exception {
        //act
        (spyMainActivity.findViewById(R.id.iv_play)).performClick();
        //verify
        Mockito.verify(mckMainPresenter).PlayClicked();

    }

    @Test
    public void RecordClickedTest() throws Exception {
        //act
        (spyMainActivity.findViewById(R.id.iv_record)).performClick();
        //verify
        Mockito.verify(mckMainPresenter).RecordClicked();

    }



    @Test
    public void RecordClickedTestSimple() throws Exception {
        MainActivity mckActivity = Mockito.mock(MainActivity.class);
        View v = Mockito.mock(View.class);
        int id = R.id.iv_record;


        //init
        Mockito.when(mckActivity.getMainPresenter()).thenReturn(mckMainPresenter);
        Mockito.doCallRealMethod().when(mckActivity).onClick(Mockito.any(View.class));
        Mockito.doCallRealMethod().when(mckActivity).setMainPresenter();
        Mockito.when(v.getId()).thenReturn(id);
        //act
        mckActivity.setMainPresenter();
        mckActivity.onClick(v);

        //verify
        Mockito.verify(mckMainPresenter).RecordClicked();

    }

    @Test
    public void SetRecordBtnImgTest() throws Exception {
        //init
        int drawableId = R.drawable.abc_ab_share_pack_mtrl_alpha;
        //act
        spyMainActivity.SetRecordBtnImg(drawableId);
        Drawable drawable = spyMainActivity.getDrawable(drawableId);
        //verify
        Assert.assertEquals(drawable, ((ImageView) spyMainActivity.findViewById(R.id.iv_record)).getDrawable());
    }


    @Test
    public void OnStopTest() throws Exception {
        //act
        spyMainActivity.onStop();
        //verify
        Mockito.verify(mckMainPresenter).StopAll();
    }


}