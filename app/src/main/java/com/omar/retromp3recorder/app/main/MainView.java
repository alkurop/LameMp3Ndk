package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.mvi.MviView;

import java.util.Set;

import static com.omar.retromp3recorder.app.recorder.VoiceRecorder.*;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface MainView extends MviView<MainView.MainViewModel> {

    enum State {Recording, Playing, Idle}

    //region ViewModel
    final class MainViewModel {
        @NonNull final  SampleRate sampleRate;
        @NonNull final State state;
        @NonNull final  BitRate bitRate;
        @Nullable final String message;
        @Nullable final String error;
        @Nullable final Set<String> requestForPermissions;

        MainViewModel(
                State state,
                SampleRate sampleRate,
                BitRate bitRate,
                String message,
                String error,
                Set<String> requestForPermissions
        ) {
            this.state = state;
            this.sampleRate = sampleRate;
            this.bitRate = bitRate;
            this.message = message;
            this.error = error;
            this.requestForPermissions = requestForPermissions;
        }
    }
    //endregion

    //region Action

    interface MainViewAction { }

    final class SampleRateChangeAction implements MainViewAction {
        final SampleRate sampleRate;

        public SampleRateChangeAction(SampleRate sampleRate) {
            this.sampleRate = sampleRate;
        }
    }

    final class BitRateChangeAction implements MainViewAction {
        final BitRate bitRate;

        public BitRateChangeAction(BitRate bitRate) {
            this.bitRate = bitRate;
        }
    }

     final class PlayAction implements MainViewAction { }

     final class RecordAction implements MainViewAction { }

     final class ShareAction implements MainViewAction { }

     final class StopAction implements MainViewAction { }

    //endregion

    //Region result

    interface MainViewResult { }

    final class MessageLogResult implements MainViewResult {
        final String message;

        MessageLogResult(String message) {
            this.message = message;
        }
    }

    final class ErrorLogResult implements MainViewResult {
        final String error;

        ErrorLogResult(String error) {
            this.error = error;
        }
    }

    final class BitrateChangedResult implements MainViewResult {
        final BitRate bitRate;

        BitrateChangedResult(BitRate bitRate) {
            this.bitRate = bitRate;
        }
    }

    final class SampleRateChangeResult implements MainViewResult {
        final SampleRate sampleRate;

          SampleRateChangeResult(SampleRate sampleRate) {
            this.sampleRate = sampleRate;
        }
    }

    final class StateChangedResult implements MainViewResult {
        final State state;

        public StateChangedResult(State state) {
            this.state = state;
        }
    }

    final class RequestPermissionsResult implements MainViewResult {
        final Set<String> permissionsToRequest;

        public RequestPermissionsResult(Set<String> permissionsToRequest) {
            this.permissionsToRequest = permissionsToRequest;
        }
    }
    //endregion
}

