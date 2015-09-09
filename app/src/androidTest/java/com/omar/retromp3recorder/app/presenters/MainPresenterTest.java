package com.omar.retromp3recorder.app.presenters;

import com.omar.retromp3recorder.app.UnitTestBase;
import com.omar.retromp3recorder.app.controllers.AudioStatesController;
import com.omar.retromp3recorder.app.controllers.StateSelector;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.views.MainActivity;
import org.junit.Test;
import org.mockito.Spy;

import static org.mockito.Mockito.*;



/**
 * Created by omar on 8/29/15.
 */

public class MainPresenterTest extends UnitTestBase {


    @Spy
    MainPresenter spyMainPresenter = new MainPresenter();
    @Test
    public void testInit() throws Exception {

    }
    @Test
    public void testRecordClicked() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector =  mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.recordClicked();

        //assert
        verify(mckStateSelector, times(1)).recordingClicked();
        verifyNoMoreInteractions(mckStateSelector);
    }
    @Test
    public void testPlayClicked() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.playClicked();

        //assert
        verify(mckStateSelector,times(1)).playCLicked();
        verifyNoMoreInteractions(mckStateSelector);


    }
    @Test
    public void testShareClicked() throws Exception {

        StateSelector mckStateSelector = mock(StateSelector.class);
        SharingModule mckSharingModule = mock(SharingModule.class);
        AudioStatesController mckAudioStatesController = mock(AudioStatesController.class);
        final String filePath = "filaPath";

        when(spyMainPresenter.getSharingModule()).thenReturn(mckSharingModule);
        when(mckAudioStatesController.getFilePath()).thenReturn(filePath);
        //init
        spyMainPresenter.setStateSelector(mckStateSelector);
        spyMainPresenter.setAudioController(mckAudioStatesController);
        //act
        spyMainPresenter.shareClicked();

        //assert
        verify(mckStateSelector,times(1)).stopAll();
        verify(mckSharingModule,times(1)).startShading(eq(filePath), any(MainPresenter.class));
        verify(spyMainPresenter).getSharingModule();

    }
    @Test
    public void testStopAll() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.stopAll();

        //assert
        verify(mckStateSelector,times(1)).stopAll();
        verifyNoMoreInteractions(mckStateSelector);
    }
    @Test
    public void testSetText() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        MainActivity mainView = mock(MainActivity.class);
        String someText = "sometext";


        //act
        mainPresenter.setView(mainView);
        mainPresenter.setText(someText);

        //assert
        verify(mainView,times(1)).setLabelText(eq(someText));
        verifyNoMoreInteractions(mainView);
    }


}