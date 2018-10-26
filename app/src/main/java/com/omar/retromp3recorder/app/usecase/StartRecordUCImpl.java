package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.recorder.FilePathGenerator;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.utils.OneShot;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function3;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class StartRecordUCImpl implements StartRecordUC {
    private static final Set<String> voiceRecordPermissions = createHashSet(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
    );

    private final FileNameRepo fileNameRepo;
    private final BitRateRepo bitRateRepo;
    private final SampleRateRepo sampleRateRepo;
    private final VoiceRecorder voiceRecorder;
    private final StopPlaybackAndRecordUCImpl stopPlaybackAndRecordUC;
    private final CheckPermissionsUCImpl checkPermissionsUC;
    private final RequestPermissionsRepo requestPermissionsRepo;
    private final FilePathGenerator filePathGenerator;

    //region constructor
    @Inject
    public StartRecordUCImpl(
            FileNameRepo fileNameRepo,
            BitRateRepo bitRateRepo,
            SampleRateRepo sampleRateRepo,
            VoiceRecorder voiceRecorder,
            StopPlaybackAndRecordUCImpl stopPlaybackAndRecordUC,
            CheckPermissionsUCImpl checkPermissionsUC,
            RequestPermissionsRepo requestPermissionsRepo,
            FilePathGenerator filePathGenerator) {
        this.fileNameRepo = fileNameRepo;
        this.bitRateRepo = bitRateRepo;
        this.sampleRateRepo = sampleRateRepo;
        this.voiceRecorder = voiceRecorder;
        this.stopPlaybackAndRecordUC = stopPlaybackAndRecordUC;
        this.checkPermissionsUC = checkPermissionsUC;
        this.requestPermissionsRepo = requestPermissionsRepo;
        this.filePathGenerator = filePathGenerator;
    }
    //endregion

    public Completable execute() {
        Observable<RequestPermissionsRepo.ShouldRequestPermissions> share =
                checkPermissionsUC.execute(voiceRecordPermissions)
                        .andThen(requestPermissionsRepo.observe()
                                .take(1)
                                .share())
                        .map(OneShot::checkValue);

        final Function3<String,
                VoiceRecorder.BitRate,
                VoiceRecorder.SampleRate,
                VoiceRecorder.RecorderProps> propsZipper = VoiceRecorder.RecorderProps::new;

        Completable begForPermissions = share
                .ofType(RequestPermissionsRepo.Yes.class)
                .flatMapCompletable(answer -> Completable.complete());

        Completable execute = share
                .ofType(RequestPermissionsRepo.No.class)
                .flatMapCompletable(answer -> Completable.fromAction(() -> {
                    String filePath = filePathGenerator.generateFilePath();
                    fileNameRepo.newValue(filePath);
                }))
                .andThen(Observable
                        .zip(
                                fileNameRepo.observe().take(1),
                                bitRateRepo.observe().take(1),
                                sampleRateRepo.observe().take(1),
                                propsZipper
                        ))
                .flatMapCompletable(props -> Completable
                        .fromAction(() -> {
                            voiceRecorder.record(props);
                        })
                );

        return stopPlaybackAndRecordUC
                .execute()
                .andThen(Completable
                        .merge(createLinkedList(
                                execute,
                                begForPermissions
                        ))
                );
    }
}
