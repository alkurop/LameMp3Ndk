package com.omar.retromp3recorder.app.recorder;

import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.StateRepo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_BETA;

@Singleton
public class StateLoggingVoiceRecorder implements VoiceRecorder {

    private final VoiceRecorder voiceRecorder;

    @Inject
    public StateLoggingVoiceRecorder(
            @Named(DECORATOR_BETA) VoiceRecorder voiceRecorder,
            Scheduler scheduler,
            StateRepo stateRepo
    ) {
        this.voiceRecorder = voiceRecorder;

        voiceRecorder.observeEvents()
                .ofType(VoiceRecorder.Error.class)
                .map(error -> MainView.State.Idle)

                .flatMapCompletable(state -> Completable.fromAction(() ->
                        stateRepo.newValue(state)
                ))
                .subscribeOn(scheduler)
                .subscribe();
    }

    @Override
    public Observable<Event> observeEvents() {
        return voiceRecorder.observeEvents();
    }

    @Override
    public void record(RecorderProps props) {
        voiceRecorder.record(props);
    }

    @Override
    public void stopRecord() {
        voiceRecorder.stopRecord();
    }
}
