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
        mainPresenter.RecordClicked();

        //assert
        verify(mckStateSelector, times(1)).RecordingClicked();
        verifyNoMoreInteractions(mckStateSelector);
    }
    @Test
    public void testPlayClicked() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.PlayClicked();

        //assert
        verify(mckStateSelector,times(1)).PlayCLicked();
        verifyNoMoreInteractions(mckStateSelector);


    }
    @Test
    public void testShareClicked() throws Exception {

        StateSelector mckStateSelector = mock(StateSelector.class);
        SharingModule mckSharingModule = mock(SharingModule.class);
        AudioStatesController mckAudioStatesController = mock(AudioStatesController.class);
        final String filePath = "filaPath";

        when(spyMainPresenter.getSharingModule()).thenReturn(mckSharingModule);
        when(mckAudioStatesController.GetFilePath()).thenReturn(filePath);
        //init
        spyMainPresenter.setStateSelector(mckStateSelector);
        spyMainPresenter.setAudioController(mckAudioStatesController);
        //act
        spyMainPresenter.ShareClicked();

        //assert
        verify(mckStateSelector,times(1)).StopAll();
        verify(mckSharingModule,times(1)).StartShading(eq(filePath), any(MainPresenter.class));
        verify(spyMainPresenter).getSharingModule();

    }
    @Test
    public void testStopAll() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.StopAll();

        //assert
        verify(mckStateSelector,times(1)).StopAll();
        verifyNoMoreInteractions(mckStateSelector);
    }
    @Test
    public void testSetText() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        MainActivity mainView = mock(MainActivity.class);
        String someText = "sometext";


        //act
        mainPresenter.setView(mainView);
        mainPresenter.SetText(someText);

        //assert
        verify(mainView,times(1)).SetLabelText(eq(someText));
        verifyNoMoreInteractions(mainView);
    }


}