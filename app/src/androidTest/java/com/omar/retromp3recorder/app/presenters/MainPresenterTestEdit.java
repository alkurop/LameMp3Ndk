package com.omar.retromp3recorder.app.presenters;

import android.widget.LinearLayout;
import com.omar.retromp3recorder.app.UnitTestBase;
import com.omar.retromp3recorder.app.controllers.AudioStatesController;
import com.omar.retromp3recorder.app.controllers.StateSelector;
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

        doCallRealMethod().when(mckMainPresenter).init(any(IMainView.class));
        doCallRealMethod().when(mckMainPresenter).setUpAudioController();
        doCallRealMethod().when(mckMainPresenter).setUpStatesSelector();
        doCallRealMethod().when(mckMainPresenter).displayView();

        doCallRealMethod().when(mckMainPresenter).getAudioController();

        doCallRealMethod().when(mckMainPresenter).getStateSelector();
        doCallRealMethod().when(mckMainPresenter).getView();
        doCallRealMethod().when(mckMainPresenter).getContext();
        doCallRealMethod().when(mckMainPresenter).getContext();

        //act
        mckMainPresenter.init(mainView);

        //assert
        assertNotNull(mckMainPresenter.getAudioController());

        assertNotNull(mckMainPresenter.getStateSelector());
        assertNotNull(mckMainPresenter.getView());
        assertNotNull(mckMainPresenter.getContext());

        verify(mckMainPresenter, times(1)).displayView();
        verify(mckMainPresenter, times(1)).setUpSampleRateRadioGroup(any(LinearLayout.class));
        verify(mckMainPresenter, times(1)).setUpBitRateRadioGroup(any(LinearLayout.class));
        verify(mainView, times(1)).setUI();



    }

    @Test
    public void testRecordClicked() throws Exception {

        //init
        mainPresenter.setStateSelector(mckStateSelector);

        //act
        mainPresenter.recordClicked();

        //assert
        verify(mckStateSelector, timeout(1)).recordingClicked();
        verifyNoMoreInteractions(mckStateSelector);
    }

    @Test
    public void testPlayClicked() throws Exception {
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.playClicked();

        //assert
        verify(mckStateSelector, times(1)).playCLicked();
        verifyNoMoreInteractions(mckStateSelector);


    }

    @Test
    public void testShareClicked() throws Exception {
        final String filePath = "filaPath";

        doCallRealMethod().when(mckMainPresenter).shareClicked();

        doCallRealMethod().when(mckMainPresenter).setStateSelector(any(StateSelector.class));
        doCallRealMethod().when(mckMainPresenter).setAudioController(any(AudioStatesController.class));
        when(mckMainPresenter.getSharingModule()).thenReturn(mckSharingModule);
        when(mckAudioStatesController.getFilePath()).thenReturn(filePath);
        //init
        mckMainPresenter.setStateSelector(mckStateSelector);
        mckMainPresenter.setAudioController(mckAudioStatesController);
        //act
        mckMainPresenter.shareClicked();

        //assert
        verify(mckStateSelector, times(1)).stopAll();
        verify(mckSharingModule, times(1)).startShading(eq(filePath), any(MainPresenter.class));
    }

    @Test
    public void testStopAll() throws Exception {
        //init
        mainPresenter.setStateSelector(mckStateSelector);

        //act
        mainPresenter.stopAll();

        //assert
        verify(mckStateSelector, times(1)).stopAll();
        verifyNoMoreInteractions(mckStateSelector);
    }

    @Test
    public void testSetText() throws Exception {
        String someText = "sometext";

        //act
        mainPresenter.setView(mainView);
        mainPresenter.setText(someText);

        //assert
        verify(mainView, times(1)).setLabelText(eq(someText));
        verifyNoMoreInteractions(mainView);
    }


}