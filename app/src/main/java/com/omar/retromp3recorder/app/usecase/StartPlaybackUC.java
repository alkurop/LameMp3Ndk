package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.AudioPlayer;
import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class StartPlaybackUC {

    private static final Set<String> playbackPermissions = createHashSet(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    private final FileNameRepo fileNameRepo;
    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;
    private final StateRepo stateRepo;
    private final StopPlaybackAndRecordUC stopUC;
    private final CheckPermissionsUC checkPermissionsUC;
    private final RequestPermissionsRepo requestPermissionsRepo;

    public StartPlaybackUC(
            FileNameRepo fileNameRepo,
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder,
            StateRepo stateRepo,
            StopPlaybackAndRecordUC stopUC,
            CheckPermissionsUC checkPermissionsUC,
            RequestPermissionsRepo requestPermissionsRepo
    ) {
        this.fileNameRepo = fileNameRepo;
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
        this.stateRepo = stateRepo;
        this.stopUC = stopUC;
        this.checkPermissionsUC = checkPermissionsUC;
        this.requestPermissionsRepo = requestPermissionsRepo;
    }

    public Completable execute() {
        Observable<RequestPermissionsRepo.ShouldRequestPermissions> shouldRequestPermissionsObservable =
                requestPermissionsRepo.observe()
                        .take(1)
                        .share();

        Completable beg = shouldRequestPermissionsObservable
                .ofType(RequestPermissionsRepo.Yes.class)
                .flatMapCompletable(answer -> Completable.complete());

        Completable execute = shouldRequestPermissionsObservable
                .ofType(RequestPermissionsRepo.No.class)
                .flatMap(answer ->
                        fileNameRepo.observe()
                )
                .take(1)
                .flatMapCompletable(fileName -> Completable
                        .fromAction(() -> {
                            voiceRecorder.stopRecord();
                            audioPlayer.playerStart(fileName);
                            stateRepo.newValue(MainView.State.Playing);
                        })
                );

        return stopUC.execute()
                .andThen(
                        Completable.merge(createLinkedList(
                                beg,
                                execute
                        ))
                )
                .andThen(checkPermissionsUC.execute(playbackPermissions));
    }
}
