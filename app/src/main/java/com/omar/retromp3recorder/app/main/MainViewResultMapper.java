package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;

import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.ErrorLogResult;
import static com.omar.retromp3recorder.app.main.MainView.MainViewModel;
import static com.omar.retromp3recorder.app.main.MainView.MessageLogResult;
import static com.omar.retromp3recorder.app.main.MainView.RequestPermissionsResult;
import static com.omar.retromp3recorder.app.main.MainView.Result;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeResult;
import static com.omar.retromp3recorder.app.main.MainView.SetPlayerId;
import static com.omar.retromp3recorder.app.main.MainView.StateChangedResult;

public class MainViewResultMapper   {

    public static ObservableTransformer<Result, MainViewModel> map() {
        return upstream -> upstream
                .scan(getDefaultViewModel(), getMapper());
    }

    //region mapper
    private static BiFunction<MainViewModel, Result, MainViewModel> getMapper() {
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
            throw new IllegalStateException("Unable to map result" + result.getClass().getCanonicalName());
        };
    }
    //endregion

    private static MainViewModel getDefaultViewModel() {
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
