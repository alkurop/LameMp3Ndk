package com.omar.retromp3recorder.app.common.repo

import com.omar.retromp3recorder.app.main.MainView
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.share.SharingModule
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateRepo2 @Inject constructor(
    audioPlayer: AudioPlayer,
    recorder: VoiceRecorder,
    sharingModule: SharingModule
) {
    private val stateSubject = BehaviorSubject
        .createDefault(MainView.State.Idle)

    fun newValue(state: MainView.State) {
        stateSubject.onNext(state)
    }

    fun observe(): Observable<MainView.State> {
        return stateSubject
    }
}
