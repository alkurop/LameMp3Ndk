package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.di.MviView;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface MainView extends MviView<MainView.MainViewModel> {

    final class MainViewModel {
        @NonNull final SampleRate sampleRate;
        @NonNull final BitRate bitRate;
        @Nullable final String message;
        @Nullable final String error;

        public MainViewModel(SampleRate sampleRate, BitRate bitRate, String message, String error) {
            this.sampleRate = sampleRate;
            this.bitRate = bitRate;
            this.message = message;
            this.error = error;
        }
    }

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

    final class ShareAction implements MainViewAction { }

    final class StopAction implements MainViewAction { }


    enum SampleRate {_44100, _22050, _11052, _8000}

    enum BitRate {_320, _192, _160, _128}

    interface MainViewResult {
    }

    final class MessageLogResult implements MainViewResult {
        final String message;

        public MessageLogResult(String message) {
            this.message = message;
        }
    }

    final class ErrorLogResult implements MainViewResult {
        final String error;

        public ErrorLogResult(String error) {
            this.error = error;
        }
    }

    final class BitrateChangedResult implements MainViewResult {
        final BitRate bitRate;

        public BitrateChangedResult(BitRate bitRate) {
            this.bitRate = bitRate;
        }
    }

    final class SampleRateChangeResult implements MainViewResult {
        final SampleRate sampleRate;

        public SampleRateChangeResult(SampleRate sampleRate) {
            this.sampleRate = sampleRate;
        }
    }
}

