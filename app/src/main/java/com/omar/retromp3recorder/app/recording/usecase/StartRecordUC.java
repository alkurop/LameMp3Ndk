package com.omar.retromp3recorder.app.recording.usecase;

import com.omar.retromp3recorder.app.recording.recorder.FilePathGenerator;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo;
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.shared.repo.FileNameRepo;
import com.omar.retromp3recorder.app.shared.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.shared.usecase.StopPlaybackAndRecordUC;
import com.omar.retromp3recorder.app.utils.OneShot;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function3;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;

public class StartRecordUC {
    private static final Set<String> voiceRecordPermissions = createHashSet(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
    );

    private final FileNameRepo fileNameRepo;
    private final BitRateRepo bitRateRepo;
    private final SampleRateRepo sampleRateRepo;
    private final VoiceRecorder voiceRecorder;
    private final StopPlaybackAndRecordUC stopPlaybackAndRecordUC;
    private final CheckPermissionsUC checkPermissionsUC;
    private final RequestPermissionsRepo requestPermissionsRepo;
    private final FilePathGenerator filePathGenerator;

    //region constructor
    @Inject
    public StartRecordUC(
            FileNameRepo fileNameRepo,
            BitRateRepo bitRateRepo,
            SampleRateRepo sampleRateRepo,
            VoiceRecorder voiceRecorder,
            StopPlaybackAndRecordUC stopPlaybackAndRecordUC,
            CheckPermissionsUC checkPermissionsUC,
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


        final Function3<String,
                VoiceRecorder.BitRate,
                VoiceRecorder.SampleRate,
                VoiceRecorder.RecorderProps> propsZipper = VoiceRecorder.RecorderProps::new;

        Completable begForPermissions = Completable.complete();

        Completable execute = Completable
                .fromAction(() -> {
                    String filePath = filePathGenerator.generateFilePath();
                    fileNameRepo.newValue(filePath);
                })
                .andThen(Observable
                        .zip(
                                fileNameRepo.observe().take(1),
                                bitRateRepo.observe().take(1),
                                sampleRateRepo.observe().take(1),
                                propsZipper
                        ))
                .flatMapCompletable(props -> Completable
                        .fromAction(() ->
                                voiceRecorder.record(props)
                        )
                );


        Completable merge =
                checkPermissionsUC.execute(voiceRecordPermissions)
                        .andThen(requestPermissionsRepo.observe()
                                .take(1)
                                .share())
                        .map(OneShot::checkValue)
                        .flatMapCompletable(shouldAskPermissions ->
                                (shouldAskPermissions instanceof RequestPermissionsRepo.Granted)
                                        ? execute
                                        : begForPermissions
                        );

        return stopPlaybackAndRecordUC
                .execute()
                .andThen(merge);
    }
}
