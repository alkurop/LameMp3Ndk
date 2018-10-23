package com.omar.retromp3recorder.app.player;

import io.reactivex.Observable;

public interface AudioPlayer {

    void playerStop();

    void playerStart(String voiceURL);

    Observable<Event> observeEvents();

    //region events
    interface Event { }

    final class Message implements Event {
        public final String message;

        public Message(String errorMessage) {
            this.message = errorMessage;
        }
    }

    final class Error implements Event {
        public final String error;

        public Error(String error) {
            this.error = error;
        }
    }

    final class SendPlayerId implements Event {
        final int playerId;

        SendPlayerId(int playerId) {
            this.playerId = playerId;
        }
    }

    final class PlaybackEnded implements Event { }
    //endregion
}
