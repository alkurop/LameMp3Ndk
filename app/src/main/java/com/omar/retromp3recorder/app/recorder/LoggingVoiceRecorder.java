package com.omar.retromp3recorder.app.recorder;

import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.logger.LogRepo;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class LoggingVoiceRecorder implements VoiceRecorder {
    private final VoiceRecorder recorder;

    public LoggingVoiceRecorder(VoiceRecorder recorder, LogRepo logRepo, Scheduler scheduler) {
        this.recorder = recorder;

        Observable<Event> share = recorder.observeEvents().share();
        Observable<LogRepo.Event> message = share.ofType(Message.class)
                .map(answer -> new LogRepo.Message(answer.message));
        Observable<LogRepo.Event> error = share.ofType(Error.class)
                .map(answer -> new LogRepo.Message(answer.error));

        Observable
                .merge(createLinkedList(
                        message, error
                ))
                .subscribeOn(scheduler)
                .subscribe();
    }

    @Override
    public Observable<Event> observeEvents() {
        return recorder.observeEvents();
    }

    @Override
    public void record(RecorderProps props) {
        recorder.record(props);
    }

    @Override
    public void stopRecord() {
        recorder.stopRecord();
    }
}
