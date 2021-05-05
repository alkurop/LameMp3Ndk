package com.omar.retromp3recorder.app.modules

import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
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
        audioBus.onNext(AudioPlayer.Event.PlayerId(PLAYER_ID))
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return audioBus
    }

    override val isPlaying: Boolean
        get() = true

    override fun observeState(): Observable<AudioPlayer.State> = state

    companion object {
        const val PLAYER_ID = 11
    }
}
