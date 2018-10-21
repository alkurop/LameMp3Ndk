package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function3;

public class StartRecordUC {

    private final FileNameRepo fileNameRepo;
    private final StateRepo stateRepo;
    private final BitRateRepo bitRateRepo;
    private final SampleRateRepo sampleRateRepo;
    private final VoiceRecorder voiceRecorder;
    private final StopPlaybackAndRecordUC stopPlaybackAndRecordUC;

    public StartRecordUC(
            FileNameRepo fileNameRepo,
            StateRepo stateRepo,
            BitRateRepo bitRateRepo,
            SampleRateRepo sampleRateRepo,
            VoiceRecorder voiceRecorder,
            StopPlaybackAndRecordUC stopPlaybackAndRecordUC
    ) {
        this.fileNameRepo = fileNameRepo;
        this.stateRepo = stateRepo;
        this.bitRateRepo = bitRateRepo;
        this.sampleRateRepo = sampleRateRepo;
        this.voiceRecorder = voiceRecorder;
        this.stopPlaybackAndRecordUC = stopPlaybackAndRecordUC;
    }

    public Completable execute() {
        final Function3<String,
                VoiceRecorder.BitRate,
                VoiceRecorder.SampleRate,
                VoiceRecorder.RecorderProps> propsZipper = VoiceRecorder.RecorderProps::new;

        return stopPlaybackAndRecordUC
                .execute()
                .andThen(Observable
                        .zip(
                                fileNameRepo.observe().take(1),
                                bitRateRepo.observe().take(1),
                                sampleRateRepo.observe().take(1),
                                propsZipper
                        )
                )
                .flatMapCompletable(props -> Completable
                        .fromAction(() -> {
                            voiceRecorder.record(props);
                            stateRepo.newValue(MainView.State.Recording);
                        })
                );
    }
}
