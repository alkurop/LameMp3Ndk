package com.omar.retromp3recorder.app.recording.recorder;

import com.omar.retromp3recorder.app.common.repo.LogRepo;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_A;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public final class LoggingVoiceRecorder implements VoiceRecorder {
    private final VoiceRecorder recorder;

    @Inject
    LoggingVoiceRecorder(
            @Named(DECORATOR_A) VoiceRecorder recorder,
            LogRepo logRepo,
            Scheduler scheduler) {
        this.recorder = recorder;

        Observable<Event> share = recorder.observeEvents().share();

        Observable<LogRepo.Event> message = share.ofType(Message.class)
                .map(answer -> new LogRepo.Event.Message(answer.message));
        Observable<LogRepo.Event> error = share.ofType(Error.class)
                .map(answer -> new LogRepo.Event.Error(answer.error));

        Observable
                .merge(createLinkedList(
                        message, error
                ))
                .flatMapCompletable(event ->
                        Completable.fromAction(() -> logRepo.newValue(event)))
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

    @Override
    public boolean isRecording() {
        return recorder.isRecording();
    }
}
