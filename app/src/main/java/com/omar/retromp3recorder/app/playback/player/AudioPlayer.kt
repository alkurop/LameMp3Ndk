package com.omar.retromp3recorder.app.playback.player

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.Observable

interface AudioPlayer {
    fun playerStop()
    fun playerStart(voiceURL: String)
    fun observeEvents(): Observable<Event>
    val isPlaying: Boolean

    //region events
    sealed class Event {
        data class Message(val message: Stringer) : Event()
        data class Error(val error: Stringer) : Event()
        data class PlayerId internal constructor(val playerId: Int) : Event()
    }
}