package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class StartRecordUC {
    private static final Set<String> recordPermissions = createHashSet(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
    );

    private final FileNameRepo fileNameRepo;
    private final StateRepo stateRepo;
    private final BitRateRepo bitRateRepo;
    private final SampleRateRepo sampleRateRepo;
    private final VoiceRecorder voiceRecorder;
    private final StopPlaybackAndRecordUC stopPlaybackAndRecordUC;
    private final CheckPermissionsUC checkPermissionsUC;
    private final RequestPermissionsRepo requestPermissionsRepo;

    public StartRecordUC(
            FileNameRepo fileNameRepo,
            StateRepo stateRepo,
            BitRateRepo bitRateRepo,
            SampleRateRepo sampleRateRepo,
            VoiceRecorder voiceRecorder,
            StopPlaybackAndRecordUC stopPlaybackAndRecordUC,
            CheckPermissionsUC checkPermissionsUC, RequestPermissionsRepo requestPermissionsRepo) {
        this.fileNameRepo = fileNameRepo;
        this.stateRepo = stateRepo;
        this.bitRateRepo = bitRateRepo;
        this.sampleRateRepo = sampleRateRepo;
        this.voiceRecorder = voiceRecorder;
        this.stopPlaybackAndRecordUC = stopPlaybackAndRecordUC;
        this.checkPermissionsUC = checkPermissionsUC;
        this.requestPermissionsRepo = requestPermissionsRepo;
    }

    public Completable execute() {
        Observable<RequestPermissionsRepo.ShouldRequestPermissions> share = requestPermissionsRepo.observe().take(1).share();
        final Function3<String,
                VoiceRecorder.BitRate,
                VoiceRecorder.SampleRate,
                VoiceRecorder.RecorderProps> propsZipper = VoiceRecorder.RecorderProps::new;


        Completable beg = share
                .ofType(RequestPermissionsRepo.Yes.class)
                .flatMapCompletable(answer -> Completable.complete());

        Completable execute = share
                .ofType(RequestPermissionsRepo.No.class)
                .flatMap(answer -> (Observable
                        .zip(
                                fileNameRepo.observe().take(1),
                                bitRateRepo.observe().take(1),
                                sampleRateRepo.observe().take(1),
                                propsZipper
                        )))
                .flatMapCompletable(props -> Completable
                        .fromAction(() -> {
                            voiceRecorder.record(props);
                            stateRepo.newValue(MainView.State.Recording);
                        })
                );


        return stopPlaybackAndRecordUC
                .execute()
                .andThen(Completable
                        .merge(createLinkedList(
                                execute,
                                beg
                        ))
                )
                .andThen(checkPermissionsUC.execute(recordPermissions));
    }
}
