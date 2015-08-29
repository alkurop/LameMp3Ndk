package com.omar.retromp3recorder.app.test;

import com.omar.retromp3recorder.app.BuildConfig;
import com.omar.retromp3recorder.app.controllers.AudioStatesController;
import com.omar.retromp3recorder.app.controllers.StateSelector;
import com.omar.retromp3recorder.app.presenters.MainPresenter;
import com.omar.retromp3recorder.app.share.IShadingModule;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.views.IMainView;
import com.omar.retromp3recorder.app.views.MainActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by omar on 8/29/15.
 */

public class MainPresenterTest extends UnitTestBase {



    @Test
    public void testInit() throws Exception {

    }
    @Test
    public void testRecordClicked() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = Mockito.mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.RecordClicked();

        //assert
        Mockito.verify(mckStateSelector,Mockito.timeout(1)).RecordingClicked();
        Mockito.verifyNoMoreInteractions(mckStateSelector);
    }
    @Test
    public void testPlayClicked() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = Mockito.mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.PlayClicked();

        //assert
        Mockito.verify(mckStateSelector,Mockito.times(1)).PlayCLicked();
        Mockito.verifyNoMoreInteractions(mckStateSelector);


    }
    @Test
    public void testShareClicked() throws Exception {

        MainPresenter mckMainPresenter = Mockito.mock(MainPresenter.class);
        StateSelector mckStateSelector = Mockito.mock(StateSelector.class);
        SharingModule mckSharingModule = Mockito.mock(SharingModule.class);
        AudioStatesController mckAudioStatesController = Mockito.mock(AudioStatesController.class);
        String filePath = "filaPath";



        Mockito.doCallRealMethod().when(mckMainPresenter).ShareClicked();

        Mockito.doCallRealMethod().when(mckMainPresenter).setStateSelector(Mockito.any(StateSelector.class));
        Mockito.doCallRealMethod().when(mckMainPresenter).setAudioController(Mockito.any(AudioStatesController.class));
        Mockito.when(mckMainPresenter.getSharingModule()).thenReturn(mckSharingModule);
        Mockito.when(mckAudioStatesController.GetFilePath()).thenReturn(filePath);
        //init
        mckMainPresenter.setStateSelector(mckStateSelector);
        mckMainPresenter.setAudioController(mckAudioStatesController);
        //act
        mckMainPresenter.ShareClicked();

        //assert
        Mockito.verify(mckStateSelector,Mockito.times(1)).StopAll();
        Mockito.verify(mckSharingModule,Mockito.times(1)).StartShading(filePath,mckMainPresenter);
    }
    @Test
    public void testStopAll() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        StateSelector mckStateSelector = Mockito.mock(StateSelector.class);
        //init
        mainPresenter.setStateSelector(mckStateSelector);


        //act
        mainPresenter.StopAll();

        //assert
        Mockito.verify(mckStateSelector,Mockito.times(1)).StopAll();
        Mockito.verifyNoMoreInteractions(mckStateSelector);
    }
    @Test
    public void testSetText() throws Exception {
        MainPresenter mainPresenter = new MainPresenter();
        MainActivity mainView = Mockito.mock(MainActivity.class);
        String someText = "sometext";


        //act
        mainPresenter.setView(mainView);
        mainPresenter.SetText(someText);

        //assert
        Mockito.verify(mainView,Mockito.times(1)).SetLabelText(someText);
        Mockito.verifyNoMoreInteractions(mainView);
    }
}