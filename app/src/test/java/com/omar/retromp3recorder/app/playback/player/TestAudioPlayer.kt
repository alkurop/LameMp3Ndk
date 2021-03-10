package com.omar.retromp3recorder.app.playback.player

import com.omar.retromp3recorder.app.di.MockModule
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

open class TestAudioPlayer @Inject internal constructor(
    @param:Named(MockModule.PLAYER_SUBJECT) private val audioBus: Subject<AudioPlayer.Event>
) : AudioPlayer {

    private val state = BehaviorSubject.createDefault(AudioPlayer.State.Idle)

    override fun playerStop() {
        state.onNext(AudioPlayer.State.Idle)
    }
    override fun playerStart(voiceURL: String) {
        state.onNext(AudioPlayer.State.Playing)
    }
    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return audioBus
    }

    override val isPlaying: Boolean
        get() = true

    override fun observeState(): Observable<AudioPlayer.State> = state

}
