package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.utils.OneShot;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;

public class StartPlaybackUCImpl implements StartPlaybackUC {

    private static final Set<String> playbackPermissions = createHashSet(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    private final FileNameRepo fileNameRepo;
    private final RequestPermissionsRepo requestPermissionsRepo;

    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;

    private final StopPlaybackAndRecordUC stopUC;
    private final CheckPermissionsUC checkPermissionsUC;

    //region constructor
    @Inject
    public StartPlaybackUCImpl(
            FileNameRepo fileNameRepo,
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder,
            StopPlaybackAndRecordUC stopUC,
            CheckPermissionsUC checkPermissionsUC,
            RequestPermissionsRepo requestPermissionsRepo
    ) {
        this.fileNameRepo = fileNameRepo;
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
        this.stopUC = stopUC;
        this.checkPermissionsUC = checkPermissionsUC;
        this.requestPermissionsRepo = requestPermissionsRepo;
    }
    //endregion

    public Completable execute() {

        Completable begForPermissions = Completable.complete();

        Completable execute =
                fileNameRepo.observe()
                        .take(1)
                        .flatMapCompletable(fileName -> Completable
                                .fromAction(() -> {
                                    voiceRecorder.stopRecord();
                                    audioPlayer.playerStart(fileName);
                                })
                        );

        Completable merge =
                checkPermissionsUC.execute(playbackPermissions)
                        .andThen(requestPermissionsRepo.observe()
                                .take(1)
                                .share())
                        .map(OneShot::checkValue)
                        .flatMapCompletable(shouldAskPermissions ->
                                (shouldAskPermissions instanceof RequestPermissionsRepo.Granted)
                                        ? execute
                                        : begForPermissions
                        );

        return stopUC.execute()
                .andThen(merge);
    }
}
