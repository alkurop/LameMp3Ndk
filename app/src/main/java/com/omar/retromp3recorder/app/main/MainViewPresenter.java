package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.mvi.Interactor;
import com.omar.retromp3recorder.app.mvi.Presenter;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;

import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.*;

public class MainViewPresenter implements Presenter<Action, MainViewModel> {

    private final Interactor<Action, Result> interactor;

    @Inject
    public MainViewPresenter(MainViewInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public ObservableTransformer<Action, MainViewModel> process() {
        return upstream -> upstream
                .compose(interactor.process())
                .scan(getDefaultViewModel(), getMapper());
    }

    //region mapper
    private BiFunction<MainViewModel, Result, MainViewModel> getMapper() {
        return (oldState, result) -> {
            if (result instanceof MessageLogResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        ((MessageLogResult) result).message,
                        null,
                        null,
                        null);
            }
            if (result instanceof ErrorLogResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        ((ErrorLogResult) result).error,
                        null,
                        null);
            }
            if (result instanceof BitrateChangedResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        ((BitrateChangedResult) result).bitRate,
                        null,
                        null,
                        null,
                        null);
            }
            if (result instanceof SampleRateChangeResult) {
                return new MainViewModel(
                        oldState.state,
                        ((SampleRateChangeResult) result).sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        null,
                        null);
            }
            if (result instanceof StateChangedResult) {
                return new MainViewModel(
                        ((MainView.StateChangedResult) result).state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        null,
                        null);
            }
            if (result instanceof RequestPermissionsResult) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        ((RequestPermissionsResult) result).permissionsToRequest,
                        null);
            }
            if (result instanceof  SetPlayerId) {
                return new MainViewModel(
                        oldState.state,
                        oldState.sampleRate,
                        oldState.bitRate,
                        null,
                        null,
                        null,
                        ((SetPlayerId) result).playerId);
            }
            throw new IllegalStateException("Unable to map results");
        };
    }
    //endregion

    private MainViewModel getDefaultViewModel() {
        return new MainViewModel(
                MainView.State.Idle,
                VoiceRecorder.SampleRate._44100,
                VoiceRecorder.BitRate._320,
                null,
                null,
                null,
                null);
    }
}
