package com.omar.retromp3recorder.app.playback.player

import io.reactivex.Observable

interface AudioPlayer {
    fun playerStop()
    fun playerStart(voiceURL: String)
    fun observeEvents(): Observable<Event>
    val isPlaying: Boolean

    //region events
    sealed class Event {
        data class Message(val message: String) : Event()
        data class Error(val error: String) : Event()
        data class SendPlayerId internal constructor(val playerId: Int) : Event()
        object PlaybackEnded : Event()
    }
}