package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.mvi.Interactor;
import com.omar.retromp3recorder.app.mvi.Presenter;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;

import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.*;

public class MainViewPresenter implements Presenter<MainViewAction, MainViewResult, MainViewModel> {

    private final Interactor<MainViewAction, MainViewResult> interactor;

    @Inject
    public MainViewPresenter(MainViewInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public ObservableTransformer<MainViewAction, MainViewModel> process() {
        return upstream -> upstream
                .compose(interactor.process())
                .scan(getDefaultViewModel(), getMapper());
    }

    private BiFunction<MainViewModel, MainViewResult, MainViewModel> getMapper() {
        return (oldState, result) -> {

            if (result instanceof MessageLogResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        ((MessageLogResult) result).message,
                        null,
                        null
                );
            }
            if (result instanceof ErrorLogResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        ((ErrorLogResult) result).error,
                        null
                );
            }
            if (result instanceof BitrateChangedResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        ((BitrateChangedResult) result).bitRate,
                        null,
                        null,
                        null
                );
            }
            if (result instanceof SampleRateChangeResult) {
                return new MainViewModel(
                        oldState.state,
                        ((SampleRateChangeResult) result).sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        null
                );
            }
            if (result instanceof StateChangedResult) {
                return new MainViewModel(
                        ((MainView.StateChangedResult) result).state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        null
                );
            }
            if (result instanceof RequestPermissionsResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        ((RequestPermissionsResult) result).permissionsToRequest
                );
            }
            throw new IllegalStateException("Unable to map results");
        };

    }

    private MainViewModel getDefaultViewModel() {
        return new MainViewModel(
                MainView.State.Idle,
                VoiceRecorder.SampleRate._44100,
                VoiceRecorder.BitRate._320,
                null,
                null,
                null
        );
    }
}
