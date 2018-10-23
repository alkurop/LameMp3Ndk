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
        @Nullable final Integer playerId;

        MainViewModel(
                State state,
                SampleRate sampleRate,
                BitRate bitRate,
                String message,
                String error,
                Set<String> requestForPermissions,
                Integer playerId) {
            this.state = state;
            this.sampleRate = sampleRate;
            this.bitRate = bitRate;
            this.message = message;
            this.error = error;
            this.requestForPermissions = requestForPermissions;
            this.playerId = playerId;
        }
    }
    //endregion

    //region Action

    interface Action { }

    final class SampleRateChangeAction implements Action {
        final SampleRate sampleRate;

        public SampleRateChangeAction(SampleRate sampleRate) {
            this.sampleRate = sampleRate;
        }
    }

    final class BitRateChangeAction implements Action {
        final BitRate bitRate;

        public BitRateChangeAction(BitRate bitRate) {
            this.bitRate = bitRate;
        }
    }

     final class PlayAction implements Action { }

     final class RecordAction implements Action { }

     final class ShareAction implements Action { }

     final class StopAction implements Action { }

    //endregion

    //region result

    interface Result { }

    final class MessageLogResult implements Result {
        final String message;

        MessageLogResult(String message) {
            this.message = message;
        }
    }

    final class ErrorLogResult implements Result {
        final String error;

        ErrorLogResult(String error) {
            this.error = error;
        }
    }

    final class BitrateChangedResult implements Result {
        final BitRate bitRate;

        BitrateChangedResult(BitRate bitRate) {
            this.bitRate = bitRate;
        }
    }

    final class SampleRateChangeResult implements Result {
        final SampleRate sampleRate;

          SampleRateChangeResult(SampleRate sampleRate) {
            this.sampleRate = sampleRate;
        }
    }

    final class StateChangedResult implements Result {
        final State state;

        StateChangedResult(State state) {
            this.state = state;
        }
    }

    final class RequestPermissionsResult implements Result {
        final Set<String> permissionsToRequest;

        RequestPermissionsResult(Set<String> permissionsToRequest) {
            this.permissionsToRequest = permissionsToRequest;
        }
    }

    final class PlayerIdResult implements Result {
        public final int playerId;

        public PlayerIdResult(int playerId) {
            this.playerId = playerId;
        }
    }
    //endregion
}

