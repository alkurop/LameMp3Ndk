package com.omar.retromp3recorder.app.playback.player

import com.omar.retromp3recorder.app.di.MockModule
import io.reactivex.Observable
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

open class TestAudioPlayer @Inject internal constructor(
    @param:Named(MockModule.PLAYER_SUBJECT) private val audioBus: Subject<AudioPlayer.Event>
) :
    AudioPlayer {
    override fun playerStop() {}
    override fun playerStart(voiceURL: String) {}
    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return audioBus
    }

    override val isPlaying: Boolean
        get() = true

}
