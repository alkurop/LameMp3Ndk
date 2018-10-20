package com.omar.retromp3recorder.app.di;

import io.reactivex.Observable;

public interface AudioPlayer {
    void playerStop();

    void playerStart(String voiceURL);

    Observable<Event> observeEvents();

    interface Event {
    }

    class Message implements Event {
        public final String message;

        public Message(String errorMessage) {
            this.message = errorMessage;
        }
    }

    class Error extends Message {
        public Error(String errorMessage) {
            super(errorMessage);
        }
    }

    class SendPlayerId implements Event {
        public final int playerId;

        public SendPlayerId(int playerId) {
            this.playerId = playerId;
        }
    }

    class PlaybackEnded implements Event {
    }
}
