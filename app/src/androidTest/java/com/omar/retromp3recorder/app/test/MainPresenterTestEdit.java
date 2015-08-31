package com.omar.retromp3recorder.app.test;

import android.widget.LinearLayout;
import com.omar.retromp3recorder.app.controllers.AudioStatesController;
import com.omar.retromp3recorder.app.controllers.StateSelector;
import com.omar.retromp3recorder.app.presenters.MainPresenter;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.views.IMainView;
import com.omar.retromp3recorder.app.views.MainActivity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by omar on 8/29/15.
 */

public class MainPresenterTestEdit extends UnitTestBase {

    @Mock
    private MainPresenter mckMainPresenter;
    @Mock
    private StateSelector mckStateSelector;
    @Mock
    private MainActivity mainView;
    @Mock
    private SharingModule mckSharingModule;
    @Mock
    private AudioStatesController mckAudioStatesController;


    private MainPresenter mainPresenter;

    @Before
    public void initVars() {
        mainPresenter = new MainPresenter();
    }

    @Test
    public void testInit() throws Exception {
        //init

        doCallRealMethod().when(mckMainPresenter).Init(any(IMainView.class));
        doCallRealMethod().when(mckMainPresenter).setUpAudioController();
        doCallRealMethod().when(mckMainPresenter).setUpStatesSelector();
        doCallRealMethod().when(mckMainPresenter).DisplayView();

        doCallRealMethod().when(mckMainPresenter).getAudioController();

        doCallRealMethod().when(mckMainPresenter).getStateSelector();
        doCallRealMethod().when(mckMainPresenter).getView();
        doCallRealMethod().when(mckMainPresenter).getContext();
        doCallRealMethod().when(mckMainPresenter).getContext();

        //act
        mckMainPresenter.Init(mainView);

        //assert
        assertNotNull(mckMainPresenter.getAudioController());

        assertNotNull(mckMainPresenter.getStateSelector());
        assertNotNull(mckMainPresenter.getView());
        assertNotNull(mckMainPresenter.getContext());

        verify(mckMainPresenter, times(1)).DisplayView();
        verify(mckMainPresenter, times(1)).setUpSampleRateRadioGroup(any(LinearLayout.class));
        verify(mckMainPresenter, times(1)).setUpBitRateRadioGroup(any(LinearLayout.class));
        verify(mainView, times(1)).SetUI();



    }

    @Test
    public void testRecordClicked() throws Exception {

        //init
        mainPresenter.setStateSelector(mckStateSelector);

        //act
        mainPresenter.RecordClicked();

        //assert
        verify(mckStateSelector, timeout(1)).RecordingClicked();
        verifyNoMoreInteractions(mckStateSelector);
    }

    @Test
    public void testPlayClicked() throws Exception {
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.PlayClicked();

        //assert
        verify(mckStateSelector, times(1)).PlayCLicked();
        verifyNoMoreInteractions(mckStateSelector);


    }

    @Test
    public void testShareClicked() throws Exception {
        final String filePath = "filaPath";

        doCallRealMethod().when(mckMainPresenter).ShareClicked();

        doCallRealMethod().when(mckMainPresenter).setStateSelector(any(StateSelector.class));
        doCallRealMethod().when(mckMainPresenter).setAudioController(any(AudioStatesController.class));
        when(mckMainPresenter.getSharingModule()).thenReturn(mckSharingModule);
        when(mckAudioStatesController.GetFilePath()).thenReturn(filePath);
        //init
        mckMainPresenter.setStateSelector(mckStateSelector);
        mckMainPresenter.setAudioController(mckAudioStatesController);
        //act
        mckMainPresenter.ShareClicked();

        //assert
        verify(mckStateSelector, times(1)).StopAll();
        verify(mckSharingModule, times(1)).StartShading(eq(filePath), any(MainPresenter.class));
    }

    @Test
    public void testStopAll() throws Exception {
        //init
        mainPresenter.setStateSelector(mckStateSelector);

        //act
        mainPresenter.StopAll();

        //assert
        verify(mckStateSelector, times(1)).StopAll();
        verifyNoMoreInteractions(mckStateSelector);
    }

    @Test
    public void testSetText() throws Exception {
        String someText = "sometext";

        //act
        mainPresenter.setView(mainView);
        mainPresenter.SetText(someText);

        //assert
        verify(mainView, times(1)).SetLabelText(eq(someText));
        verifyNoMoreInteractions(mainView);
    }


}