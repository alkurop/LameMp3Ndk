package com.omar.retromp3recorder.app.playback.usecase;

import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo;
import com.omar.retromp3recorder.app.playback.player.AudioPlayer;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC;
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.common.usecase.StopPlaybackAndRecordUC;
import com.omar.retromp3recorder.app.utils.OneShot;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Completable;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;

public class StartPlaybackUC {
    private static final Set<String> playbackPermissions = createHashSet(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    private final CurrentFileRepo currentFileRepo;
    private final RequestPermissionsRepo requestPermissionsRepo;

    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;

    private final StopPlaybackAndRecordUC stopUC;
    private final CheckPermissionsUC checkPermissionsUC;

    //region constructor
    @Inject
    public StartPlaybackUC(
            CurrentFileRepo currentFileRepo,
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder,
            StopPlaybackAndRecordUC stopUC,
            CheckPermissionsUC checkPermissionsUC,
            RequestPermissionsRepo requestPermissionsRepo
    ) {
        this.currentFileRepo = currentFileRepo;
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
                currentFileRepo.observe()
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
